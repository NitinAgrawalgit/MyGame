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

import java.util.Random;
import java.util.logging.LogRecord;

public class GameView extends View {

    Bitmap background;

    Rect rect;
    int dWidth, dHeight;

    Bitmap[] bird = new Bitmap[14];
    int birdX, birdY, velocity, birdFrame;
    int birdWidth;

    Handler handler;
    Runnable runnable;

    int UPDATE_MILLIS = 30;

    public GameView(Context context) {
        super(context);
        background = BitmapFactory.decodeResource(getResources(), R.drawable.background);

        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        rect = new Rect();
        display.getRectSize(rect);

        dWidth = rect.width();
        dHeight=  rect.height();

        bird[0] = BitmapFactory.decodeResource(getResources(), R.drawable.frame00);
        bird[1] = BitmapFactory.decodeResource(getResources(), R.drawable.frame01);
        bird[2] = BitmapFactory.decodeResource(getResources(), R.drawable.frame02);
        bird[3] = BitmapFactory.decodeResource(getResources(), R.drawable.frame03);
        bird[4] = BitmapFactory.decodeResource(getResources(), R.drawable.frame04);
        bird[5] = BitmapFactory.decodeResource(getResources(), R.drawable.frame05);
        bird[6] = BitmapFactory.decodeResource(getResources(), R.drawable.frame06);
        bird[7] = BitmapFactory.decodeResource(getResources(), R.drawable.frame07);
        bird[8] = BitmapFactory.decodeResource(getResources(), R.drawable.frame08);
        bird[9] = BitmapFactory.decodeResource(getResources(), R.drawable.frame09);
        bird[10] = BitmapFactory.decodeResource(getResources(), R.drawable.frame10);
        bird[11] = BitmapFactory.decodeResource(getResources(), R.drawable.frame11);
        bird[12] = BitmapFactory.decodeResource(getResources(), R.drawable.frame12);
        bird[13] = BitmapFactory.decodeResource(getResources(), R.drawable.frame13);

        birdX = dWidth + 300;
        birdY = dHeight + 100;
        velocity = 15;

        birdFrame = 0;
        birdWidth = bird[0].getWidth();

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };

    }

    private void renderBird(Canvas canvas){
        canvas.drawBitmap(bird[birdFrame], birdX, birdY, null);
        birdFrame++;
        if(birdFrame > 13){
            birdFrame = 0;
        }
        birdX -= velocity;

        Random random = new Random();
        if(birdX <= (-1 * birdWidth)){
            birdX = dWidth + random.nextInt(500);
            birdY = random.nextInt(300);
            velocity = 14 + random.nextInt(10);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(background, null, rect, null);

        renderBird(canvas);

        handler.postDelayed(runnable, UPDATE_MILLIS);
    }
}
