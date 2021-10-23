package com.example.lab2;

import android.location.Location;

import java.util.Arrays;

public class SpecificLocation {
    private float[] direction;
    private float angle;
    private float vectorAngle;

    public Location getLocation() {
        return location;
    }

    private Location location;
    private String locationName;

    public SpecificLocation(String locationName) {
        this.locationName = locationName;
        direction = new float[3];
    }

    public SpecificLocation(String locationName, double longitude, double latitude) {
        this(locationName);
        setMyObjectLocation(longitude, latitude);
    }

    public void setMyObjectLocation(double longitude, double latitude) {
        if (location == null) {
            location = new Location("");
        }
        location.setLatitude(latitude);
        location.setLongitude(longitude);
    }

    public void invalidate(Location sourceLocation, float[] deviceVector) {
        calculateAngleBetweenLocations(sourceLocation);
        calculateDirection();
        calculateVectorsAngle(deviceVector);
    }

    private void calculateAngleBetweenLocations(Location sourceLocation) {
        angle = sourceLocation.bearingTo(location);
    }

    private void calculateDirection() {
        direction[0] = (float) Math.sin(angle);
        direction[1] = (float) Math.cos(angle);
        direction[2] = 0;
    }

    private void calculateVectorsAngle(float[] vector) {
        vectorAngle = VectorUtils.getAngle(direction, vector);
    }

    @Override
    public String toString() {
        return "SpecificLocation{" +
                "direction=" + Arrays.toString(direction) +
                ", angle=" + angle +
                ", vectorAngle=" + vectorAngle +
                ", location=[" + location.getLongitude() + ", " + location.getLatitude() +"]"+
                ", locationName='" + locationName + '\'' +
                '}';
    }
}
