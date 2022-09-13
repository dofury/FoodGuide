package com.dofury.foodguide;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Comparator;

public class Food implements Parcelable, Comparable<Food> {

    private String id;
    private String name;
    private int image;
    private String like;
    private FoodInform foodInform;
    private String comment;


    public Food(String id, String name, int image){
        this.id = id;
        this.image = image;
        this.name = name;

    }
    public Food(String like) {
        this.like = like;
    }
    Food() {}
    public Food(String id,String name,int image,FoodInform foodInform,String comment,String like){
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
        image = in.readInt();
    }

    public int getLike() {
        return likes;
    }

    public void setLike(int like) {
        this.likes = like;
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

    public String getComment() {
    public void setLike(String like) {
        this.like = like;
    }

    public String getFoodComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    public String getComment() { return this.comment;}
    public String getLike() {
        return like;
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



    @Override
    public int compareTo(Food o) {
        if(this.likes > o.getLike()) {
            return -1;
        } else if(this.likes < o.getLike()) {
            return 1;
        }

        return 0;

    }
}
