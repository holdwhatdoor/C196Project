package com.example.c196project;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.c196project.viewmodel.CourseViewModel;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CourseActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG ="CourseActivity";

    // View Model variable
    private CourseViewModel courseVM;

    public TextView pageTitle;    // id= app_bar_title
    public ImageButton homeBtn;    //id = home_btn

    public EditText courseTitleInput;   // id = course_title_input

    public TextView startDisplayDate;      // id = course_start_date
    public DatePickerDialog.OnDateSetListener startDateSetListener;

    public TextView endDisplayDate;        // id = course_end_date
    public DatePickerDialog.OnDateSetListener endDateSetListener;

    @BindView(R.id.course_list_view)
    public RecyclerView courseRV;

    public Button createCourseBtn;      //id = course_create_btn
    public Button viewBtn;     // id = view_btn
    public Button editBtn;     // id = edit_btn
    public Button delBtn;     // id = del_btn


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        Toolbar toolbar = (Toolbar) findViewById(R.id.c_appbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        initViewModel();
        initRecyclerView();


        pageTitle = (TextView) findViewById(R.id.app_bar_title);
        pageTitle.setText("Courses");

        homeBtn = (ImageButton)findViewById(R.id.appBar_homeBtn);
        homeBtn.setOnClickListener(this);

        startDisplayDate = (TextView) findViewById(R.id.course_start_date);
        startDisplayDate.setOnClickListener(v -> {
            Calendar calStart = Calendar.getInstance();
            int startYear = calStart.get(Calendar.YEAR);
            int startMonth = calStart.get(Calendar.MONTH);
            int startDay = calStart.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog startDialog = new DatePickerDialog(
                    CourseActivity.this, android.R.style.Theme_DeviceDefault,
                    startDateSetListener, startYear, startMonth, startDay);
            startDialog.show();
        });

        endDisplayDate = (TextView) findViewById(R.id.course_end_date);
        endDisplayDate.setOnClickListener(v -> {
            Calendar calEnd = Calendar.getInstance();
            int endYear = calEnd.get(Calendar.YEAR);
            int endMonth = calEnd.get(Calendar.MONTH);
            int endDay = calEnd.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog endDialog = new DatePickerDialog(
                    CourseActivity.this, android.R.style.Theme_DeviceDefault,
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



    }


    private void initViewModel() {
        courseVM = ViewModelProviders.of(this).get(CourseViewModel.class);


    }

    private void initRecyclerView() {

    }


    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.appBar_homeBtn:
                Intent mainIntent = new Intent(this, MainActivity.class);
                this.startActivity(mainIntent);
        }
    }

    @Override
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
        }
    }

    private void initMenu(){

    }
}
