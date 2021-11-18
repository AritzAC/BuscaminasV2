package com.example.buscaminas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.gridlayout.widget.GridLayout;

import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.logging.Logger;

public class Game extends AppCompatActivity {
    public static final int SCORE_ACTIVITY = 3;

    String username, mode, theme;
    TextView usernameText;
    GridLayout grid, parent;
    ConstraintLayout consGame;
    Button btnFlag;
    ImageButton cell, restart;
    TextView textView, time;
    Handler handler;
    Runnable runnable;
    int cont, contTime, cellType, mineNum, row, column;
    String mine;
    ImageButton[][] cells;


    Random rnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        //Recoger el usuario del MainActivity
        Bundle extras = getIntent().getExtras();
        username = extras.getString("username");
        mode = extras.getString("mode");
        theme = extras.getString("theme");

        //IDs
        usernameText = findViewById(R.id.textUsernameId);
        usernameText.setText(username);


        mine = getString(R.string.mine);
        mineNum = 0;
        cells = new ImageButton[9][9];
        textView = findViewById(R.id.textView);
        time = findViewById(R.id.time);
        restart = findViewById(R.id.restart);
        btnFlag = findViewById(R.id.button2);
        grid = findViewById(R.id.grid);
        consGame = findViewById(R.id.consGame);

        if (theme.equals("the3kings")) {
            consGame.setBackground(getDrawable(R.drawable.reyes_magos));
        }
        if (theme.equals("santa")) {
            consGame.setBackground(getDrawable(R.drawable.santa_claus));
        } else {
            consGame.setBackground(getDrawable(R.drawable.santa_main));
        }
        contTime = 0;

        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();
            }
        });
        btnFlag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnFlag.getText().equals(getString(R.string.btnDesactivado))) {
                    btnFlag.setText(getString(R.string.btnActivado));

                } else {
                    btnFlag.setText(getString(R.string.btnDesactivado));
                }
            }
        });
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                cell = new ImageButton(this);
                rnd = new Random();
                cellType = rnd.nextInt(5);

                cell.setId(View.generateViewId());
                if (mode.equals("hard")) {
                    if (cellType == 1 && mineNum != 10) {
                        cell.setTag(mine);

                        mineNum++;
                    } else {
                        cell.setTag(getString(R.string.cell));

                    }
                }else{
                    if (cellType == 1 && mineNum != 1) {
                        cell.setTag(mine);

                        mineNum++;
                    } else {
                        cell.setTag(getString(R.string.cell));

                    }
                }
                cells[i][j] = cell;

                cell.setBackgroundResource(R.drawable.casi_llena);

                cell.setLayoutParams(new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                );
                cell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cont = Integer.parseInt(textView.getText().toString());
                        cell = (ImageButton) view;
                        Drawable.ConstantState cellState = cell.getBackground().getConstantState();
                        if (btnFlag.getText().equals(getString(R.string.btnActivado))) {
                            if (cellState != getResources().getDrawable(R.drawable.casi_vacia).getConstantState()) {
                                if (cellState == getResources().getDrawable(R.drawable.casi_llena).getConstantState()) {
                                    if (cont != 0) {
                                        cont--;
                                        textView.setText(String.valueOf(cont));
                                        cell.setBackgroundResource(R.drawable.bandera);
                                    }
                                } else if (cellState == getResources().getDrawable(R.drawable.bandera).getConstantState()) {
                                    cell.setBackgroundResource(R.drawable.casi_llena);
                                    cont++;
                                    textView.setText(String.valueOf(cont));
                                }
                            }
                        } else {
                            if (cellState != getResources().getDrawable(R.drawable.bandera).getConstantState()) {
                                if (!cell.getTag().equals(mine)) {
                                    parent = (GridLayout) cell.getParent();
                                    row = parent.indexOfChild(cell) / parent.getColumnCount();
                                    column = parent.indexOfChild(cell) % parent.getColumnCount();
                                    showCell(row, column);
                                } else {
                                    showMines();
                                    Toast.makeText(getApplicationContext(), R.string.lose, Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        }
                        if(!compGanar(cells)){
                            Toast.makeText(getApplicationContext(), R.string.Win, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Game.this, ScoreActivity.class);
                            intent.putExtra("username",username);
                            intent.putExtra("score",contTime);
                            intent.putExtra("theme", theme);
                            startActivityForResult(intent, SCORE_ACTIVITY);

                        }


                    }
                });

                grid.addView(cell);

            }
        }

        setMineCount();

