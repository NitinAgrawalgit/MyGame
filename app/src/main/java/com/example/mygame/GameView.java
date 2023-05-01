package com.example.mygame;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.LogRecord;

public class GameView extends View {

    Bitmap background;
    Rect rect;
    static int dWidth, dHeight;

    Handler handler;
    Runnable runnable;
    int UPDATE_MILLIS = 30;
    int NO_OF_BIRDS = 3;

    ArrayList<Bird1> birds;

    public GameView(Context context) {
        super(context);
        background = BitmapFactory.decodeResource(getResources(), R.drawable.background);

        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        rect = new Rect();
        display.getRectSize(rect);

        dWidth = rect.width();
        dHeight=  rect.height();

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };

        birds = new ArrayList<>();
        for(int i = 0; i < NO_OF_BIRDS; i++){
            Bird1 bird = new Bird1(context);
            birds.add(bird);
        }
    }

    private void renderBirds(Canvas canvas){
        for(int i = 0; i < birds.size(); i++){
            Bird1 bird = birds.get(i);
            canvas.drawBitmap(bird.getBitmap(), bird.birdX, bird.birdY, null);
            bird.birdFrame++;
            if(bird.birdFrame > 13){
                bird.birdFrame = 0;
            }

            bird.birdX -= bird.velocity;
            if(bird.birdX <= (-1 * bird.getWidth())){
                bird.resetPosition();
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(background, null, rect, null);

        renderBirds(canvas);

        handler.postDelayed(runnable, UPDATE_MILLIS);
    }
}
