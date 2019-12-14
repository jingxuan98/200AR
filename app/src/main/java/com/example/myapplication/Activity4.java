package com.example.myapplication;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.ar.core.Anchor;
import com.google.ar.core.AugmentedImageDatabase;
import com.google.ar.sceneform.AnchorNode;
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

public class Activity4 extends AppCompatActivity implements Scene.OnUpdateListener {

    CustomArFragment arFragment;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_4);

        Resources res = getResources();


        //To refer to the AR fragment in AR Fragment

        arFragment = (CustomArFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        arFragment.getArSceneView().getScene().addOnUpdateListener(this); //When got update to scene, the update function will call

    }

    //Add that image to AR image database and AR session
    public void setupDatabase(Config config, Session session) {
        Bitmap foxBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.fox);
        AugmentedImageDatabase aid = new AugmentedImageDatabase(session);
        aid.addImage("dog", foxBitmap);
        config.setAugmentedImageDatabase(aid);
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
                    if (image.getName().equals("dog")){
                        Anchor anchor = image.createAnchor(image.getCenterPose());
                        createModel(anchor);
                    }
                }
            }
        }

    private void createModel(Anchor anchor) {

        ModelRenderable.builder()
                .setSource(this, R.raw.dog
                        //Uri.parse("dog.sfb")
                )
                .build()
                .thenAccept(modelRenderable -> placeModel(modelRenderable, anchor));
    }

    private void placeModel(ModelRenderable modelRenderable, Anchor anchor) {
        AnchorNode anchorNode = new AnchorNode(anchor);
        anchorNode.setRenderable(modelRenderable);
        arFragment.getArSceneView().getScene().addChild(anchorNode);

    }
}


