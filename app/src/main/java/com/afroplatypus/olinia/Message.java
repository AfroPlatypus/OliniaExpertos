package com.afroplatypus.olinia;


public class Message {
    private String id;
    private String body;
    private String sender;

    public Message() {
    }

    public Message(String body, String sender) {
        this.body = body;
        this.sender = sender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
