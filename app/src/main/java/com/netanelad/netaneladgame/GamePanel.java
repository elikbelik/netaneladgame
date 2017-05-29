package com.netanelad.netaneladgame;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    // To save preferences and scores
    public static final String PREF_NAME = "pref";
    public static final String PREF_SCORE_NAME = "score";

    // Game parameters
    public static final int WIDTH = 856;
    public static final int HEIGHT = 480;
    public static final int MOVESPEED = -5;
    private static final int TIME_FOR_SMOKEPUFF = 120;
    private static final int TIME_FOR_RESET = 1000;
    private static final int SMOKEPUFF_LOCATION_OFFSET = 10;
    private static final int EXPLOSION_LOCATION_OFFSET = 30;
    private static final int MISSILE_MAX_TIME_DELAY = 2000;
    private static final int MISSILE_SCORE_TO_TIME_RATIO = 4;

    // Panel control members
    private long smokeStartTime;
    private long missilesStartTime;
    private long resetstartTime;
    private MainThread thread;
    private Background bg;
    private Player player;
    private ArrayList<GameObject> objectsList;
    private Random rand = new Random();
    private boolean newGameCreated;
    private Explosion explosion;
    private boolean reset;
    private boolean dissapear;
    private boolean notFirstRun = false;
    private int bestScore;
    SharedPreferences sharedPref;


    public GamePanel(Context context) {
        super(context);

        //Add the callback to the surfaceHolder to intercept events
        getHolder().addCallback(this);

        // Make gamePanel focusable so it can handle events
        setFocusable(true);

        // Preferences to save the best score
        sharedPref = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        bestScore = sharedPref.getInt(PREF_SCORE_NAME, 0);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.grassbg1));
        player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.helicopter), 65, 25, 3);
        objectsList = new ArrayList<GameObject>();
        smokeStartTime = System.nanoTime();
        missilesStartTime = System.nanoTime();

        thread = new MainThread(getHolder(), this);
        // We can safely start the game
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        boolean retry = true;
        int counter = 0;
        while (retry && counter < 1000) {
            counter++;
            try {
                thread.setRunning(false);
                thread.join();
                retry = false;
                thread = null;
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (!player.getPlaying() && newGameCreated && reset) {
                player.setPlaying(true);
                player.setUp(true);
            }
            if (player.getPlaying()) {
                notFirstRun = true;
                reset = false;
                player.setUp(true);
            }
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            player.setUp(false);
            return true;
        }
        return super.onTouchEvent(event);
    }

    public void update() {
        if (player.getPlaying()) {
            bg.update();
            player.update();
            Border.updateScore(player.getScore());

            // Add objects on timer
            addMissile();
            addSmokepuffs();

            // Update and remove and check for collision
            for (int i=0; i< objectsList.size(); i++) {
                GameObject obj = objectsList.get(i);
                obj.update();
                if (obj.shouldRemove()) {
                    objectsList.remove(i);
                    i--;
                }
                if (obj.isColidable() && collision(obj, player)) {
                    objectsList.remove(i);
                    player.setPlaying(false);
                    i--;
                }
            }
        }
        else {
            player.resetDy();
            if (!reset) {
                newGameCreated = false;
                resetstartTime = System.nanoTime();
                reset = true;
                dissapear = true;
                explosion = new Explosion(getContext(), player.getX(), player.getY()-EXPLOSION_LOCATION_OFFSET);
            }
            explosion.update();
            long resetElapsed = (System.nanoTime()- resetstartTime)/MainThread.SEC_TO_MILI;
            if (resetElapsed > TIME_FOR_RESET && !newGameCreated) {
                newGame();
            }
            if (newGameCreated) {
                bg.update();
            }
        }
    }

    public void addMissile() {
        long missileElapsed = (System.nanoTime()- missilesStartTime)/MainThread.SEC_TO_MILI;
        if (missileElapsed > (MISSILE_MAX_TIME_DELAY - player.getScore()/MISSILE_SCORE_TO_TIME_RATIO)) {
            objectsList.add(new Missile(getContext(), WIDTH+10,
                    (int)(rand.nextDouble()*HEIGHT), player.getScore()));
            // Reset timer
            missilesStartTime = System.nanoTime();
        }
    }

    public void addSmokepuffs() {
        long elapsed = (System.nanoTime() - smokeStartTime)/ MainThread.SEC_TO_MILI;
        if (elapsed > TIME_FOR_SMOKEPUFF) {
            objectsList.add(new Smokepuff(player.getX(), player.getY()+SMOKEPUFF_LOCATION_OFFSET));
            smokeStartTime = System.nanoTime();
        }
    }

    public boolean collision (GameObject a, GameObject b) {
        if (Rect.intersects(a.getRectangle(), b.getRectangle())) {
            return true;
        }
        return false;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        final float scaleFactorX = 1.f*getWidth()/WIDTH;
        final float scaleFactorY = 1.f*getHeight()/HEIGHT;
        if (canvas != null) {
            final int savedState = canvas.save();

            canvas.scale(scaleFactorX, scaleFactorY);
            bg.draw(canvas);
            if (!dissapear) {
                player.draw(canvas);
            }

            for (GameObject obj : objectsList) {
                if (!dissapear || !obj.hideWhenDead())
                    obj.draw(canvas);
            }

            // Draw explosion
            if (notFirstRun) {
                explosion.draw(canvas);
            }

            drawText(canvas);
            canvas.restoreToCount(savedState);
        }
    }

    public void newGame() {
        dissapear = false;
        for (int i=0; i < objectsList.size(); i++) {
            if (objectsList.get(i).removeWhenDead()) {
                objectsList.remove(i);
                i--;
            }
        }
        Border.resetBordersCounter();

        player.resetDy();
        player.setY(HEIGHT/2);

        if (player.getScore() > bestScore) {
            bestScore = player.getScore();
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt(PREF_SCORE_NAME, bestScore);
            editor.commit();
        }
        player.resetScore();

        // Create initial borders
        for (int i=0; i*Border.BORDER_WIDTH<WIDTH+40; i++) {
            // Border
            objectsList.add(new Border(getContext(), i* Border.BORDER_WIDTH, 10+i, Border.BorderType.Top));
            // BotBorder
            objectsList.add(new Border(getContext(), i* Border.BORDER_WIDTH, 10+i, Border.BorderType.Bottom));
        }

        newGameCreated = true;
    }

    public void drawText (Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.GRAY);
        paint.setTextSize(30);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("DISTANCE: " + player.getScore(), 14, HEIGHT-10, paint);
        canvas.drawText("BEST: " + bestScore, WIDTH - 213, HEIGHT-10, paint);
        paint.setColor(Color.DKGRAY);
        canvas.drawText("DISTANCE: " + player.getScore(), 10, HEIGHT-10, paint);
        canvas.drawText("BEST: " + bestScore, WIDTH - 215, HEIGHT - 10, paint);

        if (!player.getPlaying() && newGameCreated && reset) {
            Paint paint1 = new Paint();
            paint1.setTextSize(40);
            paint1.setColor(Color.LTGRAY);
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            canvas.drawText("PRESS TO START", WIDTH/2-50, HEIGHT/2, paint1);

            paint1.setTextSize(20);
            paint1.setColor(Color.LTGRAY);
            canvas.drawText("PRESS AND HOLD TO GO UP", WIDTH/2-50, HEIGHT/2+20, paint1);
            canvas.drawText("RELEASE TO GO DOWN", WIDTH/2-50, HEIGHT/2+40, paint1);
        }
    }
}
