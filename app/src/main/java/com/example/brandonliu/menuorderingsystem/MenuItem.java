package com.example.brandonliu.menuorderingsystem;

/**
 * Created by Jennifer on 2/20/16.
 */
public class MenuItem implements Comparable<MenuItem>{
    public MenuItem(String category, String name, double price)
    {
        m_cat = category;
        m_name = name;
        m_price = price;
        m_selected = false;
    }

    public int compareTo(MenuItem o) {
        return (this.getCategory()).compareTo(o.getCategory());
    }

    public String getCategory() { return m_cat; }
    public String getName() { return m_name; }
    public double getPrice() { return m_price; }
    public boolean getSelected() { return m_selected; }
    public void select() { m_selected = true; }
    public void deselect() { m_selected = false; }

    private String m_cat;
    private String m_name;
    private double m_price;
    private boolean m_selected;
}
