package com.shrikant.todoapp;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Shrikant Pandhare on 11/26/15.
 */
@Table(name = "Items")
public class Item extends Model {
    @Column(name = "position", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public long position;

    @Column(name = "Name")
    public String name;

    @Column(name = "Notes")
    public String notes;

    @Column(name = "Priority")
    public String priority;

    @Column(name = "Status")
    public String status;

    @Column(name = "DueDate", index = true)
    public Date dueDate;

    // Make sure to have a default constructor for every ActiveAndroid model
    public Item(){
        super();
    }

    public Item(long position, String name, String notes, String priority, String status) {
        this.position = position;
        this.name = name;
        this.notes = notes;
        this.priority = priority;
        this.status = status;
    }

    public static List<Item> getAll() {
        // This is how you execute a query

        return new Select()
                .from(Item.class)
                .execute();
    }

    public static List<String> getAllNames() {
        // This is how you execute a query
        List<Item> litems =    new Select()
                .from(Item.class)
                .execute();

        List<String> itemNames = new ArrayList<>();
        for (Item l : litems) {
            itemNames.add(l.name);
        }

        return itemNames;
    }

    public static void deleteItem(int position) {
        new Delete().from(Item.class).where("position = ?", position).execute();
        adjustPositions(position);
    }

    public static void deleteAllItems() {
        new Delete().from(Item.class).execute();
    }

    public static void saveAllItems(List<Item> items) {
        ActiveAndroid.beginTransaction();
        try {
            for (Item i : items) {
                Item item = i;
                item.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        }
        finally {
            ActiveAndroid.endTransaction();
        }
    }

    private static void adjustPositions(int position) {
        List<Item> items = new Select()
                    .from(Item.class)
                    .where("position > ?", position)
                    .execute();

        for (Item i : items) {
            i.position = position++;
            i.save();
        }
    }

    public static Date setDateFromString(String date) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd");
        sf.setLenient(true);
        Date d = null;

        try {
            d = sf.parse(date);
        } catch (ParseException e) {

        }
        return d;
    }
}
