package com.dofury.foodguide.diary;

import java.util.ArrayList;
import java.util.Date;

public class PostInfo {
    private String id;
    private String contents;
    private String image;
    private String date;
    private Float rating;

    public PostInfo(String id, String contents, String image,String date,Float rating)
    {
        this.id = id;
        this.contents = contents;
        this.image = image;
        this.date = date;
        this.rating = rating;
    }
    public PostInfo() {}

    public String getDate() { return this.date; }
    public void setDate(String date) { this.date = date; }
    public String getTitle() { return this.id;}
    public String getContents() { return this.contents;}

    public String getId() {
        return this.id;
    }

    public Float getRating() {
        return this.rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public void setId(String id) { this.id = id;}
    public void setContents(String contents) { this.contents = contents;}
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
