package com.example.btl_android.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.btl_android.Activity.MainActivity;
import com.example.btl_android.Adapter.AlbumsAdapter;
import com.example.btl_android.Adapter.SongsAdapter;
import com.example.btl_android.Interface.IClickAlbumListener;
import com.example.btl_android.Interface.IClickSongListener;
import com.example.btl_android.R;
import com.example.btl_android.modal.Album;
import com.example.btl_android.modal.Song;
import com.example.btl_android.modal.SongManager;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment {

    public static final String TAG = ListFragment.class.getName();
    TextView title;
    ImageView imgBack;
    RecyclerView rcv_list;
    private List<Song> songList;
    private List<Album> albums;
    SongManager songManager;
    int key;
    MainActivity mainActivity;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListFragment newInstance(String param1, String param2) {
        ListFragment fragment = new ListFragment();
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
        View view =  inflater.inflate(R.layout.fragment_list, container, false);

        rcv_list = view.findViewById(R.id.rcv_list);
        title = view.findViewById(R.id.txtTitleHearder);
        imgBack = view.findViewById(R.id.imgBack);
        mainActivity = (MainActivity) getActivity();

        Bundle bundle = getArguments();
        if (bundle != null){
            int tmp = bundle.getInt("title",-1);
            if(tmp == 1){
                songList = (List<Song>) bundle.getSerializable("listSong");
                key = 1;
                title.setText("Bài hát nổi bật");
                setlayoutMusic(view.getContext());
            }else if(tmp == 2){
                albums = (List<Album>) bundle.getSerializable("listAlbum");
                title.setText("Album nổi bật");
                setLayoutAlbum(view.getContext());
            }else if(tmp == 3){
                Album album = (Album) bundle.getSerializable("album");
                songList = album.getSongs();
                key = 3;
                title.setText(album.getAlbum());
                setlayoutMusic(view.getContext());
            }

        }else{
            System.out.println("bunder null rồi");
        }

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(getFragmentManager() != null){
                    getFragmentManager().popBackStack();
                }
            }
        });


        return view;
    }

    private void setLayoutAlbum(Context context) {
        // thiết lập layout với 2 item mỗi hàng
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
        rcv_list.setLayoutManager(gridLayoutManager);


        AlbumsAdapter albumsAdapter = new AlbumsAdapter(albums, new IClickAlbumListener() {
            @Override
            public void onClickAlbum(Album album) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("album",album);
                bundle.putInt("title",3);
                mainActivity.goToListFragment(bundle);
            }
        });
        rcv_list.setAdapter(albumsAdapter);

    }

    private void setlayoutMusic(Context context) {
        // set layout cho music
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        rcv_list.setLayoutManager(linearLayoutManager);
        // tạo dòng kẻ cho mỗi item
        DividerItemDecoration itemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        rcv_list.addItemDecoration(itemDecoration);

        //
        if(songList == null){
            Log.e("loi", "songs null r");
        }
        songManager = new SongManager();
        songManager.buildSongMap(songList);


        SongsAdapter songsAdapter = new SongsAdapter(songList, new IClickSongListener() {
            @Override
            public void onClickSong(Song song) {
                List<Song> suggestSong;
                if(key == 1){
                    suggestSong = songManager.getSongsByAlbumAndArtist(song.getAlbum(),song.getArtist());

                }else{
                    suggestSong = songList;
                }

                for(Song i : suggestSong){
                    System.out.println(i.getTitle());

                }
                Bundle bundle = new Bundle();
                mainActivity.gotoDetail(song,suggestSong);
            }
        });
        rcv_list.setAdapter(songsAdapter);
    }

    private void onClickGoToDetail(Song song, List<List> tmp ){

    }
}