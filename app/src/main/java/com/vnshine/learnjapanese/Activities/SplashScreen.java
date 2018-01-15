package com.vnshine.learnjapanese.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.vnshine.learnjapanese.BuildConfig;
import com.vnshine.learnjapanese.R;
import com.vnshine.lib.PreferenceManager;
import com.vnshine.lib.VnshineConstant;
import com.vnshine.lib.VnshineController;

import java.util.Locale;

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        final Intent intent=new Intent(this,MainActivity.class);
        intent.setAction("menu_translate_activity");
        VnshineController.getInstance().activeLib(getBaseContext(),BuildConfig.APPLICATION_ID);
        VnshineController.getInstance().activeAds("ca-app-pub-9164465649979150/1068452591","ca-app-pub-9164465649979150/9250194941","ca-app-pub-9164465649979150~6157127744");
       VnshineController.getInstance().trackingScreen();
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
