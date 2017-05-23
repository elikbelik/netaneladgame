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
- Add to the Git. (Elad)
- Refactor to the code, so it will be pretty! (Elad)
- Redisgn the sprites. (Net)
- Change background, so it will change color from day to night. (Net)
- Add sound effects. (Net)
- Teach Nethanel the code. (Elad)
- Add voice command! (Elad)-
 */

/*
TODO: ELAD:
- Make botborder and topborder the same class
- Make Explotion inherit from AnimatedGameObject
- Code cleanup in GamePanel
    - Use single array for all
    - Create function that is "collision" and one that is not if not relevant
    - Set the text an object itself?
    - Clean m_*
 */