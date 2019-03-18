package com.example.first.project.expensenote101;


import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class Dashboard extends Fragment implements AdapterView.OnItemSelectedListener {
    String selectType;
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat dateAndTimeSDF = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    SimpleDateFormat dateSDF = new SimpleDateFormat("dd MMM yyyy");
    SimpleDateFormat timeSDF = new SimpleDateFormat("HH:mm:ss aa");

    private Spinner spinner1;
    private Button filterBtn, dateFromBtn, dateTo;
    private DatabaseHelper databaseHelper;
    private TextView dashboardTV;
    private int total = 0;
    private Date date = null;
    private long selectedDateinMS,selectedFromDateinMS,selectedDateToinMS;
    private Cursor cursor2;
    private int year, month, day,hour,minute;
    public Dashboard() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        spinner1 = view.findViewById(R.id.spinner);
        dashboardTV = view.findViewById(R.id.dashboardTV);
        dateFromBtn = view.findViewById(R.id.dateFrom);
        dateTo = view.findViewById(R.id.dateTo);


        databaseHelper = new DatabaseHelper(getActivity());


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.spinnerTable, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(this);

        ////////////...........  Filter With Button............///////////////



        dateFromBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePickerFrom();

            }
        });
        dateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePickerTo();

            }
        });


        return view;
    }




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
                        int amount = 0;
                        cursor2 = databaseHelper.sumExpenseFrom(selectedFromDateinMS);
                        while (cursor2.moveToNext()) {
                            amount = cursor2.getInt(cursor2.getColumnIndex("Total3"));
                        }
                        dashboardTV.setText(String.valueOf(amount));

                    } else {
                        int amount = 0;
                        cursor2 = databaseHelper.sumExpenseFrom(selectedFromDateinMS, selectType);
                        while (cursor2.moveToNext()) {
                            amount = cursor2.getInt(cursor2.getColumnIndex("Total3"));
                        }
                        dashboardTV.setText(String.valueOf(amount));
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
                        int amount = 0;
                        cursor2 = databaseHelper.sumExpenseTo(selectedFromDateinMS,selectedDateToinMS);
                        while (cursor2.moveToNext()) {
                            amount = cursor2.getInt(cursor2.getColumnIndex("Total4"));
                        }
                        dashboardTV.setText(String.valueOf(amount));

                    } else {
                        int amount = 0;
                        cursor2 = databaseHelper.sumExpenseTo(selectedFromDateinMS,selectedDateToinMS, selectType);
                        while (cursor2.moveToNext()) {
                            amount = cursor2.getInt(cursor2.getColumnIndex("Total4"));
                        }
                        dashboardTV.setText(String.valueOf(amount));
                    }
            }
        };
        int year = calendar.get(calendar.YEAR);
        int month = calendar.get(calendar.MONTH);
        int day = calendar.get(calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),dateSetListener,year,month,day);

        datePickerDialog.show();

    }
    ////////////...........  Adapter view position check and converting to string.............///////////////


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selection = parent.getItemAtPosition(position).toString();
        String selectedNull = "";

        if (position > 0) {
            selectType = selection.toString();
            calculateBill();



        } else {
            selectType=selectedNull;
            showDashboard();
        }

    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void showDashboard() {
        long currentMonBill=currentMonthBill();
        long currentMonToTodayBill=currentMonthToTodayBill();
        int amount = 0;
        cursor2 = databaseHelper.sumExpense(currentMonBill,currentMonToTodayBill);
        while (cursor2.moveToNext()) {
            amount = cursor2.getInt(cursor2.getColumnIndex("Total"));
        }
        dashboardTV.setText(String.valueOf(amount));


//////Working code for dashboard SUM
//        Cursor cursor2 = databaseHelper.sumExpense();
//        while (cursor2.moveToNext()) {
//            int amount = cursor2.getInt(cursor2.getColumnIndex(databaseHelper.COL_EXPENSE_AMOUNT));
//            total = total + amount;
//        }
//        dashboardTV.setText(String.valueOf(total));
    }

    public void calculateBill() {
        cursor2 = databaseHelper.sumExpense(selectType);

        while (cursor2.moveToNext()) {
            total = cursor2.getInt(cursor2.getColumnIndex("Total2"));
        }
        dashboardTV.setText(String.valueOf(total));
    }

    public long currentMonthBill(){
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        month = month + 1;
        int day = 00;
        String value = year + "/" + month + "/" + day + " 00:00:01";
        try {
            date = dateAndTimeSDF.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        selectedDateinMS = date.getTime();
        return selectedDateinMS;
    }
    public long currentMonthToTodayBill(){
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        month = month + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour= calendar.get(Calendar.HOUR);
        minute=calendar.get(Calendar.MINUTE);
        String value = year + "/" + month + "/" + day + " " +hour+ ":" +minute+ ":00";
        try {
            date = dateAndTimeSDF.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        selectedDateinMS = date.getTime();
        return selectedDateinMS;
    }


}

