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
public interface TermDAO {

    @Insert
    void insert (TermEntity termEntity);

    @Update
    void update (TermEntity termEntity);

    @Delete
    void deleteTerm (TermEntity termEntity);

//    @Query("DELETE FROM term_table")
//    void deleteAll();

//    @Query("SELECT * FROM term_table ORDER BY start")
//    LiveData<ArrayList<TermEntity>> getAll();



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTerm(TermEntity termEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(ArrayList<TermEntity> notes);

    @Query("SELECT * FROM term_table WHERE term_id = :id")
    TermEntity getTermById(int id);

    @Query("SELECT * FROM term_table ORDER BY start DESC")
    LiveData<List<TermEntity>> getAll();

    @Query("DELETE FROM term_table")
    int deleteAll();

    @Query("SELECT COUNT(*) FROM term_table")
    int getCount();



}


