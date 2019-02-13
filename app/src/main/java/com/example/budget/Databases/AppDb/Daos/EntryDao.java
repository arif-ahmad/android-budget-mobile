package com.example.budget.Databases.AppDb.Daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Insert;
import com.example.budget.Databases.AppDb.Entities.Entry;

import java.util.List;

@Dao
public interface EntryDao {
    @Query("SELECT * FROM entry WHERE is_deleted = 0 ORDER BY creation_date DESC")
    List<Entry> getAll();

    @Query("SELECT * FROM entry WHERE id IN (:ids) AND is_deleted = 0")
    List<Entry> loadAllByIds(String[] ids);

    @Query("SELECT * FROM entry WHERE date >=:startDate AND date<=:endDate AND is_deleted = 0")
    List<Entry> getWithinDateRange(long startDate, long endDate);

    @Query("UPDATE entry SET date=:date,type=:type,amount=:amount,description=:description WHERE id = :id AND is_deleted = 0")
    void update(String id, long date, String type, double amount, String description);

    @Insert
    void insertAll(Entry... entries);

    @Query("UPDATE entry SET is_deleted = 1 WHERE id=:id")
    void delete(String id);
}
