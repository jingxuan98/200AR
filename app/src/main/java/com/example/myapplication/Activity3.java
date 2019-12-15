package com.example.myapplication;

import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Activity3 extends AppCompatActivity implements View.OnClickListener{

    CardView uploadImage,uploadModel;
    boolean uploadFinish;
    private final int PICK_MODEL = 72;
    private final int PICK_IMAGE = 73;
    String modelUri;
    String imageUri;
    Uri uri;
    List<Model> Imodels = new ArrayList<>();
    FirebaseStorage storage;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.arimage);

        storage = FirebaseStorage.getInstance();

        uploadModel = (CardView) findViewById(R.id.uploadModel);
        uploadImage = (CardView) findViewById(R.id.uploadImage);

        uploadModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Activity3.this, Activity4.class);
                //intent.putExtra("Uri", modelUri);
                //intent.putExtra("Image",bitmap);
                startActivity(intent);
            }
        });

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadFinish = false;

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                //intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

                //To start a activity that will return result

                startActivityForResult(intent,PICK_MODEL);
            }
        });

    }

    @Override
    public void onClick(View v){

    }


}
