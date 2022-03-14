package com.example.myapplication.Dao;

import com.example.myapplication.Dto.Map;
import com.example.myapplication.MainMenu.LoadingActivity;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HistoryDAO
{

    /*
Update the map in parameter
*/
    public static void NewPlayer( String idClient )
    {
        RequestBody formBody = new FormBody.Builder()
                .add("command", "InsertNewPlayer")
                .add("idClient",idClient)
                .add("api_key", LoadingActivity.API_KEY)
                .build();

        Request request = new Request.Builder()
                .url(LoadingActivity.CONNEXION_API)
                .post(formBody)
                .build();

        OkHttpClient client = new OkHttpClient();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                System.out.println("Error : " + e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response)  { }
        });
    }


}
