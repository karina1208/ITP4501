package com.example.assignment_ver10_0621;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class historyChart extends AppCompatActivity {

    ImageView chart;

    ///// database /////
    SQLiteDatabase db;
    Cursor cursorWin;
    Cursor cursorLose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_chart);

        View v = new Panel(getApplicationContext());
        Bitmap bitmap = Bitmap.createBitmap(420/*width*/, 450/*height*/, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);

        chart = findViewById(R.id.chart);

        chart.setImageBitmap(bitmap);
    }

    class Panel extends View {

        public Panel(Context context) {
            super (context);
        }


        String title = "Result Chart";
        String[] item = {"Win","Lose"};


        public void onDraw(Canvas c) {
            super.onDraw(c);
            Paint paint = new Paint();

            // ----- draw background ----- //
            paint.setColor(Color.BLACK);
            c.drawPaint(paint);

            paint.setColor(Color.WHITE);

            // ----- draw the charts ----- //

            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(5);
            c.drawLine(20,100,20,350,paint);
            c.drawLine(20,350,400,350,paint);

            //get winOrLost
            int[] result = readDB();
            int win = 350 - (result[0]*10);
            int lose = 350 - (result[1]*10);   //*2 because wants to make a bigger contrast between 2 chart

            paint.setStyle(Paint.Style.FILL);

            c.drawRect(96, win,172,350, paint);
            c.drawRect(248,lose,324,350, paint);

            // ----- draw the text ----- //
            Typeface type = Typeface.createFromAsset(getAssets(), "font/8-Bit Madness.ttf");

            paint.setTextAlign(Paint.Align.CENTER);

            paint.setTextSize(50);
            paint.setTypeface(type);
            c.drawText(title,210,50,paint);

            paint.setTextSize(30);
            c.drawText(item[0],134,400,paint);
            c.drawText(item[1],286,400,paint);
        }

    }

    public int[] readDB() {

        int[] chartValue = {0, 0}; // default value zero.

        try {
            Log.d("pre open database", "haha");
            db = SQLiteDatabase.openDatabase("/data/data/com.example.assignment_ver10_0621/asmDB",
                    null, SQLiteDatabase.OPEN_READONLY);
            Log.d("db opened", "haha");

            //GamesLog (id, gameDate, gameTime, opponentName, winOrLost)
            String[] col = {"winOrLost"};

            String[] argW = {"win"};
            String[] argL = {"lose"};

            cursorWin = db.rawQuery("SELECT * FROM GamesLog WHERE winOrLost = ?", argW);
            cursorLose = db.rawQuery("SELECT * FROM GamesLog WHERE winOrLost = ?", argL);

            chartValue[0] = cursorWin.getCount();
            chartValue[1] = cursorLose.getCount();  //save the result in an array


            db.close();


        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "read DB fail", Toast.LENGTH_LONG).show();
        }

        return chartValue;

    }
}
