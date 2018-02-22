package com.popular_movies_app_st_1.app.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.popular_movies_app_st_1.app.R;
import com.popular_movies_app_st_1.app.model.Movie;
import com.squareup.picasso.Picasso;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private Movie[] moviesArray;

    private Context context;

    private MovieAdapterOnClickHandler mClickHandler;

    public interface MovieAdapterOnClickHandler {
        void onClick(int position);
    }

    public MovieAdapter(Movie[] moviesArray, Context context, MovieAdapterOnClickHandler clickHandler) {
        this.moviesArray = moviesArray;
        this.context = context;
        this.mClickHandler = clickHandler;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.movie_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new MovieViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Picasso.with(context).load(moviesArray[position].getPosterImage()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        if (moviesArray != null) {
            return moviesArray.length;
        } else {
            return 0;
        }

    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;

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

    public void setMoviesArray(Movie[] movies) {
        moviesArray = movies;
        notifyDataSetChanged();

    }
}
