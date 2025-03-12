package com.example.expensetracker.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "expenses")
public class Expense {
    @PrimaryKey(autoGenerate = true)
    private int expenseID;
    private String name;
    private String vendor;
    private String category;
    private String description;
    private double amountSpent;
    private String paymentMethod;
    private String receiptImagePath; //file path

    //Constructor
    public Expense(String name, String vendor, String category, String description, double amountSpent, String paymentMethod, String receiptImagePath){
        this.name = name;
        this.vendor = vendor;
        this.category = category;
        this.description = description;
        this.amountSpent = amountSpent;
        this.paymentMethod = paymentMethod;
        this.receiptImagePath = receiptImagePath;
    }


    //getters and setters
    public int getExpenseID() {
        return expenseID;
    }

    public void setExpenseID(int expenseID) {
        this.expenseID = expenseID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmountSpent() {
        return amountSpent;
    }

    public void setAmountSpent(double amountSpent) {
        this.amountSpent = amountSpent;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getReceiptImagePath() {
        return receiptImagePath;
    }

    public void setReceiptImagePath(String receiptImagePath) {
        this.receiptImagePath = receiptImagePath;
    }
}
