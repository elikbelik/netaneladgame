package com.netanelad.netaneladgame;

import android.graphics.Canvas;
import android.provider.Settings;
import android.view.SurfaceHolder;

/**
 * Created by elad on 08/03/2017.
 */

public class MainThread extends Thread {

    private int FPS = 30;
    private double m_averageFPS;
    private SurfaceHolder m_surfaceHolder;
    private GamePanel m_gamePanel;
    private boolean m_running;
    public static Canvas m_canvas;

    public MainThread (SurfaceHolder surfaceHolder, GamePanel gamePanel) {
        super();
        this.m_surfaceHolder = surfaceHolder;
        this.m_gamePanel = gamePanel;
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

        while (m_running) {
            startTime = System.nanoTime();
            m_canvas = null;

            // Try locking the canvas for pixel editing
            try {
                m_canvas = this.m_surfaceHolder.lockCanvas();
                synchronized (m_surfaceHolder) {
                    this.m_gamePanel.update();
                    this.m_gamePanel.draw(m_canvas);
                }
            }
            catch (Exception e) {}
            finally {
                if (m_canvas!=null) {
                    try {
                        m_surfaceHolder.unlockCanvasAndPost(m_canvas);
                    }
                    catch (Exception e) {e.printStackTrace();}
                }
            }

            timeMillis = (System.nanoTime()-startTime)/1000000;
            waitTime = targetTime - timeMillis;

            try {
                this.sleep(waitTime);
            }
            catch (Exception e) {}

            totalTime += System.nanoTime()-startTime;
            frameCount++;
            if (frameCount == FPS) {
                m_averageFPS = 1e9*frameCount/totalTime;
                frameCount = 0;
                totalTime = 0;
                System.out.println(m_averageFPS);
            }
        }
    }

    public void setRunning(boolean b) {
        m_running = b;
    }
}
