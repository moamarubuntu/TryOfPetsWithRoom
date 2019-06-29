package com.example.android.pets.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.android.pets.database.entity.Pet;

import java.util.List;

@Dao
public interface PetDao
{
    @Query("SELECT * FROM pets")
    LiveData<List<Pet>> selectAllPets();

    @Query("SELECT * FROM pets WHERE id == :id")
    LiveData<Pet> selectPet(int id);

    @Query("SELECT * FROM pets WHERE id == :id")
    Pet selectPetSync(int id);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertPet(Pet pet);

    @Update
    void updatePet(Pet pet);

    @Delete
    void deletePet(Pet pet);

    @Query("DELETE FROM pets")
    void deleteAllPets();


}
