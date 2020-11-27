package com.softcon.findway;

public class ResultDataClass {
    private int TYPE;

    private String START;
    private String MIN;
    private String END;
    private int LEVEL;
    private String STATION_NAME;
    private int LINE1;
    private int LINE2;
    private String INFORMATION1;
    private String INFORMATION2;
    private String INFORMATION3;

    public ResultDataClass(int TYPE, String START, String MIN, String END, int LEVEL, String STATION_NAME, int LINE1, int LINE2, String INFORMATION1, String INFORMATION2, String INFORMATION3){
        this.TYPE = TYPE;
        this.START = START;
        this.MIN = MIN;
        this.END = END;
        this.LEVEL = LEVEL;
        this.STATION_NAME = STATION_NAME;
        this.LINE1 = LINE1;
        this.LINE2 = LINE2;
        this.INFORMATION1 = INFORMATION1;
        this.INFORMATION2 = INFORMATION2;
        this.INFORMATION3 = INFORMATION3;
    }

    public int getTYPE(){
        return this.TYPE;
    }

    public String getSTART(){
        return this.START;
    }

    public String getMIN(){
        return this.MIN;
    }

    public String getEND(){
        return this.END;
    }

    public int getLEVEL(){
        return this.LEVEL;
    }

    public String getSTATION_NAME(){
        return this.STATION_NAME;
    }

    public int getLINE1(){
        return this.LINE1;
    }


    public int getLINE2(){
        return this.LINE2;
    }

    public String getINFORMATION1(){
        return this.INFORMATION1;
    }

    public String getINFORMATION2(){
        return this.INFORMATION2;
    }

    public String getINFORMATION3(){
        return this.INFORMATION3;
    }


}
