package org.techtown.capstoneprojectcocktail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;

import static org.techtown.capstoneprojectcocktail.MJH_SimulatorUiActivity.test;

public class TasteInfoPopupActivity extends Activity {

    private InputMethodManager inputKeyboardHide;
    private TextView textFor1Cocktail, textFor2Cocktail, textFor3Cocktail;
    private TextView textForTasteInfo;
    private TextView textFor1FlavorInfo,textFor2FlavorInfo ,textFor3FlavorInfo;
    CocktailTasteInfo cocktailTasteInfo = new CocktailTasteInfo();
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    String[] Cocktail_name = new String[81];
    int maxIndex = 0;
    int secondMaxIndex = 0;
    int thirdMaxIndex = 0;
    double sugarValue;
    double bitterValue;
    double sourValue;
    double saltyValue;
    double spicyValue;

    double[] tempArray = new double[81];
    double max = tempArray[0]; //최대값
    double secondMax = tempArray[0]; //최대값
    double thirdMax = tempArray[0]; //최대값

    //재선언
    private ArrayList Recipe_name;

    //맛 선언
    private ArrayList Recipe_sugar;
    private ArrayList Recipe_hot;
    private ArrayList Recipe_sour;
    private ArrayList Recipe_bitter;
    private ArrayList Recipe_salty;

    private final double bittersWeight = 10.0;
    private final double syrupWeight = 2.0;
    private final double juiceWeight = 1.0;
    private final double drinkWeight = 0.7;
    private final double liqueurWeight = 0.7;
    private final double baseWeight = 0.2;

    private ArrayList volumeWithWeight;
    private ArrayList volumeWithWeightWithoutDuplicate;
    private ArrayList<String> flavorList;
    private ArrayList<String> flavorListWithoutDuplicate;

    //private double totalVol = 0.0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.taste_info_popup_activity);
        //키보드 숨기기
        inputKeyboardHide = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        textFor1Cocktail = (TextView) findViewById(R.id.textView_taste_info_1);
        textFor2Cocktail = (TextView) findViewById(R.id.textView_taste_info_2);
        textFor3Cocktail = (TextView) findViewById(R.id.textView_taste_info_3);

        textForTasteInfo = (TextView) findViewById(R.id.textView_taste_taste_info);
        textFor1FlavorInfo = (TextView) findViewById(R.id.textView_flavor_taste_info_1);
        textFor2FlavorInfo = (TextView) findViewById(R.id.textView_flavor_taste_info_2);
        textFor3FlavorInfo = (TextView) findViewById(R.id.textView_flavor_taste_info_3);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        //넘겨받은 칵테일 아아디
        sugarValue = intent.getDoubleExtra("sugarValue",0);
        bitterValue = intent.getDoubleExtra("bitterValue",0);
        sourValue = intent.getDoubleExtra("sourValue",0);
        saltyValue = intent.getDoubleExtra("saltyValue",0);
        spicyValue = intent.getDoubleExtra("spicyValue",0);

        tempArray = new double[81];
        max = tempArray[0]; //최대값
        secondMax = tempArray[0]; //최대값
        thirdMax = tempArray[0]; //최대값

        //재선언 초기화
        Recipe_name = new ArrayList();

        //맛 선언 초기화
        Recipe_sugar = new ArrayList();
        Recipe_hot = new ArrayList();
        Recipe_sour = new ArrayList();
        Recipe_bitter = new ArrayList();
        Recipe_salty = new ArrayList();


