package com.example.c196project.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "assess_table")
public class AssessmentEntity{

    // Table Key Columns
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "assess_id")
    private int assessId = 0;
    @ForeignKey(entity = CourseEntity.class, parentColumns = "courseId", childColumns = "assessId")
    @ColumnInfo(name = "course_id")
    private int courseId;

    // Table Non-Key Columns
    @ColumnInfo(name = "assess_name")
    private String assessName;
    @ColumnInfo(name = "assess_type")
    private String assessType;
    @ColumnInfo(name = "assess_due")
    private Date assessDue;
    @ColumnInfo(name = "assess_alert")
    private String assessAlert;

    // empty constructor
    @Ignore
    public AssessmentEntity() {
    }

    @Ignore
    public AssessmentEntity(String assessName, String assessType, Date assessDue,
                            String assessAlert, int courseId) {
        this.assessName = assessName;
        this.assessType = assessType;
        this.assessDue = assessDue;
        this.assessAlert = assessAlert;
        this.courseId = courseId;
    }

    // constructor
    public AssessmentEntity(int assessId, String assessName, String assessType, Date assessDue,
                            String assessAlert, int courseId) {
        this.assessId = assessId;
        this.assessName = assessName;
        this.assessType = assessType;
        this.assessDue = assessDue;
        this.assessAlert = assessAlert;
        this.courseId = courseId;
    }

    // getters
    public int getAssessId() {

        return assessId;
    }

    public String getAssessName() {

        return assessName;
    }

    public String getAssessType() {

        return assessType;
    }

    public Date getAssessDue() {

        return assessDue;
    }

    public String getAssessAlert() {
        return assessAlert;
    }

    public int getCourseId(){
        return courseId;
    }

    // setters
    public void setAssessId(int assessId) {

        this.assessId = assessId;
    }

    public void setAssessName(String assessName) {

        this.assessName = assessName;
    }

    public void setAssessType(String assessType) {

        this.assessType = assessType;
    }

    public void setAssessDue(Date assessDue) {

        this.assessDue = assessDue;
    }

    public void setAssessAlert(String alert) {
        this.assessAlert = alert;
    }

    public void setCourseId(int courseId){
        this.courseId = courseId;
    }

}
