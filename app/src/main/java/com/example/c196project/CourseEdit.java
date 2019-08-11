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

import static com.example.c196project.utilities.Constants.ASSESS_END_KEY;
import static com.example.c196project.utilities.Constants.ASSESS_ID_KEY;
import static com.example.c196project.utilities.Constants.ASSESS_START_KEY;
import static com.example.c196project.utilities.Constants.ASSESS_TITLE_KEY;
import static com.example.c196project.utilities.Constants.ASSESS_TYPE_KEY;
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

public class CourseEdit extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    private static final String TAG ="CourseEdit";

    // Header Variables
    public TextView pageTitle;
    public ImageButton homeBtn;  // id = home_btn_term

    // View Models
    public CourseViewModel courseVM;
    public AssessViewModel assessVM;
    // Course data array lists and adapters
    private List<CourseEntity> courseData = new ArrayList<>();
    private CourseEditAdapter mCourseAdapter;
    //Assessment data array lists and adapters
    private List<AssessmentEntity> assessData = new ArrayList<>();
    private CourseEditAdapter mAssessAdapter;

    // Initialized Calendar date variable
    Calendar calendar = Calendar.getInstance();
    // SimpleDateFormat initialization for all date display items
    public SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

    /**
     *   Edit Course data display/input elements
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
    /**
     *  Add Assessment input data display/imput elements
     */
    public EditText assessTitleInput;                               // id = ce_assessTitle
    // Add Course declarations for start/end date EditText with DatePickerDialog listeners
    public EditText assessStartDate;                                // id = ce_assessStart
    public DatePickerDialog.OnDateSetListener assessStartListener;
    public EditText assessEndDate;                                  // id = ce_assessEnd
    public DatePickerDialog.OnDateSetListener assessEndListener;
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
        // CourseEntity data
        int courseId = extras.getInt(COURSE_ID_KEY);
        String courseTitle = extras.getString(COURSE_TITLE_KEY);
        String courseStart = extras.getString(COURSE_START_KEY);
        String courseEnd = extras.getString(COURSE_END_KEY);
        String courseMentor = extras.getString(COURSE_MENTOR_KEY);
        String mentorEmail = extras.getString(COURSE_EMAIL_KEY);
        String mentorPhone = extras.getString(COURSE_PHONE_KEY);
        String courseStatus = extras.getString(COURSE_STATUS_KEY);
        String courseNotes = extras.getString(COURSE_NOTES_KEY);
        Date cStartDate = DateConverter.toDate(courseStart);
        Date cEndDate = DateConverter.toDate(courseEnd);

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
        String assessStart = extras.getString(ASSESS_START_KEY);
        String assessEnd = extras.getString(ASSESS_END_KEY);

        // converts dates to MM/dd/yyyy format
        String courseStartFormat = DateConverter.formatDateString(courseStart);
        String courseEndFormat = DateConverter.formatDateString(courseEnd);
        String assessStartFormat = DateConverter.formatDateString(assessStart);
        String assessEndFormat = DateConverter.formatDateString(assessEnd);

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
            DateConverter.updateDateText(courseStartDate, calendar);
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


        /**
         *  Course button id assignments/functionality
         */
        // Save updated course input info assignment/function
        saveBtn = findViewById(R.id.ce_courseSave);
        saveBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });
        // Add note button assignment/function
        addNoteBtn = findViewById(R.id.ce_noteBtn);
        addNoteBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });
        // Delete course button assignment/function
        delCourseBtn = findViewById(R.id.ce_delCourse);
        delCourseBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

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
        assessEndDate = findViewById(R.id.ce_assessEnd);

        /**
         *  Start and End date EditText ids and onClick override functionality
         */
        //  Initialized Course DatePickerDialog date listener start/end dates
        DatePickerDialog.OnDateSetListener aStart = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            DateConverter.updateDateText(courseStartDate, calendar);
        };
        DatePickerDialog.OnDateSetListener aEnd = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            DateConverter.updateDateText(courseStartDate, calendar);
        };
        // Course start date setOnClickListener
        courseStartDate.setOnClickListener(v -> new DatePickerDialog(CourseEdit.this, aStart, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar
                .get(Calendar.DAY_OF_MONTH)).show());
        // Course end date setOnClickListener
        courseEndDate.setOnClickListener(v -> new DatePickerDialog(CourseEdit.this, aEnd, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar
                .get(Calendar.DAY_OF_MONTH)).show());
        // Term start date listener functionality
        courseStartListener = (view, year, month, dayOfMonth) -> {
            Log.d(TAG, "assessStartDateSet: mm/dd/yyyy: " + month + "/" + dayOfMonth + "/" + year);

            String startDate = month + "/" + dayOfMonth + "/" + year;
            courseStartDate.setText(startDate);
        };
        // Course end date listener functionality
        courseEndListener = (view, year, month, dayOfMonth) -> {
            Log.d(TAG, "assessEndDateSet: mm/dd/yyyy: " + month + "/" + dayOfMonth + "/" + year);

            String endDate = month + "/" + dayOfMonth + "/" + year;
            courseEndDate.setText(endDate);
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
                        assessStartDate.getText().toString().equals("mm/dd/yyyy")) {
                    CourseEdit.this.assessNoInputAlert();
                } else if (assessEndDate.getText().toString().equals("") ||
                        assessEndDate.getText().toString().equals("mm/dd/yyyy")) {
                    CourseEdit.this.assessNoInputAlert();
                } else {

                    String assess = assessTitleInput.getText().toString();
                    String startString = assessStartDate.getText().toString();
                    Date start = DateConverter.toDate(startString);
                    String endString = assessEndDate.getText().toString();
                    Date end = DateConverter.toDate(endString);
                    String selectedType = CourseEdit.this.getSelectedAssessmentType();
                    int courseID = courseId;

                    if (start.before(end) && !start.before(today)) {
                        AssessmentEntity assessment = new AssessmentEntity(assess, selectedType, start,
                                end, courseID);

                        assessVM.insertAssessment(assessment);

                        assessTitleInput.setHint("Enter Assessment Name");
                        assessStartDate.setHint("mm/dd/yyyy");
                        assessEndDate.setHint("mm/dd/yyyy");
                        assessType.clearCheck();
                        Log.d(TAG, "Assessment insert complete.");

                    } else {
                        CourseEdit.this.assessConflictAlert();
                    }
                }
            } catch (Exception ex) {

            }

        });
        delAssessmentsBtn = findViewById(R.id.ce_delAssess);
        delAssessmentsBtn.setOnClickListener(v ->{
            assessVM.deleteAll();
        });

    }

    // Initialize view model
    private void initViewModel() {
        courseVM = ViewModelProviders.of(this)
                .get(CourseViewModel.class);
        courseVM.mLiveCourse.observe(this, (courseEntity) -> {
            if(courseEntity != null){
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
            }
        });

        final Observer<List<AssessmentEntity>> assessObserver =
                new Observer<List<AssessmentEntity>>() {
                    @Override
                    public void onChanged(List<AssessmentEntity> assessmentEntities) {

                        int courseId = getPassedCourse().getCourseId();
                        Log.d(TAG, "Course Edit Page: Course ID: " + courseId);
                        assessData.clear();
                        getCourseAssessments(courseId, assessmentEntities);

                        if (mAssessAdapter == null) {
                            mAssessAdapter = new CourseEditAdapter(assessData, CourseEdit.this);
                            assessRV.setAdapter(mAssessAdapter);
                        } else {
                            mAssessAdapter.notifyDataSetChanged();
                        }
                    }
                };
        assessVM = ViewModelProviders.of(this)
                .get(AssessViewModel.class);
        assessVM.mAssessments.observe(this, assessObserver);
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
    public void onClick(View v){
        switch (v.getId()){
            case R.id.appBar_homeBtn:
                Intent mainIntent = new Intent(this, MainActivity.class);
                this.startActivity(mainIntent);
        }
    }

    // Method returning course passed via Bundle/Intent from TermEdit page
    public CourseEntity getPassedCourse(){
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
        Date cStartDate = DateConverter.toDate(courseStart);
        Date cEndDate = DateConverter.toDate(courseEnd);
        int termId = extras.getInt(TERM_ID_KEY);

        CourseEntity passedCourse = new CourseEntity(courseId, courseTitle, cStartDate, cEndDate, courseStatus,
                courseMentor, mentorPhone, mentorEmail, courseNotes, termId);

        return passedCourse;
    }

    // Method to set course status spinner selection to reflect course data
    public int getStatusSelectionId(String selection){
        int position = -1;
        for(int i = 0; i < courseOptions.length; i++){
            if(courseOptions[i].equals(selection)){
                position = i;
                Log.d(TAG, "Status position selected: " + position);
            }
        }
        return position;
    }

    // Method returning list of Assessment entities with passed courseId parameter
    public List<AssessmentEntity> getCourseAssessments(int courseId, List<AssessmentEntity> assessmentEntities){

        for(int i = 0; i < assessmentEntities.size(); i++){
            if(assessmentEntities.get(i).getCourseId() == courseId){
                assessData.add(assessmentEntities.get(i));
            }
        }
        return assessData;
    }

    // Method to return selected radio button data returned as String variable
    public String getSelectedAssessmentType(){
        String type = null;
        if(assessOA.isChecked()) {
            type = "Objective";
        } else if(assessPA.isChecked()) {
            type = "Performance";
        } else {
            type = "No selection made";
        }

        return type;
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
        dateConflict.setMessage("Choose a start and end date and ensure the start date is before end" +
                " date and that no other assessments overlap chosen dates.");
        dateConflict.setPositiveButton("OK", (dialog, which) -> {

        });
        dateConflict.create().show();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(),courseOptions[position], Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
