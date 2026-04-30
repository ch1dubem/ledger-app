package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * Capstone skeleton – personal finance tracker.
 * ------------------------------------------------
 * File format  (pipe-delimited)
 *     yyyy-MM-dd|HH:mm:ss|description|vendor|amount
 * A deposit has a positive amount; a payment is stored
 * as a negative amount.
 */
public class FinancialTracker {

    /* ------------------------------------------------------------------
       Shared data and formatters
       ------------------------------------------------------------------ */
    private static final ArrayList<Transaction> transactions = new ArrayList<>();
    private static final String FILE_NAME = "transactions.csv";

    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final String TIME_PATTERN = "HH:mm:ss";
    private static final String DATETIME_PATTERN = DATE_PATTERN + " " + TIME_PATTERN;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern(DATE_PATTERN);
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern(TIME_PATTERN);
    private static final DateTimeFormatter DATETIME_FMT = DateTimeFormatter.ofPattern(DATETIME_PATTERN);

    /* ------------------------------------------------------------------
       Main menu
       ------------------------------------------------------------------ */
    public static void main(String[] args) {
        loadTransactions(FILE_NAME);

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("Welcome to TransactionApp");
            System.out.println("Choose an option:");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "D" -> addDeposit(scanner);
                case "P" -> addPayment(scanner);
                case "L" -> ledgerMenu(scanner);
                case "X" -> running = false;
                default -> System.out.println("Invalid option");
            }
        }
        scanner.close();
    }

    /* ------------------------------------------------------------------
       File I/O
       ------------------------------------------------------------------ */

    /**
     * Load transactions from FILE_NAME.
     * • If the file doesn’t exist, create an empty one so that future writes succeed.
     * • Each line looks like: date|time|description|vendor|amount
     */
    public static void loadTransactions(String fileName) {
        File file = new File(fileName);
        try {
            if (!file.exists()) {
                file.createNewFile();
                return;
            }

            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
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
            System.out.println("Error loading transactions.");
        }




        // TODO: create file if it does not exist, then read each line,
        //       parse the five fields, build a Transaction object,
        //       and add it to the transactions list.
    }
    /* ------------------------------------------------------------------
       Add new transactions
       ------------------------------------------------------------------ */

    /**
     *
     * @param scanner
     */
    private static void addDeposit(Scanner scanner) {
        LocalDate date = null;
        LocalTime time = null;
        String description = null;
        String vendor = null;
        double amount = 0;

        // Date and time input
        while (date == null || time == null) {
            System.out.println("Enter date and time (yyyy-MM-dd HH:mm:ss): ");
            String dateTime = scanner.nextLine().trim();
            try {
                String[] parts = dateTime.split(" ");
                date = LocalDate.parse(parts[0], DATE_FMT);
                time = LocalTime.parse(parts[1], TIME_FMT);
            } catch (Exception e) {
                System.out.println("Invalid format. Please use yyyy-MM-dd HH:mm:ss");
            }
        }

        // Description input
        while (description == null || description.isEmpty()) {
            System.out.println("Enter Description:");
            description = scanner.nextLine().trim();
            if (description.isEmpty()) {
                System.out.println("Description cannot be empty.");
            }
        }

        // Vendor input
        while (vendor == null || vendor.isEmpty()) {
            System.out.println("Enter vendor:");
            vendor = scanner.nextLine().trim();
            if (vendor.isEmpty()) {
                System.out.println("Vendor cannot be empty.");
            }
        }

        // Amount input
        boolean validAmount = false;
        while (!validAmount) {
            System.out.println("Enter amount to deposit:");
            String amountStr = scanner.nextLine().trim();
            try {
                amount = Double.parseDouble(amountStr);
                if (amount <= 0) {
                    System.out.println("Amount must be positive.");
                } else {
                    validAmount = true;
                }
            } catch (Exception e) {
                System.out.println("Invalid amount. Please enter a number.");
            }
        }

        transactions.add(new Transaction(date, time, description, vendor, amount));

        try {
            FileWriter fileWriter = new FileWriter(FILE_NAME, true);
            BufferedWriter bw = new BufferedWriter(fileWriter);
            bw.write(date.format(DATE_FMT) + "|" + time.format(TIME_FMT) + "|" + description + "|" + vendor + "|" + amount);
            bw.newLine();
            bw.close();
            System.out.println("Deposit Processed");
        } catch (IOException e) {
            System.out.println("Error processing deposit.");
        }
    }


    private static void addPayment(Scanner scanner) {
        LocalDate date = null;
        LocalTime time = null;
        String description = null;
        String vendor = null;
        double amount = 0;

        // Date and time input
        while (date == null || time == null) {
            System.out.println("Enter date and time (yyyy-MM-dd HH:mm:ss): ");
            String dateTime = scanner.nextLine().trim();
            try {
                String[] parts = dateTime.split(" ");
                date = LocalDate.parse(parts[0], DATE_FMT);
                time = LocalTime.parse(parts[1], TIME_FMT);
            } catch (Exception e) {
                System.out.println("Invalid format. Please use yyyy-MM-dd HH:mm:ss");
            }
        }

        // Description input
        while (description == null || description.isEmpty()) {
            System.out.println("Enter Description:");
            description = scanner.nextLine().trim();
            if (description.isEmpty()) {
                System.out.println("Description cannot be empty.");
            }
        }

        // Vendor input
        while (vendor == null || vendor.isEmpty()) {
            System.out.println("Enter vendor:");
            vendor = scanner.nextLine().trim();
            if (vendor.isEmpty()) {
                System.out.println("Vendor cannot be empty.");
            }
        }

        // Amount input
        boolean validAmount = false;
        while (!validAmount) {
            System.out.println("Enter amount to pay:");
            String amountStr = scanner.nextLine().trim();
            try {
                amount = Double.parseDouble(amountStr);
                if (amount <= 0) {
                    System.out.println("Amount must be positive.");
                } else {
                    validAmount = true;
                }
            } catch (Exception e) {
                System.out.println("Invalid amount. Please enter a number.");
            }
        }

        amount = -amount; // convert to negative for payment

        transactions.add(new Transaction(date, time, description, vendor, amount));

        try {
            FileWriter fileWriter = new FileWriter(FILE_NAME, true);
            BufferedWriter bw = new BufferedWriter(fileWriter);
            bw.write(date.format(DATE_FMT) + "|" + time.format(TIME_FMT) + "|" + description + "|" + vendor + "|" + amount);
            bw.newLine();
            bw.close();
            System.out.println("Payment Processed");
        } catch (IOException e) {
            System.out.println("Error processing payment.");
        }
    }

    /* ------------------------------------------------------------------
       Ledger menu
       ------------------------------------------------------------------ */
    private static void ledgerMenu(Scanner scanner) {

        boolean running = true;
        while (running) {
            sortTransactions();
            System.out.println("Ledger");
            System.out.println("Choose an option:");
            System.out.println("A) All");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "A" -> displayLedger();
                case "D" -> displayDeposits();
                case "P" -> displayPayments();
                case "R" -> reportsMenu(scanner);
                case "H" -> running = false;
                default -> System.out.println("Invalid option");
            }
        }
    }

    /* ------------------------------------------------------------------
       Display helpers: show data in neat columns
       ------------------------------------------------------------------ */
    private static void displayLedger() {
        System.out.println("\nAll Transactions:");
        printTransactionTable(transactions);
    }

    private static void displayDeposits() {
        System.out.println("\nAll Deposits:");

        // Build a list of only deposits
        ArrayList<Transaction> deposits = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() > 0) {
                deposits.add(transaction);
            }
        }
        printTransactionTable(deposits);
    }


    private static void displayPayments() {
        System.out.println("\nAll Payments:");

        // Build a list of only payments
        ArrayList<Transaction> payments = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() < 0) {
                payments.add(transaction);
            }
        }
        printTransactionTable(payments);
    }

    /* ------------------------------------------------------------------
       Reports menu
       ------------------------------------------------------------------ */
    private static void reportsMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Reports");
            System.out.println("Choose an option:");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("6) Custom Search");
            System.out.println("0) Back");

            String input = scanner.nextLine().trim();


            switch (input) {
                case "1" -> {
                    filterTransactionsByDate(LocalDate.now().withDayOfMonth(1), LocalDate.now());
                    /* TODO – month-to-date report */}
                case "2" -> {
                    LocalDate firstDayOfLastMonth = LocalDate.now().minusMonths(1).withDayOfMonth(1);
                    LocalDate lastDayOfLastMonth = firstDayOfLastMonth.withDayOfMonth(firstDayOfLastMonth.lengthOfMonth());
                    filterTransactionsByDate(firstDayOfLastMonth, lastDayOfLastMonth);
                    /* TODO – previous month report */
                }
                case "3" -> {
                    filterTransactionsByDate(LocalDate.now().withDayOfYear(1), LocalDate.now());
                    /* TODO – year-to-date report   */
                }
                case "4" -> {
                    LocalDate firstOfLastYear = LocalDate.now().minusYears(1).withDayOfYear(1);
                    LocalDate lastOfLastYear = firstOfLastYear.withDayOfYear(firstOfLastYear.lengthOfYear());
                    filterTransactionsByDate(firstOfLastYear, lastOfLastYear);
                    /* TODO – previous year report  */
                }
                case "5" -> {
                    System.out.print("Enter vendor name: ");
                    String vendor = scanner.nextLine().trim();
                    if (vendor.isEmpty()) {
                        System.out.println("Vendor cannot be empty.");
                    } else {
                        filterTransactionsByVendor(vendor);
                    }
                }
                case "6" -> customSearch(scanner);
                case "0" -> running = false;
                default -> System.out.println("Invalid option");
            }
        }
    }

    /* ------------------------------------------------------------------
       Reporting helpers
       ------------------------------------------------------------------ */
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

    private static void filterTransactionsByVendor(String vendor) {
        // Build a list of transactions matching the vendor
        ArrayList<Transaction> vendorResults = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getVendor().equalsIgnoreCase(vendor)) {
                vendorResults.add(transaction);
            }
        }
        printTransactionTable(vendorResults);
    }

    private static void customSearch(Scanner scanner) {
        // Prompt for each field - leave blank to skip
        System.out.print("Start date (yyyy-MM-dd or blank): ");
        String startStr = scanner.nextLine().trim();

        System.out.print("End date (yyyy-MM-dd or blank): ");
        String endStr = scanner.nextLine().trim();

        System.out.print("Description (or blank): ");
        String description = scanner.nextLine().trim();

        System.out.print("Vendor (or blank): ");
        String vendor = scanner.nextLine().trim();

        System.out.print("Amount (or blank): ");
        String amountStr = scanner.nextLine().trim();

        // Parse the optional fields - null means the user skipped it
        LocalDate start = parseDate(startStr);
        LocalDate end = parseDate(endStr);
        Double amount = parseDouble(amountStr);

        // Build a list of matching transactions
        ArrayList<Transaction> results = new ArrayList<>();
        for (Transaction transaction : transactions) {
            // Skip if start date is set and transaction is before it
            if (start != null && transaction.getDate().isBefore(start)) continue;

            // Skip if end date is set and transaction is after it
            if (end != null && transaction.getDate().isAfter(end)) continue;

            // Skip if description is set and doesn't match
            if (!description.isEmpty() && !transaction.getTransactionDescription().toLowerCase().contains(description.toLowerCase())) continue;

            // Skip if vendor is set and doesn't match
            if (!vendor.isEmpty() && !transaction.getVendor().equalsIgnoreCase(vendor)) continue;

            // Skip if amount is set and doesn't match
            if (amount != null && transaction.getAmount() != amount) continue;

            // Transaction passed all filters, add to results
            results.add(transaction);
        }
        printTransactionTable(results);
    }

    /* ------------------------------------------------------------------
       Utility parsers (you can reuse in many places)
       ------------------------------------------------------------------ */
    private static LocalDate parseDate(String s) {
        if (s == null || s.isEmpty()) return null;
        try {
            return LocalDate.parse(s.trim(), DATE_FMT);
        } catch (Exception e) {
            System.out.println("Invalid date format, skipping this filter.");
            return null;
        }
    }

    private static Double parseDouble(String s) {
        if (s == null || s.isEmpty()) return null;
        try {
            return Double.parseDouble(s.trim());
        } catch (Exception e) {
            System.out.println("Invalid amount format, skipping this filter.");
            return null;
        }
    }

    private static void printTransactionTable(ArrayList<Transaction> list) {
        System.out.println("-".repeat(75));
        System.out.printf("%-12s %-10s %-25s %-15s %10s%n", "Date", "Time", "Description", "Vendor", "Amount");
        System.out.println("-".repeat(75));

        // If no transactions to show, display a message
        if (list.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }

        // Print each transaction in formatted columns
        for (Transaction t : list) {
            System.out.printf("%-12s %-10s %-25s %-15s %10.2f%n",
                    t.getDate().format(DATE_FMT),
                    t.getTime().format(TIME_FMT),
                    t.getTransactionDescription(),
                    t.getVendor(),
                    t.getAmount());
        }
    }

    private static void sortTransactions() {
        for (int i = 0; i < transactions.size() - 1; i++) {
            for (int j = i + 1; j < transactions.size(); j++) {
                Transaction a = transactions.get(i);
                Transaction b = transactions.get(j);

                // Compare dates first
                int compare = b.getDate().compareTo(a.getDate());

                // If same date, compare times
                if (compare == 0) {
                    compare = b.getTime().compareTo(a.getTime());
                }

                // If b is newer than a, swap them
                if (compare > 0) {
                    transactions.set(i, b);
                    transactions.set(j, a);
                }
            }
        }
    }
}

