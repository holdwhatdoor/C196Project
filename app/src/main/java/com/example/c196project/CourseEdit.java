package com.example.c196project;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.c196project.database.AssessmentEntity;
import com.example.c196project.database.CourseEntity;
import com.example.c196project.database.DateConverter;
import com.example.c196project.database.TermEntity;
import com.example.c196project.ui.CourseEditAdapter;
import com.example.c196project.viewmodel.AssessViewModel;
import com.example.c196project.viewmodel.CourseViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.c196project.utilities.Constants.ASSESS_DUE_KEY;
import static com.example.c196project.utilities.Constants.ASSESS_ID_KEY;
import static com.example.c196project.utilities.Constants.ASSESS_TITLE_KEY;
import static com.example.c196project.utilities.Constants.ASSESS_TYPE_KEY;
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

public class CourseEdit extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = "CourseEdit";

    // Header Variables
    public TextView pageTitle;
    public ImageButton homeBtn;  // id = home_btn_term

    // View Models
    public CourseViewModel courseVM;
    public AssessViewModel assessVM;
    // Term data array list
    private List<TermEntity> termData = new ArrayList<>();
    // Course data array lists and adapters
    private List<CourseEntity> courseData = new ArrayList<>();
    private CourseEditAdapter mCourseAdapter;
    private List<CourseEntity> allCourses = new ArrayList<>();
    //Assessment data array lists and adapters
    private List<AssessmentEntity> assessData = new ArrayList<>();
    private CourseEditAdapter mAssessAdapter;

    // Initialized Calendar date variable
    Calendar calendar = Calendar.getInstance();
    // SimpleDateFormat initialization for all date display items
    public SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

    /**
     * Edit Course data display/input elements
     */
    public EditText courseTitleEdit;                                // id = ce_courseTitle
    // Course declarations for start/end date EditText with DatePickerDialog listeners and SimpleDateFormat
    public EditText courseStartDate;                                // id = ce_courseStart
    public DatePickerDialog.OnDateSetListener courseStartListener;
    public EditText courseEndDate;                                  // id = ce_courseEnd
    public DatePickerDialog.OnDateSetListener courseEndListener;
    // Add course mentor info
    public EditText courseMentorEdit;                                   // id = ce_ courseMentor
    public EditText mentorPhoneEdit;                                    // id = ce_mentorPhone
    public EditText mentorEmailEdit;                                    // id = ce_mentorEmail
    public Spinner courseSpinner;                                   // id = ce_courseStatus
    public String[] courseOptions = {"No Selection", "Planned", "In Progress", "Completed", "Dropped"};
    public ArrayAdapter spinAdapter;
    public CheckBox startAlert;
    public CheckBox endAlert;

    /**
     * Add Assessment input data display/imput elements
     */
    public EditText assessTitleInput;                               // id = ce_assessTitle
    // Add Course declarations for start/end date EditText with DatePickerDialog listeners
    public EditText assessDueDate;                                // id = ce_assessStart
    public DatePickerDialog.OnDateSetListener assessDueDateListener;
    // Checkbox for setting alert
    public CheckBox assessDueAlert;

    // Radio button group and selections for Assessment type
    public RadioGroup assessType;                                   // id = ce_assessRadio_grp
    public RadioButton assessOA;                                    // id = ce_oaRadio
    public RadioButton assessPA;                                    // id = ce_paRadio
    // Spinner for status of assessments assigned
    public Spinner assessStatus;                                    // id = ce_
    public String[] assessOptions = {"No Selection", "Dropped", "Failed", "Passed"};

    // Button variables
    public Button saveBtn;
    public Button addNoteBtn;
    public Button delCourseBtn;
    public Button addAssessBtn;
    public Button delAssessmentsBtn;

    // Recycler view components
    @BindView(R.id.rv_assess_list)
    public RecyclerView assessRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_edit);

        // retrieves course data passed from adapter
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
        String formatStart = DateConverter.formatDateString(courseStart);
        String formatEnd = DateConverter.formatDateString(courseEnd);
        Date cStartDate = DateConverter.toDate(formatStart);
        Date cEndDate = DateConverter.toDate(formatEnd);
        String alertStart = extras.getString(COURSE_ALERT_START_KEY);
        String alertEnd = extras.getString(COURSE_ALERT_END_KEY);
        int termId = extras.getInt(TERM_ID_KEY);

        Log.d(TAG, "Course Edit received Course Title: " + courseTitle);
        Log.d(TAG, "Course Edit received Course Start: " + courseStart);
        Log.d(TAG, "Course Edit received Course End: " + courseEnd);
        Log.d(TAG, "Course Edit received Course Mentor: " + courseMentor);
        Log.d(TAG, "Course Edit received Mentor Email: " + mentorEmail);
        Log.d(TAG, "Course Edit received Mentor Phone: " + mentorPhone);
        Log.d(TAG, "Course Edit received Course Status: " + courseStatus);
        Log.d(TAG, "Course Edit received Course Notes: " + courseNotes);
        // AssessmentEntity data
        int assessId = extras.getInt(ASSESS_ID_KEY);
        String assessTitle = extras.getString(ASSESS_TITLE_KEY);
        String type = extras.getString(ASSESS_TYPE_KEY);
        String assessDue = extras.getString(ASSESS_DUE_KEY);

        // converts dates to MM/dd/yyyy format
        String courseStartFormat = DateConverter.formatDateString(courseStart);
        String courseEndFormat = DateConverter.formatDateString(courseEnd);
        String assessDueFormat = DateConverter.formatDateString(assessDue);

        // instantiate/set page title
