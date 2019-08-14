package com.example.c196project.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface AssessmentDAO {

    @Insert
    void insert(AssessmentEntity assessmentEntity);

    @Update
    void updateAssessment(AssessmentEntity assessmentEntity);

    @Delete
    void delete(AssessmentEntity assessmentEntity);

 //   @Query("DELETE FROM assess_table")
 //   void deleteAll();

//    @Query("SELECT * FROM assess_table ORDER BY assessEnd")
//    LiveData<ArrayList<AssessmentEntity>> getAll();




    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAssessment(AssessmentEntity assessmentEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(ArrayList<AssessmentEntity> assessmentEntities);

    @Delete
    void deleteAssessment(AssessmentEntity assessmentEntity);

    @Query("SELECT * FROM assess_table WHERE assess_id = :id")
    AssessmentEntity getAssessmentById(int id);

    @Query("SELECT * FROM assess_table WHERE course_id = :id")
    List<AssessmentEntity> getCourseAssessments(int id);

    @Query("SELECT * FROM assess_table ORDER BY assess_due")
    LiveData<List<AssessmentEntity>> getAll();

    @Query("DELETE FROM assess_table")
    int deleteAll();

    @Query("DELETE FROM assess_table WHERE course_id = :id")
    int deleteAssessments(int id);

    @Query("SELECT COUNT(*) FROM assess_table")
    int getCount();

}
