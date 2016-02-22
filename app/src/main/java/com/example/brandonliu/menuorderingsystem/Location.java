package com.example.brandonliu.menuorderingsystem;

/**
 * Created by brandonliu on 2/20/16.
 */
public class Location {
    public Location(int lat, int lon) {
        m_latitude = lat;
        m_longitude = lon;
    }
    public void setLatitude(int lat) {
        m_latitude = lat;
    }
    public void setLongitude(int lon) {
        m_longitude = lon;
    }


    public int getLatitude() {
        return m_latitude;
    }

    public int getLongitude() {
        return m_longitude;
    }

    private int m_longitude;
    private int m_latitude;
}

