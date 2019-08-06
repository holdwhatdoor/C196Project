package com.example.c196project;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.c196project.database.CourseEntity;
import com.example.c196project.database.DateConverter;
import com.example.c196project.database.TermEntity;
import com.example.c196project.ui.TermEditAdapter;
import com.example.c196project.viewmodel.CourseViewModel;
import com.example.c196project.viewmodel.TermViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.c196project.utilities.Constants.TERM_END_KEY;
import static com.example.c196project.utilities.Constants.TERM_ID_KEY;
import static com.example.c196project.utilities.Constants.TERM_START_KEY;
import static com.example.c196project.utilities.Constants.TERM_TITLE_KEY;

public class TermEdit extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {


    private static final String TAG ="TermEdit";

    // View Models
    public TermViewModel termVM;
    public CourseViewModel courseVM;
    // Course data array lists and adapters
    private List<CourseEntity> courseData = new ArrayList<>();
    private TermEditAdapter mCourseAdapter;

    // Header Variables
    public TextView pageTitle;
    public ImageButton homeBtn;  // id = home_btn_term

    // Initialized Calendar date variable
    Calendar calendar = Calendar.getInstance();
    // SimpleDateFormat initialization for all date display items
    public SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

    /**
     *   Term data display/input elements
     */
    public EditText termTitleEdit;  // id = term_title_edit
    // Term declarations for start/end date EditText with DatePickerDialog listeners and SimpleDateFormat
    public EditText termStartDate;
    public DatePickerDialog.OnDateSetListener termStartListener;
    public EditText termEndDate;
    public DatePickerDialog.OnDateSetListener termEndListener;

    /**
     *  Add Course data imput elements
     */
    public EditText courseTitleInput;
    // Add Course declarations for start/end date EditText with DatePickerDialog listeners
    public EditText courseStartDate;
    public DatePickerDialog.OnDateSetListener courseStartListener;
    public EditText courseEndDate;
    public DatePickerDialog.OnDateSetListener courseEndListener;
    // Add course mentor info
    public EditText courseMentor;
    public EditText mentorPhone;
    public EditText mentorEmail;
    public Spinner statusSpinner;
    public String[] spinnerOptions = {"Planned", "Enrolled", "Completed"};

    // Recycler view components
    @BindView(R.id.rv_edit_termList)
    public RecyclerView editTermRV;

