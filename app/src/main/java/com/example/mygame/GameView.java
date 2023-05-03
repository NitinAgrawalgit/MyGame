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
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class GameView extends View {

    Bitmap background, target, stone;
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

    int life, score;

    boolean gameState;

    int TOTAL_LIVES = 3;

    public GameView(Context context) {
        super(context);
        background = BitmapFactory.decodeResource(getResources(), R.drawable.background6);

        target = BitmapFactory.decodeResource(getResources(), R.drawable.catapult);
        stone = BitmapFactory.decodeResource(getResources(), R.drawable.stone_new2);

        paintInit();

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

        stoneX = stoneY = 0;
        sX = sY = fX = fY = 0;
        dX = dY = 0;
        tempX = tempY = 0;

        life = TOTAL_LIVES;
        score = 0;

        this.context = context;

        bird_hit = MediaPlayer.create(context, R.raw.hit_sound);
        bird_miss = MediaPlayer.create(context, R.raw.bird_chirping);
        shoot = MediaPlayer.create(context, R.raw.shoot_sound);
        shoot.setVolume(1, 1);

        gameState = true;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(background, null, rect, null);
        canvas.drawBitmap(target, dWidth/2 - (target.getWidth()/2), (dHeight*.80f) - (target.getHeight()/2), null);

        //canvas.drawLine(0, dHeight * .80f, dWidth, dHeight * .80f, dividerPaint);

        if(life <= 0){
            gameOver();
        }
        drawlives(canvas);

        renderBirds(canvas);

        slingShot(canvas);

        if(gameState){
            handler.postDelayed(runnable, UPDATE_MILLIS);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                dX = dY = fX = fY = tempX = tempY = 0;
                sX = dWidth / 2;
                sY = dHeight * .80f;
                /**
                sX = event.getX();
                sY = event.getY();
                 */
                break;
            case MotionEvent.ACTION_MOVE:
                fX = event.getX();
                fY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
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
        if(sX > 0 && sY > (dHeight * .75f)){
            canvas.drawBitmap(target, sX - (target.getWidth()/2), sY - (target.getHeight()/2), null);
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
        paint.setStrokeWidth(8);
        paint.setStyle(Paint.Style.STROKE);
        //paint.setPathEffect(new DashPathEffect(new float[]{5, 10, 15, 20}, 0));

        dividerPaint = new Paint();
        dividerPaint.setStrokeWidth(4);
        dividerPaint.setARGB(255, 10, 100, 10);
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
        int heartX = dWidth-120, heartY = dHeight-120;

        for(int i = 0; i < life; i++){
            canvas.drawBitmap(heart, heartX, heartY, null);
            heartX -= 100;
        }
    }
}
