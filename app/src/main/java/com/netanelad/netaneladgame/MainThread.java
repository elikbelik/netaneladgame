package com.netanelad.netaneladgame;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by elad on 08/03/2017.
 */

public class MainThread extends Thread {

    public static final int SEC_TO_MILI = 1000000;
    private static final int FPS = 30;
    private SurfaceHolder surfaceHolder;
    private GamePanel gamePanel;
    private boolean running;
    public static Canvas canvas;

    public MainThread (SurfaceHolder surfaceHolder, GamePanel gamePanel) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
    }

    @Override
    public void run() {
        //super.run();
        long startTime;
        long timeMillis;
        long waitTime;
        long totalTime = 0;
        long frameCount = 0;
        long targetTime = 1000/FPS;

        while (running) {
            startTime = System.nanoTime();
            canvas = null;

            // Try locking the canvas for pixel editing
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    this.gamePanel.update();
                    this.gamePanel.draw(canvas);
                }
            }
            catch (Exception e) {}
            finally {
                if (canvas !=null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                    catch (Exception e) {e.printStackTrace();}
                }
            }

            timeMillis = (System.nanoTime()-startTime)/SEC_TO_MILI;
            waitTime = targetTime - timeMillis;

            try {
                this.sleep(waitTime);
            }
            catch (Exception e) {}

            totalTime += System.nanoTime()-startTime;
            frameCount++;
            if (frameCount == FPS) {
                frameCount = 0;
                totalTime = 0;
            }
        }
    }

    public void setRunning(boolean b) {
        running = b;
    }
}
