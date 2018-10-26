package com.keelanbyrne.keelan542.coffeelog;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class CoffeeRepository {

    private CoffeeDao coffeeDao;
    private LiveData<List<Coffee>> allCoffee;

    CoffeeRepository(Application application) {
        CoffeeDatabase db = CoffeeDatabase.getDatabse(application);
        coffeeDao = db.coffeeDao();
        allCoffee = coffeeDao.getAllCoffee();
    }

    LiveData<List<Coffee>> getAllCoffee() {
        return allCoffee;
    }

    public void insert(Coffee coffee) {
        new insertAsyncTask(coffeeDao).execute(coffee);
    }

    private static class insertAsyncTask extends AsyncTask<Coffee, Void, Void> {

        private CoffeeDao coffeeDaoAsync;

        insertAsyncTask(CoffeeDao dao) {
            coffeeDaoAsync = dao;
        }

        @Override
        protected Void doInBackground(Coffee... coffees) {
            coffeeDaoAsync.insert(coffees[0]);
            return null;
        }
    }
}
