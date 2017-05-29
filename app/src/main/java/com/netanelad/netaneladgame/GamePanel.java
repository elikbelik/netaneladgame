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
    private long m_smokeStartTime;
    private long m_missilesStartTime;
    private MainThread m_thread;
    private Background m_bg;
    private Player m_player;
    private ArrayList<Smokepuff> m_smoke;
    private ArrayList<Missile> m_missiles;
    private ArrayList<TopBorder> m_topBorder;
    private ArrayList<BotBorder> m_botBorder;
    private Random m_rand = new Random();
    private int m_maxBorderHeight;
    private int m_minBorderHeight;
    private boolean m_topDown = true;
    private boolean m_botDown = true;
    private boolean m_newGameCreated;

    private Explosion m_explosion;
    private long m_startReset;
    private boolean m_reset;
    private boolean m_dissapear;
    private boolean m_started;
    private int m_best;
    SharedPreferences m_sharedPref;

    // Increase to slow down difficulty progression, decrease to speed up difficulty progression
    private int m_progressDenom = 20;

    public GamePanel(Context context) {
        super(context);

        //Add the callback to the surfaceHolder to intercept events
        getHolder().addCallback(this);

        // Make gamePanel focusable so it can handle events
        setFocusable(true);

        // Preferences to save the best score
        m_sharedPref = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        m_best = m_sharedPref.getInt(PREF_SCORE_NAME, 0);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        m_bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.grassbg1));
        m_player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.helicopter), 65, 25, 3);
        m_smoke = new ArrayList<Smokepuff>();
        m_missiles = new ArrayList<Missile>();
        m_topBorder = new ArrayList<TopBorder>();
        m_botBorder = new ArrayList<BotBorder>();
        m_smokeStartTime = System.nanoTime();
        m_missilesStartTime = System.nanoTime();

        m_thread = new MainThread(getHolder(), this);
        // We can safely start the game
        m_thread.setRunning(true);
        m_thread.start();
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
                m_thread.setRunning(false);
                m_thread.join();
                retry = false;
                m_thread = null;
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (!m_player.getPlaying() && m_newGameCreated && m_reset) {
                m_player.setPlaying(true);
                m_player.setUp(true);
            }
            if (m_player.getPlaying()) {
                if (!m_started) m_started = true;
                m_reset = false;
                m_player.setUp(true);
            }
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            m_player.setUp(false);
            return true;
        }
        return super.onTouchEvent(event);
    }

    public void update() {
        if (m_player.getPlaying()) {
            if (m_botBorder.isEmpty()) {
                m_player.setPlaying(false);
                return;
            }
            if (m_topBorder.isEmpty()) {
                m_player.setPlaying(false);
                return;
            }
            m_bg.update();
            m_player.update();

            // Calculate the threshold of height the border can have based on the score
            // Max and min border heart are updated, and the border switched direction when either max or min is met
            m_maxBorderHeight = 30+m_player.getScore()/m_progressDenom;
            // Cap max border height so that borders can only take up a total of 1/2 the screen
            if (m_maxBorderHeight > HEIGHT/4) m_maxBorderHeight = HEIGHT/4;
            m_minBorderHeight = 5+m_player.getScore()/m_progressDenom;

             // Check borders collisions
            for (int i=0; i<m_topBorder.size(); i++) {
                if (collision(m_topBorder.get(i),m_player)) {
                    m_player.setPlaying(false);
                }
            }
            for (int i=0; i<m_botBorder.size(); i++) {
                if (collision(m_botBorder.get(i),m_player)) {
                    m_player.setPlaying(false);
                }
            }

            // Update top border
            updateTopBorder();
            // Update bottom border
            updateBottomBorder();

            // Add missiles on timer
            long missileElapsed = (System.nanoTime()-m_missilesStartTime)/1000000;
            if (missileElapsed > (2000 - m_player.getScore()/4)) {

                // First missile always goes down the middle
                if (m_missiles.size() == 0) {
                    m_missiles.add(new Missile(BitmapFactory.decodeResource(getResources(),
                            R.drawable.missile),WIDTH+10, HEIGHT/2, 45, 15, m_player.getScore(), 13));
                }
                else {
                    m_missiles.add(new Missile(BitmapFactory.decodeResource(getResources(),R.drawable.missile),
                            WIDTH+10, (int)(m_rand.nextDouble()*HEIGHT - m_maxBorderHeight*2) + m_maxBorderHeight,
                            45, 15, m_player.getScore(), 13));
                }
                // Reset timer
                m_missilesStartTime = System.nanoTime();
            }

            // Loop through every missile and check collision
            for (int i=0; i<m_missiles.size(); i++) {
                m_missiles.get(i).update();
                if (collision(m_missiles.get(i), m_player)) {
                    m_missiles.remove(i);
                    m_player.setPlaying(false);
                    break;
                }
                // Remove missile if it is way off the screen
                if (m_missiles.get(i).getX()<-100) {
                    m_missiles.remove(i);
                    break;
                }
            }

            // Add smokepuffs on timer
            long elapsed = (System.nanoTime() - m_smokeStartTime)/ 1000000;
            if (elapsed > 120) {
                m_smoke.add(new Smokepuff(m_player.getX(), m_player.getY()+10));
                m_smokeStartTime = System.nanoTime();
            }

            for (int i=0; i< m_smoke.size(); i++) {
                m_smoke.get(i).update();
                if (m_smoke.get(i).getX()<-10) {
                    m_smoke.remove(i);
                    i--;
                }
            }
        }
        else {
            m_player.resetDy();
            if (!m_reset) {
                m_newGameCreated = false;
                m_startReset = System.nanoTime();
                m_reset = true;
                m_dissapear = true;
                m_explosion = new Explosion(BitmapFactory.decodeResource(getResources(),
                        R.drawable.explosion),m_player.getX(),m_player.getY()-30,100,100,25);
            }
            m_explosion.update();
            long resetElapsed = (System.nanoTime()-m_startReset)/1000000;
            if (resetElapsed > 1000 && !m_newGameCreated) {
                newGame();
            }
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
            m_bg.draw(canvas);
            if (!m_dissapear) {
                m_player.draw(canvas);
                for (Smokepuff sp : m_smoke) {
                    sp.draw(canvas);
                }
            }

            for (Missile m: m_missiles) {
                m.draw(canvas);
            }

            // Draw topborder
            for (TopBorder tb: m_topBorder) {
                tb.draw(canvas);
            }

            // Draw botborder
            for (BotBorder bb: m_botBorder) {
                bb.draw(canvas);
            }

            // Draw explosion
            if (m_started) {
                m_explosion.draw(canvas);
            }

            drawText(canvas);
            canvas.restoreToCount(savedState);
        }
    }

    public void updateBottomBorder () {
        // Every 40 points insert randomly placed top blocks that break the pattern
        if (m_player.getScore()%40 == 0) {
            m_botBorder.add(new BotBorder(BitmapFactory.decodeResource(getResources(), R.drawable.brick),
                    m_botBorder.get(m_botBorder.size()-1).getX()+20,(int)(m_rand.nextDouble()*m_maxBorderHeight)+HEIGHT-m_maxBorderHeight));
        }
        for (int i=0; i<m_botBorder.size(); i++) {
            m_botBorder.get(i).update();
            if (m_botBorder.get(i).getX()<-20) {
                // Remove element of arraylist, replace it by adding a new one
                m_botBorder.remove(i);
                i--;

                // Calculate topdown which determines the direction the border is moving (up or down)
                if (m_botBorder.get(m_botBorder.size()-1).getY() <= HEIGHT - m_maxBorderHeight) {
                    m_botDown = true;
                }
                if (m_botBorder.get(m_botBorder.size()-1).getY() >= HEIGHT - m_minBorderHeight) {
                    m_botDown = false;
                }

                // New border added will have larger height
                if (m_botDown) {
                    m_botBorder.add(new BotBorder(BitmapFactory.decodeResource(getResources(), R.drawable.brick),
                            m_botBorder.get(m_botBorder.size()-1).getX()+20, m_botBorder.get(m_botBorder.size()-1).getY()+1));
                }
                // New border added will have smaller height
                else {
                    m_botBorder.add(new BotBorder(BitmapFactory.decodeResource(getResources(), R.drawable.brick),
                            m_botBorder.get(m_botBorder.size()-1).getX()+20, m_botBorder.get(m_botBorder.size()-1).getY()-1));
                }
            }
        }
    }

    public void updateTopBorder () {
        // Every 50 points insert randomly placed top blocks that break the pattern
        if (m_player.getScore()%50 == 0) {
            m_topBorder.add(new TopBorder(BitmapFactory.decodeResource(getResources(), R.drawable.brick),
                    m_topBorder.get(m_topBorder.size()-1).getX()+20,0,(int)(m_rand.nextDouble()*m_maxBorderHeight)+1));
        }
        for (int i=0; i<m_topBorder.size(); i++) {
            m_topBorder.get(i).update();
            if (m_topBorder.get(i).getX()<-20) {
                // Remove element of arraylist, replace it by adding a new one
                m_topBorder.remove(i);
                i--;

                // Calculate topdown which determines the direction the border is moving (up or down)
                if (m_topBorder.get(m_topBorder.size()-1).getHeight() >= m_maxBorderHeight) {
                    m_topDown = false;
                }
                if (m_topBorder.get(m_topBorder.size()-1).getHeight()<=m_minBorderHeight) {
                    m_topDown = true;
                }

                // New border added will have larger height
                if (m_topDown) {
                    m_topBorder.add(new TopBorder(BitmapFactory.decodeResource(getResources(), R.drawable.brick),
                            m_topBorder.get(m_topBorder.size()-1).getX()+20, 0, m_topBorder.get(m_topBorder.size()-1).getHeight()+1));
                }
                // New border added will have smaller height
                else {
                    m_topBorder.add(new TopBorder(BitmapFactory.decodeResource(getResources(), R.drawable.brick),
                            m_topBorder.get(m_topBorder.size()-1).getX()+20, 0, m_topBorder.get(m_topBorder.size()-1).getHeight()-1));
                }
            }
        }
    }

    public void newGame() {
        m_dissapear = false;
        m_botBorder.clear();
        m_topBorder.clear();
        m_missiles.clear();
        m_smoke.clear();

        m_minBorderHeight = 5;
        m_maxBorderHeight = 30;
        m_player.resetDy();
        m_player.setY(HEIGHT/2);

        if (m_player.getScore() > m_best) {
            m_best = m_player.getScore();
            SharedPreferences.Editor editor = m_sharedPref.edit();
            editor.putInt(PREF_SCORE_NAME, m_best);
            editor.commit();
        }
        m_player.resetScore();

        // Create initial borders
        for (int i=0; i*20<WIDTH+40; i++) {
            if (i==0) {
                m_topBorder.add(new TopBorder(BitmapFactory.decodeResource(getResources(), R.drawable.brick),
                        i * 20, 0, 10));
                m_botBorder.add(new BotBorder(BitmapFactory.decodeResource(getResources(),R.drawable.brick),
                        i*20, HEIGHT-m_minBorderHeight));
            }
            else {
                m_topBorder.add(new TopBorder(BitmapFactory.decodeResource(getResources(), R.drawable.brick),
                        i * 20, 0, m_topBorder.get(i-1).getHeight()+1));
                m_botBorder.add(new BotBorder(BitmapFactory.decodeResource(getResources(),R.drawable.brick),
                        i*20, m_botBorder.get(i-1).getY()-1));
            }
        }

        m_newGameCreated = true;
    }

    public void drawText (Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.GRAY);
        paint.setTextSize(30);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("DISTANCE: " + m_player.getScore(), 14, HEIGHT-10, paint);
        canvas.drawText("BEST: " + m_best, WIDTH - 213, HEIGHT-10, paint);
        paint.setColor(Color.DKGRAY);
        canvas.drawText("DISTANCE: " + m_player.getScore(), 10, HEIGHT-10, paint);
        canvas.drawText("BEST: " + m_best, WIDTH - 215, HEIGHT - 10, paint);

        if (!m_player.getPlaying() && m_newGameCreated && m_reset) {
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
