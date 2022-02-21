package com.example.myapplication.Dao;

import android.content.Context;

import com.example.myapplication.Dto.Map;
import com.example.myapplication.LevelSelect.SelectActivity;
import com.example.myapplication.MainMenu.LoadingActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MapDAO
{

    public static void getAllMap(SelectActivity myActivity)
    {
        RequestBody formBody = new FormBody.Builder()
                .add("command", "getAllMap")
                .build();

        Request request = new Request.Builder()
                .url(LoadingActivity.CONNEXION_API)
                .post(formBody)
                .build();

        OkHttpClient client = new OkHttpClient();

        System.out.println("Requete : " + request);

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                System.out.println("Erreur : " + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String responseStr = response.body().string();
                System.out.println("Reponse : " + responseStr);

                if (!responseStr.equals("false") && !responseStr.equals(""))
                {
                    try
                    {
                        JSONArray jsonArrayMap = new JSONArray(responseStr);

                        HashMap<Integer, Map> lesMaps = new HashMap<>();

                        for (int i = 0; i < jsonArrayMap.length(); i++) {

                            JSONObject json = jsonArrayMap.getJSONObject(i);

                            Map maMap = Map.hydrateMap(json);

                            lesMaps.put(maMap.getIdMap(),maMap);
                        }
                        myActivity.responseMap(lesMaps);
                    }
                    catch(JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    // ---------------------------------------------

    public static void getMaxiDClient(Context myActivity)
    {
        RequestBody formBody = new FormBody.Builder()
                .add("command", "getMaxIdClient")
                .build();

        Request request = new Request.Builder()
                .url(LoadingActivity.CONNEXION_API)
                .post(formBody)
                .build();

        OkHttpClient client = new OkHttpClient();

        System.out.println("Requete : " + request);

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                System.out.println("Erreur : " + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String responseStr = response.body().string();
                System.out.println("Reponse : " + responseStr);

                if (!responseStr.equals("false") && !responseStr.equals(""))
                {
                    try
                    {
                        JSONObject jsonIdClient= new JSONObject();
                        int idCli = jsonIdClient.getInt("idClient");
                        LoadingActivity.setlastIdClient(idCli);

                    }
                    catch(JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

}
