package com.CaveEscape.myapplication.Lib;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.CaveEscape.myapplication.Game.GameActivity;
import com.CaveEscape.myapplication.R;

public class EndGame extends Dialog
{
    // ---------------------------------------------

    private final GameActivity context;
    private final int idLayout;
    ImageView replayGame;
    Button nextLevel;
    Button return_back;
    ImageView firstStar;
    ImageView secondStar;
    ImageView thirdStar;
    private final int nbStar;

    // ---------------------------------------------

    public EndGame( Context context, int idLayout, int nbStar )
    {
        super(context,idLayout);
        this.idLayout = idLayout;
        this.context = (GameActivity) context;
        this.nbStar = nbStar;
    }

    // ---------------------------------------------

    public ImageView getReplayGame() { return replayGame; }

    public Button getNextLevel() { return nextLevel; }

    public Button getReturn_back() { return return_back; }

    // ---------------------------------------------

    public View getView( View convertView )
    {
        if(convertView == null)
        {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(idLayout, null);
        }

        replayGame = convertView.findViewById(R.id.replay_game_end);
        return_back = convertView.findViewById(R.id.button_return);
        nextLevel = convertView.findViewById(R.id.button_next_level);
        firstStar = convertView.findViewById(R.id.FirstStar);
        secondStar = convertView.findViewById(R.id.SecondStar);
        thirdStar = convertView.findViewById(R.id.ThirdStar);

        thirdStar.setImageResource(R.drawable.grey_star);
        secondStar.setImageResource(R.drawable.grey_star);
        firstStar.setImageResource(R.drawable.grey_star);

        if (nbStar > 2){
            thirdStar.setImageResource(R.drawable.yellow_star);
        }
        if (nbStar > 1 ){
            secondStar.setImageResource(R.drawable.yellow_star);
        }
        if( nbStar > 0){
            firstStar.setImageResource(R.drawable.yellow_star);
        }

        return convertView;
    }

    // ---------------------------------------------{
}
