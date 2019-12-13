package com.example.myapplication;

import android.content.ClipData;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    FloatingActionButton fab;
    ListView myListView;
    String [] items;
    String path;
    List<Model> models;
    private final int PICK_FILES = 71;
    public static List<Uri> uriList = new ArrayList<>();


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        Resources res = getResources();

        myListView = (ListView) findViewById(R.id.myListView);
            items = res.getStringArray(R.array.items);

            ItemAdapter itemAdapter = new ItemAdapter(this, items);
        myListView.setAdapter(itemAdapter);

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0){
                        Intent intent = new Intent(MainActivity.this, Activity2.class);
                        //intent.putExtra("Uri",uriList.get(0).toString());
                        startActivity(intent);
                    }
                }
        });

      //  myListView.setAdapter(new ArrayAdapter<String>(this,R.layout.));

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("*/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                //To start a activity that will return result

                startActivityForResult(intent,PICK_FILES);
                //startActivityForResult(Intent.createChooser(intent,"Select your .gITF"),PICK_FILES);
            }
        });


    }


    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {

            case PICK_FILES:

                if (resultCode == RESULT_OK && data != null) {

                    ClipData clipdata = data.getClipData();

                    if (clipdata != null) {

                        for (int i = 0; i < clipdata.getItemCount(); i++) {

                            Uri uri = clipdata.getItemAt(i).getUri();
                            models.add(new Model(getFileNameFromUri(uri), uri));

                            Log.d("Uri added",uri.toString());
                        }
                    } else {
                        Uri uri = data.getData();
                        models.add(new Model(getFileNameFromUri(uri), uri));
                    }

                    //To return the Uri of the selected file
                    //uriList.add(data.getData());
                    // path = data.getData().getPath();
                }
        }
    }

    public String getFileNameFromUri(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = this.getContentResolver().query(uri, null, null, null, null);
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
