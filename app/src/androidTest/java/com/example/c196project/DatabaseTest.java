package com.example.c196project;


import android.content.Context;
import android.util.Log;

import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.example.c196project.database.AlertDAO;
import com.example.c196project.database.AppDatabase;
import com.example.c196project.database.AssessmentDAO;
import com.example.c196project.database.CourseDAO;
import com.example.c196project.database.TermDAO;
import com.example.c196project.database.TermEntity;
import com.example.c196project.utilities.SampleData;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {

    public static String TAG = "JUnit";
    private AppDatabase mDb;
    private AlertDAO alertDAO;
    private AssessmentDAO assessmentDAO;
    private CourseDAO courseDAO;
    private TermDAO termDAO;

    @Before
    public void createDb(){
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();

        alertDAO = mDb.alertDAO();
        assessmentDAO = mDb.assessmentDAO();
        courseDAO = mDb.courseDAO();
        termDAO = mDb.termDAO();

        Log.i(TAG, "createDB");
    }

    @After
    public void close(){
        mDb.close();
        Log.i(TAG, "closeDB");
    }

    @Test
    public void createAndRetrieve(){
 //       alertDAO.insertAll(SampleData.get);
 //       assessmentDAO.insertAll(SampleData.get);
 //       courseDAO.insertAll(SampleData.get);
        termDAO.insertAll((ArrayList<TermEntity>) SampleData.getTerms());

        int count = termDAO.getCount();
        Log.i(TAG, "createAndRetrieve: count= " + count);
        assertEquals(SampleData.getTerms().size(), count);
    }

}
