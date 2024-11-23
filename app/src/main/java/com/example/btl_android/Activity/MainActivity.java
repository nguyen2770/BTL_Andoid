package com.example.btl_android.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.example.btl_android.Fragment.HomeFragment;
import com.example.btl_android.Fragment.ListFragment;
import com.example.btl_android.Fragment.LoveFragment;
import com.example.btl_android.MyService;
import com.example.btl_android.R;
import com.example.btl_android.Fragment.SearchFragment;
import com.example.btl_android.Fragment.UserFragment;
import com.example.btl_android.api.ApiService;
import com.example.btl_android.modal.Album;
import com.example.btl_android.modal.ListSongs;
import com.example.btl_android.modal.Song;
import com.example.btl_android.modal.SongManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private FrameLayout frameLayout;
    private BottomNavigationView menuView;
    private RelativeLayout relativeLayout;

    private List<Song> songs;
    private List<Song> songList;
    private List<Album> albums;
    private SongManager songManager;
    private Song mSong;
    private Boolean isPlaying;
    private int curIndexSong;

    private TextView txt_title, txt_singer;
    private ImageView img_avata, img_pauseOrStart, img_clear;



//    private Fragment homeFragment = new HomeFragment();
//    private Fragment searchFragment = new SearchFragment();
//    private Fragment loveFragment = new LoveFragment();
//    private Fragment userFragment = new UserFragment();
//    private Fragment listFragment = new ListFragment();
//    private Fragment activeFragment = homeFragment;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if(bundle == null){
                Log.e("onReceive: ", "action null");
                return;
            }

            curIndexSong = bundle.getInt("index",0);
            songList = (List<Song>) bundle.get("list_song");
            mSong = (Song) bundle.getSerializable("object_song");
            isPlaying = bundle.getBoolean("status_player");
            int actionMusic = bundle.getInt("action_music");
            Log.e("onReceive: ", "action "+actionMusic);

            // Kiểm tra giá trị curIndexSong
            Log.d("curIndexSong", "curIndexSong: " + curIndexSong);

// Kiểm tra songList có null hay không
            if (songList == null) {
                Log.e("songList", "songList is null");
            } else {
                Log.d("songList", "songList size: " + songList.size());
            }

// Kiểm tra mSong có null hay không
            if (mSong == null) {
                Log.e("mSong", "mSong is null");
            } else {
                Log.d("mSong", "mSong: " + mSong.toString());
            }
            handLayoutMusic(actionMusic);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layout_list), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // đăng ký lắng nghe broadreceiver
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(broadcastReceiver, new IntentFilter("send_data_to_activity"));
       // ánh xạ id
        mappingID();

        if (songs == null) {
            Log.e("mainactivity", "song is null");
            // khi data load xong mới hiện homeframent
            CallApiWithCallback(() -> replaceDataFragment(new HomeFragment()));
        } else {
            Log.e("mainactivity", "song not null");
        }
        // Thêm tất cả các Fragment nhưng chỉ hiển thị HomeFragment ban đầu

