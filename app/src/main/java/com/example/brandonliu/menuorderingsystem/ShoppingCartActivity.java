package com.example.brandonliu.menuorderingsystem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ShoppingCartActivity extends AppCompatActivity {

 //   private ArrayList<String> shoppingCart = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        //ArrayList<String> cart; cart = getIntent().getStringArrayListExtra("paramName");Log.d("117", cart.get(0));

        //parcelable
        ArrayList<MenuItem> parcelCart = getIntent().getParcelableArrayListExtra("paramName");
        Log.d("shoppingCartSize", String.valueOf(parcelCart.size()));
        //convert arrayList<MenuItem> into arrayList<String>
        ArrayList<String> shoppingCart = getCart(parcelCart);

        Log.d("arraylist_string", shoppingCart.get(0));

        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, shoppingCart);

        final ListView listView = (ListView)findViewById(R.id.shopping_cart);
        //sets adapter and creates the listview
        listView.setAdapter(itemsAdapter);



    }

    public static ArrayList<String> getCart(ArrayList<MenuItem> allItems) {
        ArrayList<String> tempCart = new ArrayList<String>();

        int len = allItems.size();
        for(int i = 0; i < len; i++) {
            //create string that gets added to the arraylist of strings
            String temp = allItems.get(i).getName() + "      Price: $" + String.valueOf(allItems.get(i).getPrice()) +
                    "      Quantity: " + allItems.get(i).getQuantity();

            tempCart.add(temp);
        }
        return tempCart;
    }
}

