package com.example.btl_android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int actionMusic = intent.getIntExtra("actionMusic", 0);
        int curentIndexSong = intent.getIntExtra("IndexSong",-1);


        // xử lý các action
        if(actionMusic != 0){
            Intent intentService = new Intent(context,MyService.class);
            intentService.putExtra("actionMusicService", actionMusic);
            System.out.println("action music receiver" + actionMusic);
            context.startService(intentService);
        }

    }
}
