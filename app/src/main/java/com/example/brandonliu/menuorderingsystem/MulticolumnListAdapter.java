package com.example.brandonliu.menuorderingsystem;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MulticolumnListAdapter extends BaseAdapter{

    public ArrayList<HashMap<String, String>> list;
    Activity activity;
    TextView [] textViews;
    int [] rIds;
    int numCols;
    //need to pass in the number of columns we want as well as which rids (column1, column2, etc very easy)
    public MulticolumnListAdapter(Activity activity, ArrayList<HashMap<String, String>> list, int numCols, int [] rIds) {
        super();
        this.activity=activity;
        this.list = list;
        this.rIds = rIds;
        this.numCols = numCols;
        this.textViews = new TextView[numCols];
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater=activity.getLayoutInflater();

        if(convertView == null){
            //inflate the layout with our textviews.
            convertView=inflater.inflate(R.layout.listview_mult_columns, null);
            //go through each one of our columns and select which textview xml format we use
            for(int i = 0; i < numCols; i++) {
                textViews[i] = (TextView)convertView.findViewById(rIds[i]);
            }
        }

        HashMap<String, String> map=list.get(position);
        //go through each of our textviews and place in our text. the map will be 0, 1, 2 for col
        for(int i = 0; i < numCols; i++) {
            textViews[i].setText(map.get(String.valueOf(i)));
        }
        return convertView;
    }

}
