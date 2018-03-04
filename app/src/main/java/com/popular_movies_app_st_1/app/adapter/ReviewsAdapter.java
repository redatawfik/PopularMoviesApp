package com.popular_movies_app_st_1.app.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.popular_movies_app_st_1.app.R;
import com.popular_movies_app_st_1.app.model.Review;

import java.util.List;

public class ReviewsAdapter extends ArrayAdapter<Review> {

    public ReviewsAdapter(@NonNull Context context, List<Review> reviews) {
        super(context, 0, reviews);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Review review = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.review_item, parent, false);
        }

        TextView tvAuthor = convertView.findViewById(R.id.tv_author_name);
        TextView tvContent = convertView.findViewById(R.id.tv_review_content);
        tvAuthor.setText(review.getAuthor());
        tvContent.setText(review.getContent());

        return convertView;

    }
}
