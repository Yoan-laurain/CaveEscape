package com.example.myapplication.Lib;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Async extends AsyncTask<String, String, String>
{
    @Override
    protected String doInBackground(String... uri)
    {
        URL url;
        StringBuilder response = new StringBuilder();
        try
        {
            url = new URL(uri[0]);
            HttpURLConnection httpconn = (HttpURLConnection) url.openConnection();
            if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                BufferedReader input = new BufferedReader(new InputStreamReader(httpconn.getInputStream()), 8192);
                String strLine;
                while ((strLine = input.readLine()) != null)
                {
                    response.append(strLine);
                }
                input.close();
            }
        }
        catch (java.io.IOException e)
        {
            e.printStackTrace();
        }
        return response.toString();
    }

    @Override
    protected void onPostExecute(String result)
    {
        super.onPostExecute(result);
    }
}
