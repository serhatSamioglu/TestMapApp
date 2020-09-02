package com.example.testmapapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.media.SoundPool;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListPhotosActivity extends AppCompatActivity {

    String tag;
    String name;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    ArrayList<Photo> photos;

    private SoundPool soundPool;
    int music;

    Boolean isFirstPhotoDownLoaded = false;
    Integer index = 0;

    ImageView listPhotoImageView;
    TextView listPhotoTextView;
    /*ArrayList<String> photosURLs;
    ListView listView;
    PhotosListAdapter photosListAdapter;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_photos);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            tag = bundle.getString("tag");
            name = bundle.getString("name");

        }
        setTitle(name);


        listPhotoImageView = findViewById(R.id.listPhotoImageView);
        listPhotoTextView = findViewById(R.id.listPhotoTextView);

        photos = new ArrayList<Photo>();
        /*listView = findViewById(R.id.photosListView);
        photosListAdapter = new PhotosListAdapter(photosURLs,this);
        listView.setAdapter(photosListAdapter);*/

        handlePhotosURLs();
        setSound();
    }

    private void handlePhotosURLs() {
        myRef.child("Photos").child(tag).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                photos.add(snapshot.getValue(Photo.class));

                if(!isFirstPhotoDownLoaded){
                    Picasso.get().load(photos.get(0).getUrl()).into(listPhotoImageView);
                    listPhotoTextView.setText(photos.get(0).getInformation());
                    isFirstPhotoDownLoaded = true;
                }
                //photosListAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setSound(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setMaxStreams(6)
                    .setAudioAttributes(audioAttributes)
                    .build();
            Log.d("ddddd", "ifff: ");
        } else {
            Log.d("ddddd", "else: ");

            soundPool = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);
        }

        music = soundPool.load(this, R.raw.mars, 1);

    }

    public void previousButtonTabbed(View view){
        soundPool.play(music, 1, 1, 0, 0, 1);

        if(index != 0){
            index--;
            Picasso.get().load(photos.get(index).getUrl()).into(listPhotoImageView);
            listPhotoTextView.setText(photos.get(index).getInformation());
        }
    }

    public void nextButtonTabbed(View view){
        if(index+1 != photos.size()){//sonuncu fotoğrafa bakmıyorsam ileri gidebilirim
            index++;
            Picasso.get().load(photos.get(index).getUrl()).into(listPhotoImageView);
            listPhotoTextView.setText(photos.get(index).getInformation());
        }
    }

    @Override
    protected void onDestroy() {
        soundPool.release();
        soundPool = null;

        super.onDestroy();

    }
}