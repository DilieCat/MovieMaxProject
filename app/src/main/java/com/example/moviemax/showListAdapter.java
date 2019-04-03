package com.example.moviemax;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class showListAdapter extends RecyclerView.Adapter<showListAdapter.showListViewHolder> {

    private ArrayList<ShowList> showList_list;
    private Context context;

    public showListAdapter(Context context, ArrayList<ShowList> showList_list) {
        this.showList_list = showList_list;
        this.context = context;
    }

    public class showListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView listName, listCount;

    showListViewHolder(View itemView) {
        super(itemView);

        itemView.setOnClickListener(this);

        listName = (TextView) itemView.findViewById(R.id.name_list);
        listCount = (TextView) itemView.findViewById(R.id.showsCount);
    }

        @Override
        public void onClick(View v) {
            if (mOnEntryClickListener != null) {
                mOnEntryClickListener.onEntryClick(v, getLayoutPosition());
            }
        }
    }

    @NonNull
    @Override
    public showListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.show_list_item, parent, false);
        return new showListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull showListViewHolder showListViewHolder, int position) {
        ShowList showList = showList_list.get(position);

        String name = showList.getName();
        String showCount = showList.getShowCount();

        showListViewHolder.listName.setText(name);
        showListViewHolder.listCount.setText("Aantal films: " + showCount);
    }

    @Override
    public int getItemCount() {
        return showList_list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    private OnEntryClickListener mOnEntryClickListener;

    public interface OnEntryClickListener {
        void onEntryClick(View view, int position);
    }

    public void setOnEntryClickListener(OnEntryClickListener onEntryClickListener) {
        mOnEntryClickListener = onEntryClickListener;
    }
}
