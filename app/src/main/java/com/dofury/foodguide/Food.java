package com.dofury.foodguide;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Food implements Parcelable {

    private String id;
    private String name;
    private int image;
    private String comment;

    public Food(String id,String name,int image){
        this.id = id;
        this.image = image;
        this.name = name;

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
