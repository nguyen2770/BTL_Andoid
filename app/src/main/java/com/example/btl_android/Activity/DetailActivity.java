package com.example.btl_android.Activity;

import static com.example.btl_android.MyService.ACTION_AUTONEXT;
import static com.example.btl_android.MyService.ACTION_REPLAY;
import static com.example.btl_android.MyService.ACTION_UPDATE_POSITION;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.example.btl_android.Database.DBManager;
import com.example.btl_android.MyService;
import com.example.btl_android.R;
import com.example.btl_android.modal.Song;
import com.example.btl_android.modal.SongManager;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailActivity extends AppCompatActivity {

    ImageView imgv_play, imgv_pre, imgv_next, img_back, img_act, imgv_fav;
    CircleImageView img_DetailImage;
    TextView txt_DetailNameSong, txt_DetailNameTG, txt_playerPosition, txt_playerDuration;
    SeekBar seekBarTime;
    private Song currentSong;
    private List<Song> songList;
    private int currentSongIndex;
    private SongManager songManager = new SongManager();

    private boolean isPlaying, isLooping = true;

    private BroadcastReceiver positionReceiver, Animation_manager, setDataActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // cập nhập vị trí của thanh seek bar
        registerPositionReceiver();
        // ánh xạ id
        mappingID();
        // lấy ra dữ liệu được gửi qua tuef intent
        getData();
        // nhận dữ liệu action gửi về từ service (pause, next, pre)
        SetDataForActivity();

        // chạy service
        Start_Service();
        // xử lý các sự kiện onclick
        onClickPre();
        onClickNext();
        onClickPause_Start();
        onClickBack();
        onClickAct();
        // gửi vị trí thanh seekbar khi tua
        seekBarmanager();

        // update animation của image nhạc khi dừng nhạc hoặc chạy tiếp
        AnimationManager();


        if (songList != null) {
            Log.d("onCreate: ", "ko null");
        }

        // đổi icon yêu thích của các bài hát
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userIdString = sharedPreferences.getString("userId", "");
        String a = "";

        if (!userIdString.equals(a)) {
            int userId = Integer.parseInt(userIdString);
            updateFavoriteIcon(userId, currentSong.getId());
            onClickFavorite();
        }


    }

    private void onClickAct() {
        img_act.setOnClickListener(view -> {
            if (isLooping) {
                img_act.setImageResource(R.drawable.baseline_shuffle_24);
                isLooping = false;
                sendActionToService(ACTION_AUTONEXT);
            } else {
                img_act.setImageResource(R.drawable.baseline_repeat_24);
                isLooping = true;
                sendActionToService(ACTION_REPLAY);
            }
        });
    }

    private void handLayoutMusic(int action) {
        switch (action) {
            case MyService.ACTION_PAUSE:
                setStatusButtonPlayOrPause();
                break;
            case MyService.ACTION_RESUME:
                setStatusButtonPlayOrPause();
                break;
            case MyService.ACTION_NEXT:
                imgv_play.setImageResource(R.drawable.baseline_pause_circle_outline_24);
                break;
            case MyService.ACTION_PRE:
                imgv_play.setImageResource(R.drawable.baseline_pause_circle_outline_24);
                break;
            default:

        }

    }

    private void setStatusButtonPlayOrPause() {
        if (isPlaying) {
            imgv_play.setImageResource(R.drawable.baseline_pause_circle_outline_24);
        } else {
            imgv_play.setImageResource(R.drawable.baseline_play_circle_outline_24);
        }
    }

    // xử lý sự kiện dừng bài hát
    private void onClickPause_Start() {

        imgv_play.setOnClickListener(v -> {
            if (isPlaying) {
                imgv_play.setImageResource(R.drawable.baseline_play_circle_outline_24);
                sendActionToService(MyService.ACTION_PAUSE);
            } else {
                imgv_play.setImageResource(R.drawable.baseline_pause_circle_outline_24);
                sendActionToService(MyService.ACTION_RESUME);
            }
        });


    }

    // xử lý sự kiện next sang bài hát khác
    private void onClickNext() {
        imgv_next.setOnClickListener(v -> {
            sendActionToService(MyService.ACTION_NEXT);
        });

    }

    // xử lý sự kiện click vào nút quay lại bài hát trước
    private void onClickPre() {
        imgv_pre.setOnClickListener(v -> {
            sendActionToService(MyService.ACTION_PRE);
        });

    }

    private void onClickBack() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); // Đóng Activity hiện tại
            }
        });
    }

    private void toggleFavorite(int userId, String songId) {
        DBManager dbManager = new DBManager(this); // Khởi tạo DBManager với context
        dbManager.open(); // Mở kết nối tới cơ sở dữ liệu

        if (dbManager.isFavoriteSong(userId, songId)) {
            dbManager.removeFavoriteSong(userId, songId);
            imgv_fav.setImageResource(R.drawable.baseline_favorite_border_24); // Cập nhật icon
        } else {
            dbManager.addFavoriteSong(userId, songId);
            imgv_fav.setImageResource(R.drawable.baseline_favorite_24); // Cập nhật icon
        }

        dbManager.close(); // Đóng kết nối sau khi thực hiện xong
    }


    private void onClickFavorite() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int currentUserId = Integer.parseInt(sharedPreferences.getString("userId", null)); // Lấy ID của người dùng

        imgv_fav.setOnClickListener(v -> toggleFavorite(currentUserId, currentSong.getId()));
    }

    private void updateFavoriteIcon(int userId, String songId) {
        DBManager dbManager = new DBManager(this);
        dbManager.open();

        if (dbManager.isFavoriteSong(userId, songId)) {
            imgv_fav.setImageResource(R.drawable.baseline_favorite_24); // Đổi icon thành yêu thích
        } else {
            imgv_fav.setImageResource(R.drawable.baseline_favorite_border_24); // Đổi icon thành không yêu thích
        }

        dbManager.close();
    }


    private void mappingID() {
        img_DetailImage = findViewById(R.id.img_detaiImage);
        txt_DetailNameSong = findViewById(R.id.txt_detalNameSong);
        txt_DetailNameTG = findViewById(R.id.txt_detalNameTG);
        txt_playerDuration = findViewById(R.id.txt_playerDuration);
        txt_playerPosition = findViewById(R.id.txt_playerPoisition);
        imgv_pre = findViewById(R.id.imgv_pre);
        imgv_next = findViewById(R.id.imgv_next);
        imgv_play = findViewById(R.id.imgv_play);
        seekBarTime = findViewById(R.id.seekBarTime);
        img_back = findViewById(R.id.imageButton);
        img_act = findViewById(R.id.img_act);
        imgv_fav = findViewById(R.id.imgv_fav);
    }


    // lấy data được truyền qua từ mainActivity
    private void getData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            currentSong = (Song) bundle.getSerializable("curSong");
            songList = (List<Song>) bundle.getSerializable("SuggestSong");
            currentSongIndex = bundle.getInt("index", 0);
            Log.d("Setdata: ", "index " + currentSongIndex);
            if (currentSong != null) {
                //setSongDetail(currentSong);
                Log.d("Setdata: ", "song khong null");
            } else {
                Log.d("Setdata: ", "song null");
            }
            setSongDetail(songList.get(currentSongIndex));

            int tmp = bundle.getInt("k", 0);
            if (tmp == 1) {
                isPlaying = true;
                setStatusButtonPlayOrPause();
                startAnimationMusic();
            } else if (tmp == 2) {
                isPlaying = false;
                setStatusButtonPlayOrPause();
                stopAnimationMusic();
            }

            if (songList != null) {
                Log.d("Setdata: ", "songlist ko null");
            } else {
                Log.d("Setdata: ", "songlist null");
            }
        } else {
            Log.d("Setdata: ", "bundal null");
        }
    }

    // set data lên activity
    private void setSongDetail(Song song) {
        String imageUrl = song.getImage();
        Glide.with(img_DetailImage.getContext())
                .load(imageUrl)
                .into(img_DetailImage);
        txt_DetailNameSong.setText(song.getTitle());
        txt_DetailNameTG.setText(song.getArtist());
        String sduration = convertFormat(song.getDuration());
        txt_playerDuration.setText(sduration);
        System.out.println(currentSongIndex);
        //playSong(song.getResource());

        startAnimationMusic();
    }

    // format lại thời gian
    @SuppressLint("DefaultLocale")
    private String convertFormat(int duration) {
        return String.format("%02d:%02d",
                TimeUnit.SECONDS.toMinutes(duration),
                duration % 60);
    }


    // chạy animation cho quay vòng tròn
    private void startAnimationMusic() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                img_DetailImage.animate().rotationBy(360).withEndAction(this).setDuration(100000)
                        .setInterpolator(new LinearInterpolator()).start();
            }

        };
        img_DetailImage.animate().rotationBy(360).withEndAction(runnable).setDuration(100000)
                .setInterpolator(new LinearInterpolator()).start();
    }

    private void stopAnimationMusic() {
        img_DetailImage.animate().cancel();
    }

    // quản lý thanh seek bar
    private void seekBarmanager() {
        // gửi đi vị trí của thanh seekbar sau khi tua sang service
        seekBarTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                // i: vị trí hiện tại của seekbar
                // b: hành đồng có do người dùng thao tác hay do tác nhân khác, nếu là người dùn thì trả về yes
                if (b) {
                    //mediaPlayer.seekTo(i);
                    Intent intent = new Intent(DetailActivity.this, MyService.class);
                    intent.putExtra("actionMusicService", MyService.ACTION_SEEK);
                    intent.putExtra("position_seekbar", i);
                    startService(intent);

                    // seekBarTime.setProgress(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }


        });
    }

    private void Start_Service() {
        Intent MyIntent = new Intent(DetailActivity.this, MyService.class);
        Bundle bundle = new Bundle();
        bundle.putInt("curentIndex", currentSongIndex);
        bundle.putSerializable("song_list", (Serializable) songList);
        bundle.putBoolean("isPlaying", isPlaying);
        MyIntent.putExtras(bundle);
        startService(MyIntent);
    }

    private void Stop_Service() {
        Intent MyIntent = new Intent(this, Service.class);
        stopService(MyIntent);
    }

    // nhận data từ service
    private void SetDataForActivity() {
        setDataActivity = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("send_data_to_activity")) {
                    Bundle bundle = intent.getExtras();
                    if (bundle != null) {
                        currentSong = (Song) bundle.getSerializable("object_song");
//                        currentSongIndex = bundle.getInt("index", 0);
//                        songList = (List<Song>) bundle.getSerializable("song_list");
//                        currentSong = songList.get(currentSongIndex);
                        isPlaying = bundle.getBoolean("status_player");
                        int actionMusic = bundle.getInt("action_music");
                        setSongDetail(currentSong);
                        handLayoutMusic(actionMusic);

                    }
                }
            }
        };
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(setDataActivity, new IntentFilter("send_data_to_activity"));
    }

    // nhận vị trí hiên tại của bài hát từ service
    private void registerPositionReceiver() {
        positionReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(ACTION_UPDATE_POSITION)) {
                    int currentPosition = intent.getIntExtra("current_position", 0);
                    int mediaDuration = intent.getIntExtra("media_duration", 0);

                    // Xử lý dữ liệu vị trí hiện tại
                    seekBarTime.setProgress(currentPosition);
                    seekBarTime.setMax(mediaDuration);
                    Log.e("end mussic: ", mediaDuration - currentPosition + "");
//                    if(mediaDuration - currentPosition <= 0){
//                        sendActionToService(ACTION_STATUS);
//                    }
                    String formattedCurrentPosition = convertFormat(currentPosition / 1000); // Chuyển từ mili giây sang giây
                    txt_playerPosition.setText(formattedCurrentPosition);
                }
            }
        };

        LocalBroadcastManager.getInstance(this)
                .registerReceiver(positionReceiver, new IntentFilter(ACTION_UPDATE_POSITION));
    }

    private void AnimationManager() {
        Animation_manager = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(MyService.SETUP_ANIMATIONMUSIC)) {
                    int manager = intent.getIntExtra("animation_manager", 0);
                    switch (manager) {
                        case MyService.START_ANIMATION:
                            Log.e("start animation ", "call");
                            startAnimationMusic();
                            break;
                        case MyService.STOP_ANIMATION:
                            Log.e("stop animation ", "call");
                            stopAnimationMusic();
                            break;
                        case MyService.RESTART_ANIMATION:
                            Log.e("restart animation ", "call");
                            img_DetailImage.setRotation(0);
                            startAnimationMusic();
                            break;
                        default:
                    }

                }
            }
        };
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(Animation_manager, new IntentFilter(MyService.SETUP_ANIMATIONMUSIC));
    }

    private void sendActionToService(int action) {
        Intent intent = new Intent(this, MyService.class);
        intent.putExtra("actionMusicService", action);
        startService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(positionReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(setDataActivity);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(Animation_manager);
    }
}