//        TextView title = (TextView) findViewById(R.id.title);
//        title.setText("Edit Course");

        // header element id assignments
        pageTitle = findViewById(R.id.app_bar_title);
        pageTitle.setText("Edit Course");
        homeBtn = findViewById(R.id.appBar_homeBtn);
        homeBtn.setOnClickListener(this);

        // initialize butterknife, initRecyclerView and intViewModel
        ButterKnife.bind(this);
        initRecyclerView();
        initViewModel();

        /**
         *  Course Edit input
         */
        // Course input element id assignments
        courseTitleEdit = findViewById(R.id.ce_courseTitle);
        courseStartDate = findViewById(R.id.ce_courseStart);
        courseEndDate = findViewById(R.id.ce_courseEnd);
        courseMentorEdit = findViewById(R.id.ce_mentorName);
        mentorEmailEdit = findViewById(R.id.ce_mentorEmail);
        mentorPhoneEdit = findViewById(R.id.ce_mentorPhone);
        courseSpinner = findViewById(R.id.ce_courseStatus);
        courseSpinner.setOnItemSelectedListener(this);
        spinAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,
                courseOptions);
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseSpinner.setAdapter(spinAdapter);
        startAlert = findViewById(R.id.ce_start_alert);
        endAlert = findViewById(R.id.ce_end_alert);


        /**
         *  Start and End date EditText ids and onClick override functionality
         */
        //  Initialized Course DatePickerDialog date listener start/end dates
        DatePickerDialog.OnDateSetListener cStart = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            DateConverter.updateDateText(courseStartDate, calendar);
        };
        DatePickerDialog.OnDateSetListener cEnd = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            DateConverter.updateDateText(courseEndDate, calendar);
        };
        // Course start date setOnClickListener
        courseStartDate.setOnClickListener(v -> new DatePickerDialog(CourseEdit.this, cStart, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar
                .get(Calendar.DAY_OF_MONTH)).show());
        // Course end date setOnClickListener
        courseEndDate.setOnClickListener(v -> new DatePickerDialog(CourseEdit.this, cEnd, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar
                .get(Calendar.DAY_OF_MONTH)).show());
        // Term start date listener functionality
        courseStartListener = (view, year, month, dayOfMonth) -> {
            Log.d(TAG, "courseStartDateSet: mm/dd/yyyy: " + month + "/" + dayOfMonth + "/" + year);

            String startDate = month + "/" + dayOfMonth + "/" + year;
            courseStartDate.setText(startDate);
        };
        // Course end date listener functionality
        courseEndListener = (view, year, month, dayOfMonth) -> {
            Log.d(TAG, "courseEndDateSet: mm/dd/yyyy: " + month + "/" + dayOfMonth + "/" + year);

            String endDate = month + "/" + dayOfMonth + "/" + year;
            courseEndDate.setText(endDate);
        };

        // Set edit text with selected course information
        courseTitleEdit.setText(courseTitle);
        courseMentorEdit.setText(courseMentor);
        courseStartDate.setText(courseStartFormat);
        courseEndDate.setText(courseEndFormat);
        mentorEmailEdit.setText(mentorEmail);
        mentorPhoneEdit.setText(mentorPhone);
        Log.d(TAG, "Status selection ID: " + getStatusSelectionId(courseStatus));
        courseSpinner.setSelection(getStatusSelectionId(courseStatus));
        setAlertsChecked(alertStart, alertEnd);

        /**
         *  Course button id assignments/functionality
         */
        // Save updated course input info assignment/function
        saveBtn = findViewById(R.id.ce_courseSave);
        saveBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (courseData != null) {
                    for (int i = 0; i < courseData.size(); i++) {
                        Log.d(TAG, "Course name: " + courseData.get(i).getCourseTitle());
                    }
                }
                if (termData != null) {
                    for (int i = 0; i < termData.size(); i++) {
                        Log.d(TAG, "term name: " + termData.get(i).getTermTitle());
                    }
                }
                if (assessData != null) {
                    for (int i = 0; i < assessData.size(); i++) {
                        Log.d(TAG, "Assess Name: " + assessData.get(i).getAssessName());
                    }
                }

                CourseEntity course = getPassedCourse();
                Log.d(TAG, "selected course id:" + course);
                Log.d(TAG, "selected course name:" + course);
                Log.d(TAG, "selected course start:" + course);
                Log.d(TAG, "selected course end:" + course);
                Log.d(TAG, "selected course :" + course);
                Log.d(TAG, "selected course :" + course);
                Log.d(TAG, "selected course :" + course);
                Log.d(TAG, "selected course :" + course);
                Log.d(TAG, "selected course :" + course);



                Log.d(TAG, "COURSE DATA: " + courseData);
                Log.d(TAG, "Assessment Data: " + assessData);
                Log.d(TAG, "Term Data: " + termData);
            }
        });

        // Add note button assignment/function
        addNoteBtn = findViewById(R.id.ce_noteBtn);
        addNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CourseNotes.class);

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

        // Delete course button assignment/function
        delCourseBtn = findViewById(R.id.ce_delCourse);
        delCourseBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "Assess Data: " + assessData.toString());
                if (!assessData.isEmpty()) {
                    deleteCourseError();
                } else {
                    courseVM.deleteCourse(courseId);
                    finish();
                }
            }
        });


        /**
         *   Assessment inputs
         */
        // Assessment input element id assignments
        assessTitleInput = findViewById(R.id.ce_assessTitle);
        assessType = findViewById(R.id.ce_assessRadio_grp);
        assessOA = findViewById(R.id.ce_oaRadio);
        assessPA = findViewById(R.id.ce_paRadio);
        assessDueDate = findViewById(R.id.ce_assessDue);
        assessDueAlert = findViewById(R.id.ce_due_alert);

        /**
         *  Due date EditText id and onClick override functionality
         */
        //  Initialized Course DatePickerDialog date listener start/end dates
        DatePickerDialog.OnDateSetListener aDue = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            DateConverter.updateDateText(assessDueDate, calendar);
        };
        // Assessment due date setOnClickListener
        assessDueDate.setOnClickListener(v -> new DatePickerDialog(CourseEdit.this, aDue, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar
                .get(Calendar.DAY_OF_MONTH)).show());
        //  Assessment due date listener functionality
        assessDueDateListener = (view, year, month, dayOfMonth) -> {
            Log.d(TAG, "assessDueDateSet: mm/dd/yyyy: " + month + "/" + dayOfMonth + "/" + year);

            String startDate = month + "/" + dayOfMonth + "/" + year;
            courseStartDate.setText(startDate);
        };

        // Assessment Button id assignments/functionality
        addAssessBtn = findViewById(R.id.ce_addAssess);
        addAssessBtn.setOnClickListener(v -> {

            TimeZone localTZ = TimeZone.getDefault();
            Locale locale = Locale.getDefault();

            Date today = Calendar.getInstance(localTZ, locale).getTime();
//delete below here
            if(assessData != null) {
                for(int i = 0; i < assessData.size(); i++){
                    Log.d(TAG, "Assess Name: " + assessData.get(i).getAssessName());
                    Log.d(TAG, "Assess courseID: " + assessData.get(i).getCourseId());
                }
            }

            if(courseData != null) {
                for(int i = 0; i < courseData.size(); i++){
                    Log.d(TAG, "Course Name: " + courseData.get(i).getCourseTitle());

                }
            }
// stop delete here
            Log.d(TAG, "Assess Data: " + assessData);

            try {

                if (TextUtils.isEmpty(assessTitleInput.getText())) {
                    CourseEdit.this.assessNoInputAlert();
                } else if (!assessOA.isChecked() && !assessPA.isChecked()) {
                    CourseEdit.this.assessNoType();
                } else if (assessDueDate.getText().toString().equals("") ||
                        assessDueDate.getText().toString().equals("mm/dd/yyyy") ||
                        assessDueDate.equals(null)) {
                    assessNoInputAlert();
                } else {

                    String assess = assessTitleInput.getText().toString();
                    String dueString = assessDueDate.getText().toString();
                    Log.d(TAG, "Assess String Due Date: " + dueString);
                    Date due = DateConverter.toDate(dueString);
                    Log.d(TAG, "Assess Date Due Date: " + due);
                    String selectedType = getSelectedAssessmentType();
                    String dueAlert = "not set";
                    if (assessDueAlert.isChecked()) {
                        dueAlert = "set";
                    }
                    int courseID = courseId;

                    Log.d(TAG, "cEndDate: " + cEndDate + ", due: " + due + ", cStartDate: " +
                            cStartDate);

                    if (due.before(cEndDate) && due.after(cStartDate) && !assessmentConflict(due)) {
                        AssessmentEntity assessment = new AssessmentEntity(assess, selectedType, due,
                                dueAlert, courseID);

                        assessVM.insertAssessment(assessment);

                        assessTitleInput.getText().clear();
                        assessDueDate.getText().clear();
                        assessDueAlert.setChecked(false);

                        assessTitleInput.setHint("Enter Assessment Name");
                        assessDueDate.setHint("mm/dd/yyyy");
                        assessType.clearCheck();
                        Log.d(TAG, "Assessment insert complete.");

                    } else {
                        assessConflictAlert();
                    }
                }
            } catch (Exception ex) {

            }

        });
        delAssessmentsBtn = findViewById(R.id.ce_delAssess);
        delAssessmentsBtn.setOnClickListener(v -> {
            assessVM.deleteAssessments(courseId);
        });

    }

    // Initialize view model
    private void initViewModel() {
        courseVM = ViewModelProviders.of(this)
                .get(CourseViewModel.class);
        courseVM.mLiveCourse.observe(this, (courseEntity) -> {
            if (courseEntity != null) {
                Calendar calendar = Calendar.getInstance();
                courseTitleEdit.setText(courseEntity.getCourseTitle());
                courseStartDate.setText(DateConverter.formatDateString(courseEntity.getStartDate().toString()));
                courseEndDate.setText(DateConverter.formatDateString(courseEntity.getEndDate().toString()));
                courseMentorEdit.setText(courseEntity.getMentorName());
                Log.d(TAG, "Course Mentor EditText set to: " + courseMentorEdit.getText());
                mentorEmailEdit.setText(courseEntity.getMentorEmail());
                Log.d(TAG, "Course Email EditText set to: " + mentorEmailEdit.getText());
                mentorPhoneEdit.setText(courseEntity.getMentorPhone());
                Log.d(TAG, "Course Phone EditText set to: " + mentorPhoneEdit.getText());
                int spinnerPos = spinAdapter.getPosition(courseEntity.getStatus());
                courseSpinner.setSelection(spinnerPos);
                Log.d(TAG, "Course Status Spinner set to: " + courseSpinner.getSelectedItem().toString());
                if (courseEntity.getAlertStart().equals("set")) {
                    startAlert.setChecked(true);
                }
                if (courseEntity.getAlertEnd().equals("set")) {
                    endAlert.setChecked(true);
                }
            }
        });

        final Observer<List<AssessmentEntity>> assessObserver =
                assessmentEntities -> {

                    int courseId = getPassedCourse().getCourseId();
                    Log.d(TAG, "Course Edit Page: Course ID: " + courseId);
                    Log.d(TAG, "Assess Entities: " + assessmentEntities);

                    assessData.clear();
                    getCourseAssessments(courseId, assessmentEntities);
          //          assessData.addAll(assessmentEntities);

                    if (mAssessAdapter == null) {
                        mAssessAdapter = new CourseEditAdapter(assessData, CourseEdit.this);
                        assessRV.setAdapter(mAssessAdapter);
                    } else {
                        mAssessAdapter.notifyDataSetChanged();
                    }
                };
        assessVM = ViewModelProviders.of(this)
                .get(AssessViewModel.class);
        assessVM.mAssessments.observe(this, assessObserver);

        allCourses = courseVM.mCourses.getValue();
        termData = courseVM.mTerms.getValue();
    }

    // Initialize recycler view
    private void initRecyclerView() {
        assessRV.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        assessRV.setLayoutManager(layoutManager);
        mAssessAdapter = new CourseEditAdapter(assessData, this);
        assessRV.setAdapter(mAssessAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appBar_homeBtn:
                Intent mainIntent = new Intent(this, MainActivity.class);
                this.startActivity(mainIntent);
        }
    }

    // Method returning course passed via Bundle/Intent from TermEdit page
    public CourseEntity getPassedCourse() {
        Bundle extras = getIntent().getExtras();
        int courseId = extras.getInt(COURSE_ID_KEY);
        String courseTitle = extras.getString(COURSE_TITLE_KEY);
        String courseStart = extras.getString(COURSE_START_KEY);
        String courseEnd = extras.getString(COURSE_END_KEY);
        String courseMentor = extras.getString(COURSE_MENTOR_KEY);
        String mentorEmail = extras.getString(COURSE_EMAIL_KEY);
        String mentorPhone = extras.getString(COURSE_PHONE_KEY);
        String courseStatus = extras.getString(COURSE_STATUS_KEY);
        String courseNotes = extras.getString(COURSE_NOTES_KEY);
        String formatStart = DateConverter.formatDateString(courseStart);
        String formatEnd = DateConverter.formatDateString(courseEnd);
        Log.d(TAG, "Course Formatted date: " + formatStart);
        Date cStartDate = DateConverter.toDate(formatStart);
        Log.d(TAG, "Course Start date DATE: " + cStartDate);
        Date cEndDate = DateConverter.toDate(formatEnd);
        String alertStart = extras.getString(COURSE_ALERT_START_KEY);
        String alertEnd = extras.getString(COURSE_ALERT_END_KEY);

        int termId = extras.getInt(TERM_ID_KEY);

        CourseEntity passedCourse = new CourseEntity(courseId, courseTitle, cStartDate, cEndDate, courseStatus,
                courseMentor, mentorPhone, mentorEmail, courseNotes, alertStart, alertEnd, termId);

        return passedCourse;
    }

    // Method to set course status spinner selection to reflect course data
    public int getStatusSelectionId(String selection) {
        int position = -1;
        for (int i = 0; i < courseOptions.length; i++) {
            if (courseOptions[i].equals(selection)) {
                position = i;
                Log.d(TAG, "Status position selected: " + position);
            }
        }
        return position;
    }

    // Checks to see alert status and sets Radio button true if alert is set
    public void setAlertsChecked(String start, String end) {
        if (start.equals("set")) {
            startAlert.setChecked(true);
        }
        if (end.equals("set")) {
            endAlert.setChecked(true);
        }
    }

    // Method returning list of Assessment entities with passed courseId parameter
    public List<AssessmentEntity> getCourseAssessments(int courseId, List<AssessmentEntity> assessmentEntities) {

        for (int i = 0; i < assessmentEntities.size(); i++) {
            if (assessmentEntities.get(i).getCourseId() == courseId) {
                assessData.add(assessmentEntities.get(i));
            }
        }
        return assessData;
    }

    // Method to return selected radio button data returned as String variable
    public String getSelectedAssessmentType() {
        String type;
        if (assessOA.isChecked()) {
            type = "Objective";
        } else if (assessPA.isChecked()) {
            type = "Performance";
        } else {
            type = "No selection made";
        }

        return type;
    }

    // Method to check for assessments scheduled on the same day
    private boolean assessmentConflict(Date due) {
        boolean assessConflict = false;

        if (assessData == null) {
            assessConflict = false;
        } else {
            for (int i = 0; i < assessData.size(); i++) {
                AssessmentEntity assess = assessData.get(i);
                if (due.equals(assess.getAssessDue())) {
                    assessConflict = true;
                }
            }
        }
        return assessConflict;
    }

    // Alert messages
    // Course no input alert
    public void courseNoInputAlert() {
        AlertDialog.Builder emptyInput = new AlertDialog.Builder(this);
        emptyInput.setTitle("Empty Input Field(s)");
        emptyInput.setMessage("Fill out all Course input fields.");
        emptyInput.setPositiveButton("OK", (dialog, which) -> {

        });
        emptyInput.create().show();

    }

    // Assessment no input alert
    public void assessNoInputAlert() {
        AlertDialog.Builder emptyInput = new AlertDialog.Builder(this);
        emptyInput.setTitle("Empty Input Field(s)");
        emptyInput.setMessage("Fill out all Assessment input fields.");
        emptyInput.setPositiveButton("OK", (dialog, which) -> {
        });
        emptyInput.create().show();
    }

    // Assessment no type alert
    public void assessNoType() {
        AlertDialog.Builder noSelection = new AlertDialog.Builder(this);
        noSelection.setTitle("No type selected");
        noSelection.setMessage("Select an assessment type from the selections available.");
        noSelection.setPositiveButton("OK", (dialog, which) -> {
        });
    }

    // Assessment date conflict
    public void assessConflictAlert() {
        AlertDialog.Builder dateConflict = new AlertDialog.Builder(this);
        dateConflict.setTitle("Date Conflict");
        dateConflict.setMessage("Assessment already scheduled for chosen date or falls outside " +
                "the Course start and end dates.  Please select a different date");
        dateConflict.setPositiveButton("OK", (dialog, which) -> {

        });
        dateConflict.create().show();
    }

    public void deleteCourseError() {
        AlertDialog.Builder deleteCourseError = new AlertDialog.Builder(this);
        deleteCourseError.setTitle("Course Delete Error");
        deleteCourseError.setMessage("Course has assessments assigned to it, delete all assessments " +
                "before deleting course.");
        deleteCourseError.setPositiveButton("OK", (dialog, which) -> {

        });
        deleteCourseError.create().show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(), courseOptions[position], Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
