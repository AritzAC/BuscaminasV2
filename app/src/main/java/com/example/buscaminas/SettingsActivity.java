package com.example.buscaminas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private TextView modeTxt;
    private TextView themeTxt;
    private ConstraintLayout settingsPane;
    private ImageButton santaTheme;
    private ImageButton thekingsTheme;
    private RadioGroup modeChoice;
    private RadioButton ezModeRbtn;
    private RadioButton hardModeRBtn;
    private Button saveSettings;
    boolean checked;
    boolean choice;
    String mod;
    String them;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settingsPane = (ConstraintLayout) findViewById(R.id.settingsPaneId);

        //titulos de las tablas
        modeTxt = (TextView) findViewById(R.id.modeTxt);
        themeTxt = (TextView) findViewById(R.id.themeTxt);

        // RadioGroup
        modeChoice = (RadioGroup) findViewById(R.id.modeChoiceRaGr);
        modeChoice.setOnCheckedChangeListener(this);
        //RadioButtons
        ezModeRbtn = (RadioButton) findViewById(R.id.ezMode);
        hardModeRBtn = (RadioButton) findViewById(R.id.hardMode);
        hardModeRBtn.setChecked(true);
        checked = false;
        //ImgButton
        santaTheme = (ImageButton) findViewById(R.id.santaThemImBtn);
        santaTheme.setOnClickListener(this);
        thekingsTheme = (ImageButton) findViewById(R.id.kingsThemImBtn);
        thekingsTheme.setOnClickListener(this);
        //Boton
        saveSettings = (Button) findViewById(R.id.saveBtn);
        saveSettings.setOnClickListener(this);
        choice = false;
        saveSettings.setEnabled(false);

    }

    public void onClick(View v) {
        switch(v.getId()){
            case R.id.santaThemImBtn:
                them = "santa";
                settingsPane.setBackground(getDrawable(R.drawable.santa_claus));
                choice = true;
                saveSettings.setEnabled(true);
                break;
            case R.id.kingsThemImBtn:
                them =  "the3kings";
                settingsPane.setBackground(getDrawable(R.drawable.reyes_magos));
                choice = true;
                saveSettings.setEnabled(true);
                break;
            case R.id.saveBtn:
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                intent.putExtra("mode", mod);
                intent.putExtra("theme",them);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
        switch (checkId) {
            case R.id.ezMode:
                mod = "easy";
                checked = true;
                break;
            case R.id.hardMode:
                mod = "hard";
                checked = true;
                break;
        }
    }
}