package com.example.testmapapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class PhotoPickerActivity extends AppCompatActivity {

    Integer tempPhotoCount = 0;
    Uri imageData;
    String downloadUri;
    Integer locationCount;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference = firebaseStorage.getReference();

    ImageView pickedPhoto;
    EditText pickedPhotoDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_picker);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            locationCount = bundle.getInt("locationCount");
        }

        pickedPhoto = findViewById(R.id.pickedPhoto);
        pickedPhotoDetails = findViewById(R.id.pickedPhotoDetails);

        pickedPhoto.setImageResource(R.drawable.touch_app_24);

        pickedPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissions();
            }
        });

    }

    public void pickedPhotoConfirmButton(View view){
        Photo photo = new Photo(tempPhotoCount,downloadUri,pickedPhotoDetails.getText().toString());

        myRef.child("Photos").child(locationCount.toString())
                .child(tempPhotoCount.toString()).setValue(photo);
        pickedPhoto.setImageResource(R.drawable.touch_app_24);
        pickedPhotoDetails.setText("");
        tempPhotoCount++;
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
                            downloadUri = uri.toString();
                            Picasso.get().load(uri.toString()).into(pickedPhoto);
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
}