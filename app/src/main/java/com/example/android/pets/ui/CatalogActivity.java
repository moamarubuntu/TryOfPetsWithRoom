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
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.example.android.pets.R;
import com.example.android.pets.database.entity.Pet;
import com.example.android.pets.viewmodel.PetViewModel;

import java.util.List;

/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity /*implements LoaderManager.LoaderCallbacks<List<Pet>>*/ {
    //
    private static final String LOG_TAG = CatalogActivity.class.getSimpleName();

    //
    private static final int ID_OF_PET_LOADER = 0;

    //
    private PetAdapter petAdapter;

    //
    private PetViewModel petViewModel;


    //
    private void updateUi(List<Pet> pets)
    {
        //
        // Find the ListView which will be populated with the database of pets
        ListView listOfPetsListView = (ListView) findViewById(R.id.list_of_pets);

        // Find and set empty view in the ListView,
        // this only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        listOfPetsListView.setEmptyView(emptyView);


        // Setup an the adapter to create an item of ListView for each row of pet in the ...?/(Adapter).
        this.petAdapter = new PetAdapter(this, pets);

        // Attach the adapter to the ListView.
        listOfPetsListView.setAdapter(this.petAdapter);

        listOfPetsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent editIntent = new Intent(CatalogActivity.this, EditorActivity.class);

                Log.v(LOG_TAG, "Id sent by intent is : " + id);
                //Uri uriOfClickedPet = ContentUris.withAppendedId(PetEntry.CONTENT_URI, id);

                editIntent.putExtra("idOfClickedPet" , position);

                startActivity(editIntent);
            }
        });
    }

    //
    /*private void subscribeUiPets()
    {
        //
        petViewModel.getAllPets().observe(this, new Observer<List<Pet>>() {
            @Override
            public void onChanged(@Nullable List<Pet> pets) {
                updateUi(pets);
            }
        });
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });


        //
        //displayDatabaseInfo();

//        getLoaderManager().initLoader(ID_OF_PET_LOADER, null, this);


        // was before
        //this.updateUi();

        this.petViewModel = ViewModelProviders.of(this).get(PetViewModel.class);

        // if I want this here I must remove the implementation of
        // LoaderCallBack<List<Pet>>
        final Observer<List<Pet>> listOfPetsObserver = new Observer<List<Pet>>()
        {
            @Override
            public void onChanged(@Nullable List<Pet> pets) {
                updateUi(pets);
            }
        };

        petViewModel.getAllPets().observe(this, listOfPetsObserver);
        //
    }


//    @Override
//    public Loader<List<Pet>> onCreateLoader(int id, Bundle bundle) {
//
//        String [] columns = {
//                PetEntry._ID,
//                PetEntry.COLUMN_PET_NAME,
//                PetEntry.COLUMN_PET_BREED,
//                PetEntry.COLUMN_PET_GENDER,
//                PetEntry.COLUMN_PET_WEIGHT
//        };
//
//        switch (id) {
//            //
//            case ID_OF_PET_LOADER:
//                // Return a new Loader of Cursor
//                /*return new CursorLoader(this,
//                        PetEntry.CONTENT_URI,
//                        columns,
//                        null,
//                        null,
//                        null);*/
//                return passPets();
//            default:
//                // An invalid id was passed
//                return null;
//        }
//    }

//    @Override
//    public void onLoadFinished(Loader<List<Pet>> loader, /*Cursor cursor*/List<Pet> pets) {
//        //
//        // Update {@link PetCursorAdapter} with this new cursor containing updated database of pet
//        // was before now move to updateUi()
//        //mPetCursorAdapter.swapCursor(cursor);
//
//        // now
//        this.updateUi(pets);
//    }

//    @Override
//    public void onLoaderReset(Loader<List<Pet>> loader) {
//        //
//        // Callback called when the cursor needs to be deleted
//        //mPetCursorAdapter.swapCursor(null);
//        this.petAdapter.clear();
//    }

    private void insertTestOfPet() {
        //
        // this pet to insert by pressing the menu item
        // of insert in CatalogActivity
        Pet tryPet = new Pet("Toto", "Terrier" , 0, 7);

        // insert a pet
        petViewModel.insertPet(tryPet);

        //
        Toast.makeText(this, super.getString(R.string.editor_insert_pet_successful),
                Toast.LENGTH_LONG).show();
        //
        Log.v(LOG_TAG, "Id of row is: ");
    }

    //
    //
    private void deleteAllPets() {
        // TODO: Implement this method
        //
        Log.v(this.LOG_TAG, "rows deleted from pet database count is: ");

        // Show a toast message depending on whether or not the delete was successful.
        if (0 == 0) {
            // If no rows were deleted, then there was an error with the delete.
            Toast.makeText(this, super.getString(R.string.catalog_delete_all_pets_failed) +
                    " ", Toast.LENGTH_LONG).show();
        } else {
            // Otherwise, the delete was successful and we can display a toast.
            Toast.makeText(this, super.getString(R.string.catalog_delete_all_pets_successful) +
                    " ", Toast.LENGTH_LONG).show();
        }
        // Close the activity
        //super.finish();

    }

    //
    private void showDeleteAllConfirmationAlertDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder deleteAllConfirmationAlertDialogBuilder = new AlertDialog.Builder(this);
        deleteAllConfirmationAlertDialogBuilder.setMessage(R.string.delete_all_dialog_msg);
        deleteAllConfirmationAlertDialogBuilder.setPositiveButton(R.string.delete_all,
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                //
                // User clicked the "Delete" button, so delete all pets.
                CatalogActivity.this.deleteAllPets();
            }
        });
        deleteAllConfirmationAlertDialogBuilder.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                //
                // User clicked the "Cancel" button, so dismiss the dialog
                if (dialogInterface != null) {
                    dialogInterface.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog deleteAllConfirmationAlertDialog = deleteAllConfirmationAlertDialogBuilder.create();
        deleteAllConfirmationAlertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy database" menu option
            case R.id.action_insert_dummy_data:
                // Do nothing for now
                this.insertTestOfPet();
                //this.displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                this.showDeleteAllConfirmationAlertDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Dispatch onStart() to all fragments.  Ensure any created loaders are
     * now started.
     */
    /*
    @Override
    protected void onStart() {
        super.onStart();
        this.displayDatabaseInfo();
    }*/

    //

    //
}
