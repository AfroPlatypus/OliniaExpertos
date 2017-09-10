package com.afroplatypus.oliniaExpertos;

import java.util.HashMap;


class Conversation {
    HashMap<String, Message> messages;
    private String key;
    private String user;
    private String expert;

    public Conversation() {
        messages = new HashMap<>();
    }

    Conversation(String user, String expert) {
        messages = new HashMap<>();
        this.user = user;
        this.expert = expert;
    }

    String getKey() {
        return key;
    }

    void setKey(String key) {
        this.key = key;
    }

    String getUser() {
        return user;
    }

    void setUser(String user) {
        this.user = user;
    }

    String getExpert() {
        return expert;
    }

    void setExpert(String expert) {
        this.expert = expert;
    }
}
