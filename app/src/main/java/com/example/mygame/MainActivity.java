package com.example.mygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    ImageView playBtn, exitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playBtn = findViewById(R.id.play_button);
        exitBtn = findViewById(R.id.exit_button);

        playBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch(motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        playBtn.setImageResource(R.drawable.wooden_play_press);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        playBtn.setImageResource(R.drawable.wooden_play);
                        break;
                    case MotionEvent.ACTION_UP:
                        Intent i = new Intent(MainActivity.this, StartGame.class);
                        startActivity(i);
                        playBtn.setImageResource(R.drawable.wooden_play);
                        break;
                }

                return true;
            }
        });

        exitBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch(motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        exitBtn.setImageResource(R.drawable.wooden_cross_press);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        exitBtn.setImageResource(R.drawable.wooden_cross_press);
                        break;
                    case MotionEvent.ACTION_UP:
                        finish();
                        System.exit(0);
                        break;
                }

                return true;
            }
        });
    }

    private boolean checkTouchPosition(MotionEvent event, ImageView btn){
        /** divided by density because btn.getLeft() or btn.getWidth() return values in pixels, whereas
         * event.getX() returns values in density pixels
         */
        float density = getResources().getDisplayMetrics().density;
        float btnX = btn.getX() / density, btnY = btn.getY() / density;
        float btnWidth = btn.getWidth() / density, btnHeight = btn.getHeight() / density;
        float eventX = event.getX(), eventY = event.getY();

        if((eventX >= btnX) && (eventX <= btnX+btnWidth) && (eventY >= btnY) && (eventY <= btnY+btnHeight)) {
            return true;
        }
        return false;
    }
}