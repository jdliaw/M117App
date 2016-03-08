package com.example.brandonliu.menuorderingsystem;

import android.os.AsyncTask;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

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

/**
 * Created by Jennifer on 3/8/16.
 */
public class PostTask extends AsyncTask<String, String, JSONObject>
{
    HttpURLConnection conn = null;
    URL url = null;
    JSONObject object = null;
    private OutputStream out;

    @Override
    protected JSONObject doInBackground(String... args) {
        try {
            url = new URL(args[0]);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            if (args[1] != null) {
                JSONObject jsonObject = new JSONObject(args[1]);
                out = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
                writer.write(jsonObject.toString());
                writer.flush();
                writer.close();
                out.close();
            }

            conn.connect();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null)
                conn.disconnect();
        }
        return null;
    }
}
