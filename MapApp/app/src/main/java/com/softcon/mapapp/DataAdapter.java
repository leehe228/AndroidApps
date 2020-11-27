package com.softcon.mapapp;

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
        ImageView line1View = (ImageView) view.findViewById(R.id.ListView_line1);
        ImageView line2View = (ImageView) view.findViewById(R.id.ListView_line2);
        ImageView line3View = (ImageView) view.findViewById(R.id.ListView_line3);
        ImageView line4View = (ImageView) view.findViewById(R.id.ListView_line4);

        TitleTextView.setText(sample.get(position).getTitle());

        String[] stringLines = sample.get(position).getLine().split(":");

        for (String stringLine : stringLines) {
            switch (Integer.parseInt(stringLine)) {
                case 1: {
                    line1View.setImageResource(R.drawable.line1);
                    break;
                }
                case 2: {
                    line1View.setImageResource(R.drawable.line2);
                    break;
                }
                case 3: {
                    line1View.setImageResource(R.drawable.line3);
                    break;
                }
                case 4: {
                    line1View.setImageResource(R.drawable.line4);
                    break;
                }
                case 5: {
                    line1View.setImageResource(R.drawable.line5);
                    break;
                }
                case 6: {
                    line1View.setImageResource(R.drawable.line6);
                    break;
                }
                case 7: {
                    line1View.setImageResource(R.drawable.line7);
                    break;
                }
                case 8: {
                    line1View.setImageResource(R.drawable.line8);
                    break;
                }
                case 9: {
                    line1View.setImageResource(R.drawable.line9);
                    break;
                }
            }
        }

        return view;
    }
}
