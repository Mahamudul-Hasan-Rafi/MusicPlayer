package com.example.mylenovo.musicplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

class CustomAdapter extends BaseAdapter {

    Context context;
    String[] songs;
    public CustomAdapter(Context context, String[] musics) {
        this.context=context;
        this.songs=musics;
    }

    @Override

    public int getCount() {
        return songs.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.music, parent, false);
        }
        TextView textView = convertView.findViewById(R.id.text);
        textView.setText(songs[position]);

        return convertView;
    }
}
