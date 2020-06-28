package org.techtown.capstoneprojectcocktail;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static org.techtown.capstoneprojectcocktail.CocktailAdapterForSearch.useByMinFlag;
import static org.techtown.capstoneprojectcocktail.MJH_Popup2Activity.updateTotalVol;
import static org.techtown.capstoneprojectcocktail.MJH_SimulatorUiActivity.listUpdateTech;
import static org.techtown.capstoneprojectcocktail.MJH_SimulatorUiActivity.test;
import static org.techtown.capstoneprojectcocktail.MJH_SimulatorUiActivity.usingStepNum;

public class MJH_Popup3Activity extends Activity {

    final CocktailAdapterForSearch adapterForCocktailSearch = new CocktailAdapterForSearch();

    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "DocSnippets";

    RecyclerView recyclerViewForCocktailSearch;
    ArrayList<MJH_Object_ingredient> bufferUpdateIngredient;
    ArrayList<Float> bufferUpdateIngredientAmount;

    //db 재료 저장용 리스트 선언
    //이름, 번호, 향, 당도, 도수 , ref (additem)
    private ArrayList I_name = new ArrayList();
    private ArrayList I_ID = new ArrayList();
    private ArrayList I_flavour = new ArrayList();
    private ArrayList I_sugar = new ArrayList();
    private ArrayList I_abv = new ArrayList();
    private ArrayList I_ref = new ArrayList();

    //비중
    private ArrayList I_gravity = new ArrayList();
    /////////////////////////////////////////////
    MJH_SimulatorUiActivity simulatorUiAddress;
    public static Context uiThis;

    public static int ingreAmountFlag = 0;
    public static float ingreAmount = 0;

    public static float thisStepPopup3AddAmount= 0;

    public int buttonPressCheck = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiThis = this;
        useByMinFlag = 1;

