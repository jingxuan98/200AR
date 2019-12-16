package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.view.View;

import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;


public class arimgupload extends AppCompatActivity implements View.OnClickListener{

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

        uploadModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(arimgupload.this, Animal.class);
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
