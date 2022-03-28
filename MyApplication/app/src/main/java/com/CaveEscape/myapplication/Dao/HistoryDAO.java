package com.CaveEscape.myapplication.Dao;


import com.CaveEscape.myapplication.Lib.Navigation;
import com.CaveEscape.myapplication.MainMenu.LoadingActivity;
import org.jetbrains.annotations.NotNull;
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

public class HistoryDAO
{
    //-------------------

    private static final HashMap params = new HashMap<>();

    //-------------------

    /*
       Insert new player in the dataBase
    */
    public static void NewPlayer( LoadingActivity myActivity, String idClient )
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
                params.put("NetworkFailure","true");
                Navigation.switchActivities(myActivity,LoadingActivity.class,params);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException
            {
                String responseStr = Objects.requireNonNull(response.body()).string();

                if ( responseStr.equals("false") )
                {
                    params.put("NetworkFailure","true");
                    Navigation.switchActivities(myActivity,LoadingActivity.class,params);
                }
            }
        });
    }

}
