package com.shrikant.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Shrikant Pandhare on 1/16/16.
 */
public class EditItemActivity extends AppCompatActivity {

    private EditText etEditItem;
    private EditText etEditDueDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Edit Item");
        setContentView(R.layout.activity_edit_item);

        etEditItem = (EditText) findViewById(R.id.etEditItem);
        String itemText = getIntent().getStringExtra("ItemText");
        etEditItem.setText(itemText);
        etEditItem.setSelection(etEditItem.getText().length());

        if (!getIntent().getStringExtra("ItemDueDate").equalsIgnoreCase("null")) {
            etEditDueDate = (EditText) findViewById(R.id.etDueDate);
            etEditDueDate.setText(getIntent().getStringExtra("ItemDueDate"));
        }
    }

    // ActivityTwo.java
    public void onSaveItem(View v) {
        // closes the activity and returns to first screen
        EditText etName = (EditText) findViewById(R.id.etEditItem);
        EditText etDueDate = (EditText) findViewById(R.id.etDueDate);
        // Prepare data intent
        Intent data = new Intent();
        // Pass relevant data back as a result
        data.putExtra("ItemText", etName.getText().toString());

        Log.i("onSave", etDueDate.getText().toString());

        if (etDueDate.getText() != null & !etDueDate.getText().equals(""))
            data.putExtra("ItemDueDate", etDueDate.getText().toString());
        else
            data.putExtra("ItemDueDate", "null");

        data.putExtra("code", 200); // ints work too
        // Activity finished ok, return the data
        setResult(RESULT_OK, data); // set result code and bundle data for response
        finish(); // closes the activity, pass data to parent
    }


//    public void showDatePickerDialog(View v) {
//        DialogFragment newFragment = new DatePickerFragment();
//        newFragment.show(getSupportFragmentManager(), "datePicker");
//    }

}
