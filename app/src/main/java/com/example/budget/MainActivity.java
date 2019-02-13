package com.example.budget;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.budget.Databases.AppDb.AppDatabase;
import com.example.budget.Databases.AppDb.AppDatabaseFactory;
import com.example.budget.Databases.AppDb.Entities.Entry;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private void seedDatabase(){
        this.deleteDatabase("app-db");
        AppDatabaseFactory appDatabaseFactory = new AppDatabaseFactory(getApplicationContext());
        final AppDatabase db = appDatabaseFactory.getInstance();
        List<Entry> entries = new ArrayList<Entry>();
        for(int i = 0; i < 20;i++){
            db.entryDao().insertAll(Entry.getRandom());
        }

    }
    MainActivity currentActivity = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seedDatabase();

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                db.clearAllTables();
//            }
//        }).start();
//
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_add_entry);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(currentActivity,NewEntryActivity.class);
                startActivity(intent);
            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation_bottom);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Intent intent = null;
                switch (menuItem.getItemId()) {
                    case R.id.navigation_new_entry:
                        intent = new Intent(currentActivity,NewEntryActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.navigation_entries:
                        intent = new Intent(currentActivity, ViewEntriesActivity.class);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });
    }
}
