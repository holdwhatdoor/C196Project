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
public interface AlertDAO {


    @Insert
    void insert (AlertEntity termEntity);

    @Update
    void update (AlertEntity termEntity);

    @Delete
    void delete (AlertEntity termEntity);

//    @Query("DELETE FROM alert_table")
//    void deleteAll();

//    @Query("SELECT * FROM alert_table ORDER BY alertDate")
//    LiveData<ArrayList<AlertEntity>> getAll();



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAlert(AlertEntity alertEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(ArrayList<AlertEntity> alertEntities);

    @Delete
    void deleteAlert(AlertEntity alertEntity);

    @Query("SELECT * FROM alert_table WHERE alert_id = :id")
    AlertEntity getAlertById(int id);

    @Query("SELECT * FROM alert_table ORDER BY alert_date DESC")
    LiveData<List<AlertEntity>> getAll();

    @Query("DELETE FROM alert_table")
    int deleteAll();

    @Query("SELECT COUNT(*) FROM alert_table")
    int getCount();

}
