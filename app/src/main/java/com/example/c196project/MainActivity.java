package com.example.c196project;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.c196project.database.AppDatabase;
import com.example.c196project.database.TermEntity;
import com.example.c196project.ui.TermItemAdapter;
import com.example.c196project.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // View model
    private MainViewModel mainVM;

    // Term Data array list and adapter
    private List<TermEntity> termData = new ArrayList<>();
    private TermItemAdapter mTermAdapter;

    // App bar components
    private TextView pageTitle;         // id= app_bar_title
    private ImageButton homeBtn;        // id= appBar_homeBtn

    @BindView(R.id.home_term_title)
    TextView currentTerm;
    @BindView(R.id.home_start_txt)
    TextView startDate;
    @BindView(R.id.home_end_txt)
    TextView endDate;

    public Date today = new Date();

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        db = AppDatabase.getInstance(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        today.getTime();

        ButterKnife.bind(this);
        initViewModel();

        pageTitle = (TextView) findViewById(R.id.app_bar_title);
        pageTitle.setText("Home");
        homeBtn = (ImageButton)findViewById(R.id.appBar_homeBtn);
        homeBtn.setOnClickListener(this);

//        mDataSource = new DataSource(this);
//        mDataSource.open();
//        Toast.makeText(this, "Database acquired", Toast.LENGTH_SHORT).show();

        pageTitle = (TextView) findViewById(R.id.app_bar_title);
        currentTerm = (TextView) findViewById(R.id.home_term_title);
        startDate = (TextView) findViewById(R.id.home_start_txt);
        endDate = (TextView) findViewById(R.id.home_end_txt);

        try{

        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        finally{

        }
    }


    private void initViewModel() {

        final Observer<List<TermEntity>> termsObserver = new Observer<List<TermEntity>>() {
            @Override
            public void onChanged(List<TermEntity> termEntities) {
                termData.clear();
                termData.addAll(termEntities);

                if(mTermAdapter == null){
                    mTermAdapter = new TermItemAdapter(termData, MainActivity.this);
                }
            }
        };

        mainVM = ViewModelProviders.of(this).get(MainViewModel.class);

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
                Toast.makeText(this, "Assessments selected", Toast.LENGTH_SHORT).show();
                this.startActivity(testIntent);
                return true;
            default:
                return true /*super.onOptionsItemSelected(item)*/;
        }
    }

    private void initMenu(){

    }
}
