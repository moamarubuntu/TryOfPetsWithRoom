package com.example.android.pets.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.pets.R;
import com.example.android.pets.database.entity.Pet;

import java.util.List;

public class PetAdapter extends ArrayAdapter<Pet>
{
    public PetAdapter(@NonNull Context context, List<Pet> pets)
    {
        super(context, 0, pets);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        //return super.getView(position, convertView, parent);

        // check if the convertView is inflated to reuse the view, or inflate a new view
        View childOfListViewOfPets = convertView;
        if(childOfListViewOfPets == null) {
            childOfListViewOfPets = LayoutInflater.from(super.getContext()).inflate(R.layout.list_item_as_pet, parent, false);
        }

        // get the object of Pet which is in the position of position
        Pet currentPet = super.getItem(position);

        // find the TextView of id of name of pet in the Child of list
        TextView nameOfPetTexView = (TextView) childOfListViewOfPets.findViewById(R.id.name_of_pet);

        /*
        get the name from currentPet which is in string
        and set name to TextView of name
         */
        nameOfPetTexView.setText(currentPet.getName());


        // find the TextView of id of breed of pet in the Child of list
        TextView breedOfPetTexView = (TextView) childOfListViewOfPets.findViewById(R.id.breed_of_pet);

        /*
        get the breed from currentPet which is in string
        and set breed to TextView of breed
         */
        breedOfPetTexView.setText(currentPet.getBreed());

        //
        return childOfListViewOfPets;
    }
}
