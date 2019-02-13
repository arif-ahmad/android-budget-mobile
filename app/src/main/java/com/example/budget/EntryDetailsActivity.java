package com.example.budget;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.budget.Databases.AppDb.AppDatabase;
import com.example.budget.Databases.AppDb.AppDatabaseFactory;
import com.example.budget.Databases.AppDb.Entities.Entry;
import com.example.budget.Helpers.AndroidHelper;
import com.example.budget.Helpers.DateHelper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EntryDetailsActivity extends AppCompatActivity {


    Activity currentActivity = null;
    Entry selectedEntry = null;

    DatePickerDialog datePickerDialog = null;

    Entry editedEntry = new Entry();

    private static class ViewHolder{
       public static TextView TextViewAmount = null;
       public static TextView TextViewDate = null;
       public static TextView TextViewDescription = null;
       public static LinearLayout EditContainer = null;
       public static LinearLayout DetailsContainer = null;
       public static EditText EditTextAmount = null;
       public static EditText EditTextDescription = null;
       public static RadioButton RadioButtonTypeIn = null;
       public static RadioButton RadioButtonTypeOut = null;
       public static Button ButtonCancelEdit = null;
       public static Button ButtonEditEntry = null;
       public static EditText EditTextDate = null;
       public static Button ButtonConfirmEntryEdit = null;
       public static RadioGroup RadioGroupType = null;
       public static Button ButtonDeleteEntry = null;
    }


    private void initialiseViewHolder(){
        ViewHolder.ButtonConfirmEntryEdit = (Button) findViewById(R.id.button_confirm_entry_edit);
        ViewHolder.TextViewAmount = (TextView) findViewById(R.id.text_view_amount);
        ViewHolder.TextViewDescription = (TextView) findViewById(R.id.text_view_description);
        ViewHolder.TextViewDate = (TextView) findViewById(R.id.text_view_date);
        ViewHolder.EditContainer = (LinearLayout) findViewById(R.id.container_edit_entry);
        ViewHolder.DetailsContainer = (LinearLayout) findViewById(R.id.container_entry_details);
        ViewHolder.EditTextAmount = (EditText) findViewById(R.id.edit_text_amount);
        ViewHolder.EditTextDescription = (EditText) findViewById(R.id.edit_text_description);
        ViewHolder.RadioButtonTypeIn = (RadioButton) findViewById(R.id.radio_button_type_in);
        ViewHolder.RadioButtonTypeOut = (RadioButton) findViewById(R.id.radio_button_type_out);
        ViewHolder.ButtonCancelEdit = (Button) findViewById(R.id.button_cancel_edit);
        ViewHolder.EditTextDate = (EditText) findViewById(R.id.edit_text_date);
        ViewHolder.RadioGroupType = (RadioGroup) findViewById(R.id.radio_group_type);
    }

    private void startEdit(){
        ViewHolder.DetailsContainer.setVisibility(View.GONE);
        ViewHolder.EditTextAmount.setText(String.valueOf(selectedEntry.amount));
        ViewHolder.EditTextDescription.setText(selectedEntry.description);
        ViewHolder.EditTextDate.setText(selectedEntry.getFormattedDate());
        switch(selectedEntry.type){
            case "in":
                ViewHolder.RadioButtonTypeIn.setChecked(true);
                break;
            case "out":
                ViewHolder.RadioButtonTypeOut.setChecked(true);
                break;
        }
        editedEntry.id = selectedEntry.id;
        editedEntry.amount = selectedEntry.amount;
        editedEntry.description = selectedEntry.description;
        editedEntry.type = selectedEntry.type;
        editedEntry.date = selectedEntry.date;
        ViewHolder.EditContainer.setVisibility(View.VISIBLE);
    }

    private void confirmEdit(){
        Double amount  = Double.valueOf(ViewHolder.EditTextAmount.getText().toString());
        String description = ViewHolder.EditTextDescription.getText().toString();
        String type = "";

        editedEntry.amount = amount;

        editedEntry.description = description;

        int typeRadioButtonId = ViewHolder.RadioGroupType.getCheckedRadioButtonId();
        switch(typeRadioButtonId){
            case R.id.radio_button_type_in:
                type="in";
                break;
            case R.id.radio_button_type_out:
                type = "out";
                break;
        }
        editedEntry.type = type;

        AppDatabase appDatabase = AppDatabaseFactory.getInstance();
        appDatabase.entryDao().update(editedEntry.id,editedEntry.date,editedEntry.type,editedEntry.amount,editedEntry.description);

        selectedEntry.description = description;
        selectedEntry.amount = amount;
        selectedEntry.type = type;
        selectedEntry.date = editedEntry.date;

        setDetails();
        AndroidHelper.hideKeyboard(currentActivity);
        ViewHolder.EditContainer.setVisibility(View.GONE);
        ViewHolder.DetailsContainer.setVisibility(View.VISIBLE);
    }

    private void deleteEntry(){

        AppDatabase appDatabase = AppDatabaseFactory.getInstance();
        appDatabase.entryDao().delete(selectedEntry.id);
        currentActivity.finish();
    }

    private void addListeners(){


        ViewHolder.ButtonConfirmEntryEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmEdit();
            }
        });


        ViewHolder.EditTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
        ViewHolder.ButtonCancelEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidHelper.hideKeyboard(currentActivity);
                ViewHolder.EditContainer.setVisibility(View.GONE);
                ViewHolder.DetailsContainer.setVisibility(View.VISIBLE);
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        currentActivity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LocalDate currentDate = LocalDate.now();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                LocalDate localDate = LocalDate.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
                EditText editTextDate = (EditText) findViewById(R.id.edit_text_date);
                LocalDate inputDate = LocalDate.of(year,month+1,dayOfMonth);
                String txtDate = formatter.format(inputDate);
                editTextDate.setText(txtDate);
                editedEntry.date = DateHelper.LocalDateToUnixTimestamp(inputDate);
            }
        },currentDate.getYear(),currentDate.getMonthValue()-1,currentDate.getDayOfMonth());

        initialiseViewHolder();

        Intent intent = getIntent();
        String entryId = intent.getStringExtra("EntryId");
        String[] entryIds = new String[] {
            entryId
        };

        AppDatabase appDatabase = AppDatabaseFactory.getInstance();
        List<Entry> entries = appDatabase.entryDao().loadAllByIds(entryIds);
        if(entries.size() == 1){
            selectedEntry = entries.get(0);
            setDetails();
        }else{
            final AlertDialog alertDialog = new AlertDialog.Builder(EntryDetailsActivity.this).create();
            alertDialog.setTitle("Error");
            alertDialog.setMessage("Entry does not exist.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            currentActivity.finish();
                        }
                    });
            alertDialog.show();
        }

        addListeners();
    }
    private void setDetails(){
        ViewHolder.TextViewDate.setText(selectedEntry.getFormattedDate());
        ViewHolder.TextViewAmount.setText(String.valueOf(selectedEntry.amount));
        ViewHolder.TextViewDescription.setText(selectedEntry.description);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.edit:
                startEdit();
                break;
            case R.id.delete:
                deleteEntry();
                break;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
