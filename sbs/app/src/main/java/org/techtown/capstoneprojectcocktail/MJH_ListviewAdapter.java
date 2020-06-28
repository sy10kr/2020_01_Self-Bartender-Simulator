package org.techtown.capstoneprojectcocktail;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MJH_ListviewAdapter extends BaseAdapter {

    public ArrayList<Integer> checkPositionInPopup = new ArrayList<Integer>() ;

    TextView titleTextView;
    TextView techTextView;
    public TextView descTextView;
    public int callByPopup = 0;

    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    public ArrayList<MJH_ListviewItem> listViewItemList = new ArrayList<MJH_ListviewItem>() ;

    // ListViewAdapter의 생성자
    public MJH_ListviewAdapter() {

    }

    public void setCallByPopup(int input){
        this.callByPopup = input;
    }

    public int getA(){
        return callByPopup;
    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.mjh_listview_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        titleTextView = (TextView) convertView.findViewById(R.id.textView1) ;
        techTextView = (TextView) convertView.findViewById(R.id.textView2) ;
        descTextView = (TextView) convertView.findViewById(R.id.textView3) ;

        //techTextView.setTextSize(20);
        //descTextView.setTextSize(15);        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        MJH_ListviewItem listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        ArrayList<Integer> getStepBuffer = listViewItem.getAssociateStep();
        ArrayList<MJH_Object_ingredient> getIngredientBuffer =  listViewItem.getAssociateIngredient();
        ArrayList<Float> getAmountBuffer = listViewItem.amount;

        String getBufferStr = "";
        int stepExistFlag = 0;
        for (int index = 0; index < getStepBuffer.size(); index++) {
            if(index == getStepBuffer.size() - 1 && getIngredientBuffer.size() == 0){
                getBufferStr = getBufferStr + "STEP " + getStepBuffer.get(index).toString();
            }
            else{
                getBufferStr = getBufferStr + "STEP " + getStepBuffer.get(index).toString() + "\n";
            }
            stepExistFlag = 1;
        }
        for (int index = 0; index < getIngredientBuffer.size(); index++) {
            if(index == getIngredientBuffer.size() - 1 ){
                getBufferStr = getBufferStr + getIngredientBuffer.get(index).name + " " + getAmountBuffer.get(index).toString() + " ml";
            }
            else{
                getBufferStr = getBufferStr + getIngredientBuffer.get(index).name + " " + getAmountBuffer.get(index).toString() + " ml\n";
            }
        }

        titleTextView.setText(listViewItem.getThisStep());
        techTextView .setText(listViewItem.getTech());
        descTextView.setText(getBufferStr);

        if( callByPopup == 1) {
            //descTextView.setTextSize(10);
        }

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {

        if(callByPopup == 1 && !checkPositionInPopup.contains(position)){
            titleTextView.setBackgroundColor(Color.GREEN);
            checkPositionInPopup.add(position);
        }
        else if(callByPopup == 1){
            titleTextView.setBackgroundColor(Color.rgb(0, 102, 255));
            checkPositionInPopup.remove(position);
        }

        return listViewItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String title, String tech, ArrayList<Integer> setStep, ArrayList<MJH_Object_ingredient> setIngredient, ArrayList<Float> amount) {
        MJH_ListviewItem item = new MJH_ListviewItem();

        item.setThisStep(title);
        item.setTech(tech);
        item.setAssociateStep(setStep);
        item.setAssociateIngredient(setIngredient);
        item.amount = amount;
        listViewItemList.add(item);
    }
}


