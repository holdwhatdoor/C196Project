package com.example.c196project.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.c196project.R;
import com.example.c196project.database.AssessmentEntity;
import com.example.c196project.database.DateConverter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CourseEditAdapter extends RecyclerView.Adapter<CourseEditAdapter.CourseEditHolder> {

    String TAG = "CourseItemAdapter";

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
        View view = inflater.inflate(R.layout.layout_itemlist, parent, false);
        return new CourseEditHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseEditHolder holder, int position) {

        final AssessmentEntity assessment = mAssessments.get(position);

        holder.mListItem.setText(assessment.getAssessName());
        holder.mStartDate.setText(DateConverter.formatDateString(assessment.getAssessStart().toString()));
        holder.mEndDate.setText(DateConverter.formatDateString(assessment.getAssessEnd().toString()));
    }

    @Override
    public int getItemCount() {
        return mAssessments.size();
    }

    public class CourseEditHolder extends RecyclerView.ViewHolder{

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
