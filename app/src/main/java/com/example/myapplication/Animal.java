package com.example.myapplication;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.ar.core.Anchor;
import com.google.ar.core.AugmentedImageDatabase;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;

import com.google.android.material.snackbar.Snackbar;
import com.google.ar.core.AugmentedImage;
import com.google.ar.core.Config;
import com.google.ar.core.Frame;
import com.google.ar.core.Session;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Scene;

import java.util.Collection;

public class Animal extends AppCompatActivity implements Scene.OnUpdateListener {

    CustomArFragment arFragment;
    String selected;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_4);

        Resources res = getResources();

        Intent intent = getIntent();
        selected = intent.getStringExtra("Img");

        //To refer to the AR fragment in AR Fragment

        arFragment = (CustomArFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        arFragment.getArSceneView().getScene().addOnUpdateListener(this); //When got update to scene, the update function will call
    }

    //Add that image to AR image database and AR session
    public void setupDatabase(Config config, Session session) {
        Bitmap animalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.animaimg);
        Bitmap furnitureBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.furnitureimg);
        Bitmap architectureBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.architectureimg);
        Bitmap historicalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.historicalimg);

        Toast.makeText(Animal.this, "Setting Up AR Image Detector", Toast.LENGTH_SHORT).show();


        AugmentedImageDatabase aid = new AugmentedImageDatabase(session);

        aid.addImage("animal", animalBitmap);
        aid.addImage("furniture", furnitureBitmap);
        aid.addImage("architecture", architectureBitmap);
        aid.addImage("historical", historicalBitmap);
        config.setAugmentedImageDatabase(aid);

        Toast.makeText(Animal.this, "Setting Up AR Image Detector", Toast.LENGTH_LONG).show();

    }

    public void onClickAR(View view) {

    }

    @Override
    //TO check the AR frame everytime and check the Bitmap isit available, if yes then place an achor and start AR
    public void onUpdate(FrameTime frameTime) {
        Frame frame = arFragment.getArSceneView().getArFrame();
        //To store the images being track and store to a collection
        Collection<AugmentedImage> images = frame.getUpdatedTrackables(AugmentedImage.class);


        //Check whether the image is being tracked
        for (AugmentedImage image : images ){
            if (image.getTrackingState() == TrackingState.TRACKING){
                if (image.getName().equals(selected)){
                    Anchor anchor = image.createAnchor(image.getCenterPose());
                    createModel(anchor, selected);
                }
            }
        }
    }


    private void createModel(Anchor anchor, String model) {

        switch (model) {

            case "architecture":

                ModelRenderable.builder()
                        .setSource(this, R.raw.japaneseset
                                //Uri.parse("dog.sfb")
                        )
                        .build()
                        .thenAccept(modelRenderable -> placeModel(modelRenderable, anchor));

                break;

            case "animal":

                ModelRenderable.builder()
                        .setSource(this, R.raw.cow
                                //Uri.parse("dog.sfb")
                        )
                        .build()
                        .thenAccept(modelRenderable -> placeModel(modelRenderable, anchor));

                break;

            case "furniture":

                ModelRenderable.builder()
                        .setSource(this, R.raw.furniturecloset
                                //Uri.parse("dog.sfb")
                        )
                        .build()
                        .thenAccept(modelRenderable -> placeModel(modelRenderable, anchor));

                break;

            case "historical":

                ModelRenderable.builder()
                        .setSource(this, R.raw.libertystatue
                                //Uri.parse("dog.sfb")
                        )
                        .build()
                        .thenAccept(modelRenderable -> placeModel(modelRenderable, anchor));

                break;
        }
    }







    private void placeModel(ModelRenderable modelRenderable, Anchor anchor) {
        AnchorNode anchorNode = new AnchorNode(anchor);
        anchorNode.setLocalScale(new Vector3(0.3f, 0.3f, 0.3f));
        anchorNode.setRenderable(modelRenderable);
        arFragment.getArSceneView().getScene().addChild(anchorNode);

    }
}