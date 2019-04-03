package com.example.moviemax;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ShowAdapter extends RecyclerView.Adapter<ShowAdapter.ShowViewHolder> {
    private Context context;
    private ArrayList<Show> showList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{

        void onItemClick(int position);

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

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
       String showGenre = show.getGenreToString();
       String showImageUrl = show.getPosterpath();




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
        public ImageView showView;
        public TextView genreView;
        public TextView titleView;
        public TextView descriptionView;
        public TextView trailerView;
        public Button addButton;
        public Button shareButton;



        public ShowViewHolder(View itemView) {
            super(itemView);
            showView = itemView.findViewById(R.id.image_view);
            descriptionView = itemView.findViewById(R.id.text_view_detail_description);
            genreView = itemView.findViewById(R.id.text_view_genre);
            titleView = itemView.findViewById(R.id.text_view_title);
            trailerView = itemView.findViewById(R.id.image_view_detail_trailerLink);
            addButton = itemView.findViewById(R.id.image_view_detail_add_button);
            shareButton = itemView.findViewById(R.id.image_view_detail_share_button);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            onItemClickListener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }
}
