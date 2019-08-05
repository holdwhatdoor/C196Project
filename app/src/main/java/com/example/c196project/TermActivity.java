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

    // Recycler view components
    @BindView(R.id.rv_term_list)
    public RecyclerView termRV;

    // View model
    private TermViewModel termVM;
    // Term data array list and adapter
    private List<TermEntity> termData = new ArrayList<>();
    private TermItemAdapter mTermAdapter;

    // App bar components
    private TextView pageTitle;         // id= app_bar_title
    private ImageButton homeBtn;        // id= appBar_homeBtn
    // Term page components
    private EditText termTitleInput;    // id= term_title_input
    private EditText startDisplayDate;     // id= term_sd_input
    private DatePickerDialog.OnDateSetListener startDateSetListener;
    private EditText endDisplayDate;     // id = term_ed_input
    private DatePickerDialog.OnDateSetListener endDateSetListener;
    public Button addTermBtn;   // id= add_term_btn
    public Button delAllTermBtn;    // id= del_all_term

    // On create override method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term);

        // Instantiate Toolbar by id
        Toolbar toolbar = (Toolbar) findViewById(R.id.t_appbar);
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

                termTitleInput = (EditText) findViewById(R.id.term_title_input);
                TimeZone localTZ = TimeZone.getDefault();
                Locale locale = Locale.getDefault();

                Date today = Calendar.getInstance(localTZ, locale).getTime();

                try {

                    if (TextUtils.isEmpty(termTitleInput.getText())) {

                        showNoInputAlert();
                    } else {

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

                        if (start.before(end) && !start.before(today)) {
                            TermEntity newTerm = new TermEntity(term, start, end);
                            termVM.insertTerm(newTerm);
                            termTitleInput.getText().clear();
                            startDisplayDate.setText(null);
                            endDisplayDate.setText(null);
                        } else {

                            Log.d(TAG, "today date: " + today);
                            dateConflictAlert();
                        }


                    }
                } catch (Exception ex) {

                }
            }
        });


        delAllTermBtn = findViewById(R.id.del_all_term);
        delAllTermBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                termVM.deleteAll();
            }
        });


/**        Button editListItem = itemList.findViewById(R.id.item_edit_btn);
        editListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        Button delListItem = itemList.findViewById(R.id.item_del_btn);
        delListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle extras = getIntent().getExtras();
                int termToDeleteID = extras.getInt(TERM_ID_KEY);
                termVM.loadTerm(termToDeleteID);
                Log.d("Term Id to delete: ", Integer.toString(termToDeleteID));
                termVM.deleteTerm(termToDeleteID);
            }
        });
*/
    }

    // Initiates view model
    private void initViewModel() {

        final Observer<List<TermEntity>> termsObserver =
                new Observer<List<TermEntity>>() {
                    @Override
                    public void onChanged(List<TermEntity> termEntities) {
                        termData.clear();
                        termData.addAll(termEntities);

                        if (mTermAdapter == null) {
                            mTermAdapter = new TermItemAdapter(termData,TermActivity.this);
                            termRV.setAdapter(mTermAdapter);
                        } else {
                            mTermAdapter.notifyDataSetChanged();
                        }
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

    // Update Start/End Date EditText
    private void updateDateText(EditText date, Calendar calendar){
        String dateFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());

        date.setText(sdf.format(calendar.getTime()));
    }

    // Alert messages
    public void showNoInputAlert() {
        AlertDialog.Builder noTitle = new AlertDialog.Builder(this);
        noTitle.setTitle("No Title Input");
        noTitle.setMessage("Enter a term title.");
        noTitle.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        noTitle.create().show();

    }

    public void dateConflictAlert() {
        AlertDialog.Builder dateConflict = new AlertDialog.Builder(this);
        dateConflict.setTitle("Date Conflict");
        dateConflict.setMessage("Choose a start and end date and ensure the start date is before end" +
                " date and that no other terms overlap " +
                "chosen dates.");
        dateConflict.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dateConflict.create().show();
    }

}