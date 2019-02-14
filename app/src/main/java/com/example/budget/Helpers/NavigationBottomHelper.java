package com.example.budget.Helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.example.budget.HomeActivity;
import com.example.budget.NewEntryActivity;
import com.example.budget.R;
import com.example.budget.ViewEntriesActivity;

public class NavigationBottomHelper {
    public static void setGeneral(final Activity activity){
        BottomNavigationView bottomNavigationView = (BottomNavigationView) activity.findViewById(R.id.navigation_bottom);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Intent intent = null;
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        intent = new Intent(activity, HomeActivity.class);
                        activity.startActivity(intent);
                        break;
                    case R.id.navigation_new_entry:
                        intent = new Intent(activity, NewEntryActivity.class);
                        activity.startActivity(intent);
                        break;
                    case R.id.navigation_entries:
                        intent = new Intent(activity, ViewEntriesActivity.class);
                        activity.startActivity(intent);
                        break;
                }
                return false;
            }
        });

    }
}
