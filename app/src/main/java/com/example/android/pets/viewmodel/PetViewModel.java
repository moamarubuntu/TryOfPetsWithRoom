package com.example.android.pets.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.android.pets.database.entity.Pet;
import com.example.android.pets.repository.PetRepository;

import java.util.List;

public class PetViewModel extends AndroidViewModel
{
    private PetRepository petRepository;

    private LiveData<List<Pet>> allPets;

    public PetViewModel(@NonNull Application application) {
        super(application);
        //
        this.petRepository = new PetRepository(application);
        this.allPets = petRepository.getAllPets();
    }

    public LiveData<List<Pet>> getAllPets()
    {
        return this.allPets;
    }

    //
    public LiveData<Pet> getPet(int id)
    {
        return  this.petRepository.getPet(id);
    }

    public LiveData<Long> insertPet(Pet pet)
    {
        return this.petRepository.savePet(pet);
    }

    public LiveData<Integer> updatePet(Pet pet) {
        return this.petRepository.updatePet(pet);
    }

    public LiveData<Integer> deletePet(Pet pet)
    {
        return this.petRepository.deletePet(pet);
    }

    public LiveData<Integer> deleteAllPets()
    {
        return this.petRepository.deleteAllPets();
    }
}
