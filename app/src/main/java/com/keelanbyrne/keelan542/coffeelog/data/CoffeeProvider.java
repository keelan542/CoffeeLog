package com.keelanbyrne.keelan542.coffeelog.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.keelanbyrne.keelan542.coffeelog.data.CoffeeContract.CoffeeEntry;

import static android.R.attr.id;

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

    // Static initialiser. This is called the first time anything is run from this class
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

        // Set notification on the cursor
        // so we know what content uri was created for.
        // If the data at this uri changes, then we know to update cursor
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case COFFEE:
                return insertEntry(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    // Method to insert entry with given content values into database. Return the
    // new content uri for that specific row in the database
    private Uri insertEntry(Uri uri, ContentValues values) {

        // Get writable database
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Insert the new entry with given values
        long rowId = db.insert(CoffeeEntry.TABLE_NAME, null, values);

        // If the id is -1, then the insertion failed
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Notify all listeners that data has changed for content uri
        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, rowId);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case COFFEE:
                return updateEntry(uri, values, selection, selectionArgs);
            case COFFEE_ID:
                // Extract id out of uri so we know which row to update
                selection = CoffeeEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateEntry(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    // Method to update entry in database with given content values, if there
    // is any. Returns the number of rows updated.
    private int updateEntry(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        // Check if content values size is 0. If so, return early as
        // no update necessary
        if (values.size() == 0) {
            return 0;
        }

        // Get writable database
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Update the entry with given values
        int rowsUpdated = db.update(CoffeeEntry.TABLE_NAME, values, selection, selectionArgs);

        // If rows updated is not 0, notify listeners that data at given uri
        // has changed.
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case COFFEE:
                return deleteEntry(uri, selection, selectionArgs);
            case COFFEE_ID:
                selection = CoffeeEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                return deleteEntry(uri, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
    }

    // Method to delete entry or all entries with given content values.
    // Returns the number of rows deleted.
    private int deleteEntry(Uri uri, String selection, String[] selectionArgs) {

        // Get writable database
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Perform delete operation
        int rowsDeleted = db.delete(CoffeeEntry.TABLE_NAME, selection, selectionArgs);

        // If more than 0 rows deleted, notify listeners that data
        // at given uri has changed.
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    // Returns the MIME type of data for the content uri
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case COFFEE:
                return CoffeeEntry.CONTENT_LIST_TYPE;
            case COFFEE_ID:
                return CoffeeEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown uri " + uri + " with match " + match);
        }
    }
}
