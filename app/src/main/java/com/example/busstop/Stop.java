package com.example.busstop;

import java.util.ArrayList;

public class Stop {
    private int stopNumber;

    public String getStopId() {
        return stopId;
    }

    public void setStopId(String stopId) {
        this.stopId = stopId;
    }

    private String stopId;

    public int getStopNumber() {
        return stopNumber;
    }

    public void setStopNumber(int stopNumber) {
        this.stopNumber = stopNumber;
    }

    public String getStopName() {
        return stopName;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

    private ArrayList<Bus> buses = new ArrayList<Bus>();
    private String stopName;

    public Stop(int num ){
        stopNumber = num;
    }

    public Stop(int num, String name ){
        stopNumber = num;
        stopName = name;
    }

    public Stop(String id, int num, String name ){
        stopNumber = num;
        stopName = name;
        stopId = id;
    }

    public void addBus(Bus bus){
        buses.add(bus);
    }

    public ArrayList<Bus> getBuses(){
        return buses;
    }



}