    // Button variables
    public Button delTermBtn;
    public Button saveBtn;
    public Button addCourse;
    public Button delCoursesBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_edit);

        // retrieves term data passed from adapter
        Bundle extras = getIntent().getExtras();
        int termId = extras.getInt(TERM_ID_KEY);
        String termTitle = extras.getString(TERM_TITLE_KEY);
        String startStr = extras.getString(TERM_START_KEY);
        String endStr = extras.getString(TERM_END_KEY);
        // converts dates to MM/dd/yyyy format
        String startFormat = DateConverter.formatDateString(startStr);
        String endFormat = DateConverter.formatDateString(endStr);
        Log.d("TAG", "Formatted start date: " + startFormat);
        Log.d("TAG", "Formatted end date: " + endFormat);

        // header element id assignments
        pageTitle = findViewById(R.id.app_bar_title);
        pageTitle.setText("Edit Term");
        homeBtn =  findViewById(R.id.appBar_homeBtn);
        homeBtn.setOnClickListener(this);

        // instantiate/set page title
        pageTitle = findViewById(R.id.app_bar_title);
        pageTitle.setText("Term Edit");
        TextView title = findViewById(R.id.title);

        // initialize butterknife, initRecyclerView and initViewModel
        ButterKnife.bind(this);
        initRecyclerView();
        initViewModel();

        // Term input element id assignments
        termTitleEdit = findViewById(R.id.edit_term_title);
        termStartDate = findViewById(R.id.edit_tsd_input);
        termEndDate = findViewById(R.id.edit_ted_input);

        Log.d(TAG,"Term ID retrieved: " + termId);
        Log.d(TAG,"Term Title retrieved: " + termTitle);
        Log.d(TAG,"Term Start retrieved: " + startStr);
        Log.d(TAG,"Term End retrieved: " + endStr);

        /**
         * Start and End date TextView id's and onClick override functionality
         */
        // Initialized Term DatePickerDialog date listener start/end dates
        DatePickerDialog.OnDateSetListener sDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                DateConverter.updateDateText(termStartDate, calendar);
            }
        };
        DatePickerDialog.OnDateSetListener eDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                DateConverter.updateDateText(termEndDate, calendar);
            }
        };
        // Term start date instantiation/functionality
        termStartDate = findViewById(R.id.edit_tsd_input);
        termStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(TermEdit.this, sDate, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar
                        .get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        // Term end date instantiation/functionality
        termEndDate = findViewById(R.id.edit_ted_input);
        termEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(TermEdit.this, eDate, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar
                        .get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        // Term start date listener functionality
        termStartListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Log.d(TAG, "termStartDateSet: mm/dd/yyyy: " + month + "/" + dayOfMonth + "/" + year);

                String startDate = month + "/" + dayOfMonth + "/" + year;
                termStartDate.setText(startDate);
            }
        };
        // Term end date listener functionality
        termEndListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Log.d(TAG, "termEndDateSet: mm/dd/yyyy: " + month + "/" + dayOfMonth + "/" + year);

                String endDate = month + "/" + dayOfMonth + "/" + year;
                termEndDate.setText(endDate);
            }
        };

        //  Set term data text to EditText fields for Term Title and start/end dates
        termTitleEdit.setText(termTitle);
        termStartDate.setText(startFormat);
        termEndDate.setText(endFormat);

        // Course input elements id assignments
        courseTitleInput = findViewById(R.id.et_courseTitle);
        courseMentor = findViewById(R.id.et_mentorName);
        mentorPhone = findViewById(R.id.et_mentorPhone);
        mentorEmail = findViewById(R.id.et_mentorEmail);
        statusSpinner = findViewById(R.id.et_courseStatus);

        statusSpinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        ArrayAdapter spinAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,
                spinnerOptions);
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(spinAdapter);


        // Course start date instantiation/functionality
        DatePickerDialog.OnDateSetListener courseStart = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                DateConverter.updateDateText(courseStartDate, calendar);
            }
        };

        courseStartDate = findViewById(R.id.et_courseStart);
        courseStartDate.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                new DatePickerDialog(TermEdit.this, courseStart, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar
                        .get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // Course start date listener functionality
        courseStartListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Log.d(TAG, "courseStartDateSet: mm/dd/yyyy: " + month + "/" + dayOfMonth + "/" + year);

                String startDate = month + "/" + dayOfMonth + "/" + year;
                courseStartDate.setText(startDate);
            }
        };

        DatePickerDialog.OnDateSetListener courseEnd = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                DateConverter.updateDateText(courseEndDate, calendar);
            }
        };

        // Course End date instantiation/functionality
        courseEndDate = findViewById(R.id.et_courseEnd);
        courseEndDate.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                new DatePickerDialog(TermEdit.this, courseEnd, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar
                        .get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        courseEndListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Log.d(TAG, "courseStartDateSet: mm/dd/yyyy: " + month + "/" + dayOfMonth + "/" + year);

                String endDate = month + "/" + dayOfMonth + "/" + year;
                courseEndDate.setText(endDate);
            }
        };

        //


        /**
         *  Set button id's and onClickListeners
         */
        // set delete term button id and onClickListener
        delTermBtn = findViewById(R.id.delTermBtn);
        delTermBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "Course Data: " + courseData.toString());
                if(!courseData.isEmpty() ){
                    deleteTermError();
                }else{
                    termVM.deleteTerm(termId);
                    finish();
                }

            }
        });

        //set save button id and onClickListener functionality
        saveBtn = findViewById(R.id.saveTermBtn);
        saveBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
    //            TermEntity passedTerm = getPassedTerm();
    //            int termId = passedTerm.getTermId();
                String termTitle = termTitleEdit.getText().toString();
                String termStart = termStartDate.getText().toString();
                String termEnd = termEndDate.getText().toString();
                Date start = DateConverter.toDate(termStart);
                Date end = DateConverter.toDate(termEnd);
                TermEntity updatedTerm = new TermEntity(termId, termTitle, start, end);
                termVM.updateTerm(updatedTerm);
            }
        });

        // set add course button id and onClickListener
        addCourse = findViewById(R.id.addCourse);
        addCourse.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                TimeZone localTZ = TimeZone.getDefault();
                Locale locale = Locale.getDefault();

                Date today = Calendar.getInstance(localTZ, locale).getTime();

                try {

                    if (TextUtils.isEmpty(courseTitleInput.getText())) {
                        showNoInputAlert();
                    } else if (courseStartDate.getText().toString().equals("") ||
                        courseStartDate.getText().toString().equals("mm/dd/yyyy")){
                        showNoInputAlert();
                    } else if (courseEndDate.getText().toString().equals("") ||
                        courseEndDate.getText().toString().equals("mm/dd/yyyy")){
                        showNoInputAlert();
                    } else if(TextUtils.isEmpty(courseMentor.getText())){
                        showNoInputAlert();
                    } else if(TextUtils.isEmpty(mentorPhone.getText())) {
                        showNoInputAlert();
                    } else if(TextUtils.isEmpty(mentorEmail.getText())) {
                        showNoInputAlert();
                    } else if(TextUtils.isEmpty(statusSpinner.getSelectedItem().toString())){
                        showNoInputAlert();
                    }
                    else{
                        Log.d(TAG, "today date....: " + today);
                        String course = courseTitleInput.getText().toString();
                        Log.d(TAG, "Course title: " + course);

                        String startString = courseStartDate.getText().toString();
                        Log.d(TAG, "Course Start Date String value: " + startString);
                        Date start = DateConverter.toDate(startString);
                        Log.d(TAG, "Course Start Date converted: " + start);

                        String endString = courseEndDate.getText().toString();
                        Log.d(TAG, "Course End Date String value: " + endString);
                        Date end = DateConverter.toDate(endString);
                        Log.d(TAG, "Course End Date converted: " + end);

                        String mentor = courseMentor.getText().toString();
                        Log.d(TAG,"Mentor: " + mentor);

                        String phone = mentorPhone.getText().toString();
                        Log.d(TAG, "Phone: " + phone);

                        String email = mentorEmail.getText().toString();
                        Log.d(TAG, "Email: " + email);

                        String status = statusSpinner.getSelectedItem().toString();
                        Log.d(TAG, "Spinner selection: " + status);

                        String notes = ".";
                        Log.d(TAG, "Notes: " + notes);

                      //  int termId = getPassedTerm().getTermId();
                        Log.d(TAG, "Term ID: " + termId);
                        Log.d(TAG, "Start before end:  " + start.before(end));
                        Log.d(TAG, "Start not before today: " + !start.before(today));


                        if (start.before(end) && !start.before(today)) {
                            Log.d(TAG, "Entered if that calls insertCourse()");

                            CourseEntity newCourse = new CourseEntity(course, start,
                                    end, status, mentor, phone, email, notes, termId);

                            Log.d(TAG, "Course object created");
                            Log.d(TAG, "Course title: " + newCourse.getCourseTitle());

                            courseVM.insertCourse(newCourse);

                            Log.d(TAG, "Course insert complete.");

                            courseTitleInput.getText().clear();
                            courseStartDate.setText(null);
                            courseEndDate.setText(null);
                            courseMentor.getText().clear();
                            mentorPhone.getText().clear();
                            mentorEmail.getText().clear();
                            statusSpinner.setSelection(-1);
                        } else {

                            Log.d(TAG, "today date: " + today);
                            courseConflictAlert();
                        }


                    }
                } catch (Exception ex) {

                }
            }

        });

        // set delete all courses id and OnClickListener
        delCoursesBtn = findViewById(R.id.delAllCourse);
        delCoursesBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
        //        courseViewModel.deleteAllCourses();
            }
        });
    }

    // Initialize view method
    private void initViewModel(){
        termVM = ViewModelProviders.of(this)
                .get(TermViewModel.class);
        termVM.mLiveTerm.observe(this, (termEntity) ->{
            if(termEntity != null){
                Calendar calendar = Calendar.getInstance();
                termTitleEdit.setText(termEntity.getTermTitle());
                termStartDate.setText(DateConverter.formatDateString(termEntity.getStart().toString()));
                termEndDate.setText(DateConverter.formatDateString(termEntity.getEnd().toString()));
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras == null){

        }
        else{
            int termId = extras.getInt(TERM_ID_KEY);
            termVM.loadTerm(termId);
        }
    }

    // Initiates recycler view
    private void initRecyclerView() {
        editTermRV.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        editTermRV.setLayoutManager(layoutManager);

        mCourseAdapter = new TermEditAdapter(courseData, this);
        editTermRV.setAdapter(mCourseAdapter);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.appBar_homeBtn:
                Intent mainIntent = new Intent(this, MainActivity.class);
                this.startActivity(mainIntent);
        }
    }



    public TermEntity getPassedTerm(){
        Bundle extras = getIntent().getExtras();
        int termId = extras.getInt("TERM_ID_KEY");
        String termTitle = extras.getString("TERM_TITLE_KEY");
        String startStr = extras.getString("TERM_START_KEY");
        String endStr = extras.getString("TERM_END_KEY");
        Date start = DateConverter.toDate(startStr);
        Date end = DateConverter.toDate(endStr);

        TermEntity passedTerm = new TermEntity(termId, termTitle, start, end);

        return passedTerm;
    }

    // Alert messages
    public void showNoInputAlert() {
        AlertDialog.Builder noTitle = new AlertDialog.Builder(this);
        noTitle.setTitle("No Title Input");
        noTitle.setMessage("Fill out all Course input fields.");
        noTitle.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        noTitle.create().show();

    }

    public void deleteTermError(){
        AlertDialog.Builder deleteTermError = new AlertDialog.Builder(this);
        deleteTermError.setTitle("Term Delete Error");
        deleteTermError.setMessage("Term has courses assigned to it, delete all courses before deleting" +
                "term.");
        deleteTermError.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        deleteTermError.create().show();
    }

    public void courseConflictAlert() {
        AlertDialog.Builder dateConflict = new AlertDialog.Builder(this);
        dateConflict.setTitle("Date Conflict");
        dateConflict.setMessage("Choose a start and end date and ensure the start date is before end" +
                " date and that no other courses overlap chosen dates.");
        dateConflict.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dateConflict.create().show();
    }


    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) {
        Toast.makeText(getApplicationContext(), spinnerOptions[position], Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }

}
