package com.mellocastanho.easytraining;

/**
 * Created by castanho on 11/02/16.
 */

public class Exercise {

    private int _id;
    private int training_id;
    private String name;
    private String type;
    private int n_sequences;
    private int n_repetitions;
    private int break_length;
    private int speed;

    public Exercise(){
    }

    public Exercise(int training_id,
                    String name,
                    String type,
                    int n_sequences,
                    int n_repetitions,
                    int break_length,
                    int speed){

        this.training_id = training_id;
        this.name = name;
        this.type = type;
        this.n_sequences = n_sequences;
        this.n_repetitions = n_repetitions;
        this.break_length = break_length;
        this.speed = speed;
    }

    public int getID() { return this._id; }

    public void setID(int _id) { this._id = _id; }

    public int getTrainingID() { return this.training_id; }

    public void setTrainingID(int training_id) { this.training_id = training_id; }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getN_sequences() {
        return this.n_sequences;
    }

    public void setN_sequences(int n_sequences) {
        this.n_sequences = n_sequences;
    }

    public int getN_repetitions() {
        return this.n_repetitions;
    }

    public void setN_repetitions(int n_repetitions) {
        this.n_repetitions = n_repetitions;
    }

    public int getBreak_length() {
        return this.break_length;
    }

    public void setBreak_length(int break_length) {
        this.break_length = break_length;
    }

    public int getSpeed() {
        return this.speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

}

