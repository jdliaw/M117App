package com.example.brandonliu.menuorderingsystem;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.view.View;
import android.widget.ScrollView;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class PickStoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_store);

        ScrollView sv = new ScrollView(this);
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        sv.addView(ll);

        String jsonStores = createStoreArray();
        Store[] stores = decodeStores(jsonStores);
        Arrays.sort(stores);

        for (int i = 0; i < stores.length; i++)
        {
            double dist = stores[i].getDist();
        }
        displayStores(stores, ll);

        this.setContentView(sv);
    }

    String createStoreArray() {
        // to test decodeStores
        JSONObject store1 = new JSONObject();
        try {
            store1.put("storeID", 1);
            store1.put("storeName", "McDonald's");
            store1.put("distance", 6.7);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        JSONObject store2 = new JSONObject();
        try {
            store2.put("storeID", 2);
            store2.put("storeName", "Taco Bell");
            store2.put("distance", 2.4);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        JSONArray storesJSONArray = new JSONArray();
        storesJSONArray.put(store1);
        storesJSONArray.put(store2);

        JSONObject stores = new JSONObject();
        try {
            stores.put("stores", storesJSONArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String jstr = stores.toString();
        return jstr;
    }

    Store[] decodeStores(String stores){
        try {
            JSONObject jsonRootObject = new JSONObject(stores);

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = jsonRootObject.getJSONArray("stores");
            Store[] storeArray = new Store[jsonArray.length()];

            //Iterate the jsonArray and print the info of JSONObjects
            for(int i=0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                // parse into a Store object
                int storeId = jsonObject.getInt("storeID");
                String storeName = jsonObject.getString("storeName");
                double distance = jsonObject.getDouble("distance");

                storeArray[i] = new Store(storeId, storeName, distance);
            }
            return storeArray;
        } catch (JSONException e) {e.printStackTrace();}
        return null;
    }

    void displayStores(Store[] storeArray, LinearLayout ll)
    {
        // display stores by shortest distance
        for (int i = 0; i < storeArray.length; i++)
        {
            Button b = new Button(this);
            b.setText(storeArray[i].getName() + "\n Distance: " + Double.toString(storeArray[i].getDist()) + "mi");
            ll.addView(b);

            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(PickStoreActivity.this, DisplayMenuActivity.class));
                }
            });
        }
    }

}
