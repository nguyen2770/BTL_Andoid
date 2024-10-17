package com.example.btl_android.Fragment;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.example.btl_android.Activity.MainActivity;
import com.example.btl_android.Adapter.SongsAdapter;
import com.example.btl_android.Interface.IClickSongListener;
import com.example.btl_android.R;
import com.example.btl_android.modal.Album;
import com.example.btl_android.modal.Song;
import com.example.btl_android.modal.SongManager;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    RecyclerView recyclerView;
    SearchView searchView;
    SongsAdapter songsAdapter;
    List<Song> songs;
    List<Album> albums;
    SongManager songManager;
    MainActivity mainActivity;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        recyclerView = view.findViewById(R.id.rcv_search);
        searchView = view.findViewById(R.id.search);
        mainActivity =(MainActivity) getActivity();

        // Thiết lập SearchView để tự động focus
        searchView.setIconified(false); // Mở SearchView nếu nó bị thu nhỏ
        searchView.requestFocus(); // Yêu cầu focus vào SearchView

        // Để mở bàn phím ngay lập tức
        if (searchView.hasFocus()) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(searchView, InputMethodManager.SHOW_IMPLICIT);
            }
        }

        Bundle bundle = getArguments();
        if(bundle != null){
            songs  = (List<Song>) bundle.getSerializable("listSongs");
            albums = (List<Album>) bundle.getSerializable("listAlbums");

        }
        setDataForAdaterMusic(view.getContext());

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(songsAdapter != null){
                    songsAdapter.getFilter().filter(query);
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(songsAdapter != null){
                    songsAdapter.getFilter().filter(newText);
                }
                return false;
            }
        });

        return view;


    }

    private void setDataForAdaterMusic(Context context){
        // set layout cho music
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        // tạo dòng kẻ cho mỗi item
        DividerItemDecoration itemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        if (songs == null) {
            Log.e("loi", "songs null r");
        }
        songManager = new SongManager();
        songManager.buildSongMap(songs);


        // Gán giá trị cho biến toàn cục songsAdapter
        songsAdapter = new SongsAdapter(songs, new IClickSongListener() {
            @Override
            public void onClickSong(Song song) {
                List<Song> suggestSong = songManager.getSongsByAlbumAndArtist(song.getAlbum(), song.getArtist());

                for (Song i : suggestSong) {
                    System.out.println(i.getTitle());
                }
                mainActivity.gotoDetail(song, suggestSong);
            }
        });

        recyclerView.setAdapter(songsAdapter);
    }

}