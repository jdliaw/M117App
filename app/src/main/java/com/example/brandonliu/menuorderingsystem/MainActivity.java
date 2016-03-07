package com.example.brandonliu.menuorderingsystem;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import android.location.Location;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    Button b;
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //testing stuff

        ScrollView sv = new ScrollView(this);
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        sv.addView(ll);
        this.setContentView(sv);

        b = new Button(this);
        b.setBackgroundColor(0xFF87CEFA);
        b.setText("Searching for your location...");

        /* getLoc(); is commented out because we dont have GPS location on emulator, so it crashes */
        final StringBuilder latStr = new StringBuilder();
        final StringBuilder lonStr = new StringBuilder();
        getLoc(latStr, lonStr);

        ll.addView(b);

//        sendLocation(latitude, longitude);
        //receive menu.

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* send location and receive a list of stores. then send that list over */
                Intent intent = new Intent(MainActivity.this, PickStoreActivity.class);

                intent.putExtra("latitude", latStr.toString());
                intent.putExtra("longitude", lonStr.toString());

                startActivity(intent);
            }
        });
    }

    public void getLoc(final StringBuilder latStr, final StringBuilder lonStr)
    {
        MyLocation.LocationResult locationResult = new MyLocation.LocationResult(){
            @Override
            public void gotLocation(Location location){
                //Got the location!
                latitude = location.getLatitude();
                longitude = location.getLongitude();

                latStr.append(String.valueOf(latitude));
                lonStr.append(String.valueOf(longitude));
                b.setText("Find a store near your location: (" + latStr + ", " + lonStr + ")");
            }
        };
        MyLocation myLocation = new MyLocation();
        myLocation.getLocation(this, locationResult);

    }

    //function to get GPS location from Andrew.
    public static String getLocation() {
        return null;
    }

    //returns latitude and longitude as a JSON string.
    public static String sendLocation(double lat, double lon) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("latitude", new Double(lat));
            obj.put("longitude", new Double(lon));
            return obj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    //returns storeID as a JSON string.
    public static String sendStore(int storeID) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("storeId", new Integer(storeID));
            return obj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
/*
    //returns the order as a JSON string.
    public static String sendOrder(Order[] orders) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("name", new String("John"));            //name on the order
            JSONArray array = new JSONArray();              //array of items ordered
            JSONObject orderDetails = new JSONObject();     //for each item ordered

            int orderSize = orders.length;
            for (int i = 0; i < orderSize; i++) {
                orderDetails.put("itemId", orders[i].getId());
                orderDetails.put("quantity", orders[i].getQuantity());
                orderDetails.put("comment", orders[i].getComment());
                array.put(orderDetails);
                orderDetails = new JSONObject();    //reset the orderDetail for a new item.
            }
            obj.put("order", array);
*//*
            list.put(listobj.put("itemId", new Integer(1)));
            list.put(listobj.put("quantity", new Integer(5)));
            list.put(listobj.put("comment", new String("no cheese")));
*//*
            return obj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }*/


}
