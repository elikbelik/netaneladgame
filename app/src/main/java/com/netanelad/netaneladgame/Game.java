package com.netanelad.netaneladgame;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class Game extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Turn off title
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Set to fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(new GamePanel(this));
    }
}

// Taken from here: https://www.youtube.com/watch?v=GPzTSpZwFoU&list=PLWweaDaGRHjvQlpLV0yZDmRKVBdy6rSlg&index=3

/*
TODO:
- Change background, so it will change color from day to night. (Net)
- Add voice command! (Elad)-
- Make the borders to move up and down if needed when player takes a pill!
 */

