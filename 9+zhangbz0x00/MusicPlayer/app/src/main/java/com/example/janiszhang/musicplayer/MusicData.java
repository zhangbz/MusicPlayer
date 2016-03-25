package com.example.janiszhang.musicplayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by janiszhang on 2016/3/24.
 */
public class MusicData {
    private int src;
    private String name;
    private String singer;

    public MusicData(int src, String name, String singer) {
        this.src = src;
        this.name = name;
        this.singer = singer;
    }

    public int getSrc() {
        return src;
    }

    public void setSrc(int src) {
        this.src = src;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }
}
