package org.techtown.capstoneprojectcocktail;

import java.util.ArrayList;

public class MJH_Object_simulator {


    int iceType = 0;
    int glassType = 0;
    int totalStep = 0; // 전체 스텝 갯수
    int inGlassStep = 0; // 현재 글래스에 담겨있는 스텝(0 이면 암것도 없는것)
    int isGradient = 0;
    ArrayList<MJH_Object_cocktail> simulatorStep  = new ArrayList<MJH_Object_cocktail>(); // 칵테일 시뮬 각 스텝에 대한 객체 리스트
    ArrayList<MJH_Object_ingredient> muddleList  = new ArrayList<MJH_Object_ingredient>(); // 머들링이 된 재료 목록

    public MJH_Object_simulator(int _glassType, int _iceType){ // step1: 잔, 얼음타입 선택
        this.iceType = _iceType; // 0=
        // 없음, 1=박스얼음, 2=원형
        this.glassType = _glassType; // 0=하이볼, 1=콜린스잔, 2=락글라스, 3=마티니, 4=허리케인?
    }

    //빌드 스터 셰이크 푸어 롤링 (스탭번호, 재료로 사용할 스탭의 갯수, 재료로 사용할 스탭들의 인덱스, 재료의 갯수, 재료, 재료의 양)
    public void addStepBuildings(int stepNum, ArrayList<Integer> associateStep, ArrayList<MJH_Object_ingredient> inputIngredient, ArrayList<Float> amount, boolean isIn){
        // (스탭번호, 재료로 사용할 스탭의 갯수, 재료로 사용할 스탭들의 인덱스, 재료의 갯수, 재료, 재료의 양)
        this.simulatorStep.add(calcKindBuild(stepNum, associateStep, inputIngredient, amount));

        // 현재 글래스에 담긴 음료에 대한 설정
        if(isIn == true){
            for(MJH_Object_cocktail i : simulatorStep){
                if(i.isInGlass == true)
                    i.setInGlass(false);
            }
            this.simulatorStep.get(stepNum - 1).setInGlass(true);
            inGlassStep = stepNum; //stepNum은 1부터 시작
        }
        totalStep++;
    }
    //+
    public MJH_Object_cocktail calcKindBuild(int stepNum, ArrayList<Integer> associateStep, ArrayList<MJH_Object_ingredient> inputIngredient, ArrayList<Float> inputAmount){

        MJH_Object_cocktail cocktailBuffer = new MJH_Object_cocktail();

        try { // 기존 스탭을 이용해 칵테일을 만들때
            if(associateStep.size() > 0)
                cocktailBuffer = stepClone(this.simulatorStep.get(associateStep.get(0) - 1));
            if(associateStep.size() > 1){ // 여러 단계를 사용할때
                for(int i = 1; i < associateStep.size(); i++){
                    //도수계산
                    cocktailBuffer.totalAbv = calcAbv(cocktailBuffer.totalVolume, cocktailBuffer.totalAbv,
                            this.simulatorStep.get(associateStep.get(i) - 1).totalVolume, this.simulatorStep.get(associateStep.get(i) - 1).totalAbv);

                    //비중계산
                    cocktailBuffer.specificGravity.set(0, calcSpecificGravity(cocktailBuffer.totalVolume, this.simulatorStep.get(associateStep.get(i) - 1).totalVolume,
                            cocktailBuffer.specificGravity.get(0), this.simulatorStep.get(associateStep.get(i) - 1).specificGravity.get(0)));
                    //색 변경
                    float a1 = cocktailBuffer.alpha;
                    float a2 = this.simulatorStep.get(associateStep.get(i) - 1).alpha;

                    if(a1 < 1)
                        a1 = 1;
                    if(a2 < 1)
                        a2 = 1;
                    cocktailBuffer.isColor.set(0, changeColor(cocktailBuffer.isColor.get(0), this.simulatorStep.get(associateStep.get(i) - 1).isColor.get(0) , cocktailBuffer.totalVolume*a1,
                            this.simulatorStep.get(associateStep.get(i) - 1).totalVolume*a2));

                    cocktailBuffer.alpha = (cocktailBuffer.totalVolume*cocktailBuffer.alpha + this.simulatorStep.get(associateStep.get(i) - 1).totalVolume*this.simulatorStep.get(associateStep.get(i) - 1).alpha)
                            / (cocktailBuffer.totalVolume + this.simulatorStep.get(associateStep.get(i) - 1).totalVolume);
                    //량 추가
                    cocktailBuffer.totalVolume = cocktailBuffer.totalVolume + this.simulatorStep.get(associateStep.get(i) - 1).totalVolume;

                    //노희상 맛표현
                    cocktailBuffer.sugar = cocktailBuffer.sugar + this.simulatorStep.get(associateStep.get(i) - 1).sugar;
                    cocktailBuffer.sour = cocktailBuffer.sour + this.simulatorStep.get(associateStep.get(i) - 1).sour;
                    cocktailBuffer.salty = cocktailBuffer.salty + this.simulatorStep.get(associateStep.get(i) - 1).salty;
                    cocktailBuffer.bitter = cocktailBuffer.bitter + this.simulatorStep.get(associateStep.get(i) - 1).bitter;
                    cocktailBuffer.hot = cocktailBuffer.hot + this.simulatorStep.get(associateStep.get(i) - 1).hot;

                    //노희상 향 표현
                    //String _name = this.simulatorStep.get(associateStep.get(i) - 1).ingredListForFlavour.
                    for(int j = 0; j < this.simulatorStep.get(associateStep.get(i) - 1).ingredListForFlavour.size(); j++){
                        String _name = this.simulatorStep.get(associateStep.get(i) - 1).ingredListForFlavour.get(j).name;
                        String _type = this.simulatorStep.get(associateStep.get(i) - 1).ingredListForFlavour.get(j).type;
                        String _flavour = this.simulatorStep.get(associateStep.get(i) - 1).ingredListForFlavour.get(j).flavour;
                        double _vol = this.simulatorStep.get(associateStep.get(i) - 1).ingredListForFlavour.get(j).volForFlavour;
                        cocktailBuffer.ingredListForFlavour.add(new MJH_Object_ingredient(_name, _type, _flavour, _vol));
                    }
                }
            }

            //재료들 이용하여 혼합
            for(int i = 0; i < inputIngredient.size(); i++){

                //도수계산
                cocktailBuffer.totalAbv = calcAbv(cocktailBuffer.totalVolume, cocktailBuffer.totalAbv,
                        inputAmount.get(i), inputIngredient.get(i).abv);
                cocktailBuffer.eachVolume.set(0, cocktailBuffer.totalAbv);

                //비중계산
                cocktailBuffer.specificGravity.set(0, calcSpecificGravity(cocktailBuffer.totalVolume, inputAmount.get(i),
                        cocktailBuffer.specificGravity.get(0), inputIngredient.get(i).specific_gravity));

                //색 변경
                if (associateStep.size() == 0 && i == 0){
                    cocktailBuffer.isColor.set(0, new MJH_Object_color(inputIngredient.get(i).my_color.rgb_red, inputIngredient.get(i).my_color.rgb_green,
                            inputIngredient.get(i).my_color.rgb_blue));

                    cocktailBuffer.alpha = inputIngredient.get(i).alpha;
                }
                else{
                    float a1 = cocktailBuffer.alpha;
                    float a2 = inputIngredient.get(i).alpha;

                    if(a1 < 1)
                        a1 = 1;
                    if(a2 < 1)
                        a2 = 1;
                    cocktailBuffer.isColor.set(0, changeColor(cocktailBuffer.isColor.get(0), inputIngredient.get(i).my_color, cocktailBuffer.totalVolume*a1,
                            inputAmount.get(i)*a2));
                    cocktailBuffer.alpha = (cocktailBuffer.totalVolume*cocktailBuffer.alpha + inputAmount.get(i)*inputIngredient.get(i).alpha) / (cocktailBuffer.totalVolume + inputAmount.get(i));
                }




                //량 추가
                cocktailBuffer.totalVolume = cocktailBuffer.totalVolume + inputAmount.get(i);
                cocktailBuffer.eachVolume.set(0, cocktailBuffer.totalVolume);

                //노희상 맛표현
                cocktailBuffer.sugar = cocktailBuffer.sugar + inputIngredient.get(i).sugar * inputAmount.get(i);
                cocktailBuffer.sour = cocktailBuffer.sour + inputIngredient.get(i).sour * inputAmount.get(i);
                cocktailBuffer.salty = cocktailBuffer.salty + inputIngredient.get(i).salty * inputAmount.get(i);
                cocktailBuffer.bitter = cocktailBuffer.bitter + inputIngredient.get(i).bitter * inputAmount.get(i);
                cocktailBuffer.hot = cocktailBuffer.hot + inputIngredient.get(i).hot * inputAmount.get(i);

                //노희상 향 표현
                cocktailBuffer.ingredListForFlavour.add(new MJH_Object_ingredient(inputIngredient.get(i).name, inputIngredient.get(i).type, inputIngredient.get(i).flavour, (double)inputAmount.get(i)));
            }

        }catch (Exception e){e.printStackTrace();}

        cocktailBuffer.totalSpecificGravity = cocktailBuffer.specificGravity.get(0);
        cocktailBuffer.eachAbv.set(0, cocktailBuffer.totalAbv);
        cocktailBuffer.isLayering = 1;

        cocktailBuffer.alphaList.clear();
        cocktailBuffer.alphaList.add( cocktailBuffer.alpha);
        return cocktailBuffer;
    }
    //+
    public MJH_Object_color addColor(MJH_Object_color color_one, MJH_Object_color color_two, float vol_one, float vol_two){
        float result_red;
        float result_blue;
        float result_green;

        result_red = ((color_one.rgb_red * vol_one) + (color_two.rgb_red * vol_two)) / (vol_one + vol_two);
        result_green = ((color_one.rgb_green * vol_one) + (color_two.rgb_green * vol_two)) / (vol_one + vol_two);
        result_blue = ((color_one.rgb_blue * vol_one) + (color_two.rgb_blue* vol_two)) / (vol_one + vol_two);

        MJH_Object_color color_result = new MJH_Object_color(result_red, result_green, result_blue);
        return color_result;
    }
    //+
    public MJH_Object_color changeColor( MJH_Object_color a_color, MJH_Object_color b_color, float a_volume, float b_volume ){
        MJH_Object_color color_buffer;
        color_buffer = addColor(a_color, b_color, a_volume, b_volume);
        return color_buffer;
    }




