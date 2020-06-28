package org.techtown.capstoneprojectcocktail;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyPageGradingActivity extends AppCompatActivity {

    private CocktailGradingAdapter adapterForCocktailGrading = new CocktailGradingAdapter();
    private RecyclerView recyclerViewForCocktailGrading;

    //db 북마크 컬렉션의 데이터를 읽어와 저장할 리스트 선언
    ArrayList<String> Grading_id;         //각 문서 이름
    Map<String, String> Grading_name;       //레시피 이름
    Map<String, String>  Grading_ref;        //레시피 ref
    Map<String, String> Grading_uid;       //레시피 이름
    Map<String, String>  Grading_score;


    //db 레시피들 컬렉션의 데이터를 읽어와 저장할 리스트 선언
    Map<String, String> method =  new HashMap<>();      //self이면 설명
    Map<String, String> Recipe_Base = new HashMap<>();  //self이면 만드는 방법
    Map<String, String> abv = new HashMap<>();          //self이면 테일 만든이

    FirebaseAuth mAuth  = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_grading_activity);

        recyclerViewForCocktailGrading = findViewById(R.id.recyclerViewForCocktail_myPage_grading);
        LinearLayoutManager layoutManagerForCocktailGrading = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        layoutManagerForCocktailGrading.setReverseLayout(true);
        layoutManagerForCocktailGrading.setStackFromEnd(true);
        recyclerViewForCocktailGrading.setLayoutManager(layoutManagerForCocktailGrading);
        recyclerViewForCocktailGrading.setAdapter(adapterForCocktailGrading);

        //리사이클러뷰를 클릭했을 경우
        adapterForCocktailGrading.setOnItemClickListener(new OnCocktailGradingClickListener() {
            @Override
            public void onItemClick(CocktailGradingAdapter.ViewHolder holder, View view, int position) {
                Cocktail item = adapterForCocktailGrading.getItem(position);
                System.out.println(item.getId());
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

    @Override
    protected void onResume() {
        super.onResume();

        adapterForCocktailGrading.clearAllForAdapter();
        //받아오기위해 변수들 초기화 = new ArrayList();
        this.Grading_id = new ArrayList();
        this.Grading_name = new HashMap<>();        //레시피 이름
        this.Grading_ref = new HashMap<>();         //레시피 ref
        this.Grading_uid =  new HashMap<>();
        this.Recipe_Base = new HashMap<>();
        this.Grading_score = new HashMap<>();
        this.method =  new HashMap<>();      //self이면 설명
        this.Recipe_Base = new HashMap<>();  //self이면 만드는 방법
        this.abv = new HashMap<>();          //self이면 테일 만든이

        //북마크 컬렉션에서 사용자 uid와 같은 문서들을 전부 불러온다.
        db.collection("Grading")
                .whereEqualTo("사용자 uid", currentUser.getUid()).orderBy("평가 날짜", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String id = document.get("레시피 번호").toString();
                                Grading_id.add(id);

                                Grading_name.put(id, document.get("레시피 이름").toString());       //레시피 이름
                                Grading_ref.put(id, document.get("레시피 ref").toString());        //레시피 ref
                                Grading_uid.put(id, document.get("사용자 uid").toString());        //사용자 uid
                                Grading_score.put(id, document.get("점수").toString());
                                if( Integer.parseInt(String.valueOf(document.get("레시피 번호").toString())) < 10000) {
                                    SetRecipe(document.get("레시피 번호").toString());
                                }
                                else
                                {
                                    SetSelfRecipe(document.get("레시피 번호").toString());
                                }
                            }
                            //Set_first();
                        } else {
                            System.out.println("오류 발생 Grading 컬렉션에서 정상적으로 불러와지지 않음.");
                        }

                    }
                });
    }
    public void SetRecipe(final String id){
        DocumentReference docRef = db.collection("Recipe").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // parsing Recipe_Base
                        String buf;
                        buf = String.valueOf(document.get("Ingredient_content"));
                        buf = buf.replaceAll("\\,", "ml ");
                        buf = buf.replaceAll("\\{", " ");
                        buf = buf.replaceAll("\\}", "ml ");
                        buf = buf.replaceAll("\\=", " ");

                        method.put(id, buf);
                        Recipe_Base.put(id, (document.get("method").toString()));
                        abv.put(id,  (document.get("abv").toString()) + "%");
                        RenderCocktailBookmark();
                    } else {
                        System.out.println("오류 발생 해당 컬렉션에 문서가 존재하지 않음.");
                    }
                } else {
                    System.out.println("오류 발생 레시피 컬렉션에서 정상적으로 불러와지지 않음.");
                }
            }
        });
    }

    public void SetSelfRecipe(final String id) {
        DocumentReference docRef = db.collection("Self").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Recipe_Base.put(id, String.valueOf(document.get("만드는 방법")));
                        method.put(id, (document.get("칵테일 설명").toString()));
                        abv.put(id,  (document.get("칵테일 만든이").toString()));
                        RenderCocktailBookmark();
                    } else {
                        System.out.println("오류 발생 해당 컬렉션에 문서가 존재하지 않음.");
                    }
                } else {
                    System.out.println("오류 발생 셀프 컬렉션에서 정상적으로 불러와지지 않음.");
                }
            }
        });
    }


    private void RenderCocktailBookmark() {
        if(this.method.size() ==  this.Grading_id.size()) {
            for(String id : this.Grading_id)
            {
                recyclerViewForCocktailGrading.setAdapter(adapterForCocktailGrading);

                adapterForCocktailGrading.addItem(
                        new Cocktail(
                                Grading_name.get(id),
                                Integer.parseInt(id),
                                Recipe_Base.get(id),
                                method.get(id),
                                abv.get(id),
                                Grading_ref.get(id),
                                Grading_score.get(id)));
            }


        }
    }
}

