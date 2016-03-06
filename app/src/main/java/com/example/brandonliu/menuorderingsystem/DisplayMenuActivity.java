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
import android.widget.Toast;


public class DisplayMenuActivity extends AppCompatActivity {


    ArrayList<String> cart = new ArrayList();
    ArrayList<MenuItem> parcelCart = new ArrayList<MenuItem>();
    public static ArrayList<MenuItem> shoppingCart = new ArrayList<MenuItem>();
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

        //parcelCart = intent.getParcelableArrayList("paramName");

        if(getIntent().getParcelableArrayListExtra("paramName") != null) {
            parcelCart = getIntent().getParcelableArrayListExtra("paramName");
            Log.d("test", "sent!");
            Log.d("test", parcelCart.get(0).getName());
        }
        else if(getIntent().getExtras() != null) {
            addedItem = getIntent().getExtras().getParcelable("popup");
            Log.d("test", addedItem.getName() + " YAYYYYYY ");
            Log.d("test", String.valueOf(addedItem.getQuantity()));
            shoppingCart.add(addedItem);
            Log.d("shoppingcart size", String.valueOf(shoppingCart.size()));
        }
        else {
            Log.d("non array list", "didnt send");
        }

        //generating hard coded test data that can easily be gotten by
        parcelCart.add(new MenuItem("Breakfast", "Eggs", 5));
        parcelCart.get(0).setQuantity(11);
        parcelCart.add(new MenuItem("Breakfast", "Bacon", 7));
        parcelCart.get(1).setQuantity(33);
        parcelCart.add(new MenuItem("Breakfast", "Waffle", 4));
        parcelCart.get(2).setQuantity(6);
        parcelCart.add(new MenuItem("Lunch", "Panini", 6));
        parcelCart.get(3).setQuantity(22);
        parcelCart.add(new MenuItem("Lunch", "Sandwich", 3));
        parcelCart.get(4).setQuantity(55);
        parcelCart.add(new MenuItem("Dinner", "Potato", 1));
        parcelCart.get(5).setQuantity(788);
        parcelCart.add(new MenuItem("Dinner", "Steak", 2));
        parcelCart.get(6).setQuantity(44);


        expListView = (ExpandableListView) findViewById(R.id.expandablelistView);


        prepareListData(parcelCart);

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        expListView.setAdapter(listAdapter);

        Button b = (Button)findViewById(R.id.bottombutton);
        b.setText("View Shopping Cart");


        /* button to go to shopping cart */
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DisplayMenuActivity.this, ShoppingCartActivity.class);
                intent.putParcelableArrayListExtra("paramName", shoppingCart);
                startActivity(intent);
            }
        });

        /*
        * Currently this function, on clicking child of the expandable list view, sends us to
        * a new activity (ShoppingCartActivity). What we want in the future is to check the child's
        * position and use this position in accordance with the ArrayList of all items to
        * find which item is clicked (note: headers do count as position I believe). Then we can
        * pass this info into a fragment, select quantity and add to cart.
        * Only when we press the very last button or whichever button is the "Checkout" button
        * do we start a new activity and send our shopping cart array (through parcelable functions)
        * */
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                //go to shopping cart activity class. TODO: change this to fragment.

                //fullString is the entire string that is to be stripped.
                String fullString = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
                //strip of spaces:
                String findMenuItem = stripString(fullString);

                //returns the item that we want.
                MenuItem sendMe = findItem(parcelCart, findMenuItem);
                Log.d("MenuItemSend", sendMe.getName() + " " + sendMe.getPrice());

                Intent intent = new Intent(DisplayMenuActivity.this, Popup.class);
                intent.putExtra("paramName", sendMe);
                intent.putParcelableArrayListExtra("shoppingCart", shoppingCart);
                //our intent now holds the menu item to send across to our fragment;
                startActivity(intent);
                return true;
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
            if(cat != menu.get(i).getCategory()) {
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
                String category = jsonObject.getString("category");
                String name = jsonObject.getString("name");
                double price = jsonObject.getDouble("price");
                //add to array.
                itemArray.add(new MenuItem(category, name, price));
            }
            return itemArray;
        } catch (JSONException e) {e.printStackTrace();}
        return null;
    }

    public static ArrayList<MenuItem> getShoppingCart() {
        return shoppingCart;
    }
}
