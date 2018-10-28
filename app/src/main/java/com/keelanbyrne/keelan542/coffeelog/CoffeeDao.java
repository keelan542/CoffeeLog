package com.keelanbyrne.keelan542.coffeelog;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface CoffeeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Coffee coffee);

    @Delete
    void delete(Coffee coffee);

    @Query("SELECT * from coffee_log")
    LiveData<List<Coffee>> getAllCoffee();

    @Query("SELECT * from coffee_log where _id = :id")
    Coffee getCoffee(int id);

    @Update
    void update(Coffee... coffee);
}
