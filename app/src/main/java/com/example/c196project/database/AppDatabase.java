package com.example.c196project.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {TermEntity.class, CourseEntity.class, AssessmentEntity.class, AlertEntity.class},
        version = 5, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    public static final String WGU_DB = "AppDatabase.db";

    private static AppDatabase instance;
    private static final Object LOCK = new Object();


    public abstract TermDAO termDAO();

    public abstract CourseDAO courseDAO();

    public abstract AssessmentDAO assessmentDAO();

    public abstract AlertDAO alertDAO();

    public static synchronized AppDatabase getInstance(Context context){
        if(instance == null){
            synchronized (LOCK){
                if(instance == null){
                    instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class,
                            WGU_DB)
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return instance;
    }

    public static void destroyInstance(){
        instance = null;
    }

}
