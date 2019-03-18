package com.example.first.project.expensenote101;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BottomSheetClass extends BottomSheetDialogFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView expansetypeTVBot , expanseamountTVBot , expansedateTVBot , expansetimeTVBot;
        SimpleDateFormat dateSDF= new SimpleDateFormat("dd MMMM yyyy");

        View view = inflater.inflate(R.layout.bottom_sheet,container,false);

        expansetypeTVBot = view.findViewById(R.id.expansetypeTVBot);
        expanseamountTVBot = view.findViewById(R.id.expanseamountTVBot);
        expansedateTVBot = view.findViewById(R.id.expansedateTVBot);
        expansetimeTVBot = view.findViewById(R.id.expansetimeTVBot);

        Bundle bundle = getArguments();

        if(bundle != null){

            String type = getArguments().getString("type");
            String amount=String.valueOf(getArguments().getInt("amount"));
            String time=getArguments().getString("time");
            long date=getArguments().getLong("date");
            Date date1 = new Date(date);
            String dateOfmonth = dateSDF.format(date1);

            expansetypeTVBot.setText(type);
            expanseamountTVBot.setText(amount);
            expansedateTVBot.setText(dateOfmonth);
            expansetimeTVBot.setText(time);

        }
        return view;
    }
}
