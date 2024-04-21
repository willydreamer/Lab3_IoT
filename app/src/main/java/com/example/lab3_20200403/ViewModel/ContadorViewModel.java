package com.example.lab3_20200403.ViewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ContadorViewModel extends ViewModel {
    private final MutableLiveData<Integer> contador = new MutableLiveData<>();
    public MutableLiveData<Integer> getContador() {
        return contador;
    }

    public void updateContador(int value) {
        contador.postValue(value);
    }
}
