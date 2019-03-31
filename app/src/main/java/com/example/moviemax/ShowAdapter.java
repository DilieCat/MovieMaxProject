package com.example.moviemax;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ShowAdapter extends RecyclerView.Adapter<ShowAdapter.ShowViewHolder> {
    private Context context;
    private ArrayList<Show> showList;

    public ShowAdapter(Context context, ArrayList<Show> showList){
        this.context = context;
        this.showList = showList;

    }

    @NonNull
    @Override
    public ShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.show_item, parent, false);
        return new ShowViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowViewHolder showViewHolder, int position) {
       Show show = showList.get(position );

       //String showType = show.getType();
       String showName = show.getTitle();
       //String showGenre = show.getGenre();
       String showImageUrl = show.getPosterpath();

       showViewHolder.titleView.setText(showName);
       //showViewHolder.genreView.setText(showGenre);
       //showViewHolder.typeView.setText(showType);

        Glide.with(context).load(showImageUrl).centerCrop().into(showViewHolder.showView);
    }

    @Override
    public int getItemCount() {
        return this.showList.size();
    }

    public class ShowViewHolder extends RecyclerView.ViewHolder{
        public ImageView showView;
        //public TextView typeView;
        //public TextView genreView;
        public TextView titleView;



        public ShowViewHolder(View itemView) {
            super(itemView);
            showView = itemView.findViewById(R.id.image_view);
            //typeView = itemView.findViewById(R.id.text_view_type_show);
            //genreView = itemView.findViewById(R.id.text_view_genre);
            titleView = itemView.findViewById(R.id.text_view_title);

        }
    }
}
