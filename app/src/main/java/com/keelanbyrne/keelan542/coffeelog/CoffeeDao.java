package com.keelanbyrne.keelan542.coffeelog;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

public interface CoffeeDao {

    @Insert
    void insert(Coffee coffee);

    @Delete
    void delete(Coffee coffee);

    @Query("SELECT * from coffee_log")
    LiveData<List<Coffee>> getAllCoffee();

    @Update
    void update(Coffee... coffee);
}
