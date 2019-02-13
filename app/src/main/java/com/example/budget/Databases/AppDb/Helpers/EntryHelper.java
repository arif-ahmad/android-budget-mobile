package com.example.budget.Databases.AppDb.Helpers;

import com.example.budget.Databases.AppDb.Entities.Entry;

import java.util.List;

public class EntryHelper {
    public static double getTotalAmount(List<Entry> entries){
        double total = 0;
        for(Entry entry : entries){
            switch(entry.type){
                case "in":
                    total = total + entry.amount;
                    break;
                case "out":
                    total = total - entry.amount;
                    break;
            }
        }
        return total;
    }
}
