package com.example.busstop;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Trips {

    public int getBusNo() {
        return busNo;
    }

    public void setBusNo(int busNo) {
        this.busNo = busNo;
    }

    int busNo = 0;
    public Trips(String t, int No){
        t = t.replaceAll("\"", "");
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        busNo = No;
        try {
            time = (Date)formatter.parse(t);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Date getTime() {
        return time;
    }

    public String getTimeDiffernce() throws ParseException {
        DateFormat formatter = new SimpleDateFormat("HH:mm");
        Date date = new Date();
        String sDate = formatter.format(date);
        date = formatter.parse(sDate);

        long difference = time.getTime() - date.getTime();

        int mins = (int) (difference/60000);
        if(mins< 60)
            return String.valueOf(mins) + " mins";
        float hour= mins/60.0f;
        String[] arr=String.valueOf(hour).split("\\.");
        int[] intArr=new int[2];
        intArr[0]=Integer.parseInt(arr[0]); // 1
        intArr[1]=Integer.parseInt(arr[1]);

        int min = (int) ((hour-intArr[0])*60);
        String result = Integer.toString(intArr[0]) + "h" + Integer.toString(min) + "mins";
        return result;
    }

    Date time;

}

