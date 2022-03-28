package com.CaveEscape.myapplication.Dao;

import com.CaveEscape.myapplication.Dto.Map;
import com.CaveEscape.myapplication.Dto.MapLine;
import com.CaveEscape.myapplication.Game.GameActivity;
import com.CaveEscape.myapplication.LevelSelect.SelectActivity;
import com.CaveEscape.myapplication.Lib.Navigation;
import com.CaveEscape.myapplication.MainMenu.LoadingActivity;
import com.CaveEscape.myapplication.Sandbox.SandboxActivity;
import com.CaveEscape.myapplication.Sandbox.SandboxMenuActivity;
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
    private final static HashMap params = new HashMap();

    /*
    Retrieve get lines og a map
    */
    public static void GetAllMap(SandboxMenuActivity myActivity)
    {
        RequestBody formBody = new FormBody.Builder()
                .add("command", "GetAllMap")
                .add("api_key",LoadingActivity.API_KEY)
                .add("idClient",LoadingActivity.idClient)
                .build();

        Request request = new Request.Builder()
                .url(LoadingActivity.CONNEXION_API)
                .post(formBody)
                .build();

        OkHttpClient client = new OkHttpClient();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                params.put("NetworkFailure","true");
                Navigation.switchActivities(myActivity,LoadingActivity.class,params);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                String responseStr = Objects.requireNonNull(response.body()).string();

                if ( !responseStr.equals("") )
                {
                    if (!responseStr.equals("false"))
                    {
                        try
                        {
                            JSONArray jsonArrayMap = new JSONArray(responseStr);

                            HashMap<Integer, Map> Maps = new HashMap<>();

                            for (int i = 0; i < jsonArrayMap.length(); i++) {

                                JSONObject json = jsonArrayMap.getJSONObject(i);

                                Map myMap = Map.HydrateMap(json);

                                Maps.put( myMap.getIdMap() , myMap );
                            }

                            myActivity.ResponseMap(Maps);
                        }
                        catch(JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        params.put("NetworkFailure","true");
                        Navigation.switchActivities(myActivity,LoadingActivity.class,params);
                    }
                }
                else
                {
                    HashMap<Integer, Map> Maps = new HashMap<>();
                    myActivity.ResponseMap(Maps);
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
                params.put("NetworkFailure","true");
                if ( myActivity == null)
                {
                    Navigation.switchActivities(mySandBoxActivity,LoadingActivity.class,params);

                }
                else
                {
                    Navigation.switchActivities(myActivity,LoadingActivity.class,params);
                }
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                String responseStr = Objects.requireNonNull(response.body()).string();

                if ( !responseStr.equals(""))
                {
                    if ( !responseStr.equals("false") )
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
                                mySandBoxActivity.ResponseMapLine(LinesMaps);
                            } else {
                                myActivity.ResponseMapLine(LinesMaps);
                            }
                        }
                        catch(JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        params.put("NetworkFailure","true");
                        Navigation.switchActivities(myActivity,LoadingActivity.class,params);
                    }
                }
                else
                {
                    Navigation.switchActivities(myActivity,LoadingActivity.class,params);
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
                params.put("NetworkFailure","true");
                Navigation.switchActivities(myActivity,LoadingActivity.class,params);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                String responseStr = Objects.requireNonNull(response.body()).string();
                responseStr = responseStr.replace("true","");
                responseStr = responseStr.replace("false","");

                if (  !responseStr.equals("") )
                {
                    if ( !responseStr.equals("false") )
                    {
                        try {
                            JSONObject json = new JSONObject(responseStr);

                            myActivity.ResponseAfterSaveMap( json.getInt("id") );

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else
                {
                    Navigation.switchActivities(myActivity,LoadingActivity.class,params);
                }
            }
        });
    }

    /*
        Save the mapLines in parameters in the dataBase
     */
    public static void saveMapLines( SandboxActivity myActivity , MapLine myMapLines)
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
                params.put("NetworkFailure","true");
                Navigation.switchActivities(myActivity,LoadingActivity.class,params);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                String responseStr = Objects.requireNonNull(response.body()).string();

                if ( responseStr.equals("false") )
                {
                    params.put("NetworkFailure","true");
                    Navigation.switchActivities(myActivity,LoadingActivity.class,params);
                }
            }
        });
    }

    /*
        Delete the map in parameter
     */

    public static void DeleteMap( SandboxActivity myActivity , int idMap)
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
                params.put("NetworkFailure","true");
                Navigation.switchActivities(myActivity,LoadingActivity.class,params);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                String responseStr = Objects.requireNonNull(response.body()).string();

                if ( responseStr.equals("false") )
                {
                    params.put("NetworkFailure","true");
                    Navigation.switchActivities(myActivity,LoadingActivity.class,params);
                }
            }
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
                params.put("NetworkFailure","true");
                Navigation.switchActivities(myActivity,LoadingActivity.class,params);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                String responseStr = Objects.requireNonNull(response.body()).string();

                if ( !responseStr.equals("") )
                {
                    if ( !responseStr.equals("false") )
                    {
                        try
                        {
                            JSONArray jsonArrayMap = new JSONArray(responseStr);

                            HashMap<Integer, Map> Maps = new HashMap<>();

                            for (int i = 0; i < jsonArrayMap.length(); i++) {

                                JSONObject json = jsonArrayMap.getJSONObject(i);

                                Map myMap = Map.HydrateMap(json);

                                Maps.put( myMap.getIdMap() , myMap );
                            }
                            if (myActivity == null) {
                                myActivitySandBox.ResponseMap(Maps);
                            } else {
                                myActivity.ResponseMap(Maps);
                            }
                        }
                        catch(JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        params.put("NetworkFailure","true");
                        Navigation.switchActivities(myActivity,LoadingActivity.class,params);
                    }
                }
                else
                {
                    HashMap<Integer, Map> lesMaps = new HashMap<>();
                    myActivitySandBox.ResponseMap(lesMaps);
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
                params.put("NetworkFailure","true");
                Navigation.switchActivities(myActivity,LoadingActivity.class,params);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                String responseStr = Objects.requireNonNull(response.body()).string();

                if ( !responseStr.equals(""))
                {
                    if ( !responseStr.equals("false") )
                    {
                        myActivity.responseAfterUpdateMap();
                    }
                    else
                    {
                        params.put("NetworkFailure","true");
                        Navigation.switchActivities(myActivity,LoadingActivity.class,params);
                    }
                }
            }
        });
    }

    /*
    Update the map in parameter
 */
    public static void UpdateIsTestMap( GameActivity myActivity,SandboxActivity myActivity2,Map myMap )
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

                params.put("NetworkFailure","true");
                if ( myActivity == null)
                {
                    Navigation.switchActivities(myActivity2,LoadingActivity.class,params);

                }else
                {
                    Navigation.switchActivities(myActivity,LoadingActivity.class,params);
                }

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                String responseStr = Objects.requireNonNull(response.body()).string();

                if ( responseStr.equals("false") )
                {
                    params.put("NetworkFailure","true");
                    Navigation.switchActivities(myActivity,LoadingActivity.class,params);
                }

            }
        });
    }

    /*
        Update the mapLines in parameter
     */
    public static void updateMapLines( SandboxActivity myActivity, MapLine myMapLines )
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
                params.put("NetworkFailure","true");
                Navigation.switchActivities(myActivity,LoadingActivity.class,params);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseStr = Objects.requireNonNull(response.body()).string();

                if ( responseStr.equals("false") )
                {
                    params.put("NetworkFailure","true");
                    Navigation.switchActivities(myActivity,LoadingActivity.class,params);
                }
            }
        });
    }

    public static void GetHistoryMap(SelectActivity myActivity, SandboxMenuActivity myActivitySandBox)
    {
        RequestBody formBody = new FormBody.Builder()
                .add("command", "GetHistoryMap")
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
                params.put("NetworkFailure","true");
                Navigation.switchActivities(myActivity,LoadingActivity.class,params);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                String responseStr = Objects.requireNonNull(response.body()).string();

                if ( !responseStr.equals("") )
                {
                    if ( !responseStr.equals("false") )
                    {
                        try
                        {
                            JSONArray jsonArrayMap = new JSONArray(responseStr);

                            HashMap<Integer, Map> Maps = new HashMap<>();

                            for (int i = 0; i < jsonArrayMap.length(); i++) {

                                JSONObject json = jsonArrayMap.getJSONObject(i);

                                Map myMap = Map.HydrateMap( json );

                                Maps.put( myMap.getIdMap() , myMap );
                            }
                            if (myActivity == null) {
                                myActivitySandBox.ResponseMap(Maps);
                            } else {
                                myActivity.ResponseMap(Maps);
                            }
                        }
                        catch(JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        params.put("NetworkFailure","true");
                        Navigation.switchActivities(myActivity,LoadingActivity.class,params);
                    }
                }
                else
                {
                    HashMap<Integer, Map> Maps = new HashMap<>();
                    if (myActivity == null) {
                        myActivitySandBox.ResponseMap(Maps);
                    } else {
                        myActivity.ResponseMap(Maps);
                    }
                }
            }
        });
    }

    public static void GetCommunityMap(SelectActivity myActivity, SandboxMenuActivity myActivitySandBox)
    {
        RequestBody formBody = new FormBody.Builder()
                .add("command", "GetCommunityMap")
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
                params.put("NetworkFailure","true");
                Navigation.switchActivities(myActivity,LoadingActivity.class,params);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                String responseStr = Objects.requireNonNull(response.body()).string();

                if ( !responseStr.equals(""))
                {
                    if ( !responseStr.equals("false") )
                    {
                        try
                        {
                            JSONArray jsonArrayMap = new JSONArray(responseStr);

                            HashMap<Integer, Map> Maps = new HashMap<>();

                            for (int i = 0; i < jsonArrayMap.length(); i++) {

                                JSONObject json = jsonArrayMap.getJSONObject(i);

                                Map myMap = Map.HydrateMap( json );

                                Maps.put( myMap.getIdMap() , myMap );
                            }
                            if (myActivity == null) {
                                myActivitySandBox.ResponseMap(Maps);
                            } else {
                                myActivity.ResponseMap(Maps);
                            }
                        }
                        catch(JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        params.put("NetworkFailure","true");
                        Navigation.switchActivities(myActivity,LoadingActivity.class,params);
                    }
                }
                else
                {
                    HashMap<Integer, Map> Maps = new HashMap<>();
                    if (myActivity == null) {
                        myActivitySandBox.ResponseMap(Maps);
                    } else {
                        myActivity.ResponseMap(Maps);
                    }
                }
            }
        });
    }

    public static void GetNextMap(GameActivity myActivity, int idMap)
    {
        RequestBody formBody = new FormBody.Builder()
                .add("command", "GetNextMap")
                .add("api_key",LoadingActivity.API_KEY)
                .add("idMap",String.valueOf( idMap ) )
                .build();

        Request request = new Request.Builder()
                .url(LoadingActivity.CONNEXION_API)
                .post(formBody)
                .build();

        OkHttpClient client = new OkHttpClient();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                params.put("NetworkFailure","true");
                Navigation.switchActivities(myActivity,LoadingActivity.class,params);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                String responseStr = Objects.requireNonNull(response.body()).string();

                if (!responseStr.equals(""))
                {
                    if ( !responseStr.equals("false") )
                    {
                        try
                        {
                            JSONObject json = new JSONObject(responseStr);
                            Map myMap = Map.HydrateMap( json );
                            myActivity.ResponseNextLevel(myMap);
                        }
                        catch(JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        params.put("NetworkFailure","true");
                        Navigation.switchActivities(myActivity,LoadingActivity.class,params);
                    }
                }
                else
                {
                    Navigation.switchActivities(myActivity,LoadingActivity.class,params);
                }
            }
        });
    }
}
