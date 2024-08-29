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

public class MyAdapter extends ArrayAdapter<Item> {
    private ArrayList<Item> itemArrayList;
    Context context;

    public MyAdapter(ArrayList<Item> itemArrayList, Context context) {
        super(context, R.layout.grid_item, itemArrayList);
        this.itemArrayList = itemArrayList;
        this.context = context;
    }

    public static class MyViewHolder{
        TextView txt_tenBaiHat;
        TextView txt_tenCaSi;
        ImageView imageView;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Item item = getItem(position);

        MyViewHolder myViewHolder;
        final View result;

        if(convertView == null){
            myViewHolder = new MyViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(
                    R.layout.grid_item,
                    parent, false
            );

            myViewHolder.txt_tenBaiHat = (TextView) convertView.findViewById(R.id.tenBaiHat);
            myViewHolder.txt_tenCaSi = (TextView) convertView.findViewById(R.id.tenCaSi);
            myViewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageview);

            result = convertView;
            convertView.setTag(myViewHolder);
        }else{
            myViewHolder = (MyViewHolder) convertView.getTag();
            result = convertView;
        }
        myViewHolder.txt_tenBaiHat.setText(item.getTenBaiHat());
        myViewHolder.txt_tenCaSi.setText(item.getTenCaSi());
        myViewHolder.imageView.setImageResource(item.getItemImg());
        return result;
    }
}
