package com.mellocastanho.easytraining;

/**
 * Created by castanho on 11/02/16.
 */

public class Training {

    private int _id;
    private String name;

    public Training() {
    }

    public Training (int _id, String name) {
        this._id = _id;
        this.name = name;
    }

    public Training (String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getID() {
        return this._id;
    }

    public void setID(int _id) {
        this._id = _id;
    }

}
