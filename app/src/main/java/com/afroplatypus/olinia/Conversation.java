package com.afroplatypus.olinia;

import java.util.HashMap;


public class Conversation {
    HashMap<String, Message> messages;
    private String key;
    private String user;
    private String expert;

    public Conversation() {
        messages = new HashMap<>();
    }

    public Conversation(String key, String user, String expert) {
        messages = new HashMap<>();
        this.key = key;
        this.user = user;
        this.expert = expert;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getExpert() {
        return expert;
    }

    public void setExpert(String expert) {
        this.expert = expert;
    }
}
