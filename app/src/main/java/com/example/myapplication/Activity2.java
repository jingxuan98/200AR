package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.view.View;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

public class Activity2 extends AppCompatActivity implements View.OnClickListener {

    ArFragment arFragment;
    private ModelRenderable catRenderable;

    ImageView cat;

    View arrayView[];
    ViewRenderable name_animal;

    int selected = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        arFragment = (ArFragment)getSupportFragmentManager().findFragmentById(R.id.sceneform_ux_fragment);

        //View
        cat = (ImageView)findViewById(R.id.cat);

        setArrayView();

        setClickListener();

        setUpModel();

        arFragment.setOnTapArPlaneListener(new BaseArFragment.OnTapArPlaneListener() {
            @Override
            public void onTapPlane(HitResult hitResult, Plane plane, MotionEvent motionEvent) {
                //When user Tap on plane, we will add model;
                if(selected == 1){
                    Anchor anchor = hitResult.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());

                    createModel(anchorNode,selected);
                }
            }
        });
    }

    private void createModel(AnchorNode anchorNode, int selected){
        if(selected == 1){
            TransformableNode cat = new TransformableNode(arFragment.getTransformationSystem());
            cat.setParent(anchorNode);
            cat.setRenderable(catRenderable);
            cat.select();
        }
    }

    private void setUpModel(){
        ModelRenderable.builder()
                .setSource(this,R.raw.cat)
                .build().thenAccept(renderable -> catRenderable = renderable )
                .exceptionally(
                        throwable -> {
                            Toast.makeText(this,"Unable to load cat model", Toast.LENGTH_SHORT).show();
                            return null;
                        }
                );
    }

    private void setClickListener(){
        for (int i=0; i < arrayView.length;i++)
            arrayView[i].setOnClickListener(this);
    }


    private void setArrayView(){
        arrayView = new View[]{
                cat
        };

    }

    @Override
    public void onClick(View v){

    }
}
