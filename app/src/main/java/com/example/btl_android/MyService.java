package com.example.btl_android;

import static com.example.btl_android.MyApplication.CHANNEL_ID;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;

import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;

import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.btl_android.Activity.DetailActivity;
import com.example.btl_android.modal.Song;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;


public class MyService extends Service {
    private MediaPlayer mediaPlayer;
    public static final int ACTION_PAUSE = 1, ACTION_RESUME = 2, ACTION_NEXT = 3, ACTION_PRE = 4, ACTION_START = 6, ACTION_REPLAY = 11, ACTION_AUTONEXT = 12;
    public static final int ACTION_SEEK = 5, ACTION_DESTROY = 10;
    public static final String ACTION_UPDATE_POSITION = "UPDATE_POSITION", SETUP_ANIMATIONMUSIC = "SETUP_ANIMATION";
    public static final int START_ANIMATION = 7, STOP_ANIMATION = 8, RESTART_ANIMATION = 9;
    int curentPosition;
    private boolean isPlayMusic = true, isReadyToPlay = false;
    private Song msong, oldSong;
    private List<Song> songList;
    private int crentIndex;

    @Override
    public void onCreate() {
        Log.e("nguyen", "My service start");


        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Bundle bundle = intent.getExtras();
        Song song;
        if (bundle != null) {

            List<Song> tmpSongList = (List<Song>) bundle.get("song_list");
            System.out.println("ok bundle");
            if (tmpSongList != null) {
                songList = tmpSongList;
                crentIndex = bundle.getInt("curentIndex");
                song = songList.get(crentIndex);
                msong = song;
                startMusic(msong);
                mediaPlayer.setLooping(true);
                sendNotification(msong);
            } else {
                System.out.println("song list null");
            }
        } else {
            System.out.println("bundle null");
        }

        int actionMusic = intent.getIntExtra("actionMusicService", 0);
        curentPosition = intent.getIntExtra("position_seekbar", 0);
        System.out.println("action music service" + actionMusic);
        handActionMusic(actionMusic);
        updateSeekbar();


        if(mediaPlayer != null) {
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    // Thực hiện hành động khi bài hát kết thúc

                    nextMusic();

                }
            });
        }

        return START_NOT_STICKY;
    }


    private void updateSeekbar() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mediaPlayer != null) {
                    try {
                        if(mediaPlayer.isPlaying()){
                            Intent broadcastIntent = new Intent(ACTION_UPDATE_POSITION);
                            broadcastIntent.putExtra("current_position", mediaPlayer.getCurrentPosition());
                            broadcastIntent.putExtra("media_duration", mediaPlayer.getDuration());
                            LocalBroadcastManager.getInstance(MyService.this).sendBroadcast(broadcastIntent);
                        }
                        Thread.sleep(1000); // Ngủ trong 1 giây

                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        }).start();


    }


    private void startMusic(Song song) {
        if (oldSong != null) {
            if (oldSong.getId().equals(song.getId())) {
                return;
            }
        }

        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        } else {
            mediaPlayer.reset();
        }




        oldSong = song;
        String songUrl = song.getSource(); // Thay đổi với URL của bạn

        try {
            mediaPlayer.setDataSource(songUrl);
            mediaPlayer.prepareAsync(); // Use prepareAsync for streaming from internet
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start(); // Bắt đầu phát nhạc khi sẵn sàng
                    isPlayMusic = true;
                    isReadyToPlay = true;
                    Log.d("start music ", "jdjdj");
                    sendActiontoActivity(ACTION_START);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void handActionMusic(int action) {
        switch (action) {
            case ACTION_PAUSE:
                System.out.println("pause 1");
                pauseMusic();
                break;
            case ACTION_RESUME:
                resumeMusic();
                break;
            case ACTION_NEXT:
                nextMusic();
                break;
            case ACTION_PRE:
                preMusic();
                break;
            case ACTION_SEEK:
                ActionSeek();
                break;
            case ACTION_DESTROY:
                onDestroy();
                break;
            case ACTION_REPLAY:
                if(mediaPlayer != null){
                    mediaPlayer.setLooping(true);
                }
                break;
            case ACTION_AUTONEXT:
                mediaPlayer.setLooping(false);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        nextMusic();
                    }
                });
                break;
            default:

        }
    }


    private void ActionSeek() {
        mediaPlayer.seekTo(curentPosition);
    }

    private void preMusic() {
        if (crentIndex > 0) {
            crentIndex = (crentIndex - 1) % songList.size();
            msong = songList.get(crentIndex);
            if (msong != null) {
                isPlayMusic = true;
                sendNotification(msong);
                startMusic(msong);
                sendActiontoActivity(ACTION_PRE);
                sendAationtoDetailActivity(RESTART_ANIMATION);

                Intent broadcastIntent = new Intent(ACTION_UPDATE_POSITION);
                broadcastIntent.putExtra("current_position", 0);
                broadcastIntent.putExtra("media_duration", msong.getDuration());
                LocalBroadcastManager.getInstance(MyService.this).sendBroadcast(broadcastIntent);

            } else {
                System.out.println("Null roiif ");
            }
        }

    }

    private void nextMusic() {

        if (crentIndex < songList.size()) {
            crentIndex = (crentIndex + 1) % songList.size();
            Song newsong = songList.get(crentIndex);
            if (newsong != null) {
                isPlayMusic = true;
                sendNotification(newsong);
                msong = songList.get(crentIndex);
                startMusic(msong);
                sendActiontoActivity(ACTION_NEXT);
                sendAationtoDetailActivity(RESTART_ANIMATION);

                Intent broadcastIntent = new Intent(ACTION_UPDATE_POSITION);
                broadcastIntent.putExtra("current_position", 0);
                broadcastIntent.putExtra("media_duration", msong.getDuration());
                LocalBroadcastManager.getInstance(MyService.this).sendBroadcast(broadcastIntent);

            }
        } else {
            Log.i("songlist", "jkkj");
        }

    }

    private void resumeMusic() {
        if (mediaPlayer != null && !isPlayMusic) {
            mediaPlayer.start();
            isPlayMusic = true;
            sendNotification(msong);
            System.out.println("ressum ok");
            sendActiontoActivity(ACTION_RESUME);
            sendAationtoDetailActivity(START_ANIMATION);
        } else {
            System.out.println("ressum not ok" + isPlayMusic);
        }

    }

    private void pauseMusic() {
        System.out.println(mediaPlayer);
        System.out.println(isPlayMusic);
        if (mediaPlayer != null && isPlayMusic) {
            mediaPlayer.pause();

            System.out.println("pause ok");
            if (msong != null) {
                isPlayMusic = false;
                sendNotification(msong);

            } else {
                Log.e("nguyen", "Song object is null, cannot send notification.");
            }
            sendActiontoActivity(ACTION_PAUSE);
            sendAationtoDetailActivity(STOP_ANIMATION);


        } else {
            System.out.println("pause not ok" + isPlayMusic);
        }
    }

    private void sendNotification(@NonNull Song song) {
        Intent intent = new Intent(this, DetailActivity.class);
        Bundle bundle = new Bundle();
        int k = isPlayMusic ? 1 : 2;
        bundle.putSerializable("curSong", song);
        bundle.putSerializable("SuggestSong", (Serializable) songList);
        bundle.putInt("index", crentIndex);
        bundle.putInt("k", k);
        intent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        String imageUrl = song.getImage();
////        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_custom_notification);
////        remoteViews.setImageViewResource(R.id.img_play_or_pause, R.drawable.baseline_pause_circle_outline_24);
////        if (isPlayMusic) {
////            remoteViews.setOnClickPendingIntent(R.id.img_play_or_pause, getPendingIntent(this, ACTION_RESUME));
////            remoteViews.setImageViewResource(R.id.img_play_or_pause, R.drawable.baseline_play_circle_outline_24);
////        } else {
////            remoteViews.setOnClickPendingIntent(R.id.img_play_or_pause, getPendingIntent(this, ACTION_PAUSE));
////            remoteViews.setImageViewResource(R.id.img_play_or_pause, R.drawable.baseline_pause_circle_outline_24);
////        }
////
////        remoteViews.setOnClickPendingIntent(R.id.img_next, getPendingIntent(this, ACTION_NEXT));
////        remoteViews.setOnClickPendingIntent(R.id.imgv_pre, getPendingIntent(this, ACTION_PRE));
//
//
//        Glide.with(this)
//                .asBitmap()
//                .load(imageUrl)
//                .into(new CustomTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                        // Sử dụng Bitmap đã tải về
//
//                        remoteViews.setTextViewText(R.id.txt_titleSong, song.getTitle());
//                        remoteViews.setTextViewText(R.id.txt_singerSong, song.getArtist());
//                        remoteViews.setImageViewBitmap(R.id.imgSong, resource);
//                        Log.d("Notification", "Image loaded successfully");
//                        // goiij notification khi tải xong ảnh
//                        Notification notification = new NotificationCompat.Builder(MyService.this, CHANNEL_ID)
//                                .setSmallIcon(R.drawable.baseline_music_note_24)
//                                .setContentIntent(pendingIntent)
//                                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
//                                .setCustomBigContentView(remoteViews)
//                                .build();
//
//                        startForeground(1, notification);
//                    }
//
//                    @Override
//                    public void onLoadCleared(@Nullable Drawable placeholder) {
//                        // Xử lý nếu ảnh không tải được
//                        Log.d("Notification", "Image loaded fall");
//                    }
//                });
        // tạo mediasession compat
        MediaSessionCompat mediaSessionCompat = new MediaSessionCompat(this, "tag");


        // Sử dụng Glide để tải ảnh từ URL
        Glide.with(this)
                .asBitmap()
                .load(imageUrl)
                .into(new CustomTarget<Bitmap>() {
                    @SuppressLint("ForegroundServiceType")
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        // Khi ảnh được tải thành công, tạo thông báo
                        Notification notification = new NotificationCompat.Builder(MyService.this, CHANNEL_ID)
                                .setSmallIcon(R.drawable.baseline_music_note_24)
                                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                                .setLargeIcon(resource)  // Đặt ảnh tải về làm large icon
                                // Add media control buttons that invoke intents in your media service
                                .addAction(R.drawable.baseline_skip_previous_24, "Previous", getPendingIntent(MyService.this, ACTION_PRE)) // #0
                                .addAction(isPlayMusic ? R.drawable.baseline_pause_circle_outline_24 : R.drawable.baseline_play_circle_outline_24,
                                        isPlayMusic ? "Pause" : "Play",
                                        getPendingIntent(MyService.this, isPlayMusic ? ACTION_PAUSE : ACTION_RESUME)) // #1
                                .addAction(R.drawable.baseline_skip_next_24, "Next", getPendingIntent(MyService.this, ACTION_NEXT)) // #2
                                // Apply the media style template.
                                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                                        .setShowActionsInCompactView(1)
                                        .setMediaSession(mediaSessionCompat.getSessionToken())
                                )

                                .setContentTitle(song.getTitle())
                                .setContentText(song.getArtist())
                                .setSound(null)  // Tắt âm mặc định của notification
                                .setContentIntent(pendingIntent)
                                .build();

                        // Hiển thị thông báo trong chế độ foreground
                        startForeground(1, notification);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        // Xử lý khi ảnh bị xóa hoặc không tải được
                    }
                });


    }


    private PendingIntent getPendingIntent(Context context, int action) {
        Intent intent = new Intent(this, MyReceiver.class);
        intent.putExtra("actionMusic", action);
        return PendingIntent.getBroadcast(context.getApplicationContext(), action, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
    }

    private void sendActiontoActivity(int action) {
        Intent intent = new Intent("send_data_to_activity");
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_song", msong);
        bundle.putSerializable("list_song", (Serializable) songList);
        bundle.putInt("index", crentIndex);
        bundle.putBoolean("status_player", isPlayMusic);
        bundle.putInt("action_music", action);
        intent.putExtras(bundle);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void sendAationtoDetailActivity(int action) {
        Intent intent = new Intent(SETUP_ANIMATIONMUSIC);
        intent.putExtra("animation_manager", action);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }





    @Override
    public void onDestroy() {
        Log.e("Nguyen", "My service stop");
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        stopForeground(true);  // Tắt thông báo
        stopSelf();  // Dừng dịch vụ
        super.onDestroy();
    }

}

