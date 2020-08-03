package com.example.assignment_ver10_0621;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class guess extends AppCompatActivity {

    Button guessZero;
    Button guessFive;
    Button guessTen;
    Button guessFifteen;
    Button guessTwenty;

    private int guessValue;

    ImageView guessIB;

    Intent i;

    ///// sound effect /////
    MediaPlayer choose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess);

        guessZero = findViewById(R.id.guessZero);
        guessFive = findViewById(R.id.guessFive);
        guessTen = findViewById(R.id.guessTen);
        guessFifteen = findViewById(R.id.guessFifteen);
        guessTwenty = findViewById(R.id.guessTwenty);

        guessIB = findViewById(R.id.guessIB);

        //set font type
        Typeface type = Typeface.createFromAsset(getAssets(), "font/8-Bit Madness.ttf");
        guessZero.setTypeface(type);
        guessFive.setTypeface(type);
        guessTen.setTypeface(type);
        guessFifteen.setTypeface(type);
        guessTwenty.setTypeface(type);

        try {

            final AnimationDrawable animDrawable =
                    (AnimationDrawable) guessIB.getBackground();

            guessIB.post(new Runnable() {
                public void run() {
                    animDrawable.start();
                }
            });

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    public void btnGuess(View view) {
        choose = MediaPlayer.create(getApplicationContext(), R.raw.sound_choose);


        switch (view.getId()) {

            case R.id.guessZero:
                guessValue = 0;
                choose.start();
                break;
            case R.id.guessFive:
                guessValue = 5;
                choose.start();
                break;
            case R.id.guessTen:
                guessValue = 10;
                choose.start();
                break;
            case R.id.guessFifteen:
                guessValue = 15;
                choose.start();
                break;
            case R.id.guessTwenty:
                guessValue = 20;
                choose.start();
                break;

            default:

        }



        i = new Intent(guess.this, result.class);

        try {

            ///// save guess value in sharedPreference /////
            SharedPreferences info = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = info.edit();

            editor.putInt("guessValue",guessValue);

            editor.commit();
            ///// ------------------------------------ /////


            /*///// testing sharedPreference /////

            SharedPreferences oppo = getSharedPreferences("oppoInfo",Context.MODE_PRIVATE);

            String testShare =
                    info.getString("name",null) +
                    oppo.getInt("opId",0) +
                    oppo.getString("opName",null);

            Toast.makeText(getApplicationContext(),testShare,Toast.LENGTH_LONG).show();

            ///// ------------------------ /////*/

            startActivity(i);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }


    }
}