//        getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, listFragment, "5").hide(listFragment).commit();
//        getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, userFragment, "4").hide(userFragment).commit();
//        getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, loveFragment, "3").hide(loveFragment).commit();
//        getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, searchFragment, "2").hide(searchFragment).commit();
//        getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, homeFragment, "1").commit();
//
//        menuView.setOnItemSelectedListener(item -> {
//            int ID = item.getItemId();
//            if (ID == R.id.home) {
//                showFragment(homeFragment);
//            } else if (ID == R.id.search) {
//                showFragment(searchFragment);
//            } else if (ID == R.id.love) {
//                showFragment(loveFragment);
//            } else if (ID == R.id.user) {
//                showFragment(userFragment);
//            }
//            return true;
//        });

        //replaceDataFragment(new HomeFragment());

        //
        menuView.setOnItemSelectedListener(item -> {

            int ID = item.getItemId();
            if (ID == R.id.home) {
                replaceDataFragment(new HomeFragment());
            } else if (ID == R.id.search) {
                replaceDataFragment(new SearchFragment());
            } else if (ID == R.id.love) {
                replaceDataFragment(new LoveFragment());
            } else if (ID == R.id.user) {
                replaceFragment(new UserFragment());
            }
            return true;
        });
        onClickGoToDetail();

    }


    private void mappingID() {
        frameLayout = findViewById(R.id.frameLayout);
        menuView = findViewById(R.id.bottomNavigationView);
        relativeLayout = findViewById(R.id.notification);
        txt_title = findViewById(R.id.txt_titleSong);
        txt_singer = findViewById(R.id.txt_singerSong);
        img_avata = findViewById(R.id.imgSong);
        img_pauseOrStart = findViewById(R.id.img_play_or_pause);
        img_clear = findViewById(R.id.img_clear);
    }

    private void CallApiWithCallback(Runnable onSuccess) {
        ApiService.apiService.getSongs().enqueue(new Callback<ListSongs>() {
            @Override
            public void onResponse(Call<ListSongs> call, Response<ListSongs> response) {
                Toast.makeText(MainActivity.this, "Call Api success", Toast.LENGTH_SHORT).show();
                ListSongs listSongs = response.body();
                if (listSongs != null) {
                    songs = listSongs.getSongs();
                    songManager = new SongManager();
                    albums = songManager.setAlbum(songs);

                    // Gọi hàm khi dữ liệu đã load xong
                    if (onSuccess != null) {
                        onSuccess.run();
                    }
                } else {
                    Log.e("lỗi ", "danh sách null");
                }
            }

            @Override
            public void onFailure(Call<ListSongs> call, Throwable t) {
                Log.e("API Error", t.getMessage());
                Toast.makeText(MainActivity.this, "Call Api error", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * Thay thế một Fragment hiện tại bằng một Fragment mới trong container.
     * <p>
     * fragment Fragment mới sẽ thay thế Fragment hiện tại.
     */
    private void replaceFragment(Fragment fragment) {
        // Lấy đối tượng FragmentManager để quản lý các Fragment trong Activity.
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Bắt đầu một giao dịch Fragment.
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Thay thế Fragment hiện tại trong container có ID R.id.farmeLayout bằng Fragment mới.
        // Đảm bảo R.id.farmeLayout là ID của một ViewGroup trong layout của Activity.
        fragmentTransaction.replace(R.id.frameLayout, fragment);

        // Cam kết (commit) giao dịch, áp dụng thay đổi và thực hiện thay thế Fragment.
        fragmentTransaction.commit();
    }

    // chỉ ẩn fragment cũ đi và hiện fragment mới lên <lưu lại trạng thais của frgment khi chuyển đôi"
//    private void showFragment(Fragment fragment) {
//        if (fragment != activeFragment) {
//            getSupportFragmentManager().beginTransaction().hide(activeFragment).show(fragment).commit();
//            activeFragment = fragment;
//        }
//    }

    private void replaceDataFragment(Fragment fragment) {

        if (songs != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Bundle bundle = new Bundle();
            bundle.putSerializable("listSongs", (Serializable) songs);
            bundle.putSerializable("listAlbums", (Serializable) albums);
            fragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.frameLayout, fragment);
            fragmentTransaction.commit();
        } else {
            System.out.println("song dang null");
        }
    }

    // send data go to list fragment
    public void goToListFragment(Bundle bundle) {

        if (bundle != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            ListFragment listFragment = new ListFragment();
            listFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.frameLayout, listFragment);
            fragmentTransaction.addToBackStack(ListFragment.TAG);
            fragmentTransaction.commit();


        }

    }

    // send song go to detail activity
    public void gotoDetail(Song curSong, List<Song> songSuggest){
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("curSong", curSong);
        bundle.putSerializable("SuggestSong", (Serializable) songSuggest);
        int index = songSuggest.indexOf(curSong);
        if(index > 0){
            bundle.putInt("index", index);
        }
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void handLayoutMusic(int action){
        switch (action){
            case MyService.ACTION_START:
                relativeLayout.setVisibility(View.VISIBLE);
                setStatusButtonPlayOrPause();
                showInforSong();
                Log.e("handLayoutMusic: ", "start");
                break;
            case MyService.ACTION_PAUSE:
                setStatusButtonPlayOrPause();
                break;
            case MyService.ACTION_RESUME:
                setStatusButtonPlayOrPause();
                break;
            default:

        }

    }

    private void showInforSong(){
        if(mSong == null){
            return;
        }
        Glide.with(this)
                .load(mSong.getImage())
                .into(img_avata);
        txt_title.setText(mSong.getTitle());
        txt_singer.setText(mSong.getArtist());

        img_pauseOrStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPlaying){
                    sendActionToService(MyService.ACTION_PAUSE);
                }else{
                    sendActionToService(MyService.ACTION_RESUME);
                }
            }
        });

        img_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendActionToService(MyService.ACTION_DESTROY);
                relativeLayout.setVisibility(View.GONE);

            }
        });

    }

    private void setStatusButtonPlayOrPause(){
        if(isPlaying){
            img_pauseOrStart.setImageResource(R.drawable.baseline_pause_circle_outline_24);
        }else{
            img_pauseOrStart.setImageResource(R.drawable.baseline_play_circle_outline_24);
        }
    }

    private void sendActionToService(int action){
        Intent intent = new Intent(this,MyService.class);
        intent.putExtra("actionMusicService", action);
        startService(intent);
    }

    private void onClickGoToDetail(){
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int k = isPlaying ? 1 : 2;

                Intent intent = new Intent(MainActivity.this,DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("curSong", mSong);
                bundle.putSerializable("SuggestSong", (Serializable) songList);
                bundle.putInt("k",k);
                bundle.putInt("index",curIndexSong );


                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.botton_nav_menu, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }
}