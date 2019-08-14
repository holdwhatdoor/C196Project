package com.example.c196project.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.c196project.R;
import com.example.c196project.database.DateConverter;
import com.example.c196project.database.TermEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainViewAdapter extends RecyclerView.Adapter<MainViewAdapter.MainHolder> {
    String TAG = "MainViewAdapter";

    private final List<TermEntity> mTerms;
    private final Context mContext;

    @BindView(R.id.main_rv)
    public RecyclerView main_rv;

    public MainViewAdapter(List<TermEntity> mTerms, Context mContext){
        this.mTerms = mTerms;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MainHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.main_item, parent, false);
        return new MainHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainHolder holder, int position) {
        final TermEntity term = mTerms.get(position);

        if(term != null) {

            holder.mTermItem.setText(term.getTermTitle());
            holder.mStartDate.setText(DateConverter.formatDateString(term.getStart().toString()));
            holder.mEndDate.setText(DateConverter.formatDateString(term.getEnd().toString()));
        } else {
            holder.mTermItem.setText("No Terms exist");
            holder.mStartDate.setText("mm/dd/yyyy");
            holder.mEndDate.setText("mm/dd/yyyy");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MainHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);

    }

    public TermEntity getTermAtPos(int position) {

        return mTerms != null ? mTerms.get(position) :null;
    }

    @Override
    public int getItemCount() {
        return mTerms.size();
    }


    public class MainHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.current_term)
        TextView mTermItem;
        @BindView(R.id.current_term_start)
        TextView mStartDate;
        @BindView(R.id.cur_term_end_date)
        TextView mEndDate;


        public MainHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
