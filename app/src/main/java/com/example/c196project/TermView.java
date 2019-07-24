package com.example.c196project;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

public class TermView extends AppCompatActivity implements View.OnClickListener{


    public TextView pageTitle;  // id= term_title

    public DatePicker termSD;  // id= termViewStart  a TextView
    public DatePicker termED;  // id= termViewEnd    a TextView

    public Spinner courseSelect;  // id= course_spin

    public Button addCourse; // id = add_course_btn
    public Button viewCourse; // id= view_btn
    public Button delCourse;  // id = del_btn
    public Button cancelBtn;  // id= cancel_btn

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_view);

        TextView rvListItem;
        Button rvDelBtn;

        pageTitle = (TextView) findViewById(R.id.app_bar_title);
        pageTitle.setText("Term View");

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
