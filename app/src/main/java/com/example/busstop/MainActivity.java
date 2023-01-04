package com.example.busstop;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> stopNos = new ArrayList<>();
    static ArrayList<Stop> stops = new ArrayList<>();
    ListView lv;
    double longitude = -1;
    double latitude = -1;

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setLocation();
        setData();
        setList();
        intiSearchWidget();


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == -1) {
                    return;
                }
                String stopNo = Integer.toString(stops.get(position).getStopNumber());
                String stopDis = stops.get(position).getStopName();
                String stopId = stops.get(position).getStopId();
                Intent intent = new Intent(MainActivity.this, StopDetails.class);
                intent.putExtra("StopNo", stopNo);
                intent.putExtra("StopDis", stopDis);
                intent.putExtra("StopID", stopId);
                startActivity(intent);
            }
        });
    }


    private String convertBusStopNo(int stopNo) {
        String data = "";
        if (stopNo >= 1000 && stopNo <= 9999) {
            data = Integer.toString(stopNo);
        } else if (stopNo % 100 < 10) {
            data = "0" + Integer.toString(stopNo);
        } else if (stopNo % 10 < 10) {
            data = "00" + Integer.toString(stopNo);
        } else {
            data = "000" + Integer.toString(stopNo);
        }
        return data;
    }

    public void addStops(Stop s) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                stopNos.add(Integer.toString(s.getStopNumber()));
                adapter.notifyDataSetChanged();

            }
        });

    }

    public void setData() {

        if(longitude == -1 || latitude == -1){
            StopFile stopFile = new StopFile(this.getApplicationContext());
            stopFile.run();
            stops = stopFile.getStopNo();
        }else{
            StopFile stopFile = new StopFile(this.getApplicationContext(),longitude,latitude) ;
            stopFile.run();
            stopFile.sort();
        }

    }

    public void setList() {
        lv = (ListView) findViewById(R.id.lt_StopNo);
        BusStopAdapter adapter = new BusStopAdapter(this, 0, stops);
        lv.setAdapter(adapter);
    }

    private void intiSearchWidget() {
        SearchView searchView = (SearchView) findViewById(R.id.searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Stop> updatedList = new ArrayList<>();
                for (Stop s : stops) {
                    if (s.getStopName().toLowerCase().contains(newText.toLowerCase())
                            || Integer.toString(s.getStopNumber()).toLowerCase().contains(newText.toLowerCase())) {
                        updatedList.add(s);
                    }
                }

                BusStopAdapter adapter = new BusStopAdapter(getApplicationContext(), 0, updatedList);
                lv.setAdapter(adapter);
                return false;
            }
        });
    }

    private void setLocation() {

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        ActivityResultLauncher<String> requestPermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {

            if (isGranted) {
                @SuppressLint("MissingPermission") Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                 longitude = location.getLongitude();
                 latitude = location.getLatitude();
            } else {
                // Explain to the user that the feature is unavailable because the
                // features requires a permission that the user has denied. At the
                // same time, respect the user's decision. Don't link to system
                // settings in an effort to convince the user to change their
                // decision.
            }
        });
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            return;
        }else {
            @SuppressLint("MissingPermission") Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }
    }



}

