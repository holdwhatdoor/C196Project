package com.example.c196project;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.example.c196project.database.AssessmentEntity;
import com.example.c196project.database.CourseEntity;
import com.example.c196project.database.DateConverter;
import com.example.c196project.viewmodel.AssessViewModel;
import com.example.c196project.viewmodel.CourseViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;

import static com.example.c196project.utilities.Constants.ASSESS_DUE_ALERT_KEY;
import static com.example.c196project.utilities.Constants.ASSESS_DUE_KEY;
import static com.example.c196project.utilities.Constants.ASSESS_ID_KEY;
import static com.example.c196project.utilities.Constants.ASSESS_START_ALERT_KEY;
import static com.example.c196project.utilities.Constants.ASSESS_START_KEY;
import static com.example.c196project.utilities.Constants.ASSESS_TITLE_KEY;
import static com.example.c196project.utilities.Constants.ASSESS_TYPE_KEY;
import static com.example.c196project.utilities.Constants.COURSE_ID_KEY;

public class AssessmentEdit extends AppCompatActivity implements View.OnClickListener {

    // Tag identifier string
    private static final String TAG = "AssessmentEdit";

    // Header variables
    public TextView pageTitle;
    public ImageButton homeBtn;

    // View Model variables
    AssessViewModel assessVM;
    CourseViewModel courseVM;

    // CourseEntity List
    public List<CourseEntity> courseData = new ArrayList<>();
    // AssessmentEntity list
    public List<AssessmentEntity> assessData = new ArrayList<>();

    // Initialized Calendar date variable
    Calendar calendar = Calendar.getInstance();
    // SimpleDateFormat initialization for all date display items
    public SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

    /**
     * Assessment data display/input elements
     */
    public EditText assessName;
    // Assessment declarations for start/due date EditText with DatePickerDialog listener and SimpleDateFormat
    public EditText startDate;
    private DatePickerDialog.OnDateSetListener startDateSetListener;
    public EditText dueDate;
    private DatePickerDialog.OnDateSetListener dueDateSetListener;

    // Start/Due Alert checkboxes
    public CheckBox startAlert;
    public CheckBox dueAlert;

    // Radio Button instantiations
    public RadioGroup radioGroup;
    public RadioButton oaRadio;
    public RadioButton paRadio;

