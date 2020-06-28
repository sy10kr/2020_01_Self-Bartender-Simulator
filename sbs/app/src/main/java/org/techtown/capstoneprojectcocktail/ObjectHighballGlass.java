package org.techtown.capstoneprojectcocktail;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import static org.techtown.capstoneprojectcocktail.MJH_SimulatorUiActivity.realisticChecked;

public class ObjectHighballGlass {

    int setNumber = 0;

    int left = 140;
    int right = 760;
    float alpha = 0;

    public void draw(ImageView View, Context context){

        Paint paintBound = new Paint();
        try{
            //그라데이션
            if(MJH_SimulatorUiActivity.test.isGradient == 1){
                setNumber++;

                Canvas canvas;
                Bitmap bitmap = Bitmap.createBitmap(900,1200, Bitmap.Config.ARGB_8888);
                canvas = new Canvas(bitmap);
                View.setImageBitmap(bitmap);

                float volume;
                int s_red, s_green, s_blue;
                int red, green, blue;

                int s_height = 1080;
                int height = 1080;
                int prev_h = 1080;



                Paint paint = new Paint();

                Paint paint_gradient = new Paint();
                paint_gradient.setAntiAlias(true);

                RectF rect = new RectF();

                int lastIndex;
                lastIndex = MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).isColor.size() - 1;

                if(lastIndex == 1){
                    lastIndex = 0;
                }

                red = (int) MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).isColor.get(lastIndex).rgb_red;
                green = (int) MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).isColor.get(lastIndex).rgb_green;
                blue = (int) MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).isColor.get(lastIndex).rgb_blue;

                //상단부
                for(int index = 0; index < MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).isLayering; index++){
                    volume = MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.inGlassStep-1 ).eachVolume.get(index);
                    height = (int)((float)height - ((float)4.0 * volume));
                }
                paint.setColor(Color.rgb(red ,green ,blue));
                if(realisticChecked == true){
                    alpha = MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).alphaList.get(lastIndex);
                    paint.setAlpha(getAlpha(alpha));
                }
                RectF rect2 = new RectF();
                rect2.set(left, height -25, right, height + 25);
                canvas.drawArc(rect2, 0, 360, true, paint);

                height = 1080;

                //잔
                Bitmap bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.highball_glass_5_ice);;
                if(realisticChecked == false){
                    bitmap2= BitmapFactory.decodeResource(context.getResources(), R.drawable.highball_glass_5_ice_2);
                }
                bitmap2 = resizeBitmapImg(bitmap2, 1480);
                canvas.drawBitmap(bitmap2, 0, 0, null);



                int setIndex = MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).isLayering;

                //시럽
                s_red = (int) MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).isColor.get(1).rgb_red;
                s_green = (int) MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).isColor.get(1).rgb_green;
                s_blue = (int) MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).isColor.get(1).rgb_blue;

                volume = MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.inGlassStep-1 ).eachVolume.get(1);

                ///바닥부
                paint.setColor(Color.rgb(s_red ,s_green ,s_blue));
                if(realisticChecked == true){
                    alpha = MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).alphaList.get(1);
                    paint.setAlpha(getAlpha(alpha));
                }
                rect.set(left, 1045, right, 1115);
                canvas.drawArc(rect, 0, 180, true, paint);


                // 한층 위
                red = (int) MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).isColor.get(0).rgb_red;
                green = (int) MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).isColor.get(0).rgb_green;
                blue = (int) MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).isColor.get(0).rgb_blue;

                ///그라데이션 높이
                s_height = (int)((float)height - ((float)4.0 * volume));
                height = s_height;
                prev_h = height;

                volume = MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.inGlassStep-1 ).eachVolume.get(0);

                ///전체사각
                height = (int)((float)height - ((float)4.0 * volume));
                paint.setColor(Color.rgb(red ,green ,blue));
                if(realisticChecked == true){
                    alpha = MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).alphaList.get(0);
                    paint.setAlpha(getAlpha(alpha));
                }
                canvas.drawRect(left, height, right, s_height - (int)(((float)4.0 * volume)/2), paint);
                prev_h = height;

                ///그라데이션 출력
                if(realisticChecked == true){
                    float alpha_s = MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).alphaList.get(1);
                    alpha = MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).alphaList.get(0);
                    paint_gradient.setShader(new LinearGradient(0, s_height - (int)(((float)4.0 * volume)/2), 0, 1080, Color.argb(getAlpha(alpha), red ,green ,blue), Color.argb(getAlpha(alpha_s), s_red ,s_green ,s_blue), Shader.TileMode.CLAMP));
                }
                else{
                    paint_gradient.setShader(new LinearGradient(0, s_height - (int)(((float)4.0 * volume)/2), 0, 1080, Color.rgb(red ,green ,blue), Color.rgb(s_red ,s_green ,s_blue), Shader.TileMode.CLAMP));
                }
                canvas.drawRect(left, s_height - (int)(((float)4.0 * volume)/2), right, 1080, paint_gradient);

                if(MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).isLayering > 2){
                    for(int index = 2; index < MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).isLayering; index++){
                        red = (int) MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).isColor.get(index).rgb_red;
                        green = (int) MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).isColor.get(index).rgb_green;
                        blue = (int) MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).isColor.get(index).rgb_blue;

                        volume = MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.inGlassStep-1 ).eachVolume.get(index);

                        //층 하단
                        paint.setColor(Color.rgb(red ,green ,blue));
                        if(realisticChecked == true){
                            alpha = MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).alphaList.get(index);
                            paint.setAlpha(getAlpha(alpha));
                        }
                        rect.set(left, height -25, right, height + 25);
                        canvas.drawArc(rect, 0, 180, true, paint);

                        //전체사각
                        height = (int)((float)height - ((float)4.0 * volume));
                        paint.setColor(Color.rgb(red ,green ,blue));
                        if(realisticChecked == true){
                            alpha = MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).alphaList.get(index);
                            paint.setAlpha(getAlpha(alpha));
                        }
                        canvas.drawRect(left, height, right, prev_h, paint);
                        prev_h = height;
                    }
                }


                //빛반사
                paint.setColor(0x56FFFFFF);
                canvas.drawRect(left+30, 75, left+160, 1065, paint);
                rect.set(left+30, 1035, left+290, 1095);
                canvas.drawArc(rect, 90, 90, true, paint);
            }
            //레이어링
            else if( MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).isLayering > 1){
                setNumber++;

                Canvas canvas;
                Bitmap bitmap = Bitmap.createBitmap(900,1200, Bitmap.Config.ARGB_8888);
                canvas = new Canvas(bitmap);
                View.setImageBitmap(bitmap);

                float volume;
                int red = 0, green = 0, blue = 0;
                int height = 1080;
                int prev_h = 1080;

                int lastIndex = 0;


                Paint paint = new Paint();
                Paint paint_gradient = new Paint();
                RectF rect = new RectF();

                lastIndex = MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).isColor.size() - 1;
                red = (int) MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).isColor.get(lastIndex).rgb_red;
                green = (int) MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).isColor.get(lastIndex).rgb_green;
                blue = (int) MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).isColor.get(lastIndex).rgb_blue;


                //상단부
                for(int index = 0; index < MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).isLayering; index++){
                    volume = MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.inGlassStep-1 ).eachVolume.get(index);
                    height = (int)((float)height - ((float)4.0 * volume));
                }
                paint.setColor(Color.rgb(red ,green ,blue));
                if(realisticChecked == true){
                    alpha = MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).alphaList.get(lastIndex);
                    paint.setAlpha(getAlpha(alpha));
                }
                RectF rect2 = new RectF();
                rect2.set(left, height -25, right, height + 25);
                canvas.drawArc(rect2, 0, 360, true, paint);

                //잔
                Bitmap bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.highball_glass_5_ice);
                if(realisticChecked == false){
                    bitmap2= BitmapFactory.decodeResource(context.getResources(), R.drawable.highball_glass_5_ice_2);
                }
                bitmap2 = resizeBitmapImg(bitmap2, 1480);
                canvas.drawBitmap(bitmap2, 0, 0, null);

                height = 1080;
                for(int index = 0; index < MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).isLayering; index++){
                    red = (int) MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).isColor.get(index).rgb_red;
                    green = (int) MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).isColor.get(index).rgb_green;
                    blue = (int) MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).isColor.get(index).rgb_blue;

                    volume = MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.inGlassStep-1 ).eachVolume.get(index);


                    //층 하단
                    paint.setColor(Color.rgb(red ,green ,blue));
                    if(realisticChecked == true){
                        alpha = MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).alphaList.get(index);
                        paint.setAlpha(getAlpha(alpha));
                    }
                    rect.set(left, height -25, right, height + 25);
                    canvas.drawArc(rect, 0, 180, true, paint);

                    //전체사각
                    height = (int)((float)height - ((float)4.0 * volume));
                    paint.setColor(Color.rgb(red ,green ,blue));
                    if(realisticChecked == true){
                        alpha = MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).alphaList.get(index);
                        paint.setAlpha(getAlpha(alpha));
                    }
                    canvas.drawRect(left, height, right, prev_h, paint);
                    prev_h = height;

                    //상단부
                    rect2 = new RectF();
                    rect2.set(left, height -25, right, height + 25);
                    canvas.drawArc(rect2, 0, 360, true, paint);
                }


                //빛반사
                paint.setColor(0x56FFFFFF);
                canvas.drawRect(left+30, 75, left+160, 1065, paint);
                rect.set(left+30, 1035, left+290, 1095);
                canvas.drawArc(rect, 90, 90, true, paint);
            }
            //노멀
            else{
                Canvas canvas;
                Bitmap bitmap = Bitmap.createBitmap(900,1200, Bitmap.Config.ARGB_8888);
                canvas = new Canvas(bitmap);
                View.setImageBitmap(bitmap);

                float volume;
                int height = 0;
                alpha = MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.inGlassStep-1 ).alpha;

                int red = (int) MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).isColor.get(0).rgb_red;
                int green = (int) MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).isColor.get(0).rgb_green;
                int blue = (int) MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).isColor.get(0).rgb_blue;

                volume = MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.inGlassStep-1 ).totalVolume;

                Paint paint = new Paint();
                Paint paint_gradient = new Paint();

                height = (int)((float)1080 - ((float)4.0 * volume));

                //상단부
                paint.setColor(Color.rgb(red ,green ,blue));
                if(realisticChecked == true){
                    paint.setAlpha(getAlpha(alpha));
                }
                RectF rect2 = new RectF();
                rect2.set(left, height -25, right, height + 25);
                canvas.drawArc(rect2, 0, 360, true, paint);

                //잔
                Bitmap bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.highball_glass_5_ice);
                if(realisticChecked == false){
                    bitmap2= BitmapFactory.decodeResource(context.getResources(), R.drawable.highball_glass_5_ice_2);
                }
                bitmap2 = resizeBitmapImg(bitmap2, 1480);
                canvas.drawBitmap(bitmap2, 0, 0, null);


                //전체사각
                paint.setColor(Color.rgb(red ,green ,blue));
                if(realisticChecked == true){
                    paint.setAlpha(getAlpha(alpha));
                }
                canvas.drawRect(left, height, right, 1080, paint);


                //바닥부
                paint.setColor(Color.rgb(red ,green ,blue));
                if(realisticChecked == true){
                    paint.setAlpha(getAlpha(alpha));
                }
                RectF rect = new RectF();
                rect.set(left, 1045, right, 1115);
                canvas.drawArc(rect, 0, 180, true, paint);

                //빛반사
                paint.setColor(0x56FFFFFF);
                canvas.drawRect(left+30, 75, left+160, 1065, paint);
                rect.set(left+30, 1035, left+290, 1095);
                canvas.drawArc(rect, 90, 90, true, paint);
            }
        }catch (Exception e){e.printStackTrace(); }
    }


    //param source 원본 Bitmap 객체
    //param maxResolution 제한 해상도
    //return 리사이즈된 이미지 Bitmap 객체
    public Bitmap resizeBitmapImg(Bitmap source, int maxResolution){

        int newWidth = 900;
        int newHeight = 1200;

        return Bitmap.createScaledBitmap(source, newWidth, newHeight, true);
    }

    public int getAlpha(float _alpha){
        //int alphaVal = (int)(_alpha*_alpha);

        if(_alpha == 0){
            return 40;
        }
        int alphaVal = (int) ((((_alpha)*1.5) + 0.5) * 30);

        if(alphaVal > 255){
            alphaVal = 255;
            return alphaVal;
        }
        else if(alphaVal < 135){
            alphaVal = 135;
        }
        return alphaVal;
    }
}
