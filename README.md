# Ledger Application

## Description of the Project

This is a Java console application that tracks financial transactions for personal use. Users can record deposits and payments, view their transaction history, and run reports to analyze their spending over different time periods. All transactions are saved to a CSV file so data persists between sessions. The app is built for anyone who wants a simple way to keep track of where their money is coming and going without needing a full accounting software
## User Stories



As a customer I want to be able to run a custom search using any combination of start date, end date, description, vendor, and amount so that I can find specific transactions quickly.

As a customer I want a Home Screen so that it can display a list of options I can choose from (Add Deposit, Make Payment, Ledger, Exit)

As a customer I want to be able to add a deposit so that I can record income and track money coming into my account.

As a customer I want to be able to add a payment so that I can record expenses and track money going out of my account.

As a customer I want to be able to view a ledger so that it can display a list of options I can choose from (All entries, Deposits only, Payments only, Reports, Home).

As a customer I want a Reports Screen so that it can display a list of pre-defined reports I can choose from (Month To Date, Previous Month, Year To Date, Previous Year, Search by Vendor).

As a customer I want to be able to view all transactions so that I can see every deposit and payment in my account with the newest entries first.

As a customer I want to be able to view a month to date report so that I can see all transactions from the beginning of the current month to today.

As a customer I want to be able to view a previous month report so that I can see all transactions from last month.

As a customer I want to be able to view a year to date report so that I can see all transactions from the beginning of the current year to today.

As a customer I want to be able to view a previous year report so that I can see all transactions from last year.

As a customer I want to be able to search transactions by vendor name so that I can see all entries for a specific vendor in one place.

As a customer I want my transactions sorted newest to oldest so that I can see my most recent activity first.

As a customer I want to be able to view all transactions so that I can see every deposit and payment.

As a customer I want to be able to view only deposits so that I can see all the money that has come into my account.

As a customer I want to be able to view only payments so that I can see all the money that has gone out of my account.

As a customer I want to filter transactions by date range so that I can see activity within a specific period.


## Setup

Instructions on how to set up and run the project using IntelliJ IDEA.

### Prerequisites

- IntelliJ IDEA: Ensure you have IntelliJ IDEA installed, which you can download from [here](https://www.jetbrains.com/idea/download/).
- Java SDK: Make sure Java SDK is installed and configured in IntelliJ.

### Running the Application in IntelliJ

Follow these steps to get your application running within IntelliJ IDEA:

1. Open IntelliJ IDEA.
2. Select "Open" and navigate to the directory where you cloned or downloaded the project.
3. After the project opens, wait for IntelliJ to index the files and set up the project.
4. Find the main class with the `public static void main(String[] args)` method.
5. Right-click on the file and select 'Run 'YourMainClassName.main()'' to start the application.

## Technologies Used

- Java: Mention the version you are using.
- **Git & GitHub** for version control
- `java.io` — FileReader, BufferedReader, FileWriter, BufferedWriter for reading/writing the CSV file
- `java.time` — LocalDate, LocalTime, DateTimeFormatter for date and time handling
- `java.util` — ArrayList, Scanner for data storage and user input

## Demo

Include screenshots or GIFs that show your application in action. Use tools like [Giphy Capture](https://giphy.com/apps/giphycapture) to record a GIF of your application.

![Application Screenshot](Users/dubem/Downloads/giphy.mp4)
https://media1.giphy.com/media/v1.Y2lkPTc5MGI3NjExZjNhMXNuc3pwcjM3MjBkdm42d3dnZmVuczY0YXV2bTV0NzRyZmRvYiZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/cSYTpc5YbOE3AiZ0c1/giphy.gif

## Future Work

Outline potential future enhancements or functionalities you might consider adding:

- Add the ability to edit or delete existing transactions
- Add a running balance that shows how much money is in the account
- Add category tags to transactions so spending can be grouped (food, rent, entertainment)
- Export reports to a separate file so they can be shared or printed
- Add a monthly budget feature where the user sets a spending limit and gets a warning when they're close to going over
- Add a recurring transaction option so things like rent or subscriptions get auto-logged every month without manual entry


## Resources

List resources such as tutorials, articles, or documentation that helped you during the project.

- [Java 17 LocalDate Documentation](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/time/LocalDate.html)
- [Java 17 LocalTime Documentation](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/time/LocalTime.html)
- [Java 17 DateTimeFormatter Documentation](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/time/format/DateTimeFormatter.html)
- [W3Schools Java File Handling](https://www.w3schools.com/java/java_files.asp)
- [GeeksforGeeks Bubble Sort](https://www.geeksforgeeks.org/bubble-sort/)
- https://raymaroun.github.io/yearup-java-visuals/
- https://gist.github.com/fnky/458719343aabd01cfb17a3a4f7296797
- https://www.baeldung.com/java-ansi-colors


## Team Members

- **David Amah** - Solo developer. Built all features including file I/O, input validation, sorting, reporting, and custom search.



## Thanks

Express gratitude towards those who provided help, guidance, or resources:

- Thank you to Raymond for continuous support and guidance.

 