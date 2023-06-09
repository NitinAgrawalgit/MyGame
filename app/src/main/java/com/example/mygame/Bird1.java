package com.example.mygame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class Bird1 {

    Bitmap[] bird = new Bitmap[14];

    Bitmap[] blood = new Bitmap[10];

    int birdX, birdY, velocity, birdFrame;
    int bloodFrame;

    Random random;

    boolean alive;

    public Bird1(Context context){
        bird[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.frame00);
        bird[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.frame01);
        bird[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.frame02);
        bird[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.frame03);
        bird[4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.frame04);
        bird[5] = BitmapFactory.decodeResource(context.getResources(), R.drawable.frame05);
        bird[6] = BitmapFactory.decodeResource(context.getResources(), R.drawable.frame06);
        bird[7] = BitmapFactory.decodeResource(context.getResources(), R.drawable.frame07);
        bird[8] = BitmapFactory.decodeResource(context.getResources(), R.drawable.frame08);
        bird[9] = BitmapFactory.decodeResource(context.getResources(), R.drawable.frame09);
        bird[10] = BitmapFactory.decodeResource(context.getResources(), R.drawable.frame10);
        bird[11] = BitmapFactory.decodeResource(context.getResources(), R.drawable.frame11);
        bird[12] = BitmapFactory.decodeResource(context.getResources(), R.drawable.frame12);
        bird[13] = BitmapFactory.decodeResource(context.getResources(), R.drawable.frame13);

        blood[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.blood00);
        blood[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.blood01);
        blood[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.blood02);
        blood[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.blood03);
        blood[4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.blood04);
        blood[5] = BitmapFactory.decodeResource(context.getResources(), R.drawable.blood05);
        blood[6] = BitmapFactory.decodeResource(context.getResources(), R.drawable.blood06);
        blood[7] = BitmapFactory.decodeResource(context.getResources(), R.drawable.blood07);
        blood[8] = BitmapFactory.decodeResource(context.getResources(), R.drawable.blood08);
        blood[9] = BitmapFactory.decodeResource(context.getResources(), R.drawable.blood09);

        random = new Random();

        birdX = GameView.dWidth + 2000;
        birdY = random.nextInt(300);
        velocity = 10 + random.nextInt(10);

        birdFrame = 0;
        bloodFrame = 0;

        alive = true;
    }

    public Bitmap getBitmap(){
        return bird[birdFrame];
    }

    public int getWidth(){
        return bird[0].getWidth();
    }

    public int getHeight(){
        return bird[0].getHeight();
    }

    public void resetPosition(){
        alive = true;

        birdX = GameView.dWidth + random.nextInt(1200);
        birdY = random.nextInt(300);
        velocity = 10 + random.nextInt(10);
    }

    public void startBirdKilling() {
        alive = false;
    }

    public boolean isAlive(){
        return alive;
    }

    public Bitmap getBloodBitmap() {
        return blood[bloodFrame];
    }
}
