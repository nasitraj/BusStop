package com.example.busstop;

import java.util.ArrayList;

public class Bus {
    private String busName;
    private int busNum;
    private ArrayList<Trips> trips = new ArrayList<>();

    public ArrayList<Trips> getTrips() {
        return trips;
    }

    public void addTrip(Trips trip) {
        this.trips.add(trip);
    }



    public Bus(int num){
        busNum = num;
    }

    public Bus(int num, String name){
        busNum = num;
        busName = name;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public int getBusNum() {
        return busNum;
    }

    public void setBusNum(int busNum) {
        this.busNum = busNum;
    }




}
