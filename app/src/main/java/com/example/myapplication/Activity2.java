package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.io.File;
import java.net.URI;

public class Activity2 extends AppCompatActivity implements View.OnClickListener {

    ArFragment arFragment;
    ImageView cat;
    Uri uri;

    String path ;

    private static String GLTF_ASSET =
            "https://github.com/jingxuan98/200AR/blob/master/app/sampleData/glb%20models/chic196.glb";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        //Intent to get data
        //Intent intent = getIntent();
        //GLTF_ASSET = intent.getStringExtra("Uri");

        //File file = new File(new URI(path));
        uri = Uri.parse(GLTF_ASSET);
        //path = yourAndroidURI.uri.getPath() // "/mnt/sdcard/FileName.mp3";

        System.out.println(GLTF_ASSET);

        arFragment = (ArFragment)getSupportFragmentManager().findFragmentById(R.id.sceneform_ux_fragment);


        //To set the hand instruction to null
//        if (arFragment != null) {
//            // hiding the plane discovery
//            arFragment.getPlaneDiscoveryController().hide();
//            arFragment.getPlaneDiscoveryController().setInstructionView(null);
//            arFragment.getArSceneView().getPlaneRenderer().setEnabled(false);
//         //   arFragment.getArSceneView().scene.setOnUpdateListener(
//        }


//        //View
       cat = (ImageView)findViewById(R.id.dog);


        //Setup  Model(Render) -> When user tap, create anchor  -> attach it to transformable node

        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent)-> {
                //When user Tap on plane, we will add model;

                    // Anchor : A fixed location in the real world. Used to transform local coordinates (according to user’s display) to the real-world coordinates.

                    Anchor anchor = hitResult.createAnchor();
                    placeObject(arFragment,anchor);
                });
    }



    //TO CREATE RENDERABLES = A Renderable is a 3D model that can be placed anywhere in the scene and consists of Meshes, Materials and Textures.

    private void placeObject(ArFragment arFragment, Anchor anchor){
        System.out.println(GLTF_ASSET);
        ModelRenderable.builder()
                .setSource(arFragment.getContext(),
                        RenderableSource.builder().setSource(
                                this,
                                uri,
                                RenderableSource.SourceType.GLB)

                                .setScale(0.5f)  // Scale the original model to 50%.
                                .setRecenterMode(RenderableSource.RecenterMode.ROOT)
                                .build()

                        //R.raw.table
                )    //QUES:  USE THIS FOR CONTEXT AND WHEN NEED USE GET()?
                .setRegistryId(GLTF_ASSET)
                .build().thenAccept(renderable -> addNodeToScene(arFragment, anchor, renderable) )
                .exceptionally(
                        throwable -> {
                            Toast.makeText(this,"Unable to load cat model", Toast.LENGTH_SHORT).show();
                            System.out.println(GLTF_ASSET);
                            return null;
                        }
                );
    }


    private void addNodeToScene(ArFragment arFragment, Anchor anchor, Renderable renderable){

            // To create an anchor node which is the root node of our scene (image an augmented reality scene as an inverted tree).
            AnchorNode anchorNode = new AnchorNode(anchor);

            //Set the TransformableNode to the anchorNode so, A transformable node can react to location changes and size changes when the user drags the object or uses pinch to zoom.
            //TransformableNode =  A node that can react to user’s interactions such as rotation, zoom and drag.

            TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());

            //ENABLE TO SCALE USING PINCH
            //cat.getScaleController().setMaxScale(0.2f);
            //cat.getScaleController().setMinScale(0.01f);

            node.setRenderable(renderable); //Sets the Renderable to display for this node.
            node.setParent(anchorNode);
            arFragment.getArSceneView().getScene().addChild(anchorNode);
            node.select();

    }


    @Override
    public void onClick(View v){

    }




}
