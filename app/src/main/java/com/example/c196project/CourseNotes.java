package com.example.c196project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.c196project.database.CourseEntity;
import com.example.c196project.database.DateConverter;
import com.example.c196project.viewmodel.CourseViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;

import static com.example.c196project.utilities.Constants.COURSE_ALERT_END_KEY;
import static com.example.c196project.utilities.Constants.COURSE_ALERT_START_KEY;
import static com.example.c196project.utilities.Constants.COURSE_EMAIL_KEY;
import static com.example.c196project.utilities.Constants.COURSE_END_KEY;
import static com.example.c196project.utilities.Constants.COURSE_ID_KEY;
import static com.example.c196project.utilities.Constants.COURSE_MENTOR_KEY;
import static com.example.c196project.utilities.Constants.COURSE_NOTES_KEY;
import static com.example.c196project.utilities.Constants.COURSE_PHONE_KEY;
import static com.example.c196project.utilities.Constants.COURSE_START_KEY;
import static com.example.c196project.utilities.Constants.COURSE_STATUS_KEY;
import static com.example.c196project.utilities.Constants.COURSE_TITLE_KEY;
import static com.example.c196project.utilities.Constants.TERM_ID_KEY;

public class CourseNotes extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "CourseNotes";

    // View Model instantiate
    public CourseViewModel courseVM;
    // Course Data list
    public List<CourseEntity> courseData = new ArrayList<>();
    // Course Notes components
    public EditText noteText;

    // Button instantiation
    public Button saveBtn;
    public Button clearBtn;
    public Button cancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note);

        // retrieves course data passed from CourseEdit class
        Bundle extras = getIntent().getExtras();
        // passed CourseEntity data
        int courseId = extras.getInt(COURSE_ID_KEY);
        String courseTitle = extras.getString(COURSE_TITLE_KEY);
        String courseStart = extras.getString(COURSE_START_KEY);
        String courseEnd = extras.getString(COURSE_END_KEY);
        String formatStart = DateConverter.formatDateString(courseStart);
        String formatEnd = DateConverter.formatDateString(courseEnd);
        Date cStart = DateConverter.toDate(formatStart);
        Date cEnd = DateConverter.toDate(formatEnd);
        String courseMentor = extras.getString(COURSE_MENTOR_KEY);
        String mentorEmail = extras.getString(COURSE_EMAIL_KEY);
        String mentorPhone = extras.getString(COURSE_PHONE_KEY);
        String courseStatus = extras.getString(COURSE_STATUS_KEY);
        String courseNotes = extras.getString(COURSE_NOTES_KEY);
        String alertStart = extras.getString(COURSE_ALERT_START_KEY);
        String alertEnd = extras.getString(COURSE_ALERT_END_KEY);
        int termId = extras.getInt(TERM_ID_KEY);

        noteText = findViewById(R.id.note_text);
        noteText.setText(courseNotes);

        saveBtn = findViewById(R.id.note_save_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newNote = noteText.getText().toString();

                CourseEntity updatedCourse = new CourseEntity(courseId, courseTitle, cStart, cEnd, courseMentor,
                        mentorEmail, mentorPhone, courseStatus, newNote, alertStart, alertEnd, termId);

                Log.d(TAG, "Updated Course: " + updatedCourse);
                // call update to update course and return back to course edit
                Intent intent = new Intent(v.getContext(), CourseEdit.class);

                intent.putExtra(COURSE_ID_KEY, courseId);
                intent.putExtra(COURSE_TITLE_KEY, courseTitle);
                intent.putExtra(COURSE_START_KEY, courseStart);
                intent.putExtra(COURSE_END_KEY, courseEnd);
                intent.putExtra(COURSE_MENTOR_KEY, courseMentor);
                intent.putExtra(COURSE_EMAIL_KEY, mentorEmail);
                intent.putExtra(COURSE_PHONE_KEY, mentorPhone);
                intent.putExtra(COURSE_STATUS_KEY, courseStatus);
                intent.putExtra(COURSE_NOTES_KEY, newNote);
                intent.putExtra(COURSE_ALERT_START_KEY, alertStart);
                intent.putExtra(COURSE_ALERT_END_KEY, alertEnd);
                intent.putExtra(TERM_ID_KEY, termId);


                v.getContext().startActivity(intent);

                courseVM.insertCourse(updatedCourse);
                finish();
            }
        });

        clearBtn = findViewById(R.id.note_clear_btn);
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noteText.setText("");
            }
        });

        cancelBtn = findViewById(R.id.note_cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), CourseEdit.class);

                intent.putExtra(COURSE_ID_KEY, courseId);
                intent.putExtra(COURSE_TITLE_KEY, courseTitle);
                intent.putExtra(COURSE_START_KEY, courseStart);
                intent.putExtra(COURSE_END_KEY, courseEnd);
                intent.putExtra(COURSE_MENTOR_KEY, courseMentor);
                intent.putExtra(COURSE_EMAIL_KEY, mentorEmail);
                intent.putExtra(COURSE_PHONE_KEY, mentorPhone);
                intent.putExtra(COURSE_STATUS_KEY, courseStatus);
                intent.putExtra(COURSE_NOTES_KEY, courseNotes);
                intent.putExtra(COURSE_ALERT_START_KEY, alertStart);
                intent.putExtra(COURSE_ALERT_END_KEY, alertEnd);
                intent.putExtra(TERM_ID_KEY, termId);

                v.getContext().startActivity(intent);
            }
        });


        // Initialize butterknife and initViewModel
        ButterKnife.bind(this);
        initViewModel();


    }

    private void initViewModel() {
        courseVM = ViewModelProviders.of(this)
                .get(CourseViewModel.class);

        courseVM.mLiveCourse.observe(this, (courseEntity -> {
            if(courseEntity != null){
                noteText.setText(courseEntity.getCourseNotes());
            }
        }));

        final Observer<CourseEntity> courseEntityObserver =
                courseEntity -> {
                    courseData.clear();
                    courseData.add(courseEntity);
                };
        Log.d(TAG, "Course Data: " + courseData);
    }


    @Override
    public void onClick(View v) {

    }
}
