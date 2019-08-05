package com.example.c196project.ui;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.c196project.CourseEdit;
import com.example.c196project.R;
import com.example.c196project.TermEdit;
import com.example.c196project.database.CourseEntity;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.c196project.utilities.Constants.COURSE_EMAIL_KEY;
import static com.example.c196project.utilities.Constants.COURSE_END_KEY;
import static com.example.c196project.utilities.Constants.COURSE_ID_KEY;
import static com.example.c196project.utilities.Constants.COURSE_MENTOR_KEY;
import static com.example.c196project.utilities.Constants.COURSE_NOTES_KEY;
import static com.example.c196project.utilities.Constants.COURSE_PHONE_KEY;
import static com.example.c196project.utilities.Constants.COURSE_START_KEY;
import static com.example.c196project.utilities.Constants.COURSE_STATUS_KEY;
import static com.example.c196project.utilities.Constants.COURSE_TITLE_KEY;
import static com.example.c196project.utilities.Constants.TERM_ID_KEY;


public class TermEditAdapter extends RecyclerView.Adapter<TermEditAdapter.TermEditHolder> {
    String TAG = "TermEditAdapter";

    private final List<CourseEntity> mCourses;
    private final Context mContext;

    @BindView(R.id.rv_edit_termList)
    public RecyclerView termRV;

    public TermEditAdapter(List<CourseEntity> mCourses, Context mContext) {
        this.mCourses = mCourses;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public TermEditHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_itemlist, parent, false);
        return new TermEditHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull TermEditHolder holder, int position) {

        final CourseEntity course = mCourses.get(position);

        holder.mListItem.setText(course.getCourseTitle());
 //       holder.mStartDate.setText(DateConverter.formatDateString(course.getStartDate().toString()));
 //       holder.mEndDate.setText(DateConverter.formatDateString(course.getEndDate().toString()));

        holder.mDelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int courseId = getCourseAtPos(position).getCourseId();
                String courseTitle = getCourseAtPos(position).getCourseTitle();
                Date start = getCourseAtPos(position).getStartDate();
                Date end = getCourseAtPos(position).getEndDate();
                String status = getCourseAtPos(position).getStatus();
                String mentor = getCourseAtPos(position).getMentorName();
                String mentorPhone = getCourseAtPos(position).getMentorPhone();
                String mentorEmail = getCourseAtPos(position).getMentorEmail();
                String notes = getCourseAtPos(position).getCourseNotes();
                int termId = getCourseAtPos(position).getTermId();


                CourseEntity course = new CourseEntity(courseId, courseTitle, start, end, status,
                        mentor, mentorPhone, mentorEmail, notes, termId);


                Log.d(TAG, "Course ID: " + courseId);
                Log.d(TAG, "Course Title: " + courseTitle);
                Log.d(TAG, "Course Start Date: " + start);
                Log.d(TAG, "Course End Date: " + end);
                Log.d(TAG, "Course Data: " + course.toString());

                Intent intent = new Intent(mContext, TermEdit.class);
                intent.putExtra(TERM_ID_KEY, course.getTermId());
                mContext.startActivity(intent);
            }
        });

        holder.mEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int courseId = getCourseAtPos(position).getCourseId();
                String courseTitle = getCourseAtPos(position).getCourseTitle();
                Date start = getCourseAtPos(position).getStartDate();
                Date end = getCourseAtPos(position).getEndDate();
                String status = getCourseAtPos(position).getStatus();
                String mentor = getCourseAtPos(position).getMentorName();
                String mentorPhone = getCourseAtPos(position).getMentorPhone();
                String mentorEmail = getCourseAtPos(position).getMentorEmail();
                String notes = getCourseAtPos(position).getCourseNotes();
                int termId = getCourseAtPos(position).getTermId();

                CourseEntity course = new CourseEntity(courseId, courseTitle, start, end, status,
                        mentor, mentorPhone, mentorEmail, notes, termId);

                Log.d(TAG, "Course ID: " + courseId);
                Log.d(TAG, "Course Title: " + courseTitle);
                Log.d(TAG, "Course Start Date: " + start);
                Log.d(TAG, "Course End Date: " + end);
                Log.d(TAG, "Course Data: " + course.toString());

                Intent intent = new Intent(mContext, CourseEdit.class);
                intent.putExtra(COURSE_ID_KEY, course.getTermId());
                intent.putExtra(COURSE_TITLE_KEY, course.getCourseTitle());
                intent.putExtra(COURSE_START_KEY, course.getStartDate().toString());
                intent.putExtra(COURSE_END_KEY, course.getEndDate().toString());
                intent.putExtra(COURSE_STATUS_KEY, course.getEndDate().toString());
                intent.putExtra(COURSE_MENTOR_KEY, course.getEndDate().toString());
                intent.putExtra(COURSE_PHONE_KEY, course.getEndDate().toString());
                intent.putExtra(COURSE_EMAIL_KEY, course.getEndDate().toString());
                intent.putExtra(COURSE_NOTES_KEY, course.getEndDate().toString());

                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull TermEditHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);

    }

    public CourseEntity getCourseAtPos(int position){

        return mCourses != null ? mCourses.get(position) :null;
    }

    @Override
    public int getItemCount() {
        return mCourses.size();
    }

    public class TermEditHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.list_item)
        TextView mListItem;
        @BindView(R.id.start_date)
        TextView mStartDate;
        @BindView(R.id.end_date)
        TextView mEndDate;
        @BindView(R.id.item_del_btn)
        Button mDelBtn;
        @BindView(R.id.item_edit_btn)
        Button mEditBtn;


        public TermEditHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    public void removeAt(int position) {

        mCourses.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mCourses.size());
    }
}


