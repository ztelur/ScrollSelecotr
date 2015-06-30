package com.example.randy.scrollselecotr.firststart;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.randy.scrollselecotr.R;

public class FirstActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_first, menu);
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
    private void handleStart() {
        if (checkFirst()) {
            //跳转啊

        } else {

        }
    }
    private boolean checkFirst() {
        SharedPreferences preferences=getSharedPreferences("first", Context.MODE_PRIVATE);
        if (preferences.getBoolean("firststart",true)) {
            SharedPreferences.Editor editor=preferences.edit();
            //将标准位设置为false
            editor.putBoolean("firststart",false);
            editor.commit();
            return true;
        } else {
            return false;

        }
    }
}
