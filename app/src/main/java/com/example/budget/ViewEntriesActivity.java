package com.example.budget;
import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.budget.CustomAdapters.ListViews.EntryAdapter;
import com.example.budget.Databases.AppDb.AppDatabaseFactory;
import com.example.budget.Databases.AppDb.Entities.Entry;
import com.example.budget.Databases.AppDb.Helpers.EntryHelper;
import com.example.budget.Helpers.DateHelper;

import java.time.LocalDate;
import java.util.ArrayList;

public class ViewEntriesActivity extends AppCompatActivity {
    LocalDate selectedStartDate = LocalDate.now();
    LocalDate selectedEndDate = LocalDate.now();

    public static class ViewHolder{
        public static EditText EditTextStartDate = null;
        public static EditText EditTextEndDate = null;
        public static TextView TextTotalEntries = null;
        public static TextView TextTotalAmount = null;
    }
    public void initialiseViewHolder(){
        ViewHolder.EditTextStartDate = (EditText) findViewById(R.id.edit_text_start_date);
        ViewHolder.EditTextEndDate = (EditText) findViewById(R.id.edit_text_end_date);
        ViewHolder.TextTotalEntries = (TextView) findViewById(R.id.text_total_entries);
        ViewHolder.TextTotalAmount = (TextView) findViewById(R.id.text_total_amount);
    }

    private void addListeners(){
        final DatePickerDialog startDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                selectedStartDate = LocalDate.of(year,month+1,dayOfMonth);
                ViewHolder.EditTextStartDate.setText(DateHelper.localDateToFormattedString(selectedStartDate));
                refreshEntriesList();
            }
        }, selectedStartDate.getYear(), selectedStartDate.getMonthValue()-1, selectedStartDate.getDayOfMonth());

        final DatePickerDialog endDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                selectedEndDate = LocalDate.of(year,month+1,dayOfMonth);
                ViewHolder.EditTextEndDate.setText(DateHelper.localDateToFormattedString(selectedEndDate));
                refreshEntriesList();

            }
        }, selectedEndDate.getYear(), selectedEndDate.getMonthValue()-1, selectedEndDate.getDayOfMonth());

        ViewHolder.EditTextStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDatePickerDialog.show();
            }
        });

        ViewHolder.EditTextEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endDatePickerDialog.show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_entries);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initialiseViewHolder();

        addListeners();

        ViewHolder.EditTextStartDate.setText(DateHelper.localDateToFormattedString(selectedStartDate));
        ViewHolder.EditTextEndDate.setText(DateHelper.localDateToFormattedString(selectedEndDate));

        refreshEntriesList();

    }

    private void refreshEntriesList(){
        ArrayList<Entry> entries = (ArrayList<Entry>)AppDatabaseFactory
                .getInstance().entryDao()
                .getWithinDateRange(DateHelper.LocalDateToUnixTimestamp(selectedStartDate),DateHelper.LocalDateToUnixTimestamp(selectedEndDate));

        int totalEntries = entries.size();
        double totalAmount = EntryHelper.getTotalAmount(entries);


        ViewHolder.TextTotalEntries.setText("Total Entries : "+String.valueOf(totalEntries));
        ViewHolder.TextTotalAmount.setText(String.valueOf(totalAmount));

        EntryAdapter entryAdapter = new EntryAdapter(entries,getApplicationContext());

        ListView listEntries = (ListView) findViewById(R.id.list_entries);
        listEntries.setAdapter(entryAdapter);
    }

    @Override
    protected void onResume(){
        super.onResume();
        refreshEntriesList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
