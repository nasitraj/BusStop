package com.example.busstop;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class StopFile {

    private static Context context;
    ArrayList<Stop> stopNos = new ArrayList<Stop>();
    private double longitude = -1;
    private double latitude = -1;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public StopFile(Context c) {
        context = c;
    }

    public StopFile(Context c, double lon, double lat) {
        context = c;
        longitude = lon;
        latitude = lat;
    }

    public void run() {

        InputStream is = context.getResources().openRawResource(R.raw.stopcodes);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        getStopNos(reader);

    }

    private ArrayList<Stop> getStopNos(BufferedReader bufferedReader){
        String line = "";

        while (true){
            try {
                if (!((line = bufferedReader.readLine()) != null))
                    break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(line != null)
                stopNos.add(getStop(line));

        }
        return stopNos;
    }

    public ArrayList<Stop> getStopNo(){
        return stopNos;
    }

    private Stop getStop(String s){
        int first = s.indexOf(",") + 1;
        int second = s.indexOf(",",first);
        int third = s.indexOf("," , second + 1);
        int forth = s.indexOf(",", third + 1);

            String num = s.substring(first, second);
            String dis = s.substring(second + 1, third);
            String id = s.substring(0, first - 1);
            String lon = s.substring(third+1, forth);
            String lan = s.substring(forth+1);
            Double lo = Double.parseDouble(lon);
            Double lat = Double.parseDouble(lan);
            Stop stop;
            if (isNumeric(num)) {
                stop = new Stop(id, Integer.parseInt(num), dis);
                stop.setLatitude(lat);
                stop.setLongutidue(lo);
                stop.setDist(longitude, latitude);
            }
            else
                stop = new Stop(id, 0, dis);
            return stop;

    }

    private static boolean isNumeric(String str){
        return str != null && str.matches("[0-9.]+");
    }


    public void sort() {
        Collections.sort(stopNos);
    }
}
