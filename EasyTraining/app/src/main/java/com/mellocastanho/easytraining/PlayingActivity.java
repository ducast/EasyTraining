package com.mellocastanho.easytraining;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PlayingActivity extends Activity {

    final int WAITING = -1;
    final int STARTING = 0;
    final int PLAYING = 1;
    final int PAUSED = 2;
    final int BREAK = 3;
    final int FINISHED = 4;

    int status = WAITING;
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

    }

    public void refreshExercises () {

        TextView currExerciseTextView2 = (TextView) findViewById(R.id.currExerciseTextView2);
        TextView nextExerciseTextView2 = (TextView) findViewById(R.id.nextExerciseTextView2);

        String show;
        String showNext;

        if (ExercisesList.size() > currListOrder) {

            currExercise = ExercisesList.get(currListOrder);

            show = currExercise.getName() + "\n" +
                    String.valueOf(currExercise.getN_sequences()) + " x " +
                    String.valueOf(currExercise.getN_repetitions());

            currExerciseTextView2.setText(show);
        }

        if (ExercisesList.size() > currListOrder+1) {

            nextExercise = ExercisesList.get(nextListOrder);

            showNext = nextExercise.getName();
//                    + "\n" +
//                    String.valueOf(nextExercise.getN_sequences()) + " x " +
//                    String.valueOf(nextExercise.getN_repetitions());

            nextExerciseTextView2.setText(showNext);
        } else {

            nextExerciseTextView2.setText(R.string.none);
            lastExercise = true;
        }

        currSequence = currExercise.getN_sequences();
    }

    public void startButtonDoIt(View v){

        switch (status){
            case WAITING: startExercise();
                break;
            case PAUSED: resumeExercise();
                break;
            case PLAYING: pauseExercise();
                break;
            case BREAK: pauseExercise();
                break;
            case STARTING:
                break;
            case FINISHED:
                break;
        }
    }

    public void backButtonDoIt(View v) {

        if (timer != null) if (timer != null) timer.cancel();
        finish();
    }

    public void forwardButtonDoIt(View v) {

        if (status == PLAYING || status == BREAK) {
            
            if (timer != null) timer.cancel();
            endOfTimer();
        }
    }

    public void startExercise() {

        //HOLD 3 SECS
        status = STARTING;
        imageToRedCircle();
        setTimer(3000, 1000);
    }

    //NESTED IN startExercise
    public void startSequence() {

        imageToGreenCircle();
        status = PLAYING;

        setTapToText(R.string.tap_to_pause);

        currSequence -= 1;

        int interval = 500 + 100 * (30 - currExercise.getSpeed());

        int startingTime = (currExercise.getN_repetitions())*interval;

        setTimer(startingTime, interval);
    }

    public void pauseExercise() {

        pausedStatus = status;
        status = PAUSED;

        setTapToText(R.string.tap_to_resume);

        if (timer != null) timer.cancel();
        countDown += currCountDownInterval;
        imageToYellowCircle();
    }

    public void resumeExercise() {

        setTapToText(R.string.tap_to_pause);

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

        setTapToText(R.string.tap_to_pause);

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

    public void displayCountDown(int countDown, int countDownInterval) {

        TextView timeLeft = (TextView) findViewById(R.id.timeLeftTextView);

        if (status == STARTING) {

            switch (countDown/1000) {

                case 0: timeLeft.setText(R.string.go);
                    break;
                case 1: timeLeft.setText(R.string.set);
                    break;
                case 2: timeLeft.setText(R.string.ready);
                    break;
            }
        } else {

            timeLeft.setText(String.valueOf(countDown / countDownInterval + 1));
        }
    }


    public void setTapToText (int stringID) {

        TextView tapToTextView = (TextView) findViewById(R.id.tapToTextView);
        tapToTextView.setText(stringID);
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

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            beep(countDown);

                            displayCountDown(countDown, countDownInterval);
                        }
                    });
                } else {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (timer != null) timer.cancel();
                            endOfTimer();
                        }
                    });
                }
            }
        };

        timer.schedule(timerTask, 0, countDownInterval);
    }

    public void beep(int countDown) {

        if (!(status == BREAK && countDown > 2000)) {

            ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);

            if (status == BREAK || status == STARTING) {

                tg.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD);
            } else {

                tg.startTone(ToneGenerator.TONE_CDMA_ABBR_ALERT);
            }

            tg.release();
            tg = null;
        }
    }

    public void endOfExercise() {

        currListOrder += 1;

        refreshExercises();

        imageToStart();
        setTapToText(R.string.empty);

        TextView timeLeft = (TextView) findViewById(R.id.timeLeftTextView);
        timeLeft.setText(R.string.empty);

        status = WAITING;
    }

    public void endOfTraining() {

        imageToRedCircle();
        setTapToText(R.string.empty);

        TextView timeLeft = (TextView) findViewById(R.id.timeLeftTextView);
        timeLeft.setText(R.string.done);

        TextView currExerciseTextView2 = (TextView) findViewById(R.id.currExerciseTextView2);
        currExerciseTextView2.setText(R.string.none);

        status = FINISHED;
    }

    public void endOfTimer() {

        switch (status) {
            case PLAYING: startBreak();
                break;
            case BREAK: startSequence();
                break;
            case STARTING: startSequence();
                break;
        }
    }
}
