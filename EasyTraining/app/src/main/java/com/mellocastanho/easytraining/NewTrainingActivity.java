package com.mellocastanho.easytraining;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;


public class NewTrainingActivity extends AppCompatActivity {


    final int ADD_EXERCISE_REQUEST_CODE = 1;
    int MARGIN_TOP_DP = 10;
    int MARGIN_TOP_UNIT = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_training);

        initializeTraining();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == ADD_EXERCISE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                int exerciseID = data.getIntExtra("ExerciseID", -1);

                Exercise exercise = getExerciseByID(exerciseID);
                String exerciseName = exercise.getName();
                addNewButton(exerciseName);
//                addNewStartButton();

            }
        }
    }

    public void startNewExerciseActivity(View v){
        Intent exerciseIntent = new Intent(NewTrainingActivity.this, NewExerciseActivity.class);
        Bundle exerciseBundle = new Bundle();

        int trainingID = getThisTraining().getID();

        exerciseBundle.putInt("TrainingID", trainingID);
        exerciseIntent.putExtra("TrainingBundle", exerciseBundle);
        startActivityForResult(exerciseIntent, ADD_EXERCISE_REQUEST_CODE);
    }

    public void checkButtonDoIt(View view){

        Training training = addTraining();
        answerIntent(training);
    }

    public void cancelButtonDoIt(View view){
        setResult(Activity.RESULT_CANCELED, null);
        finish();
    }

    public void answerIntent(Training training){
        Intent data = new Intent();
        data.putExtra("TrainingID", training.getID());
        setResult(Activity.RESULT_OK, data);
        finish();
    }

    public void initializeTraining(){

        //Save to DB

        Training training = new Training();

        EasyTrainingDBHandler dbHandler = new EasyTrainingDBHandler(this, null, null, 1);

        //TEMPORARY LINE:
//        dbHandler.deleteTraining(training);

        dbHandler.addTraining(training);
        dbHandler.close();

    }

    public Training addTraining(){
        //Get Name
        final EditText nameEditText = (EditText) findViewById(R.id.nameEditText);
        String name = String.valueOf(nameEditText.getText());
        int ID = getThisTraining().getID();

        Training training = new Training(ID,name);

        EasyTrainingDBHandler dbHandler = new EasyTrainingDBHandler(this, null, null, 1);
        dbHandler.updateTraining(training);
        dbHandler.close();

        return training;

    }

    public Button addNewButton(String exerciseName) {
        RelativeLayout newTrainingLayout = (RelativeLayout) findViewById(R.id.newTrainingLayout);
        Button newButton = new Button(this);

        newButton.setText(exerciseName);

        newTrainingLayout.addView(newButton);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) newButton.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params.addRule(RelativeLayout.BELOW, R.id.nameEditText);
        int marginTop = dpToPixels(MARGIN_TOP_DP);
        params.setMargins(0, marginTop, 0, 0);
        newButton.setLayoutParams(params);
        MARGIN_TOP_DP += MARGIN_TOP_UNIT;

        return newButton;
    }


    public Exercise getExerciseByID (int exerciseID){
        EasyTrainingDBHandler dbHandler = new EasyTrainingDBHandler(this, null, null, 1);

        Exercise exercise = dbHandler.getExerciseByID(exerciseID);
        dbHandler.close();

        return exercise;
    }

    public Training getThisTraining () {
        EasyTrainingDBHandler dbHandler = new EasyTrainingDBHandler(this, null, null, 1);

        Training training = dbHandler.getLastTraining();
        dbHandler.close();

        return training;
    }

    public int dpToPixels(int dp){
        Context context = getApplicationContext();
        int d = (int) context.getResources().getDisplayMetrics().density;
        return (dp * d);
    }
}
