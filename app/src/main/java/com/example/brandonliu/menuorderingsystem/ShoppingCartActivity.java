package com.example.brandonliu.menuorderingsystem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.content.Intent;
import java.util.ArrayList;
public class ShoppingCartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        /*
        //send a single string
        Bundle bundle = getIntent().getExtras();
        String sent;
        if(bundle.getString("paramName")!= null) {
            sent = bundle.getString("paramName");
        }
        else
        {
            sent = "did not send";
        }
        Log.d("117", sent);
        */
        /*
        //send a string array
        Intent intent = getIntent();
        String [] myStrings = intent.getStringArrayExtra("paramName");
        Log.d("117-1", myStrings[0]);
        Log.d("117-1", myStrings[1]);
        */

        //arraylist
        /*
        ArrayList<String> cart;
        cart = getIntent().getStringArrayListExtra("paramName");
        Log.d("117", cart.get(0));
        */

        //parcelable
        ArrayList<MenuItem> parcelCart = getIntent().getParcelableArrayListExtra("paramName");
        Log.d("parcelTest", parcelCart.get(0).getCategory());
        Log.d("parcelTestName", parcelCart.get(0).getName());
        Log.d("parcelTestQuan", Integer.toString(parcelCart.get(0).getQuantity()));

    }
}
