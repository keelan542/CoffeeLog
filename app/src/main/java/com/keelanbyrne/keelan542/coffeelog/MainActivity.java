package com.keelanbyrne.keelan542.coffeelog;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements CoffeeRecyclerAdapter.OnItemClickListener {

    public static final int NEW_COFFEE_ACTIVITY_REQUEST_CODE = 1;

    private CoffeeViewModel coffeeViewModel;

    // RecylerView
    private RecyclerView mRecyclerView;

    // RecyclerView adapter
    private CoffeeRecyclerAdapter mRecyclerAdapter;

    // Emtpy view
    private TextView mEmptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        coffeeViewModel = ViewModelProviders.of(this).get(CoffeeViewModel.class);

        // Setup FAB to open editor activity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.FAB);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivityForResult(intent, NEW_COFFEE_ACTIVITY_REQUEST_CODE);
            }
        });

        // Get reference to empty view
        mEmptyView = (TextView) findViewById(R.id.empty_view);
        mEmptyView.setVisibility(View.VISIBLE);

        // Find RecyclerView and set layout manager
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        linearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_COFFEE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Coffee coffee = (Coffee) data.getSerializableExtra(EditorActivity.EXTRA_REPLY);
            coffeeViewModel.insert(coffee);
        }
    }

    /*
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

        // Make empty view visible if no data
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
    public void onLoaderReset(Loader<Cursor> loader) {
    }
    */

    @Override
    public void onListItemClick(View view, int position) {
    }
}
