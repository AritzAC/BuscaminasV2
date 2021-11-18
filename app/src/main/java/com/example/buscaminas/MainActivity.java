package com.example.buscaminas;


import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    //Atributos
    private ImageButton bConfig, bPlay, bExit;
    private EditText inputUsername;
    private String nullValue, username, exitConfirm, mode ="hard", theme= "default";
    private Intent intent;
    private ConstraintLayout mainPane;
    //ID de los intents
    public static final int SETTINGS_ACTIVITY = 2;
    public static final int GAME_ACTIVITY = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainPane = (ConstraintLayout) findViewById(R.id.mainPaneId);
        bConfig = (ImageButton) findViewById(R.id.configButtonId);
        bPlay = (ImageButton) findViewById(R.id.playButtonId);
        bExit = (ImageButton) findViewById(R.id.exitButtonId);
        inputUsername = (EditText) findViewById(R.id.inputUsernameId);
        nullValue = getString(R.string.text_null);
        exitConfirm = getString(R.string.text_exit_confirm);
        bConfig.setOnClickListener(this);
        bPlay.setOnClickListener(this);
        bExit.setOnClickListener(this);

        mainPane.setBackground(getDrawable(R.drawable.santa_main));
    }

    @Override
    public void onClick(View v) {
        username = inputUsername.getText().toString();
        switch(v.getId()){
            case R.id.configButtonId:
                intent = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivityForResult(intent, SETTINGS_ACTIVITY);
                break;
            case R.id.playButtonId:
                //si el username esta vacio, error
                if(username.isEmpty()){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                    alertDialog.setMessage(nullValue);
                    alertDialog.setPositiveButton(android.R.string.ok, null);
                    alertDialog.show();
                //sino, pasar a ventana de juego pasandole el usuario
                }else{
                        intent = new Intent(MainActivity.this, Game.class);
                        intent.putExtra("username", username);
                        intent.putExtra("mode", mode);
                        intent.putExtra("theme", theme);
                        startActivityForResult(intent, GAME_ACTIVITY);
                }
                break;
            //Salir de la aplicacion
            case R.id.exitButtonId:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setMessage(exitConfirm);
                alertDialog.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finishAffinity();
                        System.exit(0);
                    }
                });
                alertDialog.setNegativeButton(android.R.string.no, null);
                alertDialog.show();
                break;
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //de que activity lo recibe y el resultado que obtiene
        if(requestCode == SETTINGS_ACTIVITY){
            //si obtiene un resultado, se ha ejecutado con exito
            if(resultCode == RESULT_OK){
                theme = data.getStringExtra("theme");
                mode = data.getStringExtra("mode");

                if(theme.equals("the3kings")){
                    mainPane.setBackground(getDrawable(R.drawable.reyes_magos));
                }
                if(theme.equals("santa")){
                    mainPane.setBackground(getDrawable(R.drawable.santa_claus));
                }

            }else{
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setMessage("Ha ocurrido un error");
                alertDialog.setPositiveButton(android.R.string.ok, null);
                alertDialog.show();
            }

        }
    }
}