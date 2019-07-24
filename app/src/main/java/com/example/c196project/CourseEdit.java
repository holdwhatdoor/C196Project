package com.example.c196project;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class CourseEdit extends AppCompatActivity implements View.OnClickListener{

    TextView pageTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_edit);

        pageTitle = (TextView) findViewById(R.id.app_bar_title);
        pageTitle.setText("Edit Course");


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
