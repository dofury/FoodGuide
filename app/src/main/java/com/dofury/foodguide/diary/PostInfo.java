package com.dofury.foodguide.diary;

import java.util.ArrayList;
import java.util.Date;

public class PostInfo {
    private String title;
    private String contents;
    private String image;
    private String date;

    public PostInfo(String title, String contents, String image,String date)
    {
        this.title = title;
        this.contents = contents;
        this.image = image;
        this.date = date;
    }
    public PostInfo() {}

    public String getDate() { return this.date; }
    public void setDate(String date) { this.date = date; }
    public String getTitle() { return this.title;}
    public String getContents() { return this.contents;}
    public void setTitle(String title) { this.title = title;}
    public void setContents(String contents) { this.contents = contents;}
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
