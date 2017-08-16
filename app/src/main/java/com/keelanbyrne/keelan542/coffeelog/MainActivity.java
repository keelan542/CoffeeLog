package com.keelanbyrne.keelan542.coffeelog;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.keelanbyrne.keelan542.coffeelog.data.CoffeeContract.CoffeeEntry;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    // Loader id
    private static final int LOADER_ID = 1;

    // RecylerView
    private RecyclerView mRecyclerView;

    // RecyclerView adapter
    private CoffeeRecyclerAdapter mRecyclerAdapter;

    // Cursor adapter
    private CoffeeCursorAdapter mAdapter;

    // Current entry uri
    private Uri mCurrentEntryUri;

    // Emtpy view
    private TextView mEmptyView;

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

        // Get reference to emtpy view
        mEmptyView = (TextView) findViewById(R.id.empty_view);

        // Find RecyclerView and set layout manager
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // Find listView and set empty view
        //ListView listView = (ListView) findViewById(R.id.list);
        //listView.setEmptyView(mEmptyView);

        // Create instance of CoffeeCursorAdapter
       // mAdapter = new CoffeeCursorAdapter(this, null);

        // Set adapter on listView
        //listView.setAdapter(mAdapter);

        /*
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Form content uri with appended id of entry that was clicked on
                mCurrentEntryUri = ContentUris.withAppendedId(CoffeeEntry.CONTENT_URI, id);

                // Create new intent to got to EditorActivity
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);

                // Set mCurrentEntryUri as data of intent
                intent.setData(mCurrentEntryUri);

                // Launch Editor activity to display data of selected entry
                startActivity(intent);
            }
        });
        */

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
                CoffeeEntry.COLUMN_LOG_EXTRACTION,
                CoffeeEntry.COLUMN_LOG_RATIO};

        return new CursorLoader(this,
                baseUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Create instance of CoffeeRecyclerAdapter
        mRecyclerAdapter = new CoffeeRecyclerAdapter(this, data);

        // Set adapter on recycler view
        mRecyclerView.setAdapter(mRecyclerAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Called when last Cursor provided to onLoadFinished()
        // is about to be closed
        //mAdapter.swapCursor(null);
    }
}
