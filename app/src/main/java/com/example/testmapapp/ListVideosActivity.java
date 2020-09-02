package com.example.testmapapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Vector;

public class ListVideosActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    VideoAdapter videoAdapter;
    Vector<YouTubeVideos> youtubeVideos = new Vector<YouTubeVideos>();
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    String tag;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_videos);
        setTitle(name);

        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        recyclerView = (RecyclerView) findViewById(R.id.videosRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager( linearLayoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {


                Log.d("seeee", "1: "+linearLayoutManager.findFirstCompletelyVisibleItemPosition());
                Log.d("seeee", "2: "+linearLayoutManager.findLastCompletelyVisibleItemPosition());
                Log.d("seeee", "3: "+linearLayoutManager.findFirstVisibleItemPosition());
                Log.d("seeee", "4: "+linearLayoutManager.findLastVisibleItemPosition());

                super.onScrollStateChanged(recyclerView, newState);
            }
        });


        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            tag = bundle.getString("tag");
            name = bundle.getString("name");

        }


//        youtubeVideos.add( new YouTubeVideos("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/eWEF1Zrmdow\" frameborder=\"0\" allowfullscreen></iframe>") );
//        youtubeVideos.add( new YouTubeVideos("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/KyJ71G2UxTQ\" frameborder=\"0\" allowfullscreen></iframe>") );
//        youtubeVideos.add( new YouTubeVideos("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/y8Rr39jKFKU\" frameborder=\"0\" allowfullscreen></iframe>") );
//        youtubeVideos.add( new YouTubeVideos("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/8Hg1tqIwIfI\" frameborder=\"0\" allowfullscreen></iframe>") );
//        youtubeVideos.add( new YouTubeVideos("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/uhQ7mh_o_cM\" frameborder=\"0\" allowfullscreen></iframe>") );

        videoAdapter = new VideoAdapter(youtubeVideos);

        recyclerView.setAdapter(videoAdapter);

        handleVideos();
    }

    private void handleVideos() {
        myRef.child("Videos").child(tag).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                youtubeVideos.add(new YouTubeVideos("<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/"+snapshot.getValue(String.class)+"\" frameborder=\"0\" allowfullscreen></iframe>"));
                videoAdapter.notifyDataSetChanged();
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
}