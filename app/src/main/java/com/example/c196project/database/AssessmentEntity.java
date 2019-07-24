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
    @ColumnInfo(name = "assess_end")
    private Date assessEnd;
    @ColumnInfo(name = "assess_notes")
    private String assessNotes;



    // empty constructor
    @Ignore
    public AssessmentEntity() {
    }

    // constructor
    public AssessmentEntity(String assessName, String assessType, Date assessEnd, String assessNotes,
                            int courseId) {
        this.assessName = assessName;
        this.assessType = assessType;
        this.assessEnd = assessEnd;
        this.assessNotes = assessNotes;
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

    public Date getAssessEnd() {

        return assessEnd;
    }

    public String getAssessNotes() {
        return assessNotes;
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

    public void setAssessEnd(Date assessEnd) {

        this.assessEnd = assessEnd;
    }

    public void setAssessNotes(String notes) {
        this.assessNotes = notes;
    }

    public void setCourseId(int courseId){
        this.courseId = courseId;
    }

 /*   ContentValues toValues(){
        ContentValues values = new ContentValues();

        values.put(AssessmentTable.assessIdCol, assessId);
        values.put(AssessmentTable.assessNameCol, assessName);
        values.put(AssessmentTable.assessTypeCol, assessType);
        values.put(AssessmentTable.assessEndDateCol, DatabaseHelper.dateToString(assessEnd));

        return values;
    }
*/
}
