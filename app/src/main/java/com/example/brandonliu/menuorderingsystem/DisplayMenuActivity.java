package com.example.brandonliu.menuorderingsystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.ArrayList;

public class DisplayMenuActivity extends AppCompatActivity {


    ArrayList<String> cart = new ArrayList();
    ArrayList<MenuItem> parcelCart = new ArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_menu);

        cart.add("item1");
        cart.add("item2");

        ScrollView sv = new ScrollView(this);
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        sv.addView(ll);

        String jsonMenu = createMenuArray();
        MenuItem[] menu = decodeMenu(jsonMenu);
        Arrays.sort(menu);
        displayMenu(menu, ll);

        parcelCart.add(new MenuItem("breakfast", "20McNugs", 5));
        parcelCart.get(0).setQuantity(20);
        Log.d("parcelTest:", parcelCart.get(0).getName());
        this.setContentView(sv);
    }

    String createMenuArray() {
//        {
//            "menu":[
//            {"category": "breakfast", "name": "eggs", "price": 1.50},
//            {"category": "drink", "name": "soda", "price": 1.00}
//            ]
//        }
        JSONObject item1 = new JSONObject();
        try {
            item1.put("category", "Breakfast");
            item1.put("name", "Eggs");
            item1.put("price", 1.50);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        JSONObject item2 = new JSONObject();
        try {
            item2.put("category", "Drink");
            item2.put("name", "Soda");
            item2.put("price", 1.00);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        JSONObject item3 = new JSONObject();
        try {
            item3.put("category", "Drink");
            item3.put("name", "Sprite");
            item3.put("price", 1.00);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        JSONArray menuJSONArray = new JSONArray();
        menuJSONArray.put(item1);
        menuJSONArray.put(item2);
        menuJSONArray.put(item3);

        JSONObject menuObj = new JSONObject();
        try {
            menuObj.put("menu", menuJSONArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String jstr = menuObj.toString();
        return jstr;
    }

    MenuItem[] decodeMenu(String menu){
        try {
            JSONObject jsonRootObject = new JSONObject(menu);

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = jsonRootObject.getJSONArray("menu");

            MenuItem[] itemArray = new MenuItem[jsonArray.length()];

            //Iterate the jsonArray and print the info of JSONObjects
            for(int i=0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                // parse into a Store object
                String category = jsonObject.getString("category");
                String name = jsonObject.getString("name");
                double price = jsonObject.getDouble("price");

                itemArray[i] = new MenuItem(category, name, price);
            }
            return itemArray;
        } catch (JSONException e) {e.printStackTrace();}
        return null;
    }


    void displayMenu(MenuItem[] itemArray, LinearLayout ll)
    {
        String lastCat = itemArray[0].getCategory();
        TextView tv1 = new TextView(this);
        tv1.setText(lastCat);
        ll.addView(tv1);

        // for displaying quantity spinner and add to cart button later onclick
        final TextView qty = (TextView)findViewById(R.id.quantity_label);
        final Spinner qty_spinner = (Spinner)findViewById(R.id.quantity_spinner);
        final Button add_to_cart = (Button)findViewById(R.id.add_to_cart);

        // display stores by shortest distance
        for (int i = 0; i < itemArray.length; i++)
        {
            String curCat = itemArray[i].getCategory();
            if (curCat != lastCat)
            {
                TextView tv = new TextView(this);
                tv.setText(curCat);
                ll.addView(tv);
                lastCat = curCat;
            }
            Button b = new Button(this);
            b.setBackgroundResource(R.drawable.button);
            b.setText(itemArray[i].getName() + "      Price: $" + Double.toString(itemArray[i].getPrice()));
            ll.addView(b);

            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String tag = "paramName";
                    Intent intent = new Intent(DisplayMenuActivity.this, ShoppingCartActivity.class);
                    //String [] sendMe  = new String[] {"test1", "test2"};
                    //String sendMe = "test";

                    //intent.putExtra(tag, sendMe);
                    //intent.putStringArrayListExtra(tag, cart);

                    intent.putParcelableArrayListExtra(tag, parcelCart);

                    startActivity(intent);
                    /*
                    // make quantity label appear
                    if(qty.getVisibility() == View.GONE)
                        qty.setVisibility(View.VISIBLE);
                    else
                        qty.setVisibility(View.GONE);
                    // make quantity spinner appear
                    if(qty_spinner.getVisibility() == View.GONE)
                        qty_spinner.setVisibility(View.VISIBLE);
                    else
                        qty_spinner.setVisibility(View.GONE);
                    // make add to cart button appear
                    if(add_to_cart.getVisibility() == View.GONE)
                        add_to_cart.setVisibility(View.VISIBLE);
                    else
                        add_to_cart.setVisibility(View.GONE);
                        */

                }
            });
        }
    }
}

//display menu:
/*
fragment with a spinner for quantity and add to cart button (and cancel button or something)
this adds to a ArrayList<MenuItem> shoppingCart
we'll send the shoppingCart to shoppingcartactivity

there, we can parse for strings and display the menu

 */