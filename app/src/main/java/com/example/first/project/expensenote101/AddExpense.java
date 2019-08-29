package com.example.first.project.expensenote101;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddExpense extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spinner1;
    private List<ExpenseList> expenseList;
    String selectType;
    private EditText addExpenseAmountET;
    private TextView addDateTV,addTimeTV;
    private ImageButton addTimeBTN,addDateBTN;
    Calendar calendar = Calendar.getInstance();
    private long selectedDateinMS,selectTimeInMS;
    private Button addExpenseBtn;
    private DatabaseHelper databaseHelper;
    private int value;
    private String type,time;
    private int ID,amount;
    private long temp,expenseDate;
    private TextView actionBarTV;


    Expense expense= new Expense();
    Context context= new MainActivity();
   private CustomExpenseAdapter adapter2= new CustomExpenseAdapter(expenseList,this,databaseHelper);


    SimpleDateFormat dateAndTimeSDF =new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    SimpleDateFormat dateSDF= new SimpleDateFormat("dd MMMM yyyy");
    SimpleDateFormat timeSDF= new SimpleDateFormat("HH:mm:ss aa");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);


        addExpenseAmountET=findViewById(R.id.addExpensesAmountET);
        addDateTV=findViewById(R.id.addDateTV);
        actionBarTV=findViewById(R.id.actionBarTV);
        addTimeTV=findViewById(R.id.addTimeTV);
        addTimeBTN=findViewById(R.id.addTimeBTN);
        addDateBTN=findViewById(R.id.addDateBTN);

        addExpenseBtn=findViewById(R.id.addExpenseBTN);
        databaseHelper= new DatabaseHelper(this);


        spinner1=findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(this,R.array.spinnerTable,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(this);

        if (getIntent().getStringExtra("expenseType") == null){


        }
        else {
            type = getIntent().getStringExtra("expenseType");
            ID = getIntent().getIntExtra("ID", -1);
            amount = getIntent().getIntExtra("expenseAmount", 0);
            time=getIntent().getStringExtra("expenseTime");

            long temp = getIntent().getLongExtra("expenseDate", 1);
            Date date1 = new Date(temp);
            String temp1=dateSDF.format(date1);


            int selectionPosition=adapter.getPosition(type);
            spinner1.setSelection(selectionPosition);

            addExpenseAmountET.setText(String.valueOf(amount));
            addDateTV.setText(temp1);
            addTimeTV.setText(time);
            actionBarTV.setText("Update Expense");
            addExpenseBtn.setText("Update Expense");
        }






        addDateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });

        addTimeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimePicker();
            }
        });



        expenseList= new ArrayList<>();
        ///////////////////////// After Adding Expense ///////

        addExpenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type!=null){
                    int expenseAmount= Integer.valueOf(addExpenseAmountET.getText().toString());
                        expenseDate = selectedDateinMS;
                    //special case
                    String expenseTime= addTimeTV.getText().toString();


                    if (selectType.equals("")) {
                        Toast.makeText(AddExpense.this, "Please add Expense Type or Expense Date ", Toast.LENGTH_SHORT).show();
                    } else {
                        long id = databaseHelper.updateData(ID,selectType, expenseAmount, expenseDate, expenseTime);
                        Toast.makeText(AddExpense.this, "Database Updated " + expenseTime, Toast.LENGTH_SHORT).show();

                    }




                }
                else {

                    String expenseAmountStr = addExpenseAmountET.getText().toString();
                    if (expenseAmountStr.equals("")) {
                        Toast.makeText(AddExpense.this, "Expense Amount is empty", Toast.LENGTH_SHORT).show();
                    }


                     else {
                        int expenseAmount = Integer.parseInt(expenseAmountStr);
                        long expenseDate = selectedDateinMS;
                        String expenseTime = addTimeTV.getText().toString();


                        /////........... check if custom adapter is empty or Expense date is not choosen.......////////
                        if (selectType.equals("") || expenseDate == 0) {
                            Toast.makeText(AddExpense.this, "Please add Expense Type or Expense Date ", Toast.LENGTH_SHORT).show();
                        } else {
                            long id = databaseHelper.insertData(selectType, expenseAmount, expenseDate, expenseTime);
                            Toast.makeText(AddExpense.this, "Add to database in time " + expenseTime, Toast.LENGTH_SHORT).show();
//                        expense.onResume();

                        }
                    }
                }


            }
        });




    }
    ////////////..........Date Picker Method here..............////////////////////
    private void openDatePicker() {


        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                month=month+1;
                String selectedDate = year+"/"+month+"/"+dayOfMonth+" 00:00:00";
                Date date = null;

                try {
                    date = dateAndTimeSDF.parse(selectedDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                selectedDateinMS=date.getTime();
                //msDate=selectedDateinMS;
                addDateTV.setText(dateSDF.format(date));


            }
        };



        int year = calendar.get(calendar.YEAR);
        int month = calendar.get(calendar.MONTH);
        int day = calendar.get(calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,dateSetListener,year,month,day);
        //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();



    }
    ////////////........... time picker method working here............./////////////////////////
    private void openTimePicker() {

        ////////////............ this code is for version 23 and above............////////////////////

        /*AlertDialog.Builder builder= new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_time_picker,null);

        Button doneBtn= view.findViewById(R.id.doneBtn);
        final TimePicker timePicker = view.findViewById(R.id.timePickerID)
        builder.setView(view);
        Dialog dialog=builder.create();
        dialog.show();

        doneBtn.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {
                int hour = timePicker.getHour();
                int minute=timePicker.getMinute();

            }
        });*/

        ////////////...........Time picker for API version 16 or above .............////////////////////
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String selecttime = hourOfDay + " : " + minute;
                Time time = new Time(hourOfDay, minute, 0);
                selectTimeInMS = time.getTime();
                addTimeTV.setText(timeSDF.format(time));
                //addTimeBTN.setText(String.valueOf(selectTimeInMS));

            }

        };

        int hour = calendar.get(Calendar.HOUR);
        //Toast.makeText(this, ""+hour, Toast.LENGTH_SHORT).show();
        int minute = calendar.get(Calendar.MINUTE);


        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, hour, minute, false);
        timePickerDialog.show();





    }

////////////...........  Adapter view position check and converting to string.............///////////////

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selection = parent.getItemAtPosition(position).toString();
        String selectedNull = "";

        if (position > 0) {
            selectType = selection.toString();

        }
        else {
            selectType=selectedNull;
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {


    }

    public void back(View view) {
//        adapter2.notifyDataSetChanged();
        onBackPressed();
    }

//    public int checkAmount(){
//        String expenseAmountStr=addExpenseAmountET.getText().toString();
//        if(expenseAmountStr.equals("")){
//            Toast.makeText(context, "please imput Amount", Toast.LENGTH_SHORT).show();
//            value=0;
//        }
//        else {
//             value=Integer.parseInt(expenseAmountStr);
//        }
//        return value;
//
//    }
}
