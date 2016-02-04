package com.mellocastanho.easytraining;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class MyTrainingsActivity extends ActionBarActivity {


    final String MY_EXERCISE_TITLE_KEY = "My Exercise Title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_trainings);
        /* MENU LOGO TRIAL
        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.drawable.logo);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        */


/*
------------------
Import Preferences
------------------
*/
//        Context context = MyTrainingsActivity.this;
//        SharedPreferences myPrefs = context.getSharedPreferences(getString(R.string.EASY_TRAINING_PREFS), Context.MODE_PRIVATE);
//
//        final TextView titleTextView = (TextView) findViewById(R.id.testTextView);
//        String titleString = myPrefs.getString(MY_EXERCISE_TITLE_KEY, "default value");
//        titleTextView.setText(titleString);


    }

    public void startNewTrainingActivity(View v){
        startActivity(new Intent(MyTrainingsActivity.this, NewTrainingActivity.class));
    }

}

