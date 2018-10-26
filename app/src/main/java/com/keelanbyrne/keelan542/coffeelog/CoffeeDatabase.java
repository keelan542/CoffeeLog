package com.keelanbyrne.keelan542.coffeelog;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Coffee.class}, version = 9)
public abstract class CoffeeDatabase extends RoomDatabase {

    public abstract CoffeeDao coffeeDao();

    public static volatile CoffeeDatabase INSTANCE;

    static CoffeeDatabase getDatabse(final Context context) {
        if (INSTANCE == null) {
            synchronized (CoffeeDatabase.class) {
                if  (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            CoffeeDatabase.class, "coffee_log")
                            .build();
                }
            }
        }

        return INSTANCE;
    }
}
