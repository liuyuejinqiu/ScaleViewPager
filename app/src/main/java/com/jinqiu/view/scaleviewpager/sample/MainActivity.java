package com.jinqiu.view.scaleviewpager.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;

import com.jinqiu.view.scalerecyclerpager.ScaleRecyclerViewPager;
import com.jinqiu.view.scaleviewpager.ScaleViewPager;
import com.jinqiu.view.scaleviewpager.sample.adapter.RecyclerLayoutAdapter;
import com.jinqiu.view.scaleviewpager.sample.adapter.ViewPagerAdapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //ScaleViewPager
        ScaleViewPager scaleViewPager = (ScaleViewPager) findViewById(R.id.scaleViewPager);
        scaleViewPager.setAdapter(new ViewPagerAdapter());

        //ScaleRecyclerViewPager
        ScaleRecyclerViewPager mRecyclerView = (ScaleRecyclerViewPager) findViewById(R.id.viewpager);
        LinearLayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
                false);
        mRecyclerView.setLayoutManager(layout);
        mRecyclerView.setAdapter(new RecyclerLayoutAdapter(this, mRecyclerView));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLongClickable(true);
    }

}
