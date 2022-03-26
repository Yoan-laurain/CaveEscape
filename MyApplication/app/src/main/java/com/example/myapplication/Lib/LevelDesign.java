package com.example.myapplication.Lib;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.myapplication.R;
import java.util.ArrayList;

public class LevelDesign extends ArrayAdapter<String>
{
    // ---------------------------------------------

    private final ArrayList<String> title;
    private final int idLayout;
    private final Activity context;
    private final ArrayList<Integer> score;

    // ---------------------------------------------

    public LevelDesign(Context context, int idLayout, ArrayList<String> title, ArrayList<Integer> score)
    {
        super(context, idLayout,title);
        this.title = title;
        this.idLayout = idLayout;
        this.context = (Activity) context;
        this.score = score;
    }

    // ---------------------------------------------

    /*
        Called when used setAdapter method
        Set items of row.xlm and return the object
     */

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if(convertView == null)
        {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(idLayout, null, true);

        }

        TextView textViewTitle = convertView.findViewById(R.id.TitreLevel);
        ImageView scoreView = convertView.findViewById(R.id.ScoreView);
        textViewTitle.setText(title.get(position));

        try{
            switch (score.get(position)) {
                case 1:
                    scoreView.setImageResource(R.drawable.one_star);
                    break;
                case 2:
                    scoreView.setImageResource(R.drawable.two_stars);
                    break;
                case 3:
                    scoreView.setImageResource(R.drawable.three_stars);
                    break;
                default:
                    scoreView.setImageResource(R.drawable.zero_stars);
                    break;
            }
        }
        catch(Exception e){
            System.out.println("Error  :" + e );
        }



        return  convertView;
    }
    // ---------------------------------------------

}
