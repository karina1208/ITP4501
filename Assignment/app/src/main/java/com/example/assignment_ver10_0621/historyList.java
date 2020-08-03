package com.example.assignment_ver10_0621;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class historyList extends AppCompatActivity {

    TextView list;
    Button btnChart;

    // ----- database ----- //
    SQLiteDatabase db;
    Cursor cursor;

    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_list);

        list = findViewById(R.id.list);
        btnChart = findViewById(R.id.btnChart);

        //change font type
        Typeface type = Typeface.createFromAsset(getAssets(), "font/8-Bit Madness.ttf");
        list.setTypeface(type);
        btnChart.setTypeface(type);

        readDB();
    }

    public void readDB() {

        try {
            Log.d("pre open database", "haha"); //test
            db = SQLiteDatabase.openDatabase("/data/data/com.example.assignment_ver10_0621/asmDB",
                    null, SQLiteDatabase.OPEN_READWRITE);
            Log.d("db opened", "haha"); //test

            //GamesLog (id, gameDate, gameTime, opponentName, winOrLost)
            String[] col = {"gameDate", "gameTime", "opponentName", "winOrLost"};

            cursor = db.query("GamesLog",col,null, null, null, null, null);


        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "read DB fail", Toast.LENGTH_LONG).show();
        }

        String dataStrHeader = String.format("\n%12s %11s %9s %6s\n\n", "Game date", "Game time", "Opponent", "Result");
        list.setText(dataStrHeader);

        String listContent = "";

        try {
            while (cursor.moveToNext()) {
                String gameDate = cursor.getString(cursor.getColumnIndex("gameDate"));
                String gameTime = cursor.getString(cursor.getColumnIndex("gameTime"));
                String opponentName = cursor.getString(cursor.getColumnIndex("opponentName"));
                String winOrLost = cursor.getString(cursor.getColumnIndex("winOrLost"));

                listContent +=
                        String.format("%12s %11s %9s %6s\n", gameDate, gameTime, opponentName, winOrLost);
            }


            String print = dataStrHeader += listContent;
            list.setText(print);

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "use cursor fail", Toast.LENGTH_LONG).show();
        }

    }

    public void btnOnClick(View v){
        i = new Intent(historyList.this, historyChart.class);

        try {
            startActivity(i);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
}
