package com.example.budget;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.budget.Databases.AppDb.AppDatabase;
import com.example.budget.Databases.AppDb.AppDatabaseFactory;
import com.example.budget.Databases.AppDb.Entities.Entry;
import com.example.budget.Helpers.DateHelper;
import com.example.budget.Helpers.StringHelper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.UUID;

public class NewEntryActivity extends AppCompatActivity {

    private NewEntryActivity currentActivity = this;
    DatePickerDialog datePickerDialog = null;
    private EditText editTextDate = null;
    LocalDate inputDate = null;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        LocalDate localDate = LocalDate.now();

        editTextDate = (EditText) findViewById(R.id.edit_text_date);
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                LocalDate localDate = LocalDate.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
                EditText editTextDate = (EditText) findViewById(R.id.edit_text_date);
                inputDate = LocalDate.of(year,month,dayOfMonth);
                String txtDate = formatter.format(inputDate);
                editTextDate.setText(txtDate);
            }
        },localDate.getYear(),localDate.getMonthValue(),localDate.getDayOfMonth());

        final AlertDialog alertDialog = new AlertDialog.Builder(NewEntryActivity.this).create();
        alertDialog.setTitle("Success");
        alertDialog.setMessage("Your new entry is added.");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


        Button buttonSubmit = (Button) findViewById(R.id.button_submit_entry);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextAmount = (EditText) findViewById(R.id.edit_text_amount);
                EditText editTextDescription = (EditText) findViewById(R.id.edit_text_description);

                String txtAmount = editTextAmount.getText().toString();

                String description = editTextDescription.getText().toString();
                RadioGroup radioGroupType = (RadioGroup) findViewById(R.id.radio_group_type);

                String type = "";
                int typeRadioButtonId = radioGroupType.getCheckedRadioButtonId();
                switch(typeRadioButtonId){
                    case R.id.radio_button_type_in:
                        type="in";
                        break;
                    case R.id.radio_button_type_out:
                        type = "out";
                        break;
                }


                if(inputDate == null){
                    Toast.makeText(getApplicationContext(),"Select date for entry.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if(type == ""){
                    Toast.makeText(getApplicationContext(),"Select type of entry (in or out).",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if(StringHelper.IsNullOrEmpty(txtAmount)){
                    Toast.makeText(getApplicationContext(),"Enter a valid amount.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if(description.trim().isEmpty() || StringHelper.IsNullOrEmpty(description)){
                    Toast.makeText(getApplicationContext(),"Enter a description.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                Double amount = Double.valueOf(txtAmount);

                AppDatabase db = AppDatabaseFactory.getInstance();
                Entry newEntry = new Entry();
                newEntry.id = UUID.randomUUID().toString();
                newEntry.amount = amount;
                newEntry.type = type;
                newEntry.description = description;
                newEntry.date = DateHelper.LocalDateToUnixTimestamp(inputDate);

                db.entryDao().insertAll(newEntry);
                alertDialog.show();

                EditText editTextDate = (EditText) findViewById(R.id.edit_text_date);
                editTextDate.setText("");
                inputDate = null;
                radioGroupType.clearCheck();

                editTextAmount.setText("");
                editTextDescription.setText("");
            }
        });
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
