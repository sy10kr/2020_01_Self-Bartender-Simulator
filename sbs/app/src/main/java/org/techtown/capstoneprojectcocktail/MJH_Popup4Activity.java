package org.techtown.capstoneprojectcocktail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

import static org.techtown.capstoneprojectcocktail.CocktailAdapterForSearch.useByMinFlag;
import static org.techtown.capstoneprojectcocktail.MJH_Popup2Activity.updateTotalVol;
import static org.techtown.capstoneprojectcocktail.MJH_Popup3Activity.ingreAmount;
import static org.techtown.capstoneprojectcocktail.MJH_Popup3Activity.ingreAmountFlag;
import static org.techtown.capstoneprojectcocktail.MJH_Popup3Activity.thisStepPopup3AddAmount;
import static org.techtown.capstoneprojectcocktail.MJH_SimulatorUiActivity.highBallChecked;
import static org.techtown.capstoneprojectcocktail.MJH_SimulatorUiActivity.martiniChecked;
import static org.techtown.capstoneprojectcocktail.MJH_SimulatorUiActivity.shooterChecked;
import static org.techtown.capstoneprojectcocktail.MJH_SimulatorUiActivity.test;

public class MJH_Popup4Activity  extends Activity {
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.mjh_popup4);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void mClose(View v){
        ingreAmountFlag = 1;
        ingreAmount = -1;

        finish();
    }

    public void mNext(View v){
        //데이터 전달하기
        ingreAmountFlag = 1;

        MJH_SimulatorUiActivity simulatorUiAddress = ((MJH_SimulatorUiActivity)MJH_SimulatorUiActivity.uiMain);

        editText = (EditText) findViewById(R.id.editText) ;
        String strText = editText.getText().toString() ;
        if(strText.equals(""))
            Toast.makeText(this,"량을 입력해주세요!", Toast.LENGTH_SHORT).show();
        else{
            try{

                float nowVolume = 0;
                try{
                        nowVolume = test.simulatorStep.get(test.inGlassStep-1 ).totalVolume;
                }catch(Exception e){

                }
                ingreAmount = Integer.parseInt(strText);

                if( highBallChecked == true ) {
                    if((nowVolume + ingreAmount + updateTotalVol + thisStepPopup3AddAmount) > 250){
                        Toast.makeText(this,"하이볼 잔의 용량을 넘습니다!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        thisStepPopup3AddAmount = thisStepPopup3AddAmount + ingreAmount;
                        finish();
                    }
                }
                if( martiniChecked == true ) {
                    if((nowVolume + ingreAmount + updateTotalVol + thisStepPopup3AddAmount) > 140){
                        Toast.makeText(this,"칵테일 잔의 용량을 넘습니다!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        thisStepPopup3AddAmount = thisStepPopup3AddAmount + ingreAmount;
                        finish();
                    }
                }
                if(shooterChecked == true ) {
                    if((nowVolume + ingreAmount + updateTotalVol + thisStepPopup3AddAmount) > 60){
                        Toast.makeText(this,"슈터 잔의 용량을 넘습니다!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        thisStepPopup3AddAmount = thisStepPopup3AddAmount + ingreAmount;
                        finish();
                    }
                }
            }catch(Exception e){
                Toast.makeText(this,"숫자를 입력하여 주세요!", Toast.LENGTH_SHORT).show();
            }
        }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK){
                String result = data.getStringExtra("result");
                if (result == "-1"){

                }
            }
        }
    }
}
