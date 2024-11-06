package com.example.btl_android.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.btl_android.Activity.DetailActivity;
import com.example.btl_android.Activity.MainActivity;
import com.example.btl_android.Adapter.SongsAdapter;
import com.example.btl_android.Database.DBManager;
import com.example.btl_android.Interface.IClickSongListener;
import com.example.btl_android.R;
import com.example.btl_android.api.ApiService;
import com.example.btl_android.modal.ListSongs;
import com.example.btl_android.modal.Song;
import com.example.btl_android.modal.SongManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

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
    private List<Song> favoriteSongs; // Danh sách bài hát yêu thích
    DBManager dbManager;

    SongManager songManager;
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

    //Yeu thich
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_love, container, false);
        rcv_favSongs = view.findViewById(R.id.recyclerViewFavorites);
        dbManager = new DBManager(getContext());
        dbManager.open();
        favoriteSongs = new ArrayList<>();
        mainActivity = (MainActivity) getActivity(); // Thêm dòng này



        // Thiết lập adapter cho RecyclerView
        songsAdapter = new SongsAdapter(favoriteSongs, new IClickSongListener() {
            @Override
            public void onClickSong(Song song) {
                List<Song> suggestSong = favoriteSongs; // Sử dụng danh sách bài hát yêu thích
                mainActivity.gotoDetail(song, suggestSong);
            }
        });
        rcv_favSongs.setLayoutManager(new LinearLayoutManager(getContext()));
        rcv_favSongs.setAdapter(songsAdapter);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String userIdString = sharedPreferences.getString("userId", "");
        int userId = Integer.parseInt(userIdString);

        // Lấy danh sách bài hát yêu thích
        List<String> songIds = dbManager.getFavoriteSongIds(userId); // Giả định bạn có phương thức này để lấy danh sách songId
        if (songIds != null && !songIds.isEmpty()) {
            for (String songId : songIds) {
                callApiToGetSongDetails(songId); // Gọi API để lấy thông tin bài hát theo songId
            }
        }

        return view;
    }

    private void callApiToGetSongDetails(String songId) {
        // Gọi API để lấy tất cả bài hát
        ApiService.apiService.getSongs().enqueue(new retrofit2.Callback<ListSongs>() {
            @Override
            public void onResponse(Call<ListSongs> call, retrofit2.Response<ListSongs> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Song> songs = response.body().getSongs();
                    // Tìm bài hát theo songId
                    for (Song song : songs) {
                        if (song.getId().equals(songId)) {
                            favoriteSongs.add(song);
                            songsAdapter.notifyDataSetChanged(); // Cập nhật RecyclerView
                            break; // Dừng vòng lặp khi tìm thấy bài hát
                        }
                    }
                } else {
                    Log.e("LoveFragment", "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ListSongs> call, Throwable t) {
                Log.e("LoveFragment", "Error fetching songs: " + t.getMessage());
            }
        });
    }



    //Yeu thich


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dbManager.close();  // Đóng kết nối database khi không cần thiết
    }






}