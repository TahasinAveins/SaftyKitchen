package com.example.weatherstation;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomListView extends ArrayAdapter<String> {


    private String[] humi;
    private String[] temp;
    private String[] wind;
    private String[] rain;
    private Activity context ;

    public CustomListView(Activity context, String[] humi,String[] temp,String[] wind,String[] rain) {

        super(context, R.layout.layout,humi);

        this.context=context;
        this.humi=humi;
        this.temp=temp;
        this.wind=wind;
        this.rain=rain;


    }


    @NonNull
    @Override

    public View getView(int position , @NonNull View convertView, @NonNull ViewGroup parent) {

        View r = convertView;

        ViewHolder viewHolder = null;
        if (r==null)
        {
            LayoutInflater layoutInflater = context.getLayoutInflater();

            r = layoutInflater.inflate(R.layout.layout,null,true);

            viewHolder = new ViewHolder(r);
            r.setTag(viewHolder);
        }

        else {
            viewHolder = (ViewHolder)r.getTag();
        }

        viewHolder.textView1.setText(humi[position]);
        viewHolder.textView2.setText(temp[position]);
        viewHolder.textView3.setText(wind[position]);
        viewHolder.textView4.setText(rain[position]);

        return r;

    }

    class ViewHolder{
        TextView textView1;
        TextView textView2;
        TextView textView3;
        TextView textView4;

        ViewHolder(View v){

            textView1 = (TextView)v.findViewById(R.id.humidity);
            textView2 = (TextView)v.findViewById(R.id.temperature);
            textView3 = (TextView)v.findViewById(R.id.wind);
            textView4 = (TextView)v.findViewById(R.id.rain);
        }
    }
}
