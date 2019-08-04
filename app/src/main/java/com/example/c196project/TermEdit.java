package com.example.c196project;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.c196project.database.DateConverter;
import com.example.c196project.database.TermEntity;
import com.example.c196project.ui.TermItemAdapter;
import com.example.c196project.viewmodel.CourseViewModel;
import com.example.c196project.viewmodel.TermViewModel;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.c196project.utilities.Constants.TERM_END_KEY;
import static com.example.c196project.utilities.Constants.TERM_ID_KEY;
import static com.example.c196project.utilities.Constants.TERM_START_KEY;
import static com.example.c196project.utilities.Constants.TERM_TITLE_KEY;

public class TermEdit extends AppCompatActivity implements View.OnClickListener, Serializable {


    private static final String TAG ="TermEdit";

    // View Models
    public TermViewModel termViewModel;
    public CourseViewModel courseViewModel;
    // Term data array list and adapter
    private List<TermEntity> termData = new ArrayList<>();
    private TermItemAdapter mTermAdapter;

    // Header Variables
    public TextView pageTitle;
    public ImageButton homeBtn;  // id = home_btn_term

    public EditText termTitleEdit;  // id = term_title_edit

    // Initialized Calendar date variable
    Calendar calendar = Calendar.getInstance();
    // Declare start/end date EditText with DatePickerDialog listeners and SimpleDateFormat
    public EditText startDisplayDate;
    public DatePickerDialog.OnDateSetListener startDateSetListener;
    public EditText endDisplayDate;
    public DatePickerDialog.OnDateSetListener endDateSetListener;
    public SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

    // Recycler view components
    @BindView(R.id.rv_edit_termList)
    public RecyclerView editTermRV;

    // Button variables
    public Button delTermBtn;
    public Button saveBtn;
    public Button delCourseBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_edit);

        // retrieves data passed from adapter
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

        // initialize butterknife, initRecyclerView and initViewModel
        ButterKnife.bind(this);
        initRecyclerView();
        initViewModel();

        // input element id assignments
        termTitleEdit = findViewById(R.id.edit_term_title);
        startDisplayDate = findViewById(R.id.edit_tsd_input);
        endDisplayDate = findViewById(R.id.edit_ted_input);

        Log.d(TAG,"Term ID retrieved: " + termId);
        Log.d(TAG,"Term Title retrieved: " + termTitle);
        Log.d(TAG,"Term Start retrieved: " + startStr);
        Log.d(TAG,"Term End retrieved: " + endStr);

        /**
         * Start and End date TextView id's and onClick override functionality
         */
        // Initialized DatePickerDialog date listener start/end dates
        DatePickerDialog.OnDateSetListener sDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                DateConverter.updateDateText(startDisplayDate, calendar);
            }
        };
        DatePickerDialog.OnDateSetListener eDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                DateConverter.updateDateText(endDisplayDate, calendar);
            }
        };
        //Start date instantiation/functionality
        startDisplayDate = findViewById(R.id.edit_tsd_input);
        startDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(TermEdit.this, sDate, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar
                        .get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        // End date instantiation/functionality
        endDisplayDate = findViewById(R.id.edit_ted_input);
        endDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(TermEdit.this, eDate, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar
                        .get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        // Start date listener functionality
        startDateSetListener = (view, year, month, dayOfMonth) -> {
            Log.d(TAG, "onStartDateSet: mm/dd/yyyy: " + month + "/" + dayOfMonth + "/" + year);

            String startDate = month + "/" + dayOfMonth + "/" + year;
            startDisplayDate.setText(startDate);
        };
        // End date listener functionality
        endDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Log.d(TAG, "onEndDateSet: mm/dd/yyyy: " + month + "/" + dayOfMonth + "/" + year);

                String endDate = month + "/" + dayOfMonth + "/" + year;
                endDisplayDate.setText(endDate);
            }
        };
//        TermEntity passedTerm = new TermEntity(termId, termTitle, start, end);
        // instantiate/set page title
        pageTitle = findViewById(R.id.app_bar_title);
        pageTitle.setText("Term Edit");
        TextView title = findViewById(R.id.title);
        //  Set edit text fields for Term Title and start/end dates
        termTitleEdit.setText(termTitle);
        startDisplayDate.setText(startFormat);
        endDisplayDate.setText(endFormat);

        // set delete term button id and onClickListener
        delTermBtn = findViewById(R.id.delTermBtn);
        delTermBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                termViewModel.deleteTerm(termId);
            }
        });

        //set save button id and onClickListener
        saveBtn = findViewById(R.id.saveTermBtn);
        saveBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
        //        termViewModel.updateTerm(termId);
            }
        });

        // set delete all courses id and OnClickListener
        delCourseBtn = findViewById(R.id.delAllCourse);
        delCourseBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
        //        courseViewModel.deleteAllCourses();
            }
        });
    }

    // Initialize view method
    private void initViewModel(){
        termViewModel = ViewModelProviders.of(this)
                .get(TermViewModel.class);
        termViewModel.mLiveTerm.observe(this, (termEntity) ->{
            if(termEntity != null){
                Calendar calendar = Calendar.getInstance();
                termTitleEdit.setText(termEntity.getTermTitle());
                startDisplayDate.setText(DateConverter.formatDateString(termEntity.getStart().toString()));
                endDisplayDate.setText(DateConverter.formatDateString(termEntity.getEnd().toString()));
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras == null){

        }
        else{
            int termId = extras.getInt(TERM_ID_KEY);
            termViewModel.loadTerm(termId);
        }
    }

    // Initiates recycler view
    private void initRecyclerView() {
        //  itemList = findViewById(R.id.itemlist_layout);

        editTermRV.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        editTermRV.setLayoutManager(layoutManager);

        mTermAdapter = new TermItemAdapter(termData, this);
        editTermRV.setAdapter(mTermAdapter);
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
}
