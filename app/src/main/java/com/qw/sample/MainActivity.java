package com.qw.sample;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Pigeon.getDefault().register(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Pigeon.getDefault().post(new TestEvent());
            }
        }, 3000);
    }

    @Subscribe
    public void test(TestEvent event) {
        Log.d("qw", "method invoke");
    }

    public static class TestEvent {

    }
}
