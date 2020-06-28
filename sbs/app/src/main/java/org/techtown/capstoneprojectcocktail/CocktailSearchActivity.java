package org.techtown.capstoneprojectcocktail;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CocktailSearchActivity extends AppCompatActivity{

    final CocktailAdapterForSearch adapterForCocktailSearch = new CocktailAdapterForSearch();
    //final CocktailAdapterForSearch adapterForSelfCocktailSearch = new CocktailAdapterForSearch();

    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "DocSnippets";
    //레시피 검색 플래그
    int Recipe_case = 0;
    int Self_case = 0;

    //뷰 모드 상태 플래그(기존/사용자)
    int view_case = 0;

    //레시피 정보를 받기 위한 변수들
    String[] Recipe_name = new String[81];
    int[] ID = new int[81];
    String[] method = new String[81];
    String[] Recipe_Base = new String[81];
    String[] abv = new String[81];
    String[] ref = new String[81];
    Map<String, Number> Recipe_Ingredient;
    long[] Realabv = new long[81];
    int count;

    //재료 정보를 받기 위한 변수들
    String[] Ingredient_name = new String[140];
    int[] Ingredient_ID = new int[140];
    long[] Ingredient_Realabv = new long[140];
    String[] Ingredient_abv = new String[140];
    String[] Ingredient_ref = new String[140];
    Number[] Ingredient_sugar = new Number[140];
    String[] Ingredient_Realsugar = new String[140];
    String[] Ingredient_flavour = new String[140];
    String[] Ingredient_specific_gravity = new String[140];
    int Ingredient_count;

    //사용자들의 레시피 정보를 받기위한 변수
    ArrayList Self_name = new ArrayList();
    ArrayList Self_id = new ArrayList();
    ArrayList Self_description = new ArrayList();
    ArrayList Self_base = new ArrayList();
    ArrayList Self_user = new ArrayList();
    ArrayList Self_url = new ArrayList();

    RecyclerView recyclerViewForCocktailSearch;

    @Override
    protected  void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.cocktail_search_activity);

        final ToggleButton toggleForCocktailOrIngredient = findViewById(R.id.switch_ingredient_check);
        final Switch switchForUserMade = findViewById(R.id.switch_userRecipe_search);
        final Spinner spinner = (Spinner) findViewById(R.id.spinner_orderBy_search);
        final EditText textForSearch = (EditText) findViewById(R.id.editText_search);
        //final RecyclerView recyclerViewForCocktailSearch = findViewById(R.id.recyclerViewForCocktail_search);
        recyclerViewForCocktailSearch = findViewById(R.id.recyclerViewForCocktail_search);

        Intent intent = getIntent();
        String ingredientName = intent.getExtras().getString("ingredientName");
        textForSearch.setImeOptions(EditorInfo.IME_ACTION_DONE);
        textForSearch.setText(ingredientName);

        //서치 액티비티를 실행할때 이미 입력되어 있는
        //시작 스트링
        //String initialText = textForSearch.getText().toString();
        //setAdapterForCocktailSearchMethod(textForSearch.getText().toString());

        LinearLayoutManager layoutManagerForCocktailSearch = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerViewForCocktailSearch.setLayoutManager(layoutManagerForCocktailSearch);
        recyclerViewForCocktailSearch.setAdapter(adapterForCocktailSearch);

        switchForUserMade.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                textForSearch.getText().clear();
                if (isChecked){
                    Toast.makeText(getApplicationContext(),"사용자 레시피 검색 ON",Toast.LENGTH_LONG).show();
                    //Toast.makeText(getApplicationContext(),"사용자 레시피 검색 ON" + spinner.getSelectedItemPosition(),Toast.LENGTH_LONG).show();
                    //adapterForCocktailSearch.clearAllForAdapter();
                    view_case = 1;
                    setAdapterForSelfCocktailSearchMethod("");

                }else{
                    Toast.makeText(getApplicationContext(),"사용자 레시피 검색 OFF",Toast.LENGTH_LONG).show();
                    //adapterForCocktailSearch.clearAllForAdapter();
                    view_case = 0;
                    setAdapterForCocktailSearchMethod("");
                }
            }
        });

        //레시피 검색모드와
        //재료 검색모드 변환
        toggleForCocktailOrIngredient.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                textForSearch.getText().clear();
                if (isChecked){
                    Toast.makeText(getApplicationContext(),"재료 검색 모드",Toast.LENGTH_LONG).show();
                    //switchForUserMade.setChecked(false);
                    //adapterForCocktailSearch.clearAllForAdapter();
                    switchForUserMade.setVisibility(View.GONE);
                    spinner.setVisibility(View.GONE);
                    setAdapterForIngredientSearch();
                }
                else{
                    Toast.makeText(getApplicationContext(),"칵테일 검색 모드",Toast.LENGTH_LONG).show();
                    //adapterForCocktailSearch.clearAllForAdapter();
                    switchForUserMade.setVisibility(View.VISIBLE);
                    spinner.setVisibility(View.VISIBLE);
                    //유저가 올린 칵테일 검색 모드가 켜져 있을 경우
                    if(switchForUserMade.isChecked()){
                        setAdapterForSelfCocktailSearchMethod("");
                    }
                    //유저가 올린 칵테일 검색 모드가 꺼져 있을 경우
                    else{
                        //Toast.makeText(getApplicationContext(),"칵테일 검색 모드",Toast.LENGTH_LONG).show();
                        setAdapterForCocktailSearchMethod("");
                    }
                }
            }
        });

        textForSearch.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String inputText = textForSearch.getText().toString();
                    //재료 검색 모드에서 검색
                    if (toggleForCocktailOrIngredient.isChecked()) {
                        Toast.makeText(getApplicationContext(), inputText + " 재료 검색", Toast.LENGTH_SHORT).show();
                        adapterForCocktailSearch.filterForIngredient(inputText);
                        recyclerViewForCocktailSearch.setAdapter(adapterForCocktailSearch);
                    }
                    //칵테일 검색 모드에서 검색
                    else {
                        //사용자들이 올린 칵테일 검색
                        if (switchForUserMade.isChecked()) {
                            Toast.makeText(getApplicationContext(), "사용자들이 올린 " + inputText + " 칵테일 검색", Toast.LENGTH_SHORT).show();
                            adapterForCocktailSearch.filterForCocktail(inputText);
                            recyclerViewForCocktailSearch.setAdapter(adapterForCocktailSearch);
                        }
                        //기존에 있는 칵테일 레시피 검색
                        else {
                            Toast.makeText(getApplicationContext(), inputText + " 칵테일 검색", Toast.LENGTH_SHORT).show();
                            adapterForCocktailSearch.filterForCocktail(inputText);
                            recyclerViewForCocktailSearch.setAdapter(adapterForCocktailSearch);
                        }
                    }
                }
                return false;
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView)parent.getChildAt(0)).setTextColor(Color.WHITE);
                //수정필
                //Toast.makeText(getApplicationContext(),"선택된 하이: " + parent.getItemAtPosition(position),Toast.LENGTH_LONG).show();
                //adapterForCocktailSearch.clearAllForAdapter();
                switch (position){
                    case 0:
                        //북마크 내림차순
                        Toast.makeText(getApplicationContext(),"선택된 정렬순서:" + parent.getItemAtPosition(position),Toast.LENGTH_LONG).show();
                        Recipe_case = 0; //플래그 북마크 내림차순
                        Self_case = 0;
                        if(view_case == 0)
                        {
                            setAdapterForCocktailSearchMethod(textForSearch.getText().toString());
                            recyclerViewForCocktailSearch.setAdapter(adapterForCocktailSearch);
                        }
                        else if(view_case == 1)
                        {
                            setAdapterForSelfCocktailSearchMethod(textForSearch.getText().toString());
                            recyclerViewForCocktailSearch.setAdapter(adapterForCocktailSearch);
                        }
                        else
                            System.out.println("뷰 케이스 오류발생.");
                        break;
                    case 1:
                        //북마크 오름차순
                        Toast.makeText(getApplicationContext(),"선택된 정렬순서:" + parent.getItemAtPosition(position),Toast.LENGTH_LONG).show();
                        Recipe_case = 1; //플래그 북마크 오름차순
                        Self_case = 1;
                        if(view_case == 0)
                        {
                            setAdapterForCocktailSearchMethod(textForSearch.getText().toString());
                            recyclerViewForCocktailSearch.setAdapter(adapterForCocktailSearch);
                        }
                        else if(view_case == 1)
                        {
                            setAdapterForSelfCocktailSearchMethod(textForSearch.getText().toString());
                            recyclerViewForCocktailSearch.setAdapter(adapterForCocktailSearch);
                        }
                        else
                            System.out.println("뷰 케이스 오류발생.");
                        break;
                    case 2:
                        //평점 내림차순
                        Toast.makeText(getApplicationContext(),"선택된 정렬순서:" + parent.getItemAtPosition(position),Toast.LENGTH_LONG).show();
                        Recipe_case = 2; //플래그 평가 내림차순
                        Self_case = 2;
                        if(view_case == 0)
                        {
                            setAdapterForCocktailSearchMethod(textForSearch.getText().toString());
                            recyclerViewForCocktailSearch.setAdapter(adapterForCocktailSearch);
                        }
                        else if(view_case == 1)
                        {
                            setAdapterForSelfCocktailSearchMethod(textForSearch.getText().toString());
                            recyclerViewForCocktailSearch.setAdapter(adapterForCocktailSearch);
                        }
                        else
                            System.out.println("뷰 케이스 오류발생.");
                        break;
                    case 3:
                        //평점 오름차순
                        Toast.makeText(getApplicationContext(),"선택된 정렬순서:" + parent.getItemAtPosition(position),Toast.LENGTH_LONG).show();
                        Recipe_case = 3; //플래그 평가 오름차순
                        Self_case = 3;
                        if(view_case == 0)
                        {
                            setAdapterForCocktailSearchMethod(textForSearch.getText().toString());
                            recyclerViewForCocktailSearch.setAdapter(adapterForCocktailSearch);
                        }
                        else if(view_case == 1)
                        {
                            setAdapterForSelfCocktailSearchMethod(textForSearch.getText().toString());
                            recyclerViewForCocktailSearch.setAdapter(adapterForCocktailSearch);
                        }
                        else
                            System.out.println("뷰 케이스 오류발생.");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        adapterForCocktailSearch.setOnItemClickListener(new OnCocktailItemClickListenerForSearch() {
            @Override
            public void onItemClick(CocktailAdapterForSearch.ViewHolder holder, View view, int position) {
                Cocktail item = adapterForCocktailSearch.getItem(position);
                //Toast.makeText(getActivity().getApplicationContext(),"선택된 칵테일: " + item.getName(),Toast.LENGTH_LONG).show();
                Intent intent = new Intent(view.getContext(), CocktailRecipeActivity.class);
                intent.putExtra("cocktailName", item.getName());
                intent.putExtra("cocktailID",item.getId());
                intent.putExtra("cocktailDescription",item.getDescription());
                intent.putExtra("cocktailIngredient",item.getIngredient());
                intent.putExtra("cocktailABV",item.getAbvNum());
                intent.putExtra("cocktailRef",item.getImageUrl());
                startActivity(intent);
            }
        });
    }
    //기존레시피
    private void setAdapterForCocktailSearchMethod(String str){
        final String _str = str;
        //전체 초기화
        count = 0;
        Recipe_name = new String[81];
        ID = new int[81];
        method = new String[81];
        Recipe_Base = new String[81];
        abv = new String[81];
        ref = new String[81];
        Realabv = new long[81];
        //recyclerViewForCocktailSearch.setAdapter(adapterForCocktailSearch);
        adapterForCocktailSearch.clearAllForAdapter();

        CollectionReference RecipeRef = db.collection("Recipe");

        if(Recipe_case == 0)
        {
            System.out.println("플래그 상태 : "+ Recipe_case);

            RecipeRef.orderBy("mark_number", Query.Direction.DESCENDING).orderBy("Recipe_name", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            //칵테일 이름, 칵테일 문서번호, 설명, 만드는방법, 칵테일 만든이, 이미지url
                            Recipe_name[count] = (String) document.get("Recipe_name");
                            ID[count] = Integer.parseInt(document.getId());
                            method[count] = (String) document.get("method");

                            Recipe_Ingredient = (Map<String, Number>) document.get("Ingredient_content");

                            //map으로 받아온 정보를 string으로 치환한뒤 유저에게 보여줄 수 있도록 replaceall함({, }, = 삭제 ml 추가)
                            Recipe_Base[count] = String.valueOf(Recipe_Ingredient);
                            Recipe_Base[count] = Recipe_Base[count].replaceAll("\\,", "ml ");
                            Recipe_Base[count] = Recipe_Base[count].replaceAll("\\{", " ");
                            Recipe_Base[count] = Recipe_Base[count].replaceAll("\\}", "ml ");
                            Recipe_Base[count] = Recipe_Base[count].replaceAll("\\=", " ");
                            //long형태로 받은 abv를 유저에게 보여줄 수 있도록 %를 붙여 재저장
                            Realabv[count] = (long) document.get("abv");
                            abv[count] = Realabv[count] + "%";
                            ref[count] = (String) document.get("ref");
                            adapterForCocktailSearch.addItem(new Cocktail(Recipe_name[count], ID[count], method[count], Recipe_Base[count], abv[count],ref[count]));
                            count++;
                            //refresh 해주는 함수(아마)
                            if (_str.length()==0){
                                recyclerViewForCocktailSearch.setAdapter(adapterForCocktailSearch);
                            }
                            else{
                                adapterForCocktailSearch.filterForCocktail(_str);
                                recyclerViewForCocktailSearch.setAdapter(adapterForCocktailSearch);
                            }
                        }

                    } else {
                        System.out.println("오류 발생 컬렉션에서 정상적으로 불러와지지 않음.");
                    }

                }
            });
        }
        else if(Recipe_case ==1)
        {
            System.out.println("플래그 상태 : "+ Recipe_case);

            RecipeRef.orderBy("mark_number", Query.Direction.ASCENDING).orderBy("Recipe_name", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            //칵테일 이름, 칵테일 문서번호, 설명, 만드는방법, 칵테일 만든이, 이미지url
                            Recipe_name[count] = (String) document.get("Recipe_name");
                            ID[count] = Integer.parseInt(document.getId());
                            method[count] = (String) document.get("method");

                            Recipe_Ingredient = (Map<String, Number>) document.get("Ingredient_content");

                            //map으로 받아온 정보를 string으로 치환한뒤 유저에게 보여줄 수 있도록 replaceall함({, }, = 삭제 ml 추가)
                            Recipe_Base[count] = String.valueOf(Recipe_Ingredient);
                            Recipe_Base[count] = Recipe_Base[count].replaceAll("\\,", "ml ");
                            Recipe_Base[count] = Recipe_Base[count].replaceAll("\\{", " ");
                            Recipe_Base[count] = Recipe_Base[count].replaceAll("\\}", "ml ");
                            Recipe_Base[count] = Recipe_Base[count].replaceAll("\\=", " ");
                            //long형태로 받은 abv를 유저에게 보여줄 수 있도록 %를 붙여 재저장
                            Realabv[count] = (long) document.get("abv");
                            abv[count] = Realabv[count] + "%";
                            ref[count] = (String) document.get("ref");
                            adapterForCocktailSearch.addItem(new Cocktail(Recipe_name[count], ID[count], method[count], Recipe_Base[count], abv[count],ref[count]));
                            count++;
                            //refresh 해주는 함수(아마)
                            if (_str.length()==0){
                                recyclerViewForCocktailSearch.setAdapter(adapterForCocktailSearch);
                            }
                            else{
                                adapterForCocktailSearch.filterForCocktail(_str);
                                recyclerViewForCocktailSearch.setAdapter(adapterForCocktailSearch);
                            }
                        }

                    } else {
                        System.out.println("오류 발생 컬렉션에서 정상적으로 불러와지지 않음.");
                    }

                }
            });
        }
        else if(Recipe_case ==2)
        {
            System.out.println("플래그 상태 : "+ Recipe_case);

            RecipeRef.orderBy("good_number", Query.Direction.DESCENDING).orderBy("Recipe_name", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            //칵테일 이름, 칵테일 문서번호, 설명, 만드는방법, 칵테일 만든이, 이미지url
                            Recipe_name[count] = (String) document.get("Recipe_name");
                            ID[count] = Integer.parseInt(document.getId());
                            method[count] = (String) document.get("method");

                            Recipe_Ingredient = (Map<String, Number>) document.get("Ingredient_content");

                            //map으로 받아온 정보를 string으로 치환한뒤 유저에게 보여줄 수 있도록 replaceall함({, }, = 삭제 ml 추가)
                            Recipe_Base[count] = String.valueOf(Recipe_Ingredient);
                            Recipe_Base[count] = Recipe_Base[count].replaceAll("\\,", "ml ");
                            Recipe_Base[count] = Recipe_Base[count].replaceAll("\\{", " ");
                            Recipe_Base[count] = Recipe_Base[count].replaceAll("\\}", "ml ");
                            Recipe_Base[count] = Recipe_Base[count].replaceAll("\\=", " ");
                            //long형태로 받은 abv를 유저에게 보여줄 수 있도록 %를 붙여 재저장
                            Realabv[count] = (long) document.get("abv");
                            abv[count] = Realabv[count] + "%";
                            ref[count] = (String) document.get("ref");
                            adapterForCocktailSearch.addItem(new Cocktail(Recipe_name[count], ID[count], method[count], Recipe_Base[count], abv[count],ref[count]));
                            count++;
                            //refresh 해주는 함수(아마)
                            if (_str.length()==0){
                                recyclerViewForCocktailSearch.setAdapter(adapterForCocktailSearch);
                            }
                            else{
                                adapterForCocktailSearch.filterForCocktail(_str);
                                recyclerViewForCocktailSearch.setAdapter(adapterForCocktailSearch);
                            }
                        }

                    } else {
                        System.out.println("오류 발생 컬렉션에서 정상적으로 불러와지지 않음.");
                    }

                }
            });
        }
        else if(Recipe_case ==3)
        {
            System.out.println("플래그 상태 : "+ Recipe_case);
            RecipeRef.orderBy("good_number", Query.Direction.ASCENDING).orderBy("Recipe_name", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            //칵테일 이름, 칵테일 문서번호, 설명, 만드는방법, 칵테일 만든이, 이미지url
                            Recipe_name[count] = (String) document.get("Recipe_name");
                            ID[count] = Integer.parseInt(document.getId());
                            method[count] = (String) document.get("method");

                            Recipe_Ingredient = (Map<String, Number>) document.get("Ingredient_content");

                            //map으로 받아온 정보를 string으로 치환한뒤 유저에게 보여줄 수 있도록 replaceall함({, }, = 삭제 ml 추가)
                            Recipe_Base[count] = String.valueOf(Recipe_Ingredient);
                            Recipe_Base[count] = Recipe_Base[count].replaceAll("\\,", "ml ");
                            Recipe_Base[count] = Recipe_Base[count].replaceAll("\\{", " ");
                            Recipe_Base[count] = Recipe_Base[count].replaceAll("\\}", "ml ");
                            Recipe_Base[count] = Recipe_Base[count].replaceAll("\\=", " ");
                            //long형태로 받은 abv를 유저에게 보여줄 수 있도록 %를 붙여 재저장
                            Realabv[count] = (long) document.get("abv");
                            abv[count] = Realabv[count] + "%";
                            ref[count] = (String) document.get("ref");
                            adapterForCocktailSearch.addItem(new Cocktail(Recipe_name[count], ID[count], method[count], Recipe_Base[count], abv[count],ref[count]));
                            count++;
                            //refresh 해주는 함수(아마)
                            if (_str.length()==0){
                                recyclerViewForCocktailSearch.setAdapter(adapterForCocktailSearch);
                            }
                            else{
                                adapterForCocktailSearch.filterForCocktail(_str);
                                recyclerViewForCocktailSearch.setAdapter(adapterForCocktailSearch);
                            }
                        }

                    } else {
                        System.out.println("오류 발생 컬렉션에서 정상적으로 불러와지지 않음.");
                    }

                }
            });
        }
        else
        {
            System.out.println("플래그 오류 발생 컬렉션에서 정상적으로 불러와지지 않음.");
            System.out.println("플래그 오류 상태 : "+ Recipe_case);
        }
    }
    //사용자레시피
    private void setAdapterForSelfCocktailSearchMethod(String str){
        final String _str = str;
        adapterForCocktailSearch.clearAllForAdapter();
        //사용자들의 레시피 정보를 받기위한 변수 초기화
        Self_name = new ArrayList();
        Self_id = new ArrayList();
        Self_description = new ArrayList();
        Self_base = new ArrayList();
        Self_user = new ArrayList();
        Self_url = new ArrayList();
        if(Self_case == 0)
        {
            db.collection("Self").orderBy("mark_number", Query.Direction.DESCENDING).orderBy("칵테일 이름", Query.Direction.ASCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    //칵테일 이름, 칵테일 문서번호, 설명, 만드는방법, 칵테일 만든이, 이미지url
                                    if(document.get("칵테일 만든 유저 id").equals("0"))
                                        System.out.println("얘는 안받아요");
                                    else{
                                        Self_name.add(document.get("칵테일 이름").toString());
                                        Self_id.add(document.getId().toString());
                                        Self_description.add(document.get("칵테일 설명").toString());
                                        Self_base.add(document.get("만드는 방법").toString());
                                        Self_user.add(document.get("칵테일 만든이").toString());
                                        Self_url.add(document.get("ref").toString());
                                    }
                                }
                                recyclerViewForCocktailSearch.setAdapter(adapterForCocktailSearch);
                                for(int i=0;i<Self_name.size();i++){
                                    //칵테일 이름, 칵테일 문서번호, 설명, 만드는방법, 칵테일 만든이, 이미지url
                                    adapterForCocktailSearch.addItem(new Cocktail((String) Self_name.get(i), Integer.parseInt(String.valueOf(Self_id.get(i))), (String) Self_description.get(i), (String) Self_base.get(i), (String) Self_user.get(i),
                                            (String) Self_url.get(i)));
                                }
                                //refresh 해주는 함수(아마)
                                if (_str.length()==0){
                                    recyclerViewForCocktailSearch.setAdapter(adapterForCocktailSearch);
                                }
                                else{
                                    adapterForCocktailSearch.filterForCocktail(_str);
                                    recyclerViewForCocktailSearch.setAdapter(adapterForCocktailSearch);
                                }
                            } else {
                                System.out.println("오류 발생 컬렉션에서 정상적으로 불러와지지 않음.");
                            }

                        }
                    });
        }
        else if(Self_case == 1)
        {
            db.collection("Self").orderBy("mark_number", Query.Direction.ASCENDING).orderBy("칵테일 이름", Query.Direction.ASCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    //칵테일 이름, 칵테일 문서번호, 설명, 만드는방법, 칵테일 만든이, 이미지url
                                    if(document.get("칵테일 만든 유저 id").equals("0"))
                                        System.out.println("얘는 안받아요");
                                    else{
                                        Self_name.add(document.get("칵테일 이름").toString());
                                        Self_id.add(document.getId().toString());
                                        Self_description.add(document.get("칵테일 설명").toString());
                                        Self_base.add(document.get("만드는 방법").toString());
                                        Self_user.add(document.get("칵테일 만든이").toString());
                                        Self_url.add(document.get("ref").toString());
                                    }
                                }
                                recyclerViewForCocktailSearch.setAdapter(adapterForCocktailSearch);
                                for(int i=0;i<Self_name.size();i++){
                                    //칵테일 이름, 칵테일 문서번호, 설명, 만드는방법, 칵테일 만든이, 이미지url
                                    adapterForCocktailSearch.addItem(new Cocktail((String) Self_name.get(i), Integer.parseInt(String.valueOf(Self_id.get(i))), (String) Self_description.get(i), (String) Self_base.get(i), (String) Self_user.get(i),
                                            (String) Self_url.get(i)));
                                }
                                //refresh 해주는 함수(아마)
                                if (_str.length()==0){
                                    recyclerViewForCocktailSearch.setAdapter(adapterForCocktailSearch);
                                }
                                else{
                                    adapterForCocktailSearch.filterForCocktail(_str);
                                    recyclerViewForCocktailSearch.setAdapter(adapterForCocktailSearch);
                                }
                            } else {
                                System.out.println("오류 발생  컬렉션에서 정상적으로 불러와지지 않음.");
                            }

                        }
                    });
        }
        else if(Self_case == 2)
        {
            db.collection("Self").orderBy("good_number", Query.Direction.DESCENDING).orderBy("칵테일 이름", Query.Direction.ASCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    //칵테일 이름, 칵테일 문서번호, 설명, 만드는방법, 칵테일 만든이, 이미지url
                                    if(document.get("칵테일 만든 유저 id").equals("0"))
                                        System.out.println("얘는 안받아요");
                                    else{
                                        Self_name.add(document.get("칵테일 이름").toString());
                                        Self_id.add(document.getId().toString());
                                        Self_description.add(document.get("칵테일 설명").toString());
                                        Self_base.add(document.get("만드는 방법").toString());
                                        Self_user.add(document.get("칵테일 만든이").toString());
                                        Self_url.add(document.get("ref").toString());
                                    }
                                }
                                recyclerViewForCocktailSearch.setAdapter(adapterForCocktailSearch);
                                for(int i=0;i<Self_name.size();i++){
                                    //칵테일 이름, 칵테일 문서번호, 설명, 만드는방법, 칵테일 만든이, 이미지url
                                    adapterForCocktailSearch.addItem(new Cocktail((String) Self_name.get(i), Integer.parseInt(String.valueOf(Self_id.get(i))), (String) Self_description.get(i), (String) Self_base.get(i), (String) Self_user.get(i),
                                            (String) Self_url.get(i)));
                                }
                                //refresh 해주는 함수(아마)
                                if (_str.length()==0){
                                    recyclerViewForCocktailSearch.setAdapter(adapterForCocktailSearch);
                                }
                                else{
                                    adapterForCocktailSearch.filterForCocktail(_str);
                                    recyclerViewForCocktailSearch.setAdapter(adapterForCocktailSearch);
                                }
                            } else {
                                System.out.println("오류 발생 컬렉션에서 정상적으로 불러와지지 않음.");
                            }

                        }
                    });
        }
        else if(Self_case == 3)
        {
            db.collection("Self").orderBy("good_number", Query.Direction.ASCENDING).orderBy("칵테일 이름", Query.Direction.ASCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    //칵테일 이름, 칵테일 문서번호, 설명, 만드는방법, 칵테일 만든이, 이미지url
                                    if(document.get("칵테일 만든 유저 id").equals("0"))
                                        System.out.println("얘는 안받아요");
                                    else{
                                        Self_name.add(document.get("칵테일 이름").toString());
                                        Self_id.add(document.getId().toString());
                                        Self_description.add(document.get("칵테일 설명").toString());
                                        Self_base.add(document.get("만드는 방법").toString());
                                        Self_user.add(document.get("칵테일 만든이").toString());
                                        Self_url.add(document.get("ref").toString());
                                    }
                                }
                                recyclerViewForCocktailSearch.setAdapter(adapterForCocktailSearch);
                                for(int i=0;i<Self_name.size();i++){
                                    //칵테일 이름, 칵테일 문서번호, 설명, 만드는방법, 칵테일 만든이, 이미지url
                                    adapterForCocktailSearch.addItem(new Cocktail((String) Self_name.get(i), Integer.parseInt(String.valueOf(Self_id.get(i))), (String) Self_description.get(i), (String) Self_base.get(i), (String) Self_user.get(i),
                                            (String) Self_url.get(i)));
                                }
                                //refresh 해주는 함수(아마)
                                if (_str.length()==0){
                                    recyclerViewForCocktailSearch.setAdapter(adapterForCocktailSearch);
                                }
                                else{
                                    adapterForCocktailSearch.filterForCocktail(_str);
                                    recyclerViewForCocktailSearch.setAdapter(adapterForCocktailSearch);
                                }
                            } else {
                                System.out.println("오류 발생  컬렉션에서 정상적으로 불러와지지 않음.");
                            }

                        }
                    });
        }
        else
        {
            System.out.println("플래그 오류 발생 컬렉션에서 정상적으로 불러와지지 않음.");
            System.out.println("플래그 오류 상태 : "+ Self_case);
        }

    }

    private void setAdapterForIngredientSearch(){
        count = 0;
        adapterForCocktailSearch.clearAllForAdapter();
        CollectionReference IngredientRef = db.collection("Ingredient");

        IngredientRef.orderBy("Ingredient_name", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Ingredient_name[count] = (String) document.get("Ingredient_name"); //재료 이름
                        System.out.println("array_count : "+ count + "   Ingredient_name data: " +Ingredient_name[count]);
                        Ingredient_ID[count] = Integer.parseInt(document.getId());
                        Ingredient_flavour[count] = (String) document.get("flavour"); //향(칵테일에선 설명 method)

                        Ingredient_sugar[count] = (Number) document.get("sugar_rate"); //suger_rate(칵테일에선 재료와 용량)
                        Ingredient_Realsugar[count] = Ingredient_sugar[count] + "%";

                        //long형태로 받은 abv를 유저에게 보여줄 수 있도록 %를 붙여 재저장
                        Ingredient_Realabv[count] = (long) document.get("abv");
                        Ingredient_abv[count] = Ingredient_Realabv[count] + "%";
                        Ingredient_ref[count] = (String) document.get("ref");
                        adapterForCocktailSearch.addItem(new Cocktail(Ingredient_name[count], Ingredient_ID[count], Ingredient_flavour[count], Ingredient_Realsugar[count], Ingredient_abv[count],Ingredient_ref[count]));
                        //refresh 해주는 함수(아마)
                        recyclerViewForCocktailSearch.setAdapter(adapterForCocktailSearch);
                        count++;
                    }
                } else {
                    System.out.println("오류 발생 컬렉션에서 정상적으로 불러와지지 않음.");
                }

            }
        });

    }
}
