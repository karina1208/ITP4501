package com.example.assignment_ver10_0621;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class starting  extends AppCompatActivity {

    private static final String TAG = "starting";

    ImageButton start;
    ImageButton history;
    String msg = "haha";

    ///// sound effect /////
    MediaPlayer choose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);

        start = findViewById(R.id.btnStart);
        history = findViewById(R.id.btnHistory);

        //final – constant
        //An AnimationDrawable defined in XML consists of a single <animation-list> element


        try {

            final AnimationDrawable animDrawable =
                    (AnimationDrawable) start.getBackground();

            start.post(new Runnable() {
                public void run() {
                    animDrawable.start();
                }
            });

            final AnimationDrawable animDraw =
                    (AnimationDrawable) history.getBackground();

            start.post(new Runnable() {
                public void run() {
                    animDraw.start();
                }
            });

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }



    }

    //start.setOnClickListener(new View.OnClickListener()  {
    public void onClick(View v) {
        Intent i = new Intent(starting.this, choose.class);
        choose = MediaPlayer.create(getApplicationContext(), R.raw.sound_choose);
        choose.start();
        try {
            startActivity(i);
        } catch (Exception e) {
            Log.e(TAG,Log.getStackTraceString(e));
        }

    }

    public void onClickHistory(View v) {
        Intent i = new Intent(starting.this, historyList.class);
        choose = MediaPlayer.create(getApplicationContext(), R.raw.sound_choose);
        choose.start();
        try {
            startActivity(i);
        } catch (Exception e) {
            Log.e(TAG,Log.getStackTraceString(e));
        }

    }
    //});

}
