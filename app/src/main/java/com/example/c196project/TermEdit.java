package com.example.c196project;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class TermEdit extends AppCompatActivity implements View.OnClickListener{


    private static final String TAG ="TermEdit";


    public TextView pageTitle;
    public ImageButton homeBtn;  // id = home_btn_term

    private EditText termTitleEdit;  // id = term_title_edit

    private TextView startDisplayDate;   // id= edit_tsd_input
    private DatePickerDialog.OnDateSetListener startDateSetListener;

    private TextView endDisplayDate;     // id= edit_ted_input
    private DatePickerDialog.OnDateSetListener endDateSetListener;

    public RecyclerView courseList;    // id = course_list_view

    public Button saveBtn;      // id = eTSaveBtn
    public Button cancelBtn;     // id = eTCancelBtn
    public Button delCourseBtn;  // id = eTDelCourseBtn


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_edit);

        pageTitle = (TextView) findViewById(R.id.app_bar_title);
        pageTitle.setText("Term Edit");

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
