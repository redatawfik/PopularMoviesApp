package com.movies.app.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.movies.app.R;
import com.movies.app.model.Trailer;

import java.util.List;

public class TrailerAdapter extends ArrayAdapter<Trailer> {


    public TrailerAdapter(@NonNull Context context, List<Trailer> trailers) {
        super(context, 0, trailers);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Trailer trailer = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.trailer_item, parent, false);
        }

        TextView trailerName = convertView.findViewById(R.id.tv_trailer_name);
        trailerName.setText(trailer != null ? trailer.getName() : null);

        return convertView;

    }
}
