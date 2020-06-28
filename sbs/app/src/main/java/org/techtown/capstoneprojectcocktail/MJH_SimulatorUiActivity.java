package org.techtown.capstoneprojectcocktail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static org.techtown.capstoneprojectcocktail.MJH_Popup3Activity.thisStepPopup3AddAmount;

public class MJH_SimulatorUiActivity extends AppCompatActivity implements View.OnClickListener {
    public static Context uiMain;

    public FloatingActionButton floatingActionButtonForAddList, floatingActionButtonForAddList2, button_simulation_action;
    private Button switchGlassButton;
    private Button switchGraphicButton;
    public static MJH_ListviewAdapter adapterMIN;
    public ListView listview;

    public int isFirst = 0;
    public int glassType = 0;

    public int listUpdateFlag = 0;
    public int lastStepTechFlag = 0;

    public static ArrayList<Integer> usingStep = new ArrayList<Integer>();
    public static ArrayList<Integer> usingStepNum = new ArrayList<Integer>();

    public static String listUpdateTech; // 팝업1에서 선택된 기술
    public static ArrayList<Integer> listUpdateStep = new ArrayList<Integer>();
    public static ArrayList<MJH_Object_ingredient> listUpdateIngredient = new ArrayList<MJH_Object_ingredient>();
    public static ArrayList<Float> listUpdateIngredientAmount = new ArrayList<Float>();

    public static int stepNum = 0;

    public static MJH_Object_simulator test;

    public MJH_Object_ingredient[] ingredientList = new MJH_Object_ingredient[200];
    public int listCount = 0;
    public MJH_Object_color colorBuffer;
    public Map<String, Number> ingredientRGB;

    public CheckBox cb1;
    public CheckBox cb2;

    //잔의 초기상태는 하이볼
    public static boolean highBallChecked = true;
    public static boolean martiniChecked = false;
    public static boolean shooterChecked = false;

    public static boolean realisticChecked = true;
    public static boolean illustrationChecked = false;

    @Override
    public void onCreate(Bundle saveInstanceState){
        setAdapterForIngredientSearch();
        super.onCreate(saveInstanceState);
        setContentView(R.layout.mjh_test);
        uiMain = this;



        floatingActionButtonForAddList = (FloatingActionButton) findViewById(R.id.floatingActionButtonForAddList);
        floatingActionButtonForAddList.setOnClickListener(this);
        floatingActionButtonForAddList2 = (FloatingActionButton) findViewById(R.id.floatingActionButtonForAddList2);
        floatingActionButtonForAddList2.setOnClickListener(this);

        button_simulation_action = (FloatingActionButton) findViewById(R.id.button_simulation_action);
        button_simulation_action.setOnClickListener(this);

        switchGlassButton =  (Button) findViewById(R.id.switch_glass_simulation);
        switchGlassButton.setOnClickListener(this);

        switchGraphicButton = (Button) findViewById(R.id.switch_graphic_mode_simulation);
        switchGraphicButton.setOnClickListener(this);

        // Adapter 생성
        adapterMIN = new MJH_ListviewAdapter() ;
        adapterMIN.callByPopup = 0;

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.listview1);
        listview.setAdapter(adapterMIN);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                MJH_ListviewItem item = (MJH_ListviewItem) parent.getItemAtPosition(position) ;

                //Snackbar.make(v, titleStep, Snackbar.LENGTH_LONG).setAction("Action", null).show();

