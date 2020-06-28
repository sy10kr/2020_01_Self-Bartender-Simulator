package org.techtown.capstoneprojectcocktail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import static org.techtown.capstoneprojectcocktail.CocktailAdapterForSearch.useByMinFlag;
import static org.techtown.capstoneprojectcocktail.MJH_SimulatorUiActivity.adapterMIN;
import static org.techtown.capstoneprojectcocktail.MJH_SimulatorUiActivity.highBallChecked;
import static org.techtown.capstoneprojectcocktail.MJH_SimulatorUiActivity.martiniChecked;
import static org.techtown.capstoneprojectcocktail.MJH_SimulatorUiActivity.shooterChecked;
import static org.techtown.capstoneprojectcocktail.MJH_SimulatorUiActivity.uiMain;
import static org.techtown.capstoneprojectcocktail.MJH_SimulatorUiActivity.usingStepNum;

public class MJH_Popup2Activity extends Activity {
    public Context uiMe;

    ListView listview;
    public static float updateTotalVol = 0;

    MJH_ListviewItem buffer;
    MJH_SimulatorUiActivity simulatorUiAddress = ((MJH_SimulatorUiActivity) uiMain);
    public ArrayList<Integer> bufferUpdateStep = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiMe = this;
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.mjh_popup2);

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.listviewPopup2);
        adapterMIN.callByPopup = 1; // 어댑터 변수에서 팝업에서 콜했다고 셋팅

        //수정
        if(simulatorUiAddress.listUpdateTech.equals("Layering")){
            buffer = adapterMIN.listViewItemList.get(adapterMIN.listViewItemList.size() - 1);
            adapterMIN.listViewItemList.remove(adapterMIN.listViewItemList.size() - 1);
        }

        listview.setAdapter(adapterMIN); //

        if(adapterMIN.listViewItemList.size() == 0){
             TextView txt = (TextView)findViewById(R.id.title);
             txt.setText("추가할 스텝이 없습니다!\n 다음으로 넘어가 주세요.\n");
             txt.setTextColor(Color.GRAY);
        }



        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                if(simulatorUiAddress.listUpdateTech.equals("Layering") && bufferUpdateStep.size() > 0){
                    Toast myToast = Toast.makeText(uiMe,"Layering의 인풋은 한 스텝당 하나 입니다!", Toast.LENGTH_SHORT);
                    myToast.show();
                }
                else{

                    try{
                        MJH_ListviewItem item = (MJH_ListviewItem) parent.getItemAtPosition(position) ;
                        if(!simulatorUiAddress.usingStep.contains(position+1)){
                            updateTotalVol = updateTotalVol + simulatorUiAddress.test.simulatorStep.get(position).totalVolume;

                            if(highBallChecked == true && updateTotalVol > 250){
                                Toast.makeText(uiMe,"하이볼 잔의 용량을 넘습니다!", Toast.LENGTH_SHORT).show();
                                updateTotalVol = updateTotalVol - simulatorUiAddress.test.simulatorStep.get(position).totalVolume;
                            }
                            else if(martiniChecked == true && updateTotalVol > 140) {
                                Toast.makeText(uiMe,"칵테일 잔의 용량을 넘습니다!", Toast.LENGTH_SHORT).show();
                                updateTotalVol = updateTotalVol - simulatorUiAddress.test.simulatorStep.get(position).totalVolume;
                            }
                            else if(shooterChecked == true && updateTotalVol > 60) {
                                Toast.makeText(uiMe,"슈터 잔의 용량을 넘습니다!", Toast.LENGTH_SHORT).show();
                                updateTotalVol = updateTotalVol - simulatorUiAddress.test.simulatorStep.get(position).totalVolume;
                            }
                            else{
                                v.setBackgroundColor(Color.RED);
                                simulatorUiAddress.usingStep.add(position+1);
                                bufferUpdateStep.add(position+1);
                                usingStepNum.set(usingStepNum.size() - 1, usingStepNum.get(usingStepNum.size() - 1) + 1);
                                Toast myToast = Toast.makeText(uiMe,"스텝 " + Integer.toString(position+1) + " 추가", Toast.LENGTH_SHORT);
                                myToast.show();
                            }
                        }
                        else{
                            Toast myToast = Toast.makeText(uiMe,"이미 사용된 스텝입니다.", Toast.LENGTH_SHORT);
                            myToast.show();
                        }
                    }catch(Exception e){
                    }
                }
            }
        }) ;
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateTotalVol = 0;
    }

    public void mClose(View v){
        usingStepNum.remove(usingStepNum.size() - 1);

        if(simulatorUiAddress.listUpdateTech.equals("Layering")) {
            adapterMIN.listViewItemList.add(buffer);
        }
        finish();
    }


    //다음 버튼 클릭
    public void mNext(View v){
        if(simulatorUiAddress.listUpdateTech.equals("Layering")) {
            adapterMIN.listViewItemList.add(buffer);
        }

            //데이터 전달하기
        if(simulatorUiAddress.listUpdateTech.equals("Layering") && bufferUpdateStep.size() > 0){
            simulatorUiAddress.listUpdateStep = bufferUpdateStep;
            simulatorUiAddress.listUpdateIngredient = new ArrayList<MJH_Object_ingredient>();
            simulatorUiAddress.listUpdateFlag = 1;
            useByMinFlag = 0;
            finish();
        }
        else{
            Intent intent = new Intent(this,MJH_Popup3Activity.class);
            startActivityForResult(intent, 1);
            simulatorUiAddress.listUpdateStep = bufferUpdateStep;
            finish();
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
                    finish();
                }
            }
        }
    }

}
