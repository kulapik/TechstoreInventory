package com.udacity.davidperez.techstoreinventory;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.udacity.davidperez.techstoreinventory.data.InventoryContract;
import com.udacity.davidperez.techstoreinventory.data.InventoryDbHelper;

import java.util.List;

public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int ITEM_LOADER = 0;
    private InventoryCursorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        ListView itemListView = findViewById(R.id.list);
        View emptyView = findViewById(R.id.empty_view);
        itemListView.setEmptyView(emptyView);

       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        mAdapter = new InventoryCursorAdapter(this, null);
        itemListView.setAdapter(mAdapter);

        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                Uri currentUri = ContentUris.withAppendedId(InventoryContract.NewProduct.CONTENT_URI, id);
                intent.setData(currentUri);
                startActivity(intent);
            }
        });

getLoaderManager().initLoader(ITEM_LOADER, null, this);    }

    private void addProduct() {
        // Gets the database in write mode
        ContentValues values = new ContentValues();
        values.put(InventoryContract.NewProduct.COLUMN_PRODUCT_NAME, "iPhone");
        values.put(InventoryContract.NewProduct.COLUMN_PRICE, 999);
        values.put(InventoryContract.NewProduct.COLUMN_QUANTITY, 100);
        values.put(InventoryContract.NewProduct.COLUMN_SUPPLIER_NAME, "Apple");
        values.put(InventoryContract.NewProduct.COLUMN_SUPPLIER_PHONE_NUMBER, "982159023");

getContentResolver().insert(InventoryContract.NewProduct.CONTENT_URI, values);}


    private void deleteAllItems() {
        int rowsDeleted = getContentResolver().delete(InventoryContract.NewProduct.CONTENT_URI, null, null);
        Log.v("CatalogActivity", rowsDeleted + "rows deleted from pet database");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                addProduct();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllItems();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {InventoryContract.NewProduct._ID, InventoryContract.NewProduct.COLUMN_PRODUCT_NAME, InventoryContract.NewProduct.COLUMN_PRICE, InventoryContract.NewProduct.COLUMN_QUANTITY};
        return new CursorLoader(this, InventoryContract.NewProduct.CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    mAdapter.swapCursor(null);
    }
}
