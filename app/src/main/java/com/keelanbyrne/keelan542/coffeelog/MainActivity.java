package com.keelanbyrne.keelan542.coffeelog;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;

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
        mRecyclerAdapter = new CoffeeRecyclerAdapter(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        coffeeViewModel.getAllWords().observe(this, new Observer<List<Coffee>>() {
            @Override
            public void onChanged(@Nullable List<Coffee> coffees) {
                mRecyclerAdapter.setCoffees(coffees);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_COFFEE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Coffee coffee = (Coffee) data.getSerializableExtra(EditorActivity.EXTRA_REPLY);
            coffeeViewModel.insert(coffee);
        }
    }

    @Override
    public void onListItemClick(View view, int position) {
    }
}
