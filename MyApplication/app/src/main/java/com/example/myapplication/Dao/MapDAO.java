package com.example.myapplication.Dao;

import com.example.myapplication.Dto.Map;
import com.example.myapplication.Dto.MapLine;
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
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MapDAO
{
    /*
        Retrieve all map in the dataBase
     */
    public static void GetAllMap(SelectActivity myActivity, SandboxMenuActivity myActivitySandBox)
    {
        RequestBody formBody = new FormBody.Builder()
                .add("command", "GetAllMap")
                .add("api_key",LoadingActivity.API_KEY)
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
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                String responseStr = Objects.requireNonNull(response.body()).string();

                if (!responseStr.equals("false") && !responseStr.equals(""))
                {
                    try
                    {
                        JSONArray jsonArrayMap = new JSONArray(responseStr);

                        HashMap<Integer, Map> Maps = new HashMap<>();

                        for (int i = 0; i < jsonArrayMap.length(); i++) {

                            JSONObject json = jsonArrayMap.getJSONObject(i);

                            Map myMap = Map.hydrateMap( json );

                            Maps.put( myMap.getIdMap() , myMap );
                        }
                        if (myActivity == null) {
                            myActivitySandBox.responseMap(Maps);
                        } else {
                            myActivity.responseMap(Maps);
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


    /*
        Retrieve get lines og a map
     */
    public static void GetMap(GameActivity myActivity, SandboxActivity mySandBoxActivity, String idMap)
    {
        RequestBody formBody = new FormBody.Builder()
                .add("command", "GetMapLineById")
                .add("idMap", idMap)
                .add("api_key",LoadingActivity.API_KEY)
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
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                String responseStr = Objects.requireNonNull(response.body()).string();

                if (!responseStr.equals("false") && !responseStr.equals(""))
                {
                    try
                    {
                        JSONArray jsonArrayLinesMap = new JSONArray(responseStr);

                        HashMap<Integer, MapLine> LinesMaps = new HashMap<>();

                        for (int i = 0; i < jsonArrayLinesMap.length(); i++)
                        {
                            JSONObject json = jsonArrayLinesMap.getJSONObject(i);

                            MapLine myMapLine = MapLine.hydrateMap(json);

                            LinesMaps.put(myMapLine.getId(),myMapLine);
                        }

                        if (myActivity == null) {
                            mySandBoxActivity.responseMapLine(LinesMaps);
                        } else {
                            myActivity.responseMapLine(LinesMaps);
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
                .add("command", "SaveMap")
                .add("nameMap",myMap.getNom())
                .add("nbRows", String.valueOf( myMap.getNbRows() ) )
                .add("nbColumns", String.valueOf( myMap.getNbColumns() ) )
                .add("idClient", String.valueOf( myMap.getIdClient() ) )
                .add("api_key",LoadingActivity.API_KEY)
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
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                String responseStr = Objects.requireNonNull(response.body()).string();
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

    /*
        Save the mapLines in parameters in the dataBase
     */
    public static void saveMapLines( MapLine myMapLines)
    {
        RequestBody formBody = new FormBody.Builder()
                .add("command", "SaveMapLine")
                .add("indexRow", String.valueOf( myMapLines.getIndexRow() ) )
                .add("content", myMapLines.getContent()  )
                .add("idMap", String.valueOf( myMapLines.getIdMap() ) )
                .add("api_key",LoadingActivity.API_KEY)
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
            public void onResponse(@NotNull Call call, @NotNull Response response) {}
        });
    }

    /*
        Delete the map in parameter
     */

    public static void DeleteMap( int idMap)
    {
        RequestBody formBody = new FormBody.Builder()
                .add("command", "DeleteMap")
                .add("idMap", String.valueOf(idMap))
                .add("api_key",LoadingActivity.API_KEY)
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
            public void onResponse(@NotNull Call call, @NotNull Response response) { }
        });
    }

    /*
        Retrieve all the maps of a client
     */
    public static void getMapByClient(SelectActivity myActivity, SandboxMenuActivity myActivitySandBox)
    {
        RequestBody formBody = new FormBody.Builder()
                .add("command", "GetMapByClient")
                .add("idClient", LoadingActivity.idClient)
                .add("api_key",LoadingActivity.API_KEY)
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
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                String responseStr = Objects.requireNonNull(response.body()).string();

                if (!responseStr.equals("false") && !responseStr.equals(""))
                {
                    try
                    {
                        JSONArray jsonArrayMap = new JSONArray(responseStr);

                        HashMap<Integer, Map> Maps = new HashMap<>();

                        for (int i = 0; i < jsonArrayMap.length(); i++) {

                            JSONObject json = jsonArrayMap.getJSONObject(i);

                            Map myMap = Map.hydrateMap(json);

                            Maps.put( myMap.getIdMap() , myMap );
                        }
                        if (myActivity == null) {
                            myActivitySandBox.responseMap(Maps);
                        } else {
                            myActivity.responseMap(Maps);
                        }
                    }
                    catch(JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                else if(responseStr.equals("")){
                    HashMap<Integer, Map> lesMaps = new HashMap<>();
                    myActivitySandBox.responseMap(lesMaps);
                }
            }
        });
    }

    /*
        Update the map in parameter
     */
    public static void updateMap( SandboxActivity myActivity , Map myMap)
    {
        RequestBody formBody = new FormBody.Builder()
                .add("command", "UpdateMap")
                .add("nameMap",myMap.getNom())
                .add("nbRows", String.valueOf( myMap.getNbRows() ) )
                .add("nbColumns", String.valueOf( myMap.getNbColumns() ) )
                .add("idClient", String.valueOf( myMap.getIdClient() ) )
                .add("isTested",  myMap.getIsTested() ? "1" : "0")
                .add("idMap", String.valueOf(myMap.getIdMap()))
                .add("api_key",LoadingActivity.API_KEY)
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
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                String responseStr = Objects.requireNonNull(response.body()).string();

                if (!responseStr.equals("false") && !responseStr.equals("")) {
                    myActivity.responseAfterUpdateMap();
                }
            }
        });
    }

    /*
    Update the map in parameter
 */
    public static void UpdateIsTestMap( Map myMap )
    {
        RequestBody formBody = new FormBody.Builder()
                .add("command", "UpdateIsTested")
                .add("tested",myMap.getIsTested() ? "1" : "0")
                .add("idMap", String.valueOf(myMap.getIdMap()))
                .add("api_key",LoadingActivity.API_KEY)
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

    /*
        Update the mapLines in parameter
     */
    public static void updateMapLines( MapLine myMapLines )
    {
        RequestBody formBody = new FormBody.Builder()
                .add("command", "UpdateMapLine")
                .add("indexRow", String.valueOf( myMapLines.getIndexRow() ) )
                .add("content", myMapLines.getContent()  )
                .add("idMap", String.valueOf( myMapLines.getIdMap() ) )
                .add("api_key",LoadingActivity.API_KEY)
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
            public void onResponse(@NotNull Call call, @NotNull Response response) {}
        });
    }

}
