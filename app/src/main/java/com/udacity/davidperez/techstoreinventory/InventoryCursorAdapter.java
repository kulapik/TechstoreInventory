package com.udacity.davidperez.techstoreinventory;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.udacity.davidperez.techstoreinventory.data.InventoryContract;

/**
 * Created by David on 26/08/2018.
 */

public class InventoryCursorAdapter extends CursorAdapter {

    public InventoryCursorAdapter (Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        TextView nameTextView =  view.findViewById(R.id.name);
        TextView priceTextView = view.findViewById(R.id.price);
        final TextView quantityTextView = view.findViewById(R.id.quantity);
        final Button buyButton = view.findViewById(R.id.buy);


        int nameColumnIndex = cursor.getColumnIndex(InventoryContract.NewProduct.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(InventoryContract.NewProduct.COLUMN_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(InventoryContract.NewProduct.COLUMN_QUANTITY);
        int idColumnIndex = cursor.getColumnIndex(InventoryContract.NewProduct._ID);
        String name = cursor.getString(nameColumnIndex);
        int price = cursor.getInt(priceColumnIndex);
        final int quantity = cursor.getInt(quantityColumnIndex);
        String priceString = Integer.toString(price);
        final String quantityString = Integer.toString(quantity);
        final long id = cursor.getLong(idColumnIndex);
        nameTextView.setText(name);
        priceTextView.setText(priceString);
        quantityTextView.setText(quantityString);
        buyButton.setText(R.string.buy);
        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQuantity = Integer.parseInt(quantityString) - 1;
                if (currentQuantity >= 0) {
                    ContentValues values = new ContentValues();
                    values.put(InventoryContract.NewProduct.COLUMN_QUANTITY, currentQuantity);
                    String selection = InventoryContract.NewProduct._ID + "=?";
                    Uri currentUri = ContentUris.withAppendedId(InventoryContract.NewProduct.CONTENT_URI, id);
                    String[] selectionArgs = new String[]{String.valueOf(id)};
                    context.getContentResolver().update(currentUri, values, selection, selectionArgs);

                } else {
                    buyButton.setEnabled(false);
                }
            }
        });
    }

}
