package com.example.mylenovo.musicplayer;

import android.Manifest;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    String[] musics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.list_view);

        runtimePermission();
    }

    private void runtimePermission() {
       Dexter.withActivity(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).
                withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        displaySongs();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    public ArrayList<File> findMusics(File file){
        ArrayList<File> arrayList = new ArrayList<>();

        File[] files = file.listFiles();

        for(File file1: files){
            if(file1.isDirectory() && !file1.isHidden()){
                arrayList.addAll(findMusics(file1));
            }
            else{
                if(file1.getName().endsWith(".mp3") || file1.getName().endsWith(".wav")){
                    arrayList.add(file1);
                }
            }
        }
        return arrayList;
    }

    public void displaySongs(){
        final ArrayList<File> ar_list = findMusics(Environment.getExternalStorageDirectory());

        musics = new String[ar_list.size()];

        for(int i=0; i<ar_list.size(); i++){
            musics[i]=ar_list.get(i).getName().replace(".mp3", "").replace(".wav", "");
        }

        CustomAdapter customAdapter = new CustomAdapter(this, musics);
        listView.setAdapter(customAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), PlayerActivity.class);
                i.putExtra("songs", ar_list);
                i.putExtra("songName", musics[position]);
                i.putExtra("pos", position);

                startActivity(i);
            }
        });
    }
}
