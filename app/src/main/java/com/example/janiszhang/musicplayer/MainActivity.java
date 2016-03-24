package com.example.janiszhang.musicplayer;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView mMusicListlv;
    private TextView mMusicName;
    private TextView mSingerName;
    private ImageButton mNextBtn;
    private ImageButton mPlayBtn;
    private ImageButton mPrevBtn;
    private MusicList mMusicListData;
    private ArrayList<MusicData> mMusicDatas;
    private MyAdapter mMyAdapter;
    private int mMusicIndex;
    private boolean isPlaying = false;

    private MusicService.MyBinder mMyBinder;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("zhangbz", "onServiceConnected");
            mMyBinder = (MusicService.MyBinder) service;
            setMusicNameAndSingerName();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i("zhangbz", "onServiceDisconnected");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("zhangbz", "MainActivity onCreate");
        //bind service
        Intent intent = new Intent(this, MusicService.class);
//        startService(intent);
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);

        //init listview
        mMusicListlv = (ListView) findViewById(R.id.lv_music);
        mMusicListData = MusicList.getMusicList();
        mMusicDatas = mMusicListData.getList();
        mMyAdapter = new MyAdapter(MainActivity.this, 0, mMusicDatas);
        mMusicListlv.setAdapter(mMyAdapter);


        mMusicName = (TextView) findViewById(R.id.music_name);
        mSingerName = (TextView) findViewById(R.id.singer_name);
        mNextBtn = (ImageButton) findViewById(R.id.btn_next);
        mPlayBtn = (ImageButton) findViewById(R.id.btn_play);
        mPrevBtn = (ImageButton) findViewById(R.id.btn_prev);

        mPlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyBinder.play();
                if(isPlaying) {
                    mPlayBtn.setBackgroundResource(R.drawable.desk_play);
                    isPlaying = false;
                } else {
                    mPlayBtn.setBackgroundResource(R.drawable.desk_pause);
                    isPlaying = true;
                }
                setMusicNameAndSingerName();
            }
        });
        mPrevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyBinder.prev();
                setMusicNameAndSingerName();
            }
        });
        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyBinder.next();
                setMusicNameAndSingerName();
            }
        });

    }

    private void setMusicNameAndSingerName() {
        mMusicIndex = mMyBinder.getMusicIndex();
        mMusicName.setText(mMusicDatas.get(mMusicIndex).getName());
        mSingerName.setText(mMusicDatas.get(mMusicIndex).getSinger());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("zhangbz", "MainActivity onDestory");
    }
}
