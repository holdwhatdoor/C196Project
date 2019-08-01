package com.example.c196project;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.c196project.database.DateConverter;
import com.example.c196project.database.TermEntity;
import com.example.c196project.viewmodel.TermViewModel;

import java.io.Serializable;
import java.util.Date;

import static com.example.c196project.utilities.Constants.TERM_ID_KEY;

public class TermEdit extends AppCompatActivity implements View.OnClickListener, Serializable {


    private static final String TAG ="TermEdit";

    public TermViewModel termViewModel;

    public TextView pageTitle;
    public ImageButton homeBtn;  // id = home_btn_term

    public EditText termTitleEdit;  // id = term_title_edit

    public TextView startDisplayDate;   // id= edit_tsd_input
    public DatePickerDialog.OnDateSetListener startDateSetListener;

    public TextView endDisplayDate;     // id= edit_ted_input
    public DatePickerDialog.OnDateSetListener endDateSetListener;

    public RecyclerView courseList;    // id = course_list_view

    public Button delTermBtn;     // id = delTermBtn
    public Button saveBtn;      // id = saveTermBtn
    public Button delCourseBtn;  // id = delAllCcurse


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_edit);

        Bundle extras = getIntent().getExtras();
        int termId = extras.getInt("TERM_ID_KEY");
        String termTitle = extras.getString("TERM_TITLE_KEY");
        String startStr = extras.getString("TERM_START_KEY");
        String endStr = extras.getString("TERM_END_KEY");


        //        Date start = DateConverter.toDate(startStr);
//        Date end = DateConverter.toDate(endStr);

//        TermEntity passedTerm = new TermEntity(termId, termTitle, start, end);

        pageTitle = (TextView) findViewById(R.id.app_bar_title);
        pageTitle.setText("Term Edit");

        TextView title = (TextView) findViewById(R.id.title);

        termTitleEdit.setText(termTitle);
        startDisplayDate.setText(startStr);
        endDisplayDate.setText(endStr);

        delTermBtn = findViewById(R.id.delTermBtn);
        delTermBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                termViewModel.deleteTerm(termId);
            }
        });
    }

    private void initView(){
        termViewModel = ViewModelProviders.of(this)
                .get(TermViewModel.class);
        termViewModel.mLiveTerm.observe(this, (termEntity) ->{
            if(termEntity != null){
                termTitleEdit.setText(termEntity.getTermTitle());
                startDisplayDate.setText(termEntity.getStart().toString());
                endDisplayDate.setText(termEntity.getEnd().toString());
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
