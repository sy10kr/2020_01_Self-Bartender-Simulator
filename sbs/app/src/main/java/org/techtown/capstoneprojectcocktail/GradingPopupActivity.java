package org.techtown.capstoneprojectcocktail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.RatingBar;
import android.widget.Toast;

public class GradingPopupActivity extends Activity {
    //변수명 수정 존나필요 ㅅㅄㅄㅄㅄㅂ
    //수정필
    //수정필
    private RatingBar ratingbar;
    private int cocktailID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.grading_popup_activity);

        //데이터 가져오기
        Intent intent = getIntent();
        String ratingString = intent.getStringExtra("gradeScore");
        //넘겨받은 칵테일 id
        cocktailID = intent.getIntExtra("cocktailID",0);
        float ratingFloat = Float.valueOf(ratingString);
        ratingbar = findViewById(R.id.ratingBar_grading_popup);
        ratingbar.setRating(ratingFloat);
    }

    //확인 버튼 클릭
    public void gradingPopupConfirm(View v){
        //데이터 전달하기
        String ratingFloatToString = Float.toString(ratingbar.getRating());
        //Toast.makeText(getApplicationContext(),ratingFloatToString,Toast.LENGTH_LONG).show();
        Intent intent = new Intent();
        intent.putExtra("rating", ratingFloatToString);
        setResult(RESULT_OK, intent);
        finish();
    }

    //취소 버튼 클릭
    public void gradingPopupCancel(View v){
        //데이터 전달하기
        Intent intent = new Intent();
        //intent.putExtra("result", "Close Popup");
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
