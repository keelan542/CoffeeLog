package com.keelanbyrne.keelan542.coffeelog;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class CoffeeViewModel extends AndroidViewModel {

    private CoffeeRepository repository;

    private LiveData<List<Coffee>> allCoffee;

    public CoffeeViewModel(Application application) {
        super(application);
        repository = new CoffeeRepository(application);
        allCoffee = repository.getAllCoffee();
    }

    LiveData<List<Coffee>> getAllCoffee() {
        return allCoffee;
    }

    public void insert(Coffee coffee) {
        repository.insert(coffee);
    }

    public void delete(Coffee coffee) {
        repository.delete(coffee);
    }

    public void update(Coffee coffee) {
        repository.update(coffee);
    }

    public void selectAll() {
        repository.selectAll();
    }

    public void selectCoffee(Coffee coffee) {
        repository.selectCoffee(coffee);
    }
}
