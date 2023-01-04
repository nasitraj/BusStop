package com.example.busstop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import org.chromium.net.CronetEngine;
import org.chromium.net.UrlRequest;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class StopDetails extends AppCompatActivity {
    String stopNo = "";
    String stopDis = "";
    String stopID = "";

    @Override
    synchronized protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_details);
        stopNo = getIntent().getExtras().getString("StopNo");
        stopDis = getIntent().getExtras().getString("StopDis");
        stopID = getIntent().getExtras().getString("StopID");
        TextView tv_StopNo = (TextView) findViewById(R.id.tv_StopNo);
        TextView tv_Dic = (TextView) findViewById(R.id.tv_Dic);
        tv_StopNo.setText(stopNo);
        tv_Dic.setText(stopDis);
        StopTimeFile stopTimeFile = new StopTimeFile(this, stopID);
        try {
            stopTimeFile.run();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ArrayList<Trips> trips = stopTimeFile.getData();
        ListView lv = findViewById(R.id.lt_BusNos);
        BusRouteTimeAdapter adapter = new BusRouteTimeAdapter(this, 0, trips);
        lv.setAdapter(adapter);
        //start();


    }


    private void start() {
        CronetEngine.Builder myBuilder = new CronetEngine.Builder(this);
        CronetEngine cronetEngine = myBuilder.build();
        Executor executor = Executors.newSingleThreadExecutor();

        String URL = "https://api.octranspo1.com/v2.0/GetNextTripsForStopAllRoutes?appID=a9149cb6&apiKey=ccbc338a233b4d7d22f8b1a3077293e6&stopNo=" + "7264" + "&format=JSON";
        UrlRequest.Builder requestBuilder = cronetEngine.newUrlRequestBuilder
                (URL,
                        new MyUrlRequestCallback(this), executor);
        UrlRequest urlRequest = requestBuilder.build();
        urlRequest.start();
    }




}