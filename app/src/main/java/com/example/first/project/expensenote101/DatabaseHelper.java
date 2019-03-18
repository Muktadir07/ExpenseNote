package com.example.first.project.expensenote101;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="expense.db";
    public static final String TABLE_NAME="expense";
    public static final int VERSION=1;
    public static final String COL_ID="ID";
    public static final String COL_EXPENSE_TYPE="ExpenseType";
    public static final String COL_EXPENSE_AMOUNT="ExpenseAmount";
    public static final String COL_EXPENSE_DATE="ExpenseDate";
    public static final String COL_EXPENSE_TIME="ExpenseTime";
    //public static final String COL_EXPENSE_IMAGE="ExpenseImage";

    String create_table ="create table " +TABLE_NAME+ " ("+COL_ID+ " INTEGER PRIMARY KEY," +COL_EXPENSE_TYPE+" TEXT,"+COL_EXPENSE_AMOUNT+" INTEGER,"+COL_EXPENSE_DATE+ " INTEGER,"+COL_EXPENSE_TIME+" TEXT);";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(create_table);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public long insertData(String name,int amount,long date,String time){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_EXPENSE_TYPE,name);
        contentValues.put(COL_EXPENSE_AMOUNT,amount);
        contentValues.put(COL_EXPENSE_DATE,date);
        contentValues.put(COL_EXPENSE_TIME,time);
        long id =getWritableDatabase().insert(TABLE_NAME,null,contentValues);
        return id;
    }
    public long updateData(int ID , String name,int amount,long date,String time){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID,ID);
        contentValues.put(COL_EXPENSE_TYPE,name);
        contentValues.put(COL_EXPENSE_AMOUNT,amount);
        contentValues.put(COL_EXPENSE_DATE,date);
        contentValues.put(COL_EXPENSE_TIME,time);
        long id = getWritableDatabase().update(TABLE_NAME,contentValues,COL_ID+" = ? ",new String[]{String.valueOf(ID)});
        return id;
    }
    public void deleteData(int id){
        getWritableDatabase().delete(TABLE_NAME," ID =?", new String[]{String.valueOf(id)});
    }



    ////////////...........  DASHBOARD METHOD START.............///////////////


    public Cursor sumExpense(long currentMonBill, long currentMonToTodayBill) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
         Cursor cursor = sqLiteDatabase.rawQuery(" SELECT SUM("+ COL_EXPENSE_AMOUNT +") as Total from " +TABLE_NAME
                 +" where ExpenseDate BETWEEN '"+currentMonBill+"' AND '"+currentMonToTodayBill+"'",null);
         return cursor;

//        String sum= " select ExpenseAmount from "+TABLE_NAME;
//        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
//        Cursor cursor = sqLiteDatabase.rawQuery(sum,null);
//        return cursor;

    }
    ////////////...........  Sum up Expense Amount Method (METHOD OVERLOADING).............///////////////

    public Cursor sumExpense(String selectType){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(" SELECT SUM("+ COL_EXPENSE_AMOUNT +") as Total2 from " +TABLE_NAME
                +" where ExpenseType= '"+selectType+"'",null);
        return cursor;

//        String sum="select ExpenseAmount from "+TABLE_NAME+" where ExpenseType= '"+selectType+"'";
//        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
//        Cursor cursor = sqLiteDatabase.rawQuery(sum,null);
//        return cursor;
    }
    public Cursor sumExpenseFrom(long expenseDate) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(" SELECT SUM(" + COL_EXPENSE_AMOUNT + ") as Total3 from " + TABLE_NAME
                + " where ExpenseDate >= '" + expenseDate + "'", null);
        return cursor;
    }
    public Cursor sumExpenseFrom(long expenseDate,String selectType) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(" SELECT SUM(" + COL_EXPENSE_AMOUNT + ") as Total3 from " + TABLE_NAME
                + " where ExpenseType= '" + selectType + "' AND  ExpenseDate >= '" + expenseDate + "'", null);
        return cursor;
    }
    public Cursor sumExpenseTo(long selectedDateFrominMS, long selectedDateToinMS) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(" SELECT SUM("+ COL_EXPENSE_AMOUNT +") as Total4 from " +TABLE_NAME
                +" where ExpenseDate BETWEEN '"+selectedDateFrominMS+"' AND '"+selectedDateToinMS+"'",null);
        return cursor;

    }
    public Cursor sumExpenseTo(long selectedDateFrominMS, long selectedDateToinMS,String expenseType) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(" SELECT SUM("+ COL_EXPENSE_AMOUNT +") as Total4 from " +TABLE_NAME
                +" where ExpenseType= '"+expenseType+"' AND ( ExpenseDate BETWEEN '"+selectedDateFrominMS+"' AND '"+selectedDateToinMS+"')",null);
        return cursor;

    }
    ////////////...........  DASHBOARD DATABASE CODE END.............///////////////

    ////////////...........  EXPENSE DATABASE START.............///////////////
    public Cursor showData(){
        String show_all= "select * from "+TABLE_NAME;
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(show_all,null);
        return  cursor;
    }

    public Cursor showData(String expenseType){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from "+TABLE_NAME+" where ExpenseType = '" +expenseType+ "'",null);
        return  cursor;
    }
    public Cursor showFromTimeData(long expenseDate){

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from "+TABLE_NAME+" where ExpenseDate >= '"+expenseDate+"'",null);
        return  cursor;
    }

    public Cursor showData(String expenseType,long expenseDate){

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from "+TABLE_NAME+" where ExpenseType = '"+expenseType+"' " +
                " AND ExpenseDate >= '"+expenseDate+"'",null);
        return  cursor;
    }
    public Cursor showDataWithTtime(long expenseFromDate,long expenseToDate){

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from "+TABLE_NAME+" where ExpenseDate BETWEEN '"+expenseFromDate+"' " +
                " AND '"+expenseToDate+"'",null);
        return  cursor;
    }

    public Cursor showDataWithTtime(String expenseType,long expenseFromDate,long expenseToDate){

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from "+TABLE_NAME+" where ExpenseType= '"+expenseType+"' " +
                " AND ExpenseDate BETWEEN '"+expenseFromDate+"' AND '"+expenseToDate+"'",null);
        return  cursor;
    }




}
