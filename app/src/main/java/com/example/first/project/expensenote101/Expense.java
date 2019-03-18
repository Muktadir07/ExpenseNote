package com.example.first.project.expensenote101;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.AdapterView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Expense extends Fragment implements AdapterView.OnItemSelectedListener {
    private Spinner spinner1;
    String selectType;
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat dateAndTimeSDF = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    SimpleDateFormat dateSDF = new SimpleDateFormat("dd MMM yyyy");
    SimpleDateFormat timeSDF = new SimpleDateFormat("HH:mm:ss aa");

    private Button filterBtn, dateFromBtn, dateTo;
    private Date date = null;
    private FloatingActionButton floatingActionBtn;
    private List<ExpenseList> expenseList = new ArrayList<>();
    private DatabaseHelper databaseHelper;
    private RecyclerView expenseRecycler;
    private CustomExpenseAdapter customExpenseAdapter;
    private long selectedDateinMS,selectedFromDateinMS,selectedDateToinMS;
    private int id,amount;
    private Cursor cursor2;
    String time,type;
    private int year,month,day;

    public Expense() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_expense, container, false);


        floatingActionBtn=view.findViewById(R.id.floatingActionBtn);
        expenseRecycler=view.findViewById(R.id.expenseRecycler);
        dateFromBtn = view.findViewById(R.id.dateFrom);
        dateTo = view.findViewById(R.id.dateTo);
        spinner1=view.findViewById(R.id.spinner);

        expenseRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        databaseHelper = new DatabaseHelper(getActivity());
        showAllData();


        customExpenseAdapter = new CustomExpenseAdapter(expenseList,getActivity(),databaseHelper);
        expenseRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        expenseRecycler.setAdapter(customExpenseAdapter);

        ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(getActivity(),R.array.spinnerTable,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(this);


        dateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePickerTo();

            }
        });
        dateFromBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePickerFrom();

            }
        });


////////////...........  Floating Action Button to reach next Activity.............///////////////

        floatingActionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAct= new Intent(getActivity(),AddExpense.class);
                startActivity(intentAct);
            }
        });
        return view;
    }



    ///////////.............DATE picker...........//////////////


    private void openDatePickerFrom() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                month = month + 1;
                String selectedDate = year + "/" + month + "/" + dayOfMonth + " 00:00:00";
                try {
                    date = dateAndTimeSDF.parse(selectedDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                selectedFromDateinMS = date.getTime();
                //msDate=selectedDateinMS;
                dateFromBtn.setText(dateSDF.format(date));

                if (selectType.equals("")) {
                    expenseList.clear();
                    showDataFromDate();
                    customExpenseAdapter.notifyDataSetChanged();

                }
                else {
                    expenseList.clear();
                    showDataFromDateSpinner();
                    customExpenseAdapter.notifyDataSetChanged();
                }
            }

        };
        year = calendar.get(calendar.YEAR);
        month = calendar.get(calendar.MONTH);
        day = calendar.get(calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), dateSetListener, year, month, day);

        datePickerDialog.show();
