package com.qw.sample;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.qw.pigeon.Pigeon;
import com.qw.pigeon.Subscribe;

/**
 * @author cd5160866
 * @date 2020/6/13
 */
public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Pigeon.getDefault().register(this);
    }

    public void postDefault(View v) {
        Pigeon.getDefault().post(new MainActivity.TestEvent("default event"));
    }

    public void postDelay(View v) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Pigeon.getDefault().post(new MainActivity.TestEvent("postDelay event"));
            }
        }, 500);
    }

    @Subscribe
    public void test(MainActivity.TestEvent event) {
        Log.d("qw", "SecondActivity method invoke: ");
    }

}
