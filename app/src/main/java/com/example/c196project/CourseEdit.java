package com.example.c196project;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.c196project.database.AssessmentEntity;
import com.example.c196project.database.CourseEntity;
import com.example.c196project.ui.AssessItemAdapter;
import com.example.c196project.ui.CourseItemAdapter;
import com.example.c196project.viewmodel.AssessViewModel;
import com.example.c196project.viewmodel.CourseViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;

public class CourseEdit extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG ="CourseEdit";

    // Header Variables
    public TextView pageTitle;
    public ImageButton homeBtn;  // id = home_btn_term

    // View Models
    public CourseViewModel courseVM;
    public AssessViewModel assessVM;
    // Course data array lists and adapters
    private List<CourseEntity> courseData = new ArrayList<>();
    private CourseItemAdapter mCourseAdapter;
    //Assessment data array lists and adapters
    private List<AssessmentEntity> assessData = new ArrayList<>();
    private AssessItemAdapter mAssessAdapter;

    // Initialized Calendar date variable
    Calendar calendar = Calendar.getInstance();
    // SimpleDateFormat initialization for all date display items
    public SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

    /**
     *   Edit Course data display/input elements
     */
    public EditText courseTitleEdit;                                // id = ce_courseTitle
    // Term declarations for start/end date EditText with DatePickerDialog listeners and SimpleDateFormat
    public EditText courseStartDate;                                // id = ce_courseStart
    public DatePickerDialog.OnDateSetListener courseStartListener;
    public EditText courseEndDate;                                  // id = ce_courseEnd
    public DatePickerDialog.OnDateSetListener courseEndListener;
    // Add course mentor info
    public EditText courseMentor;                                   // id = ce_ courseMentor
    public EditText mentorPhone;                                    // id = ce_mentorPhone
    public EditText mentorEmail;                                    // id = ce_mentorEmail
    public Spinner statusSpinner;                                   // id = ce_courseStatus
    public String[] spinnerOptions = {"No Selection", "Planned", "In Progress", "Completed", "Dropped"};

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
    public String[] statusOptions = {"No Selection", "Dropped", "Failed", "Passed"};

    // Button variables
    public Button delCourseBtn;
    public Button saveBtn;
    public Button addAssess;
    public Button delAssessmentsBtn;
    public Button addNote;          // id = ce_noteBtn

    // Recycler view components
    @BindView(R.id.rv_assess_list)
    public RecyclerView assessRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_edit);


        Bundle extras = getIntent().getExtras();


        pageTitle = (TextView) findViewById(R.id.app_bar_title);
        pageTitle.setText("Edit Course");


        TextView title = (TextView) findViewById(R.id.title);

    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.appBar_homeBtn:
                Intent mainIntent = new Intent(this, MainActivity.class);
                this.startActivity(mainIntent);
        }
    }

}
