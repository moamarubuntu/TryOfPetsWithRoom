package com.example.android.pets.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.content.ContentResolver;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.example.android.pets.database.ShelterContract;
import com.example.android.pets.model.InterfaceOfPet;

@Entity(tableName = "pets")
public class Pet implements InterfaceOfPet
{
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    private String name;
    private String breed;
    private int gender;
    private int weight;

    @Ignore
    public static final String PATH_PETS = "pets";

    public static final Uri CONTENT_URI = Uri.withAppendedPath(ShelterContract.BASE_CONTENT_URI, PATH_PETS);

    /*public */static final String CONTENT_LIST_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + ShelterContract.CONTENT_AUTHORITY + "/" + PATH_PETS;

    /*public */static final String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + ShelterContract.CONTENT_AUTHORITY + "/" + PATH_PETS;


    @Override
    public int getId() {
        return this.id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }


    @Override
    public String getName()
    {
        return this.name;
    }

    public void setName(@NonNull String name)
    {
        this.name = name;
    }


    @Override
    public String getBreed()
    {
        return this.breed;
    }

    public void setBreed(String breed)
    {
        this.breed = breed;
    }


    @Override
    public int getGender()
    {
        return this.gender;
    }

    public void setGender(@NonNull int gender)
    {
        if (gender>=0 && gender<=2) {
            this.gender = gender;
        }
    }


    @Override
    public int getWeight()
    {
        return this.weight;
    }

    public void setWeight(@NonNull int weight)
    {
        this.weight = weight;
    }

    /*public Pet(@NonNull int id, @NonNull String name,
               String breed, @NonNull int gender,
               @NonNull int weight)
    {
        this.setId(id);
        this.setName(name);
        this.setBreed(breed);
        this.setGender(gender);
        this.setWeight(weight);
    }*/

    public Pet(@NonNull String name, String breed,
               @NonNull int gender, @NonNull int weight) {
        //this.setId(id);
        this.setName(name);
        this.setBreed(breed);
        this.setGender(gender);
        this.setWeight(weight);
    }
}
