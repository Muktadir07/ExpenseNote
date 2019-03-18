package com.example.first.project.expensenote101;

public class ExpenseList {
    private int id;
    private String expenseType;
    private int expenseAmount;
    private long expenseDate;
    private String expenseTime;

    public ExpenseList(int id, String expenseType, int expenseAmount, long expenseDate,String expenseTime ) {
        this.id = id;
        this.expenseType = expenseType;
        this.expenseAmount = expenseAmount;
        this.expenseDate = expenseDate;
        this.expenseTime=expenseTime;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(String expenseType) {
        this.expenseType = expenseType;
    }

    public int getExpenseAmount() {
        return expenseAmount;
    }

    public void setExpenseAmount(int expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    public long getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(long expenseDate) {
        this.expenseDate = expenseDate;
    }

    public String getExpenseTime() {
        return expenseTime;
    }

    public void setExpenseTime(String expenseTime) {
        this.expenseTime = expenseTime;
    }
}
