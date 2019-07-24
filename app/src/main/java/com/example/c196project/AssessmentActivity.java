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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.c196project.viewmodel.AssessViewModel;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AssessmentActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG ="AssessmentActivity";

    // View Model variable
    AssessViewModel assessVM;

    public TextView assessTitle;   // id= app_bar_title
    public ImageButton homeBtn;    //id = home_btn

    public TextView startDisplayDate;
    private DatePickerDialog.OnDateSetListener startDateSetListener;

    public TextView endDisplayDate;
    private DatePickerDialog.OnDateSetListener endDateSetListener;

    @BindView(R.id.assess_rv)
    public RecyclerView assessRV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment);

        Toolbar toolbar = (Toolbar) findViewById(R.id.a_appbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        initViewModel();
        initRecyclerView();

        assessTitle = (TextView) findViewById(R.id.app_bar_title);
        assessTitle.setText("Assessments");

        homeBtn = (ImageButton)findViewById(R.id.appBar_homeBtn);
        homeBtn.setOnClickListener(this);

        startDisplayDate = (TextView) findViewById(R.id.assess_start_date);
        startDisplayDate.setOnClickListener(v -> {
            Calendar calStart = Calendar.getInstance();
            int startYear = calStart.get(Calendar.YEAR);
            int startMonth = calStart.get(Calendar.MONTH);
            int startDay = calStart.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog startDialog = new DatePickerDialog(
                    AssessmentActivity.this, android.R.style.Theme_DeviceDefault,
                    startDateSetListener, startYear, startMonth, startDay);
            startDialog.show();
        });

        endDisplayDate = (TextView) findViewById(R.id.assess_due_date);
        endDisplayDate.setOnClickListener(v -> {
            Calendar calEnd = Calendar.getInstance();
            int endYear = calEnd.get(Calendar.YEAR);
            int endMonth = calEnd.get(Calendar.MONTH);
            int endDay = calEnd.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog endDialog = new DatePickerDialog(
                    AssessmentActivity.this, android.R.style.Theme_DeviceDefault,
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
        assessVM = ViewModelProviders.of(this).get(AssessViewModel.class);

    }

    private void initRecyclerView(){

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


