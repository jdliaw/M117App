package com.example.brandonliu.menuorderingsystem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import java.util.ArrayList;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

public class ShoppingCartActivity extends AppCompatActivity {

 //   private ArrayList<String> shoppingCart = new ArrayList<String>();
    private ArrayList<HashMap<String, String>> list;
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
        final ArrayList<MenuItem> parcelCart = getIntent().getParcelableArrayListExtra("paramName");

        //convert arrayList<MenuItem> into arrayList<String>
        ArrayList<String> shoppingCart = getCart(parcelCart);

        //get total quantity. when I created the listview I forgot to display this.
        tax = getSubtotal(parcelCart) * taxRate;
        total = tax + getSubtotal(parcelCart);
        shoppingCart.add("Checkout:                                 $" + dec.format(total));

        //put into listview
        //ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, shoppingCart);
        final ListView listView = (ListView)findViewById(R.id.shopping_cart);

        list = new ArrayList<HashMap<String,String>>();
        //put in data for multi-column.
        HashMap<String, String> temp=new HashMap<String, String>();
        //put in a header
        temp.put("0", "  Quantity");
        temp.put("1", "Item");
        temp.put("2", "      Total");
        list.add(temp);
        for(int i = 0; i < parcelCart.size(); i++) {
            //put in data per row by column
            temp=new HashMap<String, String>();
            temp.put("0", "  " + String.valueOf(parcelCart.get(i).getQuantity()));
            temp.put("1", parcelCart.get(i).getName());
            double price = parcelCart.get(i).getQuantity() * parcelCart.get(i).getPrice();
            temp.put("2", "      $" + dec.format(price));
            list.add(temp);
        }
        //put in a footer for price.
        temp = new HashMap<String, String>();
        temp.put("0", "  Your Order:");
        temp.put("1", "");
        temp.put("2", "      $" + dec.format(getSubtotal(parcelCart)));
                list.add(temp);


        //for the columns we want to use
        int[]rIds = {R.id.column1, R.id.column2, R.id.column3 };
        MulticolumnListAdapter adapter=new MulticolumnListAdapter(this, list, 3, rIds );

        //create header
        View header = (View)getLayoutInflater().inflate(R.layout.header, null);
        TextView headerText = (TextView) header.findViewById(R.id.list_header);
        headerText.setText(" Your Cart");
        listView.addHeaderView(header, null, false);

        listView.setAdapter(adapter);

        Button b = (Button)findViewById(R.id.bottombutton);
        b.setText("Check Out!");

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //these lines of code work if you wanna send something across. we might package it as a JSON object and send though?
//                Intent intent = new Intent(ShoppingCartActivity.this, ShoppingCartActivity.class);
//                intent.putParcelableArrayListExtra("paramName", parcelCart);
//                startActivity(intent);

                String order = sendOrder(parcelCart);

                String orderPost = "http://project-order-food.appspot.com/send_order";
                final PostTask sendOrderTask = new PostTask(); // need to make a new httptask for each request
                try {
                    // try the getTask with actual location from gps
                    sendOrderTask.execute(orderPost, order).get(30, TimeUnit.SECONDS);
                    Log.d("httppost", order);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                Log.d("Button", "clicked");
                Toast toast = Toast.makeText(getApplication().getBaseContext(), "Order Placed!", Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }

    public static double getSubtotal(ArrayList<MenuItem> allItems) {
        int len = allItems.size();
        double tot = 0;
        //adds the price and quantity of all items.
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
            String quan = String.valueOf(allItems.get(i).getQuantity());
            String name = allItems.get(i).getName();
            String pr = dec.format(price);
            String temp = String.format("%s\t%s\t%s", quan, name, pr);
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
                orderDetails.put("itemId", orders.get(i).getId());
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

