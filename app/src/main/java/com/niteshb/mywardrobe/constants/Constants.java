package com.niteshb.mywardrobe.constants;

import com.niteshb.mywardrobe.R;

public class Constants {
    public static long systemDateTime(){
        return System.currentTimeMillis();
    }

    public static int getCategoryImage(String category){
        switch (category.toLowerCase()){
            case "clothing":
                return R.drawable.clothing;
            case "personal appliances":
                return R.drawable.hair_style;
            case "accessories":
                return R.drawable.hair_style;
            case "makeup":
                return R.drawable.makeup_kit;
            case "skincare":
                return R.drawable.skin_care;
            case "bags":
                return R.drawable.bags;
            case "shoes":
                return R.drawable.shoes;
            case "my pairs":
                return R.drawable.clothing;
            default:
                return R.drawable.clothing;

        }
    }
}
