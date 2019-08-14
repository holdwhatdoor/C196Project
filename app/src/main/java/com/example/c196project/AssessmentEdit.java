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

import static com.example.c196project.utilities.Constants.ASSESS_ID_KEY;
import static com.example.c196project.utilities.Constants.ASSESS_DUE_KEY;
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
    // Assessment declarations for due date EditText with DatePickerDialog listener and SimpleDateFormat
    public EditText dueDate;
    private DatePickerDialog.OnDateSetListener dueDateSetListener;

    // Radio Button instantiations
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
        dueDate = findViewById(R.id.assess_due_date);
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
        dueDate.setOnClickListener(v -> {
            Calendar calStart = Calendar.getInstance();
            int startYear = calStart.get(Calendar.YEAR);
            int startMonth = calStart.get(Calendar.MONTH);
            int startDay = calStart.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog startDialog = new DatePickerDialog(
                    AssessmentEdit.this, android.R.style.Theme_DeviceDefault,
                    dueDateSetListener, startYear, startMonth, startDay);
            startDialog.show();
        });

        dueDateSetListener = (view, year, month, dayOfMonth) -> {
            Log.d(TAG, "dueDateSet: mm/dd/yyyy: " + month + "/" + dayOfMonth + "/" + year);

            String dueDateString = month + "/" + dayOfMonth + "/" + year;
            dueDate.setText(dueDateString);
        };

        // Populate input fields with passed assessment information
        assessName.setText(passedAssessment.getAssessName());
        Date dDate = passedAssessment.getAssessDue();
        String due = passedAssessment.getAssessDue().toString();
        dueDate.setText(DateConverter.formatDateString(due));
        setAssessType(passedAssessment);

        // Button id assignments and functionality
        saveAssess = findViewById(R.id.save_assess_btn);
        saveAssess.setOnClickListener(v ->{

        });
        deleteAssess = findViewById(R.id.del_assess_btn);
        deleteAssess.setOnClickListener(v ->{
            int assessId = getPassedAssessment().getAssessId();
             assessVM.deleteAssess(assessId);
             finish();
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
                dueDate.setText(DateConverter.formatDateString(assessmentEntity.getAssessDue().toString()));
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
        String due = extras.getString(ASSESS_DUE_KEY);
        Log.d(TAG, "Start date string: " + due);
        String formatDue = DateConverter.formatDateString(due);
        Date assessDue = DateConverter.toDate(formatDue);
        Log.d(TAG, "Start date DATE: " + assessDue);
        String assessType = extras.getString(ASSESS_TYPE_KEY);
        int courseId = extras.getInt(COURSE_ID_KEY);

        AssessmentEntity passedAssessment = new AssessmentEntity(assessId, assessName, assessType,
                assessDue, courseId);

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
}
