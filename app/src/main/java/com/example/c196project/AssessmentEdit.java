package com.example.c196project;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.example.c196project.database.AssessmentEntity;
import com.example.c196project.database.DateConverter;
import com.example.c196project.viewmodel.AssessViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.ButterKnife;

import static com.example.c196project.utilities.Constants.ASSESS_END_KEY;
import static com.example.c196project.utilities.Constants.ASSESS_ID_KEY;
import static com.example.c196project.utilities.Constants.ASSESS_START_KEY;
import static com.example.c196project.utilities.Constants.ASSESS_TITLE_KEY;
import static com.example.c196project.utilities.Constants.ASSESS_TYPE_KEY;
import static com.example.c196project.utilities.Constants.COURSE_ID_KEY;

public class AssessmentEdit extends AppCompatActivity implements View.OnClickListener{

    // Tag identifier string
    private static final String TAG = "AssessmentEdit";

    // Header variables
    public TextView pageTitle;
    public ImageButton homeBtn;

    // View Model variable
    AssessViewModel assessVM;

    // Initialized Calendar date variable
    Calendar calendar = Calendar.getInstance();
    // SimpleDateFormat initialization for all date display items
    public SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

    /**
     *   Assessment data display/input elements
     */
    public EditText assessName;
    // Assessment declarations fro start/end date EditText with DatePickerDialog listener and SimpleDateFormat
    public EditText startDisplayDate;
    private DatePickerDialog.OnDateSetListener startDateSetListener;
    public EditText endDisplayDate;
    private DatePickerDialog.OnDateSetListener endDateSetListener;
    public RadioGroup radioGroup;
    public RadioButton oaRadio;
    public RadioButton paRadio;

    // Button variables
    public Button saveAssess;
    public Button deleteAssess;
    public Button setAlert;

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
        startDisplayDate = findViewById(R.id.assess_start_date);
        endDisplayDate = findViewById(R.id.assess_due_date);
        // Radio group and button id assignments
        radioGroup = findViewById(R.id.assess_radio_grp);
        oaRadio = findViewById(R.id.asses_oa_radio);
        paRadio = findViewById(R.id.assess_pa_radio);

