package com.example.btl_android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class SongList extends Fragment {

    GridView gv_songList;
    ArrayList<Item> itemArrayList;
    MyAdapter adapter;



    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public SongList() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static BaiHat newInstance(String param1, String param2) {
        BaiHat fragment = new BaiHat();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_songlist, container, false);

        // Lấy các view
//        ImageView imageView = view.findViewById(R.id.imageView1);
//        TextView songNameTextView = view.findViewById(R.id.txt_tenBaiHat);
//        TextView singerNameTextView = view.findViewById(R.id.txt_caSi);
//
//        // Lấy dữ liệu từ bundle
//        Bundle bundle = getArguments();
//        if (bundle != null) {
//            String songName = bundle.getString("tenBaiHat");
//            String singerName = bundle.getString("tenCaSi");
//            int imageResId = bundle.getInt("Anh");
//
//            // Gán dữ liệu vào các view
//            songNameTextView.setText(songName);
//            singerNameTextView.setText(singerName);
//            imageView.setImageResource(imageResId);
//        }
        gv_songList = view.findViewById(R.id.gv_songList);
        itemArrayList = new ArrayList<>();
        Item i1 = new Item("Bai hat 1", "Ca si 1", R.drawable.bg1);
        Item i2 = new Item("Bai hat 1", "Ca si 1", R.drawable.bg1);
        Item i3 = new Item("Bai hat 1", "Ca si 1", R.drawable.bg1);
        Item i4 = new Item("Bai hat 1", "Ca si 1", R.drawable.bg1);
        Item i5 = new Item("Bai hat 1", "Ca si 1", R.drawable.bg1);
        Item i6 = new Item("Bai hat 1", "Ca si 1", R.drawable.bg1);

        itemArrayList.add(i1);
        itemArrayList.add(i2);
        itemArrayList.add(i3);
        itemArrayList.add(i4);
        itemArrayList.add(i5);
        itemArrayList.add(i6);

        adapter = new MyAdapter(itemArrayList, getContext());
        gv_songList.setAdapter(adapter);

        return view;
    }
}
