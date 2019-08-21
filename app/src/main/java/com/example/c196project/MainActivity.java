package com.example.c196project;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.c196project.database.AppDatabase;
import com.example.c196project.database.TermEntity;
import com.example.c196project.ui.MainViewAdapter;
import com.example.c196project.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    // progress bar and text after loaded and status int
    private ProgressBar progressBar;
    private int progressStatus = 0;
    private Handler mHandler = new Handler();

    // View model
    private MainViewModel mainVM;

    // Term Data array list and adapter
    private List<TermEntity> termData = new ArrayList<>();
    private MainViewAdapter mMainAdapter;

    @BindView(R.id.main_rv)
    public RecyclerView currentTermRV;

    // App bar components
    private TextView pageTitle;         // id= app_bar_title
    private ImageButton homeBtn;        // id= appBar_homeBtn


    public MainViewModel mainView;

    public Date today = new Date();

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set content layout view
        setContentView(R.layout.activity_main);

        // Progress bar and message xml id assignments
        progressBar = findViewById(R.id.progress_bar);
        // Runnable thread to show progress bar status and toast message on completion
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(progressStatus < 100){
                    progressStatus++;
                    SystemClock.sleep(10);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, "Progress Bar: " + progressBar);
                            progressBar.setProgress(progressStatus);
                        }
                    });
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "Loading Complete", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        }).start();

        db = AppDatabase.getInstance(this);

        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Bind and initialize recycler view and view model
        ButterKnife.bind(this);
        initRecyclerView();
        initViewModel();

        /**
         *  Sets page title and home button xml ids
         */
        pageTitle = findViewById(R.id.app_bar_title);
        pageTitle.setText("Home");
        homeBtn = findViewById(R.id.appBar_homeBtn);
        homeBtn.setOnClickListener(this);

        pageTitle = findViewById(R.id.app_bar_title);

    }


    private void initViewModel() {

        final Observer<List<TermEntity>> termsObserver = new Observer<List<TermEntity>>() {
            @Override
            public void onChanged(List<TermEntity> termEntities) {
                termData.clear();
                TermEntity currentTerm = getCurrentTerm(termEntities);
                termData.add(currentTerm);

                if (mMainAdapter == null) {
                    mMainAdapter = new MainViewAdapter(termData, MainActivity.this);
                } else {
                    mMainAdapter.notifyDataSetChanged();
                }
            }
        };

        mainVM = ViewModelProviders.of(this)
                .get(MainViewModel.class);
        mainVM.mTerms.observe(this, termsObserver);

    }

    private void initRecyclerView() {
        currentTermRV.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        currentTermRV.setLayoutManager(layoutManager);

        mMainAdapter = new MainViewAdapter(termData, this);
        currentTermRV.setAdapter(mMainAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.appBar_homeBtn:
                Intent mainIntent = new Intent(this, MainActivity.class);
                this.startActivity(mainIntent);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //     mDataSource.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //     mDataSource.open();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, menu);

        return super.onCreateOptionsMenu(menu);
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
                return true /*super.onOptionsItemSelected(item)*/;
        }
    }

    // Method to get current term
    public TermEntity getCurrentTerm(List<TermEntity> termEntities) {
         Log.d(TAG, "all terms: " + termEntities);
         TermEntity currentTerm = null;
         List<Date> endDates = new ArrayList<>();

        if (!termEntities.isEmpty()) {
            for (int i = 0; i < termEntities.size(); i++) {
                if (termEntities.get(i).getEnd().after(today)) {
                    Date end = termEntities.get(i).getEnd();
                    endDates.add(end);
                }
            }
            Date earliest = Collections.min(endDates);
            for (int i = 0; i < termEntities.size(); i++) {
                if (earliest.equals(termEntities.get(i).getEnd())) {
                    currentTerm = termEntities.get(i);
                }
            }
        }
        return currentTerm;
    }
}

