package com.example.keelan542.coffeelog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.keelan542.coffeelog.data.CoffeeContract.CoffeeEntry;

public class EditorActivity extends AppCompatActivity {

    // Method spinner
    private Spinner mMethodSpinner;

    // Extraction spinner
    private Spinner mExtractionSpinner;

    // Extraction value
    private int mExtraction = 0;

    // Method value
    private String mMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mMethodSpinner = (Spinner) findViewById(R.id.method_spinner);
        mExtractionSpinner = (Spinner) findViewById(R.id.extraction_spinner);

        setupSpinners();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    public void setupSpinners() {
        // Create ArrayAdapters using method_options array and extraction_options array and default spinner
        ArrayAdapter<CharSequence> adapterMethod = ArrayAdapter.createFromResource(this,
                R.array.method_options, android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> adapterExtraction = ArrayAdapter.createFromResource(this,
                R.array.extraction_options, android.R.layout.simple_spinner_item);

        // Specifiy layout to use when list of choices appear
        adapterMethod.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        adapterExtraction.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply adapters to spinners
        mMethodSpinner.setAdapter(adapterMethod);
        mExtractionSpinner.setAdapter(adapterExtraction);

        // Set mExtraction to selection in mExtractionSpinner
        mExtractionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                String[] array = getResources().getStringArray(R.array.extraction_options);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(array[0])) {
                        mExtraction = CoffeeEntry.EXTRACTION_UNDER;
                    } else if (selection.equals(array[1])) {
                        mExtraction = CoffeeEntry.EXTRACTION_BALANCED;
                    } else {
                        mExtraction = CoffeeEntry.EXTRACTION_OVER;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Set mMethod to selection in mMethodSpinner
        mMethodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mMethod = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
