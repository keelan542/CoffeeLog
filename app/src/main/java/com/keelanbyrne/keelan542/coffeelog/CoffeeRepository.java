package com.keelanbyrne.keelan542.coffeelog;

import android.app.Application;
import android.arch.lifecycle.LiveData;

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
        new operationAsyncTask(coffeeDao, 0).execute(coffee);
    }

    public void delete(Coffee coffee) {
        new operationAsyncTask(coffeeDao, 1).execute(coffee);
    }

    public void update(Coffee coffee) {
        new operationAsyncTask(coffeeDao, 2).execute(coffee);
    }

    public void selectAll() {
        new operationAsyncTask(coffeeDao, 3).execute();
    }

    public void selectCoffee(Coffee coffee) {
        new operationAsyncTask(coffeeDao, 4).execute(coffee);
    }

    private static class operationAsyncTask extends android.os.AsyncTask<Coffee, Void, Void> {

        private CoffeeDao coffeeDaoAsync;
        private int operationCodeAsync;

        operationAsyncTask(CoffeeDao dao, int operationCode) {
            coffeeDaoAsync = dao;
            operationCodeAsync = operationCode;
        }

        @Override
        protected Void doInBackground(Coffee... coffees) {
            switch (operationCodeAsync) {
                case 0:
                    coffeeDaoAsync.insert(coffees[0]);
                    break;
                case 1:
                    coffeeDaoAsync.delete(coffees[0]);
                    break;
                case 2:
                    coffeeDaoAsync.update(coffees[0]);
                    break;
                case 3:
                    coffeeDaoAsync.getAllCoffee();
                    break;
                case 4:
                    coffeeDaoAsync.getCoffee(coffees[0].getId());
            }

            return null;
        }
    }
}
