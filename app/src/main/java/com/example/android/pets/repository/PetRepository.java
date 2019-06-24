package com.example.android.pets.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.android.pets.database.dao.PetDao;
import com.example.android.pets.database.Shelter;
import com.example.android.pets.database.entity.Pet;

import java.util.List;

public class PetRepository
{
    private PetDao petDao;

    private LiveData<List<Pet>> allPets;

    public PetRepository(Application application){
        //
        Shelter shelter = Shelter.getInstance(application);

        this.petDao = shelter.petDao();

        this.allPets = petDao.selectPets();
    }

    //--
    private static class insertPetAsyncTask extends AsyncTask<Pet, Void, Void>
    {
        private PetDao petDaoOfInsertAsyncTask;

        insertPetAsyncTask(PetDao petDao) {
            petDaoOfInsertAsyncTask = petDao;
        }
        @Override
        protected Void doInBackground(Pet... pets) {
            petDaoOfInsertAsyncTask.insertPet(pets[0]);
            return null;
        }
    }

    //
    public LiveData<List<Pet>> getAllPets() {
        return this.allPets;
    }

    //
    public void savePet(Pet pet) {
        new insertPetAsyncTask(this.petDao).execute(pet);
    }

    //--
    private static class updatePetAsyncTask extends AsyncTask<Pet, Void, Void>
    {
        private PetDao petDaoOfUpdateAsyncTask;

        updatePetAsyncTask(PetDao petDao) {
            petDaoOfUpdateAsyncTask = petDao;
        }
        @Override
        protected Void doInBackground(Pet... pets) {
            petDaoOfUpdateAsyncTask.updatePet(pets[0]);
            return null;
        }
    }

    //
    public void updatePet(Pet pet) {
        new updatePetAsyncTask(this.petDao).execute(pet);
    }

    //--
    private static class deletePetAsyncTask extends AsyncTask<Pet, Void, Void>
    {
        private PetDao petDaoOfDeleteAsyncTask;

        deletePetAsyncTask(PetDao petDao) {
            petDaoOfDeleteAsyncTask = petDao;
        }
        @Override
        protected Void doInBackground(Pet... pets) {
            petDaoOfDeleteAsyncTask.deletePet(pets[0]);
            return null;
        }
    }

    //
    public void deletePet(Pet pet) {
        new deletePetAsyncTask(this.petDao).execute(pet);
    }

    //--
    private static class deleteAllPetsAsyncTask extends AsyncTask<Void, Void, Void>
    {
        private PetDao petDaoOfDeleteAllAsyncTask;

        deleteAllPetsAsyncTask(PetDao petDao) {
            petDaoOfDeleteAllAsyncTask = petDao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            petDaoOfDeleteAllAsyncTask.deleteAllPets();
            return null;
        }
    }

    //
    public void deleteAllPets() {
        new deleteAllPetsAsyncTask(this.petDao).execute();
    }
}
