package com.example.budget;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.budget.Helpers.AppDatabaseHelper;
import com.example.budget.Helpers.NavigationBottomHelper;

public class HomeActivity extends AppCompatActivity {


    HomeActivity currentActivity = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        AppDatabaseHelper.seed(getApplicationContext());
        NavigationBottomHelper.setGeneral(currentActivity);
    }
}
