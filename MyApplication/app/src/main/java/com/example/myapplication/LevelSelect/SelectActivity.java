package com.example.myapplication.LevelSelect;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.example.myapplication.Dao.MapDAO;
import com.example.myapplication.Dto.Map;
import com.example.myapplication.MainMenu.LoadingActivity;
import com.example.myapplication.R;
import com.example.myapplication.Lib.Navigation;

import java.util.HashMap;

public class SelectActivity extends AppCompatActivity
{
    Button button_return;
    HashMap params = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        button_return = findViewById(R.id.button_select_return);
        button_return.setOnClickListener(view -> Navigation.switchActivities(this, LoadingActivity.class,params));

        MapDAO.getAllMap(this);
    }

    public void responseMap(HashMap<Integer, Map> lesMaps)
    {
        if ( lesMaps.size() > 0 )
        {
            lesMaps.values().forEach(tab -> System.out.println("Resultat : " + tab.getNom() ) ) ;
        }
        else
        {
            System.out.println("No result");
        }
    }
}
