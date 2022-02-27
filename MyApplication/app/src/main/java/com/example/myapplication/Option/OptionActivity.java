package com.example.myapplication.Option;

import android.support.v7.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.example.myapplication.Info.InfoActivity;
import com.example.myapplication.MainMenu.LoadingActivity;
import com.example.myapplication.R;
import com.example.myapplication.Lib.Navigation;

import java.util.HashMap;

public class OptionActivity extends AppCompatActivity
{
    Button button_return;
    Button button_credit;
    HashMap params = new HashMap<>();

    ImageView background;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        background = findViewById(R.id.View_BackGround_Option);
        Glide.with(this).load(R.drawable.back).into(background);

        button_return = findViewById(R.id.button_option_return);
        button_return.setOnClickListener(view -> Navigation.switchActivities(this, LoadingActivity.class,params));

        button_credit = findViewById(R.id.button_option_credit);
        button_credit.setOnClickListener(view -> Navigation.switchActivities(this, InfoActivity.class, params));

    }

}
