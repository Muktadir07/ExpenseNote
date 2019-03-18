package com.example.first.project.expensenote101;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CustomExpenseAdapter extends RecyclerView.Adapter<CustomExpenseAdapter.ViewHolder> {
    List<ExpenseList> expenseList;
    Context context;
    DatabaseHelper databaseHelper;
    Fragment fragment= new Expense();

    public CustomExpenseAdapter(List<ExpenseList> expenseList, Context context, DatabaseHelper databaseHelper) {
        this.context = context;
        this.expenseList = expenseList;
        this.databaseHelper = databaseHelper;
    }


//    public CustomExpenseAdapter(Context context, List<ExpenseList> expenseList, DatabaseHelper databaseHelper) {
//        this.context = context;
//        this.expenseList = expenseList;
//        this.databaseHelper = databaseHelper;
//    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.expense_list_layout,viewGroup,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomExpenseAdapter.ViewHolder viewHolder, final int i) {


        final ExpenseList currentExpense=expenseList.get(i);
        SimpleDateFormat dateSDF= new SimpleDateFormat("dd MMMM yyyy");
        Date date= new Date();
        date.setTime(currentExpense.getExpenseDate());
        viewHolder.expenseTypeTV.setText(currentExpense.getExpenseType());
        viewHolder.expenseAmountTV.setText("$ "+currentExpense.getExpenseAmount());
        viewHolder.expenseDateTV.setText(dateSDF.format(date));

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("ID",currentExpense.getId());
                bundle.putString("type",currentExpense.getExpenseType());
                bundle.putInt("amount",currentExpense. getExpenseAmount());
                bundle.putLong("date",currentExpense.getExpenseDate());
                bundle.putString("time",currentExpense.getExpenseTime());
                BottomSheetClass bottomSheetClass = new BottomSheetClass();
                bottomSheetClass.setArguments(bundle);
                MainActivity mainAct= (MainActivity) context;
                bottomSheetClass.show(mainAct.getSupportFragmentManager(),"tag");


            }
        });

        viewHolder.popupmenuID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context,viewHolder.popupmenuID);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId())
                        {
                            case R.id.deleteMenu:
                                deleteFinalize(i);
                                break;
                            case R.id.updateMenu:
                                Intent intent =  new Intent(context,AddExpense.class)
                                        .putExtra("ID", currentExpense.getId())
                                        .putExtra("expenseType", String.valueOf(currentExpense.getExpenseType()))
                                        .putExtra("expenseAmount", currentExpense.getExpenseAmount())
                                        .putExtra("expenseDate", currentExpense.getExpenseDate())
                                        .putExtra("expenseTime", String.valueOf(currentExpense.getExpenseTime()));

                                context.startActivity(intent);
                                Toast.makeText(context, "update", Toast.LENGTH_SHORT).show();
                        }
                        return false;
                    }
                });

            }
        });



    }

    public void deleteFinalize(final int i){

        final AlertDialog.Builder builder= new AlertDialog.Builder(context);
        LayoutInflater layoutInflater= LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.delete_aleart,null);

        builder.setView(view);
        final Dialog dialog = builder.create();
        dialog.show();

        TextView deleteAction=view.findViewById(R.id.deleteTV);
        TextView cancelAction =view.findViewById(R.id.cancelTV);


        deleteAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExpenseList currentExpense=expenseList.get(i);

                Toast.makeText(context, "Data deleted", Toast.LENGTH_SHORT).show();
                databaseHelper.deleteData(currentExpense.getId());
                expenseList.remove(i);
                notifyDataSetChanged();
                dialog.dismiss();

            }
        });
        cancelAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });


    }


    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView expenseTypeTV,expenseDateTV,expenseAmountTV;
        private ImageView popupmenuID;
        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            expenseTypeTV=itemView.findViewById(R.id.expenseTypeTV);
            expenseAmountTV=itemView.findViewById(R.id.expenseAmountTV);
            expenseDateTV=itemView.findViewById(R.id.expenseDateTV);
            popupmenuID=itemView.findViewById(R.id.vertIconIV);
            cardView = itemView.findViewById(R.id.cardview);
        }
    }
}
