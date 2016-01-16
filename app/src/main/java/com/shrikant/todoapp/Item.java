package com.shrikant.todoapp;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by spandhare on 11/26/15.
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
    }

    public static void deleteAllItems() {
        new Delete().from(Item.class).execute();
    }

}
