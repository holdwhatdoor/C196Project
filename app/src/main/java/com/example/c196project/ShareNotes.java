package com.example.c196project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

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

public class ShareNotes extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "ShareNotes";

    // View model instantiate

    // Input elements
    public EditText sendTo;
    public EditText subject;
    public EditText message;

    // Button instantiation
    public Button sendBtn;
    public Button cancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share);

        // retrieves course data passed from CourseNotes class
        Bundle extras = getIntent().getExtras();
        // passed CourseEntity data
        int courseId = extras.getInt(COURSE_ID_KEY);
        String courseTitle = extras.getString(COURSE_TITLE_KEY);
        String courseStart = extras.getString(COURSE_START_KEY);
        String courseEnd = extras.getString(COURSE_END_KEY);
        String courseMentor = extras.getString(COURSE_MENTOR_KEY);
        String mentorEmail = extras.getString(COURSE_EMAIL_KEY);
        String mentorPhone = extras.getString(COURSE_PHONE_KEY);
        String courseStatus = extras.getString(COURSE_STATUS_KEY);
        String courseNotes = extras.getString(COURSE_NOTES_KEY);
        String alertStart = extras.getString(COURSE_ALERT_START_KEY);
        String alertEnd = extras.getString(COURSE_ALERT_END_KEY);
        int termId = extras.getInt(TERM_ID_KEY);

        String blankLine = "\r\n";

        sendTo = findViewById(R.id.to_edit);
        subject = findViewById(R.id.subject_edit);
        message = findViewById(R.id.message_edit);

        sendTo.setText(mentorEmail);
        subject.setText(courseTitle + " Notes");
        message.setText("COURSE NOTES: " + blankLine + blankLine + courseNotes);

        sendBtn = findViewById(R.id.send_btn);
        sendBtn.setOnClickListener(v -> sendMail());

        cancelBtn = findViewById(R.id.send_cancel_btn);
        cancelBtn.setOnClickListener(v -> {
            Intent intent = new Intent (v.getContext(), CourseNotes.class);

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
        });

    }

    private void sendMail(){
        String recipientList = sendTo.getText().toString();
        String[] recipients = recipientList.split(",");

        String subjectTxt = subject.getText().toString();
        String messageTxt = message.getText().toString();

        Intent intent = new Intent(Intent.ACTION_SEND);

        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT, subjectTxt);
        intent.putExtra(Intent.EXTRA_TEXT, messageTxt);

        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Select email client"));
    }

    @Override
    public void onClick(View v) {

    }
}
