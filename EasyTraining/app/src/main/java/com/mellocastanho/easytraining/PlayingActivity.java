package com.mellocastanho.easytraining;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PlayingActivity extends Activity {

    final int STARTING = -1;
    final int PAUSED = 0;
    final int PLAYING = 1;
    final int BREAK = 2;
    final int WAITING = 3;

    int status = STARTING;
    int pausedStatus;

    Exercise currExercise;
    Exercise nextExercise;
    List<Exercise> ExercisesList;
    int currListOrder = 0;
    int nextListOrder = currListOrder+1;
    int currSequence = -1;

    boolean lastExercise = false;

    int countDown;
    int currCountDownInterval;
    Timer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);

        Intent data = getIntent();
        int trainingID = data.getIntExtra("TrainingID", -1);

        EasyTrainingDBHandler dbHandler = new EasyTrainingDBHandler(this, null, null, 1);
        Training training = dbHandler.getTrainingByID(trainingID);
        ExercisesList = dbHandler.getAllExercises(trainingID);
        dbHandler.close();

        String trainingName = training.getName();

        TextView nameTextView = (TextView) findViewById(R.id.nameTextView);
        nameTextView.setText(trainingName);

        refreshExercises();

//        nameTextView.setText(String.valueOf(currExercise.getBreak_length()));

    }

    public void refreshExercises () {

        TextView currExerciseTextView2 = (TextView) findViewById(R.id.currExerciseTextView2);
        TextView nextExerciseTextView2 = (TextView) findViewById(R.id.nextExerciseTextView2);


        if (ExercisesList.size() > currListOrder) {

            currExercise = ExercisesList.get(currListOrder);
            currExerciseTextView2.setText(currExercise.getName());
        }

        if (ExercisesList.size() > currListOrder+1) {

            nextExercise = ExercisesList.get(nextListOrder);
            nextExerciseTextView2.setText(nextExercise.getName());
        } else {

            nextExerciseTextView2.setText(R.string.none);
            lastExercise = true;
        }

        currSequence = currExercise.getN_sequences();
    }

    public void startButtonDoIt(View v){

        switch (status){
            case STARTING: startExercise();
                break;
            case PAUSED: resumeExercise();
                break;
            case PLAYING: pauseExercise();
                break;
            case BREAK: pauseExercise();
                break;
            case WAITING: startExercise();
                break;
        }
    }

    public void startExercise() {

        imageToGreenCircle();
        status = PLAYING;

        currSequence -= 1;

        int interval = 500 + 100 * (30 - currExercise.getSpeed());

        int startingTime = (currExercise.getN_repetitions())*interval;

        setTimer(startingTime, interval);
    }

    public void pauseExercise() {

        pausedStatus = status;
        status = PAUSED;

        timer.cancel();
        countDown += currCountDownInterval;
        imageToYellowCircle();
    }

    public void resumeExercise() {

        if (pausedStatus == PLAYING) {
            imageToGreenCircle();
        }
        if (pausedStatus == BREAK) {
            imageToRedCircle();
        }

        setTimer(countDown, currCountDownInterval);

        status = pausedStatus;
    }

    public void startBreak() {

        imageToRedCircle();
        status = BREAK;

        int breakLength = currExercise.getBreak_length() * 1000; //ms

        if (currSequence == 0) {

            if (!lastExercise) {

                endOfExercise();

            } else {

                endOfTraining();
            }
        } else {

            setTimer(breakLength, 1000);
        }
    }


    public void imageToGreenCircle(){

        ImageButton circleImageButton = (ImageButton) findViewById(R.id.circleImageButton);
        circleImageButton.setImageResource(R.drawable.green_circle);
    }

    public void imageToYellowCircle() {

        ImageButton circleImageButton = (ImageButton) findViewById(R.id.circleImageButton);
        circleImageButton.setImageResource(R.drawable.yellow_circle);
    }

    public void imageToRedCircle(){

        ImageButton circleImageButton = (ImageButton) findViewById(R.id.circleImageButton);
        circleImageButton.setImageResource(R.drawable.red_circle);
    }

    public void imageToStart(){

        ImageButton circleImageButton = (ImageButton) findViewById(R.id.circleImageButton);
        circleImageButton.setImageResource(R.drawable.start);

    }


    public void setTimer(final int start, final int countDownInterval) {

        timer = new Timer("Timer");

        countDown = start;
        currCountDownInterval = countDownInterval;

        final TimerTask timerTask = new TimerTask() {

            public void run() {

                if (countDown > 0) {

                    countDown -= countDownInterval;
//                    INSERT BEEP SOUND;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            TextView timeLeft = (TextView) findViewById(R.id.timeLeftTextView);
                            timeLeft.setText(String.valueOf(countDown/countDownInterval + 1));
                        }
                    });



                } else {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            timer.cancel();
                            endOfTimer();
                        }
                    });
                }
            }
        };

        timer.schedule(timerTask, 0, countDownInterval);
    }

    public void endOfExercise() {

        currListOrder += 1;
        refreshExercises();
        imageToStart();
        TextView timeLeft = (TextView) findViewById(R.id.timeLeftTextView);
        timeLeft.setText("");
        status = WAITING;
    }

    public void endOfTraining() {

        imageToStart();
        TextView timeLeft = (TextView) findViewById(R.id.timeLeftTextView);
        timeLeft.setText(R.string.finished);
    }

    public void endOfTimer() {

        if (status == PLAYING) {

            startBreak();

        } else if (status == BREAK) {

            startExercise();
        }
    }

}
