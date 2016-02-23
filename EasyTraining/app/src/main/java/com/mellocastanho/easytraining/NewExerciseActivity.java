package com.mellocastanho.easytraining;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

public class NewExerciseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_exercise);

        /*
        -----------------------------------------------------------------------
        Set and change Sequences Text when the progress of the seek bar changes
        -----------------------------------------------------------------------
        */
        final SeekBar nOfSequencesSeekBar = (SeekBar) findViewById(R.id.nOfSequencesSeekBar);
        final TextView nOfSequences = (TextView) findViewById(R.id.nOfSequences);

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
        final SeekBar nOfRepetitionsSeekBar = (SeekBar) findViewById(R.id.nOfRepetitionsSeekBar);
        final TextView nOfRepetitions = (TextView) findViewById(R.id.nOfRepetitions);

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

        //Set default breakLengthSecondsSpinner
        Spinner breakLengthSecondsSpinner = (Spinner) findViewById(R.id.breakLengthSecondsSpinner);
        breakLengthSecondsSpinner.setSelection(30);

        /*
        ----------------------------------------------------------------
        Change speed show text when the progress of the seek bar changes
        ----------------------------------------------------------------
        */
        final SeekBar speedSeekBar = (SeekBar) findViewById(R.id.speedSeekBar);
        final TextView speedShowTextView = (TextView) findViewById(R.id.speedShowTextView);
        speedSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress < 11) {
                    speedShowTextView.setText(getString(R.string.slow));
                } else if (progress < 21) {
                    speedShowTextView.setText(getString(R.string.average));
                } else {
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
    }

    /*
    ---------------------------------
    Set checkButton onClick Actions
    ---------------------------------
     */

    public void checkButtonDoIt(View view){
        Exercise exercise = addExercise();
//        lookupExercise();
        answerIntent(exercise);
    }

    public void cancelButtonDoIt(View view){
        setResult(Activity.RESULT_CANCELED, null);
        finish();
    }

    public void answerIntent(Exercise exercise) {
        Intent data = new Intent();
        data.putExtra("ExerciseID", exercise.getID());
        setResult(Activity.RESULT_OK, data);
        finish();
    }

    /*
    -------------------------
    Handle Exercises Database
    -------------------------
    */

    //Add Exercise to Database
    public Exercise addExercise () {

        //Get Training ID
        Intent data = getIntent();
        int training_id = data.getBundleExtra("TrainingBundle").getInt("TrainingID");

        //Get Name
        final EditText nameEditText = (EditText) findViewById(R.id.nameEditText);
        String name = String.valueOf(nameEditText.getText());

        //Get Type
        final Spinner typeSpinner = (Spinner) findViewById(R.id.typeSpinner);
        String type = String.valueOf(typeSpinner.getSelectedItem());

        //Get Number of Sequences
        final SeekBar nOfSequencesSeekbar = (SeekBar) findViewById(R.id.nOfSequencesSeekBar);
        int nSeq = nOfSequencesSeekbar.getProgress();

        //Get Number of Repetitions
        final SeekBar nOfRepetitionsSeekBar = (SeekBar) findViewById(R.id.nOfRepetitionsSeekBar);
        int nRep = nOfRepetitionsSeekBar.getProgress();

        //Get Break Length
        final Spinner breakLengthMinutesSpinner = (Spinner) findViewById(R.id.breakLengthMinutesSpinner);
        int breakLengthMinutes = Integer.parseInt(breakLengthMinutesSpinner.getSelectedItem().toString());

        final Spinner breakLengthSecondsSpinner = (Spinner) findViewById(R.id.breakLengthSecondsSpinner);
        int breakLengthSeconds = Integer.parseInt(breakLengthSecondsSpinner.getSelectedItem().toString());

        //Convert to Seconds
        int breakLength = breakLengthMinutes * 60 + breakLengthSeconds;

        //Get Speed
        final SeekBar speedSeekBar = (SeekBar) findViewById(R.id.speedSeekBar);
        int speed = speedSeekBar.getProgress();

        //Save to DB
        deleteExercise(name);
        Exercise exercise = new Exercise(training_id, name, type, nSeq, nRep, breakLength, speed);
        EasyTrainingDBHandler dbHandler = new EasyTrainingDBHandler(this, null, null, 1);
        dbHandler.addExercise(exercise);
        exercise = dbHandler.getLastExercise();
        dbHandler.close();

        return exercise;
    }

    //Delete Exercise from Database
    public boolean deleteExercise(String exercise) {

        boolean result = false;

        EasyTrainingDBHandler dbHandler = new EasyTrainingDBHandler(this, null, null, 1);
        result = dbHandler.deleteExercise(exercise);
        dbHandler.close();

        return result;
    }

}