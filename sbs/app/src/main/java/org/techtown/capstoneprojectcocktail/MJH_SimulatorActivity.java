package org.techtown.capstoneprojectcocktail;

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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MJH_SimulatorActivity extends AppCompatActivity {
    MJH_Object_simulator simulateObject;
    MJH_Object_ingredient[] inputIngredient;
    float[] inputIngredientAmount;
    Canvas canvas;

    MJH_Object_ingredient[] ingredientList = new MJH_Object_ingredient[200];
    int listCount = 0;
    MJH_Object_color colorBuffer;
    Map<String, Number> ingredientRGB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setAdapterForIngredientSearch();
        try{
            Thread.sleep(1000);
        }catch (Exception e){
            e.printStackTrace();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.min_cocktail_simulator_activity);

        Bitmap bitmap = Bitmap.createBitmap(720,1480, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        ImageView View = (ImageView) findViewById(R.id.imageView);
        View.setImageBitmap(bitmap);


        Button button1 = (Button) findViewById(R.id.button1_mjh);
        Button button2 = (Button) findViewById(R.id.button2_mjh);
        Button button3 = (Button) findViewById(R.id.button3_mjh);
        button1.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {


            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

            }
        });
        //sample ingredient
        MJH_Object_color color1 = new MJH_Object_color(255, 0, 0);
        MJH_Object_color color2 = new MJH_Object_color(255, 255, 0);
        MJH_Object_color color3 = new MJH_Object_color(155, 0, 100);
        MJH_Object_color color4 = new MJH_Object_color(155, 255, 111);


    }


    public void setAdapterForIngredientSearch(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef;

        for(int i=0; i < 140; i++) {
            ingredientList[i] = new MJH_Object_ingredient();
            docRef = db.collection("Ingredient").document(String.valueOf(5001 + i));

            final int finalI = i;
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            listCount = finalI;
                            try{
                                ingredientList[listCount].name = (String)document.get("Ingredient_name");
                                ingredientList[listCount].id = 5001+ listCount;
                                ingredientList[listCount].abv = Float.parseFloat(document.get("abv").toString());
                                ingredientList[listCount].sugar = Float.parseFloat(document.get("단맛").toString());
                                ingredientList[listCount].sour = Float.parseFloat(document.get("신맛").toString());
                                ingredientList[listCount].salty = Float.parseFloat(document.get("짠맛").toString());
                                ingredientList[listCount].bitter = Float.parseFloat(document.get("쓴맛").toString());
                                ingredientList[listCount].hot = Float.parseFloat(document.get("매운맛").toString());
                                ingredientList[listCount].flavour = document.get("flavour").toString();
                                ingredientList[listCount].specific_gravity = Float.parseFloat(document.get("specific_gravity").toString());

                                ingredientRGB = (Map<String, Number>) document.getData().get("Ingredient_color");
                                colorBuffer = new MJH_Object_color(Float.parseFloat(ingredientRGB.get("Red").toString()), Float.parseFloat(ingredientRGB.get("Green").toString()),
                                        Float.parseFloat(ingredientRGB.get("Blue").toString()));
                                ingredientList[listCount].my_color = colorBuffer;
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }

                }
            });
        }
        listCount = 0;

    }


    //is_layering = 0 일때 (결과가 단일색)
    public void renderingTypeOne(Canvas canvas){
        Paint paint = new Paint();
        Paint paint_gradient = new Paint();

        //바닥부
        paint.setColor(Color.YELLOW);
        RectF rect1 = new RectF();
        rect1.set(110, 350, 650, 430);
        canvas.drawArc(rect1, 180, 180, true, paint);

        //위 사각
        paint.setColor(Color.YELLOW);
        canvas.drawRect(110, 380, 650, 1320, paint);

        //그라데이션
        paint_gradient.setShader(new LinearGradient(640, 620, 640, 1020, Color.YELLOW, Color.RED, Shader.TileMode.CLAMP));
        canvas.drawRect(110, 620, 650, 1020, paint_gradient);

        //아래 사각
        paint.setColor(Color.RED);
        canvas.drawRect(110, 1020, 650, 1320, paint);

        //바닥부
        paint.setColor(Color.RED);
        RectF rect = new RectF();
        rect.set(110, 1270, 650, 1370);
        canvas.drawArc(rect, 0, 360, true, paint);


        Bitmap bitmap2 = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.highball_glass_test_ice);
        bitmap2 = resizeBitmapImg(bitmap2, 1480);
        canvas.drawBitmap(bitmap2, 0, 0, null);

        //빛반사
        paint.setColor(0x56FFFFFF);

        canvas.drawRect(150, 75, 300, 1370, paint);

        rect.set(150, 1350, 300, 1390);
        canvas.drawArc(rect, 90, 90, true, paint);
    }

    public void rendering2(Canvas canvas){
        Paint paint = new Paint();
        Paint paint_gradient = new Paint();

        //바닥부
        paint.setColor(0xEF0099FF);
        RectF rect1 = new RectF();
        rect1.set(110, 350, 650, 430);
        canvas.drawArc(rect1, 180, 180, true, paint);

        //위 사각
        paint.setColor(0xEF0099FF);
        canvas.drawRect(110, 380, 650, 1320, paint);


        //바닥부
        paint.setColor(0xEF0099FF);
        RectF rect = new RectF();
        rect.set(110, 1270, 650, 1370);
        canvas.drawArc(rect, 0, 180, true, paint);


        Bitmap bitmap2 = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.highball_glass_test_ice);
        bitmap2 = resizeBitmapImg(bitmap2, 1480);
        canvas.drawBitmap(bitmap2, 0, 0, null);

        //빛반사
        paint.setColor(0x56FFFFFF);

        canvas.drawRect(150, 75, 300, 1370, paint);

        rect.set(150, 1350, 300, 1390);
        canvas.drawArc(rect, 90, 90, true, paint);
    }

    public void rendering3(Canvas canvas){
        Paint paint = new Paint();
        Paint paint_gradient = new Paint();

        //바닥부
        paint.setColor(0xE6FFFF00);
        RectF rect1 = new RectF();
        rect1.set(110, 350, 650, 430);
        canvas.drawArc(rect1, 180, 180, true, paint);

        //위 사각




        paint.setColor(0xE6FFFF00);
        canvas.drawRect(110, 380, 650, 620, paint);

        //그라데이션
        paint_gradient.setShader(new LinearGradient(640, 620, 640, 1020, 0xE6FFFF00, 0x96FFFFFF, Shader.TileMode.CLAMP));
        canvas.drawRect(110, 620, 650, 1020, paint_gradient);

        //아래 사각
        paint.setColor(0x96FFFFFF);
        canvas.drawRect(110, 1020, 650, 1320, paint);

        //바닥부
        paint.setColor(0x96FFFFFF);
        RectF rect = new RectF();
        rect.set(110, 1270, 650, 1370);
        canvas.drawArc(rect, 0, 360, true, paint);


        Bitmap bitmap2 = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.highball_glass_test_ice);
        bitmap2 = resizeBitmapImg(bitmap2, 1480);
        canvas.drawBitmap(bitmap2, 0, 0, null);

        //빛반사
        paint.setColor(0x56FFFFFF);

        canvas.drawRect(150, 75, 300, 1370, paint);

        rect.set(150, 1350, 300, 1390);
        canvas.drawArc(rect, 90, 90, true, paint);
    }
    //param source 원본 Bitmap 객체
    //param maxResolution 제한 해상도
    //return 리사이즈된 이미지 Bitmap 객체
    public Bitmap resizeBitmapImg(Bitmap source, int maxResolution){
        int width = source.getWidth();
        int height = source.getHeight();
        int newWidth = width;
        int newHeight = height;
        float rate = 0.0f;

        if(width > height){
            if(maxResolution < width){
                rate = maxResolution / (float) width;
                newHeight = (int) (height * rate);
                newWidth = maxResolution;
            }
        }else{
            if(maxResolution < height){
                rate = maxResolution / (float) height;
                newWidth = (int) (width * rate);
                newHeight = maxResolution;
            }
        }
        return Bitmap.createScaledBitmap(source, newWidth, newHeight, true);
    }
}