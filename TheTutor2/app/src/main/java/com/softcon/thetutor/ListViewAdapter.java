package com.softcon.thetutor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {

    private View subColor;
    private TextView subTitle;
    private TextView subDetail;

    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>();

    // ListViewAdapter 생성자
    public ListViewAdapter(){
        //
    }

    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // listView_item Layout을 inflate 하여 convertView 참조 획득
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate 된)으로부터 위젯에 대한 참조 획득
        subColor = (View)convertView.findViewById(R.id.listview_item_subcolor);
        subTitle = (TextView)convertView.findViewById(R.id.listview_item_subname);
        subDetail = (TextView)convertView.findViewById(R.id.listview_item_detail);

        ListViewItem listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        subColor.setBackgroundResource(listViewItem.getColor());
        subTitle.setText(listViewItem.getTitle());
        subDetail.setText(listViewItem.getDetail());

        return convertView;
    }

    public void addItem(int Color, String Title, String Detail){
        ListViewItem item = new ListViewItem();

        item.setColor(Color);
        item.setTitle(Title);
        item.setDetail(Detail);

        listViewItemList.add(item);
    }
}
