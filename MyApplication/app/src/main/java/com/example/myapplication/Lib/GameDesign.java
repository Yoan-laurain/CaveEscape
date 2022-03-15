package com.example.myapplication.Lib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.example.myapplication.R;

public class GameDesign extends BaseAdapter
{
    //------------------------------------------------------------------------------

    private final LayoutInflater inflater;
    private final int[] matrix;
    private final int LineHeight;

    //------------------------------------------------------------------------------

    public GameDesign(Context c, int[] matrix,int LineHeight)
    {
        inflater = (LayoutInflater.from(c));
        this.matrix = matrix;
        this.LineHeight = LineHeight;
    }

    //------------------------------------------------------------------------------

    @Override
    public int getCount() {
        return matrix.length;
    }

    @Override
    public Object getItem( int i ) { return null; }

    @Override
    public long getItemId( int i ) { return 0; }

    //------------------------------------------------------------------------------

    @SuppressLint("InflateParams")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        if(view == null)
        {
            view = inflater.inflate(R.layout.row_item_game, null);
        }

        ImageView aCase = view.findViewById(R.id.caseGameImage);
        aCase.getLayoutParams().height = LineHeight;

        aCase.setImageResource(matrix[i]);

        return view;
    }

    //------------------------------------------------------------------------------
}
