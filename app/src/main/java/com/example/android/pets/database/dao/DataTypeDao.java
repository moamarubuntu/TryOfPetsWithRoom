package com.example.android.pets.database.dao;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Update;

//@Dao
public interface DataTypeDao<DT>
{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertDataType(DT dt);

    @Update
    int updateDataType(DT dt);

    @Delete
    int deleteDataType(DT dt);
}
