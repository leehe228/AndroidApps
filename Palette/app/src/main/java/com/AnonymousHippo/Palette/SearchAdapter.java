package com.AnonymousHippo.Palette;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.util.ArrayList;

public class SearchAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    ArrayList<String> sample;
    ArrayList<Bitmap> imageList;

    public SearchAdapter(Context context, ArrayList<String> data, ArrayList<Bitmap> img){
        mContext = context;
        sample = data;
        mLayoutInflater = LayoutInflater.from(mContext);
        imageList = img;
    }

    @Override
    public int getCount() {
        return sample.size();
    }

    @Override
    public String getItem(int position) {
        return sample.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.listview_search, null);

        TextView titleTextView = view.findViewById(R.id.List_TextView_title);
        TextView infoTextView = view.findViewById(R.id.List_TextView_info);
        ImageView mainImageView = view.findViewById(R.id.List_ImageView_main);

        titleTextView.setText(sample.get(position).split("-")[0]);
        infoTextView.setText(sample.get(position).split("-")[1]);

        mainImageView.setImageBitmap(imageList.get(position));

        return view;
    }
}
