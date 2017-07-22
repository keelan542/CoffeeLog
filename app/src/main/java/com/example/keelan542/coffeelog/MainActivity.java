package com.example.keelan542.coffeelog;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.example.keelan542.coffeelog.data.CoffeeContract.CoffeeEntry;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    // Loader id
    private static final int LOADER_ID = 1;

    // Cursor adapter
    private CoffeeCursorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup FAB to open editor activity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.FAB);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        // Find listView
        ListView listView = (ListView) findViewById(R.id.list);

        // Create instance of CoffeeCursorAdapter
        mAdapter = new CoffeeCursorAdapter(this, null);

        // Set adapter on listView
        listView.setAdapter(mAdapter);

        // Initialise loader
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        // Create base uri
        Uri baseUri = CoffeeEntry.CONTENT_URI;

        // Create a custom loader that will take care
        // of creating a cursor for data being displayed
        String projection[] = {
                CoffeeEntry._ID,
                CoffeeEntry.COLUMN_LOG_METHOD,
                CoffeeEntry.COLUMN_LOG_DATE,
                CoffeeEntry.COLUMN_LOG_EXTRACTION};

        return new CursorLoader(this,
                baseUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Swap the new cursor in
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Called when last Cursor provided to onLoadFinished()
        // is about to be closed
        mAdapter.swapCursor(null);
    }
}
