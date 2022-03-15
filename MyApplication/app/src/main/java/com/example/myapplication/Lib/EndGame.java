package com.example.myapplication.Lib;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.myapplication.Game.GameActivity;
import com.example.myapplication.R;

import java.util.HashMap;

public class EndGame extends Dialog
{
    // ---------------------------------------------

    private final GameActivity context;
    private int idLayout;
    ImageView replayGame;
    Button nextLevel;
    Button return_back;

    // ---------------------------------------------

    public EndGame(Context context, int idLayout)
    {
        super(context,idLayout);
        this.idLayout = idLayout;
        this.context = (GameActivity) context;
    }

    // ---------------------------------------------

    public ImageView getReplayGame() { return replayGame; }

    public Button getNextLevel() { return nextLevel; }

    public Button getReturn_back() { return return_back; }

    // ---------------------------------------------

    public View getView(View convertView)
    {
        if(convertView == null)
        {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(idLayout, null);
        }

        HashMap params = new HashMap<>();

        replayGame = convertView.findViewById(R.id.replay_game_end);

        return_back = convertView.findViewById(R.id.button_return);


        nextLevel = convertView.findViewById(R.id.button_next_level);

        return convertView;
    }

    // ---------------------------------------------{
}
