package org.techtown.capstoneprojectcocktail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ReportPopupActivity extends Activity {
    private EditText textForReport;
    private RadioButton r_btn1,r_btn2, r_btn3,r_btn4;
    private RadioGroup radioGroup;
    private InputMethodManager inputKeyboardHide;
    private FirebaseAuth mAuth;
    private int cocktailID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.report_popup_activity);

        textForReport = (EditText) findViewById(R.id.editText_report_popup);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup_report);
        r_btn1 = (RadioButton) findViewById(R.id.radioButton1_report);
        r_btn2 = (RadioButton) findViewById(R.id.radioButton2_report);
        r_btn3 = (RadioButton) findViewById(R.id.radioButton3_report);
        r_btn4 = (RadioButton) findViewById(R.id.radioButton4_report);

        //키보드 숨기기
        inputKeyboardHide = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        //데이터 가져오기
        Intent intent = getIntent();
        //넘겨받은 칵테일 아아디
        cocktailID = intent.getIntExtra("cocktailID",0);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.radioButton1_report:
                    case R.id.radioButton2_report:
                    case R.id.radioButton3_report:
                        textForReport.getText().clear();
                        inputKeyboardHide.hideSoftInputFromWindow(textForReport.getWindowToken(), 0);
                        textForReport.setVisibility(View.GONE);
                        break;
                    case R.id.radioButton4_report:
                        Toast.makeText(getApplicationContext(), "신고내용을 적어주세요", Toast.LENGTH_LONG).show();
                        textForReport.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
    }

    //확인 버튼 클릭
    public void reportPopupConfirm(View v){
        //데이터 전달하기
        Intent intent = new Intent();
        //시간을 받아와 format date 생성
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss");
        String formatDate = sdfNow.format(date);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //report에 넣기위한 데이터 선언 및 생성
        Map<String, Object> putComment = new HashMap<>();
        putComment.put("사용자 uid", currentUser.getUid());
        putComment.put("칵테일 번호", Integer.toString(cocktailID));
        putComment.put("신고 날짜", formatDate);
        String DocumentName = currentUser.getUid()+cocktailID;

        //각 선택한 것에 따라 내용에 값을 넣어줌
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.radioButton1_report:
                //내용과 관련없는 이미지
                //영진파트
                Toast.makeText(getApplicationContext(),"신고 접수 완료\n신고내용: "+ r_btn1.getText().toString(),Toast.LENGTH_LONG).show();
                setResult(RESULT_OK, intent);
                putComment.put("내용", r_btn1.getText().toString());
                finish();
                break;
            case R.id.radioButton2_report:
                //잘못된 내용
                //영진파트
                Toast.makeText(getApplicationContext(),"신고 접수 완료\n신고내용: "+ r_btn2.getText().toString(),Toast.LENGTH_LONG).show();
                setResult(RESULT_OK, intent);
                putComment.put("내용", r_btn2.getText().toString());
                finish();
                break;
            case R.id.radioButton3_report:
                //칵테일과 관련없는 게시물
                //영진파트
                Toast.makeText(getApplicationContext(),"신고 접수 완료\n신고내용: "+ r_btn3.getText().toString(),Toast.LENGTH_LONG).show();
                setResult(RESULT_OK, intent);
                putComment.put("내용", r_btn3.getText().toString());
                finish();
                break;
            case R.id.radioButton4_report:
                // 신고 내용이 기타일 경우
                // 신고 내용을 적어야함
                String inputText = textForReport.getText().toString();
                if(inputText.getBytes().length==0){
                    Toast.makeText(getApplicationContext(),"신고내용을 적어주세요",Toast.LENGTH_LONG).show();
                }
                else{
                    //영진파트
                    Toast.makeText(getApplicationContext(),"신고 접수 완료\n신고내용: "+inputText,Toast.LENGTH_LONG).show();
                    setResult(RESULT_OK, intent);
                    putComment.put("내용", inputText);
                    finish();
                }
                break;
        }
        //위에서 Map에 넣은 데이터를 바탕으로 db에 문서 생성
        db.collection("Report").document(DocumentName)
                .set(putComment)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    //취소 버튼 클릭
    public void reportPopupCancel(View v){
        //데이터 전달하기
        Toast.makeText(getApplicationContext(),"신고 접수 취소",Toast.LENGTH_LONG).show();
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
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
}
