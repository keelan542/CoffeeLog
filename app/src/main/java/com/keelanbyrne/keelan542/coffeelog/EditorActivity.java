package com.keelanbyrne.keelan542.coffeelog;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class EditorActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public static final String EXTRA_REPLY = "com.example.android.coffeelistsql.REPLY";

    // Method spinner
    private Spinner mMethodSpinner;

    // Extraction spinner
    private Spinner mExtractionSpinner;

    // Extraction value
    private int mExtraction = 0;

    // Method value
    private int mMethod = 0;

    // Coffee used edit text
    private EditText mCoffeeUsedEditText;

    // Yield edit text
    private EditText mYieldEditText;

    // Minutes edit text
    private EditText mMinutesEditText;

    // Seconds edit text
    private EditText mSecondsEditText;

    // Comments edit text
    private EditText mCommentsEditText;

    // Ratio button
    private Button mRatioButton;

    // Date button
    private Button mDateButton;

    // String to sore ratio
    private String mRatio;

    // TextView to display date
    private TextView mShowDate;

    // TextView to display ratio
    private TextView mShowRatio;

    // Current entry uri
    private Uri mCurrentEntryUri;

    // Boolean to check whether entry has
    // been edited, and if so, trigger AlertDialog.
    private boolean mEntryHasChanged;

    // Touch listener to check whether product has been edited
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mEntryHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Get references to edit fields
        mMethodSpinner = (Spinner) findViewById(R.id.method_spinner);
        mExtractionSpinner = (Spinner) findViewById(R.id.extraction_spinner);
        mCoffeeUsedEditText = (EditText) findViewById(R.id.coffee_used_edit_text);
        mYieldEditText = (EditText) findViewById(R.id.water_used_edit_text);
        mCommentsEditText = (EditText) findViewById(R.id.comment_edit_text);

        // Get references to minutes and seconds edit fields and set custom filter
        mMinutesEditText = (EditText) findViewById(R.id.time_mins_edit_text);
        mMinutesEditText.setFilters(new InputFilter[]{new InputFilterMinMax("0", "1000")});
        mSecondsEditText = (EditText) findViewById(R.id.time_sec_edit_text);
        mSecondsEditText.setFilters(new InputFilter[]{new InputFilterMinMax("0", "59")});

        // Get reference to TextViews and buttons that shows the selected date and ratio
        mShowDate = (TextView) findViewById(R.id.show_date);
        mShowRatio = (TextView) findViewById(R.id.show_ratio);
        mDateButton = (Button) findViewById(R.id.pick_date_button);
        mRatioButton = (Button) findViewById(R.id.show_ratio_button);

        // Set mTouchListener on edit fields
        mMethodSpinner.setOnTouchListener(mTouchListener);
        mExtractionSpinner.setOnTouchListener(mTouchListener);
        mCoffeeUsedEditText.setOnTouchListener(mTouchListener);
        mYieldEditText.setOnTouchListener(mTouchListener);
        mRatioButton.setOnTouchListener(mTouchListener);
        mMinutesEditText.setOnTouchListener(mTouchListener);
        mSecondsEditText.setOnTouchListener(mTouchListener);
        mDateButton.setOnTouchListener(mTouchListener);
        mCommentsEditText.setOnTouchListener(mTouchListener);

        setupSpinners();

        // Setup Calculate button to show ratio of brew
        mRatioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String coffeeUsed = mCoffeeUsedEditText.getText().toString().trim();
                String yield = mYieldEditText.getText().toString().trim();
                if (!TextUtils.isEmpty(coffeeUsed) && !TextUtils.isEmpty(yield)) {
                    setRatio(coffeeUsed, yield);
                }
            }
        });

        // Check whether mCurrentEntryUri is null.
        // If null, proceed with adding new entry, if not,
        // initialise loader to populate fields with data of entry
        // that was clicked on.
        mCurrentEntryUri = getIntent().getData();
        if (mCurrentEntryUri == null) {
            setTitle(getString(R.string.title_add_entry));
        } else {
            setTitle(getString(R.string.title_edit_entry));
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // Hide delete button if in Add entry mode
        if (mCurrentEntryUri == null) {
            MenuItem item = menu.findItem(R.id.action_delete);
            item.setVisible(false);
        }

        return true;
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
                showDeleteConfirmationDialog();
                return true;
            // Respond to a click on up arrow
            case android.R.id.home:
                // If the product hasnt changed, continue navigating
                // up to the parent activity
                if (!mEntryHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }

                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // If the pet hasn't changed, continue with handling back button press
        if (!mEntryHasChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.discard_changes));
        builder.setPositiveButton(getString(R.string.discard), discardButtonClickListener);
        builder.setNegativeButton(getString(R.string.keep_editing), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.are_you_sure));
        builder.setPositiveButton(getString(R.string.delete_confirm), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deleteEntry();
                finish();
            }
        });
        builder.setNegativeButton(getString(R.string.cancel_delete), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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
                        mExtraction = 0;
                    } else if (selection.equals(array[1])) {
                        mExtraction = 1;
                    } else {
                        mExtraction = 2;
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
                String selection = (String) parent.getItemAtPosition(position);
                String[] array = getResources().getStringArray(R.array.method_options);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(array[0])) {
                        mMethod = 0;
                    } else if (selection.equals(array[1])) {
                        mMethod = 1;
                    } else if (selection.equals(array[2])) {
                        mMethod = 2;
                    } else {
                        mMethod = 3;
                    }
                }
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
        String comments = mCommentsEditText.getText().toString().trim();

        // Check if fields empty and if so show a Toast rather than proceed
        // or show error on edit text fields
        if (TextUtils.isEmpty(coffeeUsed)) {
            mCoffeeUsedEditText.setError(getString(R.string.blank_field_error_message));
        }else if (TextUtils.isEmpty(yield)) {
            mYieldEditText.setError(getString(R.string.blank_field_error_message));
        } else if (TextUtils.isEmpty(minutesString)) {
            mMinutesEditText.setError(getString(R.string.blank_field_error_message));
        } else if (TextUtils.isEmpty(secondsString)) {
            mSecondsEditText.setError(getString(R.string.blank_field_error_message));
        } else if (TextUtils.isEmpty(mShowDate.getText().toString())) {
            Toast.makeText(this, getString(R.string.date_not_picked), Toast.LENGTH_SHORT).show();
        } else {
            // Convert time into total seconds and store in string
            int minutes = Integer.parseInt(minutesString);
            int seconds = Integer.parseInt(secondsString);
            int totalTimeInSeconds = (minutes * 60) + seconds;
            String timeString = String.valueOf(totalTimeInSeconds);
            String date = mShowDate.getText().toString();

            // New instance of ContentValues
            ContentValues values = new ContentValues();

            // Set mRatio if mCurrentEntryUri is not null
            // as button may not have been clicked
            if (!TextUtils.isEmpty(coffeeUsed) && !TextUtils.isEmpty(yield)) {
                setRatio(coffeeUsed, yield);
            }

            // Add comments to values if not empty
            if (!TextUtils.isEmpty(comments)) {
                comments = "";
            }

            Coffee coffee = new Coffee(mMethod, coffeeUsed, yield, mRatio, timeString, mExtraction, date, comments);
            Intent replyIntent = new Intent();
            replyIntent.putExtra(EXTRA_REPLY, coffee);
            setResult(RESULT_OK, replyIntent);

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

        finish();
    }

    // Method to show DatePicker onClick of button
    public void datePicker(View view) {
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

    // Method to calculate and return ratio
    private void setRatio(String coffeeUsed, String yield) {
        double coffeeUsedDouble = Double.parseDouble(coffeeUsed);
        double yieldDouble = Double.parseDouble(yield);
        mRatio = String.valueOf(Math.round((yieldDouble / coffeeUsedDouble) * 100.0) / 100.0);
        mShowRatio.setText("1:" + mRatio);
    }

    /*
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Move cursor to 0th position
        // before extracting columns
        if (data.moveToFirst()) {

            // Update editor fields and spinners with
            // values from cursor
            int method = data.getInt(data.getColumnIndex(CoffeeEntry.COLUMN_LOG_METHOD));
            String coffeeUsed = data.getString(data.getColumnIndex(CoffeeEntry.COLUMN_LOG_COFFEE_AMOUNT));
            String yield = data.getString(data.getColumnIndex(CoffeeEntry.COLUMN_LOG_YIELD));
            String ratio = data.getString(data.getColumnIndex(CoffeeEntry.COLUMN_LOG_RATIO));
            String timeString = data.getString(data.getColumnIndex(CoffeeEntry.COLUMN_LOG_TIME));
            int extraction = data.getInt(data.getColumnIndex(CoffeeEntry.COLUMN_LOG_EXTRACTION));
            String date = data.getString(data.getColumnIndex(CoffeeEntry.COLUMN_LOG_DATE));
            String comments;
            if (!TextUtils.isEmpty((data.getString(data.getColumnIndex(CoffeeEntry.COLUMN_LOG_COMMENT))))) {
                comments = data.getString(data.getColumnIndex(CoffeeEntry.COLUMN_LOG_COMMENT));
                mCommentsEditText.setText(comments);
            }

            // Seperate timeString into minutes and seconds
            int minutes = Integer.parseInt(timeString) / 60;
            int seconds = Integer.parseInt(timeString) % 60;

            // Set values from cursor on fields
            mMethodSpinner.setSelection(method);
            mCoffeeUsedEditText.setText(coffeeUsed);
            mYieldEditText.setText(yield);
            mShowRatio.setText("1:" + ratio);
            mMinutesEditText.setText(minutes + "");
            mSecondsEditText.setText(seconds + "");
            mExtractionSpinner.setSelection(extraction);
            mShowDate.setText(date);
        }
    }
    */
}
