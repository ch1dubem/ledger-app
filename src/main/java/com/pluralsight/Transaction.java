package com.pluralsight;

import java.time.LocalDate;
import java.time.LocalTime;

public class Transaction {
    private LocalTime time;
    private LocalDate date;
    private  String transactionDescription;
    private String vendor;
    private double amount;

    public Transaction(LocalTime time, LocalDate date, String transactionDescription, String vendor, double amount) {
        this.time = time;
        this.date = date;
        this.transactionDescription  = transactionDescription;
        this.vendor = vendor;
        this.amount = amount;
    }
    public LocalTime getTime() {
        return time;
    }
    public void setTime(LocalTime time) {
        this.time = time;
    }
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getVendor() {
        return vendor;

    }
    public void setVendor(String vendor) {
        this.vendor = vendor;
    }
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTransactionDescription() {
        return transactionDescription;
    }
    public void setTransactionDescription(String transactionDescription) {
        this.transactionDescription = transactionDescription;
    }


}
