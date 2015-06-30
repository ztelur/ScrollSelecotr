package com.example.randy.scrollselecotr.game.model;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.example.randy.scrollselecotr.FirstHeaderFragment;
import com.example.randy.scrollselecotr.MockListAdapter;
import com.example.randy.scrollselecotr.R;
import com.example.randy.scrollselecotr.game.model.pagedheadlistview.PagedHeadListView;

import java.util.ArrayList;


public class SelectActivity extends FragmentActivity {
    private ViewPager mView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        initView();
    }
    private void initView() {
        mView=(ViewPager)findViewById(R.id.viewpager);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select, menu);
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
