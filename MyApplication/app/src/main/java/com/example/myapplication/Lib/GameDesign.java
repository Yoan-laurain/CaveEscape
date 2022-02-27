package com.example.myapplication.Lib;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.myapplication.R;

public class GameDesign extends BaseAdapter
{
    private Context context;
    private LayoutInflater inflater;
    int animals[];
    private int[] matrix;


    public GameDesign(Context c, int[] animals, int[] matrix)
    {
        context = c;
        this.animals = animals;
        inflater = (LayoutInflater.from(c));
        this.matrix = matrix;
    }

    @Override
    public int getCount() {
        return matrix.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        if(view == null)
        {
            view = inflater.inflate(R.layout.row_item_game, null);
        }

        ImageView aCase = view.findViewById(R.id.caseGameImage);
        aCase.setImageResource(animals[0]);
        return view;
    }
}
