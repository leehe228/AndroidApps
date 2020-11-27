package com.AnonymousHippo.Palette;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class GridListAdapter extends BaseAdapter {
    ArrayList<String> titleList = new ArrayList<>();
    ArrayList<Bitmap> imageList = new ArrayList<>();
    ArrayList<String> codeList = new ArrayList<>();
    Context mContext;

    public void addItem(String title, Bitmap image, String code){
        titleList.add(title);
        imageList.add(image);
        codeList.add(code);
    }

    @Override
    public int getCount(){
        return titleList.size();
    }

    @Override
    public Object getItem(int position) {
        return codeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        mContext = parent.getContext();
        LayoutInflater layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ViewHolder") View view = layoutInflater.inflate(R.layout.grid_list, parent, false);

        ImageView mainImageView = view.findViewById(R.id.GridList_ImageView);
        TextView titleTextView = view.findViewById(R.id.GridList_TextView_title);

        mainImageView.setImageBitmap(imageList.get(position));
        titleTextView.setText(titleList.get(position));

        return view;
    }
}
