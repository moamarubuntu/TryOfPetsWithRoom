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

    //public MutableLiveData<Integer> countOfDeletedPetsIntegerMutableLiveData = new MutableLiveData<>();

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
    private static class InsertPetAsyncTask extends AsyncTask<Pet, Void, Long>
    {
        private PetDao petDaoOfInsertAsyncTask;

        // to return the id of the inserted pet
        private MutableLiveData<Long> idOfTheInsertedPetLongMutableLiveData = new MutableLiveData<>();

        InsertPetAsyncTask(PetDao petDao)
        {
            this.petDaoOfInsertAsyncTask = petDao;
        }

        @Override
        protected Long doInBackground(Pet... pets) {
            long idOfTheInsertedRow = this.petDaoOfInsertAsyncTask.insertPet(pets[0]);
            return idOfTheInsertedRow;
        }

        /**
         * <p>Runs on the UI thread after {@link #doInBackground}. The
         * specified result is the value returned by {@link #doInBackground}.</p>
         *
         * <p>This method won't be invoked if the task was cancelled.</p>
         *
         * @param aLong The result of the operation computed by {@link #doInBackground}.
         * @see #onPreExecute
         * @see #doInBackground
         * @see #onCancelled(Object)
         */
        @Override
        protected void onPostExecute(Long aLong) {
            //super.onPostExecute(aLong);
            idOfTheInsertedPetLongMutableLiveData.postValue(aLong);
        }
    }

    //
    public LiveData<Long> savePet(Pet pet)
    {
        InsertPetAsyncTask insertPetAsyncTask = new InsertPetAsyncTask(this.petDao);
        insertPetAsyncTask.execute(pet);

        return insertPetAsyncTask.idOfTheInsertedPetLongMutableLiveData;
    }

    //--
    private /*static*/ class UpdatePetAsyncTask extends AsyncTask<Pet, Void, Integer>
    {
        private PetDao petDaoOfUpdateAsyncTask;
        // to return the id of the inserted pet
        private MutableLiveData<Integer> countOfTheUpdatedPetIntegerMutableLiveData = new MutableLiveData<>();

        UpdatePetAsyncTask(PetDao petDao)
        {
            this.petDaoOfUpdateAsyncTask = petDao;
        }

        @Override
        protected Integer doInBackground(Pet... pets)
        {
            int countOfTheUpdatedRow = this.petDaoOfUpdateAsyncTask.updatePet(pets[0]);
            return countOfTheUpdatedRow;
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
            countOfTheUpdatedPetIntegerMutableLiveData.postValue(integer);
        }
    }

    //
    public LiveData<Integer> updatePet(Pet pet) {
        UpdatePetAsyncTask updatePetAsyncTask = new UpdatePetAsyncTask(this.petDao);
        updatePetAsyncTask.execute(pet);

        return updatePetAsyncTask.countOfTheUpdatedPetIntegerMutableLiveData;
    }

    //--
    private /*static*/ class DeletePetAsyncTask extends AsyncTask<Pet, Void, Integer>
    {
        private PetDao petDaoOfDeleteAsyncTask;

        private MutableLiveData<Integer> countOfTheDeletedPetIntegerMutableLiveData =
                new MutableLiveData<>();

        DeletePetAsyncTask(PetDao petDao)
        {
            this.petDaoOfDeleteAsyncTask = petDao;
        }

        @Override
        protected Integer doInBackground(Pet... pets) {
            int countOfTheDeletedRow = this.petDaoOfDeleteAsyncTask.deletePet(pets[0]);
            return countOfTheDeletedRow;
        }

        /**
         * <p>Runs on the UI thread after {@link #doInBackground}. The
         * specified result is the value returned by {@link #doInBackground}.</p>
         *
         * <p>This method won't be invoked if the task was cancelled.</p>
         *
         * @param aVoid The result of the operation computed by {@link #doInBackground}.
         * @see #onPreExecute
         * @see #doInBackground
         * @see #onCancelled(Object)
         */
        @Override
        protected void onPostExecute(Integer integer) {
            //super.onPostExecute(aVoid);
            countOfTheDeletedPetIntegerMutableLiveData.postValue(integer);
        }
    }

    //
    public LiveData<Integer> deletePet(Pet pet)
    {
        DeletePetAsyncTask deletePetAsyncTask = new DeletePetAsyncTask(this.petDao);
        deletePetAsyncTask.execute(pet);

        return deletePetAsyncTask.countOfTheDeletedPetIntegerMutableLiveData;
    }

    //--
    private /*static*/ class DeleteAllPetsAsyncTask extends AsyncTask<Void, Void, Integer>
    {
        private PetDao petDaoOfDeleteAllAsyncTask;
        private MutableLiveData<Integer> countOfDeletedPetsIntegerMutableLiveData = new MutableLiveData<>();

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
        //like this call
        //new DeleteAllPetsAsyncTask(this.petDao).execute();
        // or
        // like this call
        DeleteAllPetsAsyncTask deleteAllPetsAsyncTask = new DeleteAllPetsAsyncTask(this.petDao);
        deleteAllPetsAsyncTask.execute();

        return deleteAllPetsAsyncTask.countOfDeletedPetsIntegerMutableLiveData;
    }
}
