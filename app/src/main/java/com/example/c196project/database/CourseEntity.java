package com.example.c196project.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "course_table")
public class CourseEntity {

    // Table Key columns
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "course_id")
    private int courseId = 0;
    @ForeignKey(entity = TermEntity.class, parentColumns = "term_Id", childColumns = "course_Id")
    private int termId;

    // Table Non-Key columns
    @ColumnInfo(name = "course_title")
    private String courseTitle;
    @ColumnInfo(name = "start_date")
    private Date startDate;
    @ColumnInfo(name = "end_date")
    private Date endDate;
    @ColumnInfo(name = "status")
    private String status;
    @ColumnInfo(name = "mentor_name")
    private String mentorName;
    @ColumnInfo(name = "mentor_phone")
    private String mentorPhone;
    @ColumnInfo(name = "mentor_email")
    private String mentorEmail;
    @ColumnInfo(name = "course_notes")
    private String courseNotes;

    // empty constructor
    @Ignore
    public CourseEntity() {

    }

    @Ignore
    public CourseEntity(String courseTitle, Date startDate, Date endDate, String status,
                        String mentorName, String mentorPhone, String mentorEmail, String courseNotes, int termId){
        this.courseTitle = courseTitle;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.mentorName = mentorName;
        this.mentorPhone = mentorPhone;
        this.mentorEmail = mentorEmail;
        this.courseNotes = courseNotes;
        this.termId = termId;
    }

    public CourseEntity(int courseId, String courseTitle, Date startDate, Date endDate, String status,
                        String mentorName, String mentorPhone, String mentorEmail, String courseNotes, int termId) {

        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.mentorName = mentorName;
        this.mentorPhone = mentorPhone;
        this.mentorEmail = mentorEmail;
        this.courseNotes = courseNotes;
        this.termId = termId;
    }


    // getters
    public int getCourseId() {

        return courseId;
    }

    public String getCourseTitle() {

        return courseTitle;
    }

    public Date getStartDate() {

        return startDate;
    }

    public Date getEndDate() {

        return endDate;
    }

    public String getStatus() {

        return status;
    }

    public String getMentorName() {
        return mentorName;
    }

    public String getMentorPhone() {
        return mentorPhone;
    }

    public String getMentorEmail() {
        return mentorEmail;
    }

    public String getCourseNotes() {
        return courseNotes;
    }

    public int getTermId(){
        return termId;
    }

    // setters
    public void setCourseId(int courseId) {

        this.courseId = courseId;
    }

    public void setCourseTitle(String courseTitle) {

        this.courseTitle = courseTitle;
    }

    public void setStartDate(Date startDate) {

        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {

        this.endDate = endDate;
    }

    public void setStatus(String status) {

        this.status = status;
    }

    public void setMentorName(String mentorName) {
        this.mentorName = mentorName;
    }

    public void setMentorPhone(String mentorPhone) {
        this.mentorPhone = mentorPhone;
    }

    public void setMentorEmail(String mentorEmail) {
        this.mentorEmail = mentorEmail;
    }

    public void setTermId(int termId){
        this.termId = termId;
    }

/*    ContentValues toCourseValues() {
        ContentValues values = new ContentValues(5);

        values.put(CourseTable.courseIdCol, courseId);
        values.put(CourseTable.courseTitleCol, courseTitle);
        values.put(CourseTable.courseStartCol, DatabaseHelper.dateToString(startDate));
        values.put(CourseTable.courseEndCol, DatabaseHelper.dateToString(endDate));
        values.put(CourseTable.courseStatusCol, status);

        return values;
    }*/

}
