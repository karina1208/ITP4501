package com.example.assignment_ver10_0621;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

public class result extends AppCompatActivity {

    TextView resultOppoName;
    TextView resultUserName;
    ImageView opLeft;
    ImageView opRight;
    ImageView userLeft;
    ImageView userRight;
    ImageView resultMiddle;
    Button btnCon;
    Button btnNext;
    Button btnQuit;

    // ----- opponent ----- //
    int oppoIdByPref;
    String oppoNameByPref;

    int jsonOppoLeft;
    int jsonOppoRight;
    int jsonOppoGuess;

    // ----- user ----- //
    String userName;
    int userGuess;

    // ----- database ----- //
    SQLiteDatabase db;

    // ----- intent ----- //
    Intent i;

    // ----- sound effect ----- //
    MediaPlayer choose;
    MediaPlayer win;
    MediaPlayer lose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        resultOppoName = findViewById(R.id.resultOppoName);
        resultUserName = findViewById(R.id.resultUserName);
        opLeft = findViewById(R.id.opLeft);
        opRight = findViewById(R.id.opRight);
        userLeft = findViewById(R.id.userLeft);
        userRight = findViewById(R.id.userRight);
        resultMiddle = findViewById(R.id.resultMiddle);
        btnCon = findViewById(R.id.btnCon);
        btnNext = findViewById(R.id.btnNext);
        btnQuit = findViewById(R.id.btnQuit);

        //set font type
        Typeface type = Typeface.createFromAsset(getAssets(), "font/8-Bit Madness.ttf");
        resultOppoName.setTypeface(type);
        resultUserName.setTypeface(type);
        btnCon.setTypeface(type);
        btnQuit.setTypeface(type);
        btnNext.setTypeface(type);

        try {

            final AnimationDrawable animDrawable =
                    (AnimationDrawable) resultMiddle.getBackground();

            resultMiddle.post(new Runnable() {
                public void run() {
                    animDrawable.start();
                }
            });

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }

