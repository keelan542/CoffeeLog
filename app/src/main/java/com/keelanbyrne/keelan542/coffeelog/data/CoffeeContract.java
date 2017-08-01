package com.keelanbyrne.keelan542.coffeelog.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by keelan542 on 18/07/2017.
 */

public class CoffeeContract {

    // To prevent accidental instantiation of contract class
    private CoffeeContract() {}

    public static final String CONTENT_AUTHORITY = "com.keelanbyrne.keelan542.coffeelog";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_COFFEE = "coffee";

    // Inner class that defines the tables contents
    public static class CoffeeEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_COFFEE);

        // The MIME type of the CONTENT_URI for a list of entries
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COFFEE;

        // The MIME type of the CONTENT_URI for a single entry
        public static final String CONTENT_ITEM_TYPE =

                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_COFFEE;

        public static final String TABLE_NAME = "coffee_log";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_LOG_METHOD = "method";
        public static final String COLUMN_LOG_COFFEE_AMOUNT = "coffee_amount";
        public static final String COLUMN_LOG_YIELD = "yield";
        public static final String COLUMN_LOG_RATIO = "ratio";
        public static final String COLUMN_LOG_TIME = "time";
        public static final String COLUMN_LOG_EXTRACTION = "extraction";
        public static final String COLUMN_LOG_DATE = "date";
        public static final String COLUMN_LOG_COMMENT = "comment";

        // Different integer values for extraction
        public static final int EXTRACTION_UNDER = 0;
        public static final int EXTRACTION_BALANCED = 1;
        public static final int EXTRACTION_OVER = 2;

        // Different integer values for method
        public static final int METHOD_FRENCH_PRESS = 0;
        public static final int METHOD_AEROPRESS = 1;
        public static final int METHOD_POUR_OVER = 2;
        public static final int METHOD_MOKA_POT = 3;

        // Returns whether or not the given extraction is EXTRACTION_UNDER, EXTRACTION_BALANCED,
        // OR EXTRACTION_OVER
        public static boolean isValidExtraction(int extraction) {
            if (extraction == EXTRACTION_UNDER || extraction == EXTRACTION_BALANCED || extraction ==  EXTRACTION_OVER) {
                return true;
            }
            return false;
        }
    }
}
