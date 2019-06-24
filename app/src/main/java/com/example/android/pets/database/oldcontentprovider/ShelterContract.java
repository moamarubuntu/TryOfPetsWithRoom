package com.example.android.pets.database.oldcontentprovider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class ShelterContract {

    //
    private static final String SCHEME = "content://";
    //
    /*protected */static final String CONTENT_AUTHORITY = "com.example.android.pets";
    //
    //private static final String BASE_CONTENT_URI = SCHEME + CONTENT_AUTHORITY;
    //
    private static final Uri BASE_CONTENT_URI = Uri.parse(SCHEME + CONTENT_AUTHORITY);
    // this is the database type in URI or the path to the database.
    /*protected */static final String PATH_PETS = "pets";
    //
    //
    private ShelterContract() {}

    //
    public static class PetEntry implements BaseColumns {
        //
        private PetEntry() {}

        // the name of table of pets
        /*public */static final String TABLE_NAME = "pets";
        //the names of columns of table of pets
        //public static final String COLUMN_ID = "_id";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_PET_NAME = "name";
        public static final String COLUMN_PET_BREED = "breed";
        public static final String COLUMN_PET_GENDER = "gender";
        public static final String COLUMN_PET_WEIGHT = "weight";

        /**
         * Possible values for the gender of the pet.
         */
        public static final int GENDER_UNKNOWN = 0;
        public static final int GENDER_MALE = 1;
        public static final int GENDER_FEMALE = 2;

        //
        //public static final Uri CONTENT_URI = Uri.parse(SCHEME + CONTENT_AUTHORITY + "/" + PATH_PETS);
        //
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PETS);

        //
        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of pets.
         */
        // The MIME type for uri of PETS
        /*public */static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PETS;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single pet.
         */
        // The MIME type for uri of PET_ID
        /*public */static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PETS;

        //
        /**
         * Returns whether or not the given gender is {@link #GENDER_UNKNOWN}, {@link #GENDER_MALE},
         * or {@link #GENDER_FEMALE}.
         */
        /*public */static boolean isValidGender(int gender) {
            /*if (gender == GENDER_UNKNOWN || gender == GENDER_MALE || gender == GENDER_FEMALE) {
                return true;
            }
            return false;*/
            return gender == GENDER_UNKNOWN || gender == GENDER_MALE || gender == GENDER_FEMALE;
        }
    }
}
