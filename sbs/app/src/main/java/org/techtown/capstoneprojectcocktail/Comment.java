package org.techtown.capstoneprojectcocktail;

public class Comment {
    //코멘트 클래스 손대기전에 노희상이랑 의논 필수
    //코멘트 클래스 손대기전에 노희상이랑 의논 필수
    //코멘트 클래스 손대기전에 노희상이랑 의논 필수
    //코멘트 클래스 손대기전에 노희상이랑 의논 필수
    //코멘트 클래스 손대기전에 노희상이랑 의논 필수
    //코멘트 클래스 손대기전에 노희상이랑 의논 필수
    //코멘트 클래스 손대기전에 노희상이랑 의논 필수

    String name;
    String date;
    String contents;
    String imageUrl;
    String uid;
    String cocktailName;
    int cocktailID;
    String cocktailDescription;
    String cocktailIngredient;
    String cocktailAbvNum;
    String cocktailImageUri;

    public Comment(String name, String date, String contents, String imageUrl, String uid) {
        this.name = name;
        this.date = date;
        this.contents = contents;
        this.imageUrl = imageUrl;
        this.uid = uid;
        this.cocktailName = "";
        this.cocktailID = 0;
        this.cocktailDescription = "";
        this.cocktailIngredient = "";
        this.cocktailAbvNum = "";
        this.cocktailImageUri ="";
    }

    public Comment(String name, String date, String contents, String imageUrl, String uid,String cocktailName,int cocktailID,String cocktailDescription,String cocktailIngredient,String cocktailAbvNum,String cocktailImageUri) {
        this.name = name;
        this.date = date;
        this.contents = contents;
        this.imageUrl = imageUrl;
        this.uid = uid;
        this.cocktailName = cocktailName;
        this.cocktailID = cocktailID;
        this.cocktailDescription = cocktailDescription;
        this.cocktailIngredient = cocktailIngredient;
        this.cocktailAbvNum = cocktailAbvNum;
        this.cocktailImageUri =cocktailImageUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCocktailName() {
        return cocktailName;
    }

    public void setCocktailName(String cocktailName) {
        this.cocktailName = cocktailName;
    }

    public int getCocktailID() {
        return cocktailID;
    }

    public void setCocktailID(int cocktailID) {
        this.cocktailID = cocktailID;
    }

    public String getCocktailDescription() {
        return cocktailDescription;
    }

    public void setCocktailDescription(String cocktailDescription) {
        this.cocktailDescription = cocktailDescription;
    }

    public String getCocktailIngredient() {
        return cocktailIngredient;
    }

    public void setCocktailIngredient(String cocktailIngredient) {
        this.cocktailIngredient = cocktailIngredient;
    }

    public String getCocktailAbvNum() {
        return cocktailAbvNum;
    }

    public void setCocktailAbvNum(String cocktailAbvNum) {
        this.cocktailAbvNum = cocktailAbvNum;
    }

    public String getCocktailImageUri() {
        return cocktailImageUri;
    }

    public void setCocktailImageUri(String cocktailImageUri) {
        this.cocktailImageUri = cocktailImageUri;
    }
}
