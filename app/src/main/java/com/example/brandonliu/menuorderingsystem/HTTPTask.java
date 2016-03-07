package com.example.brandonliu.menuorderingsystem;

/**
 * Created by Jennifer on 2/28/16.
 */


import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.InputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;



public class HTTPTask extends AsyncTask<String, String, JSONObject>
{
    HttpURLConnection urlConnection = null;
    URL url = null;
    JSONObject object = null;
    InputStream inStream = null;

    @Override
    protected JSONObject doInBackground(String... args) {
        try {
            url = new URL(args[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            //urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.connect();
            inStream = urlConnection.getInputStream();

            BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));
            String temp, response = "";

            while ((temp = bReader.readLine()) != null) {
                response += temp;
            }
            object = (JSONObject) new JSONTokener(response).nextValue();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inStream != null) {
                try {
                    // this will close the bReader as well
                    inStream.close();
                } catch (IOException ignored) {
                }
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return object;
    }

//    HttpURLConnection urlConnection = null;
//
//    @Override
//    protected String doInBackground(String... args) {
//        StringBuilder result = new StringBuilder();
//
//        try {
//            URL url = new URL("http://project-order-food.appspot.com/get_stores?latitude=34.0722&longitude=-118.4441");
//            urlConnection = (HttpURLConnection) url.openConnection();
//            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
//
//            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//
//            String line;
//            while ((line = reader.readLine()) != null) {
//                result.append(line);
//                Log.d("httpget", line);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            urlConnection.disconnect();
//        }
//        return result.toString();
//    }
//
//    @Override
//    protected void onPostExecute(String result) {
//
//        //Do something with the JSON string
//        Log.d("onpostexecute", result);
//
//    }
}
