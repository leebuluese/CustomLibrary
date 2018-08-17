package com.lyz.lib;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        String[] mainArray = getResources().getStringArray(R.array.main_list);
        List<String> mainList = (List<String>) Arrays.asList(mainArray);
        ListAdapter mainListAdapter = new ListAdapter(this);
        mainListAdapter.setData(mainList);
        //TODO Tim is a dog.
        rv.setAdapter(mainListAdapter);
    }
}
