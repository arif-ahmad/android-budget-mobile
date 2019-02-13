package com.example.budget;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
        Button buttonAddEntry  = (Button) findViewById(R.id.button_add_entry);
        buttonAddEntry.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(currentActivity,NewEntryActivity.class);
                startActivity(intent);
            }
        });

        Button buttonViewEntries = (Button) findViewById(R.id.button_view_entries);
        buttonViewEntries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(currentActivity, ViewEntriesActivity.class);
                startActivity(intent);
            }
        });


    }
}
