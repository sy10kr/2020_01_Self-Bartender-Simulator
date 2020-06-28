package org.techtown.capstoneprojectcocktail;

public class Cocktail {
    //칵테일 클래스는 손대기전에 모두와 의논필수
    //칵테일 클래스는 손대기전에 모두와 의논필수
    //칵테일 클래스는 손대기전에 모두와 의논필수
    //칵테일 클래스는 손대기전에 모두와 의논필수
    //칵테일 클래스는 손대기전에 모두와 의논필수
    //칵테일 클래스는 손대기전에 모두와 의논필수
    //칵테일 클래스는 손대기전에 모두와 의논필수
    //칵테일 클래스는 손대기전에 모두와 의논필수
    //칵테일 클래스는 손대기전에 모두와 의논필수
    //칵테일 클래스는 손대기전에 모두와 의논필수
    //칵테일 클래스는 손대기전에 모두와 의논필수
    //칵테일 클래스는 손대기전에 모두와 의논필수
    //칵테일 클래스는 손대기전에 모두와 의논필수
    //칵테일 클래스는 손대기전에 모두와 의논필수
    //칵테일 클래스는 손대기전에 모두와 의논필수
    String name;
    int id;
    String description;
    String ingredient;
    String abvNum;
    String imageUrl;
    String rating;

    public Cocktail(String name, int id, String description, String ingredient, String abvNum, String imageUrl) {
        this.name = name;
        this.id = id;
        this.description = description;
        this.ingredient = ingredient;
        this.abvNum = abvNum;
        this.imageUrl = imageUrl;
        this.rating = "0.0";
    }

    public Cocktail(String name, int id, String description, String ingredient, String abvNum, String imageUrl,String rating) {
        this.name = name;
        this.id = id;
        this.description = description;
        this.ingredient = ingredient;
        this.abvNum = abvNum;
        this.imageUrl = imageUrl;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getAbvNum() {
        return abvNum;
    }

    public void setAbvNum(String abvNum) {
        this.abvNum = abvNum;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
