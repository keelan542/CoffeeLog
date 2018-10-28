package com.keelanbyrne.keelan542.coffeelog;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class CoffeeViewModel extends AndroidViewModel {

    private CoffeeRepository repository;

    private LiveData<List<Coffee>> allWords;

    public CoffeeViewModel(Application application) {
        super(application);
        repository = new CoffeeRepository(application);
        allWords = repository.getAllCoffee();
    }

    LiveData<List<Coffee>> getAllWords() {
        return allWords;
    }

    public void insert(Coffee coffee) {
        repository.insert(coffee);
    }
}
