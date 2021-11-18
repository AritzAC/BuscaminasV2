package com.example.buscaminas;

import static android.R.color.black;
import static android.text.TextUtils.concat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class ScoreActivity extends AppCompatActivity implements View.OnClickListener {
    String username, theme;
    ConstraintLayout consScor;
    ImageButton papaLoc;
    int score;
    @SuppressLint({"ResourceAsColor"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        Bundle extras = getIntent().getExtras();

        username = extras.getString("username");
        score = extras.getInt("score");
        theme = extras.getString("theme");
        consScor = findViewById(R.id.consScor);
        papaLoc = (ImageButton) findViewById(R.id.papaLoc);


        papaLoc.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lat = "66.543";
                String lon = "25.847";
                Uri web = Uri.parse((String) concat("geo:"+lat+","+lon));
                Intent intent = new Intent(Intent.ACTION_VIEW,web);
                startActivity(intent);
            }
        } );

        if (theme.equals("the3kings")) {
            consScor.setBackground(getDrawable(R.drawable.reyes_magos));
        }
        if (theme.equals("santa")) {
            consScor.setBackground(getDrawable(R.drawable.santa_claus));
        } else {
            consScor.setBackground(getDrawable(R.drawable.santa_main));
        }
        //Titulo
        TextView titleTxt = findViewById(R.id.textView);
        titleTxt.setText(getString(R.string.score));

        //Base de Datos
        SQLiteDatabase database = openOrCreateDatabase("mydatadase", Context.MODE_PRIVATE, null);

        database.execSQL("CREATE TABLE IF NOT EXISTS mydatabase(Username VARCHAR,Score INT);");

        database.execSQL("INSERT INTO mydatabase VALUES(?, ?);",new String[]{ username, String.valueOf(score)});


        Cursor cursor = database.rawQuery("SELECT * FROM mydatabase ORDER BY score", null);

        TableLayout table = findViewById(R.id.tableLayout);
        while (cursor.moveToNext()) {
            TableRow row = new TableRow(this);
            TextView position = new TextView(this);
            TextView username = new TextView(this);
            TextView score = new TextView(this);

            //Añadir posicion
            position.setText(String.valueOf(cursor.getPosition()+1));
            position.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            position.setTextSize(20);
            position.setBackground(getDrawable(R.drawable.border));
            position.setTypeface(null, Typeface.BOLD);
            position.setTextColor(Color.BLUE);

            //Añadir username
            username.setText(cursor.getString(0));
            username.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            username.setTextSize(20);
            username.setBackground(getDrawable(R.drawable.border));
            username.setTextColor(Color.BLUE);

            //Añadir score
            score.setText(String.valueOf(cursor.getInt(1)));
            score.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            score.setTextSize(20);
            score.setBackground(getDrawable(R.drawable.border));
            score.setTextColor(Color.BLUE);

            //Añadir a la fila
            row.addView(position);
            row.addView(username);
            row.addView(score);
            table.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));

        }

        cursor.close();

        //Play Again Button
        Button playAgainBtn = findViewById(R.id.button);
        playAgainBtn.setText(getString(R.string.play_again));
        playAgainBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }



}
