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

public class MyPageCommentActivity extends AppCompatActivity {

    private CocktailCommentAdapter adapterForCocktailComment = new CocktailCommentAdapter();
    private RecyclerView recyclerViewForCocktailComment;


    //db 북마크 컬렉션의 데이터를 읽어와 저장할 리스트 선언
    Map<String, String> Comment_RecipeID;         //각 문서 이름
    Map<String, String> Comment_RecipeName;       //레시피 이름
    Map<String, String>  Comment_RecipeRef;        //레시피 ref
    ArrayList<String> Comment_date;
    Map<String, String> Comment_contents;
    Map<String, String> Comment_url;
    Map<String, String> Comment_uid;
    Map<String, String> Comment_Name;

    //db 레시피들 컬렉션의 데이터를 읽어와 저장할 리스트 선언
    Map<String, String> method =  new HashMap<>();      //self이면 설명
    Map<String, String> Recipe_Base = new HashMap<>();  //self이면 만드는 방법
    Map<String, String> abv = new HashMap<>();          //self이면 테일 만든이


    int count = 0;

    FirebaseAuth mAuth  = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_comment_activity);

        recyclerViewForCocktailComment = findViewById(R.id.recyclerViewForComment_myPage_comment);
        LinearLayoutManager layoutManagerForCocktailComment = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        layoutManagerForCocktailComment.setReverseLayout(true);
        layoutManagerForCocktailComment.setStackFromEnd(true);
        recyclerViewForCocktailComment.setLayoutManager(layoutManagerForCocktailComment);
        recyclerViewForCocktailComment.setAdapter(adapterForCocktailComment);


        //리사이클러뷰를 클릭했을 경우
        adapterForCocktailComment.setOnItemClickListener(new OnCocktailCommentItemClickListener() {
            @Override
            public void onItemClick(CocktailCommentAdapter.ViewHolder holder, View view, int position) {
                Comment item = adapterForCocktailComment.getItem(position);
                Intent intent = new Intent(view.getContext(), CocktailRecipeActivity.class);
                intent.putExtra("cocktailName", item.getCocktailName());
                intent.putExtra("cocktailID",item.getCocktailID());
                intent.putExtra("cocktailDescription",item.getCocktailDescription());
                intent.putExtra("cocktailIngredient",item.getCocktailIngredient());
                intent.putExtra("cocktailABV",item.getCocktailAbvNum());
                intent.putExtra("cocktailRef",item.getCocktailImageUri());
                startActivity(intent);
                System.out.println(item.getName());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapterForCocktailComment.clearAllForAdapter();

        //내용물 초기화
        this.Comment_date = new ArrayList();
        this.Comment_RecipeID = new HashMap<>();
        this.Comment_RecipeName = new HashMap<>();
        this.Comment_RecipeRef = new HashMap<>();
        this.Comment_contents = new HashMap<>();
        this.Comment_url = new HashMap<>();
        this.Comment_uid = new HashMap<>();
        this.Comment_Name = new HashMap<>();
        this.method = new HashMap<>();      //self이면 설명
        this.Recipe_Base = new HashMap<>();  //self이면 만드는 방법
        this.abv = new HashMap<>();          //self이면 칵테일 만든이

        db.collection("Comment")
                .whereEqualTo("사용자 uid", currentUser.getUid()).orderBy("댓글 날짜", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String id = document.get("댓글 날짜").toString();
                                Comment_date.add(document.get("댓글 날짜").toString());       //레시피 이름

                                Comment_RecipeID.put(id, document.get("레시피 번호").toString());
                                Comment_RecipeName.put(id, document.get("레시피 이름").toString());       //레시피 이름
                                Comment_RecipeRef.put(id, document.get("레시피 ref").toString());        //레시피 ref
                                Comment_Name.put(id, document.get("사용자 이름").toString());        //레시피 ref
                                Comment_contents.put(id, document.get("내용").toString());        //레시피 ref
                                Comment_url.put(id, document.get("사용자 url").toString());       //레시피 이름
                                Comment_uid.put(id, document.get("사용자 uid").toString());        //레시피 ref

                                if (Integer.parseInt(String.valueOf(document.get("레시피 번호").toString())) < 6000) {
                                    SetIngredient(document.get("레시피 번호").toString(), id);
                                } else if (Integer.parseInt(String.valueOf(document.get("레시피 번호").toString())) < 7000){
                                    SetRecipe(document.get("레시피 번호").toString(), id);
                                }
                                else
                                {
                                    SetSelfRecipe(document.get("레시피 번호").toString(), id);
                                }
                            }
                        }
                    }
                });
    }


    public void SetIngredient(final String id, final String str){
        DocumentReference docRef = db.collection("Ingredient").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // parsing Recipe_Base
                        String buf;
                        buf = String.valueOf(document.get("flavour"));
                        method.put(str, buf);
                        Recipe_Base.put(str, (document.get("sugar_rate").toString()) + "%");
                        abv.put(str,  (document.get("abv").toString()) + "%");
                        RenderCocktailComment();
                    } else {
                        System.out.println("오류 발생 해당 컬렉션에 문서가 존재하지 않음.");
                    }
                } else {
                    System.out.println("오류 발생 레시피 컬렉션에서 정상적으로 불러와지지 않음.");
                }
            }
        });
    }
    public void SetRecipe(final String id, final String str){
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

                        method.put(str, buf);
                        Recipe_Base.put(str, (document.get("method").toString()));
                        abv.put(str,  (document.get("abv").toString()) + "%");
                        RenderCocktailComment();
                    } else {
                        System.out.println("오류 발생 해당 컬렉션에 문서가 존재하지 않음.");
                    }
                } else {
                    System.out.println("오류 발생 레시피 컬렉션에서 정상적으로 불러와지지 않음.");
                }
            }
        });
    }

    public void SetSelfRecipe(final String id, final String str) {
        DocumentReference docRef = db.collection("Self").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Recipe_Base.put(str, String.valueOf(document.get("만드는 방법")));
                        method.put(str, (document.get("칵테일 설명").toString()));
                        abv.put(str,  (document.get("칵테일 만든이").toString()));
                        RenderCocktailComment();
                    } else {
                        System.out.println("오류 발생 해당 컬렉션에 문서가 존재하지 않음.");
                    }
                } else {
                    System.out.println("오류 발생 셀프 컬렉션에서 정상적으로 불러와지지 않음.");
                }
            }
        });
    }


    private void RenderCocktailComment() {
        if(this.abv.size() ==  this.Comment_date.size()) {
            for(String id : this.Comment_date)
            {
                recyclerViewForCocktailComment.setAdapter(adapterForCocktailComment);

                adapterForCocktailComment.addItem(
                        new Comment(
                                (String) "\""+Comment_RecipeName.get(id)+"\" 에 달았던 댓글",
                                (String) id,
                                (String) Comment_contents.get(id),
                                (String) Comment_url.get(id),
                                (String) Comment_uid.get(id),
                                Comment_RecipeName.get(id),
                                Integer.parseInt(Comment_RecipeID.get(id)),
                                this.Recipe_Base.get(id),
                                this.method.get(id),
                                this.abv.get(id),
                                this.Comment_RecipeRef.get(id)));
            }
        }
    }

}
//마이 코멘트 액티비티