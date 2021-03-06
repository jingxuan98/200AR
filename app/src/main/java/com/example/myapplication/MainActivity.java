package com.example.myapplication;

import android.content.ClipData;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    CardView loadingCard;
    ProgressBar loading;
    FloatingActionButton fab;
    ListView myListView;
    String [] items;
    TextView loadingText;
    List<Model> models = new ArrayList<>();
    private final int PICK_FILES = 71;
    public static List<Uri> uriList = new ArrayList<>();
    CardView arimage,ar;
    private FirebaseAuth mAuth;
    FirebaseStorage storage;
    FirebaseFirestore firestore;
    CollectionReference reference;
    Set<String> savedFiles;
    String uriString;

    boolean uploadFinish = false;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("onCreate() called");

        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        reference = firestore.collection("main");
        savedFiles = new HashSet<String>();
        mAuth = FirebaseAuth.getInstance();

//        loadingText = (TextView) findViewById(R.id.textView) ;
//        loading = (ProgressBar) findViewById(R.id.progressBar);
//        loadingCard = (CardView) findViewById(R.id.loadingCard);



        Resources res = getResources();

        ar = (CardView) findViewById(R.id.cardView2);
        arimage = (CardView) findViewById(R.id.cardView);

        ar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, Activity3.class);
                startActivity(myIntent);
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                LoginManager.getInstance().logOut();

                updateUI();


            }
        });

        arimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadFinish = false;

                Intent intent = new Intent();
                intent.setType("application/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

                //To start a activity that will return result

                startActivityForResult(intent,PICK_FILES);
            }
        }
        );

    }

    @Override
    public void onBackPressed()
    {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

   public void upload(){
        final StorageReference storageReference = storage.getReference();
        int counter;
        for(int i = 0; i < models.size(); i++){

//            loading.setVisibility(View.VISIBLE);
//            loadingText.setVisibility(View.VISIBLE);
//            loadingCard.setVisibility(View.VISIBLE);

            final int finalI = i;

            storageReference.child("userData/").child(models.get(i).getName()).putFile(models.get(i).getURI()).addOnCompleteListener(new
             OnCompleteListener<UploadTask.TaskSnapshot>() {
                 @Override
                 public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                     if(task.isSuccessful()){
                         storageReference.child("userData/").child(models.get(finalI).getName()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                             @Override
                             public void onComplete(@NonNull Task<Uri> task) {

                                 if(task.isSuccessful()){
                                     Toast.makeText(MainActivity.this, "Preparing AR scene", Toast.LENGTH_SHORT).show();

                                     String input = task.getResult().toString();

                                     boolean isFound = (input.indexOf("gltf") !=- 1 || input.indexOf("glb") != -1 )? true: false;

                                     if(isFound){
                                        uriString = task.getResult().toString();
                                        uploadFinish = true;
                                     }

                                     savedFiles.add(task.getResult().toString());

                                     //start intent here
                                     if(uploadFinish) {
                                         startIntent();
                                     }

                                     Log.d("FIREBASE", task.getResult().toString());

                                 }else{

                                     storageReference.child("userData/").child(models.get(finalI).getName()).delete();
                                     Toast.makeText(MainActivity.this, "Coudn't save" + models.get(finalI).getName(), Toast.LENGTH_SHORT).show();

                                 }

                                 if(finalI == models.size()-1){
                                    saveFilesToFirestore();
                                 }
                             }
                         });
                     }else{
                         Toast.makeText(MainActivity.this, "Coudn't upload file", Toast.LENGTH_SHORT).show();
                     }
                 }
             }
            );
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null) {
            updateUI();
        }
    }

    private void updateUI(){

        Toast.makeText(MainActivity.this,"You're logged out",Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(MainActivity.this,Login.class);
        startActivity(intent);
        finish();
    }



   @Override
   protected  void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case PICK_FILES:

                Toast.makeText(MainActivity.this, "Adding Model and preparing AR scene....", Toast.LENGTH_SHORT).show();

                if (resultCode == RESULT_OK && data != null) {

                    ClipData clipdata = data.getClipData();

                    if (clipdata != null) {

                        for (int i = 0; i < clipdata.getItemCount(); i++) {

                            Uri uri = clipdata.getItemAt(i).getUri();

                            Model fetchModels = new Model(getFileNameFromUri(uri), uri);

                            models.add(fetchModels);

                            upload();

                            Log.d("Filename", fetchModels.getName());
                        }

                    } else {
                        Uri uri = data.getData();
                        models.add(new Model(getFileNameFromUri(uri), uri));
                        upload();


                        Log.d("Uri added", getFileNameFromUri(uri));
                    }

                }
        }
    }


    public void startIntent(){
        Intent intent = new Intent(MainActivity.this, Activity2.class);
        intent.putExtra("Uri", uriString);
        startActivity(intent);

    }


    public void saveFilesToFirestore(){
        Map<String,String> dataMap = new HashMap<>();
        Iterator<String> it = savedFiles.iterator();
        for(int i=0; i< savedFiles.size(); i++) {
            while (it.hasNext()) {
                dataMap.put("files" + i, it.next());
            }
        }

        reference.add(dataMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
//                loading.setVisibility(View.VISIBLE);
//                loadingText.setVisibility(View.VISIBLE);
//                loadingCard.setVisibility(View.VISIBLE);

                Toast.makeText(MainActivity.this, "Rendering Model", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                loading.setVisibility(View.VISIBLE);
//                loadingText.setVisibility(View.VISIBLE);
//                loadingCard.setVisibility(View.VISIBLE);

                Toast.makeText(MainActivity.this, "Upload failed", Toast.LENGTH_SHORT).show();
            }

        });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*
         Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}


