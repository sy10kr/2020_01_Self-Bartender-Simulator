package org.techtown.capstoneprojectcocktail;

import java.util.ArrayList;

public class MJH_Object_cocktail implements Cloneable{
    boolean isInGlass = false; // 이 칵테일이 현재 잔에 들어가 있는 단계 인가?

    int isLayering = 0; // 칵테일 층의 수
    float totalVolume = 0; // 칵테일 총 부피
    float totalAbv = 0; // 단위 %
    float totalSpecificGravity = 0;

    //is_layering > 1일때 필요(레이어링 칵테일 일때)
    ArrayList<Float> eachAbv = new ArrayList<Float>();
    ArrayList<Float> eachVolume = new ArrayList<Float>();
    ArrayList<Float> specificGravity  = new ArrayList<Float>();
    ArrayList<MJH_Object_color> isColor  = new ArrayList<MJH_Object_color>();
    ArrayList<Float> alphaList  = new ArrayList<Float>();
    float alpha = 0;
    float muddy = 0;

    //플로팅이 깔끔하지 않은가?
    ArrayList<Integer> isBoundaryDirty  = new ArrayList<Integer>();

    //그라데이션이 있는가(그라데이션은 무조건 1개)
    int isGradient = 0;

    //맛에 대한 정보
    double sugar  = 0.0;
    double sour  = 0.0;
    double salty  = 0.0;
    double bitter  = 0.0;
    double hot = 0.0;

    //for 노희상 [향정보]
    ArrayList<MJH_Object_ingredient> ingredListForFlavour = new ArrayList<MJH_Object_ingredient>();


    //사용 x
    String[] flavour = new String[15];

    public MJH_Object_cocktail() {
        eachAbv.add((float)0);
        eachVolume.add((float)0);
        specificGravity.add((float)0);

        isColor.add(new MJH_Object_color(0,0,0));
    }


    public boolean isInGlass() {
        return isInGlass;
    }

    public void setInGlass(boolean inGlass) {
        this.isInGlass = inGlass;
    }

    public int getIsLayering() {
        return isLayering;
    }

    public void setIsLayering(int isLayering) {
        this.isLayering = isLayering;
    }

    public float getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(float totalVolume) {
        this.totalVolume = totalVolume;
    }

    public float getTotalAbv() {
        return totalAbv;
    }

    public void setTotalAbv(float totalAbv) {
        this.totalAbv = totalAbv;
    }

    public ArrayList<Float> getEachVolume() {
        return eachVolume;
    }

    public void setEachVolume(ArrayList<Float> eachVolume) {
        this.eachVolume = eachVolume;
    }

    public ArrayList<Float> getEachAbv() {
        return eachAbv;
    }

    public void setEachAbv(ArrayList<Float> eachAbv) {
        this.eachAbv = eachAbv;
    }

    public ArrayList<Float> getSpecificGravity() {
        return specificGravity;
    }

    public void setSpecificGravity(ArrayList<Float> specificGravity) {
        this.specificGravity = specificGravity;
    }

    public ArrayList<MJH_Object_color> getIs_color() {
        return isColor;
    }

    public void setIs_colorlor(ArrayList<MJH_Object_color> is_colorlor) {
        this.isColor = is_colorlor;
    }

    public ArrayList<Integer> getIsBoundaryDirty() {
        return isBoundaryDirty;
    }

    public void setIsBoundaryDirty(ArrayList<Integer> isBoundaryDirty) {
        this.isBoundaryDirty = isBoundaryDirty;
    }

    public int getIsGradient() {
        return isGradient;
    }

    public void setIsGradient(int isGradient) {
        this.isGradient = isGradient;
    }

    public String[] getFlavour() {
        return flavour;
    }

    public void setFlavour(String[] flavour) {
        this.flavour = flavour;
    }


    @Override
    public Object clone() throws CloneNotSupportedException{ //깊은복사
        //CloneNotSupportedException 처리
        return super.clone();
    }

    //전체 부피와 알콜도수 값 구하기
    public void add_vol_abv_total(){
        this.totalVolume = 0;
        this.totalAbv = 0;

        for(int i = 0; i < this.eachVolume.size(); i++){
            this.totalVolume = this.totalVolume + this.eachVolume.get(i);
            this.totalAbv = this.totalAbv + this.eachVolume.get(i) * this.eachAbv.get(i) / 100;
        }
        this.totalAbv = this.totalAbv / this.totalVolume * 100;
    }
}
