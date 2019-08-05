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
public interface CourseDAO {

    @Insert
    void insert(CourseEntity courseEntity);

    @Update
    void updateCourse(CourseEntity courseEntity);

    @Delete
    void delete(CourseEntity courseEntity);


//    @Query("DELETE FROM course_table")
//    void deleteAll();

//    @Query("SELECT * FROM course_table ORDER BY startDate")
//    LiveData<ArrayList<CourseEntity>> getAll();





    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCourse(CourseEntity courseEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(ArrayList<CourseEntity> courseEntities);

    @Delete
    void deleteCourse(CourseEntity courseEntity);

    @Query("SELECT * FROM course_table WHERE course_id = :id")
    CourseEntity getCourseById(int id);

    @Query("SELECT * FROM course_table ORDER BY start_date DESC")
    LiveData<List<CourseEntity>> getAll();

    @Query("DELETE FROM course_table")
    int deleteAll();

    @Query("SELECT COUNT(*) FROM course_table")
    int getCount();

    @Query("SELECT * FROM course_table WHERE termId = termId")
    int getCourses();

}

