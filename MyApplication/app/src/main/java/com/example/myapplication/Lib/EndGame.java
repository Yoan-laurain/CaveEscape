package com.example.myapplication.Lib;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.myapplication.Game.GameActivity;
import com.example.myapplication.LevelSelect.SelectActivity;
import com.example.myapplication.R;
import java.util.HashMap;

public class EndGame extends Dialog
{
    // ---------------------------------------------

    private final GameActivity context;
    private int idLayout;
    ImageView replayGame;
    Button nextLevel;

    // ---------------------------------------------

    public EndGame(Context context, int idLayout)
    {
        super(context,idLayout);
        this.idLayout = idLayout;
        this.context = (GameActivity) context;
    }

    // ---------------------------------------------

    public ImageView getReplayGame() { return replayGame; }

    public Button getnextLevel() { return nextLevel; }
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

        Button return_back = convertView.findViewById(R.id.button_return);
        return_back.setOnClickListener(var -> Navigation.switchActivities(context, SelectActivity.class,params));

        nextLevel = convertView.findViewById(R.id.button_next_level);

        return convertView;
    }

    // ---------------------------------------------{
}
