package com.example.myapplication.Game;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.myapplication.Lib.GameDesign;
import com.example.myapplication.R;

public class GameActivity extends AppCompatActivity
{
    GridView gameBoard;
    int animals[] = {R.drawable.sol};
    private int[] matrix = new int[25];


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Bundle args =  getIntent().getExtras();
        int idMap = args.getInt("idMap");

        FillGameBoard();

    }

    public void FillGameBoard()
    {
        this.runOnUiThread(() ->
        {
            gameBoard = findViewById(R.id.gameBoard);
            gameBoard.setColumnWidth(50);
            gameBoard.setNumColumns(5);

            GameDesign adapter = new GameDesign(this, animals, matrix);
            gameBoard.setAdapter(adapter);
            gameBoard.setOnItemClickListener((parent, view, position, id) -> Toast.makeText(getApplicationContext(), "You clicked ", Toast.LENGTH_SHORT).show());

        });
    }
}