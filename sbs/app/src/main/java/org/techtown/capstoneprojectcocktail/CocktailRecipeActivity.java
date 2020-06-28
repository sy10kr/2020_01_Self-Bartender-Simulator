package org.techtown.capstoneprojectcocktail;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CocktailRecipeActivity extends AppCompatActivity {
    private static final String TAG = "CocktailRecipeActivity";
    CocktailCommentAdapter adapterForCocktailComment = new CocktailCommentAdapter();
    private FloatingActionButton floatingActionButtonForBookmark;
    private FloatingActionButton floatingActionButtonForGrade;
    private FloatingActionButton floatingActionButtonForReport;
    private FloatingActionButton floatingActionButtonForAnimation;
    private TextInputLayout textInputLayoutForComment;
    private RatingBar ratingBar;
    private int cocktailID;
    private String cocktailName;
    private String cocktailRef;
    private String stringForCocktailComment;
    private FirebaseAuth mAuth;
    private ImageButton imageButtonForComment;
    private RecyclerView recyclerViewForComment;
    private LinearLayoutManager layoutManagerForCocktailComment;
    //이전에 북마크,평가,신고를 체크 했는지 안했는지 판단
    private boolean bookmarkChecked=false;
    private boolean gradeChecked=false;
    private boolean reportChecked=false;
    private String gradeScore;
    //플로팅 버튼 애니메이션
    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;
    //db댓글을 불러와 저장하기 위한 선언
    private ArrayList Comment_name;
    private ArrayList Comment_date;
    private ArrayList Comment_content;
    private ArrayList Comment_url;
    private ArrayList Comment_uid;

    private String totalGradeScore;
    //db북마크 카운트용
    private int Bookmark_count;

    //dbGrading 카운트 및 총합세기위한 변수 선언
    private float Score_count = 0;
    private float Score_total = 0;


    @SuppressLint("RestrictedApi")
    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.cocktail_recipe_activity);

        Intent intent = getIntent();

        TextView textForCocktailName = (TextView) findViewById(R.id.textView_cocktailName_recipe);
        TextView textForCocktailID = (TextView) findViewById(R.id.textView_ingredientText_recipe);
        TextView textForCocktailDescription = (TextView) findViewById(R.id.textView_cocktailDescription_recipe);
        TextView textForCocktailIngredient = (TextView) findViewById(R.id.textView_cocktailIngredient_recipe);
        TextView textForSimpleABV = (TextView) findViewById(R.id.textView_ABVText_recipe);
        TextView textForCocktailABV = (TextView) findViewById(R.id.textView_cocktailABV_recipe);
        TextView textForNonLoginUser = (TextView) findViewById(R.id.textView_info_for_nonLoginUser_recipe);
        TextView textForGrading = (TextView) findViewById(R.id.textView_simpleRating_recipe);
        floatingActionButtonForBookmark = (FloatingActionButton) findViewById(R.id.floatingActionButton_bookmark_recipe);
        floatingActionButtonForGrade = (FloatingActionButton) findViewById(R.id.floatingActionButton_grade_recipe);
        floatingActionButtonForReport = (FloatingActionButton) findViewById(R.id.floatingActionButton_report_recipe);
        floatingActionButtonForAnimation = (FloatingActionButton) findViewById(R.id.floatingActionButton_animation_recipe);
        textInputLayoutForComment = (TextInputLayout) findViewById(R.id.textInputLayout_comment_recipe);
        imageButtonForComment = (ImageButton) findViewById(R.id.imageButton_comment_recipe);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar_grading_recipe);
        //수정필
        gradeScore="0.0";
        totalGradeScore="0.0";
        recyclerViewForComment = findViewById(R.id.recyclerViewForComment_recipe);
        LinearLayout linearLayoutForCommentTextInput = findViewById(R.id.linearLayout_cocktail_recipe);
        final ImageView imageForCocktail = (ImageView) findViewById(R.id.imageView_cocktail_recipe);

        cocktailName = intent.getExtras().getString("cocktailName");
        cocktailID = intent.getExtras().getInt("cocktailID");
        String cocktailDescription = intent.getExtras().getString("cocktailDescription");
        String cocktailIngredient = intent.getExtras().getString("cocktailIngredient");
        String cocktailABV = intent.getExtras().getString("cocktailABV");
        cocktailRef = intent.getExtras().getString("cocktailRef");

        layoutManagerForCocktailComment = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        //역순 출력
        layoutManagerForCocktailComment.setReverseLayout(true);
        layoutManagerForCocktailComment.setStackFromEnd(true);

        recyclerViewForComment.setLayoutManager(layoutManagerForCocktailComment);
        recyclerViewForComment.setAdapter(adapterForCocktailComment);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference gsReference = storage.getReferenceFromUrl(cocktailRef);

        gsReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    // Glide 이용하여 이미지뷰에 로딩
                    Glide.with(CocktailRecipeActivity.this)
                            .load(task.getResult())
                            .into(imageForCocktail);
                } else {
                    // URL을 가져오지 못하면 토스트 메세지
                }
            }
        });
        textForCocktailName.setText(cocktailName);
        //인텐트로 받은 값이
        //재료일 경우
        //재료대신 설탕 함유량 문구 출력
        //그리고 평가 부분 삭제
        //북마크 삭제
        //별점 버튼도 삭제
        if((cocktailID/1000)==5){
            textForCocktailID.setText("설탕 함유량");
            textForGrading.setVisibility(View.GONE);
            ratingBar.setVisibility(View.GONE);
            floatingActionButtonForBookmark.setVisibility(View.GONE);
            floatingActionButtonForGrade.setVisibility(View.GONE);
        }
        else{
            if ((cocktailID/1000)>=10) {
                //유저가 올린 레시피이면, abv대신 작성자 텍스트 출력
                textForSimpleABV.setText("작성자: ");
            }
            else{
                textForCocktailID.setText("재료");
            }
            //영진 여기다가 별점 불러다 박아주셈 totalGradeScore=
            currentUser = mAuth.getCurrentUser();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            if((cocktailID/1000)==6)
            {
                DocumentReference docRef = db.collection("Recipe").document(Integer.toString(cocktailID));
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                totalGradeScore = document.get("good_number").toString();
                                ratingBar.setRating(Float.valueOf(totalGradeScore));
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
            }
            else
            {
                DocumentReference docRef = db.collection("Self").document(Integer.toString(cocktailID));
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                totalGradeScore = document.get("good_number").toString();
                                ratingBar.setRating(Float.valueOf(totalGradeScore));
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
            }

        }
        textForCocktailDescription.setText(cocktailDescription);
        textForCocktailIngredient.setText(cocktailIngredient);
        textForCocktailABV.setText(cocktailABV);

        //플로팅 버튼 애니메이션
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);

        //로그인 안한 경우
        if(currentUser == null){
            //로그인 하지 않으면
            //북마크, 평가, 신고, 댓글입력 버튼 안보이게
            //floatingActionButtonForBookmark.setVisibility(View.GONE);
            //floatingActionButtonForGrade.setVisibility(View.GONE);
            //floatingActionButtonForReport.setVisibility(View.GONE);

            //애니메이션 버튼만 안보이면 나머지는 안보이겠지 아마
            floatingActionButtonForAnimation.setVisibility(View.GONE);
            linearLayoutForCommentTextInput.setVisibility(View.GONE);
        }
        //로그인한 경우
        else{
            //로그인한 유저의 북마크와
            //평가, 신고 정보를 가져와서
            //북마크, 평가, 신고 버튼의 초기값 세팅
            //영진파트
            //checked의 값이 true면 버튼의 초기모양 변경

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            //유저의 uid + 레시피 ID를 사용하여 리포트 이름 선정
            final String ReportName = currentUser.getUid()+cocktailID;
            DocumentReference Report_check = db.collection("Report").document(ReportName);

            //해당 이름으로 문서가 비어있는지 아닌지 map크기를 확인하여 체크하여 false/ true로 바꿔줌.
            Report_check.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Map<String, Object> map = document.getData();
                            if (map.size() == 0) {
                                reportChecked = false;
                                Log.d(TAG, "Report Document is empty!");
                            } else {
                                reportChecked = true;
                                Log.d(TAG, "Report Document is not empty!");
                            }
                        }
                    }
                }
            });

            //유저의 uid + 레시피 ID를 사용하여 리포트 이름 선정
            final String BookmarkName = currentUser.getUid()+cocktailID;
            DocumentReference Bookmark_check = db.collection("Bookmark").document(BookmarkName);

            //해당 이름으로 문서가 비어있는지 아닌지 map크기를 확인하여 체크
            Bookmark_check.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Map<String, Object> map = document.getData();
                            if (map.size() == 0) {
                                bookmarkChecked = false;
                                Log.d(TAG, "Bookmark Document is empty!");
                            } else {
                                bookmarkChecked = true;
                                Log.d(TAG, "Bookmark Document is not empty!");
                            }
                        }
                    }
                }
            });

            //유저의 uid + 레시피 ID를 사용하여 리포트 이름 선정
            final String GradingName = currentUser.getUid()+cocktailID;
            DocumentReference Grading_check = db.collection("Grading").document(GradingName);

            //해당 이름으로 문서가 비어있는지 아닌지 map크기를 확인하여 체크
            Grading_check.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Map<String, Object> map = document.getData();
                            if (map.size() == 0) {
                                gradeChecked = false;
                                Log.d(TAG, "Grading Document is empty!");
                            } else {
                                gradeChecked = true;
                                gradeScore = String.valueOf(document.get("점수"));
                                Log.d(TAG, "Grading Document is not empty!");
                            }
                        }
                    }
                }
            });

            //로그인한 유저에게는 로그인 하지 않았다는 메시지 출력 삭제
            textForNonLoginUser.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


        //Toast.makeText(getApplicationContext(),"on resume",Toast.LENGTH_LONG).show();
        //영진 이부분에서 db 에 있는 댓글내용 불러와
        //adapterForCocktailComment.addItem(new Comment(user.getDisplayName(),"날짜: " + formatDate,stringForCocktailComment,user.getPhotoUrl().toString(),user.getUid()));
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        //받아오기위해 변수들 초기화
        Comment_name = new ArrayList();
        Comment_date = new ArrayList();
        Comment_content = new ArrayList();
        Comment_url = new ArrayList();
        Comment_uid = new ArrayList();
        //Comment컬렉션의 문서들중 레시피 번호 필드가 현재 보고있는 레시피 번호와 일치하는 것들 검색
        db.collection("Comment")
                .whereEqualTo("레시피 번호", Integer.toString(cocktailID)).orderBy("댓글 날짜", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            adapterForCocktailComment.clearAllForAdapter();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.get("사용자 uid").equals("0") )
                                {
                                    System.out.println("얘는 거른다");
                                }
                                else
                                {
                                    Comment_name.add(document.get("사용자 이름").toString());
                                    Comment_date.add("날짜: "+document.get("댓글 날짜").toString());
                                    Comment_content.add(document.get("내용").toString());
                                    Comment_url.add(document.get("사용자 url").toString());
                                    Comment_uid.add(document.get("사용자 uid").toString());
                                }
                            }
                            for(int i = 0; i< Comment_name.size(); i++)
                            {
                                adapterForCocktailComment.addItem(new Comment(Comment_name.get(i).toString(),Comment_date.get(i).toString(),Comment_content.get(i).toString(),Comment_url.get(i).toString(),Comment_uid.get(i).toString()));
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        textInputLayoutForComment.setCounterEnabled(true);
        textInputLayoutForComment.setCounterMaxLength(150);
        final EditText editTextForCocktailComment = textInputLayoutForComment.getEditText();
        editTextForCocktailComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>150) {
                    editTextForCocktailComment.setError("150자 이하로 입력해주세요!");
                } else {
                    editTextForCocktailComment.setError(null);
                }
            }
        });

        imageButtonForComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = mAuth.getCurrentUser();
                stringForCocktailComment = editTextForCocktailComment.getText().toString();
                if(stringForCocktailComment.getBytes().length==0){
                    Toast.makeText(getApplicationContext(),"빈칸을 남기지 말아주세요!",Toast.LENGTH_LONG).show();
                }else if(editTextForCocktailComment.getError()!=null){
                    Toast.makeText(getApplicationContext(),"문자 길이를 준수해주세요!",Toast.LENGTH_LONG).show();
                }
                //정상 상태일 경우
                //날짜 정보등과 함께
                //댓글 등록
                //영진파트
                else{

                    long now = System.currentTimeMillis();
                    Date date = new Date(now);
                    SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss");
                    String formatDate = sdfNow.format(date);

                    //adapterForCocktailComment.addItem(new Comment(user.getDisplayName(),"날짜: " + formatDate,stringForCocktailComment,user.getPhotoUrl().toString(),user.getUid()));
                    //레시피번호, 레시피이름, 레시피ref, 사용자이름, 사용자url, 사용자uid, 내용, 날짜를 Comment 컬렉션에 저장
                    //도큐먼트 이름 형식은 날짜+uid
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    Map<String, Object> putComment = new HashMap<>();
                    putComment.put("레시피 번호", Integer.toString(cocktailID));
                    putComment.put("레시피 이름", cocktailName);
                    putComment.put("레시피 ref", cocktailRef);
                    putComment.put("사용자 이름", currentUser.getDisplayName());
                    putComment.put("사용자 url", currentUser.getPhotoUrl().toString());
                    putComment.put("사용자 uid", currentUser.getUid());
                    putComment.put("내용", stringForCocktailComment);
                    putComment.put("댓글 날짜", formatDate);
                    putComment.put("문서 날짜", date.toString());
                    String DocumentName = "날짜: "+formatDate+currentUser.getUid();
                    db.collection("Comment").document(DocumentName)
                            .set(putComment)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                    //Toast.makeText(getApplicationContext(),"댓글 내용: " + stringForCocktailComment,Toast.LENGTH_LONG).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error writing document", e);
                                    Toast.makeText(getApplicationContext(),"댓글 달기 실패! 다시 시도해주세요",Toast.LENGTH_LONG).show();
                                }
                            });


                    //recyclerViewForComment.setAdapter(adapterForCocktailComment);
                    editTextForCocktailComment.getText().clear();
                    //영진 이부분에서 db 에 있는 댓글내용 불러와
                    //받아오기위해 변수들 초기화
                    Comment_name = new ArrayList();
                    Comment_date = new ArrayList();
                    Comment_content = new ArrayList();
                    Comment_url = new ArrayList();
                    Comment_uid = new ArrayList();
                    //Comment컬렉션의 문서들중 레시피 번호 필드가 현재 보고있는 레시피 번호와 일치하는 것들 검색
                    db.collection("Comment")
                            .whereEqualTo("레시피 번호", Integer.toString(cocktailID)).orderBy("댓글 날짜", Query.Direction.ASCENDING)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        adapterForCocktailComment.clearAllForAdapter();
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            if(document.get("사용자 uid").equals("0") )
                                            {
                                                System.out.println("얘는 거른다");
                                            }
                                            else
                                            {
                                                Comment_name.add(document.get("사용자 이름").toString());
                                                Comment_date.add("날짜: "+document.get("댓글 날짜").toString());
                                                Comment_content.add(document.get("내용").toString());
                                                Comment_url.add(document.get("사용자 url").toString());
                                                Comment_uid.add(document.get("사용자 uid").toString());
                                            }
                                        }
                                        for(int i = 0; i< Comment_name.size(); i++)
                                        {
                                            Log.d(TAG, "들어가야하는 uid : " + Comment_uid.get(i).toString());
                                            adapterForCocktailComment.addItem(new Comment(Comment_name.get(i).toString(),Comment_date.get(i).toString(),Comment_content.get(i).toString(),Comment_url.get(i).toString(),Comment_uid.get(i).toString()));
                                        }
                                    } else {
                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                    }
                                }
                            });
                }
            }
        });

        adapterForCocktailComment.setOnItemClickListener(new OnCocktailCommentItemClickListener() {
            @Override
            public void onItemClick(CocktailCommentAdapter.ViewHolder holder, View view, int position) {
                Comment item = adapterForCocktailComment.getItem(position);
                FirebaseUser user = mAuth.getCurrentUser();

                //자신이 올린 글을 선택했을 경우 삭제 가능
                //로그인을 하지 않은 경우
                if(user==null) {
                    Toast.makeText(getApplicationContext(), "본인의 댓글만 삭제 가능합니다! 로그인 필요", Toast.LENGTH_LONG).show();
                    //Toast.makeText(getApplicationContext(),"삭제 가능한 칵테일 " + item.getName(),Toast.LENGTH_LONG).show();
                }
                //자신이 올린 글을 선택했을 경우
                //삭제 가능
                //영진 파트
                else if(item.getUid().equals(user.getUid())){
                    PopupMenu popup= new PopupMenu(getApplicationContext(), view);
                    final int positionForDelete=position;
                    final String DocumentName = item.getDate()+item.getUid();
                    getMenuInflater().inflate(R.menu.popup_menu_user_comment, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()){
                                case R.id.popup_delete:
                                    //댓글 삭제
                                    //누른 댓글의 item정보를 확인하여 getDate + getUid형태의 이름을 가진 문서를 DB에서 삭제한다.
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    db.collection("Comment").document(DocumentName)
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                                    Toast.makeText(getApplication(),"댓글 삭제",Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Error deleting document", e);
                                                    Toast.makeText(getApplication(),"댓글 삭제 실패! 다시 시도해주세요",Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                    //adapterForCocktailComment.removeItem(positionForDelete);
                                    //recyclerViewForComment.setAdapter(adapterForCocktailComment);
                                    //영진 이부분에서 db 에 있는 댓글내용 불러와
                                    //받아오기위해 변수들 초기화
                                    Comment_name = new ArrayList();
                                    Comment_date = new ArrayList();
                                    Comment_content = new ArrayList();
                                    Comment_url = new ArrayList();
                                    Comment_uid = new ArrayList();
                                    //Comment컬렉션의 문서들중 레시피 번호 필드가 현재 보고있는 레시피 번호와 일치하는 것들 검색
                                    db.collection("Comment")
                                            .whereEqualTo("레시피 번호", Integer.toString(cocktailID)).orderBy("댓글 날짜", Query.Direction.ASCENDING)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        adapterForCocktailComment.clearAllForAdapter();
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                            if(document.get("사용자 uid").equals("0") )
                                                            {
                                                                System.out.println("얘는 거른다");
                                                            }
                                                            else
                                                            {
                                                                Comment_name.add(document.get("사용자 이름").toString());
                                                                Comment_date.add("날짜: "+document.get("댓글 날짜").toString());
                                                                Comment_content.add(document.get("내용").toString());
                                                                Comment_url.add(document.get("사용자 url").toString());
                                                                Comment_uid.add(document.get("사용자 uid").toString());
                                                            }

                                                        }
                                                        for(int i = 0; i< Comment_name.size(); i++)
                                                        {
                                                            adapterForCocktailComment.addItem(new Comment(Comment_name.get(i).toString(),Comment_date.get(i).toString(),Comment_content.get(i).toString(),Comment_url.get(i).toString(),Comment_uid.get(i).toString()));
                                                        }
                                                    } else {
                                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                                    }
                                                }
                                            });
                                    break;
                                default:
                                    Toast.makeText(getApplication(),"취소",Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            return false;
                        }
                    });
                    popup.show();
                }
                //로그인을 했지만
                //다른사람의 댓글을 선택했을 경우
                else{
                    Toast.makeText(getApplicationContext(),"본인의 댓글만 삭제 가능합니다!",Toast.LENGTH_LONG).show();
                }
            }
        });


        floatingActionButtonForBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //영진 파트
                FirebaseUser currentUser = mAuth.getCurrentUser();
                final FirebaseFirestore db = FirebaseFirestore.getInstance();
                String BookmarkName = currentUser.getUid()+cocktailID;
                Bookmark_count = 0;

                //북마크가 이미 선택되었던 경우
                if(bookmarkChecked==true){
                    db.collection("Bookmark").document(BookmarkName)
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                    Toast.makeText(getApplicationContext(),"북마크 취소",Toast.LENGTH_LONG).show();
                                    floatingActionButtonForBookmark.setImageResource(R.mipmap.outline_bookmark_border_white_36dp);
                                    bookmarkChecked=false;
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error deleting document", e);
                                    Toast.makeText(getApplication(),"북마크 취소 실패! 다시 시도해주세요",Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                //북마크를 추가하는 경우
                else{
                    //formatdate를 위한 선언 및 변환
                    long now = System.currentTimeMillis();
                    Date date = new Date(now);
                    SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss");
                    String formatDate = sdfNow.format(date);

                    //카드뷰 어케 만들지 몰라서 일단 많이 넣음
                    Map<String, Object> putBookmark = new HashMap<>();
                    putBookmark.put("레시피 번호", Integer.toString(cocktailID));
                    putBookmark.put("레시피 이름", cocktailName);
                    putBookmark.put("레시피 ref", cocktailRef);
                    putBookmark.put("사용자 이름", currentUser.getDisplayName());
                    putBookmark.put("사용자 url", currentUser.getPhotoUrl().toString());
                    putBookmark.put("사용자 uid", currentUser.getUid());
                    putBookmark.put("북마크 날짜", formatDate);
                    db.collection("Bookmark").document(BookmarkName)
                            .set(putBookmark)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                    Toast.makeText(getApplicationContext(),"북마크",Toast.LENGTH_LONG).show();
                                    floatingActionButtonForBookmark.setImageResource(R.mipmap.baseline_bookmark_white_36dp);
                                    bookmarkChecked=true;
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplication(),"북마크 실패! 다시 시도해주세요",Toast.LENGTH_SHORT).show();
                                    Log.w(TAG, "Error writing document", e);
                                }
                            });
                }
                //db북마크 컬렉션의 레시피 번호 같은 갯수만큼 bookmark_count갯수 세어서 넣어줌.
                db.collection("Bookmark")
                        .whereEqualTo("레시피 번호", Integer.toString(cocktailID))
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    //수정
                                    //adapterForCocktailComment.clearAllForAdapter();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Bookmark_count++;
                                    }
                                    //해당 레시피번호와 동일한 레시피의 mark_number를 Bookmark_count와 동일하게 해서 업데이트함
                                    if((cocktailID/1000)==6)
                                    {
                                        DocumentReference Bookmark_ref = db.collection("Recipe").document(Integer.toString(cocktailID));
                                        Bookmark_ref
                                                .update("mark_number", Bookmark_count)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w(TAG, "Error updating document", e);
                                                    }
                                                });
                                    }
                                    else
                                    {
                                        DocumentReference Bookmark_ref = db.collection("Self").document(Integer.toString(cocktailID));
                                        Bookmark_ref
                                                .update("mark_number", Bookmark_count)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w(TAG, "Error updating document", e);
                                                    }
                                                });
                                    }

                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }
        });
        floatingActionButtonForGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //평가는 계정당 한번만
                //평가는 취소 불가
                //평가는 수정만 가능
                //영진 파트
                //평가 수정을 원하는 경우

                //평가 날짜를 위한 format date생성
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss");
                String formatDate = sdfNow.format(date);
                if(gradeChecked==true){
                    Intent intent = new Intent(v.getContext(), GradingPopupActivity.class);
                    intent.putExtra("cocktailID",cocktailID);
                    intent.putExtra("gradeCheck",gradeChecked);
                    intent.putExtra("gradeScore",gradeScore);
                    startActivityForResult(intent, 1);
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    String GradingName = currentUser.getUid()+cocktailID;
                    DocumentReference Grading_ref = db.collection("Grading").document(GradingName);
                    Grading_ref
                            .update("점수", gradeScore)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully updated!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error updating document", e);
                                }
                            });
                    Grading_ref
                            .update("평가 날짜", formatDate)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully updated!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error updating document", e);
                                }
                            });
                }
                //처음으로 평가를 하는 경우
                else{
                    Toast.makeText(getApplicationContext(),"평가 시작",Toast.LENGTH_LONG).show();
                    //평가를 중간에 취소하면
                    //gradeChecked가 true로 변경되면 안됨
                    //수정 필
                    Intent intent = new Intent(v.getContext(), GradingPopupActivity.class);
                    intent.putExtra("cocktailID",cocktailID);
                    intent.putExtra("gradeCheck",gradeChecked);
                    intent.putExtra("gradeScore",gradeScore);
                    startActivityForResult(intent, 1);
                    //floatingActionButtonForGrade.setImageResource(R.mipmap.outline_star_white_36dp);
                    //gradeChecked=true;
                }
            }
        });

        floatingActionButtonForReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //신고는 계정당 한번만
                //같은 게시물에 같은 계정이 여러번 신고 불가능
                //영진 파트

                //이미 신고를 한 경우
                //신고 거부 안내
                if(reportChecked==true){
                    Toast.makeText(getApplicationContext(),"이미 신고하신 게시물입니다",Toast.LENGTH_LONG).show();
                }
                //처음으로 신고를 하는 경우
                else{
                    Intent intent = new Intent(v.getContext(), ReportPopupActivity.class);
                    intent.putExtra("cocktailID",cocktailID);
                    startActivityForResult(intent, 2);
                    //Toast.makeText(getApplicationContext(),"신고 접수",Toast.LENGTH_LONG).show();
                }
            }
        });

        floatingActionButtonForAnimation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //애니메이션 시작
                anim();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //평가 팝업의 결과물
        //requestCode == 1
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                //data.getFloatExtra("rating");
                //ratingBar.setRating(data.getExtras().getFloat("rating"));
                String ratingString = data.getStringExtra("rating");
                float ratingFloat = Float.valueOf(ratingString);
                gradeScore = ratingString;
                //평가 날짜를 위한 format date생성
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss");
                String formatDate = sdfNow.format(date);

                //레시피번호, 레시피이름, 레시피ref, 사용자이름, 사용자url, 사용자uid, 내용, 날짜를 Comment 컬렉션에 저장
                //도큐먼트 이름 형식은 유저의 uid + 레시피 ID
                FirebaseUser currentUser = mAuth.getCurrentUser();
                final FirebaseFirestore db = FirebaseFirestore.getInstance();
                String GradingName = currentUser.getUid()+cocktailID;
                Score_count = 0;
                Score_total = 0;

                Map<String, Object> putGrading = new HashMap<>();
                putGrading.put("레시피 번호", Integer.toString(cocktailID));
                putGrading.put("레시피 이름", cocktailName);
                putGrading.put("레시피 ref", cocktailRef);
                putGrading.put("사용자 이름", currentUser.getDisplayName());
                putGrading.put("사용자 url", currentUser.getPhotoUrl().toString());
                putGrading.put("사용자 uid", currentUser.getUid());
                putGrading.put("점수", gradeScore);
                putGrading.put("평가 날짜", formatDate);
                db.collection("Grading").document(GradingName)
                        .set(putGrading)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document", e);
                            }
                        });


                //db의 Grading 컬렉션의 레시피 번호 같은 갯수만큼 Score_count갯수 세어서 넣어줌.
                db.collection("Grading")
                        .whereEqualTo("레시피 번호", Integer.toString(cocktailID))
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        //나누어줄 총 갯수 세기
                                        Score_count++;
                                        //점수들의 총합 계산
                                        Score_total += Float.parseFloat((String) document.get("점수"));
                                    }
                                    //해당 레시피번호와 동일한 레시피의 mark_number를 Bookmark_count와 동일하게 해서 업데이트함
                                    if((cocktailID/1000)==6)
                                    {
                                        DocumentReference Gradingmark_ref = db.collection("Recipe").document(Integer.toString(cocktailID));
                                        Gradingmark_ref
                                                .update("good_number", Float.toString((Score_total/Score_count)))
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                                                        Toast.makeText(getApplicationContext(), "나의 평가: " + gradeScore, Toast.LENGTH_SHORT).show();
                                                        totalGradeScore = Float.toString((Score_total/Score_count));
                                                        ratingBar.setRating(Float.valueOf(totalGradeScore));
                                                        floatingActionButtonForGrade.setImageResource(R.mipmap.outline_star_white_36dp);
                                                        gradeChecked=true;
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w(TAG, "Error updating document", e);
                                                    }
                                                });
                                    }
                                    else
                                    {
                                        DocumentReference Gradingmark_ref = db.collection("Self").document(Integer.toString(cocktailID));
                                        Gradingmark_ref
                                                .update("good_number", Float.toString((Score_total/Score_count)))
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                                                        Toast.makeText(getApplicationContext(), "나의 평가: " + gradeScore, Toast.LENGTH_SHORT).show();
                                                        totalGradeScore = Float.toString((Score_total/Score_count));
                                                        ratingBar.setRating(Float.valueOf(totalGradeScore));
                                                        floatingActionButtonForGrade.setImageResource(R.mipmap.outline_star_white_36dp);
                                                        gradeChecked=true;
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w(TAG, "Error updating document", e);
                                                    }
                                                });
                                    }

                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }
        }
        //신고 팝업의 결과물
        //requestCode == 2
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                floatingActionButtonForReport.setImageResource(R.mipmap.baseline_feedback_white_36dp);
                reportChecked=true;
            }
        }
    }

    //플로팅 버튼 애니메이션을 위한 버튼
    public void anim() {
        if(bookmarkChecked==true){
            floatingActionButtonForBookmark.setImageResource(R.mipmap.baseline_bookmark_white_36dp);
        }
        if(gradeChecked==true){
            floatingActionButtonForGrade.setImageResource(R.mipmap.outline_star_white_36dp);
        }
        if(reportChecked==true){
            floatingActionButtonForReport.setImageResource(R.mipmap.baseline_feedback_white_36dp);
        }
        if (isFabOpen) {
            floatingActionButtonForAnimation.setImageResource(R.mipmap.outline_more_vert_white_36dp);
            floatingActionButtonForBookmark.startAnimation(fab_close);
            floatingActionButtonForGrade.startAnimation(fab_close);
            floatingActionButtonForReport.startAnimation(fab_close);
            floatingActionButtonForBookmark.setClickable(false);
            floatingActionButtonForGrade.setClickable(false);
            floatingActionButtonForReport.setClickable(false);
            isFabOpen = false;
        } else {
            floatingActionButtonForAnimation.setImageResource(R.mipmap.outline_close_white_36dp);
            floatingActionButtonForBookmark.startAnimation(fab_open);
            floatingActionButtonForGrade.startAnimation(fab_open);
            floatingActionButtonForReport.startAnimation(fab_open);
            floatingActionButtonForBookmark.setClickable(true);
            floatingActionButtonForGrade.setClickable(true);
            floatingActionButtonForReport.setClickable(true);
            isFabOpen = true;
        }
    }
}