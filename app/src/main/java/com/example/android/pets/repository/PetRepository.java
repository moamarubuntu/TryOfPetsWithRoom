package com.example.android.pets.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import com.example.android.pets.database.dao.PetDao;
import com.example.android.pets.database.Shelter;
import com.example.android.pets.database.entity.Pet;

import java.util.List;

public class PetRepository
{
    private PetDao petDao;

    private LiveData<List<Pet>> allPets;

    public MutableLiveData<Integer> countOfDeletedPetsIntegerMutableLiveData = new MutableLiveData();

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
    private /*static*/ class DeleteAllPetsAsyncTask extends AsyncTask<Void, Void, Integer>
    {
        private PetDao petDaoOfDeleteAllAsyncTask;

        DeleteAllPetsAsyncTask(PetDao petDao)
        {
            this.petDaoOfDeleteAllAsyncTask = petDao;
        }

        @Override
        protected Integer doInBackground(Void... voids)
        {
            int countOfDeletedRows = this.petDaoOfDeleteAllAsyncTask.deleteAllPets();
            return countOfDeletedRows;
        }

        /**
         * <p>Runs on the UI thread after {@link #doInBackground}. The
         * specified result is the value returned by {@link #doInBackground}.</p>
         *
         * <p>This method won't be invoked if the task was cancelled.</p>
         *
         * @param integer The result of the operation computed by {@link #doInBackground}.
         * @see #onPreExecute
         * @see #doInBackground
         * @see #onCancelled(Object)
         */
        @Override
        protected void onPostExecute(Integer integer) {
            //super.onPostExecute(integer);
            // TODO: how to return this integer to
            countOfDeletedPetsIntegerMutableLiveData.postValue(integer);
        }
    }

    //
    public LiveData<Integer> deleteAllPets()
    {
        new DeleteAllPetsAsyncTask(this.petDao).execute();

        return countOfDeletedPetsIntegerMutableLiveData;
    }

    private void deleteAllPetsReturnInteger(Integer integer)
    {
        //return integer;
    }
}
