package org.techtown.capstoneprojectcocktail;

public class MJH_Object_ingredient { // 겟셋, 추상화 시간관계상 구현 안할 예정

    String name;
    String type;

    int id = 0;

    float abv = 0;

    double sugar = 0;
    double sour = 0;
    double salty = 0;
    double bitter = 0;
    double hot = 0;

    float specific_gravity = 0;

    MJH_Object_color my_color;


    String flavour; // 스트링한개로 넣고 파싱할지 이런식으로 배열로 할지는 논의 필요
    float alpha = 0;
    float muddy = 0;



    double volForFlavour;

    public MJH_Object_ingredient(){}
    public MJH_Object_ingredient(String _name, String _type, String _flavour, double _vol){
        name = _name;
        type = _type;
        flavour = _flavour;
        volForFlavour = _vol;
    }

}
