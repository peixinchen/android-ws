package com.peixinchen.mion;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

class GridViewAdapter extends ArrayAdapter<String> {
    private Context context;
    private int resource;

    GridViewAdapter(@NonNull Context context, int resource) {
        super(context, resource);

        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(resource, parent,false);
        }

        String imageUrl = (String) this.getItem(position);
        Picasso.with(context)
                .load(imageUrl)
                .into((ImageView) view.findViewById(R.id.imageView));

        return view;
    }
}
