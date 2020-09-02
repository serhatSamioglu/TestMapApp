package com.example.testmapapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

public class ListPhotoAndVideosActivity extends AppCompatActivity {

    ViewPager viewPager;//yana kaydırılan bölüm
    LinearLayout sliderDotspanel;//alttaki nokta kısmı
    TextView photoDetails;//fotoğraf açıklama yazısı
    TextView titleTextView;
    TextView detailTextView;

    RecyclerView recyclerView;
    VideoAdapter videoAdapter;
    Vector<YouTubeVideos> youtubeVideos = new Vector<YouTubeVideos>();
    LinearLayoutManager videoLinearLayoutManager = new LinearLayoutManager(this);

    private int dotscount;
    private ImageView[] dots;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    ArrayList<Photo> photos;
    ViewPagerAdapter viewPagerAdapter;

    String tag;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_photo_and_videos);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);
        photoDetails = (TextView) findViewById(R.id.photoDetails);
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        detailTextView = (TextView) findViewById(R.id.detailTextView);
        videoLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager( videoLinearLayoutManager);
        videoAdapter = new VideoAdapter(youtubeVideos);
        recyclerView.setAdapter(videoAdapter);


        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            tag = bundle.getString("tag");
            name = bundle.getString("name");

        }
        setTitle(name);

        photos = new ArrayList<Photo>();

        viewPagerAdapter = new ViewPagerAdapter(this,photos);//Adaptörü yarattı

        viewPager.setAdapter(viewPagerAdapter);//yana kaydırılan liste ile adatörü bağladık

        //fotoğraflar adaptörde olduğundan dolayı,
        // kaç tane fotoğraf olduğunu ordan öğreniyoruz


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for(int i = 0; i< dotscount; i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
                photoDetails.setText(photos.get(position).getInformation());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        checkPhotoChildCount();
        handlePhotosURLs();
        handleVideos();
        handleLocationDetail();


        /*try {
            playSong();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    /*private void playSong() throws IOException {
        Uri myUri = Uri.parse("https://mp3semti.com/Haluk-Levent-Izmir-Marsi.html"); // initialize Uri here
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(
                new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
        );
        mediaPlayer.setDataSource(getApplicationContext(), myUri);
        mediaPlayer.prepare();
        mediaPlayer.start();
    }*/

    private void handleLocationDetail() {
        myRef.child("Locations").child(tag).child("locationDetail").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                titleTextView.setText(name);
                detailTextView.setText(snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkPhotoChildCount() {
        myRef.child("Photos").child(tag).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dotscount = (int)snapshot.getChildrenCount();
                loadDots();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void loadDots(){
        dots = new ImageView[dotscount];//noktoların tutulacağı arrayi yarattık

        for(int i = 0; i < dotscount; i++){//boş noktaları yarattık

            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            sliderDotspanel.addView(dots[i], params);

        }

        //ilk noktayı aktif ettik
        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
    }

    private void handlePhotosURLs() {
        myRef.child("Photos").child(tag).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                photos.add(snapshot.getValue(Photo.class));
                photoDetails.setText(photos.get(0).getInformation());//ilk fotoğraf açıklamasını yazsın diye
                viewPagerAdapter.notifyDataSetChanged();
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