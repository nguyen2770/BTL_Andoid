package com.example.btl_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MyAdapter2 extends ArrayAdapter<Item2> {
    private ArrayList<Item2> item2ArrayList;
    Context context;

    public MyAdapter2(ArrayList<Item2> item2ArrayList, Context context) {
        super(context, R.layout.grid_item2, item2ArrayList);
        this.item2ArrayList = item2ArrayList;
        this.context = context;
    }
    public static class MyViewHolder{
        TextView moTa;
        ImageView imageView;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Item2 item2 = getItem(position);
        MyViewHolder myViewHolder;
        final View result;
        if(convertView == null){
            myViewHolder = new MyViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(
                    R.layout.grid_item2,
                    parent,
                    false
            );
            myViewHolder.moTa = (TextView) convertView.findViewById(R.id.moTa);
            myViewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageview);
            result = convertView;
            convertView.setTag(myViewHolder);
        }else{
            myViewHolder = (MyViewHolder) convertView.getTag();
            result = convertView;
        }
        myViewHolder.moTa.setText(item2.getTenBaiHat());
        myViewHolder.imageView.setImageResource(item2.getImageView());
        return result;
    }
}
