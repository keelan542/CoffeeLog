package com.keelanbyrne.keelan542.coffeelog;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
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

    private CoffeeViewModel coffeeViewModel;

    // Method spinner
    private Spinner methodSpinner;

    // Extraction spinner
    private Spinner extractionSpinner;

    // Extraction value
    private int extraction = 0;

    // Method value
    private int method = 0;

    // Coffee used edit text
    private EditText coffeeUsedEditText;

    // Yield edit text
    private EditText yieldEditText;

    // Minutes edit text
    private EditText minutesEditText;

    // Seconds edit text
    private EditText secondsEditText;

    // Comments edit text
    private EditText commentsEditText;

    // Ratio button
    private Button ratioButton;

    // Date button
    private Button dateButton;

    // String to sore ratio
    private String ratio;

    // TextView to display date
    private TextView showDate;

    // TextView to display ratio
    private TextView showRatio;

    // Current entry uri
    private Coffee itemClicked;

    // Boolean to check whether entry has
    // been edited, and if so, trigger AlertDialog.
    private boolean entryHasChanged;

    // Touch listener to check whether product has been edited
    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            entryHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        itemClicked = (Coffee)getIntent().getSerializableExtra(getString(R.string.item_clicked_id_extra_tag));

        coffeeViewModel = ViewModelProviders.of(this).get(CoffeeViewModel.class);

        // Get references to edit fields
        methodSpinner = (Spinner) findViewById(R.id.method_spinner);
        extractionSpinner = (Spinner) findViewById(R.id.extraction_spinner);
        coffeeUsedEditText = (EditText) findViewById(R.id.coffee_used_edit_text);
        yieldEditText = (EditText) findViewById(R.id.water_used_edit_text);
        commentsEditText = (EditText) findViewById(R.id.comment_edit_text);

        // Get references to minutes and seconds edit fields and set custom filter
        minutesEditText = (EditText) findViewById(R.id.time_mins_edit_text);
        minutesEditText.setFilters(new InputFilter[]{new InputFilterMinMax("0", "1000")});
        secondsEditText = (EditText) findViewById(R.id.time_sec_edit_text);
        secondsEditText.setFilters(new InputFilter[]{new InputFilterMinMax("0", "59")});

        // Get reference to TextViews and buttons that shows the selected date and ratio
        showDate = (TextView) findViewById(R.id.show_date);
        showRatio = (TextView) findViewById(R.id.show_ratio);
        dateButton = (Button) findViewById(R.id.pick_date_button);
        ratioButton = (Button) findViewById(R.id.show_ratio_button);

        // Set touchListener on edit fields
        methodSpinner.setOnTouchListener(touchListener);
        extractionSpinner.setOnTouchListener(touchListener);
        coffeeUsedEditText.setOnTouchListener(touchListener);
        yieldEditText.setOnTouchListener(touchListener);
        ratioButton.setOnTouchListener(touchListener);
        minutesEditText.setOnTouchListener(touchListener);
        secondsEditText.setOnTouchListener(touchListener);
        dateButton.setOnTouchListener(touchListener);
        commentsEditText.setOnTouchListener(touchListener);

        setupSpinners();

        // Setup Calculate button to show ratio of brew
        ratioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String coffeeUsed = coffeeUsedEditText.getText().toString().trim();
                String yield = yieldEditText.getText().toString().trim();
                if (!TextUtils.isEmpty(coffeeUsed) && !TextUtils.isEmpty(yield)) {
                    setRatio(coffeeUsed, yield);
                }
            }
        });

        // Check whether mCurrentEntryUri is null.
        // If null, proceed with adding new entry, if not,
        // initialise loader to populate fields with data of entry
        // that was clicked on.
        if (itemClicked == null) {
            setTitle(getString(R.string.title_add_entry));
        } else {
            setTitle(getString(R.string.title_edit_entry));
            populateFields();
        }
    }

    private void populateFields() {
        // Update editor fields and spinners with
        // values from database
        int method = itemClicked.getMethod();
        String coffeeUsed = itemClicked.getCoffeeUsed();
        String yield = itemClicked.getYield();
        String ratio = itemClicked.getRatio();
        String timeString = itemClicked.getTime();
        int extraction = itemClicked.getExtraction();
        String date = itemClicked.getDate();
        String comments;
        if (!TextUtils.isEmpty(itemClicked.getComment())) {
            comments = itemClicked.getComment();
            commentsEditText.setText(comments);
        }

        // Seperate timeString into minutes and seconds
        int minutes = Integer.parseInt(timeString) / 60;
        int seconds = Integer.parseInt(timeString) % 60;

        // Set values from cursor on fields
        methodSpinner.setSelection(method);
        coffeeUsedEditText.setText(coffeeUsed);
        yieldEditText.setText(yield);
        showRatio.setText("1:" + ratio);
        minutesEditText.setText(minutes + "");
        secondsEditText.setText(seconds + "");
        extractionSpinner.setSelection(extraction);
        showDate.setText(date);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // Hide delete button if in Add entry mode
        if (itemClicked == null) {
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
                if (!entryHasChanged) {
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
        if (!entryHasChanged) {
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
        methodSpinner.setAdapter(adapterMethod);
        extractionSpinner.setAdapter(adapterExtraction);

        // Set extraction to selection in extractionSpinner
        extractionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                String[] array = getResources().getStringArray(R.array.extraction_options);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(array[0])) {
                        extraction = 0;
                    } else if (selection.equals(array[1])) {
                        extraction = 1;
                    } else {
                        extraction = 2;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Set method to selection in methodSpinner
        methodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                String[] array = getResources().getStringArray(R.array.method_options);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(array[0])) {
                        method = 0;
                    } else if (selection.equals(array[1])) {
                        method = 1;
                    } else if (selection.equals(array[2])) {
                        method = 2;
                    } else {
                        method = 3;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    public void saveEntry() {

        // Get text of edit fields and store in variables
        String coffeeUsed = coffeeUsedEditText.getText().toString().trim();
        String yield = yieldEditText.getText().toString().trim();
        String minutesString = minutesEditText.getText().toString().trim();
        String secondsString = secondsEditText.getText().toString().trim();
        String comments = commentsEditText.getText().toString().trim();

        // Check if fields empty and if so show a Toast rather than proceed
        // or show error on edit text fields
        if (TextUtils.isEmpty(coffeeUsed)) {
            coffeeUsedEditText.setError(getString(R.string.blank_field_error_message));
        }else if (TextUtils.isEmpty(yield)) {
            yieldEditText.setError(getString(R.string.blank_field_error_message));
        } else if (TextUtils.isEmpty(minutesString)) {
            minutesEditText.setError(getString(R.string.blank_field_error_message));
        } else if (TextUtils.isEmpty(secondsString)) {
            secondsEditText.setError(getString(R.string.blank_field_error_message));
        } else if (TextUtils.isEmpty(showDate.getText().toString())) {
            Toast.makeText(this, getString(R.string.date_not_picked), Toast.LENGTH_SHORT).show();
        } else {

            // Convert time into total seconds and store in string
            int minutes = Integer.parseInt(minutesString);
            int seconds = Integer.parseInt(secondsString);
            int totalTimeInSeconds = (minutes * 60) + seconds;
            String timeString = String.valueOf(totalTimeInSeconds);
            String date = showDate.getText().toString();

            // Set ratio if mCurrentEntryUri is not null
            // as button may not have been clicked
            if (!TextUtils.isEmpty(coffeeUsed) && !TextUtils.isEmpty(yield)) {
                setRatio(coffeeUsed, yield);
            }

            // Add comments to values if not empty
            if (!TextUtils.isEmpty(comments)) {
                comments = "";
            }

            Coffee coffee = new Coffee(method, coffeeUsed, yield, ratio, timeString, extraction, date, comments);
            if (itemClicked == null) {
                coffeeViewModel.insert(coffee);
            } else {
                coffeeViewModel.update(coffee);
            }

            finish();
        }
    }

    private void deleteEntry() {

        if (itemClicked  != null) {
            coffeeViewModel.delete(itemClicked);
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
        showDate.setText(dateFormat.format(calendar.getTime()));
    }

    // Method to calculate and return ratio
    private void setRatio(String coffeeUsed, String yield) {
        double coffeeUsedDouble = Double.parseDouble(coffeeUsed);
        double yieldDouble = Double.parseDouble(yield);
        ratio = String.valueOf(Math.round((yieldDouble / coffeeUsedDouble) * 100.0) / 100.0);
        showRatio.setText("1:" + ratio);
    }
}
