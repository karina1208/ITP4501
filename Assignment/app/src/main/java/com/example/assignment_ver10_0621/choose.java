package com.example.assignment_ver10_0621;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class choose extends AppCompatActivity {

    private TextView user;
    private TextView testing;

    private ImageView userPic;
    private ImageButton btnGuess;
    private ImageButton btnLeft;
    private ImageButton btnRight;

    //private String jurl;

    private int btnLeftCount;
    private int btnRightCount;

    private int leftValue;
    private int rightValue;

    private int total;


    /////  for debug    /////
    private static final String TAG = "Asynctask";
    private static final String TAGG = "choose";

    /////   inside asyncTask   /////
    int opId;
    String opName;
    String opCountry;

    Intent i;

    ///// sound effect /////
    MediaPlayer choose;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);


        user = findViewById(R.id.user);
        userPic = findViewById(R.id.userPic);
        btnGuess = findViewById(R.id.btnGuess);
        btnLeft = findViewById(R.id.btnLeft);
        btnRight = findViewById(R.id.btnRight);




        //testing2
        btnLeftCount = 0;
        btnRightCount = 0;

        leftValue = 0;
        rightValue = 0;

        total = 0;

        //set font type
        Typeface type = Typeface.createFromAsset(getAssets(), "font/8-Bit Madness.ttf");
        user.setTypeface(type);


        try {

            final AnimationDrawable animDrawable =
                    (AnimationDrawable) btnGuess.getBackground();

            btnGuess.post(new Runnable() {
                public void run() {
                    animDrawable.start();
                }
            });

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }

        new myTask().execute("https://4qm49vppc3.execute-api.us-east-1.amazonaws.com/Prod/itp4501_api/opponent/0");

    }

    private class myTask extends AsyncTask<String, Integer, String> {
        InputStream inputStream;
        String result = "";
        URL url;
        String line = "";


        protected String doInBackground(String... values) {

            try {
                url = new URL(values[0]);

                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                // Make GET request
                con.setRequestMethod("GET");  // May omit this line since "GET" is the default.
                con.connect();


                // Get response string from inputstream of the connection

                inputStream = con.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));


                while ((line = bufferedReader.readLine()) != null)
                    result += line;

                //Log.d(TAG, "get data complete");
                inputStream.close();


            } catch (Exception e) {
                //
            }
            return result;
        }

        protected void onPostExecute(String result) {

            //Toast.makeText(getApplicationContext(),"connect successful",Toast.LENGTH_SHORT).show();


            try {
                JSONObject jObj = new JSONObject(result);

                opId = jObj.getInt("id");
                opName = jObj.getString("name");
                opCountry = jObj.getString("country");

                Log.d("Reading from jSON", "haha");

                ///// save opponent data in sharedPreference /////
                SharedPreferences oppo = getSharedPreferences("oppoInfo",Context.MODE_PRIVATE);
                SharedPreferences.Editor oppoEditor = oppo.edit();

                oppoEditor.putInt("opId",opId);
                oppoEditor.putString("opName",opName);

                oppoEditor.commit();
                ///// -------------------------------------- /////

                String newUserText =
                        "Opponent's Name: \n" + opName + "\n\nCountry: \n" + opCountry;

                user.setText(newUserText);

                //Toast.makeText(getApplicationContext(),"JSON",Toast.LENGTH_SHORT).show();

                switch(opId) { //set user photo
                    case 1:
                        userPic.setImageResource(R.drawable.user_01);
                        break;
                    case 2:
                        userPic.setImageResource(R.drawable.user_02);
                        break;
                    case 3:
                        userPic.setImageResource(R.drawable.user_03);
                        break;
                    case 4:
                        userPic.setImageResource(R.drawable.user_04);
                        break;
                    case 5:
                        userPic.setImageResource(R.drawable.user_05);
                        break;

                        default:

                }



            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"JSON fail",Toast.LENGTH_SHORT).show();
            }

        }

    }


    public void onClickLeft(View v) {

        if ((btnLeftCount%2)==0) { //even=rock=0 //odd=paper=5
            btnLeft.setBackgroundResource(R.drawable.paper); //if current is rock, then change to paper

            leftValue = 5;

        } else if ((btnLeftCount%2)==1) { //even=rock=0 //odd=paper=5
            btnLeft.setBackgroundResource(R.drawable.rock); //if current is paper, then change to rock

            leftValue = 0;
        }
        btnLeftCount++;
    }

    public void onClickRight(View v) {

        if ((btnRightCount%2)==0) { //even=rock=0 //odd=paper=5
            btnRight.setBackgroundResource(R.drawable.paper_r); //if current is rock, then change to paper

            rightValue = 5;

        } else if ((btnRightCount%2)==1) { //even=rock=0 //odd=paper=5
            btnRight.setBackgroundResource(R.drawable.rock_r); //if current is paper, then change to rock

            rightValue = 0;
        }
        btnRightCount++;
    }


    public void onClickChoose(View v) {
        total = leftValue + rightValue;

        SharedPreferences info = getSharedPreferences("userInfo",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = info.edit();

        editor.putInt("userLeft",leftValue);
        editor.putInt("userRight",rightValue);
        editor.putInt("userTotal",total);

        editor.commit();

        i = new Intent(choose.this, guess.class);

        choose = MediaPlayer.create(getApplicationContext(), R.raw.sound_choose);
        choose.start();

        try {
            //Toast.makeText(getApplicationContext(),String.valueOf(total),Toast.LENGTH_LONG).show();

            startActivity(i);

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"fail",Toast.LENGTH_SHORT).show();
        }
    }



}

