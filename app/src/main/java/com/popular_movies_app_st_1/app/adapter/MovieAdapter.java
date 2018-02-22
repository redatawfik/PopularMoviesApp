package com.popular_movies_app_st_1.app.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.popular_movies_app_st_1.app.R;
import com.popular_movies_app_st_1.app.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private ArrayList<Movie> moviesArrayList;

    private final Context context;

    private final MovieAdapterOnClickHandler mClickHandler;

    public interface MovieAdapterOnClickHandler {
        void onClick(int position);
    }

    public MovieAdapter(ArrayList<Movie> moviesArrayList, Context context, MovieAdapterOnClickHandler clickHandler) {
        this.moviesArrayList = moviesArrayList;
        this.context = context;
        this.mClickHandler = clickHandler;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.movie_item;
        LayoutInflater inflater = LayoutInflater.from(context);


        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new MovieViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Picasso.with(context).load(moviesArrayList.get(position).getPosterImage()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        if (moviesArrayList != null) {
            return moviesArrayList.size();
        } else {
            return 0;
        }

    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView imageView;

        MovieViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.tv_text);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(adapterPosition);
        }
    }

    public void setMoviesArray() {
        moviesArrayList = null;
        notifyDataSetChanged();

    }
}
