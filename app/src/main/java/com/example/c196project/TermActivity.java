package com.example.c196project;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.c196project.database.DateConverter;
import com.example.c196project.database.TermEntity;
import com.example.c196project.ui.TermItemAdapter;
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

public class TermActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "TermActivity";

    // View model
    private TermViewModel termVM;
    // Term data array list and adapter
    private List<TermEntity> termData = new ArrayList<>();
    private TermItemAdapter mTermAdapter;

    // App bar components
    private TextView pageTitle;
    private ImageButton homeBtn;
    // Term page components
    private EditText termTitleInput;
    private EditText startDisplayDate;
    private DatePickerDialog.OnDateSetListener startDateSetListener;
    private EditText endDisplayDate;
    private DatePickerDialog.OnDateSetListener endDateSetListener;
    public Button addTermBtn;

    // Recycler view binding
    @BindView(R.id.rv_term_list)
    public RecyclerView termRV;

    // On create override method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term);

        // Instantiate Toolbar by id
        Toolbar toolbar = findViewById(R.id.t_appbar);
        setSupportActionBar(toolbar);

        // Set Toolbar text and home button id and function
        pageTitle = findViewById(R.id.app_bar_title);
        pageTitle.setText("Terms");
        homeBtn =  findViewById(R.id.appBar_homeBtn);
        homeBtn.setOnClickListener(this);

        // initialize butterknife, initRecyclerView and initViewModel
        ButterKnife.bind(this);
        initRecyclerView();
        initViewModel();

        /**
         * Start and End date TextView id's and onClick override functionality
         */
        // Initialized Calendar date variable
        Calendar calendar = Calendar.getInstance();
        // Initialized DatePickerDialog date listener
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
        startDisplayDate = findViewById(R.id.term_sd_input);
        startDisplayDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(TermActivity.this, sDate, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar
                        .get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // End date instantiation/functionality
        endDisplayDate = findViewById(R.id.term_ed_input);
        endDisplayDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(TermActivity.this, eDate, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar
                        .get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // End date listener functionality
        endDateSetListener = (view, year, month, dayOfMonth) -> {
            Log.d(TAG, "onEndDateSet: mm/dd/yyyy: " + month + "/" + dayOfMonth + "/" + year);

            String endDate = month + "/" + dayOfMonth + "/" + year;
            endDisplayDate.setText(endDate);
        };

        // Start date listener functionality
        startDateSetListener = (view, year, month, dayOfMonth) -> {
            Log.d(TAG, "onStartDateSet: mm/dd/yyyy: " + month + "/" + dayOfMonth + "/" + year);

            String startDate = month + "/" + dayOfMonth + "/" + year;
            startDisplayDate.setText(startDate);
        };


        /**
         * Add Term button instantiation and functionality
         */
        addTermBtn = findViewById(R.id.add_term_btn);
        addTermBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                termTitleInput = findViewById(R.id.term_title_input);
                TimeZone localTZ = TimeZone.getDefault();
                Locale locale = Locale.getDefault();

                Date today = Calendar.getInstance(localTZ, locale).getTime();

                try {

                    if (TextUtils.isEmpty(termTitleInput.getText())) {
                        showNoInputAlert();
                    } else if (startDisplayDate.getText().toString().equals("") ||
                        startDisplayDate.getText().toString().equals("mm/dd/yyyy")){
                        showNoInputAlert();
                    } else if(endDisplayDate.getText().toString().equals("") ||
                        endDisplayDate.getText().toString().equals("mm/dd/yyyy")){
                        showNoInputAlert();
                    }else{

                        Log.d(TAG, "today date: " + today);
                        String term = termTitleInput.getText().toString();
                        String startString = startDisplayDate.getText().toString();
                        String endString = endDisplayDate.getText().toString();
                        Log.d(TAG, "Start Date String value: " + startString);
                        Log.d(TAG, "End Date String value: " + endString);


                        Date start = DateConverter.toDate(startString);
                        Log.d(TAG, "Start Date converted: " + start);
                        Date end = DateConverter.toDate(endString);
                        Log.d(TAG, "End Date converted: " + end);

                        if (start.before(end) && (start.compareTo(today) == 0 || !start.before(today))
                                && !overlappingTerms(start, end)) {
                            TermEntity newTerm = new TermEntity(term, start, end);
                            termVM.insertTerm(newTerm);

                            termTitleInput.getText().clear();
                            startDisplayDate.getText().clear();
                            endDisplayDate.getText().clear();

                            termTitleInput.setHint("Enter Term Title");
                            startDisplayDate.setHint("mm/dd/yyyy");
                            endDisplayDate.setHint("mm/dd/yyyy");

                        } else {

                            Log.d(TAG, "today date: " + today);
                            dateConflictAlert();
                        }


                    }
                } catch (Exception ex) {

                }
            }
        });
    }

    // Initiates view model
    private void initViewModel() {

        final Observer<List<TermEntity>> termsObserver =
                termEntities -> {
                    termData.clear();
                    termData.addAll(termEntities);

                    if (mTermAdapter == null) {
                        mTermAdapter = new TermItemAdapter(termData,TermActivity.this);
                        termRV.setAdapter(mTermAdapter);
                    } else {
                        mTermAdapter.notifyDataSetChanged();
                    }
                };
        termVM = ViewModelProviders.of(this)
                .get(TermViewModel.class);
        termVM.mTerms.observe(this, termsObserver);
    }

    // Initiates recycler view
    private void initRecyclerView() {
      //  itemList = findViewById(R.id.itemlist_layout);

        termRV.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        termRV.setLayoutManager(layoutManager);

        mTermAdapter = new TermItemAdapter(termData, this);
        termRV.setAdapter(mTermAdapter);
    }

    // Overrides on click
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.appBar_homeBtn:
                Intent mainIntent = new Intent(this, MainActivity.class);
                this.startActivity(mainIntent);
                break;
        }
    }

    /**
     *  Creates menu drop down and functionality
     */
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
                return true;
        }
    }

    // Update Start/End Date EditText
    private void updateDateText(EditText date, Calendar calendar){
        String dateFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());

        date.setText(sdf.format(calendar.getTime()));
    }

    // Method to check for overlapping start and end dates for terms in database
    private boolean overlappingTerms(Date newStart, Date newEnd){
        boolean termOverlap = false;

        if(termData == null){
            termOverlap = false;
        } else {
            for(int i = 0; i < termData.size(); i++){
                TermEntity term = termData.get(i);
                if(newStart.after(term.getStart()) && newStart.before(term.getEnd())){
                    termOverlap = true;
                } else if(newEnd.after(term.getStart()) && newEnd.before(term.getEnd())){
                    termOverlap = true;
                }else if(newStart.before(term.getStart()) && newEnd.after(term.getEnd())){
                    termOverlap = true;
                }
            }
        }
        return termOverlap;
    }

    // Alert messages
    // Empty input fields alert
    public void showNoInputAlert() {
        AlertDialog.Builder emptyInput = new AlertDialog.Builder(this);
        emptyInput.setTitle("Empty Input Field(s)");
        emptyInput.setMessage("Fill out all input fields.");
        emptyInput.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        emptyInput.create().show();

    }
    // Date conflict alert
    public void dateConflictAlert() {
        AlertDialog.Builder dateConflict = new AlertDialog.Builder(this);
        dateConflict.setTitle("Date Conflict");
        dateConflict.setMessage("Choose a start and end date and ensure the start date is before end" +
                " date and that no other terms overlap chosen dates.");
        dateConflict.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dateConflict.create().show();
    }

}