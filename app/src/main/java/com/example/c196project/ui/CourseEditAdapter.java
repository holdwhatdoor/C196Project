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

import com.example.c196project.AssessmentEdit;
import com.example.c196project.R;
import com.example.c196project.database.AssessmentEntity;
import com.example.c196project.database.DateConverter;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.c196project.utilities.Constants.ASSESS_ALERT_KEY;
import static com.example.c196project.utilities.Constants.ASSESS_ID_KEY;
import static com.example.c196project.utilities.Constants.ASSESS_DUE_KEY;
import static com.example.c196project.utilities.Constants.ASSESS_TITLE_KEY;
import static com.example.c196project.utilities.Constants.ASSESS_TYPE_KEY;
import static com.example.c196project.utilities.Constants.COURSE_ID_KEY;

public class CourseEditAdapter extends RecyclerView.Adapter<CourseEditAdapter.CourseEditHolder> {

    String TAG = "CourseEditAdapter";

    private final List<AssessmentEntity> mAssessments;
    private final Context mContext;

    @BindView(R.id.course_edit_scroll)
    public RecyclerView assessRV;

    public CourseEditAdapter(List<AssessmentEntity> mAssessments, Context mContext) {
        this.mAssessments = mAssessments;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public CourseEditHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_assesslist, parent, false);
        return new CourseEditHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseEditHolder holder, int position) {

        final AssessmentEntity assessment = mAssessments.get(position);

        holder.mListItem.setText(assessment.getAssessName());
        holder.mDueDate.setText(DateConverter.formatDateString(assessment.getAssessDue().toString()));

        holder.mEditBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                int assessId = getAssessmentAtPos(position).getAssessId();
                Log.d(TAG, "Assess Id on Edit click: " + assessId);
                String assessTitle = getAssessmentAtPos(position).getAssessName();
                Log.d(TAG, "Assess Name on Edit click: " + assessTitle);
                Date due = getAssessmentAtPos(position).getAssessDue();
                Log.d(TAG, "Assess Start: " + due);
                String type = getAssessmentAtPos(position).getAssessType();
                String alert = getAssessmentAtPos(position).getAssessAlert();
                int courseId = getAssessmentAtPos(position).getCourseId();

                AssessmentEntity assessment = new AssessmentEntity(assessId, assessTitle, type, due,
                        alert, courseId);

                Intent intent = new Intent(mContext, AssessmentEdit.class);
                intent.putExtra(ASSESS_ID_KEY, assessment.getAssessId());
                intent.putExtra(ASSESS_TITLE_KEY, assessment.getAssessName());
                intent.putExtra(ASSESS_DUE_KEY, assessment.getAssessDue().toString());
                intent.putExtra(ASSESS_ALERT_KEY, assessment.getAssessAlert());
                intent.putExtra(ASSESS_TYPE_KEY, assessment.getAssessType());
                intent.putExtra(COURSE_ID_KEY, assessment.getCourseId());

                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public void onBindViewHolder(@NonNull CourseEditHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);

    }

    public AssessmentEntity getAssessmentAtPos(int position) {

        return mAssessments != null ? mAssessments.get(position) :null;
    }

    @Override
    public int getItemCount() {
        return mAssessments.size();
    }

    public class CourseEditHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.assess_item)
        TextView mListItem;
        @BindView(R.id.due_date)
        TextView mDueDate;
        @BindView(R.id.assess_edit_btn)
        Button mEditBtn;


        public CourseEditHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    public void removeAt(int position) {

        mAssessments.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mAssessments.size());
    }


}
