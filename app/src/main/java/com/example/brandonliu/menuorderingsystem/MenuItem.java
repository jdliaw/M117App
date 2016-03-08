package com.example.brandonliu.menuorderingsystem;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.os.Parcel;
import android.util.Log;

/**
 * Created by Jennifer on 2/20/16.
 */
//IMPLEMENTS BOTH INTERFACES
public class MenuItem implements Comparable<MenuItem>, Parcelable {
    public MenuItem(int id, String category, String name, double price)
    {
        m_id = id;
        m_cat = category;
        m_name = name;
        m_price = price;
        m_quantity = 0;
    }
    //compares menu items
    public int compareTo(MenuItem o) {
        return (this.getCategory()).compareTo(o.getCategory());
    }

    //get methods
    public String getCategory() { return m_cat; }
    public String getName() { return m_name; }
    public double getPrice() { return m_price; }
    public int getQuantity() { return m_quantity; }
    public int getId() { return m_id; }
    //set methods
    public void setCategory(String cat) { m_cat = cat; }
    public void setQuantity(int quan) {
        m_quantity = quan;
    }

    //private vars
    private String m_cat;
    private String m_name;
    private double m_price;
    private int m_quantity;
    private int m_id;

    //initializer in a sense
    protected MenuItem(Parcel in) {
        m_id = in.readInt();
        m_cat = in.readString();
        m_name = in.readString();
        m_price = in.readDouble();
        m_quantity = in.readInt();
    }

    //return 0, not important not called.
    @Override
    public int describeContents() {
        return 0;
    }

    //what we want to send
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(m_cat);
        dest.writeString(m_name);
        dest.writeDouble(m_price);
        dest.writeInt(m_quantity);
    }

    //add a creator
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MenuItem> CREATOR = new Parcelable.Creator<MenuItem>() {
        @Override
        public MenuItem createFromParcel(Parcel in) {
            return new MenuItem(in);
        }

        @Override
        public MenuItem[] newArray(int size) {
            return new MenuItem[size];
        }
    };
}