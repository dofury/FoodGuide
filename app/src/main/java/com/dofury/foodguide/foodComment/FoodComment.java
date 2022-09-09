package com.dofury.foodguide.foodComment;

public class FoodComment {
    private String uid;
    private String name;
    private String content;
    private String Date;

    public FoodComment() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    @Override
    public String toString() {
        return "FoodComment{" +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", Date='" + Date + '\'' +
                '}';
    }
}
