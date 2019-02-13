package com.example.budget.Databases.AppDb;

import android.arch.persistence.room.Room;
import android.content.Context;

public class AppDatabaseFactory {
    private static AppDatabase db = null;
    public static AppDatabase getInstance(){
        return db;
    }
    public AppDatabaseFactory(Context applicationContext){
        this.db = Room.databaseBuilder(applicationContext,
                AppDatabase.class, "app-db").allowMainThreadQueries().build();
    }
}