    // Button variables
    public Button saveAssess;
    public Button deleteAssess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assess_edit);

        Toolbar toolbar = findViewById(R.id.a_appbar);
        setSupportActionBar(toolbar);

        // head element id assignments
        pageTitle = findViewById(R.id.app_bar_title);
        pageTitle.setText("Edit Assessment");
        homeBtn = findViewById(R.id.appBar_homeBtn);
        homeBtn.setOnClickListener(this);

        // initialize butterknife and initViewModel
        ButterKnife.bind(this);
        initViewModel();

        // instantiates passed assessment
        AssessmentEntity passedAssessment = getPassedAssessment();

        // Assessment input element id assignments
        assessName = findViewById(R.id.enter_assess_name);
        startDate = findViewById(R.id.assess_start_date);
        dueDate = findViewById(R.id.assess_due_date);
        startAlert = findViewById(R.id.assess_start_alert);
        dueAlert = findViewById(R.id.assess_cb_alert);
        // Radio group and button id assignments
        radioGroup = findViewById(R.id.assess_radio_grp);
        oaRadio = findViewById(R.id.asses_oa_radio);
        paRadio = findViewById(R.id.assess_pa_radio);

        /**
         *  Due date EditText id and onClick override functionality
         */
        //  Initialized Assessment DatePickerDialog date listener start/due
        DatePickerDialog.OnDateSetListener aStart = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            DateConverter.updateDateText(startDate, calendar);
        };

        // Assessment due date setOnClickListener
        startDate.setOnClickListener(v -> new DatePickerDialog(AssessmentEdit.this, aStart, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar
                .get(Calendar.DAY_OF_MONTH)).show());

        startDateSetListener = (view, year, month, dayOfMonth) -> {
            String startDateString = month + "/" + dayOfMonth + "/" + year;
            dueDate.setText(startDateString);
        };


        DatePickerDialog.OnDateSetListener aDue = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            DateConverter.updateDateText(dueDate, calendar);
        };

        // Assessment due date setOnClickListener
        dueDate.setOnClickListener(v -> new DatePickerDialog(AssessmentEdit.this, aDue, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar
                .get(Calendar.DAY_OF_MONTH)).show());

        dueDateSetListener = (view, year, month, dayOfMonth) -> {
            String dueDateString = month + "/" + dayOfMonth + "/" + year;
            dueDate.setText(dueDateString);
        };

        // Populate input fields with passed assessment information
        assessName.setText(passedAssessment.getAssessName());
        // Date retrieval, format convesion and text set
        Date sDate = passedAssessment.getAssessStart();
        String start = passedAssessment.getAssessStart().toString();
        Date dDate = passedAssessment.getAssessDue();
        String due = passedAssessment.getAssessDue().toString();
        startDate.setText(DateConverter.formatDateString(start));
        dueDate.setText(DateConverter.formatDateString(due));
        // method calls to set assessment type and whether alert checkbox is set

        setAssessType(passedAssessment);
        setAlertChecked(passedAssessment);

        // Button id assignments and functionality
        saveAssess = findViewById(R.id.save_assess_btn);
        saveAssess.setOnClickListener(v -> {

            int assessId = getPassedAssessment().getAssessId();
            int courseId = getPassedAssessment().getCourseId();
            Log.d(TAG, "passedAssess courseID: " + courseId);

            if (TextUtils.isEmpty(assessName.getText())) {
                assessNoInputAlert();
            } else if (!oaRadio.isChecked() && !paRadio.isChecked()) {
                assessNoType();
            } else if (startDate.getText().toString().equals("") ||
                    startDate.getText().toString().equals("mm/dd/yyyy") ||
                    startDate.equals(null)) {
                assessNoInputAlert();
            } else if (dueDate.getText().toString().equals("") ||
                    dueDate.getText().toString().equals("mm/dd/yyyy") ||
                    dueDate.equals(null)) {
                assessNoInputAlert();
            } else {

                String assess = assessName.getText().toString();
                String sDt = startDate.getText().toString();
                Date stDt = DateConverter.toDate(sDt);
                String dueString = dueDate.getText().toString();
                Date dueDate = DateConverter.toDate(dueString);
                String selectedType = getSelectedAssessmentType();
                String sAlert = "not set";
                String dAlert = "not set";
                if(startAlert.isChecked()) {
                    sAlert = "set";
                }
                if(dueAlert.isChecked()){
                    dAlert = "set";
                }
                Date parentCourseStart = getParentCourse(courseId).getStartDate();
                Date parentCourseEnd = getParentCourse(courseId).getEndDate();
                if(dueDate.before(stDt)){
                    startDueConflict();
                } else if(dueDate.before(parentCourseStart) || dueDate.after(parentCourseEnd) ||
                        assessmentConflict(assessId, dueDate)) {

                    assessConflictAlert();
                }
                else {
                    AssessmentEntity updatedAssessment = new AssessmentEntity(assessId, assess,
                            selectedType, stDt, dueDate, sAlert, dAlert, courseId);

                    String channelStartId = Integer.toString(assessId) + "AS";
                    String channelDueId = Integer.toString(assessId) + "AD";

                    assessVM.insertAssessment(updatedAssessment);

                    // Calls Notification object to set or cancel alert
                    if(startAlert.equals("set")){

                    } else {

                    }
                    if(dueAlert.equals("set")){

                    } else {

                    }

                    finish();
                }
            }

        });
        deleteAssess = findViewById(R.id.del_assess_btn);
        deleteAssess.setOnClickListener(v -> {
            int assessId = getPassedAssessment().getAssessId();
            assessVM.deleteAssess(assessId);
            finish();
        });
    }


    /**
     *  Creates menu dropdown and functionality
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
                return true /*super.onOptionsItemSelected(item)*/;
        }
    }

    // Initialize View Model method
    private void initViewModel() {
        assessVM = ViewModelProviders.of(this)
                .get(AssessViewModel.class);
        assessVM.mLiveAssess.observe(this, assessmentEntity -> {
            if (assessmentEntity != null) {
                Calendar calendar = Calendar.getInstance();
                assessName.setText(assessmentEntity.getAssessName());
                dueDate.setText(DateConverter.formatDateString(assessmentEntity.getAssessDue().toString()));
                AssessmentEdit.this.setAssessType(assessmentEntity);
            }
        });

        courseData = assessVM.mCourses.getValue();
        assessData = assessVM.mAssessments.getValue();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appBar_homeBtn:
                Intent mainIntent = new Intent(this, MainActivity.class);
                this.startActivity(mainIntent);
        }
    }

    // Method returning assessment passed via Bundle/Intent from CourseEdit page
    public AssessmentEntity getPassedAssessment() {
        Bundle extras = getIntent().getExtras();
        int assessId = extras.getInt(ASSESS_ID_KEY);
        Log.d(TAG, "Assess ID: " + assessId);
        String assessName = extras.getString(ASSESS_TITLE_KEY);
        Log.d(TAG, "Assess Name: " + assessName);
        String start = extras.getString(ASSESS_START_KEY);
        String formatStart = DateConverter.formatDateString(start);
        Date assessStart = DateConverter.toDate(formatStart);
        String due = extras.getString(ASSESS_DUE_KEY);
        Log.d(TAG, "Due date string: " + due);
        String formatDue = DateConverter.formatDateString(due);
        Date assessDue = DateConverter.toDate(formatDue);
        Log.d(TAG, "Due date DATE: " + assessDue);
        String assessType = extras.getString(ASSESS_TYPE_KEY);
        String startAlert = extras.getString(ASSESS_START_ALERT_KEY);
        String assessAlert = extras.getString(ASSESS_DUE_ALERT_KEY);
        int courseId = extras.getInt(COURSE_ID_KEY);

        AssessmentEntity passedAssessment = new AssessmentEntity(assessId, assessName, assessType,
                 assessStart, assessDue, startAlert, assessAlert, courseId);

        return passedAssessment;
    }

    // Method to get parent course of selected assessment to edit
 //   public CourseEntity getAssessmentCourse(){

 //   }

    // Method to set selected assessment type from passed assessment entity
    public void setAssessType(AssessmentEntity assessmentEntity) {
        String assessType = assessmentEntity.getAssessType();
        Log.d(TAG, "Assessment Type: " + assessType);
        if (assessType.equals("Objective")) {
            oaRadio.setChecked(true);
        } else if (assessType.equals("Performance")) {
            paRadio.setChecked(true);
        }
    }

    // Method to set if Alert box is checked
    public void setAlertChecked(AssessmentEntity assessmentEntity) {
        String startChecked = assessmentEntity.getStartAlert();
        String dueChecked = assessmentEntity.getAssessAlert();
        Log.d(TAG, "Start Alert is..... " + startChecked);
        Log.d(TAG, "Alert is.... " + dueChecked);
        if(startChecked.equals("set")){
            startAlert.setChecked(true);
        }
        if (dueChecked.equals("set")) {
            dueAlert.setChecked(true);
        }
    }

    // Method to retrieve String variable of selected Assessment type
    private String getSelectedAssessmentType() {
        String type;
        if(oaRadio.isChecked()) {
            type = "Objective";
        } else if(paRadio.isChecked()) {
            type = "Performance";
        } else {
            type = "No selection made";
        }
        return type;
    }

    // Method to check for assessments scheduled on the same day
    private boolean assessmentConflict(int id, Date due){
        boolean assessConflict = false;

        if(assessData == null){
            assessConflict = false;
        } else {
            for(int i = 0; i < assessData.size(); i++){
                AssessmentEntity assess = assessData.get(i);
                if(assess.getAssessId() != id && due.equals(assess.getAssessDue())){
                    assessConflict = true;
                }
            }
        }
        return assessConflict;
    }

    // Method to get parent course entity
    public CourseEntity getParentCourse(int courseId){
        CourseEntity parentCourse = null;
        for(int i = 0; i < courseData.size(); i++) {
            if(courseData.get(i).getCourseId() == courseId){
                parentCourse = courseData.get(i);
                break;
            }
        }
        return parentCourse;
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

    // Assessment start/due date conflict
    public void startDueConflict() {
        AlertDialog.Builder startDueConflict = new AlertDialog.Builder(this);
        startDueConflict.setTitle("Start/Due Conflict");
        startDueConflict.setMessage("Assessment start date must be before due date.");
        startDueConflict.setPositiveButton("OK", (dialog, which) -> {

        });
    }

}
