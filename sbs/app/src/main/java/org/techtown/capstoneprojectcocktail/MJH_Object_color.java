package org.techtown.capstoneprojectcocktail;

public class MJH_Object_color {
    float rgb_red = 0;
    float rgb_green = 0;
    float rgb_blue = 0;

    // cmyk
    float cmyk_cyan = 0;
    float cmyk_magenta = 0;
    float cmyk_yellow = 0;
    float cmyk_black_key = 0;


    public MJH_Object_color(float red, float green, float blue){
        set_color_by_rgb(red, green, blue);
    }

    // rgb 값으로 색 셋팅
    public void set_color_by_rgb(float _red, float _green, float _blue){

        //System.out.println("셋팅 콜");
        this.rgb_red = _red;
        this.rgb_green = _green;
        this.rgb_blue = _blue;

        float r_255 = (float)(this.rgb_red / 255);
        float g_255 = (float)(this.rgb_green / 255);
        float b_255 = (float)(this.rgb_blue / 255);

        //System.out.println("r_255:"+r_255);
        float max_buffer = 0;
        max_buffer = Math.max(r_255, g_255);
        this.cmyk_black_key = 1 - Math.max(max_buffer, b_255);

        float compute_buffer = 0;
        this.cmyk_cyan =  (1 - r_255 - this.cmyk_black_key) / (1 - this.cmyk_black_key);
        this.cmyk_magenta =  (1 - g_255 - this.cmyk_black_key) / (1 - this.cmyk_black_key);
        this.cmyk_yellow =  (1 - b_255 - this.cmyk_black_key) / (1 - this.cmyk_black_key);
    }

    // cmyk 값으로 색 셋팅
    public void set_color_by_cmyk(float _cyan, float _magenta, float _yellow, float _black_key){
        this.cmyk_cyan = _cyan;
        this.cmyk_magenta = _magenta;
        this.cmyk_yellow = _yellow;
        this.cmyk_black_key = _black_key;

        this.rgb_red = (int)(255 * (1 - this.cmyk_cyan) * (1 - this.cmyk_black_key));
        this.rgb_green = (int)(255 * (1 - this.cmyk_magenta) * (1 - this.cmyk_black_key));
        this.rgb_blue = (int)(255 * (1 - this.cmyk_yellow) * (1 - this.cmyk_black_key));
    }

    public long get_android_color_type(){

        int red_int = (int)rgb_red;
        int green_int = (int)rgb_green;
        int blue_int = (int)rgb_blue;

        String red_s = String.format("0x%08X", red_int);
        String red_g = String.format("0x%08X", green_int);
        String red_b = String.format("0x%08X", blue_int);

        StringBuilder android_color_builder = new StringBuilder("0xEF");
        android_color_builder.append(red_s.substring(8));
        android_color_builder.append(red_g.substring(8));
        android_color_builder.append(red_b.substring(8));

        String android_color = android_color_builder.toString();

        return Long.parseLong(android_color);
    }
}
