package com.example.c196project.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.c196project.R;
import com.example.c196project.database.AssessmentEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AssessEditAdapter extends RecyclerView.Adapter<AssessEditAdapter.AssessEditHolder> {

    String TAG = "AssessItemAdapter";

    private final List<AssessmentEntity> mAssessments;
    private final Context mContext;

    public AssessEditAdapter(List<AssessmentEntity> mAssessments, Context mContext) {
        this.mAssessments = mAssessments;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public AssessEditHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_itemlist, parent, false);
        return new AssessEditHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AssessEditHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mAssessments.size();
    }

    public class AssessEditHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.list_item)
        TextView mListItem;


        public AssessEditHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
