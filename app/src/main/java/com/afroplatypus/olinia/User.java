package com.afroplatypus.olinia;

/**
 * Created by omarsaucedo on 25/03/17.
 */

public class User {

    private int id;
    private String name;

    public User(String name) {
        //TODO id = getIDFromFirebase();
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
