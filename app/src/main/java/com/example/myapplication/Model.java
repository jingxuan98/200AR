package com.example.myapplication;

import android.net.Uri;

public class Model {
    String modelName;
    Uri modelURI;

    public Model() {
    }

    public Model(String Name, Uri URI) {
        this.modelName = Name;
        this.modelURI = URI;
    }

    public String getName() {
        return modelName;
    }

    public Uri getURI() {
        return modelURI;
    }
}
