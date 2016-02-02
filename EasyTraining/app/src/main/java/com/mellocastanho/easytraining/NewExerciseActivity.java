package com.mellocastanho.easytraining;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;


public class NewExerciseActivity extends ActionBarActivity {

    final String MY_EXERCISE_TITLE_KEY = "My Exercise Title";
    final String MY_EXERCISE_TYPE_KEY = "My Exercise Type";
    final String MY_EXERCISE_N_SEQ_KEY = "My Exercise Num of Sequences";
    final String MY_EXERCISE_N_REP_KEY = "My Exercise Num of Repetitions";
    final String MY_EXERCISE_BREAK_LENGTH_KEY = "My Exercise Break Length";
    final String MY_EXERCISE_SPEED_KEY = "My Exercise Speed";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_exercise);

/*
-----------------------------------------------------------------------
Set and change Sequences Text when the progress of the seek bar changes
-----------------------------------------------------------------------
*/
        final SeekBar nOfSequencesSeekBar = (SeekBar)findViewById(R.id.nOfSequencesSeekBar);
        final TextView nOfSequences = (TextView)findViewById(R.id.nOfSequences);

        nOfSequences.setText(String.valueOf(nOfSequencesSeekBar.getProgress()));
        nOfSequencesSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                nOfSequences.setText(String.valueOf(progress));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


/*
--------------------------------------------------------------------------
Set and change Repetitions Text when the progress of the seek bar changes
--------------------------------------------------------------------------
*/
        final SeekBar nOfRepetitionsSeekBar = (SeekBar)findViewById(R.id.nOfRepetitionsSeekBar);
        final TextView nOfRepetitions = (TextView)findViewById(R.id.nOfRepetitions);

        nOfRepetitions.setText(String.valueOf(nOfRepetitionsSeekBar.getProgress()));
        nOfRepetitionsSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                nOfRepetitions.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

/*
--------------------------------------
Set default breakLengthSecondsSpinner
--------------------------------------
*/
        Spinner breakLengthSecondsSpinner = (Spinner)findViewById(R.id.breakLengthSecondsSpinner);
        breakLengthSecondsSpinner.setSelection(30);

        //Change speed show text when the progress of the seek bar changes
        final SeekBar speedSeekBar = (SeekBar)findViewById(R.id.speedSeekBar);
        final TextView speedShowTextView = (TextView)findViewById(R.id.speedShowTextView);
        speedSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress < 11) {
                    speedShowTextView.setText(getString(R.string.slow));
                }
                else if (progress < 21) {
                    speedShowTextView.setText(getString(R.string.average));
                }
                else{
                    speedShowTextView.setText(getString(R.string.fast));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


/*
-----------------
Set check button
-----------------
*/
        ImageButton checkImageButton = (ImageButton)findViewById(R.id.checkImageButton);

        checkImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context context = NewExerciseActivity.this;
                SharedPreferences myPrefs = context.getSharedPreferences(getString(R.string.EASY_TRAINING_PREFS), Context.MODE_PRIVATE);
                final SharedPreferences.Editor prefsEditor = myPrefs.edit();

                // Save title to Prefs
                final EditText titleEditText = (EditText)findViewById(R.id.titleEditText);
                String myExerciseTitle = String.valueOf(titleEditText.getText());
                prefsEditor.putString(MY_EXERCISE_TITLE_KEY, myExerciseTitle);

                // Save number of sequences to Prefs
                final SeekBar nOfSequencesSeekbar = (SeekBar)findViewById(R.id.nOfSequencesSeekBar);
                String myExerciseNSeq = String.valueOf(nOfSequencesSeekbar.getProgress());
                prefsEditor.putString(MY_EXERCISE_N_SEQ_KEY, myExerciseNSeq);

                // Save number of repetitions to Prefs
                final SeekBar nOfRepetitionsSeekBar = (SeekBar)findViewById(R.id.nOfRepetitionsSeekBar);
                String myExerciseNRep = String.valueOf(nOfRepetitionsSeekBar.getProgress());
                prefsEditor.putString(MY_EXERCISE_N_REP_KEY, myExerciseNRep);

                // Save break length converted to seconds to Prefs
                final Spinner breakLengthMinutesSpinner = (Spinner)findViewById(R.id.breakLengthMinutesSpinner);
                String myExerciseBreakLengthMinutes = breakLengthMinutesSpinner.getSelectedItem().toString();

                final Spinner breakLengthSecondsSpinner = (Spinner)findViewById(R.id.breakLengthSecondsSpinner);
                String myExerciseBreakLengthSeconds = breakLengthSecondsSpinner.getSelectedItem().toString();

                String myExerciseBreakLength = String.valueOf(Integer.parseInt(myExerciseBreakLengthMinutes)*60 + Integer.parseInt(myExerciseBreakLengthSeconds));

                prefsEditor.putString(MY_EXERCISE_BREAK_LENGTH_KEY, myExerciseBreakLength);

                // Save number of repetitions to Prefs
                final SeekBar speedSeekBar = (SeekBar)findViewById(R.id.speedSeekBar);
                String myExerciseSpeedSeekBar = String.valueOf(speedSeekBar.getProgress());
                prefsEditor.putString(MY_EXERCISE_SPEED_KEY, myExerciseSpeedSeekBar);



                prefsEditor.apply();

                //Test Prefs
                String keyToTest = MY_EXERCISE_SPEED_KEY;
                final TextView testPrefs = (TextView) findViewById(R.id.testPrefsText);
                String titleString = myPrefs.getString(keyToTest,"Nada a declarar");
                testPrefs.setText(titleString);

            }
        });


/*

        final EditText titleEditText = (EditText)findViewById(R.id.titleEditText);
        String myExerciseTitle = String.valueOf(titleEditText.getText());
        prefsEditor.putString(MY_EXERCISE_TITLE_KEY, myExerciseTitle);
*/




    }

    public void startNewTrainingActivity(View v) {
        //startActivity(new Intent(NewExerciseActivity.this, NewTrainingActivity.class));

        finish();
    }

}
