package org.techtown.capstoneprojectcocktail;

public class CocktailIngredientButton {
    //건드리는거 절대금지 노희상전용 클래스임 ㅇㅇ;
    private String IngredientCategorizedName;

    public CocktailIngredientButton(String ingredientCategorizedName) {
        this.IngredientCategorizedName = ingredientCategorizedName;
    }

    public String getIngredientCategorizedName() {
        return IngredientCategorizedName;
    }

    public void setIngredientCategorizedName(String ingredientCategorizedName) {
        IngredientCategorizedName = ingredientCategorizedName;
    }
}
