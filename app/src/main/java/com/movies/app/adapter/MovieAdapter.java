package com.movies.app.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.movies.app.R;
import com.movies.app.model.Movie;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private final List<Movie> mMoviesList;

    private final Context context;

    private final MovieAdapterOnClickHandler mClickHandler;

    public interface MovieAdapterOnClickHandler {
        void onClick(int position);
    }


    public MovieAdapter(List<Movie> moviesList, Context context, MovieAdapterOnClickHandler clickHandler) {
        this.mMoviesList = moviesList;
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
    public void onBindViewHolder(final MovieViewHolder holder, int position) {
        String posterUri = mMoviesList.get(position).getPosterURL();
        Picasso.with(context).load(posterUri).into(holder.imageView, new Callback() {
            @Override
            public void onSuccess() {
                holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {

            }
        });

    }

    @Override
    public int getItemCount() {
        if (mMoviesList != null) {
            return mMoviesList.size();
        } else {
            return 0;
        }

    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView imageView;
        final ProgressBar progressBar;

        MovieViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.tv_text);
            progressBar = itemView.findViewById(R.id.poster_pb);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(adapterPosition);
        }
    }

    public void setMoviesArray() {
        mMoviesList.clear();
        notifyDataSetChanged();
    }
}
