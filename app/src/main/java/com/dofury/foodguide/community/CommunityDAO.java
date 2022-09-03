package com.dofury.foodguide.community;

public class CommunityDAO {
    String title;
    String uid;
    String nickname;
    String content;
    String likes;
    String image;
    String reply;
    Boolean delete;
    String data;
    String data_;
    String cUid;

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public CommunityDAO() {
    }

    public CommunityDAO(String title, String uid, String nickname, String content, String likes, String image, String reply, Boolean delete, String data, String data_, String cUid) {
        this.title = title;
        this.uid = uid;
        this.nickname = nickname;
        this.content = content;
        this.likes = likes;
        this.image = image;
        this.reply = reply;
        this.delete = delete;
        this.data = data;
        this.data_ = data_;
        this.cUid = cUid;
    }

    public String getcUid() {
        return cUid;
    }

    public void setcUid(String cUid) {
        this.cUid = cUid;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData_() {
        return data_;
    }

    public void setData_(String data_) {
        this.data_ = data_;
    }

    public String getTitle() {
        return title;
    }

    public Boolean getDelete() {
        return delete;
    }

    public void setDelete(Boolean delete) {
        this.delete = delete;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
