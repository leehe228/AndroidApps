package com.softcon.findway;

public class DataClass {
    private String Title;
    private String line;

    public DataClass(String title, String line){
        this.Title = title;
        this.line = line;
    }

    public String getTitle(){
        return this.Title;
    }

    public String getLine(){
        return this.line;
    }
}
