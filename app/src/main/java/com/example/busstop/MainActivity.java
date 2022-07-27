package com.example.busstop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> stopNos =new ArrayList<String>();
    static ArrayList<Stop> stops = new ArrayList<>();
    ListView lv;

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setData();
        setList();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == -1){
                    return;
                }
                String stopNo = Integer.toString(stops.get(position).getStopNumber());
                String stopDis = stops.get(position).getStopName();
                String stopId = stops.get(position).getStopId();
                Intent intent = new Intent(MainActivity.this,StopDetails.class);
                intent.putExtra("StopNo", stopNo);
                intent.putExtra("StopDis", stopDis);
                intent.putExtra("StopID", stopId);
                startActivity(intent);
            }
        });
    }




    private String convertBusStopNo(int stopNo){
        String data = "";
        if(stopNo >= 1000  && stopNo <=9999){
            data = Integer.toString(stopNo);
        }else if (stopNo%100 < 10){
            data = "0" + Integer.toString(stopNo);
        }else if (stopNo%10 < 10){
            data = "00" + Integer.toString(stopNo);
        }else{
            data = "000" + Integer.toString(stopNo);
        }
        return data;
    }

    public void addStops(Stop s){

        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                stopNos.add(Integer.toString(s.getStopNumber()));
                adapter.notifyDataSetChanged();

            }
        });

    }

    public void setData(){
        StopFile stopFile = new StopFile(this.getApplicationContext());
        stopFile.run();
        stops = stopFile.getStopNo();
        stops.remove(0);
    }

    public void setList(){
        lv = (ListView)findViewById(R.id.lt_StopNo);
        BusStopAdapter adapter = new BusStopAdapter(this,0,stops);
        lv.setAdapter(adapter);
    }


}

