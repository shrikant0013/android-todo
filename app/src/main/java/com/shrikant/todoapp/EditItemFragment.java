package com.shrikant.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shrikant Pandhare on 1/16/16.
 */
public class EditItemFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {

    private EditText etEditItem;
    private EditText etDueDateFrag;
    private String prioritySelection ="Medium";
    Spinner spinner;

    Map<String, Integer> priorityLookUp = new HashMap<>();
    {
        priorityLookUp.put("High", 0);
        priorityLookUp.put("Medium", 1);
        priorityLookUp.put("Low", 2);
    }

    int position;
    ItemsAdapter itemsAdapter;

    public EditItemFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static EditItemFragment newInstance(String itemName, String dueDate, String priority,
                                               int position, ItemsAdapter itemsAdapter) {
        EditItemFragment frag = new EditItemFragment();
        Bundle args = new Bundle();
        args.putString("itemName", itemName);
        args.putString("dueDate", dueDate);
        args.putString("priority", priority);
        frag.setArguments(args);
        frag.position = position;
        frag.itemsAdapter = itemsAdapter;
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_edit_item, container, false);
        etEditItem = (EditText) rootView.findViewById(R.id.etEditItemf);
        //etEditItem.setOnEditorActionListener(this);

        etDueDateFrag = (EditText) rootView.findViewById(R.id.etDueDateFrag);

        spinner = (Spinner) rootView.findViewById(R.id.spinPriorityFrag);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.prioity_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setSelection(2, true);
        spinner.setOnItemSelectedListener(this);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String itemText =  getArguments().getString("itemName", "");
        String dueDate =  getArguments().getString("dueDate", "");
        String priority =  getArguments().getString("priority", "");

        etEditItem.setText(itemText);
        etEditItem.setSelection(etEditItem.getText().length());

        etDueDateFrag.setText(dueDate);

        if (priorityLookUp.containsKey(priority))
            spinner.setSelection(priorityLookUp.get(priority), true);
        else
            spinner.setSelection(1, true);

        Button svbutton = (Button)view.findViewById(R.id.btnSaveEditf);
        svbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // When button is clicked, call up to owning activity.
                Log.i("onSave fragment click", etEditItem.getText().toString());

                Intent data = new Intent();
                // Pass relevant data back as a result
                data.putExtra("ItemText", etEditItem.getText().toString());

                ListView etItemText = (ListView) getActivity().findViewById(R.id.lvItems);

                Item i = (Item) etItemText.getItemAtPosition(position);
                i.name = etEditItem.getText().toString();

                if (etDueDateFrag.getText() != null & !etDueDateFrag.getText().toString().equals(""))
                    i.dueDate = Item.setDateFromString(etDueDateFrag.getText().toString());
                else
                    i.dueDate = null;

                i.priority = prioritySelection;

                i.save();
                itemsAdapter.notifyDataSetChanged();
                dismiss();
            }
        });

        Button clbutton = (Button)view.findViewById(R.id.btnCancelEditf);
        clbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        EditText etDueDate = (EditText)view.findViewById(R.id.etDueDateFrag);
        etDueDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.setTargetFragment(EditItemFragment.this, 300);
                newFragment.show(getFragmentManager(), "datePicker");
                //dismiss();
            }
        });
    }

    @Override
    public void onResume() {
        // Get existing layout params for the window
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        // Call super onResume after sizing
        super.onResume();
    }

    public void fromDate(String inputText) {
        etDueDateFrag.setText(inputText);
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        prioritySelection = (String) parent.getItemAtPosition(pos);

    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
        prioritySelection = "Medium";
    }
}
