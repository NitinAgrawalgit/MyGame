package com.example.mygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class GameOver extends AppCompatActivity {

    TextView scoreTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        scoreTV = findViewById(R.id.score_tv);

        Intent i = getIntent();
        int score = i.getIntExtra("score", 0);

        String scoreString = scoreTV.getText().toString();
        scoreString += score;
        scoreTV.setText(scoreString);
    }
}