        bufferUpdateIngredient = new ArrayList<MJH_Object_ingredient>() ;
        bufferUpdateIngredientAmount = new ArrayList<Float>();

        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.mjh_popup3);
        simulatorUiAddress = ((MJH_SimulatorUiActivity)MJH_SimulatorUiActivity.uiMain);


        ListView listview ;


        ////////////////////////////
        setAdapterForIngredientSearch();


        final EditText textForSearch = (EditText) findViewById(R.id.editText_search);

        textForSearch.setImeOptions(EditorInfo.IME_ACTION_DONE);
        textForSearch.setText("");
        textForSearch.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String inputText = textForSearch.getText().toString();
                    //재료 검색 모드에서 검색
                    Toast.makeText(getApplicationContext(), inputText + " 재료 검색", Toast.LENGTH_SHORT).show();
                    adapterForCocktailSearch.filterForIngredient(inputText);
                    recyclerViewForCocktailSearch.setAdapter(adapterForCocktailSearch);
                    return false;
                }
                return false;
            }
        });

        recyclerViewForCocktailSearch = findViewById(R.id.recyclerViewForCocktail_search);
        LinearLayoutManager layoutManagerForCocktailSearch = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        recyclerViewForCocktailSearch.setLayoutManager(layoutManagerForCocktailSearch);




        adapterForCocktailSearch.setOnItemClickListener(new OnCocktailItemClickListenerForSearch() {
            @Override
            public void onItemClick(CocktailAdapterForSearch.ViewHolder holder, View view, int position) {
                Cocktail item = adapterForCocktailSearch.getItem(position);
                //Toast.makeText(getActivity().getApplicationContext(),"선택된 칵테일: " + item.getName(),Toast.LENGTH_LONG).show();
                for(int i = 0; i < 140; i++){
                    if(simulatorUiAddress.ingredientList[i].name.equals(item.getName())){
                        for(int j = 0; j < bufferUpdateIngredient.size(); j++){
                            if( bufferUpdateIngredient.get(j).name.equals(item.getName()) && buttonPressCheck == 0){
                                Toast.makeText(uiThis,"이미 추가된 재료 입니다",Toast.LENGTH_LONG).show();
                                return;
                            }
                        }

                        if(buttonPressCheck == 0){
                            Toast.makeText(uiThis,"선택된 재료: " + item.getName(),Toast.LENGTH_LONG).show();
                            bufferUpdateIngredient.add(simulatorUiAddress.ingredientList[i]);
                        }
                    }
                }

                if( buttonPressCheck == 0){
                    buttonPressCheck = 1;
                    Intent intent2 = new Intent(uiThis, MJH_Popup4Activity.class);
                    startActivityForResult(intent2, 1);
                }
            }
        });
    }

    public void mClose(View v){
        usingStepNum.remove(usingStepNum.size() - 1);
        updateTotalVol = 0;
        useByMinFlag = 0;
        finish();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    public void mNext(View v){
        //데이터 전달하기
        if(bufferUpdateIngredient.size() == 0 && simulatorUiAddress.listUpdateStep.size() == 0) {
            Toast.makeText(uiThis,"이번 스텝에 사용할 재료나 이전 스텝을 선택해주세요!",Toast.LENGTH_LONG).show();
        }
        else{
            simulatorUiAddress.listUpdateFlag = 1;
            simulatorUiAddress.listUpdateIngredient = bufferUpdateIngredient;
            simulatorUiAddress.listUpdateIngredientAmount =  bufferUpdateIngredientAmount;
            useByMinFlag = 0;
            updateTotalVol = 0;
            finish();
        }
;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }

    @Override
    protected void onResume() {
        super.onResume();
        buttonPressCheck = 0;
        if(ingreAmountFlag == 1){
            try{
                if(ingreAmount == -1){
                    Toast.makeText(this,"취소", Toast.LENGTH_SHORT).show();
                    bufferUpdateIngredient.remove(bufferUpdateIngredient.size()-1);
                    ingreAmountFlag = 0;
                }
                else{
                    Toast.makeText(this,"양 입력 완료", Toast.LENGTH_SHORT).show();
                    bufferUpdateIngredientAmount.add(ingreAmount);
                    ingreAmountFlag = 0;
                }
            }catch(Exception e){
            }
        }
    }


    private void setAdapterForIngredientSearch(){
        //Ingredient_type이 시럽인것만 나오도록
        try{
            if(listUpdateTech.equals("Gradient")){
                adapterForCocktailSearch.clearAllForAdapter();
                setAdapterForIngredientSearchByType("시럽");
            }
            else if(listUpdateTech.equals("Layering")){
                adapterForCocktailSearch.clearAllForAdapter();

                System.out.println("확인용 : " + (I_name.size()-1));
                db.collection("Ingredient").orderBy("Ingredient_name", Query.Direction.ASCENDING)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    I_name.clear();
                                    I_ID.clear();
                                    I_flavour.clear();
                                    I_sugar.clear();
                                    I_abv.clear();
                                    I_ref.clear();
                                    I_gravity.clear();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        float stdSg = test.simulatorStep.get(test.simulatorStep.size() - 1).specificGravity.get(test.simulatorStep.get(test.simulatorStep.size() - 1).specificGravity.size() - 1);
                                        if(Float.parseFloat(String.valueOf((document.get("specific_gravity")))) < stdSg )
                                        {
                                            if(document.get("Ingredient_type").equals("가니쉬"))
                                            {
                                                System.out.println("해당하는 문서는 표현되지 않아야 합니다.");
                                            }
                                            else if(document.get("Ingredient_type").equals("기타"))
                                            {
                                                System.out.println("해당하는 문서는 표현되지 않아야 합니다.");
                                            }
                                            else
                                            {
                                                I_name.add(document.get("Ingredient_name"));
                                                I_ID.add(document.getId());
                                                I_flavour.add(document.get("flavour"));
                                                I_sugar.add(document.get("sugar_rate"));
                                                I_abv.add(document.get("abv") + "%");
                                                I_ref.add(document.get("ref"));
                                                I_gravity.add(document.get("specific_gravity"));
                                                adapterForCocktailSearch.addItem(new Cocktail((String) I_name.get(I_name.size()-1),
                                                        Integer.parseInt((String) I_ID.get(I_ID.size()-1)),
                                                        (String) I_flavour.get(I_flavour.size()-1),
                                                        String.valueOf(I_sugar.get(I_sugar.size()-1)) ,
                                                        String.valueOf(I_abv.get(I_abv.size()-1)) ,
                                                        (String) I_ref.get(I_ref.size()-1)));
                                            }

                                        }
                                    }
                                    System.out.println("시럽인 재료들의 비중 값 : " + I_gravity );
                                    recyclerViewForCocktailSearch.setAdapter(adapterForCocktailSearch);
                                } else {
                                    System.out.println("해당하는 문서가 없습니다.");
                                }
                            }
                        });
            }

            else {
                adapterForCocktailSearch.clearAllForAdapter();
                setAdapterForIngredientSearchByType("베이스");
                setAdapterForIngredientSearchByType("리큐르");
                setAdapterForIngredientSearchByType("시럽");
                setAdapterForIngredientSearchByType("주스");
                setAdapterForIngredientSearchByType("음료");
                setAdapterForIngredientSearchByType("비터스");
            }
        }catch(Exception e){
        }
    }

    private void setAdapterForIngredientSearchByType(String _type){
        //Ingredient_type이 시럽인것만 나오도록
        db.collection("Ingredient").whereEqualTo("Ingredient_type", _type).orderBy("Ingredient_name", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            I_name.clear();
                            I_ID.clear();
                            I_flavour.clear();
                            I_sugar.clear();
                            I_abv.clear();
                            I_ref.clear();
                            I_gravity.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                I_name.add(document.get("Ingredient_name"));
                                I_ID.add(document.getId());
                                I_flavour.add(document.get("flavour"));
                                I_sugar.add(document.get("sugar_rate"));
                                I_abv.add(document.get("abv") + "%");
                                I_ref.add(document.get("ref"));
                                I_gravity.add(document.get("specific_gravity"));

                                adapterForCocktailSearch.addItem(new Cocktail((String) I_name.get(I_name.size()-1),
                                        Integer.parseInt((String) I_ID.get(I_ID.size()-1)),
                                        (String) I_flavour.get(I_flavour.size()-1),
                                        String.valueOf(I_sugar.get(I_sugar.size()-1)) ,
                                        String.valueOf(I_abv.get(I_abv.size()-1)) ,
                                        (String) I_ref.get(I_ref.size()-1)));
                            }
                            System.out.println("시럽인 재료들의 비중 값 : " + I_gravity );
                            recyclerViewForCocktailSearch.setAdapter(adapterForCocktailSearch);
                        } else {
                            System.out.println("해당하는 문서가 없습니다.");
                        }
                    }
                });

    }

}
