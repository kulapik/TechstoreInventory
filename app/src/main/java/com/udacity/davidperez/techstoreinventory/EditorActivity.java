package com.udacity.davidperez.techstoreinventory;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.udacity.davidperez.techstoreinventory.data.InventoryContract;

/**
 * Created by David on 23/08/2018.
 */

public class EditorActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks <Cursor> {

    private Uri mCurrentUri;

    private static final int ITEM_LOADER = 0;

    private EditText mNameEditText;

    private EditText mPriceEditText;

    private TextView mQuantityTextView;

    private EditText mSupplierNameEditText;

    private EditText mSupplierPhoneNumberEditText;

    private Button incrementButton;

    private Button decrementButton;

    private boolean mWasEdited = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mWasEdited = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        Intent intent = getIntent();
        mCurrentUri = intent.getData();

        if (mCurrentUri == null) {
            setTitle("Add a new product");
            invalidateOptionsMenu();
        } else {
            setTitle("Edit product");
            getLoaderManager().initLoader(ITEM_LOADER, null, this);
        }

        mNameEditText = (EditText) findViewById(R.id.edit_name);
        mPriceEditText = (EditText) findViewById(R.id.edit_price);
        mQuantityTextView = findViewById(R.id.quantity);
        mSupplierNameEditText = (EditText) findViewById(R.id.edit_supplier_name);
        mSupplierPhoneNumberEditText = (EditText) findViewById(R.id.edit_supplier_phone_number);
        incrementButton = (Button) findViewById(R.id.increment);
        decrementButton = (Button) findViewById(R.id.decrement);

        mNameEditText.setOnTouchListener(mTouchListener);
        mPriceEditText.setOnTouchListener(mTouchListener);
        mQuantityTextView.setOnTouchListener(mTouchListener);
        mSupplierNameEditText.setOnTouchListener(mTouchListener);
        mSupplierPhoneNumberEditText.setOnTouchListener(mTouchListener);

