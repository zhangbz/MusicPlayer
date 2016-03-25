package com.example.janiszhang.musicplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by janiszhang on 2016/3/24.
 */
public class MyAdapter extends ArrayAdapter<MusicData>{

    private Context mContext;
    private int mResource;
    private List mObjects;

    public MyAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
        this.mObjects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MusicData item = getItem(position);
        ViewHolder viewHolder;
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.music_list_view,null);
            viewHolder = new ViewHolder();
            viewHolder.musicName = (TextView) convertView.findViewById(R.id.music_name);
            viewHolder.singerName = (TextView) convertView.findViewById(R.id.singer_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.musicName.setText(item.getName());
        viewHolder.singerName.setText(item.getSinger());
        return convertView;
    }

    class ViewHolder {
        TextView musicName;
        TextView singerName;
    }
}
