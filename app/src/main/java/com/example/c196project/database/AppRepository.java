package com.example.c196project.database;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.c196project.utilities.SampleData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppRepository {

    // Repository instance variable
    private static AppRepository ourInstance;
    // Live Date instance variables
    public LiveData<List<TermEntity>> mTerms;
    public LiveData<List<CourseEntity>> mCourses;
    public LiveData<List<AssessmentEntity>> mAssessments;
    public LiveData<List<AlertEntity>> mAlerts;
    // Database variable and executor
    private AppDatabase mDb;
    private Executor executor = Executors.newSingleThreadExecutor();

    // Returns instance of app repository
    public static AppRepository getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new AppRepository(context);
        }
        return ourInstance;
    }

    // Gets all database repository information
    private AppRepository(Context context) {
        mDb = AppDatabase.getInstance(context);
        mTerms = getAllTerms();
        mCourses = getAllCourses();
        mAssessments = getAllAssessments();
        mAlerts = getAllAlerts();
    }

    public void addSampleData() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                      mDb.termDAO().insertAll((ArrayList<TermEntity>) SampleData.getTerms());
            }
        });
    }

    // get methods for all live data arraylists for each object entity
    private LiveData<List<TermEntity>> getAllTerms() {
        return mDb.termDAO().getAll();
    }

    private LiveData<List<CourseEntity>> getAllCourses() {
        return mDb.courseDAO().getAll();
    }

    private LiveData<List<AssessmentEntity>> getAllAssessments() {
        return mDb.assessmentDAO().getAll();
    }

    private LiveData<List<AlertEntity>> getAllAlerts() {
        return mDb.alertDAO().getAll();
    }


    // get entity methods by id
    public TermEntity getTermById(int termId) {
        return mDb.termDAO().getTermById(termId);
    }
    public CourseEntity getCourseById(int courseId) {
        return mDb.courseDAO().getCourseById(courseId);
    }
    public AssessmentEntity getAssessmentById(int assessId) {
        return mDb.assessmentDAO().getAssessmentById(assessId);
    }
    public AlertEntity getAlertById(int alertId) {
        return mDb.alertDAO().getAlertById(alertId);
    }

    // Term insert method
    public void insertTerm(final TermEntity term) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.termDAO().insertTerm(term);
            }
        });
    }

    // Course insert method
    public void insertCourse(final CourseEntity course) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.courseDAO().insertCourse(course);
            }
        });
    }


    // Assessment insert method
    public void insertAssessment(final AssessmentEntity assessment) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.assessmentDAO().insertAssessment(assessment);
            }
        });
    }


    // Alert entity methods
    public void insertAlert(final AlertEntity alert) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.alertDAO().insertAlert(alert);
            }
        });
    }


    /**
     *  delete methods for all entity classes
     */

    // delete term methods
    public void deleteTerm(final TermEntity termEntity){
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.termDAO().deleteTerm(termEntity);
            }
        });
    }

    public void deleteAllTerms() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.termDAO().deleteAll();
            }
        });
    }

    // delete course methods
    public void deleteCourse(final CourseEntity course) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.courseDAO().deleteCourse(course);
            }
        });
    }


    public void deleteAllCourses() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.courseDAO().deleteAll();
            }
        });
    }

    // delete assessment methods
    public void deleteAssessment(final AssessmentEntity assessment) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.assessmentDAO().deleteAssessment(assessment);
            }
        });
    }


    public void deleteAllAssessments() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.assessmentDAO().deleteAll();
            }
        });
    }

    //delete alert methods
    public void deleteAlert(final AlertEntity alert) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.alertDAO().deleteAlert(alert);
            }
        });
    }


    public void deleteAllAlerts() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.alertDAO().deleteAll();
            }
        });
    }

    public LiveData<List<CourseEntity>> getTermCourses(int termId){
        LiveData<List<CourseEntity>> courses = null;
        courses = getAllCourses();


        return courses;
    }

    public List<AssessmentEntity> getCourseAssessments(int courseId){
        List<AssessmentEntity> assessments = null;

        return assessments;
    }

    public void updateTerm(TermEntity term) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.termDAO().updateTerm(term);
            }
        });
    }

    public void updateCourse(CourseEntity course) {
        executor.execute(new Runnable() {

            @Override
            public void run() {
                mDb.courseDAO().updateCourse(course);
            }
        });
    }
}
