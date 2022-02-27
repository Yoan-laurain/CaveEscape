package com.example.myapplication.Lib;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.myapplication.LevelSelect.SelectActivity;
import com.example.myapplication.R;

import java.util.ArrayList;


public class LevelDesign extends ArrayAdapter
{

    // ---------------------------------------------
    private ArrayList<String> title;
    private int idLayout;
    private Activity context;


    // ---------------------------------------------

    public LevelDesign(Context context, int idLayout, ArrayList<String> title)
    {
        super(context, idLayout,title);
        this.title = title;
        this.idLayout = idLayout;
        this.context = (Activity) context;
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

        TextView textViewTitle = convertView.findViewById(R.id.TitreLevel);
        textViewTitle.setText(title.get(position));

        return  convertView;
    }
    // ---------------------------------------------

}
