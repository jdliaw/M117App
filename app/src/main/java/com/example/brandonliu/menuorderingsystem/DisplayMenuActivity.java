package com.example.brandonliu.menuorderingsystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import android.widget.ExpandableListView;


public class DisplayMenuActivity extends AppCompatActivity {


    ArrayList<String> cart = new ArrayList();
    ArrayList<MenuItem> parcelCart = new ArrayList();

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    private static DecimalFormat dec = new DecimalFormat("#.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_menu);

        //test data
        parcelCart.add(new MenuItem("breakfast", "Eggs", 5));
        parcelCart.get(0).setQuantity(11);
        parcelCart.add(new MenuItem("breakfast", "Bacon", 7));
        parcelCart.get(1).setQuantity(33);
        parcelCart.add(new MenuItem("breakfast", "Waffle", 4));
        parcelCart.get(2).setQuantity(66);
        parcelCart.add(new MenuItem("lunch", "Panini", 6));
        parcelCart.get(3).setQuantity(22);
        parcelCart.add(new MenuItem("lunch", "Sandwich", 3));
        parcelCart.get(4).setQuantity(55);
        parcelCart.add(new MenuItem("dinner", "Potato", 1));
        parcelCart.get(5).setQuantity(77);
        parcelCart.add(new MenuItem("dinner", "Steak", 2));
        parcelCart.get(6).setQuantity(44);

        expListView = (ExpandableListView) findViewById(R.id.expandablelistView);
        //getListData();

        prepareListData(parcelCart);

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        expListView.setAdapter(listAdapter);

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

      /* You must make use of the View v, find the view by id and extract the text as below*/
                /*
                TextView tv = (TextView) v.findViewById(R.id.expandablelist_item);

                String data = tv.getText().toString();
                */


                Intent intent = new Intent(DisplayMenuActivity.this, ShoppingCartActivity.class);
                intent.putParcelableArrayListExtra("paramName", parcelCart);
                startActivity(intent);
                return true;
            }


        });

/*
        cart.add("item1");
        cart.add("item2");



        ScrollView sv = new ScrollView(this);
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        sv.addView(ll);

        //generates a sample menu JSON String.
        String jsonMenu = createMenuArray();

        //given a string JSONobject, (can just use toString), convert into a menu array list
        ArrayList<MenuItem> menu = decodeMenu(jsonMenu);
        //sort by category
        Collections.sort(menu);

        displayMenu(menu, ll);

        parcelCart.add(new MenuItem("breakfast", "20McNugs", 5));
        parcelCart.get(0).setQuantity(20);
        Log.d("parcelTest:", parcelCart.get(0).getName());
        //this.setContentView(sv);
        */
    }

    private void prepareListData(ArrayList<MenuItem> menu) {
        listDataHeader = new ArrayList<String>();                                   //header which is the category
        listDataChild = new HashMap<String, List<String>>();                        //maps string, category, to all of the items of that category
        ArrayList<List<String>> listOfCategories = new ArrayList<List<String>>();   //holds entire menu separated by category. each list<string> are all the items of that specfic category

        int numCategories = 0;          //tracks how many categories
        int menuSize = menu.size();     //for iteration purposes (reduce function calls)
        //gets categories
        for(int i = 0; i < menuSize; i++) {
            if(!listDataHeader.contains(menu.get(i).getCategory())) {               //if category isn't present
                listDataHeader.add(menu.get(i).getCategory());                      //add this category
                listOfCategories.add(new ArrayList<String>());                      //add list of items for that category index
                numCategories++;                                                    //increment number of categories
            }
            //else, continue
        }

        String cat = listDataHeader.get(0);     //first category
        int whichCat = 0;
        //assuming list is sorted
        for(int i = 0; i < menuSize; i++) {
            if(cat != menu.get(i).getCategory()) {
                whichCat++;
                if(whichCat == numCategories)
                    break;
                cat = listDataHeader.get(whichCat);
            }
            String price = "Price: " + dec.format(menu.get(i).getPrice());
            String formatMe = String.format("%s %20s", menu.get(i).getName(), price);
            listOfCategories.get(whichCat).add(formatMe);      //add
        }

        for(int i = 0; i < numCategories; i++) {
            listDataChild.put(listDataHeader.get(i), listOfCategories.get(i));
        }
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

    public static ArrayList<MenuItem> decodeMenu(String menu){
        try {
            JSONObject jsonRootObject = new JSONObject(menu);

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = jsonRootObject.getJSONArray("menu");

            ArrayList<MenuItem> itemArray = new ArrayList<>();

            //Iterate the jsonArray and print the info of JSONObjects
            for(int i=0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                // parse into a Store object
                String category = jsonObject.getString("category");
                String name = jsonObject.getString("name");
                double price = jsonObject.getDouble("price");

                itemArray.add(new MenuItem(category, name, price));
            }
            return itemArray;
        } catch (JSONException e) {e.printStackTrace();}
        return null;
    }

/*
    void displayMenu(ArrayList<MenuItem> itemArray, LinearLayout ll)
    {
        String lastCat = itemArray.get(0).getCategory();
        TextView tv1 = new TextView(this);
        tv1.setText(lastCat);
        ll.addView(tv1);

        // for displaying quantity spinner and add to cart button later onclick
        final TextView qty = (TextView)findViewById(R.id.quantity_label);
        final Spinner qty_spinner = (Spinner)findViewById(R.id.quantity_spinner);
        final Button add_to_cart = (Button)findViewById(R.id.add_to_cart);

        // display stores by shortest distance
        for (int i = 0; i < itemArray.size(); i++)
        {
            String curCat = itemArray.get(i).getCategory();
            if (curCat != lastCat)
            {
                TextView tv = new TextView(this);
                tv.setText(curCat);
                ll.addView(tv);
                lastCat = curCat;
            }
            Button b = new Button(this);
            b.setBackgroundResource(R.drawable.button);
            b.setText(itemArray.get(i).getName() + "      Price: $" + Double.toString(itemArray.get(i).getPrice()));
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
                    *//*
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
                        *//*

                }
            });
        }
    }
           */
}

//display menu:
/*
fragment with a spinner for quantity and add to cart button (and cancel button or something)
this adds to a ArrayList<MenuItem> shoppingCart
we'll send the shoppingCart to shoppingcartactivity

there, we can parse for strings and display the menu

 */