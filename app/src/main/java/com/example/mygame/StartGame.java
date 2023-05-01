package com.example.mygame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class StartGame extends AppCompatActivity {

    GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        gameView = new GameView(this);
        setContentView(gameView);
    }
}