        db.collection("Recipe")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Recipe_name.add(document.get("Recipe_name").toString());       //레시피 이름
                                Recipe_sugar.add(document.get("단맛").toString());             //레시피의 단맛
                                Recipe_hot.add(document.get("매운맛").toString());             //레시피의 매운맛
                                Recipe_sour.add(document.get("신맛").toString());              //레시피의 신맛
                                Recipe_bitter.add(document.get("쓴맛").toString());            //레시피의 쓴맛
                                Recipe_salty.add(document.get("짠맛").toString());             //레시피의 짠맛
                            }
                            //여기에 밑에 함수 넣듯이 넣어서 써먹으면 무조건 다 들어간 이후에 작동하는거라 null값 안들어가요(예시 SetDocument)
                            SetDocument();
                        } else {
                            System.out.println("오류 발생 Grading 컬렉션에서 정상적으로 불러와지지 않음.");
                        }
                    }
                });
    }

    public void SetDocument(){
        //단맛, 쓴맛, 신맛,짠맛,매운맛
        double recipe_sugar_sum = 0.0;
        double recipe_bitter_sum = 0.0;
        double recipe_sour_sum = 0.0;
        double recipe_salty_sum = 0.0;
        double recipe_hot_sum = 0.0;

        String recipe_sugar_sum_string;
        String recipe_bitter_sum_string;
        String recipe_sour_sum_string;
        String recipe_salty_sum_string;
        String recipe_hot_sum_string;

        for (int i =0;i<81;i++){
            //단맛, 쓴맛, 신맛,짠맛,매운맛
            double[] tasteInfo = {sugarValue,bitterValue,sourValue,saltyValue,spicyValue};
            tempArray[i]=cosineSimilarity(cocktailTasteInfo.cocktailInfo[i],tasteInfo);
            //영진 여기 로그로 바꾸든지 알아서
        }
        for(int i=0;i<tempArray.length;i++) {
            if(max<tempArray[i]) {
                max = tempArray[i];
                maxIndex = i;
            }
        }
        //최대값 0.0으로 값 줄임
        tempArray[maxIndex]=0.0;
        for(int i=0;i<tempArray.length;i++) {
            if(secondMax<tempArray[i]) {
                secondMax = tempArray[i];
                secondMaxIndex = i;
            }
        }
        //두번째 최대값 0.0으로 값 줄임
        tempArray[secondMaxIndex]=0.0;
        for(int i=0;i<tempArray.length;i++) {
            if(thirdMax<tempArray[i]) {
                thirdMax = tempArray[i];
                thirdMaxIndex = i;
            }
        }
        //유사도가 떨어지는 칵테일은 출력하지 않음
        //단맛, 쓴맛, 신맛,짠맛,매운맛
        if (max<0.92){
            textFor1Cocktail.setText("1순위 : 유사한 칵테일 없음");
            textFor2Cocktail.setText("2순위 : 유사한 칵테일 없음");
            textFor3Cocktail.setText("3순위 : 유사한 칵테일 없음");
        }
        else if(secondMax <0.92){
            textFor1Cocktail.setText("1순위 : " + Recipe_name.get(maxIndex).toString());
            textFor2Cocktail.setText("2순위 : 유사한 칵테일 없음");
            textFor3Cocktail.setText("3순위 : 유사한 칵테일 없음");

            recipe_sugar_sum = Double.parseDouble(Recipe_sugar.get(maxIndex).toString()) *1;
            recipe_bitter_sum = Double.parseDouble(Recipe_bitter.get(maxIndex).toString()) *1;
            recipe_sour_sum = Double.parseDouble(Recipe_sour.get(maxIndex).toString()) *1;
            recipe_salty_sum = Double.parseDouble(Recipe_salty.get(maxIndex).toString()) *1;
            recipe_hot_sum = Double.parseDouble(Recipe_hot.get(maxIndex).toString()) *1;
        }
        else if (thirdMax <0.92){
            textFor1Cocktail.setText("1순위 : " + Recipe_name.get(maxIndex).toString());
            textFor2Cocktail.setText("2순위 : " + Recipe_name.get(secondMaxIndex).toString());
            textFor3Cocktail.setText("3순위 : 유사한 칵테일 없음");

            recipe_sugar_sum = Double.parseDouble(Recipe_sugar.get(maxIndex).toString()) *1+Double.parseDouble(Recipe_sugar.get(secondMaxIndex).toString())*0.5;
            recipe_bitter_sum = Double.parseDouble(Recipe_bitter.get(maxIndex).toString()) *1+Double.parseDouble(Recipe_bitter.get(secondMaxIndex).toString())*0.5;
            recipe_sour_sum = Double.parseDouble(Recipe_sour.get(maxIndex).toString()) *1 + Double.parseDouble(Recipe_sour.get(secondMaxIndex).toString())*0.5;
            recipe_salty_sum = Double.parseDouble(Recipe_salty.get(maxIndex).toString()) *1 +Double.parseDouble(Recipe_salty.get(secondMaxIndex).toString())*0.5;
            recipe_hot_sum = Double.parseDouble(Recipe_hot.get(maxIndex).toString()) *1 + Double.parseDouble(Recipe_hot.get(secondMaxIndex).toString())*0.5;
        }
        else{
            textFor1Cocktail.setText("1순위 : " + Recipe_name.get(maxIndex).toString());
            textFor2Cocktail.setText("2순위 : " + Recipe_name.get(secondMaxIndex).toString());
            textFor3Cocktail.setText("3순위 : " + Recipe_name.get(thirdMaxIndex).toString());

            recipe_sugar_sum = Double.parseDouble(Recipe_sugar.get(maxIndex).toString()) *1+Double.parseDouble(Recipe_sugar.get(secondMaxIndex).toString())*0.5+Double.parseDouble(Recipe_sugar.get(thirdMaxIndex).toString())*0.3;
            recipe_bitter_sum = Double.parseDouble(Recipe_bitter.get(maxIndex).toString()) *1+Double.parseDouble(Recipe_bitter.get(secondMaxIndex).toString())*0.5+Double.parseDouble(Recipe_bitter.get(thirdMaxIndex).toString())*0.3;
            recipe_sour_sum = Double.parseDouble(Recipe_sour.get(maxIndex).toString()) *1 + Double.parseDouble(Recipe_sour.get(secondMaxIndex).toString())*0.5+ Double.parseDouble(Recipe_sour.get(thirdMaxIndex).toString())*0.3;
            recipe_salty_sum = Double.parseDouble(Recipe_salty.get(maxIndex).toString()) *1 +Double.parseDouble(Recipe_salty.get(secondMaxIndex).toString())*0.5+Double.parseDouble(Recipe_salty.get(thirdMaxIndex).toString())*0.3;
            recipe_hot_sum = Double.parseDouble(Recipe_hot.get(maxIndex).toString()) *1 + Double.parseDouble(Recipe_hot.get(secondMaxIndex).toString())*0.5+ Double.parseDouble(Recipe_hot.get(thirdMaxIndex).toString())*0.3;;
        }

        recipe_sugar_sum_string = Integer.toString((int)recipe_sugar_sum);
        recipe_bitter_sum_string = Integer.toString((int)recipe_bitter_sum);
        recipe_sour_sum_string = Integer.toString((int)recipe_sour_sum);
        recipe_salty_sum_string=Integer.toString((int)recipe_salty_sum);
        recipe_hot_sum_string = Integer.toString((int)recipe_hot_sum);

        String tResult = printTaste(
                (int)recipe_sugar_sum
                ,(int)recipe_bitter_sum
                ,(int)recipe_sour_sum
                ,(int)recipe_salty_sum
                ,(int)recipe_hot_sum);

        if(tResult.equals("두드러지는 맛이 없습니다"))
            textForTasteInfo.setText(tResult);
        else
            textForTasteInfo.setText(tResult + "이 두드러지는 칵테일 입니다.");
        /*
        textForTasteInfo.setText("단맛 : "+recipe_sugar_sum_string+
                "\n쓴맛 : "+recipe_bitter_sum_string+
                "\n신맛 : "+recipe_sour_sum_string+
                "\n짠맛 : "+recipe_salty_sum_string+
                "\n매운맛 : "+recipe_hot_sum_string);
                */

        //향 표현현
        //트링 공백 지우기
        volumeWithWeight = new ArrayList();
        volumeWithWeightWithoutDuplicate = new ArrayList();
        flavorList = new ArrayList<String>();
        flavorListWithoutDuplicate = new ArrayList<String>();

        try{
            int ingredientSize = test.simulatorStep.get(test.simulatorStep.size() - 1).ingredListForFlavour.size();
            for(int i = 0; i < ingredientSize; i++){
                //String name = test.simulatorStep.get(test.simulatorStep.size() - 1).ingredListForFlavour.get(i).name.replaceAll(" ","");
                //String flavour = test.simulatorStep.get(test.simulatorStep.size() - 1).ingredListForFlavour.get(i).flavour.replaceAll(" ","");
                flavorList.add(test.simulatorStep.get(test.simulatorStep.size() - 1).ingredListForFlavour.get(i).flavour.replaceAll(" ",""));
                String type = test.simulatorStep.get(test.simulatorStep.size() - 1).ingredListForFlavour.get(i).type;
                double inputVol = test.simulatorStep.get(test.simulatorStep.size() - 1).ingredListForFlavour.get(i).volForFlavour;
                //totalVol+=inputVol;
                if(type.equals("비터스")){
                    volumeWithWeight.add((double) (inputVol*bittersWeight));
                }
                else if (type.equals("시럽")){
                    volumeWithWeight.add((double) (inputVol*syrupWeight));
                }
                else if (type.equals("주스")){
                    volumeWithWeight.add((double) (inputVol*juiceWeight));
                }
                else if (type.equals("음료")){
                    volumeWithWeight.add((double) (inputVol*drinkWeight));
                }
                else if (type.equals("리큐르")){
                    volumeWithWeight.add((double) (inputVol*liqueurWeight));
                }
                else if (type.equals("베이스")){
                    volumeWithWeight.add((double) (inputVol*baseWeight));
                }
                else{
                    volumeWithWeight.add((double) 0.0);
                }
            }
            for(int i = 0; i < ingredientSize; i++){
                if(!flavorListWithoutDuplicate.contains(flavorList.get(i))) {
                    flavorListWithoutDuplicate.add(flavorList.get(i));
                    volumeWithWeightWithoutDuplicate.add(volumeWithWeight.get(i));
                }
                else{
                    volumeWithWeightWithoutDuplicate.set(flavorListWithoutDuplicate.indexOf(flavorList.get(i)),
                            (( (double) volumeWithWeight.get(i))+( (double) volumeWithWeightWithoutDuplicate.get(flavorListWithoutDuplicate.indexOf(flavorList.get(i))))));
                }
            }
            //System.out.println(flavorListWithoutDuplicate.size());
            for(int i = 0; i < volumeWithWeightWithoutDuplicate.size(); i++){
                if(flavorListWithoutDuplicate.get(i).equals("")) {
                    volumeWithWeightWithoutDuplicate.set(i,(double) 0.0);
                }
            }
            //System.out.println(totalVol);
            if (volumeWithWeightWithoutDuplicate.size() <=0){
                textFor1FlavorInfo.setText("1순위 : 예측되는 향 없음");
                textFor2FlavorInfo.setText("2순위 : 예측되는 향 없음");
                textFor3FlavorInfo.setText("3순위 : 예측되는 향 없음");
            }
            else if (volumeWithWeightWithoutDuplicate.size()==1){
                if (((double) Collections.max(volumeWithWeightWithoutDuplicate))>1.0){
                    textFor1FlavorInfo.setText("1순위 : "+ flavorListWithoutDuplicate.get(volumeWithWeightWithoutDuplicate.indexOf((double) Collections.max(volumeWithWeightWithoutDuplicate))));
                }
                else{
                    textFor1FlavorInfo.setText("1순위 : 예측되는 향 없음");
                }
                textFor2FlavorInfo.setText("2순위 : 예측되는 향 없음");
                textFor3FlavorInfo.setText("3순위 : 예측되는 향 없음");
            }
            else if (volumeWithWeightWithoutDuplicate.size()==2){
                if (((double) Collections.max(volumeWithWeightWithoutDuplicate))>1.0){
                    textFor1FlavorInfo.setText("1순위 : "+ flavorListWithoutDuplicate.get(volumeWithWeightWithoutDuplicate.indexOf((double) Collections.max(volumeWithWeightWithoutDuplicate))));
                    volumeWithWeightWithoutDuplicate.set(volumeWithWeightWithoutDuplicate.indexOf((double) Collections.max(volumeWithWeightWithoutDuplicate)),(double)0.0);
                    if (((double) Collections.max(volumeWithWeightWithoutDuplicate))>1.0){
                        textFor2FlavorInfo.setText("2순위 : "+ flavorListWithoutDuplicate.get(volumeWithWeightWithoutDuplicate.indexOf((double) Collections.max(volumeWithWeightWithoutDuplicate))));
                    }
                    else{
                        textFor2FlavorInfo.setText("2순위 : 예측되는 향 없음");
                    }
                }
                else{
                    textFor1FlavorInfo.setText("1순위 : 예측되는 향 없음");
                    textFor2FlavorInfo.setText("2순위 : 예측되는 향 없음");
                }
                textFor3FlavorInfo.setText("3순위 : 예측되는 향 없음");
            }
            else{
                if (((double) Collections.max(volumeWithWeightWithoutDuplicate))>1.0){
                    textFor1FlavorInfo.setText("1순위 : "+ flavorListWithoutDuplicate.get(volumeWithWeightWithoutDuplicate.indexOf((double) Collections.max(volumeWithWeightWithoutDuplicate))));
                    volumeWithWeightWithoutDuplicate.set(volumeWithWeightWithoutDuplicate.indexOf((double) Collections.max(volumeWithWeightWithoutDuplicate)),(double)0.0);
                    if (((double) Collections.max(volumeWithWeightWithoutDuplicate))>1.0){
                        textFor2FlavorInfo.setText("2순위 : "+ flavorListWithoutDuplicate.get(volumeWithWeightWithoutDuplicate.indexOf((double) Collections.max(volumeWithWeightWithoutDuplicate))));
                        volumeWithWeightWithoutDuplicate.set(volumeWithWeightWithoutDuplicate.indexOf((double) Collections.max(volumeWithWeightWithoutDuplicate)),(double)0.0);
                        if (((double) Collections.max(volumeWithWeightWithoutDuplicate))>1.0) {
                            textFor3FlavorInfo.setText("3순위 : " + flavorListWithoutDuplicate.get(volumeWithWeightWithoutDuplicate.indexOf((double) Collections.max(volumeWithWeightWithoutDuplicate))));
                        }
                        else{
                            textFor3FlavorInfo.setText("3순위 : 예측되는 향 없음");
                        }
                    }
                    else{
                        textFor2FlavorInfo.setText("2순위 : 예측되는 향 없음");
                        textFor3FlavorInfo.setText("3순위 : 예측되는 향 없음");
                    }
                }
                else{
                    textFor1FlavorInfo.setText("1순위 : 예측되는 향 없음");
                    textFor2FlavorInfo.setText("2순위 : 예측되는 향 없음");
                    textFor3FlavorInfo.setText("3순위 : 예측되는 향 없음");
                }
            }
        }catch(Exception e){System.out.println(e);}
}

    public void tastePopupClose(View v){
        finish();
    }

    public void tasteInfoPopupCancel(View v){
        finish();
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

    public static double cosineSimilarity(double[] vectorA, double[] vectorB) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        double result = 0.0;
        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            normA += Math.pow(vectorA[i], 2);
            normB += Math.pow(vectorB[i], 2);
        }
        result = dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
        if(Double.isNaN(result)) {
            return 0.0;
        }
        else {
            return result;
        }
    }


    public String printTaste(int a, int b, int c, int d, int e){
        ArrayList<Integer> result = new ArrayList<Integer>();
        result.add((int)a);
        result.add((int)b);
        result.add((int)c);
        result.add((int)d);
        result.add((int)e);

        int maxOne = Collections.max(result);
        for(int i = 0; i < result.size(); i++){
            if(result.get(i) == maxOne){
                result.remove(i);
            }
        }

        if(maxOne == 0)
            return "두드러지는 맛이 없습니다";

        int maxTwo = Collections.max(result);

        result.clear();
        result.add((int)a);
        result.add((int)b);
        result.add((int)c);
        result.add((int)d);
        result.add((int)e);

        String one = "";
        String two = "";

        int maxCount = -1;
        for(int i = 0; i < result.size(); i++){
            if(result.get(i) == maxOne){
                if(i == 0)
                    one = one + "단맛 ";
                if(i == 1)
                    one = one + "쓴맛 ";
                if(i == 2)
                    one = one + "신맛 ";
                if(i == 3)
                    one = one + "짠맛 ";
                if(i == 4)
                    one = one + "매운맛 ";
                maxCount++;
            }
        }

        if(maxCount < 1){
            for(int i = 0; i < result.size(); i++){
                if(result.get(i) == maxTwo){
                    if(i == 0)
                        one = one + "단맛 ";
                    if(i == 1)
                        one = one + "쓴맛 ";
                    if(i == 2)
                        one = one + "신맛 ";
                    if(i == 3)
                        one = one + "짠맛 ";
                    if(i == 4)
                        one = one + "매운맛 ";
                    maxCount++;
                }
            }
        }
        return one.trim();
    }

}
