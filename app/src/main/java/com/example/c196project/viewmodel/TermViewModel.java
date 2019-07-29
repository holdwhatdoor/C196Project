package com.example.c196project.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.c196project.database.AppRepository;
import com.example.c196project.database.TermEntity;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TermViewModel extends AndroidViewModel {

    public MutableLiveData<TermEntity> mLiveTerm = new MutableLiveData<>();
    public LiveData<TermEntity> mTerm;
    public LiveData<List<TermEntity>> mTerms;
    public AppRepository mRepository;
    public Executor executor = Executors.newSingleThreadExecutor();

    public TermViewModel(@NonNull Application application) {
        super(application);

        mRepository = AppRepository.getInstance(getApplication());
        mTerms = mRepository.mTerms;
    }

    public void insertTerm(TermEntity newTerm){
        mRepository.insertTerm(newTerm);
    }

    public void deleteTerm(int termId){
        mRepository.deleteTerm(mRepository.getTermById(termId));
    }



    public void loadData(final int termId){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                TermEntity term = mRepository.getTermById(termId);
                mLiveTerm.postValue(term);
            }
        });
    }

    public void saveTerm(TermEntity termEntity){
        TermEntity term = mLiveTerm.getValue();
        if(term == null){

        }
    }

    public void deleteTerm(){
        TermEntity term = mLiveTerm.getValue();
        mRepository.deleteTerm(term);

    }

}
