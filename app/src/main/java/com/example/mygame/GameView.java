package com.example.mygame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintSet;

import java.util.ArrayList;

public class GameView extends View {

    Bitmap background, target, stone;
    Bitmap pauseBtn, playBtn;
    Rect rect;
    Paint paint, dividerPaint;
    static int dWidth, dHeight;

    Handler handler;
    Runnable runnable;
    int UPDATE_MILLIS = 15;
    int NO_OF_BIRDS = 2;
    ArrayList<Bird1> birds;

    MediaPlayer shoot, bird_hit, bird_miss;
    Context context;

    float stoneX, stoneY;
    float sX, sY, fX, fY; /** sX and sY for ACTION_DOWN, fX and fY for ACTION_MOVE and ACTION_UP */
    float dX, dY; /** displacement of Stone */
    float tempX, tempY; /** Change in the value of X and Y for Stone*/

    float pauseX, pauseY; /** X, Y coordinates of the pause and play button */

    int life, score;

    boolean gameState, isPaused;

    int TOTAL_LIVES = 5;

    public GameView(Context context) {
        super(context);
        background = BitmapFactory.decodeResource(getResources(), R.drawable.background6);

        this.context = context;

        target = BitmapFactory.decodeResource(getResources(), R.drawable.catapult);
        stone = BitmapFactory.decodeResource(getResources(), R.drawable.stone_new2);

        pauseBtn = BitmapFactory.decodeResource(getResources(), R.drawable.pause_wooden);
        playBtn = BitmapFactory.decodeResource(getResources(), R.drawable.play_wooden);

        paintInit();

        createBackground();

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

        stoneX = stoneY = 0;
        sX = sY = fX = fY = 0;
        dX = dY = 0;
        tempX = tempY = 0;

        life = TOTAL_LIVES;
        score = 0;

        bird_hit = MediaPlayer.create(context, R.raw.hit_sound_short);
        bird_miss = MediaPlayer.create(context, R.raw.bird_chirping_short);
        shoot = MediaPlayer.create(context, R.raw.shoot_sound_short);
        shoot.setVolume(1, 1);

        gameState = true;
        isPaused = false;

        //pauseX = dWidth - 160;
        //pauseY = dHeight - 180;
        pauseX = 30;
        pauseY = 30;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(background, null, rect, null);
        canvas.drawRect(0, dHeight, dWidth, dHeight+20, dividerPaint);
        canvas.drawBitmap(target, dWidth/2 - (target.getWidth()/2), (dHeight*.80f) - (target.getHeight()/2), null);

        if(life <= 0){
            gameOver();
        }
        drawlives(canvas);

        renderBirds(canvas);

        if(isPaused){
            canvas.drawBitmap(playBtn, pauseX, pauseY, null);
        }else {
            canvas.drawBitmap(pauseBtn, pauseX, pauseY, null);
        }

        slingShot(canvas);

        if(gameState){
            handler.postDelayed(runnable, UPDATE_MILLIS);
        }
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                dX = dY = fX = fY = tempX = tempY = 0;
                sX = dWidth / 2;
                sY = dHeight * .80f;
                break;
            case MotionEvent.ACTION_MOVE:
                fX = event.getX();
                fY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                checkPausePlay(event);
                fX = event.getX();
                fY = event.getY();
                stoneX = fX;
                stoneY = fY;
                dX = fX - sX;
                dY = fY - sY;
                if(shoot != null){
                    shoot.start();
                }
                break;
        }
        return true;
    }

    private void renderBirds(Canvas canvas){
        for(int i = 0; i < birds.size(); i++){
            Bird1 bird = birds.get(i);

            if(bird.isAlive()){
                canvas.drawBitmap(bird.getBitmap(), bird.birdX, bird.birdY, null);
                if(isPaused){
                    continue;
                }

                bird.birdFrame++;
                if(bird.birdFrame > 13){
                    bird.birdFrame = 0;
                }

                bird.birdX -= bird.velocity;
                if(bird.birdX <= (-1 * bird.getWidth())){
                    decrementLife();
                    bird.resetPosition();
                }
            }else {
                canvas.drawBitmap(bird.getBloodBitmap(), bird.birdX, bird.birdY, null);
                if(isPaused){
                    continue;
                }
                bird.bloodFrame++;
                if(bird.bloodFrame > 9){
                    bird.bloodFrame = 0;
                    bird.resetPosition();
                }
            }

            detectCollision(bird);
        }
    }

    private void detectCollision(Bird1 bird){
        if((stoneX <= bird.birdX + bird.getWidth())
        && (stoneX + stone.getWidth() >= bird.birdX)
        && (stoneY <= bird.birdY + bird.getHeight())
        && (stoneY >= bird.birdY)) {
            incrementScore();
            bird.startBirdKilling();

            dX = dY = fX = fY = tempX = tempY = 0;
            stoneX = stoneY = 0;
        }
    }

    private void slingShot(Canvas canvas){
        if(isPaused ||(fX <= pauseX && fY <= pauseY)){
            return;
        }

        if((Math.abs(fX - sX) > 0 || Math.abs(fY - sY) > 0) && (sY > dHeight * .75 && fY > dHeight * .75)){
            if(tempX == 0 && tempY == 0){ /** To remove stone once user releases finger */
                canvas.drawBitmap(stone, fX - (stone.getWidth()/2), fY - (stone.getWidth()/2), null);
                canvas.drawLine(sX - target.getWidth()/4, sY - target.getHeight()/3, fX-40, fY-40, paint);
                canvas.drawLine(sX + target.getWidth()/4 - 10, sY - target.getHeight()/3, fX+40, fY-40, paint);
            }
        }
        if((Math.abs(dX) > 10 || Math.abs(dY) > 10) && (sY > dHeight * .75 && fY > dHeight * .75)){
            stoneX = fX - (stone.getWidth() / 2) - tempX;
            stoneY = fY - (stone.getHeight() / 2) - tempY;
            canvas.drawBitmap(stone, stoneX, stoneY, null);
            tempX += dX/2;
            tempY += dY;
        }
    }

    private void paintInit(){
        paint = new Paint();
        paint.setARGB(255, 100, 10, 10);
        paint.setStrokeWidth(15);
        paint.setStyle(Paint.Style.STROKE);
        //paint.setPathEffect(new DashPathEffect(new float[]{5, 10, 15, 20}, 0));

        dividerPaint = new Paint();
        dividerPaint.setStrokeWidth(10);
        dividerPaint.setARGB(255, 10, 100, 10);
        dividerPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    private void decrementLife(){
        if(bird_miss != null){
            bird_miss.start();
        }
        life--;
    }

    private void incrementScore() {
        if (bird_hit != null) {
            bird_hit.start();
        }
        score++;
    }

    private  void gameOver(){
        gameState = false;
        Intent intent = new Intent(context, GameOver.class);
        intent.putExtra("score", score);
        context.startActivity(intent);
        ((Activity) context).finish();
    }

    private void drawlives(Canvas canvas){
        Bitmap heart = BitmapFactory.decodeResource(context.getResources(), R.drawable.heart_icon);
        int heartX = 30, heartY = dHeight - 170;

        for(int i = 0; i < life; i++){
            canvas.drawBitmap(heart, heartX, heartY, null);
            heartY -= 120;
        }
    }

    private void createBackground(){
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();

        Point point = new Point();
        display.getSize(point);
        dWidth = point.x;
        dHeight=  point.y;

        rect = new Rect(0, 0, dWidth, dHeight);
    }

    private void checkPausePlay(MotionEvent event){
        float pressX = event.getX();
        float pressY = event.getY();

        if(pressX <= pauseX && pressY <= pauseY){
            if(isPaused){
                isPaused = false;
            }else {
                isPaused = true;
            }
        }
    }
}
