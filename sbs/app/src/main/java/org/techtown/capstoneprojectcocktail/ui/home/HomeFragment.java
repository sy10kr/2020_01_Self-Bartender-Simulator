package org.techtown.capstoneprojectcocktail.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.techtown.capstoneprojectcocktail.Cocktail;
import org.techtown.capstoneprojectcocktail.CocktailAdapterForHome;
import org.techtown.capstoneprojectcocktail.CocktailIngredientButton;
import org.techtown.capstoneprojectcocktail.CocktailIngredientButtonAdapter;
import org.techtown.capstoneprojectcocktail.CocktailRecipeActivity;
import org.techtown.capstoneprojectcocktail.CocktailSearchActivity;
import org.techtown.capstoneprojectcocktail.MJH_CustomView;
import org.techtown.capstoneprojectcocktail.MJH_ListviewItem;
import org.techtown.capstoneprojectcocktail.MJH_SimulatorUiActivity;
import org.techtown.capstoneprojectcocktail.OnCocktailIngredientButtonItemClickListener;
import org.techtown.capstoneprojectcocktail.OnCocktailItemClickListenerForHome;
import org.techtown.capstoneprojectcocktail.R;
import org.techtown.capstoneprojectcocktail.TestForCosine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static java.lang.Integer.parseInt;
import static java.util.Date.parse;
import static org.techtown.capstoneprojectcocktail.MJH_SimulatorUiActivity.usingStepNum;

public class HomeFragment extends Fragment {
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    String[] Recipe_name = new String[20];
    int[] ID = new int[20];
    String[] method = new String[20];
    String[] Recipe_Base = new String[20];
    String[] abv = new String[20];
    String[] ref = new String[20];
    long[] Realabv = new long[20];
    int count;
    Map<String, Number> Recipe_Ingredient;
    //레시피에 들어가는 재료들을 {}형식의 스트링 형태로 저장
    String[] Recipe_list = new String[81];
    //레시피에 들어가는 재료의 갯수 및 각 5미의 총합을 int형으로 저장(계산 편의를 위하여)
    int[] Recipe_count = new int[81];
    int[] Sum_sugar = new int[81];
    int[] Sum_bitter = new int[81];
    int[] Sum_sour = new int[81];
    int[] Sum_salty = new int[81];
    int[] Sum_hot = new int[81];
    //레시피에 들어가는 재료 이름, 양을 스트링, 넘버 형태로 저장
    String[][] Recipe_namelist = new String[81][10];
    Number[][] Recipe_volumelist = new Number[81][10];
    //레시피에 들어간 각 재료들의 5미를 스트링 형태로 저장, 한 레시피당 최대 재료갯수를 10으로 임의지정
    String[][] Sugar_Value = new String[81][10];
    String[][] Bitter_Value = new String[81][10];
    String[][] Sour_Value = new String[81][10];
    String[][] Salty_Value = new String[81][10];
    String[][] Hot_Value = new String[81][10];


    public void setDocument() {

    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        Button searchButtonHome = root.findViewById(R.id.button_search_home);
        //서치 빈칸
        searchButtonHome.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(view.getContext(), CocktailSearchActivity.class);
                intent.putExtra("ingredientName", "");
                startActivity(intent);
            }
        });

        FloatingActionButton simulationFloatingButtonHome = root.findViewById(R.id.button_simulation_action);
        simulationFloatingButtonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "시뮬레이션 기능이 들어갈 예정입니다.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                //////////////// By MJH
                Intent intent = new Intent(view.getContext(), // 현재 화면의 제어권자
                        MJH_SimulatorUiActivity.class); // 다음 넘어갈 클래스 지정
                startActivity(intent); // 다음 화면으로 넘어간다
                //////////////// By MJH
            }
        });

        RecyclerView recyclerViewForIngredientButton = root.findViewById(R.id.recyclerViewForIngredient);
        LinearLayoutManager layoutManagerForIngredientButton = new LinearLayoutManager(root.getContext(), LinearLayoutManager.HORIZONTAL,false);
        recyclerViewForIngredientButton.setLayoutManager(layoutManagerForIngredientButton);
        final CocktailIngredientButtonAdapter adapterForIngredientButton = new CocktailIngredientButtonAdapter();

        //영진 여기 확인
        List<String> main_keyword = new ArrayList<>(Arrays.asList("데킬라", "럼", "리큐르", "베르무트", "보드카", "브랜디", "와인", "위스키", "주스", "진"));
        Collections.shuffle(main_keyword);
        for(int i = 0; i < main_keyword.size() ; i ++) {
            adapterForIngredientButton.addItem(new CocktailIngredientButton(main_keyword.get(i)));
        }

        recyclerViewForIngredientButton.setAdapter(adapterForIngredientButton);
        adapterForIngredientButton.setOnItemClickListener(new OnCocktailIngredientButtonItemClickListener() {
            @Override
            public void onItemClick(CocktailIngredientButtonAdapter.ViewHolder holder, View view, int position) {
                CocktailIngredientButton item = adapterForIngredientButton.getItem(position);

                Intent intent = new Intent(view.getContext(), CocktailSearchActivity.class);
                intent.putExtra("ingredientName", item.getIngredientCategorizedName());
                startActivity(intent);
            }
        });

        final RecyclerView recyclerViewForCocktailHome = root.findViewById(R.id.recyclerViewForCocktail_home);
        LinearLayoutManager layoutManagerForCocktailHome = new LinearLayoutManager(root.getContext(), LinearLayoutManager.HORIZONTAL,false);
        recyclerViewForCocktailHome.setLayoutManager(layoutManagerForCocktailHome);
        final CocktailAdapterForHome adapterForCocktailHome = new CocktailAdapterForHome();

        //2020 06 08 10:04 수정
        //오류나면 아래 라인 수정
        recyclerViewForCocktailHome.setAdapter(adapterForCocktailHome);

        //수정필 테스트용
        //영진 여기 확인
        for(int i=0; i < 20; i++)
        {
            List<String> list;
            count = i;
            DocumentReference docRef = db.collection("Recipe").document(String.valueOf(i+6001));

            final int finalI = i;
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    count = finalI;
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
                    Realabv[count] = (long) document.get("abv");
                    abv[count] = Realabv[count] + "%";
                    ref[count] = (String) document.get("ref");
                    adapterForCocktailHome.addItem(new Cocktail(Recipe_name[count], ID[count], method[count], Recipe_Base[count], abv[count],ref[count]));
                    //refresh 해주는 함수(아마)
                    recyclerViewForCocktailHome.setAdapter(adapterForCocktailHome);
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        }
        });
        }
        adapterForCocktailHome.setOnItemClickListener(new OnCocktailItemClickListenerForHome() {
            @Override
            public void onItemClick(CocktailAdapterForHome.ViewHolder holder, View view, int position) {
                Cocktail item = adapterForCocktailHome.getItem(position);
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

        //테스트용 삭제필
        /*
        Button testButton = root.findViewById(R.id.buttonForTestCosine_home);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent intent = new Intent(view.getContext(), MJH_CustomView.class);
                    startActivity(intent);
                }catch(Exception e){
                    System.out.println("err" + e.toString());
                }
            }
        });
         */
        return root;
    }
}
