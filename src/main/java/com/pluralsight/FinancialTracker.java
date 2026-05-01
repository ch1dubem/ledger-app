package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

// Personal finance tracker that reads/writes transactions to a CSV file
public class FinancialTracker {

    // Holds all transactions loaded from file and added during runtime
    private static final ArrayList<Transaction> transactions = new ArrayList<>();
    private static final String FILE_NAME = "transactions.csv";

    // Patterns used for parsing and formatting dates and times
    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final String TIME_PATTERN = "HH:mm:ss";
    private static final String DATETIME_PATTERN = DATE_PATTERN + " " + TIME_PATTERN;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern(DATE_PATTERN);
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern(TIME_PATTERN);
    private static final DateTimeFormatter DATETIME_FMT = DateTimeFormatter.ofPattern(DATETIME_PATTERN);

    // Console colors
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String PURPLE = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";
    private static final String BOLD = "\u001B[1m";

    // ==================== MAIN MENU ====================

    public static void main(String[] args) {
        // Pull in any existing transactions from the file
        loadTransactions(FILE_NAME);

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        // Keep showing the menu until the user exits
        while (running) {
            System.out.println(BOLD + CYAN + "\n===== Welcome to TransactionApp =====" + RESET);
            System.out.println("Choose an option:");
            System.out.println(YELLOW + "D)" + RESET + " Add Deposit");
            System.out.println(YELLOW + "P)" + RESET + " Make Payment (Debit)");
            System.out.println(YELLOW + "L)" + RESET + " Ledger");
            System.out.println(RED + "X)" + RESET + " Exit");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "D" -> addDeposit(scanner);
                case "P" -> addPayment(scanner);
                case "L" -> ledgerMenu(scanner);
                case "X" -> {
                    System.out.println(GREEN + "Goodbye!" + RESET);
                    running = false;
                }
                default -> System.out.println(RED + "Invalid option" + RESET);
            }
        }
        scanner.close();
    }

    // ==================== FILE I/O ====================

    // Reads the CSV file line by line, splits on pipe, and builds Transaction objects
    // Creates an empty file if one doesn't exist yet
    public static void loadTransactions(String fileName) {
        File file = new File(fileName);
        try {
            if (!file.exists()) {
                file.createNewFile();
                return;
            }

            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split("\\|");
                LocalDate transactionDate = LocalDate.parse(parts[0], DATE_FMT);
                LocalTime transactionTime = LocalTime.parse(parts[1], TIME_FMT);
                String transactionDescription = parts[2];
                String vendor = parts[3];
                double amount = Double.parseDouble(parts[4]);
                transactions.add(new Transaction(transactionDate, transactionTime, transactionDescription, vendor, amount));
            }
            bufferedReader.close();

        } catch (IOException e) {
            System.out.println(RED + "Error loading transactions." + RESET);
        }
    }

    // ==================== ADD DEPOSIT ====================

    // Walks the user through entering deposit info with validation on every field
    // Saves to both the in-memory list and the CSV file
    private static void addDeposit(Scanner scanner) {
        LocalDate date = null;
        LocalTime time = null;
        String description = null;
        String vendor = null;
        double amount = 0;

        System.out.println(BOLD + GREEN + "\n--- Add Deposit ---" + RESET);

        // Keep prompting until we get a valid date and time
        while (date == null || time == null) {
            System.out.print(CYAN + "Enter date and time (yyyy-MM-dd HH:mm:ss): " + RESET);
            String dateTime = scanner.nextLine().trim();
            try {
                String[] parts = dateTime.split(" ");
                date = LocalDate.parse(parts[0], DATE_FMT);
                time = LocalTime.parse(parts[1], TIME_FMT);
            } catch (Exception e) {
                System.out.println(RED + "Invalid format. Please use yyyy-MM-dd HH:mm:ss" + RESET);
            }
        }

        // Don't allow blank descriptions
        while (description == null || description.isEmpty()) {
            System.out.print(CYAN + "Enter Description: " + RESET);
            description = scanner.nextLine().trim();
            if (description.isEmpty()) {
                System.out.println(RED + "Description cannot be empty." + RESET);
            }
        }

        // Don't allow blank vendor names
        while (vendor == null || vendor.isEmpty()) {
            System.out.print(CYAN + "Enter vendor: " + RESET);
            vendor = scanner.nextLine().trim();
            if (vendor.isEmpty()) {
                System.out.println(RED + "Vendor cannot be empty." + RESET);
            }
        }

        // Amount has to be a positive number — deposits can't be zero or negative
        boolean validAmount = false;
        while (!validAmount) {
            System.out.print(CYAN + "Enter amount to deposit: " + RESET);
            String amountStr = scanner.nextLine().trim();
            try {
                amount = Double.parseDouble(amountStr);
                if (amount <= 0) {
                    System.out.println(YELLOW + "Amount must be positive." + RESET);
                } else {
                    validAmount = true;
                }
            } catch (Exception e) {
                System.out.println(RED + "Invalid amount. Please enter a number." + RESET);
            }
        }

        // Add to the list and write to file
        transactions.add(new Transaction(date, time, description, vendor, amount));

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME, true));
            bw.write(date.format(DATE_FMT) + "|" + time.format(TIME_FMT) + "|" + description + "|" + vendor + "|" + amount);
            bw.newLine();
            bw.close();
            System.out.println(GREEN + "✓ Deposit Processed" + RESET);
        } catch (IOException e) {
            System.out.println(RED + "Error processing deposit." + RESET);
        }
    }

    // ==================== ADD PAYMENT ====================

    // Same flow as addDeposit but flips the amount to negative before saving
    private static void addPayment(Scanner scanner) {
        LocalDate date = null;
        LocalTime time = null;
        String description = null;
        String vendor = null;
        double amount = 0;

        System.out.println(BOLD + RED + "\n--- Make Payment ---" + RESET);

        while (date == null || time == null) {
            System.out.print(CYAN + "Enter date and time (yyyy-MM-dd HH:mm:ss): " + RESET);
            String dateTime = scanner.nextLine().trim();
            try {
                String[] parts = dateTime.split(" ");
                date = LocalDate.parse(parts[0], DATE_FMT);
                time = LocalTime.parse(parts[1], TIME_FMT);
            } catch (Exception e) {
                System.out.println(RED + "Invalid format. Please use yyyy-MM-dd HH:mm:ss" + RESET);
            }
        }

        while (description == null || description.isEmpty()) {
            System.out.print(CYAN + "Enter Description: " + RESET);
            description = scanner.nextLine().trim();
            if (description.isEmpty()) {
                System.out.println(RED + "Description cannot be empty." + RESET);
            }
        }

        while (vendor == null || vendor.isEmpty()) {
            System.out.print(CYAN + "Enter vendor: " + RESET);
            vendor = scanner.nextLine().trim();
            if (vendor.isEmpty()) {
                System.out.println(RED + "Vendor cannot be empty." + RESET);
            }
        }

        // User enters a positive number, we flip it to negative since it's a payment
        boolean validAmount = false;
        while (!validAmount) {
            System.out.print(CYAN + "Enter amount to pay: " + RESET);
            String amountStr = scanner.nextLine().trim();
            try {
                amount = Double.parseDouble(amountStr);
                if (amount <= 0) {
                    System.out.println(YELLOW + "Amount must be positive." + RESET);
                } else {
                    validAmount = true;
                }
            } catch (Exception e) {
                System.out.println(RED + "Invalid amount. Please enter a number." + RESET);
            }
        }

        // Payments are stored as negative values in the file
        amount = -amount;

        transactions.add(new Transaction(date, time, description, vendor, amount));

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME, true));
            bw.write(date.format(DATE_FMT) + "|" + time.format(TIME_FMT) + "|" + description + "|" + vendor + "|" + amount);
            bw.newLine();
            bw.close();
            System.out.println(GREEN + "✓ Payment Processed" + RESET);
        } catch (IOException e) {
            System.out.println(RED + "Error processing payment." + RESET);
        }
    }

    // ==================== LEDGER MENU ====================

    // Sorts transactions before showing options so everything is in order
    private static void ledgerMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            // Sort every time so newly added entries show up in the right spot
            sortTransactions();

            System.out.println(BOLD + BLUE + "\n===== Ledger =====" + RESET);
            System.out.println("Choose an option:");
            System.out.println(YELLOW + "A)" + RESET + " All");
            System.out.println(YELLOW + "D)" + RESET + " Deposits");
            System.out.println(YELLOW + "P)" + RESET + " Payments");
            System.out.println(YELLOW + "R)" + RESET + " Reports");
            System.out.println(RED + "H)" + RESET + " Home");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "A" -> displayLedger();
                case "D" -> displayDeposits();
                case "P" -> displayPayments();
                case "R" -> reportsMenu(scanner);
                case "H" -> running = false;
                default -> System.out.println(RED + "Invalid option" + RESET);
            }
        }
    }

    // ==================== DISPLAY METHODS ====================

    // Shows every transaction
    private static void displayLedger() {
        System.out.println(BOLD + "\nAll Transactions:" + RESET);
        printTransactionTable(transactions);
    }

    // Pulls out only positive amounts (deposits) and displays them
    private static void displayDeposits() {
        System.out.println(BOLD + GREEN + "\nAll Deposits:" + RESET);
        ArrayList<Transaction> deposits = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() > 0) {
                deposits.add(transaction);
            }
        }
        printTransactionTable(deposits);
    }

    // Pulls out only negative amounts (payments) and displays them
    private static void displayPayments() {
        System.out.println(BOLD + RED + "\nAll Payments:" + RESET);
        ArrayList<Transaction> payments = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() < 0) {
                payments.add(transaction);
            }
        }
        printTransactionTable(payments);
    }

    // ==================== REPORTS MENU ====================

    private static void reportsMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println(BOLD + PURPLE + "\n===== Reports =====" + RESET);
            System.out.println("Choose an option:");
            System.out.println(YELLOW + "1)" + RESET + " Month To Date");
            System.out.println(YELLOW + "2)" + RESET + " Previous Month");
            System.out.println(YELLOW + "3)" + RESET + " Year To Date");
            System.out.println(YELLOW + "4)" + RESET + " Previous Year");
            System.out.println(YELLOW + "5)" + RESET + " Search by Vendor");
            System.out.println(YELLOW + "6)" + RESET + " Custom Search");
            System.out.println(RED + "0)" + RESET + " Back");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1" -> {
                    System.out.println(BOLD + "\nMonth To Date Report:" + RESET);
                    filterTransactionsByDate(LocalDate.now().withDayOfMonth(1), LocalDate.now());
                }
                case "2" -> {
                    System.out.println(BOLD + "\nPrevious Month Report:" + RESET);
                    LocalDate firstDayOfLastMonth = LocalDate.now().minusMonths(1).withDayOfMonth(1);
                    LocalDate lastDayOfLastMonth = firstDayOfLastMonth.withDayOfMonth(firstDayOfLastMonth.lengthOfMonth());
                    filterTransactionsByDate(firstDayOfLastMonth, lastDayOfLastMonth);
                }
                case "3" -> {
                    System.out.println(BOLD + "\nYear To Date Report:" + RESET);
                    filterTransactionsByDate(LocalDate.now().withDayOfYear(1), LocalDate.now());
                }
                case "4" -> {
                    System.out.println(BOLD + "\nPrevious Year Report:" + RESET);
                    LocalDate firstOfLastYear = LocalDate.now().minusYears(1).withDayOfYear(1);
                    LocalDate lastOfLastYear = firstOfLastYear.withDayOfYear(firstOfLastYear.lengthOfYear());
                    filterTransactionsByDate(firstOfLastYear, lastOfLastYear);
                }
                case "5" -> {
                    System.out.print(CYAN + "Enter vendor name: " + RESET);
                    String vendor = scanner.nextLine().trim();
                    if (vendor.isEmpty()) {
                        System.out.println(RED + "Vendor cannot be empty." + RESET);
                    } else {
                        System.out.println(BOLD + "\nVendor Report for \"" + vendor + "\":" + RESET);
                        filterTransactionsByVendor(vendor);
                    }
                }
                case "6" -> customSearch(scanner);
                case "0" -> running = false;
                default -> System.out.println(RED + "Invalid option" + RESET);
            }
        }
    }

    // ==================== FILTER / SEARCH METHODS ====================

    // Finds all transactions between the start and end dates, inclusive on both ends
    private static void filterTransactionsByDate(LocalDate start, LocalDate end) {
        ArrayList<Transaction> dateResults = new ArrayList<>();
        for (Transaction transaction : transactions) {
            LocalDate date = transaction.getDate();
            boolean onOrAfterStart = date.isEqual(start) || date.isAfter(start);
            boolean onOrBeforeEnd = date.isEqual(end) || date.isBefore(end);

            if (onOrAfterStart && onOrBeforeEnd) {
                dateResults.add(transaction);
            }
        }
        printTransactionTable(dateResults);
    }

    // Finds all transactions for a specific vendor, ignoring case
    private static void filterTransactionsByVendor(String vendor) {
        ArrayList<Transaction> vendorResults = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getVendor().equalsIgnoreCase(vendor)) {
                vendorResults.add(transaction);
            }
        }
        printTransactionTable(vendorResults);
    }

    // Lets the user search by any combination of fields — blank means skip that filter
    private static void customSearch(Scanner scanner) {
        System.out.println(BOLD + PURPLE + "\n--- Custom Search ---" + RESET);

        System.out.print(CYAN + "Start date (yyyy-MM-dd or blank): " + RESET);
        String startStr = scanner.nextLine().trim();

        System.out.print(CYAN + "End date (yyyy-MM-dd or blank): " + RESET);
        String endStr = scanner.nextLine().trim();

        System.out.print(CYAN + "Description (or blank): " + RESET);
        String description = scanner.nextLine().trim();

        System.out.print(CYAN + "Vendor (or blank): " + RESET);
        String vendor = scanner.nextLine().trim();

        System.out.print(CYAN + "Amount (or blank): " + RESET);
        String amountStr = scanner.nextLine().trim();

        // null means the user didn't enter anything for that field
        LocalDate start = parseDate(startStr);
        LocalDate end = parseDate(endStr);
        Double amount = parseDouble(amountStr);

        ArrayList<Transaction> results = new ArrayList<>();
        for (Transaction transaction : transactions) {
            // Each check only runs if the user actually entered a value for that field
            if (start != null && transaction.getDate().isBefore(start)) continue;
            if (end != null && transaction.getDate().isAfter(end)) continue;
            if (!description.isEmpty() && !transaction.getTransactionDescription().toLowerCase().contains(description.toLowerCase())) continue;
            if (!vendor.isEmpty() && !transaction.getVendor().equalsIgnoreCase(vendor)) continue;
            if (amount != null && transaction.getAmount() != amount) continue;

            results.add(transaction);
        }

        // Tell the user what they searched for if nothing came back
        if (results.isEmpty()) {
            String message = YELLOW + "No transactions found";

            if (!vendor.isEmpty()) {
                message += " for vendor \"" + vendor + "\"";
            }
            if (!description.isEmpty()) {
                message += " with description \"" + description + "\"";
            }
            if (start != null) {
                message += " from " + start.format(DATE_FMT);
            }
            if (end != null) {
                message += " to " + end.format(DATE_FMT);
            }
            if (amount != null) {
                message += " with amount " + amount;
            }

            System.out.println(message + "." + RESET);
            return;
        }

        printTransactionTable(results);
    }

    // ==================== UTILITY METHODS ====================

    // Tries to parse a date string — returns null if blank or badly formatted
    private static LocalDate parseDate(String s) {
        if (s == null || s.isEmpty()) return null;
        try {
            return LocalDate.parse(s.trim(), DATE_FMT);
        } catch (Exception e) {
            System.out.println(YELLOW + "Invalid date format, skipping this filter." + RESET);
            return null;
        }
    }

    // Tries to parse a number — returns null if blank or not a number
    private static Double parseDouble(String s) {
        if (s == null || s.isEmpty()) return null;
        try {
            return Double.parseDouble(s.trim());
        } catch (Exception e) {
            System.out.println(YELLOW + "Invalid amount format, skipping this filter." + RESET);
            return null;
        }
    }

    // Prints any list of transactions in a clean table with headers
    private static void printTransactionTable(ArrayList<Transaction> list) {
        System.out.println(CYAN + "-".repeat(75) + RESET);
        System.out.printf(BOLD + "%-12s %-10s %-25s %-15s %10s%n" + RESET, "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println(CYAN + "-".repeat(75) + RESET);

        if (list.isEmpty()) {
            System.out.println(YELLOW + "No transactions found." + RESET);
            return;
        }

        for (Transaction t : list) {
            // Green for deposits, red for payments
            String color = t.getAmount() > 0 ? GREEN : RED;

            System.out.printf("%-12s %-10s %-25s %-15s " + color + "%10.2f" + RESET + "%n",
                    t.getDate().format(DATE_FMT),
                    t.getTime().format(TIME_FMT),
                    t.getTransactionDescription(),
                    t.getVendor(),
                    t.getAmount());
        }
        System.out.println(CYAN + "-".repeat(75) + RESET);
    }

    // Bubble sort — puts newest transactions first by comparing date then time
    private static void sortTransactions() {
        for (int i = 0; i < transactions.size() - 1; i++) {
            for (int j = i + 1; j < transactions.size(); j++) {
                Transaction a = transactions.get(i);
                Transaction b = transactions.get(j);

                // Check date first
                int compare = b.getDate().compareTo(a.getDate());

                // Same date? Check time instead
                if (compare == 0) {
                    compare = b.getTime().compareTo(a.getTime());
                }

                // If b should come before a, swap them
                if (compare > 0) {
                    transactions.set(i, b);
                    transactions.set(j, a);
                }
            }
        }
    }
}