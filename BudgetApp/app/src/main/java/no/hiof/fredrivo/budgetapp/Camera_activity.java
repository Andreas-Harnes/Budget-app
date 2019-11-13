package no.hiof.fredrivo.budgetapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
//<<<<<<< Updated upstream
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
//=======
import android.support.annotation.NonNull;
//>>>>>>> Stashed changes
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Camera_activity extends AppCompatActivity {
// kilde https://developer.android.com/training/camera/photobasics#TaskPhotoView
    private Button Camerabtn;
    private ImageView imageView;
// <<<<<<< Updated upstream
    private static final int REQUEST_IMAGE = 1;
    StorageReference ref;
// =======
    private static final int CAMERA_REQUEST_CODE = 1;
   // private StorageReference ref;
// >>>>>>> Stashed changes
    private ProgressDialog progress;
    Uri photoURI;
    File image;

    String mCurrentPhotoPath;
    private File createImageFile() throws IOException {
        // lage fil for bilde
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",   /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

// <<<<<<< Updated upstream
        setContentView(R.layout.activity_camera_activity);
        Camerabtn = findViewById(R.id.camerashot);
        imageView = findViewById(R.id.imageV);

// =======
        ref = FirebaseStorage.getInstance().getReference("receipts");
        setContentView(R.layout.activity_camera_activity);
       // Uri file = ;
        Camerabtn = (Button) findViewById(R.id.camerashot);
        imageView = (ImageView) findViewById(R.id.imageV);
        progress = new ProgressDialog(this);
// >>>>>>> Stashed changes

        Camerabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// <<<<<<< Updated upstream
                //https://developer.android.com/training/camera/photobasics
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        photoURI = FileProvider.getUriForFile(getApplicationContext(), "no.hiof.fredrivo.budgetapp.fileprovider", photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE);
// =======
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                // intent.putExtra(MediaStore.EXTRA_OUTPUT,v.getContext());
                startActivityForResult(intent,CAMERA_REQUEST_CODE);
// >>>>>>> Stashed changes

                    }
                }
            }
        });
    }

    @Override
// <<<<<<< Updated upstream
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
// =======


    }

}}
