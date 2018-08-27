package com.udacity.davidperez.techstoreinventory.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by David on 23/08/2018.
 */

public final class InventoryContract {
    private InventoryContract() {
    }
    public static final String CONTENT_AUTHORITY = "com.udacity.davidperez.techstoreinventory";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_ITEM = "inventory";

    public static final class NewProduct implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ITEM);
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEM;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEM;

        // Name of database table
        public final static String TABLE_NAME = "inventory";

        //ID for the item
        public final static String _ID = BaseColumns._ID;

        //Name of the product, TEXT
        public final static String COLUMN_PRODUCT_NAME = "name";

        //Price of the item, INTEGER
        public final static String COLUMN_PRICE = "price";

        //Number of items available, INTEGER
        public final static String COLUMN_QUANTITY = "quantity";

        public final static String COLUMN_SUPPLIER_NAME = "supplier_name";

        public final static String COLUMN_SUPPLIER_PHONE_NUMBER = "phone_number";


    }
}
