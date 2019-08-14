package com.example.c196project.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.c196project.database.AppRepository;
import com.example.c196project.database.TermEntity;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainViewModel extends AndroidViewModel {

    public LiveData<List<TermEntity>> mTerms;

    public AppRepository mRepository;
    public Executor executor = Executors.newSingleThreadExecutor();


    public MainViewModel(@NonNull Application application) {
        super(application);

        mRepository = AppRepository.getInstance(getApplication());
        mTerms = mRepository.mTerms;
    }


}
