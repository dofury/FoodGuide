package com.dofury.foodguide.login;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class UserAccount {
    private String idToken;
    private String email;
    private String nickname;
    private String profile;
    private String profileM;
    private String foodLogs;

    public String getFoodLogs() {
        return foodLogs;
    }

    public void setFoodLogs(String foodLogs) {
        this.foodLogs = foodLogs;
    }


    private static final UserAccount singleton = new UserAccount();

    private UserAccount() {
        // 파이어베이스는 빈 생성자나 혹은 다른 생성자를 미리 만들어 놔야함
    }

    public static UserAccount getInstance() {
        return singleton;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getProfileM() {
        return profileM;
    }

    public void setProfileM(String profileM) {
        this.profileM = profileM;
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
        result.put("profile", profile);
        result.put("profileM", profileM);
        result.put("foodLogs", foodLogs);

        return result;
    }

    @Override
    public String toString() {
        return "UserAccount{" +
                "idToken='" + idToken + '\'' +
                ", email='" + email + '\'' +
                ", nickname='" + nickname + '\'' +
                ", profile='" + profile + '\'' +
                ", profileM='" + profileM + '\'' +
                ", foodLogs='" + foodLogs + '\'' +
                '}';
    }
}
