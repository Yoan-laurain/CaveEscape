package com.example.myapplication.Lib;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.myapplication.R;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class TutoDesign extends Dialog
{
    // ---------------------------------------------

    private final Activity context;
    private int idLayout;
    private static ArrayList<String> text;
    private int image;
    private boolean end;
    private int currentText;
    public boolean listenerActive;
    public PropertyChangeSupport changes = new PropertyChangeSupport(this);


    // ---------------------------------------------

    public TutoDesign(Context context, int idLayout, ArrayList<String> text)
    {
        super(context,idLayout);
        this.idLayout = idLayout;
        this.context = (Activity) context;
        this.text = text;
        this.image = R.drawable.pnj;
        this.end = false;
        this.currentText = 0;
    }


    // ---------------------------------------------

    public int getCurrentText() {
        return currentText;
    }

    // ---------------------------------------------

    public View getView( View convertView)
    {
        if(convertView == null)
        {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.popup_tuto, null);
        }

        TextView dialog = convertView.findViewById(R.id.text_dialog);
        ImageView pnj = convertView.findViewById(R.id.image_pnj);
        pnj.setImageResource(image);

        Button hidden_button = convertView.findViewById(R.id.hidden_button);
        hidden_button.setOnClickListener(var ->
                {
                    if ( !end )
                    {
                        currentText++;
                        this.listenerActive = true;
                        this.changes.firePropertyChange("listenerActive",false,true);
                    }
                });

        dialog.setText( text.get(currentText) );

        return convertView;
    }

    // ---------------------------------------------

}
