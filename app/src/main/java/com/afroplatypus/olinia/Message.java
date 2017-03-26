package com.afroplatypus.olinia;

/**
 * Created by moy on 3/20/17.
 */

public class Message {
    private String id;
    private String body;
    private String receiver;
    private String sender;

    public Message() {
    }

    public Message(String body, String receiver, String sender) {
        this.body = body;
        this.receiver = receiver;
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

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
