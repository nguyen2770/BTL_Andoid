package com.example.btl_android.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btl_android.Activity.MainActivity;
import com.example.btl_android.Adapter.AlbumsAdapter;
import com.example.btl_android.Adapter.SongsAdapter;
import com.example.btl_android.Interface.IClickAlbumListener;
import com.example.btl_android.Interface.IClickSongListener;
import com.example.btl_android.R;
import com.example.btl_android.api.ApiService;
import com.example.btl_android.modal.Album;
import com.example.btl_android.modal.ListSongs;
import com.example.btl_android.modal.Song;
import com.example.btl_android.modal.SongManager;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private RecyclerView rcv_MusicHome, rcv_AlbumMain;
    private SongManager songManager;
    private TextView txtListMusic, txtlistAlbum;
    private List<Song> songs;

    private List<Album> albums;
    private MainActivity mainActivity;

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
        mainActivity =(MainActivity) getActivity();
        rcv_MusicHome = view.findViewById(R.id.rcv_MusicHome);
        rcv_AlbumMain = view.findViewById(R.id.rcv_AlbumHome);
        txtListMusic = view.findViewById(R.id.txtTitleBaiHat);
        txtlistAlbum = view.findViewById(R.id.txtAlbum);


        Bundle bundle = getArguments();
        if(bundle != null){
            songs  = (List<Song>) bundle.getSerializable("listSongs");
            albums = (List<Album>) bundle.getSerializable("listAlbums");

        }

        if(songs == null){

           System.out.println("not found data");
        }else{
            setDataForAdaterMusic(view.getContext());
            setDataForAdapterAlbum(view.getContext());
        }

        txtListMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("listSong", (Serializable) songs);
                bundle.putInt("title",1);
                mainActivity.goToListFragment(bundle);
            }
        });

        txtlistAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putSerializable("listAlbum", (Serializable) albums);
                bundle.putInt("title",2);
                mainActivity.goToListFragment(bundle);
            }
        });


        return view;
    }

    private void setDataForAdaterMusic(Context context){
        // set layout cho music
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        rcv_MusicHome.setLayoutManager(linearLayoutManager);
        // tạo dòng kẻ cho mỗi item
        DividerItemDecoration itemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        rcv_MusicHome.addItemDecoration(itemDecoration);

        //
        if(songs == null){
            Log.e("loi", "songs null r");
        }
        songManager = new SongManager();
        songManager.buildSongMap(songs);
        List<Song> tmp = songManager.getRandomSong();
        SongsAdapter songsAdapter = new SongsAdapter(tmp, new IClickSongListener() {
            @Override
            public void onClickSong(Song song) {

                List<Song> suggestSong = songManager.getSongsByAlbumAndArtist(song.getAlbum(),song.getArtist());

                for(Song i : suggestSong){
                    System.out.println(i.getTitle());
                }
                mainActivity.gotoDetail(song,suggestSong);
            }
        });
        rcv_MusicHome.setAdapter(songsAdapter);
    }

    private void setDataForAdapterAlbum(Context context){
        // thiết lập layout với 2 item mỗi hàng
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        rcv_AlbumMain.setLayoutManager(gridLayoutManager);
        albums = songManager.setAlbum(songs);
        List<Album> tmp = songManager.getAlbumsBySongCount(6);
        AlbumsAdapter albumsAdapter = new AlbumsAdapter(tmp, new IClickAlbumListener() {
            @Override
            public void onClickAlbum(Album album) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("album",album);
                bundle.putInt("title",3);
                mainActivity.goToListFragment(bundle);
            }


        });
        rcv_AlbumMain.setAdapter(albumsAdapter);

    }
    private void  onClickGoToDetail(Context context, Song song, List<Song> songList){

    }



}