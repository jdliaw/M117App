package com.example.brandonliu.menuorderingsystem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.text.DecimalFormat;

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


        //convert arrayList<MenuItem> into arrayList<String>
        ArrayList<String> shoppingCart = getCart(parcelCart);

        //get total quantity
        tax = getSubtotal(parcelCart) * taxRate;
        total = tax + getSubtotal(parcelCart);
        shoppingCart.add("Total: $" + dec.format(total));

        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, shoppingCart);

        final ListView listView = (ListView)findViewById(R.id.shopping_cart);
        //sets adapter and creates the listview
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
}

