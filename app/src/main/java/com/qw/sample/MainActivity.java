package com.qw.sample;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.qw.pigeon.Pigeon;
import com.qw.pigeon.Subscribe;
import com.qw.sample.event.ToastEvent;

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
                Pigeon.getDefault().post(new ToastEvent("postDelay event"));
            }
        }, 3000);
    }


    @Subscribe
    public void test(TestEvent event) {
        Log.d("qw", "MainActivity method test invoke: ");
    }

    @Subscribe
    public void test2(TestEvent event) {
        Log.d("qw", "MainActivity method test2 invoke: ");
    }

    @Subscribe
    public void test(ToastEvent event) {
        Toast.makeText(this, event.message, Toast.LENGTH_SHORT).show();
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
