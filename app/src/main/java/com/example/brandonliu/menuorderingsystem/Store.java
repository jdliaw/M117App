package com.example.brandonliu.menuorderingsystem;

/**
 * Created by Jennifer on 2/20/16.
 */
public class Store implements Comparable<Store> {
    public Store(int id, String name, double dist) {
        m_id = id;
        m_name = name;
        m_dist = dist;
    }
    public int compareTo(Store o) {
//        if (this.m_dist > o.m_dist)
//            return 1;
//        if (this.m_dist < o.m_dist)
//            return -1;
//        return 0;
        return Double.compare(this.getDist(), o.getDist());
    }
    public int getID()
    {
        return m_id;
    }
    public String getName()
    {
        return m_name;
    }
    public double getDist()
    {
        return m_dist;
    }

    private int m_id;
    private String m_name;
    private double m_dist;
}