        /**
         *  Start and End date EditText ids and onClick override functionality
         */
        //  Initialized Assessment DatePickerDialog date listener start/end dates
        DatePickerDialog.OnDateSetListener aStart = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            DateConverter.updateDateText(startDisplayDate, calendar);
        };
        DatePickerDialog.OnDateSetListener aEnd = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            DateConverter.updateDateText(endDisplayDate, calendar);
        };
        // Assessment start date setOnClickListener
        startDisplayDate.setOnClickListener(v -> {
            Calendar calStart = Calendar.getInstance();
            int startYear = calStart.get(Calendar.YEAR);
            int startMonth = calStart.get(Calendar.MONTH);
            int startDay = calStart.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog startDialog = new DatePickerDialog(
                    AssessmentEdit.this, android.R.style.Theme_DeviceDefault,
                    startDateSetListener, startYear, startMonth, startDay);
            startDialog.show();
        });
        // Assessment end date setOnClickListener
        endDisplayDate.setOnClickListener(v -> {
            Calendar calEnd = Calendar.getInstance();
            int endYear = calEnd.get(Calendar.YEAR);
            int endMonth = calEnd.get(Calendar.MONTH);
            int endDay = calEnd.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog endDialog = new DatePickerDialog(
                    AssessmentEdit.this, android.R.style.Theme_DeviceDefault,
                    endDateSetListener, endYear, endMonth, endDay);
            endDialog.show();
        });

        endDateSetListener = (view, year, month, dayOfMonth) -> {
            Log.d(TAG, "onEndDateSet: mm/dd/yyyy: " + month + "/" + dayOfMonth + "/" + year);

            String endDate = month + "/" + dayOfMonth + "/" + year;
            endDisplayDate.setText(endDate);
        };

        startDateSetListener = (view, year, month, dayOfMonth) -> {
            Log.d(TAG, "onStartDateSet: mm/dd/yyyy: " + month + "/" + dayOfMonth + "/" + year);

            String startDate = month + "/" + dayOfMonth + "/" + year;
            startDisplayDate.setText(startDate);
        };

        // Populate input fields with passed assessment information
        assessName.setText(passedAssessment.getAssessName());
        Date sDate = passedAssessment.getAssessStart();
        Date eDate = passedAssessment.getAssessEnd();
        String start = passedAssessment.getAssessStart().toString();
        String end = passedAssessment.getAssessEnd().toString();
        startDisplayDate.setText(DateConverter.formatDateString(start));
        endDisplayDate.setText(DateConverter.formatDateString(end));
        setAssessType(passedAssessment);

        // Button id assignments and functionality
        saveAssess = findViewById(R.id.save_assess_btn);
        saveAssess.setOnClickListener(v ->{

        });
        deleteAssess = findViewById(R.id.del_assess_btn);
        deleteAssess.setOnClickListener(v ->{

        });
        setAlert = findViewById(R.id.set_alert_btn);
        setAlert.setOnClickListener(v ->{

        });
    }

    // Initialize View Model method
    private void initViewModel() {
        assessVM = ViewModelProviders.of(this)
                .get(AssessViewModel.class);
        assessVM.mLiveAssess.observe(this, (assessmentEntity) -> {
            if(assessmentEntity !=null) {
                Calendar calendar = Calendar.getInstance();
                assessName.setText(assessmentEntity.getAssessName());
                startDisplayDate.setText(DateConverter.formatDateString(assessmentEntity.getAssessStart().toString()));
                endDisplayDate.setText(DateConverter.formatDateString(assessmentEntity.getAssessEnd().toString()));
                setAssessType(assessmentEntity);
            }
        });
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.appBar_homeBtn:
                Intent mainIntent = new Intent(this, MainActivity.class);
                this.startActivity(mainIntent);
        }
    }

    // Method returning assessment passed via Bundle/Intent from CourseEdit page
    public AssessmentEntity getPassedAssessment(){
        Bundle extras = getIntent().getExtras();
        int assessId = extras.getInt(ASSESS_ID_KEY);
        Log.d(TAG, "Assess ID: " + assessId);
        String assessName = extras.getString(ASSESS_TITLE_KEY);
        Log.d(TAG, "Assess Name: " + assessName);
        String start = extras.getString(ASSESS_START_KEY);
        Log.d(TAG, "Start date string: " + start);
        String end = extras.getString(ASSESS_END_KEY);
        String formatStart = DateConverter.formatDateString(start);
        String formatEnd = DateConverter.formatDateString(end);
        Date assessStart = DateConverter.toDate(formatStart);
        Log.d(TAG, "Start date DATE: " + assessStart);
        Date assessEnd = DateConverter.toDate(formatEnd);
        String assessType = extras.getString(ASSESS_TYPE_KEY);
        int courseId = extras.getInt(COURSE_ID_KEY);

        AssessmentEntity passedAssessment = new AssessmentEntity(assessId, assessName, assessType, assessStart,
                assessEnd, courseId);

        return passedAssessment;
    }

    // Method to set selected assessment type from passed assessment entity
    public void setAssessType(AssessmentEntity assessmentEntity){
        String assessType = assessmentEntity.getAssessType();
        Log.d(TAG, "Assessment Type: " + assessType);
        if(assessType.equals("Objective")){
            oaRadio.setChecked(true);
        } else if(assessType.equals("Performance")){
            paRadio.setChecked(true);
        }
    }


/**    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
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
            case R.id.menu_courses:
                Intent courseIntent = new Intent(this, CourseActivity.class);
                Toast.makeText(this, "Courses selected", Toast.LENGTH_SHORT).show();
                this.startActivity(courseIntent);
                return true;
            case R.id.menu_tests:
                Intent testIntent = new Intent(this, AssessmentActivity.class);
                Toast.makeText(this, "Tests selected", Toast.LENGTH_SHORT).show();
                this.startActivity(testIntent);
                return true;
            default:
                return true /*super.onOptionsItemSelected(item)*/;
  /**      }
    }*/

}
