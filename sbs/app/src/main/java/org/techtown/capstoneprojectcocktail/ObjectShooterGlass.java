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
import android.widget.ImageView;
import android.widget.Toast;

import static org.techtown.capstoneprojectcocktail.MJH_SimulatorUiActivity.realisticChecked;

public class ObjectShooterGlass {

    int setNumber = 0;

    int left = 298;
    int right = 602;

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

                int s_height = 835;
                int height = 835;
                int prev_h = 835;


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
                    height = (int)((float)height - ((float)8.0 * volume));
                }
                paint.setColor(Color.rgb(red ,green ,blue));
                if(realisticChecked == true){
                    alpha = MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).alphaList.get(lastIndex);
                    paint.setAlpha(getAlpha(alpha));
                }
                RectF rect2 = new RectF();
                rect2.set(left, height -20, right, height + 20);
                canvas.drawArc(rect2, 180, 180, true, paint);

                height = 835;

                //잔
                Bitmap bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.highball_glass_5);
                bitmap2 = resizeBitmapImg(bitmap2, 1480);
                canvas.drawBitmap(bitmap2, 225, 300, null);



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
                rect.set(left, 815, right, 855);
                canvas.drawArc(rect, 0, 180, true, paint);


                // 한층 위
                red = (int) MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).isColor.get(0).rgb_red;
                green = (int) MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).isColor.get(0).rgb_green;
                blue = (int) MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).isColor.get(0).rgb_blue;

                ///그라데이션 높이
                s_height = (int)((float)height - ((float)8.0 * volume));
                height = s_height;
                prev_h = height;

                volume = MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.inGlassStep-1 ).eachVolume.get(0);

                ///전체사각
                height = (int)((float)height - ((float)8.0 * volume));
                paint.setColor(Color.rgb(red ,green ,blue));
                if(realisticChecked == true){
                    alpha = MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).alphaList.get(0);
                    paint.setAlpha(getAlpha(alpha));
                }
                canvas.drawRect(left, height, right, s_height - (int)(((float)8.0 * volume)/2), paint);
                prev_h = height;


                ///그라데이션 출력
                if(realisticChecked == true){
                    float alpha_s = MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).alphaList.get(1);
                    alpha = MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).alphaList.get(0);
                    paint_gradient.setShader(new LinearGradient(0, s_height - (int)(((float)8.0 * volume)/2), 0, 835, Color.argb(getAlpha(alpha), red ,green ,blue), Color.argb(getAlpha(alpha_s), s_red ,s_green ,s_blue), Shader.TileMode.CLAMP));
                }
                else{
                    paint_gradient.setShader(new LinearGradient(0, s_height - (int)(((float)8.0 * volume)/2), 0, 835, Color.rgb(red ,green ,blue), Color.rgb(s_red ,s_green ,s_blue), Shader.TileMode.CLAMP));
                }
                canvas.drawRect(left, s_height - (int)(((float)8.0 * volume)/2), right, 835, paint_gradient);


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
                        rect.set(left, height -20, right, height + 20);
                        canvas.drawArc(rect, 0, 180, true, paint);

                        //전체사각
                        height = (int)((float)height - ((float)8.0 * volume));
                        paint.setColor(Color.rgb(red ,green ,blue));
                        if(realisticChecked == true){
                            alpha = MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).alphaList.get(index);
                            paint.setAlpha(getAlpha(alpha));
                        }
                        canvas.drawRect(left, height, right, prev_h, paint);
                        prev_h = height;
                    }
                }

                //경계
                paintBound.setColor(0x56000000);
                paintBound.setStyle(Paint.Style.STROKE);
                RectF rect3 = new RectF();
                rect3.set(left, height -8, right, height + 8);
                canvas.drawArc(rect3, 0, 180, false, paintBound);

                //빛반사
                paint.setColor(0x56FFFFFF);
                canvas.drawRect(left+30, 350, left+70, 810, paint);
                rect.set(left+30, 795, left+110, 825);
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
                int height = 835;
                int prev_h = 835;

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
                    height = (int)((float)height - ((float)8.0 * volume));
                }
                paint.setColor(Color.rgb(red ,green ,blue));
                if(realisticChecked == true){
                    alpha = MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).alphaList.get(lastIndex);
                    paint.setAlpha(getAlpha(alpha));
                }
                RectF rect2 = new RectF();
                rect2.set(left, height -20, right, height + 20);
                canvas.drawArc(rect2, 180, 180, true, paint);

                //잔
                Bitmap bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.highball_glass_5);
                bitmap2 = resizeBitmapImg(bitmap2, 1480);
                canvas.drawBitmap(bitmap2, 225, 300, null);

                height = 835;
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
                    rect.set(left, height -20, right, height + 20);
                    canvas.drawArc(rect, 0, 180, true, paint);

                    //전체사각
                    height = (int)((float)height - ((float)8.0 * volume));
                    paint.setColor(Color.rgb(red ,green ,blue));
                    if(realisticChecked == true){
                        alpha = MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).alphaList.get(index);
                        paint.setAlpha(getAlpha(alpha));
                    }
                    canvas.drawRect(left, height, right, prev_h, paint);
                    prev_h = height;

                    //상단부
                    rect2 = new RectF();
                    rect2.set(left, height -20, right, height + 20);
                    canvas.drawArc(rect2, 180, 180, true, paint);;
                }


                //경계
                paintBound.setColor(0x56000000);
                paintBound.setStyle(Paint.Style.STROKE);
                RectF rect3 = new RectF();
                rect3.set(left, height -8, right, height + 8);
                canvas.drawArc(rect3, 0, 180, false, paintBound);

                //빛반사
                paint.setColor(0x56FFFFFF);
                canvas.drawRect(left+30, 350, left+70, 810, paint);
                rect.set(left+30, 795, left+110, 825);
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

                int red = (int) MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).isColor.get(0).rgb_red;
                int green = (int) MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).isColor.get(0).rgb_green;
                int blue = (int) MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).isColor.get(0).rgb_blue;

                volume = MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.inGlassStep-1 ).totalVolume;
                alpha = MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.inGlassStep-1 ).alpha;
                
                Paint paint = new Paint();
                Paint paint_gradient = new Paint();

                height = (int)((float)835 - ((float)8.0 * volume));

                //상단부
                paint.setColor(Color.rgb(red ,green ,blue));
                if(realisticChecked == true){
                    paint.setAlpha(getAlpha(alpha));
                }
                RectF rect2 = new RectF();
                rect2.set(left, height -20, right, height + 20);
                canvas.drawArc(rect2, 180, 180, true, paint);

                //잔
                Bitmap bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.highball_glass_5);
                bitmap2 = resizeBitmapImg(bitmap2, 1480);
                canvas.drawBitmap(bitmap2, 225, 300, null);


                //전체사각
                paint.setColor(Color.rgb(red ,green ,blue));
                if(realisticChecked == true){
                    paint.setAlpha(getAlpha(alpha));
                }
                canvas.drawRect(left, height, right, 835, paint);

                //경계
                paintBound.setColor(0x56000000);
                paintBound.setStyle(Paint.Style.STROKE);
                RectF rect3 = new RectF();
                rect3.set(left, height -8, right, height + 8);
                canvas.drawArc(rect3, 0, 180, false, paintBound);

                //바닥부
                paint.setColor(Color.rgb(red ,green ,blue));
                if(realisticChecked == true){
                    paint.setAlpha(getAlpha(alpha));
                }
                RectF rect = new RectF();
                rect.set(left, 815, right, 855);
                canvas.drawArc(rect, 0, 180, true, paint);

                //빛반사
                paint.setColor(0x56FFFFFF);
                canvas.drawRect(left+30, 350, left+70, 810, paint);
                rect.set(left+30, 795, left+110, 825);
                canvas.drawArc(rect, 90, 90, true, paint);
            }
        }catch (Exception e){e.printStackTrace(); }
    }


    //param source 원본 Bitmap 객체
    //param maxResolution 제한 해상도
    //return 리사이즈된 이미지 Bitmap 객체
    public Bitmap resizeBitmapImg(Bitmap source, int maxResolution){

        int newWidth = 450;
        int newHeight = 600;


        return Bitmap.createScaledBitmap(source, newWidth, newHeight, true);
    }

    public int getAlpha(float _alpha){
        //int alphaVal = (int)(_alpha*_alpha);

        if(_alpha == 0){
            return 50;
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
