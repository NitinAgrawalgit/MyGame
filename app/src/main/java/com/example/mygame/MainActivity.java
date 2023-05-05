package com.example.mygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Rect;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    LinearLayout rootLayout;
    View emptyView;
    ImageView playBtn, exitBtn, infoBtn;
    ImageView infoView;
    boolean isPressed = false;
    boolean isInfoVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_main);

        rootLayout = findViewById(R.id.root_view);

        playBtn = findViewById(R.id.play_button);
        exitBtn = findViewById(R.id.exit_button);
        infoBtn = findViewById(R.id.info_button);

        infoView = findViewById(R.id.info_view);
        emptyView = findViewById(R.id.empty_frame);

        playBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch(motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        playBtn.setImageResource(R.drawable.wooden_play_press);
                        isPressed = true;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        playBtn.setImageResource(R.drawable.wooden_play_press);
                        isPressed = false;
                        break;
                    case MotionEvent.ACTION_UP:
                        if(isPressed){
                            Intent i = new Intent(MainActivity.this, StartGame.class);
                            startActivity(i);
                        }
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
                        isPressed = true;
                        exitBtn.setImageResource(R.drawable.wooden_cross_press);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        isPressed = false;
                        exitBtn.setImageResource(R.drawable.wooden_cross_press);
                        break;
                    case MotionEvent.ACTION_UP:
                        if(isPressed){
                            finishAffinity();
                        }
                        exitBtn.setImageResource(R.drawable.wooden_cross);
                        break;
                }

                return true;
            }
        });

        infoBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch(motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        isPressed = true;
                        infoBtn.setImageResource(R.drawable.info_press);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        isPressed = false;
                        infoBtn.setImageResource(R.drawable.info_press);
                        break;
                    case MotionEvent.ACTION_UP:
                        if(isPressed && !isInfoVisible){
                            infoView.setVisibility(View.VISIBLE);
                            isInfoVisible = true;
                            infoBtn.setImageResource(R.drawable.info_press);
                            emptyView.setVisibility(View.VISIBLE);
                        }else {
                            infoView.setVisibility(View.GONE);
                            isInfoVisible = false;
                            infoBtn.setImageResource(R.drawable.info2);
                            emptyView.setVisibility(View.GONE);
                        }
                        break;
                }

                return true;
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Rect rect = new Rect();
        if(isInfoVisible && !rect.contains((int)ev.getRawX(), (int)ev.getRawY())){
            isInfoVisible = false;
            infoView.setVisibility(View.GONE);
            infoBtn.setImageResource(R.drawable.info2);
            emptyView.setVisibility(View.GONE);
            return true;
        }

        return super.dispatchTouchEvent(ev);
    }
}