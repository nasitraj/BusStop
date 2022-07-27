package com.example.busstop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class BusStopAdapter extends ArrayAdapter<Stop>
{


    public BusStopAdapter( Context context, int resource,  List<Stop> stops) {
        super(context, resource, stops);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Stop stop = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.bus_stop, parent, false);
        }

        TextView tv_BusStop = (TextView)convertView.findViewById(R.id.tv_BusStopNo);
        TextView tv_StopDis = (TextView)convertView.findViewById(R.id.tv_StopDis);

        tv_BusStop.setText(Integer.toString(stop.getStopNumber()));
        tv_StopDis.setText(stop.getStopName());

        return convertView;
    }
}
