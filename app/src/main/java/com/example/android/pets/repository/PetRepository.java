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

        this.allPets = petDao.selectAllPets();
    }

    //
    public LiveData<List<Pet>> getAllPets()
    {
        return this.allPets;
    }

    //
    public LiveData<Pet> getPet(int id)
    {
        return  this.petDao.selectPet(id);
    }

    //--
    private static class InsertPetAsyncTask extends AsyncTask<Pet, Void, Void>
    {
        private PetDao petDaoOfInsertAsyncTask;

        InsertPetAsyncTask(PetDao petDao)
        {
            this.petDaoOfInsertAsyncTask = petDao;
        }
        @Override
        protected Void doInBackground(Pet... pets) {
            this.petDaoOfInsertAsyncTask.insertPet(pets[0]);
            return null;
        }
    }

    //
    public void savePet(Pet pet)
    {
        new InsertPetAsyncTask(this.petDao).execute(pet);
    }

    //--
    private static class UpdatePetAsyncTask extends AsyncTask<Pet, Void, Void>
    {
        private PetDao petDaoOfUpdateAsyncTask;

        UpdatePetAsyncTask(PetDao petDao)
        {
            this.petDaoOfUpdateAsyncTask = petDao;
        }
        @Override
        protected Void doInBackground(Pet... pets)
        {
            this.petDaoOfUpdateAsyncTask.updatePet(pets[0]);
            return null;
        }
    }

    //
    public void updatePet(Pet pet) {
        new UpdatePetAsyncTask(this.petDao).execute(pet);
    }

    //--
    private static class DeletePetAsyncTask extends AsyncTask<Pet, Void, Void>
    {
        private PetDao petDaoOfDeleteAsyncTask;

        DeletePetAsyncTask(PetDao petDao)
        {
            this.petDaoOfDeleteAsyncTask = petDao;
        }
        @Override
        protected Void doInBackground(Pet... pets) {
            this.petDaoOfDeleteAsyncTask.deletePet(pets[0]);
            return null;
        }
    }

    //
    public void deletePet(Pet pet)
    {
        new DeletePetAsyncTask(this.petDao).execute(pet);
    }

    //--
    private static class DeleteAllPetsAsyncTask extends AsyncTask<Void, Void, Void>
    {
        private PetDao petDaoOfDeleteAllAsyncTask;

        DeleteAllPetsAsyncTask(PetDao petDao)
        {
            this.petDaoOfDeleteAllAsyncTask = petDao;
        }
        @Override
        protected Void doInBackground(Void... voids)
        {
            this.petDaoOfDeleteAllAsyncTask.deleteAllPets();
            return null;
        }
    }

    //
    public void deleteAllPets()
    {
        new DeleteAllPetsAsyncTask(this.petDao).execute();
    }
}