    public void addStepLayering(int stepNum, int associateStep, MJH_Object_ingredient inputIngredient, float inputAmount, int layeringType){
        int returnValue = 0;
        MJH_Object_cocktail cocktailBufferLayering = new MJH_Object_cocktail();
        int beforeInGlassStep = inGlassStep; // 이번 스텝을 하기전에 글래스에 있던 스텝

        if(beforeInGlassStep != 0){ // 현재 글래스에 담긴 스텝이 있을 경우
            try{
                calcKindLayering(associateStep, inputIngredient, inputAmount, layeringType); // 레이어링 작업
                inGlassStep = stepNum; // 이번 스텝이 이제 글래스에 있음 (레이어링은 글래스에 해야함으로)
                // 현재 글래스에 담긴 음료를 이번 스텝으로 설정
                for(MJH_Object_cocktail i : simulatorStep){
                    if(i.isInGlass == true)
                        i.setInGlass(false);
                }
                this.simulatorStep.get(stepNum - 1).setInGlass(true);
                inGlassStep = stepNum; //stepNum은 1부터 시작
            }catch (Exception e){e.printStackTrace(); }
        }
        else{ // 현재 글래에 담긴 스텝이 없을 경우
            //예외처리 해주기
        }
        totalStep++;

    }
    //+
    public void calcKindLayering(int associateStep, MJH_Object_ingredient inputIngredient, float inputAmount, int layeringType){
        //int layeringType: 1->그라디언트
        if(layeringType == 1)
            isGradient = 1;

        try{
            MJH_Object_cocktail buf = stepClone(this.simulatorStep.get(inGlassStep - 1));
            this.simulatorStep.add(buf);// 현재 글래스에 담겨있는 칵테일을 복사
        }catch (Exception e){e.printStackTrace(); }
        int layerNumBuffer = simulatorStep.get(inGlassStep - 1).isLayering; // 현재 글래스에 들어 있는 층 계산
        simulatorStep.get(inGlassStep).isLayering++;
        inGlassStep++;

        if(associateStep == 0){ // 재료로 레이어링 할 때
            //토탈 abv, 토탈 비중, 토탈 부피 갱신
            simulatorStep.get(inGlassStep - 1).totalAbv = calcAbv(simulatorStep.get(inGlassStep - 1).totalVolume, simulatorStep.get(inGlassStep - 1).totalAbv,
                    inputAmount, inputIngredient.abv);
            simulatorStep.get(inGlassStep - 1).totalSpecificGravity = calcSpecificGravity(simulatorStep.get(inGlassStep - 1).totalVolume , inputAmount,
                    simulatorStep.get(inGlassStep - 1).totalSpecificGravity , inputIngredient.specific_gravity);
            simulatorStep.get(inGlassStep - 1).totalVolume = simulatorStep.get(inGlassStep - 1).totalVolume + inputAmount;

            // 재료 부피, 알콜도수, 비중, 색 추가
            simulatorStep.get(inGlassStep - 1).getEachVolume().add(inputAmount);
            simulatorStep.get(inGlassStep - 1).getEachAbv().add(inputIngredient.abv);
            simulatorStep.get(inGlassStep - 1).getSpecificGravity().add(inputIngredient.specific_gravity);
            simulatorStep.get(inGlassStep - 1).getIs_color().add(inputIngredient.my_color);

            simulatorStep.get(inGlassStep - 1).alphaList.add(inputIngredient.alpha);

            //노희상 맛표현
            simulatorStep.get(inGlassStep - 1).sugar = simulatorStep.get(inGlassStep - 1).sugar + inputIngredient.sugar*inputAmount;
            simulatorStep.get(inGlassStep - 1).sour = simulatorStep.get(inGlassStep - 1).sour + inputIngredient.sour*inputAmount;
            simulatorStep.get(inGlassStep - 1).salty = simulatorStep.get(inGlassStep - 1).salty + inputIngredient.salty*inputAmount;
            simulatorStep.get(inGlassStep - 1).bitter = simulatorStep.get(inGlassStep - 1).bitter + inputIngredient.bitter*inputAmount;
            simulatorStep.get(inGlassStep - 1).hot = simulatorStep.get(inGlassStep - 1).hot + inputIngredient.hot*inputAmount;

            //노희상
            simulatorStep.get(inGlassStep - 1).ingredListForFlavour.add(new MJH_Object_ingredient(inputIngredient.name, inputIngredient.type, inputIngredient.flavour, (double)inputAmount));
        }

        if(associateStep != 0){ // 연관스탭로 레이어링 할 때

            //토탈 abv, 토탈 비중, 토탈 부피 갱신
            simulatorStep.get(inGlassStep - 1).totalAbv = calcAbv(simulatorStep.get(inGlassStep - 1).totalVolume, simulatorStep.get(inGlassStep - 1).totalAbv,
                    simulatorStep.get(associateStep  - 1).totalVolume, simulatorStep.get(associateStep  - 1).totalAbv);
            simulatorStep.get(inGlassStep - 1).totalSpecificGravity = calcSpecificGravity(simulatorStep.get(inGlassStep - 1).totalVolume ,  simulatorStep.get(associateStep  - 1).totalVolume,
                    simulatorStep.get(inGlassStep - 1).totalSpecificGravity , simulatorStep.get(associateStep  - 1).getSpecificGravity().get(0));
            simulatorStep.get(inGlassStep - 1).totalVolume = simulatorStep.get(inGlassStep - 1).totalVolume + simulatorStep.get(associateStep  - 1).totalVolume;

            // 재료 부피, 알콜도수, 비중, 색 추가
            simulatorStep.get(inGlassStep - 1).getEachVolume().add(simulatorStep.get(associateStep  - 1).totalVolume);
            simulatorStep.get(inGlassStep - 1).getEachAbv().add(simulatorStep.get(associateStep  - 1).totalAbv);
            simulatorStep.get(inGlassStep - 1).getSpecificGravity().add(simulatorStep.get(associateStep  - 1).getSpecificGravity().get(0));
            simulatorStep.get(inGlassStep - 1).getIs_color().add(simulatorStep.get(associateStep  - 1).getIs_color().get(0));

            simulatorStep.get(inGlassStep - 1).alphaList.add(simulatorStep.get(associateStep  - 1).alpha);

            //노희상 맛표현
            simulatorStep.get(inGlassStep - 1).sugar = simulatorStep.get(inGlassStep - 1).sugar + simulatorStep.get(associateStep  - 1).sugar*simulatorStep.get(associateStep  - 1).totalVolume;
            simulatorStep.get(inGlassStep - 1).sour = simulatorStep.get(inGlassStep - 1).sour + simulatorStep.get(associateStep  - 1).sour*simulatorStep.get(associateStep  - 1).totalVolume;
            simulatorStep.get(inGlassStep - 1).salty = simulatorStep.get(inGlassStep - 1).salty + simulatorStep.get(associateStep  - 1).salty*simulatorStep.get(associateStep  - 1).totalVolume;
            simulatorStep.get(inGlassStep - 1).bitter = simulatorStep.get(inGlassStep - 1).bitter + simulatorStep.get(associateStep  - 1).bitter*simulatorStep.get(associateStep  - 1).totalVolume;
            simulatorStep.get(inGlassStep - 1).hot = simulatorStep.get(inGlassStep - 1).hot + simulatorStep.get(associateStep  - 1).hot*simulatorStep.get(associateStep  - 1).totalVolume;

            //노희상 향표현
            for(int j = 0; j < this.simulatorStep.get(associateStep - 1).ingredListForFlavour.size(); j++){
                String _name = this.simulatorStep.get(associateStep - 1).ingredListForFlavour.get(j).name;
                String _type = this.simulatorStep.get(associateStep - 1).ingredListForFlavour.get(j).type;
                String _flavour = this.simulatorStep.get(associateStep - 1).ingredListForFlavour.get(j).flavour;
                double _vol = this.simulatorStep.get(associateStep - 1).ingredListForFlavour.get(j).volForFlavour;
                simulatorStep.get(inGlassStep - 1).ingredListForFlavour.add(new MJH_Object_ingredient(_name, _type, _flavour, _vol));
            }
        }
    }




