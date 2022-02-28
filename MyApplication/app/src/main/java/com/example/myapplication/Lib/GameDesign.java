package com.example.myapplication.Lib;

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
    int images[];
    private int[] matrix;
    private int LineHeight;


    public GameDesign(Context c, int[] animals, int[] matrix,int LineHeight)
    {
        context = c;
        this.images = animals;
        inflater = (LayoutInflater.from(c));
        this.matrix = matrix;
        this.LineHeight = LineHeight;
    }

    @Override
    public int getCount() {
        return matrix.length;
    }

    @Override
    public Object getItem(int i) { return null;}

    @Override
    public long getItemId(int i) {return 0; }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        if(view == null)
        {
            view = inflater.inflate(R.layout.row_item_game, null);
        }

        ImageView aCase = view.findViewById(R.id.caseGameImage);
        aCase.getLayoutParams().height = LineHeight;
        System.out.println("Index : " + i);

        aCase.setImageResource(matrix[i]);

        return view;
    }
}
