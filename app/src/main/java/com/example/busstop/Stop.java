package com.example.busstop;

import java.util.ArrayList;

public class Stop implements Comparable{
    private int stopNumber;

    private double longutidue;

    private double dist = -1;

    public double getDist(){
        return dist;
    }
    public double getLongutidue() {
        return longutidue;
    }

    public void setLongutidue(double longutidue) {
        this.longutidue = longutidue;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    private double latitude;

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


    @Override
    public int compareTo(Object o) {

        double comparedist = ((Stop)o).getDist();
        double difference = this.dist - comparedist;
        int diffinInt = (int)Math.round(difference);
        return diffinInt;
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts decimal degrees to radians             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts radians to decimal degrees             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public void setDist(Double lon2, Double lat2) {
        double theta = longutidue - lon2;
        dist = Math.sin(deg2rad(latitude)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(latitude)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;
    }
}
