package com.example.assignment_ver10_0621;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import static android.provider.ContactsContract.Intents.Insert.NOTES;


public class MainActivity extends AppCompatActivity{

    private static final String TAG = "MyActivity";

    TextView please;

    EditText name;
    EditText birth;
    EditText phone;
    EditText email;

    ImageView titleImage;


    Button btnReg;
    Button btnRead;
    Button btnNext;
    Button btnClear;

    Intent i;

    ///// database /////
    SQLiteDatabase db;
    String sql;
    String [] columns = {"gameDate", "gameTime", "opponentName", "winOrLost"};

    ///// sound effect /////
    MediaPlayer choose;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id. name);
        birth = findViewById(R.id.birth);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);

        titleImage = findViewById(R.id.titleImage);

        please = findViewById(R.id.please);
        btnReg = findViewById(R.id.btnReg);
        btnRead = findViewById(R.id.btnRead);
        btnNext = findViewById(R.id.btnNext);
        btnClear = findViewById(R.id.btnClear);


        Typeface type = Typeface.createFromAsset(getAssets(), "font/8-Bit Madness.ttf");
        please.setTypeface(type);
        btnReg.setTypeface(type);
        btnRead.setTypeface(type);
        btnNext.setTypeface(type);
        btnClear.setTypeface(type);




        SharedPreferences info = getSharedPreferences("userInfo",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = info.edit();

        try {
            final AnimationDrawable animDrawable =
                    (AnimationDrawable) titleImage.getDrawable();

            titleImage.post(new Runnable() {
                public void run() {
                    animDrawable.start();
                }
            });

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }

        initialDB();

    }

    //btnReg.setOnClickListener(new View.OnClickListener(){
    //@Override
    public void btnOnClick(View view) {

        SharedPreferences info = getSharedPreferences("userInfo",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = info.edit();

        switch (view.getId()) {

            case R.id.btnReg:

                try {

                   /* OutputStreamWriter out = new OutputStreamWriter(openFileOutput(NOTES, 0));
                    out.write(name.getText().toString());

                    out.close();*/

                    editor.putString("name",name.getText().toString());
                    editor.putString("birth",birth.getText().toString());
                    editor.putString("phone",phone.getText().toString());
                    editor.putString("email",email.getText().toString());

                    editor.commit();


                    Toast.makeText(getApplicationContext(),"Saved",Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.btnRead:
                try {
                    /*InputStream in = openFileInput(NOTES);
                    if(in!=null) {
                        InputStreamReader tmp = new InputStreamReader(in);
                        BufferedReader reader = new BufferedReader(tmp);
                        String str;

                        StringBuffer buf = new StringBuffer();
                        while ((str=reader.readLine())!=null) {
                            buf.append(str+"\n");
                        }
                        in.close();
                        name.setText(buf.toString());

                    }*/
                    name.setText(info.getString("name",null));
                    birth.setText(info.getString("birth",null));
                    phone.setText(info.getString("phone",null));
                    email.setText(info.getString("email",null));


                    Toast.makeText(getApplicationContext(),"Read User Info",Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.btnClear:
                name.setText("");
                birth.setText("");
                phone.setText("");
                email.setText("");


                Toast.makeText(getApplicationContext(),"Cleared",Toast.LENGTH_SHORT).show();

                break;

            case R.id.btnNext:

                i = new Intent(MainActivity.this, starting.class);

                try {
                    choose = MediaPlayer.create(getApplicationContext(), R.raw.sound_choose);
                    choose.start();

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),"unable to play sound effect",Toast.LENGTH_SHORT).show();
                }

                try {
                    startActivity(i);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }

        }

    }

    public void initialDB() {
        try {
            db = SQLiteDatabase.openDatabase("/data/data/com.example.assignment_ver10_0621/asmDB",
                    null,SQLiteDatabase.CREATE_IF_NECESSARY);

            //sql = "DROP TABLE IF EXISTS GamesLog;";
            //db.execSQL(sql);

            //id, gameDate, gameTime, opponentName, winOrLost
            sql="CREATE TABLE if not exists GamesLog (" +
                    "gameid INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "gameDate TEXT," +
                    "gameTime TEXT," +
                    "opponentName TEXT," +
                    "winOrLost TEXT);";

            db.execSQL(sql);

            db.close();

            //Toast.makeText(getApplicationContext(), "create DB success", Toast.LENGTH_LONG).show();




        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "create DB fail", Toast.LENGTH_LONG).show();
        }
    }



}
