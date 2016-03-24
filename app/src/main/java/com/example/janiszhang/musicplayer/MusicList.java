package com.example.janiszhang.musicplayer;

import java.util.ArrayList;

/**
 * Created by janiszhang on 2016/3/24.
 */
public class MusicList {

    private volatile static MusicList sMusicList;
    private final ArrayList<MusicData> mMusicDatas;

    private MusicList (){
        mMusicDatas = new ArrayList<>();
        mMusicDatas.add(new MusicData(R.raw.song1,"买还私奔的", "郝云"));
        mMusicDatas.add(new MusicData(R.raw.song2, "突然想到理想这个词", "郝云"));
        mMusicDatas.add(new MusicData(R.raw.song3, "要怎么办", "李柏凝"));
    }

    public static MusicList getMusicList() {
        if (sMusicList == null) {
            synchronized (MusicList.class) {
                if (sMusicList == null) {
                    sMusicList = new MusicList();
                }
            }
        }
        return sMusicList;
    }

    public ArrayList<MusicData> getList() {
        return mMusicDatas;
    }
    public MusicData getMusicData(int i) {
        return mMusicDatas.get(i);
    }
}
