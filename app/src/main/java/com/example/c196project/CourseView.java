package com.example.c196project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CourseView extends AppCompatActivity implements View.OnClickListener{

    TextView pageTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_view);


        pageTitle = (TextView) findViewById(R.id.app_bar_title);
        pageTitle.setText("Course View");

        TextView rvListItem;
        Button rvDelBtn;

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
