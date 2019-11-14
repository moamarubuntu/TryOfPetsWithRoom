package com.example.android.pets.database;

import android.net.Uri;

public class ShelterContract
{
    //
    private static final String SCHEME = "content://";
    //
    public static final String CONTENT_AUTHORITY = "com.example.android.pets";
    //
    //private static final String BASE_CONTENT_URI = SCHEME + CONTENT_AUTHORITY;
    //
    public static final Uri BASE_CONTENT_URI = Uri.parse(SCHEME + CONTENT_AUTHORITY);
    // this is the data type in URI or the path to the data.
    ///*protected */static final String PATH_PETS = "pets";
    //
    //
    private ShelterContract() {}
}
