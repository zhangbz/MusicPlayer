package com.example.janiszhang.musicplayer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by janiszhang on 2016/3/24.
 */
public class MusicService extends Service{

    private MyBinder mMyBinder = new MyBinder();
    private MusicList mMusicList;
    private ArrayList<MusicData> mMusicDatas;
    private int i = 0;
    private MediaPlayer mMediaPlayer;

    private boolean isPlaying = false;

//    Messenger mMessenger = new Messenger(new IncomingHandler());
//
//    class IncomingHandler extends Handler {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case 0:
//                    //
//                    break;
//            }
//        }
//    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("zhangbz", "service onBind");
        return mMyBinder;
//        return mMessenger.getBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mMusicList = MusicList.getMusicList();
        mMusicDatas = mMusicList.getList();
        mMediaPlayer = MediaPlayer.create(this, mMusicDatas.get(i).getSrc());
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //next;
                mMyBinder.next();
            }
        });
        Log.i("zhangbz", "service onCreate");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("zhangbz", "service onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("zhangbz", "service onDestroy");
    }

    class MyBinder extends Binder {

        public void next() {
            i = (i+1)>=mMusicDatas.size()? 0 : i+1;
            mMediaPlayer.stop();
            mMediaPlayer = MediaPlayer.create(MusicService.this, mMusicDatas.get(i).getSrc());
            if(isPlaying) {
                mMediaPlayer.start();
            }
        }

        public void prev() {
            i = (i-1)<0?mMusicDatas.size()-1:i-1;
            mMediaPlayer.stop();
            mMediaPlayer = MediaPlayer.create(MusicService.this, mMusicDatas.get(i).getSrc());
            if(isPlaying) {
                mMediaPlayer.start();
            }
        }

        public void play() {
            if(mMediaPlayer.isPlaying()){
                isPlaying =false;
                mMediaPlayer.pause();
            } else {
                isPlaying = true;
                mMediaPlayer.start();
            }
        }

        public int getMusicIndex() {
            return i;
        }
    }
}
