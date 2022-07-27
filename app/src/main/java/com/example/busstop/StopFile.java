package com.example.busstop;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class StopFile {

    private static Context context;
    ArrayList<Stop> stopNos = new ArrayList<Stop>();

    public StopFile(Context c){
        context = c;
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
        String num = s.substring(first,second);
        String dis = s.substring(second+2,third-1);
        String id = s.substring(0,first-1);
        Stop stop;
        if(isNumeric(num))
            stop = new Stop(id, Integer.parseInt(num),dis);
        else
            stop = new Stop(id,0,dis);
        return stop;
    }

    private static boolean isNumeric(String str){
        return str != null && str.matches("[0-9.]+");
    }
}
