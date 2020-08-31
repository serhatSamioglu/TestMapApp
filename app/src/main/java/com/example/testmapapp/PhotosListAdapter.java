package com.example.testmapapp;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PhotosListAdapter extends ArrayAdapter<String> {

    private final ArrayList<String> photos;
    private final Activity context;

    public PhotosListAdapter(ArrayList<String> photos, Activity context) {
        super(context, R.layout.photo_view,photos);
        this.photos = photos;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();

        View customView = layoutInflater.inflate(R.layout.photo_view,null,true );

        ImageView photo = customView.findViewById(R.id.photoView);
        Picasso.get().load(photos.get(position)).into(photo);

        return customView;
    }
}
