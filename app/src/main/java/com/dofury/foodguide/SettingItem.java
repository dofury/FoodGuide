package com.dofury.foodguide;

public class SettingItem {
    private String title;
    private String content;
    private int image;

    SettingItem(){}

    SettingItem(String title, String content, int image)
    {
        this.title = title;
        this.content = content;
        this.image = image;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
