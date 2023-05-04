package com.example.mygame;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class GameOver extends AppCompatActivity {

    MediaPlayer gameOverMusic;
    TextView scoreTV;
    ImageView restartButton, homeButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_game_over);

        playMusic();

        scoreTV = findViewById(R.id.score_tv);
        restartButton = findViewById(R.id.restart_btn);
        homeButton = findViewById(R.id.close_game);

        Intent i = getIntent();
        int score = i.getIntExtra("score", 0);

        String scoreString = scoreTV.getText().toString();
        scoreString += score;
        scoreTV.setText(scoreString);

        restartButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch(motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        restartButton.setImageResource(R.drawable.restart_wood_press);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        restartButton.setImageResource(R.drawable.restart_wood_press);
                        break;
                    case MotionEvent.ACTION_UP:
                        Intent i = new Intent(GameOver.this, StartGame.class);
                        startActivity(i);
                        finish();
                        restartButton.setImageResource(R.drawable.restart_wood);
                        break;
                }
                return true;
            }
        });

        homeButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch(motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        homeButton.setImageResource(R.drawable.wooden_home_press);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        homeButton.setImageResource(R.drawable.wooden_home_press);
                        break;
                    case MotionEvent.ACTION_UP:
                        Intent i = new Intent(GameOver.this, MainActivity.class);
                        startActivity(i);
                        finish();
                        homeButton.setImageResource(R.drawable.wooden_home);
                        break;
                }
                return true;
            }
        });
    }

    private void playMusic(){
        gameOverMusic = MediaPlayer.create(GameOver.this, R.raw.game_over);
        gameOverMusic.setLooping(false);
        gameOverMusic.start();
    }
}