package com.example.first.project.expensenote101;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {
    BottomNavigationView navigation;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            =new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){

                case R.id.navigation_dashboard:
                    Dashboard dashboard = new Dashboard();
                    FragmentManager fmd = getSupportFragmentManager();
                    FragmentTransaction ftd = fmd.beginTransaction();
                    ftd.replace(R.id.framecontainer, dashboard);
                    ftd.commit();
                    return true;

                case R.id.navigation_expense:
                    Expense expense= new Expense();
                    FragmentManager fme = getSupportFragmentManager();
                    FragmentTransaction fte= fme.beginTransaction();
                    fte.replace(R.id.framecontainer, expense);
                    fte.addToBackStack("expense");
                    fte.commit();
                    return true;

            }
            return false;
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Dashboard dashboard = new Dashboard();
        FragmentManager fm2d = getSupportFragmentManager();
        FragmentTransaction ftd1 = fm2d.beginTransaction();
        ftd1.replace(R.id.framecontainer, dashboard);
        ftd1.commit();

        navigation=findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
}
