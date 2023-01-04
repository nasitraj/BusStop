package com.example.busstop;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class StopTimeFile {

    private String stop_ID;
    private ArrayList<Trips> trips = new ArrayList<>();
    private ArrayList<String> text = new ArrayList<>();
    private static Context context;

    public StopTimeFile(Context c, String id){
        context = c;
        stop_ID = id;
    }


    public void run() throws ParseException {
        Date date = new Date();
        InputStream is = null;
        int day = getDay(date);
        if(day > 1  && day <= 6) {
            is = context.getResources().openRawResource(R.raw.updatedstoptimes);
        }else if(day == 7){
            is = context.getResources().openRawResource(R.raw.saturdaytimes);
        }else if(day == 1){
            is = context.getResources().openRawResource(R.raw.sundaytimes);
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        readData(reader);
    }


    private void readData(BufferedReader bufferedReader) throws ParseException {
        String line = "";

        while (true){
            try {
                if (!((line = bufferedReader.readLine()) != null))
                    break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(line != null)
                if(check(line))
                    text.add(line);
        }
        trips = getBuses(text);
    }

    private Boolean check(String s){
        int first = s.indexOf(",");

        String id = s.substring(0,first);
        if(stop_ID.equals(id))
            return true;

        return false;
    }

    private ArrayList<Trips> getBuses(ArrayList<String> data) throws ParseException {
        for (String trip: data) {
            String[] tripData = trip.split(",");
            String[] busNo = tripData[2].split("-");
            Trips t = new Trips(tripData[1],Integer.parseInt(busNo[0]));
            DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            Date date = new Date();
            String sDate = formatter.format(date);
            date = formatter.parse(sDate);

            if(t.getTime().after(date))
                trips.add(t);
        }

        return trips;

    }


    public ArrayList<Trips> getData(){
        return trips;
    }


    public int getDay(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        return day;
    }
}
