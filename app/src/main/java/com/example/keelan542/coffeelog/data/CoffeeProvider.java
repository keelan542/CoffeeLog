package com.example.keelan542.coffeelog.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.example.keelan542.coffeelog.data.CoffeeContract.CoffeeEntry;

/**
 * Created by keelan542 on 20/07/2017.
 */

public class CoffeeProvider extends ContentProvider {

    // Tag for log messages
    public static final String LOG_TAG = CoffeeProvider.class.getSimpleName();

    // Instance of CoffeeSQLiteOpenHelper
    private CoffeeSQLiteOpenHelper mDbHelper;

    // URI matcher code for the content uri for the whole table
    private static final int COFFEE = 100;

    // URI matcher code for the content uri for a single entry in table
    private static final int COFFEE_ID = 101;

    // UriMatcher object to match a content uri to a corresponding code.
    // Input passed into the constructor represents the code to return for the root uri
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    /// Static initialiser. This is called the first time anything is run from this class
    static {
        // Content uri patterns that the provider should recognise
        sUriMatcher.addURI(CoffeeContract.CONTENT_AUTHORITY, CoffeeContract.PATH_COFFEE, COFFEE);
        sUriMatcher.addURI(CoffeeContract.CONTENT_AUTHORITY, CoffeeContract.PATH_COFFEE + "/#", COFFEE_ID);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new CoffeeSQLiteOpenHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        // Get readable database
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // This cursor will load the result of the query
        Cursor cursor;

        // Figure out if UriMatcher can match the uri to a specific integer code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case COFFEE:
                // Perform database query on coffee_log table
                cursor = db.query(CoffeeEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case COFFEE_ID:
                // Perform databse query on a single entry of coffee_log table
                selection = CoffeeEntry._ID + "=?";
                selectionArgs = new String[] {  String.valueOf(ContentUris.parseId(uri)) };
                cursor = db.query(CoffeeEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }
}
