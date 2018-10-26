package com.keelanbyrne.keelan542.coffeelog;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

@Database(entities = {Coffee.class}, version = 9)
public abstract class CoffeeDatabase extends RoomDatabase {

    public abstract CoffeeDao coffeeDao();

    public static volatile CoffeeDatabase INSTANCE;

    static final Migration MIGRATION_8_9 = new Migration(8, 9) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Didnt alter table, so no more code needed
        }
    };

    static CoffeeDatabase getDatabse(final Context context) {
        if (INSTANCE == null) {
            synchronized (CoffeeDatabase.class) {
                if  (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            CoffeeDatabase.class, "coffee_log")
                            .addMigrations(MIGRATION_8_9)
                            .build();
                }
            }
        }

        return INSTANCE;
    }
}
