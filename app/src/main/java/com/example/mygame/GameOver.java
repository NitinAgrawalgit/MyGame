package com.example.mygame;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class GameOver extends AppCompatActivity {

    TextView scoreTV;

    ImageView restartButton, exitButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_game_over);

        scoreTV = findViewById(R.id.score_tv);
        restartButton = findViewById(R.id.restart_btn);
        exitButton = findViewById(R.id.close_game);

        Intent i = getIntent();
        int score = i.getIntExtra("score", 0);

        String scoreString = scoreTV.getText().toString();
        scoreString += score;
        scoreTV.setText(scoreString);

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(GameOver.this, StartGame.class);
                startActivity(i);
                finish();
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                System.exit(0);
            }
        });
    }
}