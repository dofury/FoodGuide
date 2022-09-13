package com.dofury.foodguide;

import android.os.Parcel;
import android.os.Parcelable;

import com.dofury.foodguide.inform.FoodInform;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Food implements Parcelable, Comparable<Food> {

    private String id;
    private String name;
    private String image;
    private String like;
    private FoodInform foodInform;
    private String comment;
    private int rank;


    public Food(String id, String name, String image){
        this.id = id;
        this.image = image;
        this.name = name;

    }
    public Food(String like) {
        this.like = like;
    }

    public Food() {
    }

    public Food(String id, String name, String image, FoodInform foodInform, String comment, String like){
        this.id = id;
        this.image = image;
        this.name = name;
        this.foodInform = foodInform;
        this.comment = comment;
        this.like = like;
    }

    protected Food(Parcel in) {
        id = in.readString();
        name = in.readString();
        image = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public FoodInform getFoodInform() {
        return foodInform;
    }

    public void setFoodInform(FoodInform foodInform) {
        this.foodInform = foodInform;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public static final Creator<Food> CREATOR = new Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel in) {
            return new Food(in);
        }

        @Override
        public Food[] newArray(int size) {
            return new Food[size];
        }
    };


    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.image);

    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public int compareTo(Food o) {
        List<String> list = new Gson().fromJson(this.like, new TypeToken<List<String>>(){}.getType());
        List<String> list2 = new Gson().fromJson(o.getLike(), new TypeToken<List<String>>(){}.getType());
        if(list.size() > list2.size()) {
            return -1;
        } else if(list.size() < list2.size()) {
            return 1;
        }

        return 0;

    }
}
