package com.mellocastanho.easytraining;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class NewTrainingActivity extends AppCompatActivity {


    final int ADD_EXERCISE_REQUEST_CODE = 1;
    int MARGIN_TOP_DP = 10;
    int MARGIN_TOP_UNIT = 50;
    int CURR_DELETE_BUTTON_ID;
    int CURR_EXERCISE_BUTTON_ID;

    Training training;
    //IMPROVE CODE WITH "GLOBAL" PROPERTIES HERE

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_training);

        initializeTraining();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ADD_EXERCISE_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {

                int exerciseID = data.getIntExtra("ExerciseID", -1);
                addNewDeleteButton(exerciseID);
            }
        }
    }

    public void startNewExerciseActivity(View v) {

        Intent exerciseIntent = new Intent(NewTrainingActivity.this, NewExerciseActivity.class);
        Bundle exerciseBundle = new Bundle();

        int trainingID = training.getID();

        exerciseBundle.putInt("TrainingID", trainingID);
        exerciseIntent.putExtra("TrainingBundle", exerciseBundle);
        startActivityForResult(exerciseIntent, ADD_EXERCISE_REQUEST_CODE);
    }

    public void checkButtonDoIt(View view) {

        EasyTrainingDBHandler dbHandler = new EasyTrainingDBHandler(this, null, null, 1);
        List<Exercise> ExercisesList = dbHandler.getAllExercises(training.getID());
        int size = ExercisesList.size();

        if (size == 0) {

            Toast.makeText(this, R.string.no_exercises, Toast.LENGTH_LONG).show();
        } else {

            Training training = addTraining();
            answerIntent(training);
        }
    }

    public void cancelButtonDoIt(View view) {

        setResult(Activity.RESULT_CANCELED, null);
        EasyTrainingDBHandler dbHandler = new EasyTrainingDBHandler(this, null, null, 1);
        dbHandler.deleteTraining(training.getID());
        finish();
    }

    public void answerIntent(Training training) {

        Intent data = new Intent();
        data.putExtra("TrainingID", training.getID());
        setResult(Activity.RESULT_OK, data);
        finish();
    }

    public void initializeTraining() {

        //Save to DB
        training = new Training();

        EasyTrainingDBHandler dbHandler = new EasyTrainingDBHandler(this, null, null, 1);

        //TEMPORARY LINE:
//        dbHandler.deleteTraining(training);

        dbHandler.addTraining(training);
        training = dbHandler.getLastTraining();
        dbHandler.close();
    }

    public Training addTraining() {

        //Get Name
        final EditText nameEditText = (EditText) findViewById(R.id.nameEditText);
        String name = String.valueOf(nameEditText.getText());

        training.setName(name);

        EasyTrainingDBHandler dbHandler = new EasyTrainingDBHandler(this, null, null, 1);
        dbHandler.updateTraining(training);
        dbHandler.close();

        return training;
    }

    public ImageButton addNewDeleteButton(final int exerciseID) {

        RelativeLayout newTrainingLayout = (RelativeLayout) findViewById(R.id.newTrainingLayout);
        ImageButton newImageButton = new ImageButton(this);

        //Set Button Parameters
        newImageButton.setImageResource(R.drawable.wrong_mark);
        newImageButton.setBackgroundColor(Color.TRANSPARENT);
        newImageButton.setScaleType(ImageView.ScaleType.FIT_XY);

        newTrainingLayout.addView(newImageButton);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) newImageButton.getLayoutParams();
        params.width = dpToPixels(50);
        params.height = dpToPixels(50);

        params.addRule(RelativeLayout.BELOW, R.id.nameTextView);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

        int marginTop = dpToPixels(MARGIN_TOP_DP);
        params.setMargins(0, marginTop, 0, 0);

        //Update Margin
        MARGIN_TOP_DP += MARGIN_TOP_UNIT;

        newImageButton.setLayoutParams(params);

        //Generate ID
        CURR_DELETE_BUTTON_ID = generateViewId();
        newImageButton.setId(CURR_DELETE_BUTTON_ID);

        addNewExerciseButton(exerciseID);

        //Set OnClick
        final int deleteButtonID = CURR_DELETE_BUTTON_ID;
        final int exerciseButtonID = CURR_EXERCISE_BUTTON_ID;

        newImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteExercise(v, exerciseID);
                deleteButtons(deleteButtonID, exerciseButtonID);
            }
        });

        return newImageButton;
    }

    public Button addNewExerciseButton(int trainingID) {

        EasyTrainingDBHandler dbHandler = new EasyTrainingDBHandler(this, null, null, 1);
        String exerciseName = dbHandler.getExerciseByID(trainingID).getName();

        RelativeLayout newTrainingLayout = (RelativeLayout) findViewById(R.id.newTrainingLayout);
        Button newButton = new Button(this);

        newButton.setText(exerciseName);

        newTrainingLayout.addView(newButton);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) newButton.getLayoutParams();

        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.addRule(RelativeLayout.RIGHT_OF, CURR_DELETE_BUTTON_ID);
        params.addRule(RelativeLayout.ALIGN_BOTTOM, CURR_DELETE_BUTTON_ID);

        int marginTop = dpToPixels(MARGIN_TOP_DP);
        params.setMargins(0, marginTop, 0, 0);

        newButton.setLayoutParams(params);

        //Generate ID
        CURR_EXERCISE_BUTTON_ID = generateViewId();
        newButton.setId(CURR_EXERCISE_BUTTON_ID);

        return newButton;
    }

    public void deleteExercise(View v, int exerciseID) {

        EasyTrainingDBHandler dbHandler = new EasyTrainingDBHandler(this, null, null, 1);
        dbHandler.deleteExercise(exerciseID);
        dbHandler.close();
    }

    public void deleteButtons(int deleteButtonID, int exerciseButtonID) {

        RelativeLayout newTrainingLayout = (RelativeLayout)findViewById(R.id.newTrainingLayout);

        View deleteButtonView = findViewById(deleteButtonID);
        View exerciseButtonView = findViewById(exerciseButtonID);

        newTrainingLayout.removeView(deleteButtonView);
        newTrainingLayout.removeView(exerciseButtonView);
    }

    public int dpToPixels(int dp) {
        Context context = getApplicationContext();
        int d = (int) context.getResources().getDisplayMetrics().density;
        return (dp * d);
    }


    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    public static int generateViewId() {
        for (; ; ) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }

}
