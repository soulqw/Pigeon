package com.qw.sample;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.qw.pigeon.Pigeon;
import com.qw.pigeon.Subscribe;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Pigeon.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Pigeon.getDefault().unRegister(this);
    }

    public void nextPage(View v) {
        startActivity(new Intent(this, SecondActivity.class));
    }

    public void postDefault(View v) {
        Pigeon.getDefault().post(new TestEvent("default event"));
    }

    public void postDelay(View v) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Pigeon.getDefault().post(new TestEvent("postDelay event"));
            }
        }, 3000);
    }


    @Subscribe
    public void test(TestEvent event) {
        Log.d("qw", "MainActivity method invoke: ");
    }

    public static class TestEvent {

        private String content;

        public TestEvent(String content) {
            this.content = content;
        }

        @NonNull
        @Override
        public String toString() {
            return content;
        }
    }
}
