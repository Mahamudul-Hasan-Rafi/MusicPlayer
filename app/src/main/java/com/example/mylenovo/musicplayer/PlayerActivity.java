package com.example.mylenovo.musicplayer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {

    ImageButton play, pause, next, prev;
    SeekBar seekBar;
    TextView textView;
    static MediaPlayer mediaPlayer;
    Thread updateSeekbar;
    int currentPos, totalDuration, position;
    ArrayList<File> musics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        pause = findViewById(R.id.imageButton);
        prev = findViewById(R.id.imageButton2);
        next = findViewById(R.id.imageButton3);

        seekBar = findViewById(R.id.seekBar);

        textView = findViewById(R.id.textView);

        getSupportActionBar().setTitle("Now Playing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        updateSeekbar = new Thread(){
            @Override
            public void run() {
                totalDuration=mediaPlayer.getDuration();
                currentPos=0;

                while(currentPos<totalDuration){
                    try {
                        sleep(500);
                        currentPos=mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentPos);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        };

        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();

            mediaPlayer=null;
        }

        Intent i = getIntent();
        Bundle b = i.getExtras();

        musics = (ArrayList)b.getParcelableArrayList("songs");
        String music_name = i.getStringExtra("songName");
        position = b.getInt("pos");

        textView.setText(music_name);
        textView.setSelected(true);

        Uri uri = Uri.parse(musics.get(position).toString());

        mediaPlayer = MediaPlayer.create(this, uri);
        mediaPlayer.start();

        seekBar.setMax(mediaPlayer.getDuration());
        updateSeekbar.start();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    pause.setImageResource(R.drawable.ic_play);
                }
                else{
                    mediaPlayer.start();
                    pause.setImageResource(R.drawable.ic_pause);
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();

                position = (position+1)%musics.size();
                Uri uri1 = Uri.parse(musics.get(position).toString());

                String song_name = musics.get(position).getName().toString();
                textView.setText(song_name);

                mediaPlayer=MediaPlayer.create(getApplicationContext(), uri1);
                mediaPlayer.start();
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();

                position = (position-1)<0?musics.size()-1:position-1;
                Uri uri1 = Uri.parse(musics.get(position).toString());

                String song_name = musics.get(position).getName().toString();
                textView.setText(song_name);

                mediaPlayer=MediaPlayer.create(getApplicationContext(), uri1);
                mediaPlayer.start();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
