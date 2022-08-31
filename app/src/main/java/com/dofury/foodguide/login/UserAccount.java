package com.dofury.foodguide.login;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class UserAccount {
    private String idToken;
    private String email;
    private String nickname;

    private static UserAccount singleton = new UserAccount();

    private UserAccount() {
        // 파이어베이스는 빈 생성자나 혹은 다른 생성자를 미리 만들어 놔야함
    }

    public static UserAccount getInstance() {
        return singleton;
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("idToken", idToken);
        result.put("email", email);
        result.put("nickname", nickname);

        return result;
    }

}
