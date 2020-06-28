package org.techtown.capstoneprojectcocktail;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MyPageMyRecipeActivity extends AppCompatActivity {

    private CocktailAdapterForSearch adapterForCocktailMyRecipe = new CocktailAdapterForSearch();
    private RecyclerView recyclerViewForCocktailMyRecipe;
    //db Self 컬렉션의 데이터를 읽어와 저장할 리스트 선언
    //칵테일 이름, 칵테일 문서번호, 설명, 만드는방법, 칵테일 만든이, 이미지url
    private ArrayList Self_name;
    private ArrayList Self_id;
    private ArrayList Self_description;
    private ArrayList Self_base;
    private ArrayList Self_user;
    private ArrayList Self_url;

    FirebaseAuth mAuth  = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_myrecipe_activity);

        recyclerViewForCocktailMyRecipe = findViewById(R.id.recyclerViewForCocktail_myPage_myRecipe);
        LinearLayoutManager layoutManagerForCocktailMyRecipe = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        layoutManagerForCocktailMyRecipe.setReverseLayout(true);
        layoutManagerForCocktailMyRecipe.setStackFromEnd(true);


        recyclerViewForCocktailMyRecipe.setLayoutManager(layoutManagerForCocktailMyRecipe);
        recyclerViewForCocktailMyRecipe.setAdapter(adapterForCocktailMyRecipe);

        //리사이클러뷰를 클릭했을 경우
        adapterForCocktailMyRecipe.setOnItemClickListener(new OnCocktailItemClickListenerForSearch() {
            @Override
            public void onItemClick(CocktailAdapterForSearch.ViewHolder holder, final View view, int position) {
                final Cocktail cocktail = adapterForCocktailMyRecipe.getItem(position);

                PopupMenu popup= new PopupMenu(getApplicationContext(), view);
                getMenuInflater().inflate(R.menu.popup_menu_my_recipe, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.popup_myRecipe_delete:
                                DocumentReference update_S_ref = db.collection("Self").document(String.valueOf(cocktail.id));
                                update_S_ref
                                        .update("칵테일 만든 유저 id", "0")
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                //받아오기위해 변수들 초기화
                                                Self_name = new ArrayList();
                                                Self_id = new ArrayList();
                                                Self_description = new ArrayList();
                                                Self_base = new ArrayList();
                                                Self_user = new ArrayList();
                                                Self_url = new ArrayList();
                                                adapterForCocktailMyRecipe.clearAllForAdapter();
                                                String B_name  = currentUser.getUid() + String.valueOf(cocktail.id);

                                                DocumentReference update_B_ref = db.collection("Bookmark").document(B_name);
                                                update_B_ref.update("사용자 uid", "0").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                    }
                                                });
                                                DocumentReference update_G_ref = db.collection("Grading").document(B_name);
                                                update_G_ref.update("사용자 uid", "0").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                    }
                                                });
                                                db.collection("Comment")
                                                        .whereEqualTo("레시피 번호", String.valueOf(cocktail.id))
                                                        .get()
                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                if (task.isSuccessful()) {
                                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                                        DocumentReference update_C_ref = db.collection("Comment").document(document.getId());
                                                                        update_C_ref.update("사용자 uid", "0").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {
                                                                            }
                                                                        }).addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                            }
                                                                        });
                                                                    }
                                                                    recyclerViewForCocktailMyRecipe.setAdapter(adapterForCocktailMyRecipe);
                                                                } else {
                                                                }

                                                            }
                                                        });

                                                db.collection("Self")
                                                        .whereEqualTo("칵테일 만든 유저 id", currentUser.getUid())
                                                        .get()
                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                if (task.isSuccessful()) {
                                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                                        //칵테일 이름, 칵테일 문서번호, 설명, 만드는방법, 칵테일 만든이, 이미지url
                                                                        Self_name.add(document.get("칵테일 이름").toString());
                                                                        Self_id.add(document.getId().toString());
                                                                        Self_description.add(document.get("칵테일 설명").toString());
                                                                        Self_base.add(document.get("만드는 방법").toString());
                                                                        Self_user.add(document.get("칵테일 만든이").toString());
                                                                        Self_url.add(document.get("ref").toString());
                                                                    }
                                                                    recyclerViewForCocktailMyRecipe.setAdapter(adapterForCocktailMyRecipe);
                                                                    for(int i=0;i<Self_name.size();i++){
                                                                        //칵테일 이름, 칵테일 문서번호, 설명, 만드는방법, 칵테일 만든이, 이미지url
                                                                        adapterForCocktailMyRecipe.addItem(new Cocktail((String) Self_name.get(i), Integer.parseInt(String.valueOf(Self_id.get(i))), (String) Self_description.get(i), (String) Self_base.get(i), (String) Self_user.get(i),
                                                                                (String) Self_url.get(i)));
                                                                    }
                                                                } else {
                                                                    System.out.println("오류 발생 Grading 컬렉션에서 정상적으로 불러와지지 않음.");
                                                                }

                                                            }
                                                        });
                                                Toast.makeText(getApplication(),"게시물 삭제 성공",Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getApplication(),"게시물 삭제 실패",Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                recyclerViewForCocktailMyRecipe.setAdapter(adapterForCocktailMyRecipe);
                                break;
                            case R.id.popup_myRecipe_recipe:
                                Intent intent = new Intent(view.getContext(), CocktailRecipeActivity.class);
                                intent.putExtra("cocktailName", cocktail.getName());
                                intent.putExtra("cocktailID",cocktail.getId());
                                intent.putExtra("cocktailDescription",cocktail.getDescription());
                                intent.putExtra("cocktailIngredient",cocktail.getIngredient());
                                intent.putExtra("cocktailABV",cocktail.getAbvNum());
                                intent.putExtra("cocktailRef",cocktail.getImageUrl());
                                startActivity(intent);
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapterForCocktailMyRecipe.clearAllForAdapter();

        //받아오기위해 변수들 초기화
        Self_name = new ArrayList();
        Self_id = new ArrayList();
        Self_description = new ArrayList();
        Self_base = new ArrayList();
        Self_user = new ArrayList();
        Self_url = new ArrayList();

        db.collection("Self")
                .whereEqualTo("칵테일 만든 유저 id", currentUser.getUid()).orderBy("칵테일 이름", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //칵테일 이름, 칵테일 문서번호, 설명, 만드는방법, 칵테일 만든이, 이미지url
                                Self_name.add(document.get("칵테일 이름").toString());
                                Self_id.add(document.getId().toString());
                                Self_description.add(document.get("칵테일 설명").toString());
                                Self_base.add(document.get("만드는 방법").toString());
                                Self_user.add(document.get("칵테일 만든이").toString());
                                Self_url.add(document.get("ref").toString());
                            }
                            recyclerViewForCocktailMyRecipe.setAdapter(adapterForCocktailMyRecipe);
                            for(int i=0;i<Self_name.size();i++){
                                //칵테일 이름, 칵테일 문서번호, 설명, 만드는방법, 칵테일 만든이, 이미지url
                                adapterForCocktailMyRecipe.addItem(new Cocktail((String) Self_name.get(i), Integer.parseInt(String.valueOf(Self_id.get(i))), (String) Self_description.get(i), (String) Self_base.get(i), (String) Self_user.get(i),
                                        (String) Self_url.get(i)));
                            }
                        } else {
                            System.out.println("오류 발생 컬렉션에서 정상적으로 불러와지지 않음.");
                        }

                    }
                });

    }
}
