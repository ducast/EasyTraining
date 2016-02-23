package com.mellocastanho.easytraining;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class MyTrainingsActivity extends AppCompatActivity {

    final int ADD_TRAINING_REQUEST_CODE = 1;
    int MARGIN_TOP_DP = 10;
    int MARGIN_TOP_UNIT = 50;
    int CURR_BUTTON_ID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_trainings);

        //Create Training Buttons
        EasyTrainingDBHandler dbHandler = new EasyTrainingDBHandler(this, null, null, 1);
        List<Training> trainings = dbHandler.getAllTrainings();
        dbHandler.close();

        for (Training tempTraining : trainings) {
            int trainingID = tempTraining.getID();
            addNewStartButton(trainingID);
            addNewExerciseButton(tempTraining.getName());
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == ADD_TRAINING_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                int trainingID = data.getIntExtra("TrainingID", -1);

                Training training = getTrainingByID(trainingID);

                String trainingName = training.getName();
                addNewStartButton(trainingID);
                addNewExerciseButton(trainingName);
            }
        }
    }


    public ImageButton addNewStartButton(final int trainingID) {

        RelativeLayout newTrainingLayout = (RelativeLayout) findViewById(R.id.myTrainingsLayout);
        ImageButton newImageButton = new ImageButton(this);

        //Set Button Parameters
        newImageButton.setImageResource(R.drawable.start);
        newImageButton.setBackgroundColor(Color.TRANSPARENT);
        newImageButton.setScaleType(ImageView.ScaleType.FIT_XY);

        newTrainingLayout.addView(newImageButton);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) newImageButton.getLayoutParams();
        params.width = dpToPixels(50);
        params.height = dpToPixels(50);

        params.addRule(RelativeLayout.BELOW, R.id.myTrainingsTextView);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        int marginTop = dpToPixels(MARGIN_TOP_DP);
        params.setMargins(0, marginTop, 0, 0);

        newImageButton.setLayoutParams(params);

        //Set OnClick
        newImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startBodyBuildingActivity(v, trainingID);
            }
        });

        //Generate ID
        CURR_BUTTON_ID = generateViewId();
        newImageButton.setId(CURR_BUTTON_ID);

        //Update Margin
        MARGIN_TOP_DP += MARGIN_TOP_UNIT;

        return newImageButton;
    }

    public Button addNewExerciseButton(String trainingName){
        RelativeLayout myTrainingsLayout = (RelativeLayout)findViewById(R.id.myTrainingsLayout);
        Button newButton = new Button (this);

        newButton.setText(trainingName);


        myTrainingsLayout.addView(newButton);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)newButton.getLayoutParams();

        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params.addRule(RelativeLayout.LEFT_OF, CURR_BUTTON_ID);
        params.addRule(RelativeLayout.ALIGN_BOTTOM, CURR_BUTTON_ID);

        newButton.setLayoutParams(params);

        return newButton;
    }

    public Training getTrainingByID (int trainingID){

        EasyTrainingDBHandler dbHandler = new EasyTrainingDBHandler(this, null, null, 1);
        Training training = dbHandler.getTrainingByID(trainingID);
        dbHandler.close();

        return training;
    }

    public int dpToPixels(int dp){
        Context context = getApplicationContext();
        int d = (int) context.getResources().getDisplayMetrics().density;
        return (dp * d);
    }


    public void startNewTrainingActivity(View v){

        Intent newTrainingIntent = new Intent(MyTrainingsActivity.this, NewTrainingActivity.class);
        startActivityForResult(newTrainingIntent, ADD_TRAINING_REQUEST_CODE);
    }

    public void startBodyBuildingActivity(View v, int trainingID) {

        Intent bodyBuildingIntent = new Intent(MyTrainingsActivity.this, PlayingActivity.class);
        bodyBuildingIntent.putExtra("TrainingID", trainingID);
        startActivity(bodyBuildingIntent);

    }
















    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    public static int generateViewId() {
        for (;;) {
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

