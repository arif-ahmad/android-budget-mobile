package com.example.budget.Helpers;

import android.content.Context;

import com.example.budget.Databases.AppDb.AppDatabase;
import com.example.budget.Databases.AppDb.AppDatabaseFactory;
import com.example.budget.Databases.AppDb.Entities.Entry;

import java.util.ArrayList;
import java.util.List;

public class AppDatabaseHelper {
    public static void seed(Context context){
        context.deleteDatabase("app-db");
        AppDatabaseFactory appDatabaseFactory = new AppDatabaseFactory(context);
        final AppDatabase db = appDatabaseFactory.getInstance();
        List<Entry> entries = new ArrayList<Entry>();
        for(int i = 0; i < 20;i++){
            db.entryDao().insertAll(Entry.getRandom());
        }
    }
}
