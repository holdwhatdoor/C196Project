package com.example.c196project.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.c196project.R;
import com.example.c196project.TermActivity;
import com.example.c196project.database.TermEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.c196project.utilities.Constants.TERM_ID_KEY;

public class TermItemAdapter extends RecyclerView.Adapter<TermItemAdapter.ViewHolder> {

    private final List<TermEntity> mTerms;
    private final Context mContext;

    public TermItemAdapter(List<TermEntity> mTerms, Context mContext) {
        this.mTerms = mTerms;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_itemlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final TermEntity term = mTerms.get(position);
        holder.mListItem.setText(term.getTermTitle());

        holder.mDelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,"Term item at " + getTermAtPos(position).getTermTitle() +
                        " deleted", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(mContext, TermActivity.class);
                intent.putExtra(TERM_ID_KEY, term.getTermId());
                mContext.startActivity(intent);
                removeAt(holder.getAdapterPosition());
            }
        });

        holder.mEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);

    }

    public TermEntity getTermAtPos(int position){

        return mTerms != null ? mTerms.get(position) :null;
    }

    @Override
    public int getItemCount() {
        return mTerms.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.list_item)
        TextView mListItem;
        @BindView(R.id.item_del_btn)
        Button mDelBtn;
        @BindView(R.id.item_edit_btn)
        Button mEditBtn;


        public ViewHolder(@NonNull View itemView) {
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
