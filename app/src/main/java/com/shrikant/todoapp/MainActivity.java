package com.shrikant.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText etNewItem;
    private List<String> items;
    private ArrayAdapter<String> itemsAdaptor;
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

        itemsAdaptor = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdaptor);

        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                items.remove(position);
                itemsAdaptor.notifyDataSetChanged();
                deleteItemFromDB();
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
        i.putExtra("ItemText", items.get(position));
        startActivityForResult(i, REQUEST_CODE); // brings up the second activity
    }

    private void readItemsFromDB() {
        items = Item.getAllNames();
    }

    private void writeAllItemsToDB() {
        int pos = 0;
        for (String i : items) {
            Item item = new Item();
            item.name = i;
            item.position = pos++;
            item.save();
        }
    }

    private void deleteItemFromDB() {
        Item.deleteAllItems();
        writeAllItemsToDB();
    }

    public void addNewItem(View view) {
        itemsAdaptor.add(etNewItem.getText().toString());
        etNewItem.setText("");
        writeAllItemsToDB();
    }

    // ActivityOne.java, time to handle the result of the sub-activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            String text = data.getExtras().getString("ItemText");
            Log.i("POSITION", "** editItemPosition: " + editItemPosition);
            items.set(editItemPosition, text);
            itemsAdaptor.notifyDataSetChanged();
            writeAllItemsToDB();
        }
    }
}