        try {
            //get opponent's id to assess to the related http link
            SharedPreferences oppo = getSharedPreferences("oppoInfo", Context.MODE_PRIVATE);
            oppoIdByPref = oppo.getInt("opId", 1);
            oppoNameByPref = oppo.getString("opName", null);

            //get user data
            SharedPreferences info = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            userName = info.getString("name", null);
            userGuess = info.getInt("guessValue", 0);

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "get shared preference fail", Toast.LENGTH_SHORT).show();
        }

        //call asyncTask
        new myTask().execute("https://4qm49vppc3.execute-api.us-east-1.amazonaws.com/Prod/itp4501_api/opponent/" + oppoIdByPref);
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
                con.setRequestMethod("GET");
                con.connect();


                // Get response string from inputStream of the connection

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

            //Toast.makeText(getApplicationContext(),"Connect successful",Toast.LENGTH_SHORT).show();


            try {
                JSONObject jObj = new JSONObject(result);

                jsonOppoLeft = jObj.getInt("left");
                jsonOppoRight = jObj.getInt("right");
                jsonOppoGuess = jObj.getInt("guess");

                Log.d("Reading from jSON", "haha");

                // ----- save opponent data in sharedPreference ----- //
                SharedPreferences oppo = getSharedPreferences("oppoInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor oppoEditor = oppo.edit();

                oppoEditor.putInt("opLeft", jsonOppoLeft);
                oppoEditor.putInt("opRight", jsonOppoRight);
                oppoEditor.putInt("opGuess", jsonOppoGuess);

                oppoEditor.commit();
                // -------------------------------------------------- //

                //Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();

                setTextForBoth();
                setPicForBoth();
                setResult();


            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "JSON fail", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void setTextForBoth() {
        resultUserName.setText(userName + " guess " + userGuess);
        resultOppoName.setText(oppoNameByPref);
    }

    public void setPicForBoth() {
        SharedPreferences info = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences oppo = getSharedPreferences("oppoInfo", Context.MODE_PRIVATE);

        // ----- set user pic ----- //
        if (info.getInt("userLeft", 0) == 0) {      //set left
            userLeft.setBackgroundResource(R.drawable.rock);
        } else {
            userLeft.setBackgroundResource(R.drawable.paper);
        }

        if (info.getInt("userRight", 0) == 0) {     //set right
            userRight.setBackgroundResource(R.drawable.rock_r);
        } else {
            userRight.setBackgroundResource(R.drawable.paper_r);
        }
        // ------------------------- //

        // ----- set oppo pic ----- //
        if (oppo.getInt("opLeft", 0) == 0) {        //set left
            opLeft.setBackgroundResource(R.drawable.rock_oppo_l);
        } else {
            opLeft.setBackgroundResource(R.drawable.paper_oppo_l);
        }

        if (oppo.getInt("opRight", 0) == 0) {       //set right
            opRight.setBackgroundResource(R.drawable.rock_oppo);
        } else {
            opRight.setBackgroundResource(R.drawable.paper_oppo);
        }
        // ------------------------- //


    }

    public void setResult() {
        SharedPreferences info = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        int plusTotal =
                info.getInt("userTotal", 0) + jsonOppoLeft + jsonOppoRight;

        try {
            if (userGuess == plusTotal) {     //user win
                // ----- change middle bar to win ----- //
                resultMiddle.setBackgroundResource(R.drawable.winani);


                final AnimationDrawable animDrawable =
                        (AnimationDrawable) resultMiddle.getBackground();

                //restart animation
                resultMiddle.post(new Runnable() {
                    public void run() {
                        animDrawable.start();
                    }
                });

                btnCon.setText("PLAY AGAIN >>");     //active these two button
                btnQuit.setText("QUIT >>");

                insertDB("win");

                win = MediaPlayer.create(getApplicationContext(), R.raw.sound_win);
                win.start();


            } else if (userGuess != plusTotal) { //user lose
                // ----- change middle bar to lose ----- //
                resultMiddle.setBackgroundResource(R.drawable.loseani);

                final AnimationDrawable animDrawable =
                        (AnimationDrawable) resultMiddle.getBackground();

                //restart animation
                resultMiddle.post(new Runnable() {
                    public void run() {
                        animDrawable.start();
                    }
                });

                btnNext.setText("NEXT >>");         //active next button

                lose = MediaPlayer.create(getApplicationContext(), R.raw.sound_lose);
                lose.start();

            }
        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(), "Set result fail", Toast.LENGTH_SHORT).show();
        }
    }

    public void btnResult(View view) {
        switch (view.getId()) {

            case R.id.btnNext:
                i = new Intent(result.this, chooseNext.class);

                choose = MediaPlayer.create(getApplicationContext(), R.raw.sound_choose);
                choose.start();

                try {
                    startActivity(i);
                } catch (Exception e) {
                    //Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btnCon:
                i = new Intent(result.this, starting.class);

                choose = MediaPlayer.create(getApplicationContext(), R.raw.sound_choose);
                choose.start();

                try {
                    startActivity(i);
                } catch (Exception e) {
                    //Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btnQuit:
                i = new Intent(result.this, MainActivity.class);

                choose = MediaPlayer.create(getApplicationContext(), R.raw.sound_choose);
                choose.start();

                try {
                    startActivity(i);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }

                break;

            default:

        }
    }

    public void insertDB(String winOrLost) {

        try {
            Log.d("pre open database", "haha");  //test
            db = SQLiteDatabase.openDatabase("/data/data/com.example.assignment_ver10_0621/asmDB",
                    null, SQLiteDatabase.OPEN_READWRITE);
            Log.d("db opened", "haha");          //test

            String insertString = "('" + getDate() + "','" + getTime() + "','" + oppoNameByPref + "','" + winOrLost + "');";


            //GamesLog (id, gameDate, gameTime, opponentName, winOrLost)
            db.execSQL("INSERT INTO GamesLog (gameDate,gameTime,opponentName,winOrLost) VALUES" + insertString);
            Log.d("db inserted", "haha");        //test
            //Toast.makeText(getApplicationContext(), "insert DB success", Toast.LENGTH_LONG).show();

            db.close();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "insert DB fail", Toast.LENGTH_LONG).show();
        }

    }

    public String getDate() {
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return year + "-" + month + "-" + day;
    }

    public String getTime() {
        Calendar calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        return hour + ":" + minute + ":" + second;
    }
}
