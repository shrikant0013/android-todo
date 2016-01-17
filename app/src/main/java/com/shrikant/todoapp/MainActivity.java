package com.shrikant.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Shrikant Pandhare on 1/16/16.
 */
public class MainActivity extends AppCompatActivity {

    private EditText etNewItem;
    private ArrayList<Item> items;
    private ItemsAdapter itemsAdapter;
    private ListView lvItems;
    private int editItemPosition;
    private final int REQUEST_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etNewItem = (EditText) findViewById(R.id.etNewItem);
        lvItems = (ListView) findViewById(R.id.lvItems);
        readItemsFromDB();

        itemsAdapter = new ItemsAdapter(this, items);
        // Attach the adapter to a ListView
        lvItems.setAdapter(itemsAdapter);


        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                deleteItemFromDB(position);
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editItemPosition = position;
                launchEditItem(position);
            }
        });
    }

    // ActivityOne.java
    public void launchEditItem(int position) {
        String itemName = items.get(position).name;
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");

        String itemDueDate = "";
        if (items.get(position).dueDate != null) {
            itemDueDate = df.format(items.get(position).dueDate);
        }

        String priority =  items.get(position).priority;

        FragmentManager fm = getSupportFragmentManager();
        EditItemFragment editItemDialog = EditItemFragment.newInstance(itemName, itemDueDate,
                priority, position, itemsAdapter);

        editItemDialog.show(fm, "fragment_edit_item");
    }

    private void readItemsFromDB() {
        items =  (ArrayList)Item.getAll();
    }

    private void writeAllItemsToDB() {
        Item.saveAllItems(items);
    }

    private void deleteItemFromDB(int position) {
        Item.deleteItem(position);
    }

    public void addNewItem(View view) {
        Item newItem = new Item();
        newItem.name = etNewItem.getText().toString();
        newItem.position = items.size();
        newItem.dueDate = null;
        newItem.save();

        itemsAdapter.add(newItem);
        etNewItem.setText("");
    }

    // ActivityOne.java, time to handle the result of the sub-activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            String text = data.getExtras().getString("ItemText");
            String duedate = data.getExtras().getString("ItemDueDate");
            Log.i("POSITION", "** editItemPosition: " + editItemPosition);

            Item i = items.get(editItemPosition);
            i.name = text;

            if (data.getExtras().getString("ItemDueDate") != null &&
                    !data.getExtras().getString("ItemDueDate").equals("null") &&
                    !data.getExtras().getString("ItemDueDate").isEmpty()) {
                i.dueDate = Item.setDateFromString(duedate);
            } else {
                i.dueDate = null;
            }
            i.save();
            itemsAdapter.notifyDataSetChanged();
        }
    }
}