        int quantity = 1;
        mQuantityTextView.setText(String.valueOf(quantity));
        incrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQuantity = Integer.parseInt(mQuantityTextView.getText().toString().trim());
                mQuantityTextView.setText(String.valueOf(currentQuantity + 1));
                if (currentQuantity >= 0) {
                    decrementButton.setEnabled(true);
                }

            }
        });
        decrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQuantity = Integer.parseInt(mQuantityTextView.getText().toString().trim());
                mQuantityTextView.setText(String.valueOf(currentQuantity - 1));
                if (currentQuantity <= 1) {
                    decrementButton.setEnabled(false);
                }
            }
        });

    }
    private void orderFromSupplier () {
        String currentPhoneNumber = mSupplierPhoneNumberEditText.getText().toString();
        String uri = "tel:" + currentPhoneNumber;
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(uri));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

    }

    private void addProduct() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String nameString = mNameEditText.getText().toString().trim();
        String priceString = mPriceEditText.getText().toString().trim();
        int price = 0;
        String quantityString = mQuantityTextView.getText().toString().trim();
        int quantity = 0;
        String supplierNameString = mSupplierNameEditText.getText().toString().trim();
        long phoneNumber = 0;
        String supplierPhoneNumberString = mSupplierPhoneNumberEditText.getText().toString().trim();

        if (TextUtils.isEmpty(nameString) || TextUtils.isEmpty(priceString)
                || TextUtils.isEmpty(quantityString) || TextUtils.isEmpty(supplierNameString) || TextUtils.isEmpty(supplierPhoneNumberString)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
        } else {

            // Create a ContentValues object where column names are the keys,
            // and pet attributes from the editor are the values.
            ContentValues values = new ContentValues();
            values.put(InventoryContract.NewProduct.COLUMN_PRODUCT_NAME, nameString);
            if (!TextUtils.isEmpty(priceString)) {
                price = Integer.parseInt(priceString);
            }
            values.put(InventoryContract.NewProduct.COLUMN_PRICE, price);
            if (!TextUtils.isEmpty(quantityString)) {
                quantity = Integer.parseInt(quantityString);
            }
            values.put(InventoryContract.NewProduct.COLUMN_QUANTITY, quantity);
            values.put(InventoryContract.NewProduct.COLUMN_SUPPLIER_NAME, supplierNameString);
            if (!TextUtils.isEmpty(supplierPhoneNumberString)) {
                phoneNumber = Long.parseLong(supplierPhoneNumberString);
            }
            values.put(InventoryContract.NewProduct.COLUMN_SUPPLIER_PHONE_NUMBER, phoneNumber);

            // Insert a new row for pet in the database, returning the ID of that new row.
            if (mCurrentUri == null) {
                Uri newUri = getContentResolver().insert(InventoryContract.NewProduct.CONTENT_URI, values);
                // Show a toast message depending on whether or not the insertion was successful
                if (newUri == null) {
                    // If the row ID is -1, then there was an error with insertion.
                    Toast.makeText(this, "Error with saving item", Toast.LENGTH_SHORT).show();
                } else {
                    // Otherwise, the insertion was successful and we can display a toast with the row ID.
                    Toast.makeText(this, "Item saved", Toast.LENGTH_SHORT).show();
                }
            } else {
                int rowsAffected = getContentResolver().update(mCurrentUri, values, null, null);
                if (rowsAffected == 0) {
                    Toast.makeText(this, "Error with updating item", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Item updated", Toast.LENGTH_SHORT).show();

                }
            }
            finish();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new pet, hide the "Delete" menu item.
        if (mCurrentUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save pet to database
                addProduct();
                // Exit activity
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            case R.id.order_from_provider:
                orderFromSupplier();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                if (!mWasEdited) {
                    NavUtils.navigateUpFromSameTask(this);
                    return true;
                }

                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);

                            }
                        };
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        // If the pet hasn't changed, continue with handling back button press
        if (!mWasEdited) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                InventoryContract.NewProduct._ID,
                InventoryContract.NewProduct.COLUMN_PRODUCT_NAME,
                InventoryContract.NewProduct.COLUMN_PRICE,
                InventoryContract.NewProduct.COLUMN_QUANTITY,
                InventoryContract.NewProduct.COLUMN_SUPPLIER_NAME,
                InventoryContract.NewProduct.COLUMN_SUPPLIER_PHONE_NUMBER };


        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mCurrentUri,         // Query the content URI for the current pet
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        if (cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(InventoryContract.NewProduct.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(InventoryContract.NewProduct.COLUMN_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(InventoryContract.NewProduct.COLUMN_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(InventoryContract.NewProduct.COLUMN_SUPPLIER_NAME);
            int supplierPhoneColumnIndex = cursor.getColumnIndex(InventoryContract.NewProduct.COLUMN_SUPPLIER_PHONE_NUMBER);

            String name = cursor.getString(nameColumnIndex);
            int price = cursor.getInt(priceColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);
            String supplierName = cursor.getString(supplierNameColumnIndex);
            long supplierPhone = cursor.getLong(supplierPhoneColumnIndex);

            mNameEditText.setText(name);
            mPriceEditText.setText(Integer.toString(price));
            mQuantityTextView.setText(Integer.toString(quantity));
            mSupplierNameEditText.setText(supplierName);
            mSupplierPhoneNumberEditText.setText(Long.toString(supplierPhone));
        }
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
        mNameEditText.setText("");
        mPriceEditText.setText("");
        mQuantityTextView.setText(String.valueOf(0));
        mSupplierNameEditText.setText("");
        mSupplierPhoneNumberEditText.setText("");
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteItem();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }  private void deleteItem() {
        if (mCurrentUri != null) {
                    int rowsDeleted = getContentResolver().delete(mCurrentUri, null, null);

            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, getString(R.string.editor_delete_item_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_delete_item_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        // Close the activity
        finish();
    }
}