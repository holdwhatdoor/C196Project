package com.example.c196project;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.c196project.database.AssessmentEntity;
import com.example.c196project.database.CourseEntity;
import com.example.c196project.database.DateConverter;
import com.example.c196project.database.TermEntity;
import com.example.c196project.ui.CourseEditAdapter;
import com.example.c196project.utilities.Constants;
import com.example.c196project.utilities.Notifications;
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

import static com.example.c196project.utilities.Constants.ASSESS_DUE_ALERT_KEY;
import static com.example.c196project.utilities.Constants.ASSESS_DUE_KEY;
import static com.example.c196project.utilities.Constants.ASSESS_ID_KEY;
import static com.example.c196project.utilities.Constants.ASSESS_START_ALERT_KEY;
import static com.example.c196project.utilities.Constants.ASSESS_START_KEY;
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
import static com.example.c196project.utilities.Constants.EDITING_KEY;
import static com.example.c196project.utilities.Constants.TERM_ID_KEY;

public class CourseEdit extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
///////  320 course edit save alert intents....  445 assess add alert intents
    private static final String TAG = "CourseEdit";

    // Header Variables
    public TextView pageTitle;
    public ImageButton homeBtn;

    // Context variable
    public Context context;

    // View Models
    public CourseViewModel courseVM;
    public AssessViewModel assessVM;
    // Term data array list
    private List<TermEntity> termData = new ArrayList<>();
    // Course data array lists and adapters
    private List<CourseEntity> allCourses = new ArrayList<>();
    //Assessment data array lists and adapters
    private List<AssessmentEntity> assessData = new ArrayList<>();
    private CourseEditAdapter mAssessAdapter;
    private List<AssessmentEntity> allAssessments = new ArrayList<>();

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
    public EditText assessTitleInput;
    // Add Course declarations for start/end date EditText with DatePickerDialog listeners
    public EditText assessStartDate;
    public DatePickerDialog.OnDateSetListener assessStartDateListener;
    public EditText assessDueDate;
    public DatePickerDialog.OnDateSetListener assessDueDateListener;

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

    public boolean mEditing;

    // Recycler view components
    @BindView(R.id.rv_assess_list)
    public RecyclerView assessRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_edit);

        Toolbar toolbar = findViewById(R.id.ce_appbar);
        setSupportActionBar(toolbar);

        context = getApplicationContext();

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

        // AssessmentEntity data
        int assessId = extras.getInt(ASSESS_ID_KEY);
        String assessTitle = extras.getString(ASSESS_TITLE_KEY);
        String type = extras.getString(ASSESS_TYPE_KEY);
        String assessDue = extras.getString(ASSESS_DUE_KEY);

        // converts dates to MM/dd/yyyy format
        String courseStartFormat = DateConverter.formatDateString(courseStart);
        String courseEndFormat = DateConverter.formatDateString(courseEnd);
        String assessDueFormat = DateConverter.formatDateString(assessDue);

        // header element id assignments
        pageTitle = findViewById(R.id.app_bar_title);
        pageTitle.setText("Edit Course");
        homeBtn = findViewById(R.id.appBar_homeBtn);
        homeBtn.setOnClickListener(this);

        // initialize butterknife, initRecyclerView and intViewModel
        ButterKnife.bind(this);

        if(savedInstanceState != null){
            mEditing = savedInstanceState.getBoolean(EDITING_KEY);
        }
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
            String startDate = month + "/" + dayOfMonth + "/" + year;
            courseStartDate.setText(startDate);
        };
        // Course end date listener functionality
        courseEndListener = (view, year, month, dayOfMonth) -> {
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

                TimeZone localTZ = TimeZone.getDefault();
                Locale locale = Locale.getDefault();

                Date today = Calendar.getInstance(localTZ, locale).getTime();

                int passedCourseID = getPassedCourse().getCourseId();
                String passedCourseNotes = getPassedCourse().getCourseNotes();
                int parentTermID = getPassedCourse().getTermId();
                TermEntity parentTerm = getParentTerm(parentTermID);

                if (TextUtils.isEmpty(courseTitleEdit.getText())) {
                    courseNoInputAlert();
                } else if(TextUtils.isEmpty(courseStartDate.getText())) {
                    courseNoInputAlert();
                } else if(TextUtils.isEmpty(courseEndDate.getText())) {
                    courseNoInputAlert();
                } else if(TextUtils.isEmpty(courseMentorEdit.getText())) {
                    courseNoInputAlert();
                } else if(TextUtils.isEmpty(mentorPhoneEdit.getText())) {
                    courseNoInputAlert();
                } else if(TextUtils.isEmpty(mentorEmailEdit.getText())) {
                    courseNoInputAlert();
                } else if (TextUtils.isEmpty(courseSpinner.getSelectedItem().toString()) ||
                        courseSpinner.getSelectedItem().toString().equals("No Selection")) {
                    courseNoInputAlert();
                } else {

                    String updateTitle = courseTitleEdit.getText().toString();
                    String cStart = courseStartDate.getText().toString();
                    String cEnd = courseEndDate.getText().toString();
                    // Convert String dates to date
                    Date start = DateConverter.toDate(cStart);
                    Date end = DateConverter.toDate(cEnd);
                    String cMentor = courseMentorEdit.getText().toString();
                    String cPhone = mentorPhoneEdit.getText().toString();
                    String cEmail = mentorEmailEdit.getText().toString();
                    String cStatus = courseSpinner.getSelectedItem().toString();
                    String alertStart = "not set";
                    String alertEnd = "not set";
                    if (startAlert.isChecked()){
                        alertStart = "set";
                    }
                    if (endAlert.isChecked()){
                        alertEnd = "set";
                    }

                    Log.d(TAG, "Parent Term Start, End: " + parentTerm.getStart() + ", " + parentTerm.getEnd());

                    if (start.before(end) && (start.after(parentTerm.getStart()) ||
                            start.compareTo(parentTerm.getStart()) == 0) && (end.before(parentTerm.getEnd()) ||
                            end.compareTo(parentTerm.getEnd()) == 0) && !overlappingCourses(start, end)){

                        CourseEntity updatedCourse = new CourseEntity(passedCourseID, updateTitle, start, end,
                                cStatus, cMentor, cPhone, cEmail, passedCourseNotes, alertStart, alertEnd, parentTermID);

                        // Creates unique integer request codes for start and end dates for setting/cancelling alarms
                        int startRequestCode = courseId * 100;
                        int endRequestCode = (courseId*100) + 1;
                        Log.d(TAG, "startRequestCode: " + startRequestCode);
                        Log.d(TAG, "endRequestCode: " + endRequestCode);

                        String notifyTitle = "Course Notification";
                        String targetStartDate = "Notification for " + updateTitle + " starting on " + DateConverter.formatDateString(start.toString());
                        String targetEndDate = "Notification for " + updateTitle + " ending on " + DateConverter.formatDateString(end.toString());
                        Date now = new Date();

                        long startDifference = Notifications.getDifference(start, now);
                        long endDifference = Notifications.getDifference(end, now);
                        Log.d(TAG, "Now date: " + now);
                        Log.d(TAG, "Now.getTime(): " + now.getTime());
                        Log.d(TAG, "Start date: " + start);
                        Log.d(TAG, "Start.getTime(): " + start.getTime());
                        Log.d(TAG, "End Date: " + end);
                        Log.d(TAG, "End.getTime(): " + end.getTime());
                        Log.d(TAG, "Difference Start: " + startDifference);
                        Log.d(TAG, "End Difference: " + endDifference);
                        Log.d(TAG, "Difference Start minutes: " + startDifference);
                        Log.d(TAG, "End Difference: " + endDifference);

                        courseVM.insertCourse(updatedCourse);

                        // Calls notification alert to set or cancel alert
                        if(alertStart.equals("set")){
                            Notification notification = Notifications.getNotification(context, notifyTitle,
                                    targetStartDate, startRequestCode);
                            Notifications.scheduleNotification(context, notification, startDifference,
                                    startRequestCode);
                        } else {
                            int notificationId = Notifications.getActiveNotificationId(context, startRequestCode);
                            if(notificationId != 0){
                                Notifications.cancelNotification(context, startRequestCode);
                            }
                        }
                        if(alertEnd.equals("set")){
                            Notification notification = Notifications.getNotification(context, notifyTitle,
                                    targetEndDate, endRequestCode);
                            Notifications.scheduleNotification(context, notification, endDifference,
                                    endRequestCode);
                        } else {
                            int notificationId = Notifications.getActiveNotificationId(context, endRequestCode);
                            if(notificationId != 0){
                                Notifications.cancelNotification(context, endRequestCode);
                            }
                        }

                        finish();
                    } else {
                        updateDateConflictAlert();
                    }
                }
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
        assessStartDate = findViewById(R.id.ce_assessStart);
        assessDueDate = findViewById(R.id.ce_assessDue);

        /**
         *  Due date EditText id and onClick override functionality
         */
        //  Initialized Course DatePickerDialog date listener start/end dates
        DatePickerDialog.OnDateSetListener aStart = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            DateConverter.updateDateText(assessStartDate, calendar);
        };

        DatePickerDialog.OnDateSetListener aDue = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            DateConverter.updateDateText(assessDueDate, calendar);
        };

        // Assessment start date setOnClickListener
        assessStartDate.setOnClickListener(v -> new DatePickerDialog(CourseEdit.this, aStart, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar
                .get(Calendar.DAY_OF_MONTH)).show());
        //  Assessment due date listener functionality
        assessStartDateListener = (view, year, month, dayOfMonth) -> {
            String startDate = month + "/" + dayOfMonth + "/" + year;
            courseStartDate.setText(startDate);
        };


        // Assessment due date setOnClickListener
        assessDueDate.setOnClickListener(v -> new DatePickerDialog(CourseEdit.this, aDue, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar
                .get(Calendar.DAY_OF_MONTH)).show());
        //  Assessment due date listener functionality
        assessDueDateListener = (view, year, month, dayOfMonth) -> {
            String dueDate = month + "/" + dayOfMonth + "/" + year;
            courseStartDate.setText(dueDate);
        };

        // Assessment Button id assignments/functionality
        addAssessBtn = findViewById(R.id.ce_addAssess);
        addAssessBtn.setOnClickListener(v -> {

            TimeZone localTZ = TimeZone.getDefault();
            Locale locale = Locale.getDefault();

            Date today = Calendar.getInstance(localTZ, locale).getTime();

            try {

                if (TextUtils.isEmpty(assessTitleInput.getText())) {
                    CourseEdit.this.assessNoInputAlert();
                } else if (!assessOA.isChecked() && !assessPA.isChecked()) {
                    CourseEdit.this.assessNoType();
                } else if (assessStartDate.getText().toString().equals("") ||
                        assessStartDate.getText().toString().equals("mm/dd/yyyy") ||
                        assessStartDate.equals(null)){
                    assessNoInputAlert();
                }else if (assessDueDate.getText().toString().equals("") ||
                        assessDueDate.getText().toString().equals("mm/dd/yyyy") ||
                        assessDueDate.equals(null)) {
                    assessNoInputAlert();
                } else {

                    String assess = assessTitleInput.getText().toString();
                    String aStartString = assessStartDate.getText().toString();
                    String dueString = assessDueDate.getText().toString();
                    Date aStartDate = DateConverter.toDate(aStartString);
                    Date due = DateConverter.toDate(dueString);
                    String selectedType = getSelectedAssessmentType();
                    String aStartAlert = "not set";
                    String dueAlert = "not set";

                    int courseID = courseId;

                    if (aStartDate.before(due) && aStartDate.before(cEndDate) &&
                            (aStartDate.compareTo(cStartDate) == 0 || !aStartDate.before(cStartDate)) &&
                            (due.compareTo(cEndDate) == 0 || due.before(cEndDate)) && due.after(cStartDate) &&
                            !assessmentConflict(due)) {
                        AssessmentEntity assessment = new AssessmentEntity(assess, selectedType, aStartDate, due,
                                aStartAlert, dueAlert, courseID);

                        assessVM.insertAssessment(assessment);

                        // Passes assessment info to Notifications class
                        Intent intent = new Intent(v.getContext(), Notifications.class);
                        intent.putExtra(COURSE_TITLE_KEY, courseTitle);
                        intent.putExtra(ASSESS_TITLE_KEY, assess);
                        intent.putExtra(ASSESS_START_KEY, aStartDate);
                        intent.putExtra(ASSESS_DUE_KEY, due);
                        intent.putExtra(ASSESS_START_ALERT_KEY, aStartAlert);
                        intent.putExtra(ASSESS_DUE_ALERT_KEY, dueAlert);

                        assessTitleInput.getText().clear();
                        assessStartDate.getText().clear();
                        assessDueDate.getText().clear();

                        assessTitleInput.setHint("Enter Assessment Name");
                        assessStartDate.setHint("mm/dd/yyyy");
                        assessDueDate.setHint("mm/dd/yyyy");
                        assessType.clearCheck();

                    } else {
                        assessConflictAlert();
                    }
                }
            } catch (Exception ex) {

            }

        });
    }


    /**
     *  Creates Menu dropdown and functionality
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_home:
                Intent homeIntent = new Intent(this, MainActivity.class);
                Toast.makeText(this, "Home selected", Toast.LENGTH_SHORT).show();
                this.startActivity(homeIntent);
                return true;
            case R.id.menu_terms:
                Intent termIntent = new Intent(this, TermActivity.class);
                Toast.makeText(this, "Terms selected", Toast.LENGTH_SHORT).show();
                this.startActivity(termIntent);
                return true;
            default:
                return true;
        }
    }

    // Initialize view model
    private void initViewModel() {
        courseVM = ViewModelProviders.of(this)
                .get(CourseViewModel.class);
        courseVM.mLiveCourse.observe(this, (courseEntity) -> {
            if (courseEntity != null && !mEditing) {
                Calendar calendar = Calendar.getInstance();
                courseTitleEdit.setText(courseEntity.getCourseTitle());
                courseStartDate.setText(DateConverter.formatDateString(courseEntity.getStartDate().toString()));
                courseEndDate.setText(DateConverter.formatDateString(courseEntity.getEndDate().toString()));
                courseMentorEdit.setText(courseEntity.getMentorName());
                mentorEmailEdit.setText(courseEntity.getMentorEmail());
                mentorPhoneEdit.setText(courseEntity.getMentorPhone());
                int spinnerPos = spinAdapter.getPosition(courseEntity.getStatus());
                courseSpinner.setSelection(spinnerPos);

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

        allAssessments = courseVM.mAssessments.getValue();
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
        Date cStartDate = DateConverter.toDate(formatStart);
        Date cEndDate = DateConverter.toDate(formatEnd);
        String alertStart = extras.getString(COURSE_ALERT_START_KEY);
        String alertEnd = extras.getString(COURSE_ALERT_END_KEY);
        int termId = extras.getInt(TERM_ID_KEY);

        CourseEntity passedCourse = new CourseEntity(courseId, courseTitle, cStartDate, cEndDate, courseStatus,
                courseMentor, mentorPhone, mentorEmail, courseNotes, alertStart, alertEnd, termId);

        return passedCourse;
    }

    // Method to get parent term of selected course
    public TermEntity getParentTerm(int termId) {
        TermEntity parentTerm = null;

        for(int i = 0; i < termData.size(); i++) {
            int id = termData.get(i).getTermId();
            if(id == termId){
                parentTerm = termData.get(i);
            }
        }
        return parentTerm;
    }

    // Method to set course status spinner selection to reflect course data
    public int getStatusSelectionId(String selection) {
        int position = -1;
        for (int i = 0; i < courseOptions.length; i++) {
            if (courseOptions[i].equals(selection)) {
                position = i;
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

    // Method returning list of Assessment entities with passed courseId parameter
    public List<AssessmentEntity> getCourseAssessments(int courseId, List<AssessmentEntity> assessmentEntities) {

        for (int i = 0; i < assessmentEntities.size(); i++) {
            if (assessmentEntities.get(i).getCourseId() == courseId) {
                assessData.add(assessmentEntities.get(i));
            }
        }
        return assessData;
    }

    // Method to check updated Course date conflicts with all other courses
    private boolean overlappingCourses(Date newStart, Date newEnd){
        boolean courseOverlap = false;

        for(int i =0; i < allCourses.size(); i++) {
            CourseEntity course = allCourses.get(i);
            int checkedId = course.getCourseId();
            int passedId = getPassedCourse().getCourseId();
            Date otherStart = course.getStartDate();
            Date otherEnd = course.getEndDate();
            Log.d(TAG, "checkedId: " + checkedId);
            Log.d(TAG, "passedId: " + passedId);

            if(checkedId != passedId) {
                if(newStart.after(otherStart) && newStart.before(otherEnd)){
                    courseOverlap = true;
                    break;
                } else if (newEnd.after(otherStart) && newEnd.before(otherEnd)) {
                    courseOverlap = true;
                    break;
                } else if (newStart.before(otherStart) && newEnd.after(otherEnd)) {
                    courseOverlap = true;
                    break;
                }
            }
        }
        return courseOverlap;
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

    // Course date conflict
    public void updateDateConflictAlert() {
        AlertDialog.Builder datesConflict = new AlertDialog.Builder(this);
        datesConflict.setTitle("Conflicting Dates");
        datesConflict.setMessage("Updated course start/end dates overlap other courses assigned to term " +
                "or fall outside the start and end dates of the term courses are assigned to.  Select " +
                "start and end dates that are within parent term start and end dates and don't interfere " +
                "with other assigned courses.");
        datesConflict.setPositiveButton("OK", (dialog, which) ->{

        });
        datesConflict.create().show();
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(Constants.EDITING_KEY, true);
        super.onSaveInstanceState(outState);
    }
}
