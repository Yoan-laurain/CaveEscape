package com.example.myapplication.Sandbox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.example.myapplication.MainMenu.LoadingActivity;
import com.example.myapplication.R;
import com.example.myapplication.Lib.Navigation;

import java.util.HashMap;

public class SandboxMenuActivity extends AppCompatActivity
{
    Button button_return;
    HashMap params = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sandbox_menu);

        button_return = findViewById(R.id.button_sandboxmenu_return);
        button_return.setOnClickListener(view -> Navigation.switchActivities(this, LoadingActivity.class,params));
    }

}
