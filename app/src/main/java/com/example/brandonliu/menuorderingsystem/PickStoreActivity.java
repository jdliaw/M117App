package com.example.brandonliu.menuorderingsystem;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class PickStoreActivity extends AppCompatActivity {
    private ArrayList<HashMap<String, String>> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_store);

        //testing purposes hard coded for now, it's the stores that we'll receive.
        String jsonStores = createStoreArray();
        //convert json object/array into an array of stores
        final Store[] stores = decodeStores(jsonStores);
        //sort stores distance
        Arrays.sort(stores);


        //create an arraylist of strings to be displayed. trivial but too lazy to change for now.
        ArrayList<String> storesString = getStores(stores);

        //get the listView
        final ListView listView = (ListView)findViewById(R.id.storelist);

        list = new ArrayList<HashMap<String,String>>();
        //put in data for multi-column.
        for(int i = 0; i < stores.length; i++) {
            HashMap<String,String> temp=new HashMap<String, String>();
            //put in data per row by column
            temp.put("0", stores[i].getName());
            temp.put("1", String.valueOf(stores[i].getDist()) + "mi");
            list.add(temp);
        }
        //for the columns we want to use
        int[]rIds = {R.id.column1, R.id.column2 };
        MulticolumnListAdapter adapter=new MulticolumnListAdapter(this, list, 2, rIds );
        listView.setAdapter(adapter);

        //adds header
        View header = (View)getLayoutInflater().inflate(R.layout.header, null);
        TextView headerText = (TextView) header.findViewById(R.id.list_header);
        headerText.setText(" Nearby Restaurants");

        //add header to listView. makes non-clickable
        listView.addHeaderView(header, null, false);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
//                String selectedFromList = String.valueOf((TextView) findViewById(R.id.column1));
//                Log.d("col1", selectedFromList);
                Log.d("stores:", stores[position-1].getName());
                startActivity(new Intent(PickStoreActivity.this, DisplayMenuActivity.class));
            }

        });
/*
        //sets onclick listener to start new activity. onItemClick = when we click a particular item
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                String selectedFromList = (String) (listView.getItemAtPosition(myItemInt));
                //Log.d("m117test", selectedFromList);    //test if we get the string we click

                //technically should send the store clicked to

                //move onto displayMenuActivity
                startActivity(new Intent(PickStoreActivity.this, DisplayMenuActivity.class));
            }
        });
        */

        //displayStores(stores, storeList);
//        setContentView(R.layout.activity_pick_store);
    }

    //returns JSON string of stores
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

    //returns an array list of stores used for arrayadapter
    public static ArrayList<String> getStores(Store[] storeArray) {
        int len = storeArray.length;
        ArrayList<String> storeArrayList = new ArrayList<String>();
        //iterates through the length on storeArray and adds to arrayList.
        for(int i = 0; i < len; i++) {
            storeArrayList.add(storeArray[i].getName() + "        " + Double.toString(storeArray[i].getDist()) + "mi");
        }
        return storeArrayList;
    }



    void displayStores(Store[] storeArray, ListView storeList)
    {

        // display stores by shortest distance
        for (int i = 0; i < storeArray.length; i++)
        {
            Button b = new Button(this);
            b.setText(storeArray[i].getName() + "\n Distance: " + Double.toString(storeArray[i].getDist()) + "mi");

            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(PickStoreActivity.this, DisplayMenuActivity.class));
                }
            });
        }
    }

}
