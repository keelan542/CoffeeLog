package com.keelanbyrne.keelan542.coffeelog;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private CoffeeViewModel coffeeViewModel;

    // RecylerView
    private RecyclerView recyclerView;

    // RecyclerView adapter
    private CoffeeRecyclerAdapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        coffeeViewModel = ViewModelProviders.of(this).get(CoffeeViewModel.class);

        // Find RecyclerView and set layout manager
        recyclerAdapter = new CoffeeRecyclerAdapter(this);
        recyclerView = (RecyclerView) findViewById(R.id.rv_list);
        recyclerView.setAdapter(recyclerAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        coffeeViewModel.getAllCoffee().observe(this, new Observer<List<Coffee>>() {
            @Override
            public void onChanged(@Nullable List<Coffee> coffees) {
                recyclerAdapter.setCoffees(coffees);
            }
        });

        // Setup FAB to open editor activity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.FAB);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        // Snackbar to prompt user to make entry
        Snackbar.make(findViewById(R.id.FAB), "Tap the plus button to make an entry!", Snackbar.LENGTH_SHORT).show();
    }


}
