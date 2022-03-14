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

public class SandBoxLevelDesigner extends ArrayAdapter
{
    // ---------------------------------------------

    private final ArrayList<String> title;
    private final int idLayout;
    private final Activity context;
    private final ArrayList<Boolean> isTested;

    // ---------------------------------------------

    public SandBoxLevelDesigner(Context context, int idLayout, ArrayList<String> title, ArrayList<Boolean> isTested)
    {
        super(context, idLayout,title);
        this.title = title;
        this.idLayout = idLayout;
        this.context = (Activity) context;
        this.isTested = isTested;
    }

    // ---------------------------------------------

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

        TextView textViewTitle = convertView.findViewById(R.id.TitreLevelSandbox);
        textViewTitle.setText(title.get(position));

        ImageView lightIsTested = convertView.findViewById(R.id.lightLevelTested);
        if ( isTested.get(position) )
        {
            lightIsTested.setImageResource(R.drawable.green_circle);
        }
        else
        {
            lightIsTested.setImageResource(R.drawable.red_circle);
        }

        return  convertView;
    }
    // ---------------------------------------------
}
