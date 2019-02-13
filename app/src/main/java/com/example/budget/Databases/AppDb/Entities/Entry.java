package com.example.budget.Databases.AppDb.Entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.example.budget.Helpers.DateHelper;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

@Entity
public class Entry {
    @PrimaryKey
    @NonNull
    public String id;
    public String type;
    public Double amount;
    public String description;
    public long date = DateHelper.LocalDateToUnixTimestamp(LocalDateTime.now());

    @NonNull
    @ColumnInfo(name="is_deleted")
    public int isDeleted = 0;

    @NonNull
    @ColumnInfo(name="creation_date")
    public long creationDate;

    public static Entry getRandom(){
        int randomNumber = (int)(Math.random() * 500 + 1);
        Entry randomEntry = new Entry();
        randomEntry.id = UUID.randomUUID().toString();
        if(randomNumber % 2 == 0){
            randomEntry.type = "in";
        }else{
            randomEntry.type = "out";
        }
        randomEntry.amount = (double)randomNumber;
        randomEntry.description = "Test " + randomNumber;
        randomEntry.date = DateHelper.LocalDateToUnixTimestamp(LocalDate.now());
        return randomEntry;
    }
    public String getFormattedDate(String format){
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date date = new Date(this.date*1000);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return formatter.format(calendar.getTime());
    }
    public String getFormattedDate(){
        return getFormattedDate("dd LLLL yyyy");
    }
}
