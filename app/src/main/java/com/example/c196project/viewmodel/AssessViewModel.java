package com.example.c196project.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.c196project.database.AppRepository;
import com.example.c196project.database.AssessmentEntity;
import com.example.c196project.database.CourseEntity;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AssessViewModel extends AndroidViewModel {

    // Course Mutable/Live Data variables
    public MutableLiveData<CourseEntity> mLiveCourse = new MutableLiveData<>();
    public MutableLiveData<List<CourseEntity>> mLiveCourses = new MutableLiveData<>();
    public LiveData<CourseEntity> mCourse;
    public LiveData<List<CourseEntity>> mCourses;
    // Assessment Mutable/Live Data variables
    public MutableLiveData<AssessmentEntity> mLiveAssess = new MutableLiveData<>();
    public MutableLiveData<List<AssessmentEntity>> mLiveAssessments = new MutableLiveData<>();
    public LiveData<AssessmentEntity> mAssess;
    public LiveData<List<AssessmentEntity>> mAssessments;

    public AppRepository mRepository;
    public Executor executor = Executors.newSingleThreadExecutor();


    public AssessViewModel(@NonNull Application application) {
        super(application);

        mRepository = AppRepository.getInstance(getApplication());
        mAssessments = mRepository.mAssessments;
        mCourses = mRepository.mCourses;

    }

    public void insertAssessment(AssessmentEntity newAssess){
        mRepository.insertAssessment(newAssess);
    }

    public void loadAssessment(final int assessId){
        executor.execute(() -> {
            AssessmentEntity assessment = mRepository.getAssessmentById(assessId);
            mLiveAssess.postValue(assessment);
        });
    }

    public void deleteAssess(int assessId){
        Log.d("Assessment to delete: ", mRepository.getAssessmentById(assessId).getAssessName());
        Log.d("Assessment to del: ", mRepository.getAssessmentById(assessId).toString());
        mRepository.deleteAssess(mRepository.getAssessmentById(assessId));

    }


    public void deleteAssessments(int courseId){
        mRepository.deleteAssessments(courseId);

    }

    public void saveAssessment(AssessmentEntity assessmentEntity){
        AssessmentEntity assess = mLiveAssess.getValue();
        if(assess == null){

        }
    }

    public void updateAssessment(AssessmentEntity assessmentEntity){
        mRepository.updateAssessment(mAssess.getValue());
    }


    public void deleteAll() {
        mRepository.deleteAllAssessments();
    }


}
