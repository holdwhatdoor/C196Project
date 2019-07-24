package com.example.c196project.database;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "alert_table")
public class AlertEntity {

    // Table Key Columns
    @PrimaryKey(autoGenerate= true)
    @ColumnInfo(name = "alert_id")
    private int alertId = 0;
    @ForeignKey(entity = CourseEntity.class, parentColumns = "courseId", childColumns = "alertId")
    @ColumnInfo(name = "course_id")
    private int courseId;

    // Table Non-Key Columns
    @ColumnInfo(name = "assess_type")
    private String assessType;
    @ColumnInfo(name = "alert_date")
    private Date alertDate;
    @ColumnInfo(name = "alert_time")
    private String alertTime;
    @ColumnInfo(name = "alert_descr")
    private String alertDescr;


    // empty constructor
    @Ignore
    public AlertEntity() {
    }

    // constructor
    public AlertEntity(int alertId, String assessType, Date alertDate, String alertTime, String alertDescr, int courseId) {
        this.alertId = alertId;
        this.assessType = assessType;
        this.alertDate = alertDate;
        this.alertTime = alertTime;
        this.alertDescr = alertDescr;
        this.courseId = courseId;
    }

    // Getters
    public int getAlertId() {
        return alertId;
    }

    public String getAssessType() {
        return assessType;
    }

    public Date getAlertDate() {
        return alertDate;
    }

    public String getAlertTime() {
        return alertTime;
    }

    public String getAlertDescr() {
        return alertDescr;
    }

    public int getCourseId() {
        return courseId;
    }

    // Setters
    public void setAlertId(int alertId) {
        this.alertId = alertId;
    }

    public void setAssessType(String assessType) {
        this.assessType = assessType;
    }

    public void setAlertDate(Date alertDate) {
        this.alertDate = alertDate;
    }

    public void setAlertTime(String alertTime) {
        this.alertTime = alertTime;
    }

    public void setAlertDescr(String alertDescr) {
        this.alertDescr = alertDescr;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }


}