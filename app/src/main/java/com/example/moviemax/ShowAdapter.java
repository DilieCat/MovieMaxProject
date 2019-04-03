package com.example.moviemax;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.w3c.dom.Text;

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

        final ShowViewHolder showViewHolder = new ShowViewHolder(v);
        showViewHolder.viewContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);

                Show show = showList.get(showViewHolder.getAdapterPosition());
                String imgJson = new Gson().toJson(show);
                intent.putExtra("Show", imgJson);

                context.startActivity(intent);
            }
        });
        return showViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ShowViewHolder showViewHolder, int position) {
       Show show = showList.get(position );

       //String showType = show.getType();
       String showName = show.getTitle();
       String showGenre = show.getGenreToString();
       String showImageUrl = show.getPosterPath();


       showViewHolder.titleView.setText("Title: \n" + showName);

       showViewHolder.genreView.setText("Genre's: \n" + showGenre);
//       showViewHolder.genreView.setText(showGenre);
       //showViewHolder.typeView.setText(showType);

        Glide.with(context).load(showImageUrl).centerCrop().into(showViewHolder.showView);
    }

    @Override
    public int getItemCount() {
        return this.showList.size();
    }

    public class ShowViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout viewContainer;
        ImageView showView;
        TextView genreView;
        TextView titleView;
        TextView descriptionView;
        TextView trailerView;
        Button addButton;
        Button shareButton;


        public ShowViewHolder(View itemView) {
            super(itemView);

            viewContainer = itemView.findViewById(R.id.item_container);
            showView = itemView.findViewById(R.id.image_view);
            descriptionView = itemView.findViewById(R.id.text_view_detail_description);
            genreView = itemView.findViewById(R.id.text_view_genre);
            titleView = itemView.findViewById(R.id.text_view_title);
            trailerView = itemView.findViewById(R.id.image_view_detail_trailerLink);
            addButton = itemView.findViewById(R.id.image_view_detail_add_button);
            shareButton = itemView.findViewById(R.id.image_view_detail_share_button);
        }
    }
}
