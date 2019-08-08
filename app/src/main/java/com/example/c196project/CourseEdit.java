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
    public EditText courseTitleEdit;  // id = term_title_edit
    // Term declarations for start/end date EditText with DatePickerDialog listeners and SimpleDateFormat
    public EditText courseStartDate;
    public DatePickerDialog.OnDateSetListener courseStartListener;
    public EditText courseEndDate;
    public DatePickerDialog.OnDateSetListener courseEndListener;
    // Add course mentor info
    public EditText courseMentor;
    public EditText mentorPhone;
    public EditText mentorEmail;
    public Spinner statusSpinner;
    public String[] spinnerOptions = {"No Selection", "Planned", "In Progress", "Completed", "Dropped"};

    /**
     *  Add Assessment input data display/imput elements
     */
    public EditText assessTitleInput;
    // Add Course declarations for start/end date EditText with DatePickerDialog listeners
    public EditText assessStartDate;
    public DatePickerDialog.OnDateSetListener assessStartListener;
    public EditText assessEndDate;
    public DatePickerDialog.OnDateSetListener assessEndListener;
    // Radio button group and selections for Assessment type
    public RadioGroup assessType;
    public RadioButton assessPA;
    public RadioButton assessOA;
    // Spinner for status of assessments assigned
    public Spinner assessStatus;
    public String[] statusOptions = {"No Selection", "Dropped", "Failed", "Passed"};

    // Button variables
    public Button delCourseBtn;
    public Button saveBtn;
    public Button addAssess;
    public Button delAssessmentsBtn;

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
