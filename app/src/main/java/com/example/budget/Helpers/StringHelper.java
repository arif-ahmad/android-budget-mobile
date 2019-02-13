package com.example.budget.Helpers;

public class StringHelper {
    public static boolean IsNullOrEmpty(String str){
        if(str == null || str.equals("")){
            return true;
        }else{
            return false;
        }
    }
}
