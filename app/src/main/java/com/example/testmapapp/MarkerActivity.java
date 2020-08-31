package com.example.testmapapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;

public class MarkerActivity extends FragmentActivity implements
        GoogleMap.OnMarkerClickListener, OnMapReadyCallback {

//    private static final LatLng PERTH = new LatLng(-31.952854, 115.857342);
//    private static final LatLng SYDNEY = new LatLng(-33.87365, 151.20689);
//    private static final LatLng BRISBANE = new LatLng(-27.47093, 153.0235);
    private static final LatLng IZMIR = new LatLng(38, 27);

    AlertDialog VideoURLAlertDialog;
    AlertDialog NameAlertDialog;

    LatLng tempLatLng;
    String tempLocationName;
    Integer tempPhotoCount = 0;
    Integer tempVideoCount = 0;
    Uri imageData;
    Integer locationCount;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference = firebaseStorage.getReference();

//    private Marker mPerth;
//    private Marker mSydney;
//    private Marker mBrisbane;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map3);
        mapFragment.getMapAsync(this);

        checkLocationCount();
        handleVideoURLAlertDialog();
        handleNameAlertDialog();

    }

    /** Called when the map is ready. */
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {
                tempLatLng = latLng;
                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();

                // Setting the position for the marker
                markerOptions.position(latLng);

                // Setting the title for the marker.
                // This will be displayed on taping the marker
                markerOptions.title(latLng.latitude + " : " + latLng.longitude);

                // Clears the previously touched position
                mMap.clear();

                // Animating to the touched position
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                // Placing a marker on the touched position
                mMap.addMarker(markerOptions);
            }
        });
        // Add some markers to the map, and add a data object to each marker.
//        mPerth = mMap.addMarker(new MarkerOptions()
//                .position(PERTH)
//                .title("Perth"));
//        mPerth.setTag(3);
//
//        mSydney = mMap.addMarker(new MarkerOptions()
//                .position(SYDNEY)
//                .title("Sydney"));
//        mSydney.setTag(1);
//
//        mBrisbane = mMap.addMarker(new MarkerOptions()
//                .position(BRISBANE)
//                .title("Brisbane"));
//        mBrisbane.setTag(2);

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(IZMIR,8));

        // Set a listener for marker click.
        mMap.setOnMarkerClickListener(this);
    }

    /** Called when the user clicks a marker. */
    @Override
    public boolean onMarkerClick(final Marker marker) {
        // Retrieve the data from the marker.
        Integer clickCount = (Integer) marker.getTag();

        // Check if a click count was set, then display the click count.
//        if (clickCount != null) {
//            clickCount = clickCount + 1;
//            marker.setTag(clickCount);
//            Toast.makeText(this,
//                    marker.getTitle() +
//                            " has been clicked " + clickCount + " times.",
//                    Toast.LENGTH_SHORT).show();
//
//        }

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

    public void confirmButton(View view){

        Location location = new Location(tempLatLng.latitude,tempLatLng.longitude,locationCount,tempLocationName);
        myRef.child("Locations").child(locationCount.toString()).setValue(location);
        finish();

    }

    private void checkLocationCount() {
        myRef.child("locationCount").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                locationCount = snapshot.getValue(Integer.class) + 1;
                myRef.child("locationCount").setValue(locationCount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void openStorageTabbed(View view){
        //checkPermissions();
        Intent intent = new Intent(getApplicationContext(), PhotoPickerActivity.class);
        intent.putExtra("locationCount",locationCount);
        startActivity(intent);
    }

    public void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},1);
        } else {
            Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intentToGallery,2);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentToGallery,2);
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 2 && resultCode == RESULT_OK && data != null ) {

            imageData = data.getData();

            storageReference.child("Photos").child(locationCount.toString())
                    .child(tempPhotoCount.toString()).putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.child("Photos").child(locationCount.toString())
                            .child(tempPhotoCount.toString()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            myRef.child("Photos").child(locationCount.toString())
                                    .child(tempPhotoCount.toString()).setValue(uri.toString());
                            tempPhotoCount++;
                        }
                    });
                }
            });

//            try {
//
//                if (Build.VERSION.SDK_INT >= 28) {
//                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(),imageData);
//                    selectedImage = ImageDecoder.decodeBitmap(source);
//                } else {
//                    selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageData);
//                }
//
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void openAlertTabbed(View view){
        VideoURLAlertDialog.show();
    }

    public void openNameAlertTabbed(View view){
        NameAlertDialog.show();
    }

    public void handleVideoURLAlertDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add a video URL");

        final EditText VideoInputView = new EditText(this);
        VideoInputView.setHint("Tap to write");
        builder.setView(VideoInputView);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                uploadDatabase(VideoInputView.getText().toString());
                VideoInputView.getText().clear();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                VideoInputView.getText().clear();
                dialog.dismiss();
            }
        });

        VideoURLAlertDialog = builder.create();
    }

    public void uploadDatabase(String url){
        myRef.child("Videos").child(locationCount.toString()).child(tempVideoCount.toString()).setValue(url);
        tempVideoCount++;
    }

    public void handleNameAlertDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add a location name");

        final EditText nameInputView = new EditText(this);
        nameInputView.setHint("Tap to write");
        builder.setView(nameInputView);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tempLocationName = nameInputView.getText().toString();
                nameInputView.getText().clear();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                nameInputView.getText().clear();
                dialog.dismiss();
            }
        });

        NameAlertDialog = builder.create();
    }
}