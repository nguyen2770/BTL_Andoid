package com.example.btl_android;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    GridView gridView;
    GridView gridView2;

    ArrayList<Item> itemArrayList;
    ArrayList<Item2> item2ArrayList;
    MyAdapter adapter;
    MyAdapter2 adapter2;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        gridView = view.findViewById(R.id.gridView);
        gridView2 = view.findViewById(R.id.gridview2);


        itemArrayList = new ArrayList<>();
        Item i1 = new Item("Bai hat 1", "Ca si 1", R.drawable.bg1);
        Item i2 = new Item("Bai hat 1", "Ca si 1", R.drawable.bg1);
        Item i3 = new Item("Bai hat 1", "Ca si 1", R.drawable.bg1);
        Item i4 = new Item("Bai hat 1", "Ca si 1", R.drawable.bg1);
        Item i5 = new Item("Bai hat 1", "Ca si 1", R.drawable.bg1);
        Item i6 = new Item("Bai hat 1", "Ca si 1", R.drawable.bg1);

        item2ArrayList = new ArrayList<>();
        Item2 it1 = new Item2("Bai hat 1", R.drawable.bg1);
        Item2 it2 = new Item2("Bai hat 1", R.drawable.bg1);
        Item2 it3 = new Item2("Bai hat 1", R.drawable.bg1);
        Item2 it4 = new Item2("Bai hat 1", R.drawable.bg1);
        Item2 it5 = new Item2("Bai hat 1", R.drawable.bg1);
        Item2 it6 = new Item2("Bai hat 1", R.drawable.bg1);



        itemArrayList.add(i1);
        itemArrayList.add(i2);
        itemArrayList.add(i3);
        itemArrayList.add(i4);
        itemArrayList.add(i5);
        itemArrayList.add(i6);

        item2ArrayList.add(it1);
        item2ArrayList.add(it2);
        item2ArrayList.add(it3);
        item2ArrayList.add(it4);
        item2ArrayList.add(it5);
        item2ArrayList.add(it6);




        adapter = new MyAdapter(itemArrayList, getContext());
        gridView.setAdapter(adapter);


        adapter2 = new MyAdapter2(item2ArrayList, getContext());
        gridView2.setAdapter(adapter2);


        return view;


    }




}