package com.example.myapplication.Dao;

import com.example.myapplication.Sandbox.SandboxActivity;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.Objects;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TextModeration
{

    public static void ScanText(SandboxActivity myActivity,String myText)
    {
        RequestBody formBody = new FormBody.Builder()
                .add("text", myText)
                .add("lang", "fr")
                .add("opt_countries","us,gb,fr")
                .add("mode","standard")
                .add("api_user","1461132288")
                .add("api_secret","X9bMLZ62wkaKULL2ZPRD")
                .build();

        Request request = new Request.Builder()
                .url("https://api.sightengine.com/1.0/text/check.json")
                .post(formBody)
                .build();

        OkHttpClient client = new OkHttpClient();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                System.out.println("Error : " + e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                String responseStr = Objects.requireNonNull(response.body()).string();

                if (!responseStr.equals("false") && !responseStr.equals(""))
                {
                    try
                    {
                        JSONObject json = new JSONObject(responseStr);

                        JSONArray jsonArray = json.getJSONObject("profanity").getJSONArray("matches");

                        if ( jsonArray.length() != 0 )
                        {
                            myActivity.ResponseTextModeration(false);
                        }

                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
