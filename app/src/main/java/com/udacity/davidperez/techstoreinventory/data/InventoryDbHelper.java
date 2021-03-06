package com.udacity.davidperez.techstoreinventory.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by David on 23/08/2018.
 */

public class InventoryDbHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = InventoryDbHelper.class.getSimpleName();

    // Name of the database file
    private static final String DATABASE_NAME = "inventory.db";

    //Database version
    private static final int DATABASE_VERSION = 1;


    public InventoryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the pets table
        String SQL_CREATE_INVENTORY_TABLE =  "CREATE TABLE " + InventoryContract.NewProduct.TABLE_NAME + " ("
                + InventoryContract.NewProduct._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + InventoryContract.NewProduct.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, "
                + InventoryContract.NewProduct.COLUMN_PRICE + " INTEGER NOT NULL, "
                + InventoryContract.NewProduct.COLUMN_QUANTITY + " INTEGER NOT NULL, "
                + InventoryContract.NewProduct.COLUMN_SUPPLIER_NAME + " TEXT NOT NULL, "
                + InventoryContract.NewProduct.COLUMN_SUPPLIER_PHONE_NUMBER + " INTEGER NOT NULL);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_INVENTORY_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }
}
