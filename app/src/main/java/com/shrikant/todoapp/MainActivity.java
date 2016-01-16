package com.shrikant.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

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
        // first parameter is the context, second is the class of the activity to launch
        Intent i = new Intent(this, EditItemActivity.class);
        //Intent i = new Intent(this, EditItemExtended.class);
        //i.putExtra("ItemText", strItems.get(position));
        i.putExtra("ItemText", items.get(position).name);
        startActivityForResult(i, REQUEST_CODE); // brings up the second activity
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
            Log.i("POSITION", "** editItemPosition: " + editItemPosition);

            Item i = items.get(editItemPosition);
            i.name = text;
            i.save();
            itemsAdapter.notifyDataSetChanged();
        }
    }
}
