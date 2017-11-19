package com.peixinchen.mion;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

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
        ViewHolder viewHolder;

        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            view = layoutInflater.inflate(resource, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) view.findViewById(R.id.imageView);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        String imageUrl = (String) this.getItem(position);
        Log.d("API", String.format("%d: %s", position, imageUrl));
        Glide.with(context)
                .load(imageUrl)
                .into(viewHolder.imageView);

        return view;
    }

    static class ViewHolder {
        ImageView imageView;
    }
}
