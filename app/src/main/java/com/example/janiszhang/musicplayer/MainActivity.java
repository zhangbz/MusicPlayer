package com.example.janiszhang.musicplayer;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
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
    private int mIndex = 0;
    private Messenger mMessenger;

    public static final int CODE = 1;
    private MusicService.MyBinder mMyBinder;
    private  Messenger mActivityMessenger = new Messenger(new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CODE:
                    mIndex = msg.arg1;
                    setMusicNameAndSingerName(mIndex);
                    if(msg.arg2 == 1) {
                        mPlayBtn.setBackgroundResource(R.drawable.desk_pause);
                    } else {
                        mPlayBtn.setBackgroundResource(R.drawable.desk_play);
                    }
            }
        }
    });



    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("zhangbz", "onServiceConnected");
//            mMyBinder = (MusicService.MyBinder) service;
//            setMusicNameAndSingerName();
            mMessenger = new Messenger(service);

            //注意这里紧接着要send一次
            Message message = Message.obtain();
            message.replyTo = mActivityMessenger;
            try {
                mMessenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
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

        setMusicNameAndSingerName(mIndex);

        if(mMessenger != null) {
            Message message = Message.obtain(null, 0);
            try {
                mMessenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mPlayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mMyBinder.play();
//                if(isPlaying) {
//                    mPlayBtn.setBackgroundResource(R.drawable.desk_play);
//                    isPlaying = false;
//                } else {
//                    mPlayBtn.setBackgroundResource(R.drawable.desk_pause);
//                    isPlaying = true;
//                }
//                setMusicNameAndSingerName();
                if(isPlaying) {
                    if(mMessenger != null) {
                        Message message = Message.obtain(null, 1);//?????第三个参数的问题
                        message.arg1 = 1;
                        message.arg2 = mIndex;
                        message.replyTo = mActivityMessenger;
                        try {
                            mMessenger.send(message);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    mPlayBtn.setBackgroundResource(R.drawable.desk_play);
                    isPlaying = false;
                } else {
                    if(mMessenger != null) {
                        Message message = Message.obtain(null, 1);
                        message.arg1 = 0;
                        message.arg2 = mIndex;
                        message.replyTo = mActivityMessenger;
                        try {
                            mMessenger.send(message);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    mPlayBtn.setBackgroundResource(R.drawable.desk_pause);
                    isPlaying = true;
                }
            }
        });
        mPrevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mMyBinder.prev();
//                setMusicNameAndSingerName();
                mIndex = (mIndex-1)<0?mMusicDatas.size()-1:mIndex-1;
                setMusicNameAndSingerName(mIndex);
                if(mMessenger != null) {
                    Message message = Message.obtain(null, 0);
                    message.arg1 =mIndex;
                    message.replyTo = mActivityMessenger;
                    try {
                        mMessenger.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mMyBinder.next();
//                setMusicNameAndSingerName();
                mIndex = (mIndex+1)>=mMusicDatas.size()? 0 : mIndex+1;
                setMusicNameAndSingerName(mIndex);
                if(mMessenger != null) {
                    Message message = Message.obtain(null, 0);
                    message.arg1 = mIndex;
                    message.replyTo = mActivityMessenger;
                    try {
                        mMessenger.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

//    private void setMusicNameAndSingerName() {
//        mMusicIndex = mMyBinder.getMusicIndex();
//        mMusicName.setText(mMusicDatas.get(mMusicIndex).getName());
//        mSingerName.setText(mMusicDatas.get(mMusicIndex).getSinger());
//    }

    private void setMusicNameAndSingerName(int index) {
        mMusicName.setText(mMusicDatas.get(index).getName());
        mSingerName.setText(mMusicDatas.get(index).getSinger());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("zhangbz", "MainActivity onDestory");
    }
}
