package com.example.budget.Databases.AppDb;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.budget.Databases.AppDb.Daos.EntryDao;
import com.example.budget.Databases.AppDb.Entities.Entry;

@Database(entities = {Entry.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract EntryDao entryDao();
}