package com.example.btl_android.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.btl_android.Activity.MainActivity;
import com.example.btl_android.Adapter.SongsAdapter;
import com.example.btl_android.Database.DBManager;
import com.example.btl_android.Interface.IClickSongListener;
import com.example.btl_android.R;
import com.example.btl_android.modal.Album;
import com.example.btl_android.modal.Song;
import com.example.btl_android.modal.SongManager;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoveFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoveFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoveFragment() {
        // Required empty public constructor
    }

    private RecyclerView rcv_favSongs;
    SongsAdapter songsAdapter;
    private List<Song> favoriteSongs, songList; // Danh sách bài hát yêu thích
    DBManager dbManager;

    SongManager songManager = new SongManager();
    MainActivity mainActivity;
    Context context;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoveFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoveFragment newInstance(String param1, String param2) {
        LoveFragment fragment = new LoveFragment();
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
        View view = inflater.inflate(R.layout.fragment_love, container, false);
        rcv_favSongs = view.findViewById(R.id.recyclerViewFavorites);
        dbManager = new DBManager(view.getContext());
        dbManager.open();
        favoriteSongs = new ArrayList<>();
        mainActivity = (MainActivity) getActivity(); // Thêm dòng này

        Bundle bundle = getArguments();
        if(bundle != null){
            songList  = (List<Song>) bundle.getSerializable("listSongs");
        }

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String userIdString = sharedPreferences.getString("userId", "");
        String a = "";

        if(!userIdString.equals(a)){
            int userId = Integer.parseInt(userIdString);
            setAdapterSong(view.getContext(),userId);
        }


        return view;
    }

    private void setAdapterSong(Context context, int userId){
        // set layout cho music
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        rcv_favSongs.setLayoutManager(linearLayoutManager);
        // tạo dòng kẻ cho mỗi item
        DividerItemDecoration itemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        rcv_favSongs.addItemDecoration(itemDecoration);

        // Thiết lập adapter cho RecyclerView
        songsAdapter = new SongsAdapter(favoriteSongs, new IClickSongListener() {
            @Override
            public void onClickSong(Song song) {
                 // Sử dụng danh sách bài hát yêu thích
                mainActivity.gotoDetail(song, favoriteSongs);

            }
        });
        rcv_favSongs.setAdapter(songsAdapter);

        songManager.buildSongMap(songList);
        // lấy ra danh sách bài hát yêu thích
        List<String> songIds = dbManager.getFavoriteSongIds(userId); // Giả định bạn có phương thức này để lấy danh sách songId
        if (songIds != null && !songIds.isEmpty() && songList != null) {
            for (String songId : songIds) {
                // lấy ra thông tin bài hát theo songid
                favoriteSongs.add(songManager.getSongById(songId));
                songsAdapter.notifyDataSetChanged(); // cập nhập list yêu thích
            }
        }

    }
}