package com.example.mygame;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;

public class StartGame extends AppCompatActivity {

    GameView gameView;

    MediaPlayer bgMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        gameView = new GameView(this);
        setContentView(gameView);

        bgMusic = MediaPlayer.create(this, R.raw.bg_music);
        if(bgMusic != null){
            bgMusic.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(bgMusic != null){
            bgMusic.stop();
            bgMusic.release();
        }
    }
}