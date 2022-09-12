package com.dofury.foodguide.inform;

import java.util.ArrayList;

public class FoodInform {
    private String intro;
    private String origin;
    private String recipes;

    public FoodInform(){}

    public FoodInform(String intro, String origin, String recipes) {
        this.intro = intro;
        this.origin = origin;
        this.recipes = recipes;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getRecipes() {
        return recipes;
    }

    public void setRecipes(String recipes) {
        this.recipes = recipes;
    }
}
