package com.example.android.pets.database.oldcontentprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.android.pets.database.oldcontentprovider.ShelterContract.PetEntry;

public class ShelterDbHelper extends SQLiteOpenHelper {
    //
    private static final String LOG_TAG = ShelterDbHelper.class.getSimpleName();
    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    public static final int DATABASE_VERSION = 1;

    /** Name of the database file */
    public static final String DATABASE_NAME = "shelter.db";

    // constants for the SQL statements
    private final static String CREATE_TABLE = "CREATE TABLE";
    private final static String TEXT_TYPE = "TEXT";
    private final static String INTEGER_TYPE = "INTEGER";
    private final static String PK_AUTOINCREMENT = "PRIMARY KEY AUTOINCREMENT";
    private final static String NOT_NULL = "NOT NULL";
    private final static String DEFAULT = "DEFAULT";
    private final static String DROP_TABLE_IF_EXISTS = "DROP TABLE IF EXISTS";
    //private final static String COMMA_SEP = ",";
    //private final static String SPACE = " ";

    // constant of SQL statement to create table
    private final static String SQL_CREATE_PETS_TABLE =
            CREATE_TABLE + " " + PetEntry.TABLE_NAME + " " + "(" +
                    PetEntry._ID + " " + INTEGER_TYPE + " " + PK_AUTOINCREMENT + "," +
                    PetEntry.COLUMN_PET_NAME + " " + TEXT_TYPE + " " + NOT_NULL + "," +
                    PetEntry.COLUMN_PET_BREED + " " + TEXT_TYPE + "," +
                    PetEntry.COLUMN_PET_GENDER + " " + INTEGER_TYPE + " " + NOT_NULL + /*" " +
                    DEFAULT + " " + PetEntry.GENDER_UNKNOWN + */"," +
                    PetEntry.COLUMN_PET_WEIGHT + " " + INTEGER_TYPE + " " + /*NOT_NULL + " " +*/
                    DEFAULT + " " + "0" + ");";

    // constant of SQL statement to delete table
    private final static String SQL_DELETE_PETS_TABLE =
            DROP_TABLE_IF_EXISTS + " " + PetEntry.TABLE_NAME;

    //
    public ShelterDbHelper (Context context) {
        super(context, ShelterDbHelper.DATABASE_NAME, null, ShelterDbHelper.DATABASE_VERSION);
    }
    //
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //
        Log.v(LOG_TAG, "SQL Create table of Pets" + SQL_CREATE_PETS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_PETS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //
        sqLiteDatabase.execSQL(SQL_DELETE_PETS_TABLE);
        this.onCreate(sqLiteDatabase);
    }
}
