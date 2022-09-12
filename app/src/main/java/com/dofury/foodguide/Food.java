package com.dofury.foodguide;

import android.os.Parcel;
import android.os.Parcelable;

import com.dofury.foodguide.inform.FoodInform;

import java.util.ArrayList;

public class Food implements Parcelable {

    private String id;
    private String name;
    private int image;
    private String comment;
    private int likes;

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    private FoodInform foodInform;

    public Food(String id,String name,int image){
        this.id = id;
        this.image = image;
        this.name = name;

    }
    public Food(String id,String name,int image,FoodInform foodInform,int likes){
        this.id = id;
        this.image = image;
        this.name = name;
        this.foodInform = foodInform;

    }

    protected Food(Parcel in) {
        id = in.readString();
        name = in.readString();
        image = in.readInt();
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getId(){
        return id;
    }
    public void setId(String id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public int getImage(){
        return image;
    }

    public void setImage(int image){
        this.image = image;
    }

    public FoodInform getFoodInform() {
        return this.foodInform;
    }

    public void setFoodInform(FoodInform foodInform) {
        this.foodInform = foodInform;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.image);

    }
    @Override
    public int describeContents() {
        return 0;
    }
}
