package com.keelanbyrne.keelan542.coffeelog;

import android.app.LoaderManager;
import android.content.ContentUris;
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

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, CoffeeRecyclerAdapter.OnItemClickListener {

    // Loader id
    private static final int LOADER_ID = 1;

    // RecylerView
    private RecyclerView mRecyclerView;

    // RecyclerView adapter
    private CoffeeRecyclerAdapter mRecyclerAdapter;

    // Current entry uri
    private Uri mCurrentEntryUri;

    // Emtpy view
    private TextView mEmptyView;

    // Cursor
    private Cursor mCursor;

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
        mEmptyView.setVisibility(View.VISIBLE);

        // Find RecyclerView and set layout manager
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

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

        mCursor = data;

        // Make empty view invisible if no data
        if (data.getCount() == 0) {
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            mEmptyView.setVisibility(View.INVISIBLE);
        }

        // Create instance of CoffeeRecyclerAdapter
        mRecyclerAdapter = new CoffeeRecyclerAdapter(this, data);

        // Set adapter on recycler view
        mRecyclerView.setAdapter(mRecyclerAdapter);

        // Set click listener on adapter
        mRecyclerAdapter.setItemClickListener(this);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}

    @Override
    public void onListItemClick(View view, int position) {

        mCursor.moveToPosition(position);

        // Form content uri with appended id of entry that was clicked on
        mCurrentEntryUri = ContentUris.withAppendedId(CoffeeEntry.CONTENT_URI, mCursor.getInt(mCursor.getColumnIndex(CoffeeEntry._ID)));

        // Create new intent to got to EditorActivity
        Intent intent = new Intent(MainActivity.this, EditorActivity.class);

        // Set mCurrentEntryUri as data of intent
        intent.setData(mCurrentEntryUri);

        // Launch Editor activity to display data of selected entry
        startActivity(intent);
    }
}
