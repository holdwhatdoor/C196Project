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

public class CourseViewModel extends AndroidViewModel {

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

    public CourseViewModel(@NonNull Application application) {
        super(application);

        mRepository = AppRepository.getInstance(getApplication());
        mAssessments = mRepository.mAssessments;
        mCourses = mRepository.mCourses;

    }

    public void insertCourse(CourseEntity newCourse){
        mRepository.insertCourse(newCourse);
    }

    public void loadCourse(final int courseId){
        executor.execute(() -> {
            CourseEntity course = mRepository.getCourseById(courseId);
            mLiveCourse.postValue(course);
        });
    }

    public void deleteCourse(int courseId){
        Log.d("Course to delete: ", mRepository.getCourseById(courseId).getCourseTitle());
        Log.d("Course data to del: ", mRepository.getCourseById(courseId).toString());
        mRepository.deleteCourse(mRepository.getCourseById(courseId));

    }

    public void deleteCourses(int termId){
        mRepository.deleteCourses(termId);
    }

    public void deleteCourse(){
        mRepository.deleteCourse(mCourse.getValue());

    }

    public void saveCourse(CourseEntity courseEntity){
        CourseEntity course = mLiveCourse.getValue();
        if(course == null){

        }
    }

    public void updateCourse(CourseEntity courseEntity){
        mRepository.updateCourse(mCourse.getValue());
    }

    public void deleteAll() {
        mRepository.deleteAllCourses();
    }


}
