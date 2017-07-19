package com.example.keelan542.coffeelog;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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

    // Coffee used edit text
    private EditText mCoffeeUsedEditText;

    // Yield edit text
    private EditText mYieldEditText;

    // Minutes edit text
    private EditText mMinutesEditText;

    // Seconds edit text
    private EditText mSecondsEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mMethodSpinner = (Spinner) findViewById(R.id.method_spinner);
        mExtractionSpinner = (Spinner) findViewById(R.id.extraction_spinner);
        mCoffeeUsedEditText = (EditText) findViewById(R.id.coffee_used_edit_text);
        mYieldEditText = (EditText) findViewById(R.id.water_used_edit_text);
        mMinutesEditText = (EditText) findViewById(R.id.time_mins_edit_text);
        mSecondsEditText = (EditText) findViewById(R.id.time_sec_edit_text);

        setupSpinners();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar
        switch (item.getItemId()) {
            // Respond to a click on the save button
            case R.id.action_save:
                saveEntry();
                finish();
                return true;
            // Respond to a click on the delete button
            case R.id.action_delete:
                return true;
            // Respond to a click on up arrow
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                return true;
        }
        return super.onOptionsItemSelected(item);
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

    public void saveEntry() {

    }
}
