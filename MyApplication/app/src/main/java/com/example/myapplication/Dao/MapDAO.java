package com.example.myapplication.Dao;

import android.content.Context;
import com.example.myapplication.Dto.Map;
import com.example.myapplication.Dto.MapLigne;
import com.example.myapplication.Game.GameActivity;
import com.example.myapplication.LevelSelect.SelectActivity;
import com.example.myapplication.MainMenu.LoadingActivity;
import com.example.myapplication.Sandbox.SandboxActivity;
import com.example.myapplication.Sandbox.SandboxMenuActivity;
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

    public static void getAllMap(SelectActivity myActivity, SandboxMenuActivity myActivitySandBox)
    {
        RequestBody formBody = new FormBody.Builder()
                .add("command", "getAllMap")
                .build();

        Request request = new Request.Builder()
                .url(LoadingActivity.CONNEXION_API)
                .post(formBody)
                .build();

        OkHttpClient client = new OkHttpClient();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                System.out.println("Erreur : " + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String responseStr = response.body().string();

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
                        if (myActivity == null) {
                            myActivitySandBox.responseMap(lesMaps);
                        } else {
                            myActivity.responseMap(lesMaps);
                        }

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

    /*public static void getMaxiDClient(Context myActivity)
    {
        RequestBody formBody = new FormBody.Builder()
                .add("command", "getMaxIdClient")
                .build();

        Request request = new Request.Builder()
                .url(LoadingActivity.CONNEXION_API)
                .post(formBody)
                .build();

        OkHttpClient client = new OkHttpClient();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                System.out.println("Erreur : " + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String responseStr = response.body().string();

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
*/
    // ---------------------------------------------
    public static void getMap(GameActivity myActivity, SandboxActivity mySandBoxActivity, String idMap)
    {
        RequestBody formBody = new FormBody.Builder()
                .add("command", "getMapLigneById")
                .add("idMap", idMap)
                .build();

        Request request = new Request.Builder()
                .url(LoadingActivity.CONNEXION_API)
                .post(formBody)
                .build();

        OkHttpClient client = new OkHttpClient();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                System.out.println("Erreur : " + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String responseStr = response.body().string();

                if (!responseStr.equals("false") && !responseStr.equals(""))
                {
                    try
                    {
                        JSONArray jsonArrayLigneMap = new JSONArray(responseStr);

                        HashMap<Integer, MapLigne> lesLignesMaps = new HashMap<>();

                        for (int i = 0; i < jsonArrayLigneMap.length(); i++)
                        {
                            JSONObject json = jsonArrayLigneMap.getJSONObject(i);

                            MapLigne maMapLigne = MapLigne.hydrateMap(json);

                            lesLignesMaps.put(maMapLigne.getId(),maMapLigne);
                        }

                        if (myActivity == null) {
                            mySandBoxActivity.responseMapLigne(lesLignesMaps);
                        } else {
                            myActivity.responseMapLigne(lesLignesMaps);
                        }


                    }
                    catch(JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static void saveMap( SandboxActivity myActivity , Map myMap)
    {
        RequestBody formBody = new FormBody.Builder()
                .add("command", "saveMap")
                .add("nameMap",myMap.getNom())
                .add("nbRows", String.valueOf( myMap.getNbRows() ) )
                .add("nbColumns", String.valueOf( myMap.getNbColumns() ) )
                .add("idClient", String.valueOf( myMap.getIdClient() ) )
                .build();

        Request request = new Request.Builder()
                .url(LoadingActivity.CONNEXION_API)
                .post(formBody)
                .build();

        OkHttpClient client = new OkHttpClient();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                System.out.println("Erreur : " + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String responseStr = response.body().string();
                responseStr = responseStr.replace("true","");
                responseStr = responseStr.replace("false","");

                if (!responseStr.equals("false") && !responseStr.equals(""))
                {
                    try {
                        JSONObject json = new JSONObject(responseStr);

                        myActivity.responseAfterSaveMap( json.getInt("id") );

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static void saveMapLines( SandboxActivity myActivity , MapLigne myMapLines)
    {
        RequestBody formBody = new FormBody.Builder()
                .add("command", "saveMapLine")
                .add("indexRow", String.valueOf( myMapLines.getIndexRow() ) )
                .add("content", myMapLines.getContent()  )
                .add("idMap", String.valueOf( myMapLines.getIdMap() ) )
                .build();

        Request request = new Request.Builder()
                .url(LoadingActivity.CONNEXION_API)
                .post(formBody)
                .build();

        OkHttpClient client = new OkHttpClient();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                System.out.println("Erreur : " + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String responseStr = response.body().string();

                if (!responseStr.equals("false") && !responseStr.equals(""))
                {
                }
            }
        });
    }


    public static void DeleteMap( SandboxActivity myActivity , int idMap)
    {
        String result;
        RequestBody formBody = new FormBody.Builder()
                .add("command", "deleteMap")
                .add("idMap", String.valueOf(idMap))
                .build();

        Request request = new Request.Builder()
                .url(LoadingActivity.CONNEXION_API)
                .post(formBody)
                .build();

        OkHttpClient client = new OkHttpClient();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                System.out.println("Erreur : " + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String responseStr = response.body().string();

                if (responseStr.equals("false") && !responseStr.equals(""))
                {
                    resultReturn(false);
                }
                else{
                    resultReturn(true);
                }
            }

            public boolean resultReturn(Boolean result){
                return result;
            }
        });
    }


    public static void getMapByClient(SelectActivity myActivity, SandboxMenuActivity myActivitySandBox)
    {
        RequestBody formBody = new FormBody.Builder()
                .add("command", "getMapByClient")
                .add("idClient", LoadingActivity.idClient)
                .build();

        Request request = new Request.Builder()
                .url(LoadingActivity.CONNEXION_API)
                .post(formBody)
                .build();

        OkHttpClient client = new OkHttpClient();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                System.out.println("Erreur : " + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String responseStr = response.body().string();

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
                        if (myActivity == null) {
                            myActivitySandBox.responseMap(lesMaps);
                        } else {
                            myActivity.responseMap(lesMaps);
                        }

                    }
                    catch(JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                else if(responseStr == ""){
                    HashMap<Integer, Map> lesMaps = new HashMap<>();
                    myActivitySandBox.responseMap(lesMaps);
                }
            }
        });
    }

}
