package com.example.c196project;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

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

import static com.example.c196project.utilities.Constants.ASSESS_ALERT_KEY;
import static com.example.c196project.utilities.Constants.ASSESS_DUE_KEY;
import static com.example.c196project.utilities.Constants.ASSESS_ID_KEY;
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
    // Assessment declarations for due date EditText with DatePickerDialog listener and SimpleDateFormat
    public EditText dueDate;
    private DatePickerDialog.OnDateSetListener dueDateSetListener;

    // Set Alert checkbox
    public CheckBox setAlert;

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
        dueDate = findViewById(R.id.assess_due_date);
        setAlert = findViewById(R.id.assess_cb_alert);
        // Radio group and button id assignments
        radioGroup = findViewById(R.id.assess_radio_grp);
        oaRadio = findViewById(R.id.asses_oa_radio);
        paRadio = findViewById(R.id.assess_pa_radio);

        /**
         *  Due date EditText id and onClick override functionality
         */
        //  Initialized Assessment DatePickerDialog date listener due
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
            Log.d(TAG, "dueDateSet: mm/dd/yyyy: " + month + "/" + dayOfMonth + "/" + year);

            String dueDateString = month + "/" + dayOfMonth + "/" + year;
            dueDate.setText(dueDateString);
        };

        // Populate input fields with passed assessment information
        assessName.setText(passedAssessment.getAssessName());
        // Date retrieval, format convesion and text set
        Date dDate = passedAssessment.getAssessDue();
        String due = passedAssessment.getAssessDue().toString();
        dueDate.setText(DateConverter.formatDateString(due));
        // method calls to set assessment type and whether alert checkbox is set

        setAssessType(passedAssessment);
        setAlertChecked(passedAssessment);

        // Button id assignments and functionality
        saveAssess = findViewById(R.id.save_assess_btn);
        saveAssess.setOnClickListener(v -> {

            int assessId = getPassedAssessment().getAssessId();
            int courseId = getPassedAssessment().getCourseId();

            if (TextUtils.isEmpty(assessName.getText())) {
                assessNoInputAlert();
            } else if (!oaRadio.isChecked() && !paRadio.isChecked()) {
                assessNoType();
            } else if (dueDate.getText().toString().equals("") ||
                    dueDate.getText().toString().equals("mm/dd/yyyy") ||
                    dueDate.equals(null)) {
                assessNoInputAlert();
            } else {

                String assess = assessName.getText().toString();
                String dueString = dueDate.getText().toString();
                Date dueDate = DateConverter.toDate(dueString);
                String selectedType = getSelectedAssessmentType();
                String dueAlert = "not set";
                if(setAlert.isChecked()){
                    dueAlert = "set";
                }
                Log.d(TAG, "Parent Course: " + courseData.get(0));
                Date parentCourseStart = courseData.get(0).getStartDate();
                Date parentCourseEnd = courseData.get(0).getEndDate();

                if(dueDate.before(parentCourseStart) || dueDate.after(parentCourseEnd) ||
                        assessmentConflict(assessId, dueDate)) {

                    assessConflictAlert();
                }
                else {
                    AssessmentEntity updatedAssessment = new AssessmentEntity(assessId, assess,
                            selectedType, dueDate, dueAlert, courseId);
                    assessVM.insertAssessment(updatedAssessment);
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

    // Initialize View Model method
    private void initViewModel() {
        assessVM = ViewModelProviders.of(this)
                .get(AssessViewModel.class);
        assessVM.mLiveAssess.observe(this, (assessmentEntity) -> {
            if (assessmentEntity != null) {
                Calendar calendar = Calendar.getInstance();
                assessName.setText(assessmentEntity.getAssessName());
                dueDate.setText(DateConverter.formatDateString(assessmentEntity.getAssessDue().toString()));
                setAssessType(assessmentEntity);
            }
        });

        courseData = assessVM.mCourses.getValue();
        Log.d(TAG, "Course data: " + courseData);
        assessData = assessVM.mAssessments.getValue();
        Log.d(TAG, "Assess data: " + assessData);
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
        String due = extras.getString(ASSESS_DUE_KEY);
        Log.d(TAG, "Start date string: " + due);
        String formatDue = DateConverter.formatDateString(due);
        Date assessDue = DateConverter.toDate(formatDue);
        Log.d(TAG, "Start date DATE: " + assessDue);
        String assessType = extras.getString(ASSESS_TYPE_KEY);
        String assessAlert = extras.getString(ASSESS_ALERT_KEY);
        int courseId = extras.getInt(COURSE_ID_KEY);

        AssessmentEntity passedAssessment = new AssessmentEntity(assessId, assessName, assessType,
                assessDue, assessAlert, courseId);

        return passedAssessment;
    }

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
        String alertChecked = assessmentEntity.getAssessAlert();
        Log.d(TAG, "Alert is.... " + alertChecked);
        if (alertChecked.equals("set")) {
            setAlert.setChecked(true);
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

}
