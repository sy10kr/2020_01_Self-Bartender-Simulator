package org.techtown.capstoneprojectcocktail;

import java.util.Map;

public class CocktailIngredient {
    //    private String Ingredient_name;
//    private String Ingredient_type;
//    private float abv;
//    private float sugar_rate;
//    private float salty;
//    private float bitter;
//    private float sour;
//    private String flavour;
//    private float specific_gravity;
//    private Map<String, Number> Ingredient_color;
//    private String ref;
//    private String main_keyword;
    public String Ingredient_name;
    public String Ingredient_type;
    public float abv;
    public float sugar_rate;
    public float salty;
    public float bitter;
    public float sour;
    public String flavour;
    public float specific_gravity;
    public Map<String, Number> Ingredient_color;
    public String ref;
    public String main_keyword;

    public CocktailIngredient(
            String Ingredient_name,
            String Ingredient_type,
            float abv,
            float sugar_rate,
            float salty,
            float bitter,
            float sour,
            String flavour,
            float specific_gravity,
            Map<String, Number> Ingredient_color,
            String ref,
            String main_keyword) {
        this.Ingredient_name =Ingredient_name;
        this.Ingredient_type =Ingredient_type;
        this.abv =abv;
        this.sugar_rate =sugar_rate;
        this.salty =salty;
        this.bitter =bitter;
        this.sour =sour;
        this.flavour =flavour;
        this.specific_gravity =specific_gravity;
        this.Ingredient_color =Ingredient_color;
        this.ref = ref;
        this.main_keyword = main_keyword;
    }

    public String getIngredient_name() {
        return Ingredient_name;
    }

    public String getIngredient_type() {
        return Ingredient_type;
    }

    public float getAbv() {
        return abv;
    }

    public float getSugar_rate() {
        return sugar_rate;
    }

    public float getSalty() {
        return salty;
    }

    public float getBitter() {
        return bitter;
    }

    public float getSour() {
        return sour;
    }

    public String getFlavour() {
        return flavour;
    }
    public Map<String, Number> getIngredient_color(){
        //Ingredient_color.put("Red", 0);
        //Ingredient_color.put("Blue", 0);
        //Ingredient_color.put("Green", 0);
        return Ingredient_color;
    }

    public float getSpecific_gravity() {
        return specific_gravity;
    }

    public String getRef() {
        return ref;
    }

    public String getMain_keyword() {
        return main_keyword;
    }

}


