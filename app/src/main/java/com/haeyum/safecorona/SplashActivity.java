package com.haeyum.safecorona;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    private View vFade;

    private float alpha = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        vFade = findViewById(R.id.v_splash_fade);

        if(!PreferenceManager.getBoolean(this, "init")) {
            PreferenceManager.setBoolean(this, "init", true);
            PreferenceManager.setBoolean(this, "isUseWaring", true);
        }

        final Timer timer = new Timer();
        final Timer timer2 = new Timer();

        final TimerTask TT2 = new TimerTask() {
            @Override
            public void run() {
                alpha += 0.04f;
                vFade.setAlpha(alpha);

                if(alpha >= 1) {
                    timer2.cancel();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }

//                Log.d("Splash1", "Animate: " + vFade.getAlpha());
            }
        };

        TimerTask TT = new TimerTask() {
            @Override
            public void run() {
                alpha -= 0.04f;
                vFade.setAlpha(alpha);

                if(alpha <= 0) {
                    timer.cancel();
                    timer2.schedule(TT2, 1000, 30);
                }

//                Log.d("Splash2", "Animate: " + vFade.getAlpha());
            }
        };

        timer.schedule(TT, 250, 30);
    }
}
