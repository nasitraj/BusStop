package com.example.busstop;

import android.util.Log;
import android.widget.ListView;

import org.chromium.net.CronetException;
import org.chromium.net.UrlRequest;
import org.chromium.net.UrlResponseInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;

public class MyUrlRequestCallback extends UrlRequest.Callback{
    String data = "";

    StopDetails stopDetails;
    Stop stop = new Stop(0,"");


    public MyUrlRequestCallback( StopDetails ma){
        stopDetails = ma;
    }
    @Override
    public void onRedirectReceived(UrlRequest request, UrlResponseInfo info, String newLocationUrl) throws Exception {
        Log.i("MyUrlRequestCallback", "onRedirectReceived method called.");

        // You should call the request.followRedirect() method to continue
        // processing the request.
        request.followRedirect();
    }

    @Override
    public void onResponseStarted(UrlRequest request, UrlResponseInfo info) throws Exception {
        Log.i("MyUrlRequestCallback", "onResponseStarted method called.");
        ByteBuffer buffer = ByteBuffer.allocateDirect(102400);
        String data = "";
        int httpCode = info.getHttpStatusCode();
        if( checkHttpErrorCode(httpCode)){
            Log.i("onResponseStarted", "Http Connection is Success");
            request.read(buffer);
        }else{
            Log.i("onResponseStarted", "Http Connection Failed");
            request.read(buffer);
            request.cancel();
            if(buffer.hasArray()){
                data = new String(buffer.array());
            }
            Log.i("onResponseStarted",Integer.toString(httpCode) );
            Log.i("onResponseStarted",data );
            System.out.print(buffer);
        }
    }

    @Override
    public void onReadCompleted(UrlRequest request, UrlResponseInfo info, ByteBuffer byteBuffer) throws Exception {
        Log.i("MyUrlRequestCallback", "onReadCompleted method called.");
        String data = converter(byteBuffer);
        Log.i("MyUrlRequestCallback", data);
        data = subString(data);
        int length = data.length();
        // char character = data.charAt(127);
        Log.i("MyUrlRequestCallback", data);

        if(data != null){
            JSONObject jsonObject = new JSONObject(data);
            JSONObject gernalData = jsonObject.getJSONObject("GetRouteSummaryForStopResult");
            JSONObject routeData = gernalData.getJSONObject("Routes");
            Iterator gernalKey = gernalData.keys();
            Iterator routeKey = routeData.keys();
            int stopNo = gernalData.getInt("StopNo");
            if(gernalData.has("StopDescription")) {
                String stopName = gernalData.getString("StopDescription");
                stop = new Stop(stopNo,stopName);
            }else {
                stop = new Stop(stopNo);
            }

            if(routeData.getString("Route") != "null" && routeData.getString("Route") != null ){
                routeData = checkRoute(routeData);
                JSONArray routeArray = routeData.getJSONArray("Route");
                for(int i = 0;i<routeArray.length();i++){
                    JSONObject obj = routeArray.getJSONObject(i);
                    Bus bus = new Bus(obj.getInt("RouteNo"), obj.getString("RouteHeading"));
                    Boolean check = obj.has("Trip");
                    JSONArray triparray;
                    if(obj.has("Trip")) {
                        JSONObject tripObj = obj.getJSONObject("Trips");
                        triparray = tripObj.getJSONArray("Trip");
                    }else{
                        triparray = obj.getJSONArray("Trips");
                    }
                   /* for(int j = 0; j <triparray.length();j++) {
                        Trips trip = new Trips();
                        trip.setAdjustedScheduleTime(triparray.getJSONObject(i).getInt("AdjustedScheduleTime"));
                        bus.addTrip(trip);
                    }*/
                    stop.addBus(bus);
                }

            }
        }
        byteBuffer.clear();
        request.read(byteBuffer);

    }

    @Override
    public void onSucceeded(UrlRequest request, UrlResponseInfo info) {
        Log.i("MyUrlRequestCallback", "onSucceeded method called.");
        updateList();
    }

    @Override
    public void onFailed(UrlRequest request, UrlResponseInfo info, CronetException error) {
        Log.i("MyUrlRequestCallback", "onFailed method called.");
    }

    private String converter(ByteBuffer buffer){
        String data = "";
        if(buffer.hasArray()){
            data = new String(buffer.array());
        }
        return data;
    }

    private String subString(String data){
        int begin = data.indexOf("{");
        int end = data.lastIndexOf("}");
        data = data.substring(begin,end+1);
        return data;
    }

    private Boolean checkHttpErrorCode(int httpCode){
        if( httpCode/100 == 2) {
            return true;
        }
        return false;
    }

    private JSONObject checkRoute(JSONObject object) throws JSONException {
        String data  = object.toString();
        JSONObject checked = object;
        String subdata = data.substring(0,10);
        int index = subdata.indexOf('[') ;
        if(subdata.indexOf('[') == -1){
            int first = data.indexOf(':')+ 1;
            int last = data.length() -1;
            String firstHalf = data.substring(0,first);
            String secondHalf = data.substring(first , last);
            String thirdHalf = data.substring(last);
            String whole = firstHalf + "[" + secondHalf + "]" + thirdHalf;
            checked = new JSONObject(whole);
        }
        return checked;
    }


    private void updateList(){
        stopDetails.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                ArrayList<String> bus = new ArrayList<>();
                ArrayList<Bus> buses = stop.getBuses();
                String busDis = "";
                for(int i = 0; i <buses.size();i++){
                    busDis = (Integer.toString(buses.get(i).getBusNum()) + " : " + buses.get(i).getBusName());
                    bus.add(busDis);
                }
                ListView lv = stopDetails.findViewById(R.id.lt_BusNos);
                //  BusRouteTimeAdapter adapter = new BusRouteTimeAdapter(stopDetails,0,buses);
                //    lv.setAdapter(adapter);

            }
        });
    }


}