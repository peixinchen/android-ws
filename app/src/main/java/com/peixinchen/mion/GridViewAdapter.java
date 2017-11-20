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

import com.peixinchen.mion.models.ShareImageItem;
import com.squareup.picasso.Picasso;

import java.util.List;

class GridViewAdapter extends ArrayAdapter<ShareImageItem> {
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

        ShareImageItem shareImageItem = (ShareImageItem) this.getItem(position);
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);
        checkBox.setTag(position);
        checkBox.setChecked(shareImageItem.isChecked);
        Picasso.with(context)
                .load(shareImageItem.imageUrl)
                .into((ImageView) view.findViewById(R.id.imageView));

        return view;
    }
}