//        int amount = 0;
//        cursor2 = databaseHelper.sumExpense(selectedFromDateinMS);
//        while (cursor2.moveToNext()) {
//            amount = cursor2.getInt(cursor2.getColumnIndex("Total3"));
//        }
//        dashboardTV.setText(String.valueOf(amount));


    }

    private void openDatePickerTo() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                String selectedDate = year+"/"+month+"/"+dayOfMonth+" 23:59:59";

                try {
                    date = dateAndTimeSDF.parse(selectedDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                selectedDateToinMS=date.getTime();
                dateTo.setText(dateSDF.format(date));

                if (selectType.equals("")) {
                    expenseList.clear();
                    showDataToDate();
                    customExpenseAdapter.notifyDataSetChanged();

                } else {
                    expenseList.clear();
                    showDataToDateSpinner();
                    customExpenseAdapter.notifyDataSetChanged();
                }

            }
        };

        int year = calendar.get(calendar.YEAR);
        int month = calendar.get(calendar.MONTH);
        int day = calendar.get(calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),dateSetListener,year,month,day);
        datePickerDialog.show();

    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selection = parent.getItemAtPosition(position).toString();
        String selectedNull ="";

        if (position > 0) {
            selectType = selection.toString();
            expenseList.clear();
            showDataFromSpinner();
            customExpenseAdapter.notifyDataSetChanged();

        }
        else {
            selectType=selectedNull;
            expenseList.clear();
            showAllData();
            customExpenseAdapter.notifyDataSetChanged();

        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void showAllData(){
            Cursor cursor = databaseHelper.showData();
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(databaseHelper.COL_ID));
                String type = cursor.getString(cursor.getColumnIndex(databaseHelper.COL_EXPENSE_TYPE));
                int amount = cursor.getInt(cursor.getColumnIndex(databaseHelper.COL_EXPENSE_AMOUNT));
                long Date = cursor.getLong(cursor.getColumnIndex(databaseHelper.COL_EXPENSE_DATE));
                String time = cursor.getString(cursor.getColumnIndex(databaseHelper.COL_EXPENSE_TIME));

                expenseList.add(new ExpenseList(id,type,amount,Date,time));
            }
    }
    public void showDataFromSpinner() {
        cursor2 = databaseHelper.showData(selectType);
        while (cursor2.moveToNext()) {
            int id = cursor2.getInt(cursor2.getColumnIndex(databaseHelper.COL_ID));
            String type = cursor2.getString(cursor2.getColumnIndex(databaseHelper.COL_EXPENSE_TYPE));
            int amount = cursor2.getInt(cursor2.getColumnIndex(databaseHelper.COL_EXPENSE_AMOUNT));
            long Date = cursor2.getLong(cursor2.getColumnIndex(databaseHelper.COL_EXPENSE_DATE));
            String time = cursor2.getString(cursor2.getColumnIndex(databaseHelper.COL_EXPENSE_TIME));

            expenseList.add(new ExpenseList(id,type,amount,Date,time));
        }
    }
    public void showDataFromDate() {
        cursor2 = databaseHelper.showFromTimeData(selectedFromDateinMS);
        while (cursor2.moveToNext()) {
            int id = cursor2.getInt(cursor2.getColumnIndex(databaseHelper.COL_ID));
            String type = cursor2.getString(cursor2.getColumnIndex(databaseHelper.COL_EXPENSE_TYPE));
            int amount = cursor2.getInt(cursor2.getColumnIndex(databaseHelper.COL_EXPENSE_AMOUNT));
            long Date = cursor2.getLong(cursor2.getColumnIndex(databaseHelper.COL_EXPENSE_DATE));
            String time = cursor2.getString(cursor2.getColumnIndex(databaseHelper.COL_EXPENSE_TIME));

            expenseList.add(new ExpenseList(id,type,amount,Date,time));
        }
    }

    public void showDataFromDateSpinner() {
        cursor2 = databaseHelper.showData(selectType,selectedFromDateinMS);
        while (cursor2.moveToNext()) {
            int id = cursor2.getInt(cursor2.getColumnIndex(databaseHelper.COL_ID));
            String type = cursor2.getString(cursor2.getColumnIndex(databaseHelper.COL_EXPENSE_TYPE));
            int amount = cursor2.getInt(cursor2.getColumnIndex(databaseHelper.COL_EXPENSE_AMOUNT));
            long Date = cursor2.getLong(cursor2.getColumnIndex(databaseHelper.COL_EXPENSE_DATE));
            String time = cursor2.getString(cursor2.getColumnIndex(databaseHelper.COL_EXPENSE_TIME));

            expenseList.add(new ExpenseList(id,type,amount,Date,time));
        }
    }
    public void showDataToDate() {
        cursor2 = databaseHelper.showDataWithTtime(selectedFromDateinMS,selectedDateToinMS);
        while (cursor2.moveToNext()) {
            int id = cursor2.getInt(cursor2.getColumnIndex(databaseHelper.COL_ID));
            String type = cursor2.getString(cursor2.getColumnIndex(databaseHelper.COL_EXPENSE_TYPE));
            int amount = cursor2.getInt(cursor2.getColumnIndex(databaseHelper.COL_EXPENSE_AMOUNT));
            long Date = cursor2.getLong(cursor2.getColumnIndex(databaseHelper.COL_EXPENSE_DATE));
            String time = cursor2.getString(cursor2.getColumnIndex(databaseHelper.COL_EXPENSE_TIME));

            expenseList.add(new ExpenseList(id,type,amount,Date,time));
        }
    }
    public void showDataToDateSpinner() {
        cursor2 = databaseHelper.showDataWithTtime(selectType,selectedFromDateinMS,selectedDateToinMS);
        while (cursor2.moveToNext()) {
            int id = cursor2.getInt(cursor2.getColumnIndex(databaseHelper.COL_ID));
            String type = cursor2.getString(cursor2.getColumnIndex(databaseHelper.COL_EXPENSE_TYPE));
            int amount = cursor2.getInt(cursor2.getColumnIndex(databaseHelper.COL_EXPENSE_AMOUNT));
            long Date = cursor2.getLong(cursor2.getColumnIndex(databaseHelper.COL_EXPENSE_DATE));
            String time = cursor2.getString(cursor2.getColumnIndex(databaseHelper.COL_EXPENSE_TIME));

            expenseList.add(new ExpenseList(id,type,amount,Date,time));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        expenseRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        databaseHelper = new DatabaseHelper(getActivity());
        showAllData();


        customExpenseAdapter = new CustomExpenseAdapter(expenseList,getActivity(),databaseHelper);
        expenseRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        expenseRecycler.setAdapter(customExpenseAdapter);

    }
}
