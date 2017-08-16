package com.jiandan.terence.firstdraglayout;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jiandan.terence.firstdraglayout.adapter.SimpleAdapter;

/**
 * Created by HP on 2017/8/16.
 */

public class MainActivity2 extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_new_main);
        RecyclerView recyclerView=(RecyclerView) findViewById(R.id.recycle_view);
        recyclerView.setAdapter(new SimpleAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}
