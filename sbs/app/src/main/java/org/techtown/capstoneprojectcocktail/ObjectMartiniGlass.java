package org.techtown.capstoneprojectcocktail;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.widget.ImageView;
import android.widget.Toast;

import static org.techtown.capstoneprojectcocktail.MJH_SimulatorUiActivity.realisticChecked;

public class ObjectMartiniGlass {

    float alpha = 0;
    int tuningVal = 14;

    public void draw(ImageView View, Context context){
        try{
            //그라데이션
            if(MJH_SimulatorUiActivity.test.isGradient == 1){
                Canvas canvas;
                Bitmap bitmap = Bitmap.createBitmap(500,700, Bitmap.Config.ARGB_8888);
                canvas = new Canvas(bitmap);
                View.setImageBitmap(bitmap);

                float realVolume;
                float remainVolume = MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.inGlassStep-1 ).totalVolume;;
                float graphicVolumeWeight = (float)82687;

                float finalIndexHeightBuf = 0;

                double graphicVol = 0;
                float firstConeHeight = 0;
                float firstConeX = 0;

                int red = 0;
                int green = 0;
                int blue = 0;

                int s_red = (int) MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).isColor.get(1).rgb_red;
                int s_green = (int) MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).isColor.get(1).rgb_green;
                int s_blue = (int) MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).isColor.get(1).rgb_blue;

                Paint paint = new Paint();
                Paint paint_gradient = new Paint();


                //////////////////
                int start = MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).isLayering;

                for(int index = start - 1; index > -1; index--){

                    if(index == 1){
                        index = 0;
                    }
                    red = (int) MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).isColor.get(index).rgb_red;
                    green = (int) MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).isColor.get(index).rgb_green;
                    blue = (int) MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).isColor.get(index).rgb_blue;

                    realVolume = remainVolume;
                    graphicVol = (double) (realVolume * graphicVolumeWeight);
                    remainVolume = remainVolume - MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).eachVolume.get(index);

                    paint.setColor(Color.rgb(red ,green ,blue));
                    if(realisticChecked == true){
                        alpha = MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).alphaList.get(index);
                        paint.setAlpha(getAlpha(alpha));
                    }

                    firstConeHeight =(float) (Math.pow(graphicVol/3.14*3/Math.pow(225, 2)*Math.pow(210, 2) ,1d/3d));
                    firstConeX = firstConeHeight * 210 /225;


                    /////도화지 그리기
                    //좌측삼각
                    Path path2 = new Path();
                    Path path3 = new Path();
                    Paint back= new Paint();;
                    back.setColor(Color.parseColor("#DDDDDD"));
                    path2.moveTo(215, 285-firstConeHeight);
                    path2.lineTo(247 - firstConeX - tuneGraphic(realVolume), 285-firstConeHeight);
                    path2.lineTo(215, 285);
                    path2.close();
                    canvas.drawPath(path2, back);
                    //우측삼각
                    path3.moveTo(285, 285-firstConeHeight);
                    path3.lineTo(253 + firstConeX + tuneGraphic(realVolume), 285-firstConeHeight);
                    path3.lineTo(285, 285);
                    path3.close();
                    canvas.drawPath(path3, back);
                    //전체사각
                    canvas.drawRect(215,287-firstConeHeight, 285, 285, back);



                    Path path = new Path();

                    //좌측삼각
                    path.moveTo(215, 285-firstConeHeight);
                    path.lineTo(247 - firstConeX - tuneGraphic(realVolume), 285-firstConeHeight);
                    path.lineTo(215, 285);
                    path.close();
                    canvas.drawPath(path, paint);

                    //우측삼각
                    Path path1 = new Path();
                    path1.moveTo(285, 285-firstConeHeight);
                    path1.lineTo(253 + firstConeX + tuneGraphic(realVolume), 285-firstConeHeight);
                    path1.lineTo(285, 285);
                    path1.close();
                    canvas.drawPath(path1, paint);

                    //전체사각
                    canvas.drawRect(215,287-firstConeHeight, 285, 285, paint);

                    //윗원
                    RectF rect = new RectF();
                    rect.set(250 - firstConeX - tuneGraphic(realVolume), 275-firstConeHeight, 250 + firstConeX + tuneGraphic(realVolume), 295-firstConeHeight);
                    canvas.drawArc(rect, 0, 360, true, paint);
                }

                s_red = (int) MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).isColor.get(1).rgb_red;
                s_green = (int) MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).isColor.get(1).rgb_green;
                s_blue = (int) MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).isColor.get(1).rgb_blue;

                float s_vol;
                s_vol = MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.inGlassStep-1 ).eachVolume.get(1);

                //바닥부
                RectF rect1 = new RectF();
                rect1.set(215, 270, 285, 300);
                paint.setColor(Color.rgb(s_red ,s_green ,s_blue));
                if(realisticChecked == true){
                    alpha = MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).alphaList.get(1);
                    paint.setAlpha(getAlpha(alpha));
                }
                canvas.drawArc(rect1, 0, 360, true, paint);


                realVolume = MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).eachVolume.get(1);
                graphicVol = (double) (realVolume * graphicVolumeWeight);

                finalIndexHeightBuf = firstConeHeight/2;

                firstConeHeight =(float) (Math.pow(graphicVol/3.14*3/Math.pow(225, 2)*Math.pow(210, 2) ,1d/3d));
                firstConeHeight = (firstConeHeight + firstConeHeight + finalIndexHeightBuf) / 2;
                firstConeX = firstConeHeight * 210 /225;

                if(realisticChecked == true) {
                    float alpha_s = MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).alphaList.get(1);
                    alpha = MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).alphaList.get(0);
                    paint_gradient.setShader(new LinearGradient(0, 287-firstConeHeight, 0, 285, Color.argb(getAlpha(alpha), red ,green ,blue), Color.argb(getAlpha(alpha_s), s_red ,s_green ,s_blue), Shader.TileMode.CLAMP));
                }
                else{
                    paint_gradient.setShader(new LinearGradient(0, 287-firstConeHeight, 0, 285, Color.rgb(red ,green ,blue), Color.rgb(s_red ,s_green ,s_blue), Shader.TileMode.CLAMP));
                }


                /////도화지 그리기
                Path path2 = new Path();
                Path path3 = new Path();
                Paint back= new Paint();;
                //좌측삼각
                back.setColor(Color.parseColor("#DDDDDD"));
                path2.moveTo(215, 285-firstConeHeight);
                path2.lineTo(247 - firstConeX - tuneGraphic(realVolume), 285-firstConeHeight);
                path2.lineTo(215, 285);
                path2.close();
                canvas.drawPath(path2, back);
                //우측삼각
                path3.moveTo(285, 285-firstConeHeight);
                path3.lineTo(253 + firstConeX + tuneGraphic(realVolume), 285-firstConeHeight);
                path3.lineTo(285, 285);
                path3.close();
                canvas.drawPath(path3, back);
                //전체사각
                canvas.drawRect(215,287-firstConeHeight, 285, 285, back);

                Path path = new Path();

                //좌측삼각
                path.moveTo(215, 285-firstConeHeight);
                path.lineTo(247 - firstConeX - tuneGraphic(realVolume), 285-firstConeHeight);
                path.lineTo(215, 285);
                path.close();
                canvas.drawPath(path, paint_gradient);

                //우측삼각
                Path path1 = new Path();
                path1.moveTo(285, 285-firstConeHeight);
                path1.lineTo(253 + firstConeX + tuneGraphic(realVolume), 285-firstConeHeight);
                path1.lineTo(285, 285);
                path1.close();
                canvas.drawPath(path1, paint_gradient);

                //전체사각
                canvas.drawRect(215,287-firstConeHeight, 285, 285, paint_gradient);


                //잔
                Bitmap bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.martini_glass_re);
                bitmap2 = resizeBitmapImg(bitmap2);
                canvas.drawBitmap(bitmap2, 0, 0, null);

            }

            else if( MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).isLayering > 1){
                Canvas canvas;
                Bitmap bitmap = Bitmap.createBitmap(500,700, Bitmap.Config.ARGB_8888);
                canvas = new Canvas(bitmap);
                View.setImageBitmap(bitmap);

                float realVolume;
                float remainVolume = MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.inGlassStep-1 ).totalVolume;
                float graphicVolumeWeight = (float)82687;

                double graphicVol = 0;
                float firstConeHeight = 0;
                float firstConeX = 0;

                int red;
                int green;
                int blue;

                Paint paint = new Paint();
                Paint paint_gradient = new Paint();

                //////////////////
                int start = MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).isLayering;

                for(int index = start - 1; index > -1; index--){
                    red = (int) MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).isColor.get(index).rgb_red;
                    green = (int) MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).isColor.get(index).rgb_green;
                    blue = (int) MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).isColor.get(index).rgb_blue;

                    realVolume = remainVolume;
                    graphicVol = (double) (realVolume * graphicVolumeWeight);
                    remainVolume = remainVolume - MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).eachVolume.get(index);

                    paint.setColor(Color.rgb(red ,green ,blue));
                    if(realisticChecked == true){
                        alpha = MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).alphaList.get(index);
                        paint.setAlpha(getAlpha(alpha));
                    }

                    firstConeHeight =(float) (Math.pow(graphicVol/3.14*3/Math.pow(225, 2)*Math.pow(210, 2) ,1d/3d));
                    firstConeX = firstConeHeight * 210 /225;


                    /////도화지 그리기
                    Path path2 = new Path();
                    Path path3 = new Path();
                    Paint back= new Paint();;
                    //좌측삼각
                    back.setColor(Color.parseColor("#DDDDDD"));
                    path2.moveTo(215, 285-firstConeHeight);
                    path2.lineTo(247 - firstConeX - tuneGraphic(realVolume), 285-firstConeHeight);
                    path2.lineTo(215, 285);
                    path2.close();
                    canvas.drawPath(path2, back);
                    //우측삼각
                    path3.moveTo(285, 285-firstConeHeight);
                    path3.lineTo(253 + firstConeX + tuneGraphic(realVolume), 285-firstConeHeight);
                    path3.lineTo(285, 285);
                    path3.close();
                    canvas.drawPath(path3, back);
                    //전체사각
                    canvas.drawRect(215,287-firstConeHeight, 285, 285, back);

                    Path path = new Path();

                    //좌측삼각
                    path.moveTo(215, 285-firstConeHeight);
                    path.lineTo(247 - firstConeX - tuneGraphic(realVolume), 285-firstConeHeight);
                    path.lineTo(215, 285);
                    path.close();
                    canvas.drawPath(path, paint);

                    //우측삼각
                    Path path1 = new Path();
                    path1.moveTo(285, 285-firstConeHeight);
                    path1.lineTo(253 + firstConeX + tuneGraphic(realVolume), 285-firstConeHeight);
                    path1.lineTo(285, 285);
                    path1.close();
                    canvas.drawPath(path1, paint);

                    //윗원
                    RectF rect = new RectF();
                    rect.set(250 - firstConeX - tuneGraphic(realVolume), 275-firstConeHeight, 250 + firstConeX + tuneGraphic(realVolume), 295-firstConeHeight);
                    canvas.drawArc(rect, 0, 360, true, paint);

                    //전체사각
                    canvas.drawRect(215,287-firstConeHeight, 285, 285, paint);

                    //바닥부
                    RectF rect1 = new RectF();
                    rect1.set(215, 270, 285, 300);
                    canvas.drawArc(rect1, 0, 360, true, paint);
                }

                //잔
                Bitmap bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.martini_glass_re);
                bitmap2 = resizeBitmapImg(bitmap2);
                canvas.drawBitmap(bitmap2, 0, 0, null);
            }
            else{
                Canvas canvas;
                Bitmap bitmap = Bitmap.createBitmap(500,700, Bitmap.Config.ARGB_8888);
                canvas = new Canvas(bitmap);
                View.setImageBitmap(bitmap);

                float realVolume;
                float graphicVolumeWeight = (float)82687;
                double graphicVol = 0;
                float height = 0;
                float firstConeHeight = 0;
                float firstConeX = 0;

                float cosineCone = (float) (225 /307.77426793);

                int red = (int) MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).isColor.get(0).rgb_red;
                int green = (int) MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).isColor.get(0).rgb_green;
                int blue = (int) MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.simulatorStep.size()-1 ).isColor.get(0).rgb_blue;

                realVolume = MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.inGlassStep-1 ).totalVolume;
                graphicVol = (double) (realVolume * graphicVolumeWeight);


                Paint paint = new Paint();
                Paint paint_gradient = new Paint();


                firstConeHeight =(float) (Math.pow(graphicVol/3.14*3/Math.pow(225, 2)*Math.pow(210, 2) ,1d/3d));
                firstConeX = firstConeHeight * 210 /225;

                paint.setColor(Color.rgb(red ,green ,blue));
                if(realisticChecked == true){
                    alpha = MJH_SimulatorUiActivity.test.simulatorStep.get(MJH_SimulatorUiActivity.test.inGlassStep-1 ).alpha;
                    paint.setAlpha(getAlpha(alpha));
                }

                /////도화지 그리기
                Path path2 = new Path();
                Path path3 = new Path();
                Paint back= new Paint();;
                //좌측삼각
                back.setColor(Color.parseColor("#DDDDDD"));
                path2.moveTo(215, 285-firstConeHeight);
                path2.lineTo(247 - firstConeX - tuneGraphic(realVolume), 285-firstConeHeight);
                path2.lineTo(215, 285);
                path2.close();
                canvas.drawPath(path2, back);
                //우측삼각
                path3.moveTo(285, 285-firstConeHeight);
                path3.lineTo(253 + firstConeX + tuneGraphic(realVolume), 285-firstConeHeight);
                path3.lineTo(285, 285);
                path3.close();
                canvas.drawPath(path3, back);
                //전체사각
                canvas.drawRect(215,287-firstConeHeight, 285, 285, back);

                Path path = new Path();

                //좌측삼각
                path.moveTo(215, 285-firstConeHeight);
                path.lineTo(247 - firstConeX - tuneGraphic(realVolume), 285-firstConeHeight);
                path.lineTo(215, 285);
                path.close();
                canvas.drawPath(path, paint);

                //우측삼각
                Path path1 = new Path();
                path1.moveTo(285, 285-firstConeHeight);
                path1.lineTo(253 + firstConeX + tuneGraphic(realVolume), 285-firstConeHeight);
                path1.lineTo(285, 285);
                path1.close();
                canvas.drawPath(path1, paint);

                //윗원
                RectF rect = new RectF();
                rect.set(250 - firstConeX - tuneGraphic(realVolume), 275-firstConeHeight, 250 + firstConeX + tuneGraphic(realVolume), 295-firstConeHeight);
                canvas.drawArc(rect, 0, 360, true, paint);


                //전체사각
                canvas.drawRect(215,287-firstConeHeight, 285, 285, paint);

                //바닥부
                RectF rect1 = new RectF();
                rect1.set(215, 270, 285, 300);
                canvas.drawArc(rect1, 0, 360, true, paint);


                //잔
                Bitmap bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.martini_glass_re);
                bitmap2 = resizeBitmapImg(bitmap2);
                canvas.drawBitmap(bitmap2, 0, 0, null);
            }

        }catch (Exception e){e.printStackTrace(); }
    }

    //param source 원본 Bitmap 객체
    //param maxResolution 제한 해상도
    //return 리사이즈된 이미지 Bitmap 객체
    public Bitmap resizeBitmapImg(Bitmap source){
        int width = source.getWidth();
        int height = source.getHeight();
        int newWidth = 500;
        int newHeight = 700;

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

    public int tuneGraphic(float _vol){
        int returnVal = 20;
        int minus = 0;

        minus = (int)_vol / 7;

        if(minus > 20){
            minus = 20;
        }

        returnVal = returnVal - minus;

        return returnVal;
    }


}