    public float calcAbv(float a_volume, float a_abv, float b_volume, float b_abv){
        return (a_volume * a_abv + b_volume * b_abv)
                / (a_volume + b_volume);
    }

    public float calcSpecificGravity(float volume_origin, float volume_add, float specific_gravity_origin, float specific_gravity_add){
        float weight_origin = volume_origin * specific_gravity_origin;
        float weight_add = volume_add * specific_gravity_add;
        return (weight_origin + weight_add) / (volume_origin + volume_add);
    }

    public MJH_Object_cocktail stepClone(MJH_Object_cocktail input){
        MJH_Object_cocktail n = new MJH_Object_cocktail();

        n.isInGlass = input.isInGlass;

        n.isLayering = input.isLayering;
        n.totalVolume = input.totalVolume;
        n.totalAbv = input.totalAbv;
        n.totalSpecificGravity = input.totalSpecificGravity;

        n.alpha = input.alpha;
        if(input.alphaList.size() == 0){
            n.alphaList.add(input.alpha);
        }
        else{
            for(int i = 0; i < input.alphaList.size(); i++){
                n.alphaList.add(input.alphaList.get(i));
            }
        }


        n.eachAbv.remove(0);
        n.eachVolume.remove(0);
        n.specificGravity.remove(0);
        n.isColor.remove(0);


        for(int i = 0; i <input.eachAbv.size(); i++){
            n.eachAbv.add(input.eachAbv.get(i));
            n.eachVolume.add(input.eachVolume.get(i));
            n.specificGravity.add(input.specificGravity.get(i));
            n.isColor.add(new MJH_Object_color(input.isColor.get(i).rgb_red, input.isColor.get(i).rgb_green, input.isColor.get(i).rgb_blue));
        }

        //노희상 맛표현
        n.sugar = input.sugar;
        n.sour = input.sour;
        n.salty = input.salty;
        n.bitter = input.bitter;
        n.hot = input.hot;

        // 노희상 향표현
        for(int i =0; i <input.ingredListForFlavour.size(); i ++){
            String _name = input.ingredListForFlavour.get(i).name;
            String _type = input.ingredListForFlavour.get(i).type;
            String _flavour = input.ingredListForFlavour.get(i).flavour;
            double _vol = input.ingredListForFlavour.get(i).volForFlavour;
            n.ingredListForFlavour.add(new MJH_Object_ingredient(_name, _type, _flavour, _vol));
        }
        return n;
    }
}

/*
public MJH_Object_ingredient simul_step_to_ingredient(MJH_Object_cocktail s){
    MJH_Object_ingredient retrun_ingre = new MJH_Object_ingredient(s.specific_gravity[0], s.total_abv, s.sugar[0], s.sour[0], s.salty[0], s.bitter[0], s.is_color[0]);
    return retrun_ingre;
}
*/