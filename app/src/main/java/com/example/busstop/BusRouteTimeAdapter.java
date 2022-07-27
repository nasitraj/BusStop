package com.example.busstop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class BusRouteTimeAdapter extends ArrayAdapter<Trips>
{


    public BusRouteTimeAdapter(Context context, int resource, List<Trips> trips) {
        super(context, resource, trips);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Trips t = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.bus_route_time, parent, false);
        }

        TextView tv_BusNo = (TextView)convertView.findViewById(R.id.tv_routeNo);
        TextView tv_RouteTime = (TextView)convertView.findViewById(R.id.tv_routeTime);
        TextView tv_TripTime = (TextView)convertView.findViewById(R.id.tv_TimeDefference);

        Format f = new SimpleDateFormat("HH:mm:ss");
        String strResult = f.format(t.getTime());


        tv_BusNo.setText(Integer.toString(t.getBusNo()));
        tv_RouteTime.setText(strResult);
        try {
            tv_TripTime.setText(t.getTimeDiffernce());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return convertView;
    }
}
