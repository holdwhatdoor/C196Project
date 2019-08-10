package com.example.c196project.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;


@Entity(tableName = "term_table")
public class TermEntity {

    // Table Key Columns
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "term_id")
    private int termId = 0;

    // Table Non-Key Columns
    @ColumnInfo(name = "term_title")
    private String termTitle;
    @ColumnInfo(name = "start")
    private Date start;
    @ColumnInfo(name = "end")
    private Date end;

    // empty constructor
    @Ignore
    public TermEntity() {

    }

    @Ignore
    // constructor for insert no id integer
    public TermEntity(String termTitle, Date start, Date end){
        this.termTitle = termTitle;
        this.start = start;
        this.end = end;
    }


    // constructor
    public TermEntity(int termId, String termTitle, Date start, Date end) {
        this.termId = termId;
        this.termTitle = termTitle;
        this.start = start;
        this.end = end;
    }

    // Getters
    public int getTermId() {
        return termId;
    }

    public String getTermTitle() {
        return termTitle;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    // Setters
    public void setTermId(int termId) {
        this.termId = termId;
    }

    public void setTermTitle(String termTitle) {
        this.termTitle = termTitle;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    @Override
    public String toString(){
        return "TermEntity{" + "TermId= " + termId + ", Term Title= " + termTitle + ", Term Start= " +
                start + ", Term End= " + end + "}";
    }
}
