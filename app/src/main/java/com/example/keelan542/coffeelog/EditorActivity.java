package com.example.keelan542.coffeelog;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.keelan542.coffeelog.data.CoffeeContract.CoffeeEntry;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class EditorActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

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

    // Ratio button
    private Button mRatioButton;

    // String to sore ratio
    private String mRatio;

    // TextView to display date
    private TextView mShowDate;

    // TextView to display ratio
    private TextView mShowRatio;

    // Current entry uri
    private Uri mCurrentEntryUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Get references to edit fields
        mMethodSpinner = (Spinner) findViewById(R.id.method_spinner);
        mExtractionSpinner = (Spinner) findViewById(R.id.extraction_spinner);
        mCoffeeUsedEditText = (EditText) findViewById(R.id.coffee_used_edit_text);
        mYieldEditText = (EditText) findViewById(R.id.water_used_edit_text);

        // Get references to minutes and seconds edit fields and set custom filter
        mMinutesEditText = (EditText) findViewById(R.id.time_mins_edit_text);
        mMinutesEditText.setFilters(new InputFilter[]{new InputFilterMinMax("0", "1000")});
        mSecondsEditText = (EditText) findViewById(R.id.time_sec_edit_text);
        mSecondsEditText.setFilters(new InputFilter[]{new InputFilterMinMax("0", "59")});

        // Get reference to TextViews that shows the selected date and ratio
        mShowDate = (TextView) findViewById(R.id.show_date);
        mShowRatio = (TextView) findViewById(R.id.show_ratio);

        setupSpinners();

        // Setup Calculate button to show ratio of brew
        mRatioButton = (Button) findViewById(R.id.show_ratio_button);
        mRatioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String coffeeUsedString = mCoffeeUsedEditText.getText().toString().trim();
                String yieldString = mYieldEditText.getText().toString().trim();
                if (!TextUtils.isEmpty(coffeeUsedString) && !TextUtils.isEmpty(yieldString)) {
                    double coffeeUsed = Double.parseDouble(coffeeUsedString);
                    double yield = Double.parseDouble(yieldString);
                    mRatio = String.valueOf(Math.round((yield / coffeeUsed) * 100.0)/100.0);
                    mShowRatio.setText("1:" + mRatio);
                }
            }
        });
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
                return true;
            // Respond to a click on the delete button
            case R.id.action_delete:
                deleteEntry();
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

        // Get text of edit fields and store in variables
        String coffeeUsed = mCoffeeUsedEditText.getText().toString().trim();
        String yield = mYieldEditText.getText().toString().trim();
        String minutesString = mMinutesEditText.getText().toString().trim();
        String secondsString = mSecondsEditText.getText().toString().trim();

        // Check if fields empty and if so
        // show a Toast rather than proceed
        if (TextUtils.isEmpty(coffeeUsed) ||
                TextUtils.isEmpty(yield) ||
                TextUtils.isEmpty(minutesString) ||
                TextUtils.isEmpty(secondsString) ||
                TextUtils.isEmpty(mShowDate.getText().toString())) {

            Toast.makeText(this, getString(R.string.fields_empty), Toast.LENGTH_SHORT).show();
        } else {
            // Convert time into total seconds and store in string
            int minutes = Integer.parseInt(minutesString);
            int seconds = Integer.parseInt(secondsString);
            int totalTimeInSeconds = (minutes * 60) + seconds;
            String timeString = String.valueOf(totalTimeInSeconds);
            String date = mShowDate.getText().toString();

            // New instance of ContentValues
            ContentValues values = new ContentValues();

            // Insert data into values
            values.put(CoffeeEntry.COLUMN_LOG_METHOD, mMethod);
            values.put(CoffeeEntry.COLUMN_LOG_COFFEE_AMOUNT, coffeeUsed);
            values.put(CoffeeEntry.COLUMN_LOG_YIELD, yield);
            values.put(CoffeeEntry.COLUMN_LOG_RATIO, mRatio);
            values.put(CoffeeEntry.COLUMN_LOG_TIME, timeString);
            values.put(CoffeeEntry.COLUMN_LOG_EXTRACTION, mExtraction);
            values.put(CoffeeEntry.COLUMN_LOG_DATE, date);

            // Insert values into coffee_log database
            Uri uri = getContentResolver().insert(CoffeeEntry.CONTENT_URI, values);

            // Display toast depending on whether insertion successful or not
            if (uri == null) {
                Toast.makeText(this, getString(R.string.insertion_successful), Toast.LENGTH_SHORT);
            } else {
                Toast.makeText(this, getString(R.string.insertion_failed), Toast.LENGTH_SHORT);
            }

            finish();
        }
    }

    private void deleteEntry() {

        int rowsDeleted = getContentResolver().delete(mCurrentEntryUri, null, null);

        if (rowsDeleted != 0) {
            Toast.makeText(this, getString(R.string.editor_delete_successful), Toast.LENGTH_SHORT);
        } else {
            Toast.makeText(this, getString(R.string.editor_delete_failed), Toast.LENGTH_SHORT).show();
        }
    }

    // Method to show DatePicker onClick of button
    public void datePicker(View view){
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.show(getFragmentManager(), "datePicker");
    }

    // Called once date is set, calls setDate()
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = new GregorianCalendar(year, month, dayOfMonth);
        setDate(calendar);
    }

    // Set selected date on TextView
    private void setDate(final Calendar calendar) {
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        mShowDate.setText(dateFormat.format(calendar.getTime()));
    }
}
