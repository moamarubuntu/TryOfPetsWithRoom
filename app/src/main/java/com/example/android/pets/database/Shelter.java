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

    private static volatile Shelter SHELTER;

    //
    private static class PopulateDataAsyncTask extends AsyncTask<Void, Void, Void>
    {
        private final PetDao petDao;

        PopulateDataAsyncTask(Shelter shelter)
        {
            this.petDao = shelter.petDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
            //
            //this.petDao.deleteAllPets();

            /*Pet pet = new Pet("Pet name",
                    "Pet breed", 0, 7);
            this.petDao.insertPet(pet);*/
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

            // If you want to keep the data through app restarts,
            // comment out the following line.
            //
            new PopulateDataAsyncTask(SHELTER).execute();
        }
    };

    public static Shelter getInstance(final Context context) {
        if (SHELTER == null) {
            synchronized (Shelter.class) {
                if (SHELTER == null) {
                    SHELTER = Room.databaseBuilder(context.getApplicationContext(),
                            Shelter.class, "shelter")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }//synchronized
        }//first if
        return SHELTER;
    }//getInstance

    //
}
