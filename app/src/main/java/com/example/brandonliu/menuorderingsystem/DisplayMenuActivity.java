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
import java.util.concurrent.TimeUnit;

import android.widget.ExpandableListView;
import android.widget.Toast;


public class DisplayMenuActivity extends AppCompatActivity {


    ArrayList<String> cart = new ArrayList();
    public static ArrayList<MenuItem> parcelCart = new ArrayList<MenuItem>();
    public static ArrayList<MenuItem> shoppingCart = new ArrayList<MenuItem>();
    public static String storeId;
    MenuItem addedItem;

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    private static DecimalFormat dec = new DecimalFormat("#.00");

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display_menu);

        //grab storeId
        if(getIntent().getStringExtra("selectedStore") != null) {
            storeId = getIntent().getStringExtra("selectedStore");
            Log.d("store id", storeId);
        }
        else {
            Log.d("didn't get", "store id");
            storeId = "";
        }
        //grab added item from menu if applicable
        if(getIntent().getExtras().getParcelable("popup") != null) {
            addedItem = getIntent().getExtras().getParcelable("popup");
            Log.d("Quantity in displayMenu", String.valueOf(addedItem.getQuantity()));
            //only add if there's quantity greater than 0.
            if(addedItem.getQuantity() > 0) {
                if(!itemExists(addedItem, shoppingCart)) {
                    shoppingCart.add(addedItem);
                }
                for(int i = 0; i < shoppingCart.size(); i++) {
                    if(shoppingCart.get(i).getName().equals(addedItem.getName()))
                        shoppingCart.get(i).setQuantity(addedItem.getQuantity());
                }
            }

        }

        //http get request
        String menuRequest = "http://project-order-food.appspot.com/get_menu?storeId=" + storeId;
        String httpMenu = "";
        final HTTPTask getMenuTask = new HTTPTask(); // need to make a new httptask for each request
        try {
            // try the getTask with actual location from gps
            JSONObject httpMenuData = getMenuTask.execute(menuRequest).get(30, TimeUnit.SECONDS);
            httpMenu = httpMenuData.toString();
            Log.d("httpget", httpMenu);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        //decode menu now working.
        parcelCart = decodeMenu(httpMenu);

        //if not zero, we can go ahead and populate explistview
        if(parcelCart.size() != 0) {
            expListView = (ExpandableListView) findViewById(R.id.expandablelistView);
            prepareListData(parcelCart);
            listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
            expListView.setAdapter(listAdapter);

            expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                    //go to shopping cart activity class. TODO: change this to fragment.

                    //fullString is the entire string that is to be stripped.
                    String fullString = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
                    //strip of spaces
                    String findMenuItem = stripString(fullString);

                    //returns the item that we want.
                    MenuItem sendMe = findItem(parcelCart, findMenuItem);
                    Log.d("MenuItemSend", sendMe.getName() + " " + sendMe.getPrice());

                    Intent intent = new Intent(DisplayMenuActivity.this, Popup.class);
                    intent.putExtra("paramName", sendMe);
                    intent.putExtra("storeId", storeId);
                    //our intent now holds the menu item to send across to our fragment;
                    startActivity(intent);
                    return true;
                }


            });
        }


        Button b = (Button)findViewById(R.id.bottombutton);
        b.setText("View Shopping Cart");

        /* button to go to shopping cart */
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DisplayMenuActivity.this, ShoppingCartActivity.class);
                intent.putParcelableArrayListExtra("paramName", shoppingCart);
                intent.putExtra("storeId", storeId);
                startActivity(intent);
            }
        });

    }
    /*
    * This function serves to take and ArrayList of menu items and prepare it into a usable format.
    *  The function loops through the ArrayList to gather all the different categories of items to
    *  be used as expandablelistview headers, and then place each item by category into the
    *  category that it belongs in. It assumes the the ArrayList passed in is sorted by category
    *  in the order the categories will be displayed.
    * */
    private void prepareListData(ArrayList<MenuItem> menu) {
        if(menu.size() == 0) {
            Log.d("Warning", "menu sie is 0");
            return;
        }
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
            //places item by category into the category it belongs in.
            if(!cat.equals(menu.get(i).getCategory())) {
                whichCat++;
                if(whichCat == numCategories)
                    break;
                cat = listDataHeader.get(whichCat);
            }
            //formatting but it doesnt work. need to make multiple columns
            String formatMe = formatCols(menu.get(i).getName(), menu.get(i).getPrice());
            //String price = "Price: " + dec.format(menu.get(i).getPrice());
            //String formatMe = String.format("%s %20s", menu.get(i).getName(), price);
            listOfCategories.get(whichCat).add(formatMe);      //add
        }

        //put the list of categories (as child) by header.
        for(int i = 0; i < numCategories; i++) {
            listDataChild.put(listDataHeader.get(i), listOfCategories.get(i));
        }
    }
    /* this function is a workaround to formatting. we assume monospace and format the first col
    to be 32 characters long
     */
    String formatCols(String name, double price) {
        int i;
        String formatMe = name;
        for(i = name.length(); i < 32; i++) {
            formatMe += " ";
        }
        formatMe += ("$"+dec.format(price));

        return " " + formatMe;
    }

    String stripString(String stripMe) {
        //ex of Stripme with _ equivalent to a space: string Stripme = " Panini          $5.00";
        String ret = "";
        //start at 1 because we offset by 1 space for formatting. we should probably use padding instead.
        for(int j = 1; j < stripMe.length(); j++) {
            //we break when we hit a space.
            if(stripMe.charAt(j) == ' ') {
                break;
            }
            ret += Character.toString(stripMe.charAt(j));
        }
        return ret;
    }

    //given an MenuItem name, search through the entire menu to find a menuItem that has that name.
    //We assume that every item name is unique. this function will fail if we have items of same name
    // even in diff categories.
    MenuItem findItem(ArrayList<MenuItem> itemList, String item) {
        for(int i = 0; i < itemList.size(); i++) {
            if(item.equals(itemList.get(i).getName())) {
                return itemList.get(i);
            }
         }
        return null;
    }

    //this function just generates a menu for testing purposes.
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

    //takes a JSON string and converts into an arrayList of menu items.
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
                int id = jsonObject.getInt("itemId");
                String category = jsonObject.getString("category");
                String name = jsonObject.getString("name");
                double price = jsonObject.getDouble("price");
                //add to array.
                itemArray.add(new MenuItem(id, category, name, price));
            }
            return itemArray;
        } catch (JSONException e) {e.printStackTrace();}
        return null;
    }

    boolean itemExists(MenuItem item, ArrayList<MenuItem> cart) {
        for(int i = 0; i < cart.size(); i++) {
            if (cart.get(i).getName().equals(item.getName())) {
                return true;
            }
        }
        return false;
    }

}
