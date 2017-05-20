package com.afroplatypus.olinia;

import java.util.HashMap;


public class Conversation {
    HashMap<String, Message> messages;
    private String key;
    private String user1;
    private String user2;

    public Conversation() {
        messages = new HashMap<>();
    }

    public Conversation(String key, String user1, String user2) {
        messages = new HashMap<>();
        this.key = key;
        this.user1 = user1;
        this.user2 = user2;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUser1() {
        return user1;
    }

    public void setUser1(String user1) {
        this.user1 = user1;
    }

    public String getUser2() {
        return user2;
    }

    public void setUser2(String user2) {
        this.user2 = user2;
    }
}
