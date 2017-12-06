package com.vnshine.learnjapanese.Activities;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.vnshine.learnjapanese.R;

import java.util.Locale;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        final Intent intent=new Intent(this,MainActivity.class);
        intent.setAction("menu_translate_activity");
        CountDownTimer countDownTimer=new CountDownTimer(200,100) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                startActivity(intent);
                finish();
            }
        }.start();
    }
}
