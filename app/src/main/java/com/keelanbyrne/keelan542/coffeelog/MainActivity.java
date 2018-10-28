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

public class MainActivity extends AppCompatActivity {

    public static final int NEW_COFFEE_ACTIVITY_REQUEST_CODE = 1;

    private CoffeeViewModel coffeeViewModel;

    // RecylerView
    private RecyclerView recyclerView;

    // RecyclerView adapter
    private CoffeeRecyclerAdapter recyclerAdapter;

    // Emtpy view
    private TextView emptyView;

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
        emptyView = (TextView) findViewById(R.id.empty_view);

        // Find RecyclerView and set layout manager
        recyclerAdapter = new CoffeeRecyclerAdapter(this);
        recyclerView = (RecyclerView) findViewById(R.id.rv_list);
        recyclerView.setAdapter(recyclerAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        coffeeViewModel.getAllWords().observe(this, new Observer<List<Coffee>>() {
            @Override
            public void onChanged(@Nullable List<Coffee> coffees) {
                recyclerAdapter.setCoffees(coffees);
            }
        });
    }
}
