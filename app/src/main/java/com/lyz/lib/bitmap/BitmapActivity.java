package com.lyz.lib.bitmap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lyz.lib.ListAdapter;
import com.lyz.lib.R;

import java.util.Arrays;
import java.util.List;

public class BitmapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        String[] bitmapArray = getResources().getStringArray(R.array.list_bitmap);
        List<String> bitmapList = (List<String>) Arrays.asList(bitmapArray);
        ListAdapter mainListAdapter = new ListAdapter(this);
        mainListAdapter.setData(bitmapList);
        rv.setAdapter(mainListAdapter);
    }
}
