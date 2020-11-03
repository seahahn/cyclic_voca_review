package com.seahahn.cyclicvocareview.vocagroup;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class VocagroupViewModel extends ViewModel {

    private MutableLiveData<ArrayList<VocagroupArea>> vocagroupArea;
    public LiveData<ArrayList<VocagroupArea>> getVocagroupArea() {
        if (vocagroupArea == null) {
            vocagroupArea = new MutableLiveData<ArrayList<VocagroupArea>>();
        }
        return vocagroupArea;
    }
}
