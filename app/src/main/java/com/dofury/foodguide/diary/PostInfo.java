package com.dofury.foodguide.diary;

public class PostInfo {
    String title;
    String contents;
    String image;

    public PostInfo(String title,String contents)
    {
        this.title = title;
        this.contents = contents;
        this.image = image;
    }

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
