package com.example.brandonliu.menuorderingsystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by brandonliu on 3/5/16.
 */
public class Popup extends Activity {
    private EditText item_quantity;
    MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //popup menu layout + drawables + themes etc take care of dimming backround, corners, colors, etc.
        setContentView(R.layout.popup_window);
        menuItem = getIntent().getExtras().getParcelable("paramName");
        //ArrayList<MenuItem> shoppingCart = getIntent().getParcelableArrayListExtra("shoppingCart");


        //Log.d("Test arraylist", String.valueOf(shoppingCart.size()));
        //grab the display metrics for the window
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        //set the width and height to be a fraction
        double width = dm.widthPixels * .8;
        double height = dm.heightPixels * .6;
        getWindow().setLayout((int)(width), (int)(height));

        //grab the relative layout
        RelativeLayout layout_menu = (RelativeLayout) findViewById(R.id.popupMenu);

        //grab the textView for setting the item name, which we can set to the object's name.
        TextView headerText = (TextView) layout_menu.findViewById(R.id.itemName);
        headerText.setText(menuItem.getName());




        Button b = (Button)findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Popup.this, DisplayMenuActivity.class);

                item_quantity = (EditText)findViewById(R.id.setQuan);
                String test = item_quantity.getText().toString();

                if(test.matches("")){
                    Log.d("test", "no input");
                }
                //set quantity.
                else {
                    menuItem.setQuantity(Integer.valueOf(test));
                }
                intent.putExtra("popup", menuItem);
                startActivity(intent);
            }
        });
    }
}
