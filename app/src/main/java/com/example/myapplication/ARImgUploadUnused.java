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


public class ARImgUploadUnused extends AppCompatActivity implements View.OnClickListener{

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
        //uploadImage = (CardView) findViewById(R.id.uploadImage);

        uploadModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ARImgUploadUnused.this, Animal.class);
                //intent.putExtra("Uri", modelUri);
                //intent.putExtra("Image",bitmap);
                startActivity(intent);

//                uploadFinish = false;
//
//                Intent intent = new Intent();
//                intent.setType("application/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//
//                //To start a activity that will return result
//
//                startActivityForResult(intent,PICK_MODEL);
            }
        });

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                uploadFinish = false;
//
//                Intent intent = new Intent();
//                intent.setType("application/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//
//                //To start a activity that will return result
//
//                startActivityForResult(intent,PICK_MODEL);

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

    public void startIntent(){
        Intent intent = new Intent(ARImgUploadUnused.this, Animal.class);
        //intent.putExtra("Uri", modelUri);
        //intent.putExtra("Image",bitmap);
        startActivity(intent);

    }

    public void upload(){
        final StorageReference storageReference = storage.getReference();
        int counter;
        for(int i = 0; i < Imodels.size(); i++){

//            loading.setVisibility(View.VISIBLE);
//            loadingText.setVisibility(View.VISIBLE);
//            loadingCard.setVisibility(View.VISIBLE);

            final int finalI = i;

            storageReference.child("userModel/").child(Imodels.get(i).getName()).putFile(Imodels.get(i).getURI()).addOnCompleteListener(new
                OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            storageReference.child("userModel/").child(Imodels.get(finalI).getName()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {

                                    if(task.isSuccessful()){


                                        String input = task.getResult().toString();

                                        boolean isFound = (input.indexOf("gltf") !=- 1 || input.indexOf("glb") != -1 )? true: false;

                                        if(isFound){
                                            modelUri = task.getResult().toString();
                                            uploadFinish = true;
                                            Toast.makeText(ARImgUploadUnused.this, "Upload successful 1", Toast.LENGTH_SHORT).show();
                                        }

                                        //start intent here
//                                         if(uploadFinish) {
//                                             startIntent();
//                                         }

                                        Log.d("FIREBASE", task.getResult().toString());

                                    }else{

                                        storageReference.child("userData/").child(Imodels.get(finalI).getName()).delete();
                                        Toast.makeText(ARImgUploadUnused.this, "Coudn't save" + Imodels.get(finalI).getName(), Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });
                        }else{
                            Toast.makeText(ARImgUploadUnused.this, "Coudn't save file", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            );
        }
    }


    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case PICK_MODEL:

                if (resultCode == RESULT_OK && data != null) {

                    ClipData clipdata = data.getClipData();

                    if (clipdata != null) {

                        for (int i = 0; i < clipdata.getItemCount(); i++) {

                            Uri uri = clipdata.getItemAt(i).getUri();

                            Model fetchModels = new Model(getFileNameFromUri(uri), uri);

                            Imodels.add(fetchModels);

                            //Toast.makeText(MainActivity.this, "Added multiple files", Toast.LENGTH_SHORT).show();
                            upload();

                            Log.d("Filename", fetchModels.getName());
                        }

                    } else {
                        Uri uri = data.getData();
                        Imodels.add(new Model(getFileNameFromUri(uri), uri));
                        upload();

                        Log.d("Uri added", getFileNameFromUri(uri));
                    }

                }

            case PICK_IMAGE:

                if (resultCode == RESULT_OK && data != null) {
                    uri = data.getData();
                    try{
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);

                    }catch (IOException e){
                        Toast.makeText(ARImgUploadUnused.this, "Upload Images fail", Toast.LENGTH_SHORT).show();
                    }
                }


        }
    }

    public String getFileNameFromUri(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = this.getBaseContext().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }


}
