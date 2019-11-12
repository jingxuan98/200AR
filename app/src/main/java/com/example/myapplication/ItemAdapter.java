package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

//A class that are BaseAdapter but with another name call 'ItemAdapter'

//To pass values/data to ListView

public class ItemAdapter extends BaseAdapter {

    LayoutInflater mInflater;
    String[] items;
    private MainActivity activity;

    public ItemAdapter(Context c, String[] i){
        items = i;
        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);     //To initialize inflater
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int i) {
        return items[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

//    public void openActivity2(){
//        Intent intent = new Intent(activity, Activity2.class);
//        activity.startActivity(intent);
//
//        //activity.startActivity(new Intent(activity, Activity2.class));
//    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        View v = mInflater.inflate(R.layout.my_listview_details,null);
        TextView nameTextView = (TextView) v.findViewById(R.id.nameTextView);

        String name = items[i];

        nameTextView.setText(name);
//        nameTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openActivity2();
//            }
//        });

        return v;
    }
}
