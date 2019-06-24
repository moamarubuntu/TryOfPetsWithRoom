package com.example.android.pets.ui;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.pets.R;
import com.example.android.pets.database.oldcontentprovider.ShelterContract.PetEntry;

/**
 * {@link PetCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of pet database as its database source. This adapter knows
 * how to create list items for each row of pet database in the {@link Cursor}.
 */
public class PetCursorAdapter extends CursorAdapter {

    /**
     * Constructs a new {@link PetCursorAdapter}.
     *
     * @param context The context
     * @param cursor The cursor which to get the database from.
     */
    public PetCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    /**
     * Makes a new blank list item view. No database is set (or bound) to the views yet.
     * Makes a new view to hold the database pointed to by cursor.
     *
     * @param context Interface to application's global information, or app context
     * @param cursor  The cursor from which to get the database. The cursor is already
     *                moved to the correct position.
     * @param viewGroup  The ViewGroup to which the new view is attached to
     * @return the newly created view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        // TODO: Fill out this method and return the list item view (instead of null)
        return LayoutInflater.from(context).inflate(R.layout.list_item_as_pet, viewGroup, false);
        //return null;
    }

    /**
     * This function binds the pet database (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current pet can be set on the name TextView
     * in the list item layout.
     * Bind an existing view to the database pointed to by cursor
     *
     * @param view    Existing view, returned earlier by newView
     * @param context Interface to application's global information, or app context
     * @param cursor  The cursor from which to get the database. The cursor is already
     *                The cursor from which to get the database. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // TODO: Fill out this method
        // Text View for the name of pet in item of list which is the passed view
        TextView nameOfPetTextView = (TextView) view.findViewById(R.id.name_of_pet);
        // Text View for the summary of pet in item of list layout
        TextView breedOfPetTextView = (TextView) view.findViewById(R.id.breed_of_pet);

        // get the values of indices of columns that we want from the cursor
        int nameColumnIndex = cursor.getColumnIndexOrThrow(PetEntry.COLUMN_PET_NAME);
        int breedColumnIndex = cursor.getColumnIndexOrThrow(PetEntry.COLUMN_PET_BREED);

        // get the values of columns that we want from the cursor
        String nameOfPetAtCursor = cursor.getString(nameColumnIndex);
        String breedOfPetAtCursor = cursor.getString(breedColumnIndex);

        // If the pet breed is empty string or null, then use some default text
        // that says "Unknown breed", so the TextView isn't blank.
        if (TextUtils.isEmpty(breedOfPetAtCursor)) {
            breedOfPetAtCursor = context.getString(R.string.unknown_breed);
        }

        // assign the values to the views inside the view of item of list
        // which is passed in the signature of this function
        nameOfPetTextView.setText(nameOfPetAtCursor);
        breedOfPetTextView.setText(breedOfPetAtCursor);
    }
}
