/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.pets.ui;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.pets.R;
import com.example.android.pets.database.entity.Pet;
import com.example.android.pets.viewmodel.PetViewModel;

import java.util.List;
import java.util.Locale;

/**
 * Allows user to create a new pet or edit an existing one.
 */
public class EditorActivity extends AppCompatActivity /*implements LoaderManager.LoaderCallbacks<Cursor>*/ {
    //
    private static final String LOG_TAG = EditorActivity.class.getSimpleName();

    /** EditText field to enter the pet's name */
    private EditText mNameEditText;

    /** EditText field to enter the pet's breed */
    private EditText mBreedEditText;

    /** EditText field to enter the pet's weight */
    private EditText mWeightEditText;

    /** EditText field to enter the pet's gender */
    private Spinner mGenderSpinner;

    /**
     * Gender of the pet. The possible values are:
     * 0 for unknown gender, 1 for male, 2 for female.
     */
    private int mGender = 0;

    /** Content URI for the existing pet (null if it's a new pet) */
    private Uri mUriOfClickedPetFromReceivedIntent = null;

    private int idOfClickedPet = -1;

    //
    private PetViewModel petViewModel;

    //
    private final static int ID_OF_EDIT_OR_INSERT_PET_LOADER = 0;

    //
    private boolean mPetHasChanged = false;

