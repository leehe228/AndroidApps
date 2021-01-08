package com.softcon.thetutor;

public class ListViewItem {
    private int ColorString;
    private String subTitleString;
    private String subDetailString;

    public void setColor(int color){
        ColorString = color;
    }
    public void setTitle(String title){
        subTitleString = title;
    }
    public void setDetail(String detail){
        subDetailString = detail;
    }

    public int getColor(){
        return this.ColorString;
    }
    public String getTitle(){
        return subTitleString;
    }
    public String getDetail(){
        return subDetailString;
    }
}
