package org.techtown.capstoneprojectcocktail;

import java.util.ArrayList;

public class MJH_ListviewItem {

    public String thisStep;
    public String listUpdateTech;

    public ArrayList<Integer> listUpdateAssociateStep;
    public ArrayList<MJH_Object_ingredient> listUpdateIngredient;
    public ArrayList<Float> amount;


    public void setThisStep(String _step) {
        this.thisStep = _step ;
    }
    public void setTech(String _tech) {
        this.listUpdateTech = _tech ;
    }

    public void setAssociateStep(ArrayList<Integer> _step) {
        this.listUpdateAssociateStep = _step ;
    }
    public void setAssociateIngredient(ArrayList<MJH_Object_ingredient> _ingre) {
        this.listUpdateIngredient = _ingre ;
    }


    public String getThisStep() {
        return this.thisStep;
    }
    public String getTech() {
        return this.listUpdateTech;
    }

    public ArrayList<Integer> getAssociateStep() {
        return this.listUpdateAssociateStep;
    }
    public ArrayList<MJH_Object_ingredient> getAssociateIngredient() {
        return this.listUpdateIngredient;
    }
}
