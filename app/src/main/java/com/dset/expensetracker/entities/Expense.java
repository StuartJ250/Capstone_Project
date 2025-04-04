package com.dset.expensetracker.entities;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Entity(
        tableName = "expenses",
        foreignKeys = @ForeignKey(
                entity = Login.class,
                parentColumns = "userID",
                childColumns = "userID",
                onDelete = ForeignKey.CASCADE),
        indices = {@Index(value = "userID")}
)
public class Expense {
    @PrimaryKey(autoGenerate = true)
    private int expenseID;
    private String name;
    private String expenseDate;
    private String timeStamp;
    private String vendor;
    private String category;
    private String description;
    private double amountSpent;
    private String paymentMethod;

    @ColumnInfo(name = "userID")
    private int userID;  //foreign key for login
    private String receiptImagePath; //file path

    //Constructor
    public Expense(String name, String vendor, String category, String description, double amountSpent, String paymentMethod, String receiptImagePath, String expenseDate, int userID){
        this.name = name;
        this.vendor = vendor;
        this.category = category;
        this.description = description;
        this.amountSpent = amountSpent;
        this.paymentMethod = paymentMethod;
        this.receiptImagePath = receiptImagePath;
        this.expenseDate = expenseDate;
        this.timeStamp = getCurrentTimeStamp();
        this.userID = userID;
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

    public String getExpenseDate() { return expenseDate; }

    public void setExpenseDate(String expenseDate) {this.expenseDate = expenseDate;}

    public String getTimeStamp() {return timeStamp;}

    public void setTimeStamp(String timeStamp) {this.timeStamp = timeStamp;}

    public int getUserID() {return userID;}

    public void setUserID(int userID) {this.userID = userID;}

    public void setReceiptImagePath(String receiptImagePath) {
        this.receiptImagePath = receiptImagePath;
    }

    private String getCurrentTimeStamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}