// Contador de tiempo jugando.
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                contTime++;
                time.setText(String.valueOf(contTime));
                handler.postDelayed(runnable, 1000);

            }
        };

        // Iniciar bucle de ejecucion
        handler.postDelayed(runnable, 1000);

    }

    private void showMines() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (cells[i][j].getTag().equals(getString(R.string.mine))) {
                    cells[i][j].setBackgroundResource(R.drawable.carbon);
                }
            }
        }
    }

    private void showAdjacentCells(int row, int column) {

        if (!cells[row][column].getTag().equals(getString(R.string.mine))) {
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    if ((0 <= i + row && i + row < 9) && (0 <= j + column && j + column < 9) && (i != 0 || j != 0)) {
                        Log.d("MainActivity", String.valueOf(row + i) + "," + String.valueOf(column + j));
                        if (cells[row + i][column + j].getBackground().getConstantState() != getResources().getDrawable(R.drawable.casi_vacia).getConstantState()) {
                            showCell(row + i, column + j);

                        }
                    }
                }
            }
        }

    }

    public void showCell(int row, int column) {


        switch (cells[row][column].getTag().toString()) {

            case "0":
                cells[row][column].setBackgroundResource(R.drawable.casi_vacia);
                showAdjacentCells(row, column);
                break;
            case "1":
                cells[row][column].setBackgroundResource(R.drawable.choc_1);
                break;
            case "2":
                cells[row][column].setBackgroundResource(R.drawable.choc_2);
                break;
            case "3":
                cells[row][column].setBackgroundResource(R.drawable.choc_3);
                break;
            case "4":
                cells[row][column].setBackgroundResource(R.drawable.choc_4);
                break;
            case "5":
                cells[row][column].setBackgroundResource(R.drawable.choc_5);
                break;
            case "6":
                cells[row][column].setBackgroundResource(R.drawable.choc_6);
                break;
            case "7":
                cells[row][column].setBackgroundResource(R.drawable.choc_7);
                break;
            case "8":
                cells[row][column].setBackgroundResource(R.drawable.choc_8);
                break;

        }


    }

//cambiar para el modo de juego

    public void setMineCount() {

            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if (cells[i][j].getTag() != getString(R.string.mine)) {
                        int mineCont = 0;

                        if (i > 0 && j > 0 && cells[i - 1][j - 1].getTag() == mine) {
                            mineCont++;
                        }
                        if (i > 0 && cells[i - 1][j].getTag() == mine) {
                            mineCont++;
                        }
                        if (i > 0 && j < 8 && cells[i - 1][j + 1].getTag() == mine) {
                            mineCont++;
                        }
                        if (j > 0 && cells[i][j - 1].getTag() == mine) {
                            mineCont++;
                        }
                        if (j < 8 && cells[i][j + 1].getTag() == mine) {
                            mineCont++;
                        }
                        if (i < 8 && j > 0 && cells[i + 1][j - 1].getTag() == mine) {
                            mineCont++;
                        }
                        if (i < 8 && cells[i + 1][j].getTag() == mine) {
                            mineCont++;
                        }
                        if (i < 8 && j < 8 && cells[i + 1][j + 1].getTag() == mine) {
                            mineCont++;
                        }
                        cells[i][j].setTag(String.valueOf(mineCont));

                    }
                }
           }
       }



    public Boolean compGanar(ImageButton[][] cells) {
        Boolean seguJugando = false;

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                Drawable.ConstantState cellState = cells[i][j].getBackground().getConstantState();

                if (cellState == getResources().getDrawable(R.drawable.casi_llena).getConstantState()) {
                    if (!cells[i][j].getTag().equals(getString(R.string.mine))) {
                        seguJugando = true;
                    }
                }

            }
        }
        return seguJugando;
    }


}
