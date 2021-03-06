package com.example.randy.scrollselecotr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.randy.scrollselecotr.game.model.GameActivity;
import com.example.randy.scrollselecotr.game.model.SelectActivity;
import com.example.randy.scrollselecotr.ui.droprefresh.DropRefreshListView;


public class MainActivity extends Activity {
    private DropRefreshListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        listView=(DropRefreshListView)findViewById(R.id.listview);
        Intent intent=new Intent(MainActivity.this, SelectActivity.class);
        startActivity(intent);

           }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