                // TODO : use item data.
            }
        }) ;

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (listUpdateFlag == 1) {
            try{
                stepNum++;
                adapterMIN.addItem(Integer.toString(stepNum), listUpdateTech, listUpdateStep, listUpdateIngredient, listUpdateIngredientAmount);
                listview.setAdapter(adapterMIN);
                listUpdateFlag = 0;
                if(listUpdateTech.equals("Layering")){
                    if(listUpdateStep.size() == 0){
                        test.addStepLayering(stepNum, 0, listUpdateIngredient.get(0), listUpdateIngredientAmount.get(0), 0);
                    }
                    else{
                        test.addStepLayering(stepNum, listUpdateStep.get(0), null, 0, 0);
                    }
                }
                else if(listUpdateTech.equals("Gradient")){
                    test.addStepLayering(stepNum, 0, listUpdateIngredient.get(0), listUpdateIngredientAmount.get(0), 1);
                }
                else{
                    if(stepNum != 1) {
                        listUpdateStep.add(stepNum-1);
                    }
                    test.addStepBuildings(stepNum, listUpdateStep, listUpdateIngredient, listUpdateIngredientAmount, true);
                    listUpdateStep.clear();
                }
            }catch(Exception e){
            }
            if(listUpdateTech.equals("Layering") || listUpdateTech.equals("Gradient") ){
                lastStepTechFlag = 1;
            }

            drawingCocktail();
        }
        adapterMIN.callByPopup = 0;
        thisStepPopup3AddAmount = 0;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.floatingActionButtonForAddList: // 시뮬 스텝 추가 팝업으로 넘어감

                if(isFirst == 0){
                    isFirst = 1;
                    test = new MJH_Object_simulator(glassType, 1); // 글래스타입 0 -> 하이볼 잔
                }

                Intent intent = new Intent(this,MJH_Popup1Activity.class);
                startActivityForResult(intent, 1);
                adapterMIN.callByPopup = 1;
                usingStepNum.add(0);
                break;
            case R.id.floatingActionButtonForAddList2: // 시뮬스텝 지우기
                TextView txt1 = (TextView)findViewById(R.id.abv_print);
                TextView txt2 = (TextView)findViewById(R.id.soju_num);
                txt1.setText(Integer.toString(0));
                txt2.setText("0.0");
                txt2.setTextColor(Color.parseColor("#333333"));
                if(stepNum != 0){
                    try{
                        int deleteBuff;
                        deleteBuff = adapterMIN.listViewItemList.size();
                        stepNum--;

                        if( adapterMIN.listViewItemList.get(deleteBuff-1).getTech().equals("Gradient")){
                            test.isGradient = 0;
                        }
                        adapterMIN.listViewItemList.remove(deleteBuff-1);
                        listview.setAdapter(adapterMIN);

                        //step 하나 지우기
                        test.simulatorStep.remove(deleteBuff-1);
                        test.totalStep--; // 전체 스텝 갯수
                        test.inGlassStep--;

                        listUpdateTech =""; // 팝업1에서 선택된 기술
                        listUpdateStep = new ArrayList<Integer>();
                        listUpdateIngredient = new ArrayList<MJH_Object_ingredient>();
                        listUpdateIngredientAmount = new ArrayList<Float>();

                        for(int i = 0; i <usingStepNum.get(usingStepNum.size() - 1); i++){
                            usingStep.remove(usingStep.size() - 1);
                        }

                        float nowCupVol = 0;
                        try{
                            nowCupVol = test.simulatorStep.get(test.simulatorStep.size() - 1).totalVolume;
                        }catch(Exception e){ }
                        if(highBallChecked == true && nowCupVol <= 250 ){

                            try{
                                test.glassType = 0;
                                drawingCocktail();
                            }catch(Exception e){ }
                        }
                        //현재 잔이 마티니일 경우
                        //슈터로 잔 변경
                        else if (martiniChecked == true && nowCupVol <= 140){
                            try{
                                test.glassType = 1;
                                drawingCocktail();
                            }catch(Exception e){ }
                        }
                        else if (shooterChecked  == true && nowCupVol <= 60){

                            try{
                                test.glassType = 2;
                                drawingCocktail();
                            }catch(Exception e){ }
                        }
                        else{
                            martiniChecked = false;
                            shooterChecked = false;
                            highBallChecked = true;
                            switchGlassButton.setText("Highball");

                            try{
                                test.glassType = 0;
                                drawingCocktail();
                            }catch(Exception e){ }
                            Toast myToast = Toast.makeText(this.getApplicationContext(),"해당 용량은 현재 잔에 불가합니다! 하이볼 잔으로 변환 됩니다.", Toast.LENGTH_SHORT);
                            myToast.show();
                        }

                    }catch(Exception e){
                    }
                }


                if(stepNum == 0){
                    lastStepTechFlag = 0;

                    ImageView View = (ImageView) findViewById(R.id.simulatorImage);
                    View .setImageResource(0);
                }
                else if(adapterMIN.listViewItemList.get(adapterMIN.listViewItemList.size()-1).getTech().equals("Layering") || adapterMIN.listViewItemList.get(adapterMIN.listViewItemList.size()-1).getTech().equals("Gradient") ){
                    lastStepTechFlag = 1;
                    drawingCocktail();
                }
                else{
                    lastStepTechFlag = 0;
                    drawingCocktail();
                }
                break;
            case R.id.button_simulation_action: // 맛 예측 팝업 작동시키기
                Intent intentForTasteInfo = new Intent(this, TasteInfoPopupActivity.class);
                try {
                    double heesangSugar = test.simulatorStep.get(test.simulatorStep.size() - 1).sugar;
                    double heesangSour = test.simulatorStep.get(test.simulatorStep.size() - 1).sour;
                    double heesangSalty = test.simulatorStep.get(test.simulatorStep.size() - 1).salty;
                    double heesangBitter = test.simulatorStep.get(test.simulatorStep.size() - 1).bitter;
                    double heesangHot = test.simulatorStep.get(test.simulatorStep.size() - 1).hot;
                    //단맛, 쓴맛, 신맛,짠맛,매운맛
                    intentForTasteInfo.putExtra("sugarValue",heesangSugar);
                    intentForTasteInfo.putExtra("bitterValue",heesangBitter);
                    intentForTasteInfo.putExtra("sourValue",heesangSour);
                    intentForTasteInfo.putExtra("saltyValue",heesangSalty);
                    intentForTasteInfo.putExtra("spicyValue",heesangHot);
                    startActivity(intentForTasteInfo);
                }catch (Exception e){}
                break;
                //글래스 선택 버튼
            case R.id.switch_glass_simulation:
                //현재 잔이 하이볼일 경우
                //마티니로 잔 변경
                float nowCupVol = 0;
                try{
                    nowCupVol = test.simulatorStep.get(test.simulatorStep.size() - 1).totalVolume;
                }catch(Exception e){ }
                if(highBallChecked == true && nowCupVol <= 140 ){
                    highBallChecked =false;
                    martiniChecked = true;
                    switchGlassButton.setText("Martini");

                    try{
                        test.glassType = 1;
                        drawingCocktail();
                    }catch(Exception e){ }
                }
                //현재 잔이 마티니일 경우
                //슈터로 잔 변경
                else if (martiniChecked == true && nowCupVol <= 60){
                    martiniChecked = false;
                    shooterChecked = true;
                    switchGlassButton.setText("Shooter");

                    try{
                        test.glassType = 2;
                        drawingCocktail();
                    }catch(Exception e){ }
                }
                else if (martiniChecked == true && nowCupVol > 60){ // 하이볼로 강제 변경
                    martiniChecked = false;
                    shooterChecked = false;
                    highBallChecked = true;
                    switchGlassButton.setText("Highball");

                    try{
                        test.glassType = 0;
                        drawingCocktail();
                        Toast myToast = Toast.makeText(this.getApplicationContext(),"해당 용량은 슈터잔이 불가 합니다.\n[하이볼로 대체되었습니다.]", Toast.LENGTH_SHORT);
                        myToast.show();
                    }catch(Exception e){ }
                }
                //현재 잔이 슈터일 경우
                //하이볼로 잔 변경
                else if (shooterChecked == true ){
                    shooterChecked = false;
                    highBallChecked = true;
                    switchGlassButton.setText("Highball");

                    try{
                        test.glassType = 0;
                        drawingCocktail();
                    }catch(Exception e){ }
                }
                else{
                    Toast myToast = Toast.makeText(this.getApplicationContext(),"해당 용량은 하이볼 잔만 가능입니다!", Toast.LENGTH_SHORT);
                    myToast.show();
                }
                break;
            //그래픽 모드 선택
            case R.id.switch_graphic_mode_simulation:
                if (realisticChecked == true){
                    realisticChecked = false;
                    illustrationChecked = true;
                    switchGraphicButton.setText("Illustration");
                }
                else if (illustrationChecked == true){
                    illustrationChecked = false;
                    realisticChecked = true;
                    switchGraphicButton.setText("Realistic");

                }
                drawingCocktail();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                //데이터 받기
                //String result = data.getStringExtra("result");
                //Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        isFirst = 0;

        listUpdateFlag = 0;
        lastStepTechFlag = 0;

        usingStep = new ArrayList<Integer>();
        usingStepNum = new ArrayList<Integer>();


        listUpdateStep = new ArrayList<Integer>();
        listUpdateIngredient = new ArrayList<MJH_Object_ingredient>();
        listUpdateIngredientAmount = new ArrayList<Float>();

        stepNum = 0;

        ingredientList = new MJH_Object_ingredient[200];
        listCount = 0;


        martiniChecked = false;
        shooterChecked = false;
        highBallChecked = true;

        test = null;
        finish();
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
                                ingredientList[listCount].type = (String)document.get("Ingredient_type");
                                ingredientList[listCount].flavour = (String)document.get("flavour");
                                ingredientList[listCount].id = 5001+ listCount;
                                ingredientList[listCount].abv = Float.parseFloat(document.get("abv").toString());
                                ingredientList[listCount].sugar = Double.parseDouble(document.get("단맛").toString());
                                ingredientList[listCount].sour = Double.parseDouble(document.get("신맛").toString());
                                ingredientList[listCount].salty = Double.parseDouble(document.get("짠맛").toString());
                                ingredientList[listCount].bitter = Double.parseDouble(document.get("쓴맛").toString());
                                ingredientList[listCount].hot = Double.parseDouble(document.get("매운맛").toString());
                                ingredientList[listCount].specific_gravity = Float.parseFloat(document.get("specific_gravity").toString());

                                ingredientRGB = (Map<String, Number>) document.getData().get("Ingredient_color");
                                colorBuffer = new MJH_Object_color(Float.parseFloat(ingredientRGB.get("Red").toString()), Float.parseFloat(ingredientRGB.get("Green").toString()),
                                        Float.parseFloat(ingredientRGB.get("Blue").toString()));
                                ingredientList[listCount].alpha = Float.parseFloat(document.get("alpha").toString());
                                ingredientList[listCount].muddy = Float.parseFloat(document.get("muddy").toString());

                                ingredientList[listCount].my_color = colorBuffer;
                            }catch (Exception e){
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


    public void drawingCocktail(){

        try{
            String _buff = "";
            for(int i = 0; i < test.simulatorStep.get(test.simulatorStep.size() - 1).ingredListForFlavour.size(); i++){
                _buff =  _buff + "/" + test.simulatorStep.get(test.simulatorStep.size() - 1).ingredListForFlavour.get(i).flavour;
            }
            //Toast myToast = Toast.makeText(this.getApplicationContext(), _buff, Toast.LENGTH_SHORT);
            //myToast.show();
        }catch (Exception e){}

        try{

            if(highBallChecked == true){
                ImageView View = (ImageView) findViewById(R.id.simulatorImage);
                ObjectHighballGlass graphic = new  ObjectHighballGlass();
                //ObjectShooterGlass graphic = new  ObjectShooterGlass();
                graphic.draw(View, uiMain);
            }
            else if(martiniChecked == true) {
                ImageView View = (ImageView) findViewById(R.id.simulatorImage);
                ObjectMartiniGlass graphic = new  ObjectMartiniGlass();
                graphic.draw(View, uiMain);
            }

            else if(shooterChecked == true) {
                ImageView View = (ImageView) findViewById(R.id.simulatorImage);
                ObjectShooterGlass graphic = new  ObjectShooterGlass();
                graphic.draw(View, uiMain);
            }
        }catch (Exception e){}

        try{
            int testIndex = test.simulatorStep.size();
            float abvBuf = test.simulatorStep.get(testIndex - 1).totalAbv;
            float volBuf = test.simulatorStep.get(testIndex - 1).totalVolume;

            TextView txt1 = (TextView)findViewById(R.id.abv_print);
            TextView txt2 = (TextView)findViewById(R.id.soju_num);

            float sojuUnit = 0;
            sojuUnit = (abvBuf * volBuf) / 900;
            double sojuUnitDouble = 0.0;
            sojuUnitDouble = Math.round(sojuUnit*10)/10.0;
            txt1.setText(Integer.toString((int)abvBuf));
            txt2.setText(Double.toString((double)sojuUnitDouble));
            if(sojuUnit >= 4){
                txt2.setTextColor(Color.RED);
            }
            else{
                txt2.setTextColor(Color.parseColor("#333333"));
            }
        }catch (Exception e){}
    }
}