    //
    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mPetHasChanged = true;
            // onTouch himself is not returning if the view is touched or not
            // return false to make touch effective
            return false;
        }
    };


    private void updateUi(Pet pet)
    {
        //
        // assign the values to the views
        mNameEditText.setText(pet.getName());
        mBreedEditText.setText(pet.getBreed());

        // Gender is a dropdown spinner, so map the constant value from the database
        // into one of the dropdown options (0 is Unknown, 1 is Male, 2 is Female).
        // Then call setSelection() so that option is displayed on screen as the current selection.
        switch (pet.getGender()) {
            case 1:
                mGenderSpinner.setSelection(1);
                break;
            case 2:
                mGenderSpinner.setSelection(2);
                break;
            default:
                mGenderSpinner.setSelection(0);
                break;
        }
        // set the weight
        mWeightEditText.setText(String.format(Locale.UK, "%d", pet.getWeight()));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent receivedIntent = super.getIntent();
        //this.mUriOfClickedPetFromReceivedIntent = receivedIntent.getData();

        // get the id of the pet
        this.idOfClickedPet = receivedIntent.getIntExtra("idOfClickedPet", -1);
        // change the title of the EditorActivity
        if (idOfClickedPet == -1) {
            this.setTitle(R.string.editor_activity_title_new_pet);

            // Invalidate the options menu, so the "Delete" menu option can be hidden.
            // (It doesn't make sense to delete a pet that hasn't been created yet.)
            invalidateOptionsMenu();
        }
        else {
            this.setTitle(R.string.editor_activity_title_old_pet);
        }

        // to test if the uri was passed correctly
        Log.v(LOG_TAG, "Uri from received intent is : " + this.idOfClickedPet);

        // Find all relevant views that we will need to read user input from
        mNameEditText = (EditText) findViewById(R.id.edit_pet_name);
        mBreedEditText = (EditText) findViewById(R.id.edit_pet_breed);
        mWeightEditText = (EditText) findViewById(R.id.edit_pet_weight);
        mGenderSpinner = (Spinner) findViewById(R.id.spinner_gender);

        setupSpinner();

        //
        //**getLoaderManager().initLoader(ID_OF_EDIT_OR_INSERT_PET_LOADER, null, this);

        //
        mNameEditText.setOnTouchListener(mOnTouchListener);
        mBreedEditText.setOnTouchListener(mOnTouchListener);
        mWeightEditText.setOnTouchListener(mOnTouchListener);
        mGenderSpinner.setOnTouchListener(mOnTouchListener);



        this.petViewModel = ViewModelProviders.of(this).get(PetViewModel.class);

        final Observer<Pet> petObserver = new Observer<Pet>()
        {
            @Override
            public void onChanged(@Nullable Pet pet) {
                updateUi(pet);
            }
        };

        petViewModel.getPet(this.idOfClickedPet).observe(this, petObserver);

    }

    /**
     * Setup the dropdown spinner that allows the user to select the gender of the pet.
     */
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mGenderSpinner.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.gender_male))) {
                        mGender = 1;// Male
                    } else if (selection.equals(getString(R.string.gender_female))) {
                        mGender = 2; // Female
                    } else {
                        mGender = 0; // Unknown
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGender = 0; // Unknown
            }
        });
    }

    private void insertPetOrUpdatePet() {
        //
        // prepare the values to be inserted or updated
        // before deciding if the function is insertPet or updatePet
        //
        // trim() remove any white spaces before and after the string
        String name = mNameEditText.getText().toString().trim();
        String breed = mBreedEditText.getText().toString().trim();
        String weightAsString = mWeightEditText.getText().toString().trim();
        //
        // Solving the bug of pressing Add a InterfaceOfPet without entering values
        if (this.idOfClickedPet == -1 && TextUtils.isEmpty(name) &&
                TextUtils.isEmpty(breed) && TextUtils.isEmpty(weightAsString) &&
                mGender == 0) {
            //
            return;
        }

        // if there is no weight the weight is 0
        int weight = 0;
        if (!TextUtils.isEmpty(weightAsString)) {
            weight = Integer.parseInt(weightAsString);
        }


        // prepare the values of a row
        Pet pet = new Pet(name, breed, mGender, weight);
        //

        // change the function of the EditorActivity
        // to either insertPet or updatePet
        // according to this.mUriOfClickedPetFromReceivedIntent
        if (this.idOfClickedPet == -1) {
            // this is the function of insertPet

            //**Uri uriOfInsertedRowOfPet = getContentResolver().insert(PetEntry.CONTENT_URI, contentValues);

            //
            //Log.v(LOG_TAG, "Id of inserted row is: " + ContentUris.parseId(uriOfInsertedRowOfPet));

            /*if (uriOfInsertedRowOfPet == null) {
                Toast.makeText(this, super.getString(R.string.editor_insert_pet_failed) +
                        ContentUris.parseId(uriOfInsertedRowOfPet), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, super.getString(R.string.editor_insert_pet_successful) +
                        ContentUris.parseId(uriOfInsertedRowOfPet), Toast.LENGTH_LONG).show();
            }*/
        }
        else {
            // this is the function of updatePet
            /*int countOfUpdatedRows = getContentResolver().update(
                    this.mUriOfClickedPetFromReceivedIntent, contentValues,
                    null, null);*/

//            Log.v(LOG_TAG, "Count of updated rows is: " + countOfUpdatedRows);

            /*if (countOfUpdatedRows == 0) {
                Toast.makeText(this, super.getString(R.string.editor_update_pet_failed) +
                        countOfUpdatedRows, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, super.getString(R.string.editor_update_pet_successful) +
                        countOfUpdatedRows, Toast.LENGTH_LONG).show();
            }*/
        }
        //
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Do nothing for now
                this.insertPetOrUpdatePet();
                // Exit EditorActivity
                super.finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Do nothing for now
                // Pop up confirmation dialog for deletion
                this.showDeleteConfirmationAlertDialog();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // If the pet hasn't changed, continue with navigating up to parent activity
                // which is the {@link CatalogActivity}.
                if (!mPetHasChanged) {
                    // Navigate back to parent activity (CatalogActivity)
                    NavUtils.navigateUpFromSameTask(this);
                    return true;
                }
                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonOnClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };
                //
                Log.v(EditorActivity.LOG_TAG, "view is touch");
                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesAlertDialog(discardButtonOnClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*@Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        //

        switch (id) {
            //
            case ID_OF_EDIT_OR_INSERT_PET_LOADER:
                //
                // to test if this.mUriOfClickedPetFromReceivedIntent is not null
                if (this.mUriOfClickedPetFromReceivedIntent == null) {
                    return null;
                }
                // Return a new Loader of Cursor
                return null;
            default:
                // An invalid id was passed
                return null;
        }
    }*/

    /*@Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        //
        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // there is only one row in the cursor
        // moveToNext means move

    }*/

    /*@Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //
        mNameEditText.getText().clear();
        mBreedEditText.getText().clear();
        mGenderSpinner.setSelection(0);
        mWeightEditText.getText().clear();
    }*/

    //
    private void showUnsavedChangesAlertDialog(
            DialogInterface.OnClickListener discardButtonOnClickListener) {
        //
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder unsavedChangesAlertDialogBuilder = new AlertDialog.Builder(this);
        unsavedChangesAlertDialogBuilder.setMessage(R.string.unsaved_changes_dialog_msg);
        unsavedChangesAlertDialogBuilder.setPositiveButton(R.string.discard, discardButtonOnClickListener);
        unsavedChangesAlertDialogBuilder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                //
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialogInterface != null) {
                    dialogInterface.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog unsavedChangesAlertDialog = unsavedChangesAlertDialogBuilder.create();
        unsavedChangesAlertDialog.show();
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        // If the pet hasn't changed, continue with handling back button press
        // means no view was touched
        // or if (mNameEditText.isPressed() || ...) {...}
        /*if (!this.mOnTouchListener.onTouch(mGenderSpinner, null)) {
            super.onBackPressed();
            Log.v(EditorActivity.LOG_TAG, "no view is touch");
            return;
        }*/
        //
        if (!mPetHasChanged) {
            super.onBackPressed();
            Log.v(EditorActivity.LOG_TAG, "no view is touch");
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonOnClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        // User clicked "Discard" button, close the current activity.
                        EditorActivity.super.finish();
                    }
                };
        //
        Log.v(EditorActivity.LOG_TAG, "view is touch");
        // Show dialog that there are unsaved changes
        showUnsavedChangesAlertDialog(discardButtonOnClickListener);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //return super.onPrepareOptionsMenu(menu);
        super.onPrepareOptionsMenu(menu);
        // If this is a new pet, hide the "Delete" menu item.
        if (this.mUriOfClickedPetFromReceivedIntent == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    //
    /**
     * Perform the deletion of the pet in the database.
     */
    private void deleteTheClickedPet() {
        // TODO: Implement this method
        // Only perform the delete if this is an existing pet.
        if (this.mUriOfClickedPetFromReceivedIntent != null) {
            // Call the ContentResolver to delete the pet at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentPetUri
            // content URI already identifies the pet that we want.

            // either PetEntry.CONTENT_URI with selection and selectionArgs
            // or this.mUriOfClickedPet with null and null
            int countOfDeletedRows = getContentResolver().delete(this.mUriOfClickedPetFromReceivedIntent,
                    null, null);
            //
            // Show a toast message depending on whether or not the delete was successful.
            if (countOfDeletedRows == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, super.getString(R.string.editor_delete_pet_failed) +
                        countOfDeletedRows, Toast.LENGTH_LONG).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, super.getString(R.string.editor_delete_pet_successful) +
                        countOfDeletedRows, Toast.LENGTH_LONG).show();
            }
            // Close the editor activity
            super.finish();
        }
    }

    //
    private void showDeleteConfirmationAlertDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder deleteConfirmationAlertDialogBuilder = new AlertDialog.Builder(this);
        deleteConfirmationAlertDialogBuilder.setMessage(R.string.delete_dialog_msg);
        deleteConfirmationAlertDialogBuilder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                //
                // User clicked the "Delete" button, so delete the pet.
                EditorActivity.this.deleteTheClickedPet();
            }
        });
        deleteConfirmationAlertDialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                //
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialogInterface != null) {
                    dialogInterface.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog deleteConfirmationAlertDialog = deleteConfirmationAlertDialogBuilder.create();
        deleteConfirmationAlertDialog.show();
    }
}