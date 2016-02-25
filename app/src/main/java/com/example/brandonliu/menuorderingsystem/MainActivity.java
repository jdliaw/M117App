package com.example.brandonliu.menuorderingsystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //testing stuff
        Location loc = new Location(2, 4);
        Log.d("117", sendLocation(loc));
        Log.d("117", sendStore(2));
        Order[] orders = new Order[2];
        orders[0] = new Order(5, 10, "no cheese");
        orders[1] = new Order(2, 1, "");
        //Log.d("117", sendOrder(orders));


        ScrollView sv = new ScrollView(this);
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        sv.addView(ll);
        this.setContentView(sv);



        /*
        int lat = getLatitude();
        int lon = getLongitude();

        */
        //button

        b = new Button(this);
        //Button b = (Button)findViewById(R.id.button);
        String latStr = Integer.toString(loc.getLatitude());
        String lonStr = Integer.toString(loc.getLongitude());
        b.setText("Find a store near your location: (" + latStr + ", " + lonStr + ")");
        ll.addView(b);




        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PickStoreActivity.class));
            }
        });



    }

    //function to get GPS location from Andrew.
    public static String getLocation() {
        return null;
    }

    //returns latitude and longitude as a JSON string.
    public static String sendLocation(Location loc) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("latitude", new Double(loc.getLatitude()));
            obj.put("longitude", new Double(loc.getLongitude()));
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
