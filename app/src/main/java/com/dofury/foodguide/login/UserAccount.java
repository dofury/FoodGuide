package com.dofury.foodguide.login;

public class UserAccount {
    private String idToken;
    private String email;
    private String pw;
    private String nickname;

    public UserAccount() {
        // 파이어베이스는 빈 생성자나 혹은 다른 생성자를 미리 만들어 놔야함
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
