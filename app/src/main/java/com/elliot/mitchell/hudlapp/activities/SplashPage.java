package com.elliot.mitchell.hudlapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.elliot.mitchell.hudlapp.R;
import com.elliot.mitchell.hudlapp.main.MainActivity;

/**
 * Created by Elliot on 3/3/2015.
 */
public class SplashPage extends Activity {
    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 3000;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle n) {
        super.onCreate(n);
        setContentView(R.layout.splash);

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashPage.this, MainActivity.class);
                SplashPage.this.startActivity(mainIntent);
                SplashPage.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
