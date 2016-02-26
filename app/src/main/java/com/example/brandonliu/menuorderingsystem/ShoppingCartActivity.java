package com.example.brandonliu.menuorderingsystem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View;

import java.util.ArrayList;
import java.text.DecimalFormat;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

public class ShoppingCartActivity extends AppCompatActivity {

 //   private ArrayList<String> shoppingCart = new ArrayList<String>();
    private double total;
    private double tax;
    private final static double taxRate = .08; //will be gotten based off location
    private static DecimalFormat dec = new DecimalFormat("#.00");
    /*
    Double number = Double.valueOf(text);

    DecimalFormat dec = new DecimalFormat("#.00 EUR");
    String credits = dec.format(number);
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        //get Array List of menu items
        ArrayList<MenuItem> parcelCart = getIntent().getParcelableArrayListExtra("paramName");
        Log.d("shoppingCartSize", String.valueOf(parcelCart.size()));
        //ArrayList<MenuItem> parcelCart = new ArrayList<MenuItem>();
        //parcelCart.add(new MenuItem("bla", "bla", 5));

        //convert arrayList<MenuItem> into arrayList<String>
        ArrayList<String> shoppingCart = getCart(parcelCart);

        //get total quantity
        tax = getSubtotal(parcelCart) * taxRate;
        total = tax + getSubtotal(parcelCart);
        shoppingCart.add("Checkout:                                 $" + dec.format(total));

        //put into listview
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, shoppingCart);
        final ListView listView = (ListView)findViewById(R.id.shopping_cart);
        View header = (View)getLayoutInflater().inflate(R.layout.header, null);
        TextView headerText = (TextView) header.findViewById(R.id.list_header);
        headerText.setText("HEADER TEST!");

        listView.addHeaderView(header, null, false);

        listView.setAdapter(itemsAdapter);
    }

    public static double getSubtotal(ArrayList<MenuItem> allItems) {
        int len = allItems.size();
        double tot = 0;
        for(int i = 0; i < len; i++) {
            tot += (allItems.get(i).getPrice() * allItems.get(i).getQuantity());
        }
        return tot;
    }

    public static ArrayList<String> getCart(ArrayList<MenuItem> allItems) {
        ArrayList<String> tempCart = new ArrayList<String>();

        int len = allItems.size();
        for(int i = 0; i < len; i++) {
            //create string that gets added to the arraylist of strings
            double price = allItems.get(i).getQuantity() * allItems.get(i).getPrice();
            String temp = allItems.get(i).getPrice() + "         " +
                    allItems.get(i).getName() + "          $" +
                    dec.format(price);
            Log.d("print tempCart", temp);
            tempCart.add(temp);
        }
        return tempCart;
    }

    //returns the order as a JSON string.
    public static String sendOrder(ArrayList<MenuItem> orders) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("name", new String("John"));            //name on the order
            JSONArray array = new JSONArray();              //array of items ordered
            JSONObject orderDetails = new JSONObject();     //for each item ordered

            int orderSize = orders.size();
            for (int i = 0; i < orderSize; i++) {
                //add order details
                orderDetails.put("itemId", orders.get(i).getName());
                orderDetails.put("quantity", orders.get(i).getQuantity());
                //put order details into this array
                array.put(orderDetails);
                orderDetails = new JSONObject();    //reset the orderDetail for a new item.
            }
            obj.put("order", array);

            return obj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}

