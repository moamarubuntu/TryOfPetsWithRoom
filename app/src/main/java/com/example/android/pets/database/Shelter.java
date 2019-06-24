package com.example.android.pets.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.android.pets.database.dao.PetDao;
import com.example.android.pets.database.entity.Pet;

@Database(entities = {Pet.class}, version = 1, exportSchema = false)
public abstract class Shelter extends RoomDatabase
{
    public abstract PetDao petDao();

    private static volatile Shelter INSTANCE;

    //
    private static class PopulateDataAsyncTask extends AsyncTask<Void, Void, Void>
    {
        private final PetDao petDao;

        PopulateDataAsyncTask(Shelter shelter) {
            petDao = shelter.petDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //
            //petDao.deleteAllPets();
            Pet pet = new Pet(0,"Pet name",
                    "Pet breed", 0, 7);
            petDao.insertPet(pet);
            return null;
        }
    }//PopulateDataAsyncTask

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        /**
         * Called when the database has been opened.
         *
         * @param db The database.
         */
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDataAsyncTask(INSTANCE).execute();
        }
    };

    public static Shelter getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (Shelter.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            Shelter.class, "shelter")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }//synchronized
        }//first if
        return INSTANCE;
    }//getInstance

    //
}
