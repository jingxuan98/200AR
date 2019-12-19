package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.view.View;
import android.widget.Toast;


public class Activity3 extends AppCompatActivity implements View.OnClickListener{

    CardView architecture, furniture, animal, historical;

    //Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.archi);


        architecture = (CardView) findViewById(R.id.architecture);
        furniture = (CardView) findViewById(R.id.furniture);
        animal = (CardView) findViewById(R.id.animal);
        historical = (CardView) findViewById(R.id.historical);

        architecture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(Activity3.this, "Preparing AR scene", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Activity3.this, Animal.class);
                intent.putExtra("Img","architecture");
                //intent.putExtra("Image",bitmap);
                startActivity(intent);

            }
        });

        furniture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(Activity3.this, "Preparing AR scene", Toast.LENGTH_SHORT).show();


                Intent intent = new Intent(Activity3.this, Animal.class);
                intent.putExtra("Img", "furniture");
                //intent.putExtra("Image",bitmap);
                startActivity(intent);

            }

        });

        historical.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(Activity3.this, "Preparing AR scene", Toast.LENGTH_SHORT).show();


                Intent intent = new Intent(Activity3.this, Animal.class);
                intent.putExtra("Img", "historical");
                //intent.putExtra("Image",bitmap);
                startActivity(intent);

            }

        });

        animal.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Activity3.this, Animal.class);
                intent.putExtra("Img", "animal");
                //intent.putExtra("Image",bitmap);
                Toast.makeText(Activity3.this, "Preparing AR scene", Toast.LENGTH_SHORT).show();
                startActivity(intent);

            }

        });


    }

    @Override
    public void onClick(View v){

    }


}
