package com.example.android.pets.database.oldcontentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.CancellationSignal;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.pets.database.oldcontentprovider.ShelterContract.PetEntry;

public class ShelterProvider extends ContentProvider {
    //
    /** Tag for the log messages */
    private static final String LOG_TAG = ShelterProvider.class.getSimpleName();

    // To access our database, we instantiate our subclass of SQLiteOpenHelper
    // and pass the context, which is the current activity.
    private ShelterDbHelper mShelterDbHelper;

    /** URI matcher code for the content URI for the pets table */
    private static final int PETS = 100;

    /** URI matcher code for the content URI for a single pet in the pets table */
    private static final int PET_ID = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.

        // TODO: Add 2 content URIs to URI matcher
        sUriMatcher.addURI(ShelterContract.CONTENT_AUTHORITY, ShelterContract.PATH_PETS, PETS);
        sUriMatcher.addURI(ShelterContract.CONTENT_AUTHORITY, ShelterContract.PATH_PETS + "/#", PET_ID);
    }


    @Override
    public boolean onCreate() {
        // TODO: Create and initialize a ShelterDbHelper object to gain access to the shelter database.

        // Make sure the variable is a global variable, so it can be referenced from other
        // ContentProvider functions.
        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        mShelterDbHelper = new ShelterDbHelper(super.getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] columns,
                        @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] columns,
                        @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder,
                        @Nullable CancellationSignal cancellationSignal) {
        //return null;
        //
        // Get readable database
        SQLiteDatabase sqLiteDatabase = mShelterDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);

        switch (match) {
            case PETS:
                // For the PETS code, query the pets table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the pets table.
                // TODO: Perform query of database on table of pets
                cursor = sqLiteDatabase.query(false, PetEntry.TABLE_NAME, columns,
                        selection, selectionArgs, null, null, sortOrder,
                        null, cancellationSignal);
                break;
            case PET_ID:
                // For the PET_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.android.pets/pets/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                selection = PetEntry._ID + "==?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                // This will perform a query on the pets table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = sqLiteDatabase.query(false, PetEntry.TABLE_NAME, columns,
                        selection, selectionArgs, null, null, sortOrder,
                        null, cancellationSignal);
                break;
            default:
                Log.v(LOG_TAG, "uri is " + uri);
                throw new IllegalArgumentException("Can not query unknown URI " + uri);
        }
        // Set notification URI on the cursor
        // to know what content URI the cursor was created for
        // if the database at this URI changes, then we know we need to update the cursor
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        //return null;
        //
        final int match = sUriMatcher.match(uri);
        //
        switch (match) {
            //
            case PETS:
                return PetEntry.CONTENT_LIST_TYPE;
            case PET_ID:
                return PetEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    /**
     * Insert a pet into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     *
     * @param uri
     * @param contentValues
     * @return
     */
    private Uri insertPet(Uri uri, ContentValues contentValues) {
        //
        // Check that the name is not null
        String name = contentValues.getAsString(PetEntry.COLUMN_PET_NAME);
        if (name == null/* || TextUtils.isEmpty(name)*/) {
            throw new IllegalArgumentException("InterfaceOfPet requires a name");
        }

        // TODO: Finish validation of database of he rest of the attributes in ContentValues
        // No need to check the breed, any value is valid (including null).
//        String breed = contentValues.getAsString(PetEntry.COLUMN_PET_BREED);
//        if (breed == null || TextUtils.isEmpty(breed)) {
//            throw new IllegalArgumentException("Breed requires a name");
//        }

        // Check that the gender is valid
        Integer gender = contentValues.getAsInteger(PetEntry.COLUMN_PET_GENDER);
        if (gender == null || !PetEntry.isValidGender(gender)) {
            throw new IllegalArgumentException("InterfaceOfPet requires a gender");
        }

        // If the weight is provided, check that it's greater than or equal to 0 kg
        Integer weight = contentValues.getAsInteger(PetEntry.COLUMN_PET_WEIGHT);
        if (weight != null && weight < 0) {
            throw new IllegalArgumentException("InterfaceOfPet requires a positive value of weight");
        }

        // TODO: Insert a new pet into the pets database table with the given ContentValues
        // get writable database
        SQLiteDatabase sqLiteDatabase = mShelterDbHelper.getWritableDatabase();
        // perform the insertion of the new pet which is in the contentValues
        long idOfInsertedRowOfPet = sqLiteDatabase.insert(PetEntry.TABLE_NAME, null, contentValues);

        // to log the error if the insertion failed.
        if (idOfInsertedRowOfPet == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        // Notify all listeners that the cursor or database has changed, for this uri of pet content
        // Return the new URI with the ID (of the newly inserted row) appended at the end
        // URI: content://com.example.android.pets/pets
        super.getContext().getContentResolver().notifyChange(uri, null);

        // Once we know the ID of the new row in the table,
        // return the new URI with the ID appended to the end of it
        return ContentUris.withAppendedId(uri, idOfInsertedRowOfPet);
    }
    //
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        //return null;
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                return insertPet(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private int deletePet(Uri uri, String selection, String[] selectionArgs) {
        //
        // get writable database
        SQLiteDatabase sqLiteDatabase = mShelterDbHelper.getWritableDatabase();

        // perform delete on table of pets
        int countOfDeletedRows = sqLiteDatabase.delete(PetEntry.TABLE_NAME, selection, selectionArgs);

        // If 1 or more rows were deleted, then notify all listeners that the database at the
        // given URI has changed
        if (countOfDeletedRows != 0) {
            // Return the new URI with the ID (of the newly inserted row) appended at the end
            // URI: content://com.example.android.pets/pets/id
            super.getContext().getContentResolver().notifyChange(uri, null);
        }

        return countOfDeletedRows;
    }


    //

    /**
     * I must See the comments in app of Pets in EditorActivity
     * in function of deletePet
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return
     */
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        //return 0;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                // Delete all rows that match the selection and selection args
                // if there is no selection and selectionArgs
                // and the uri is PetEntry.CONTENT_URI
                // the deletion will be for the table
                return this.deletePet(uri, selection, selectionArgs);
            case PET_ID:
                // Delete a single row given by the ID in the URI
                // For the PET_ID code, extract out the ID from the URI,
                // so we know which row to delete. Selection will be "_id==?" and
                // selection arguments will be a String array containing the actual ID.
                selection = PetEntry._ID + "==?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                //
                // an other opinion
                // if I want io delete a specific pet,
                // no need for selection and selectionArgs.
                // Because the specific pet is defined by the uri which contains the id
                return this.deletePet(uri, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
    }

    //
    /**
     * Update pets in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more pets).
     * Return the number of rows that were successfully updated.
     */
    private int updatePet(Uri uri, ContentValues contentValues,
                          String selection, String[] selectionArgs) {

        //Because not all attributes will be present, we encourage you to use
        // the ContentValues.containsKey() to check whether a key/value pair exists
        // If the {@link PetEntry#COLUMN_PET_NAME} key is present,
        if (contentValues.containsKey(PetEntry.COLUMN_PET_NAME)) {
            // Check that the name is not null
            String name = contentValues.getAsString(PetEntry.COLUMN_PET_NAME);
            if (name == null/* || TextUtils.isEmpty(name)*/) {
                throw new IllegalArgumentException("InterfaceOfPet requires a name");
            }
        }

        // No need to check the breed, any value is valid (including null).
        //String breed = contentValues.getAsString(PetEntry.COLUMN_PET_BREED);
        //if (breed == null || TextUtils.isEmpty(breed)) {
            //throw new IllegalArgumentException("Breed requires a name");

        // If the {@link PetEntry#COLUMN_PET_GENDER} key is present,
        if (contentValues.containsKey(PetEntry.COLUMN_PET_GENDER)) {
            // Check that the gender is valid
            Integer gender = contentValues.getAsInteger(PetEntry.COLUMN_PET_GENDER);
            if (gender == null || !PetEntry.isValidGender(gender)) {
                throw new IllegalArgumentException("InterfaceOfPet requires a gender");
            }
        }

        // If the {@link PetEntry#COLUMN_PET_WEIGHT} key is present,
        if (contentValues.containsKey(PetEntry.COLUMN_PET_WEIGHT)) {
            // If the weight is provided, check that it's greater than or equal to 0 kg
            Integer weight = contentValues.getAsInteger(PetEntry.COLUMN_PET_WEIGHT);
            if (weight != null && weight < 0) {
                throw new IllegalArgumentException("InterfaceOfPet requires a weight");
            }
        }

        // If there are no contentValues to update, then don't try to update the database
        if (contentValues.size() == 0) {
            return 0;
        }

        // TODO: Update the selected pets in the pets database table with the given ContentValues
        // get writable database
        SQLiteDatabase sqLiteDatabase = mShelterDbHelper.getWritableDatabase();
        // perform the update of pets
        int countOfUpdatedRows = sqLiteDatabase.update(PetEntry.TABLE_NAME, contentValues,
                selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the database at the
        // given URI has changed
        if (countOfUpdatedRows != 0) {
            // Return the new URI with the ID (of the newly inserted row) appended at the end
            // URI: content://com.example.android.pets/pets/id
            super.getContext().getContentResolver().notifyChange(uri, null);
        }

        // TODO: Return the number of rows that were affected
        return countOfUpdatedRows;
    }

    //

    /**
     * I must See the comments in app of Pets in EditorActivity
     * in function of savePet in the else block
     * @param uri
     * @param contentValues
     * @param selection
     * @param selectionArgs
     * @return
     */
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues,
                      @Nullable String selection, @Nullable String[] selectionArgs) {
        //return 0;
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                // if there is no selection and selectionArgs
                // and the uri is PetEntry.CONTENT_URI
                // the update will be for the table with the new contentValues
                // if there selection and selectionArgs the update with the new contentValues
                // will be to rows that match the the selection and selectionArgs
                return updatePet(uri, contentValues, selection, selectionArgs);
            case PET_ID:
                // For the PET_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id==?" and
                // selection arguments will be a String array containing the actual ID.
                selection = PetEntry._ID + "==?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                //may be if I comment them something happened in the EditorActivity
                // an other opinion
                // if I want io update a specific pet,
                // no need for selection and selectionArgs.
                // Because the specific pet is defined by the uri which contains the id
                return updatePet(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }
}
