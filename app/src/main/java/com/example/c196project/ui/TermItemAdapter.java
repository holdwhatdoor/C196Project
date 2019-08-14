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

import com.example.c196project.R;
import com.example.c196project.TermEdit;
import com.example.c196project.database.DateConverter;
import com.example.c196project.database.TermEntity;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.c196project.utilities.Constants.TERM_END_KEY;
import static com.example.c196project.utilities.Constants.TERM_ID_KEY;
import static com.example.c196project.utilities.Constants.TERM_START_KEY;
import static com.example.c196project.utilities.Constants.TERM_TITLE_KEY;

public class TermItemAdapter extends RecyclerView.Adapter<TermItemAdapter.TermHolder> {
    String TAG = "TermItemAdapter";

    private final List<TermEntity> mTerms;
    private final Context mContext;

    @BindView(R.id.rv_term_list)
    public RecyclerView termRV;

    public TermItemAdapter(List<TermEntity> mTerms, Context mContext) {
        this.mTerms = mTerms;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public TermHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_itemlist, parent, false);
        return new TermHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TermHolder holder, int position) {

        final TermEntity term = mTerms.get(position);

        holder.mListItem.setText(term.getTermTitle());
        holder.mStartDate.setText(DateConverter.formatDateString(term.getStart().toString()));
        holder.mEndDate.setText(DateConverter.formatDateString(term.getEnd().toString()));

        holder.mEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int termId = TermItemAdapter.this.getTermAtPos(position).getTermId();
                String termTitle = TermItemAdapter.this.getTermAtPos(position).getTermTitle();
                Date start = TermItemAdapter.this.getTermAtPos(position).getStart();
                Date end = TermItemAdapter.this.getTermAtPos(position).getEnd();
                TermEntity term = new TermEntity(termId, termTitle, start, end);

                Log.d(TAG, "Term ID: " + termId);
                Log.d(TAG, "Term Title: " + termTitle);
                Log.d(TAG, "Term Start Date: " + start);
                Log.d(TAG, "Term End Date: " + end);
                Log.d(TAG, "Term Data: " + term.toString());

                Intent intent = new Intent(mContext, TermEdit.class);
                intent.putExtra(TERM_ID_KEY, term.getTermId());
                intent.putExtra(TERM_TITLE_KEY, term.getTermTitle());
                intent.putExtra(TERM_START_KEY, term.getStart().toString());
                intent.putExtra(TERM_END_KEY, term.getEnd().toString());
                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull TermHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);

    }

    public TermEntity getTermAtPos(int position){

        return mTerms != null ? mTerms.get(position) :null;
    }

    @Override
    public int getItemCount() {
        return mTerms.size();
    }

    public class TermHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.list_item)
        TextView mListItem;
        @BindView(R.id.start_date)
        TextView mStartDate;
        @BindView(R.id.end_date)
        TextView mEndDate;
        @BindView(R.id.item_edit_btn)
        Button mEditBtn;


        public TermHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    public void removeAt(int position) {

        mTerms.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mTerms.size());
    }
}

