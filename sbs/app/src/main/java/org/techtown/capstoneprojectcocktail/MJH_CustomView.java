package org.techtown.capstoneprojectcocktail;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas; import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader.TileMode;
import android.graphics.SweepGradient;
import android.os.Bundle;
import android.view.View;

public class MJH_CustomView extends Activity {
    /** Called when the activity is first created. */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myView vm = new myView(this);
        setContentView(vm);
    }

    protected class myView extends View {
        public myView(Context context) {
            super(context);
            // TODO Auto-generated constructor stub
        }

        public void onDraw(Canvas canvas){
            Paint pnt = new Paint();
            pnt.setAntiAlias(true);
            // 수평
            pnt.setShader(new LinearGradient(0, 975, 0, 1370, Color.YELLOW, Color.RED, TileMode.CLAMP));
            canvas.drawRect(110, 975, 650, 1370, pnt);


        }
    }
}

