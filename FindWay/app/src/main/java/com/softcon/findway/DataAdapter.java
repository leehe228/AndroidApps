package com.softcon.findway;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.drawable.DrawableResource;

import java.io.InputStreamReader;
import java.util.ArrayList;

public class DataAdapter extends BaseAdapter {
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<DataClass> sample;

    public DataAdapter(Context context, ArrayList<DataClass> data) {
        mContext = context;
        sample = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return sample.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public DataClass getItem(int position) {
        return sample.get(position);
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        @SuppressLint({"ViewHolder", "InflateParams"}) View view = mLayoutInflater.inflate(R.layout.listview_layout, null);

        TextView TitleTextView = (TextView) view.findViewById(R.id.ListView_title);
        ImageView[] lineView = new ImageView[4];
        lineView[0] = (ImageView) view.findViewById(R.id.ListView_line1);
        lineView[1] = (ImageView) view.findViewById(R.id.ListView_line2);
        lineView[2] = (ImageView) view.findViewById(R.id.ListView_line3);
        lineView[3] = (ImageView) view.findViewById(R.id.ListView_line4);

        TitleTextView.setText(sample.get(position).getTitle());

        String[] lines = new String[4];
        lines = sample.get(position).getLine().split(":");

        for (int i = 0; i < lines.length; i++) {
            switch (lines[i]){
                case "1":{
                    lineView[i].setImageResource(R.drawable.line1);
                    break;
                }
                case "2":{
                    lineView[i].setImageResource(R.drawable.line2);
                    break;
                }
                case "3":{
                    lineView[i].setImageResource(R.drawable.line3);
                    break;
                }
                case "4":{
                    lineView[i].setImageResource(R.drawable.line4);
                    break;
                }
                case "5":{
                    lineView[i].setImageResource(R.drawable.line5);
                    break;
                }
                case "6":{
                    lineView[i].setImageResource(R.drawable.line6);
                    break;
                }
                case "7":{
                    lineView[i].setImageResource(R.drawable.line7);
                    break;
                }
                case "8":{
                    lineView[i].setImageResource(R.drawable.line8);
                    break;
                }
                case "9":{
                    lineView[i].setImageResource(R.drawable.line9);
                    break;
                }
                default:{
                    //
                }
            }
        }

        return view;
    }
}
