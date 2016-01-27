package com.mellocastanho.easytraining;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;


public class NewTrainingActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_training);
    }

    public void startNewExerciseActivity(View v){
        startActivity(new Intent(NewTrainingActivity.this, NewExerciseActivity.class));
    }

}
