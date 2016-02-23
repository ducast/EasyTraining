package com.mellocastanho.easytraining;

/**
 * Created by castanho on 11/02/16.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class EasyTrainingDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "easyTrainingDB.db";
    private static final String TABLE_EXERCISES = "exercises";
    private static final String TABLE_TRAININGS = "trainings";

    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TRAINING_ID = "training_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_N_SEQUENCES =  "n_sequences";
    private static final String COLUMN_N_REPETITIONS = "n_repetitions";
    private static final String COLUMN_BREAK_LENGTH = "break_length";
    private static final String COLUMN_SPEED = "speed";

    public EasyTrainingDBHandler(Context context, String name,
      SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TRAININGS_TABLE = "CREATE TABLE " + TABLE_TRAININGS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAME + " TEXT" + ")";
        db.execSQL(CREATE_TRAININGS_TABLE);

        String CREATE_EXERCISES_TABLE = "CREATE TABLE " + TABLE_EXERCISES + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_TRAINING_ID + " INTEGER," +
                COLUMN_NAME + " TEXT," +
                COLUMN_TYPE + " TEXT," +
                COLUMN_N_SEQUENCES + " INTEGER," +
                COLUMN_N_REPETITIONS + " INTEGER," +
                COLUMN_BREAK_LENGTH + " INTEGER," +
                COLUMN_SPEED + " INTEGER" + ")";
        db.execSQL(CREATE_EXERCISES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISES);
        onCreate(db);
    }

    public int addTraining(Training training) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, training.getName());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_TRAININGS, null, values);
        db.close();

        return getLastTraining().getID();
    }

    public void updateTraining(Training training) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, training.getName());

        db.update(TABLE_TRAININGS, cv, COLUMN_ID + " = " + training.getID(), null);

    }

    public Training getTrainingByID(int trainingID) {

        String query = "Select * FROM " + TABLE_TRAININGS +
                " WHERE " + COLUMN_ID + " =  \"" + String.valueOf(trainingID) + "\"";


        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Training training = new Training();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            training.setID(Integer.parseInt(cursor.getString(0)));
            training.setName(cursor.getString(1));
            cursor.close();
        } else {
            training = null;
        }
        db.close();
        return training;

    }


    public Training getLastTraining() {

        String query = "SELECT * FROM " + TABLE_TRAININGS +
                " ORDER BY " + COLUMN_ID + " DESC LIMIT 1";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Training training = new Training();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            training.setID(Integer.parseInt(cursor.getString(0)));
            training.setName(cursor.getString(1));
            cursor.close();
        } else {
            training = new Training(-1, "NULL TRAINING");
        }

        return training;
    }

    public List<Training> getAllTrainings() {

        String query = "Select * FROM " + TABLE_TRAININGS;

        List<Training> result = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);


        while(cursor.moveToNext()) {
            Training training = new Training();
            training.setID(Integer.parseInt(cursor.getString(0)));
            training.setName(cursor.getString(1));
            result.add(training);
        }
        cursor.close();
        db.close();

        return result;

    }

    public int addExercise(Exercise exercise) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TRAINING_ID, exercise.getTrainingID());
        values.put(COLUMN_NAME, exercise.getName());
        values.put(COLUMN_TYPE, exercise.getType());
        values.put(COLUMN_N_SEQUENCES, exercise.getN_sequences());
        values.put(COLUMN_N_REPETITIONS, exercise.getN_repetitions());
        values.put(COLUMN_BREAK_LENGTH, exercise.getBreak_length());
        values.put(COLUMN_SPEED, exercise.getSpeed());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_EXERCISES, null, values);
        db.close();

        return getLastTraining().getID();
    }

    public Exercise getExerciseByID(int exerciseID) {
        String query = "Select * FROM " + TABLE_EXERCISES +
                " WHERE " + COLUMN_ID + " =  \"" + String.valueOf(exerciseID) + "\"";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Exercise exercise = new Exercise();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            exercise.setID(Integer.parseInt(cursor.getString(0)));
            exercise.setTrainingID(Integer.parseInt(cursor.getString(1)));
            exercise.setName(cursor.getString(2));
            exercise.setType(cursor.getString(3));
            exercise.setN_sequences(Integer.parseInt(cursor.getString(4)));
            exercise.setN_repetitions(Integer.parseInt(cursor.getString(5)));
            exercise.setBreak_length(Integer.parseInt(cursor.getString(6)));
            exercise.setSpeed(Integer.parseInt(cursor.getString(7)));
            cursor.close();
        } else {
            exercise = new Exercise(-1, "NULL EXERCISE", "", 0, 0, 0, 0);
        }
        db.close();
        return exercise;

    }

    public Exercise getLastExercise() {

        String query = "SELECT * FROM " + TABLE_EXERCISES +
                " ORDER BY " + COLUMN_ID + " DESC LIMIT 1";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Exercise exercise = new Exercise();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            exercise.setID(Integer.parseInt(cursor.getString(0)));
            exercise.setTrainingID(Integer.parseInt(cursor.getString(1)));
            exercise.setName(cursor.getString(2));
            exercise.setType(cursor.getString(3));
            exercise.setN_sequences(Integer.parseInt(cursor.getString(4)));
            exercise.setN_repetitions(Integer.parseInt(cursor.getString(5)));
            exercise.setBreak_length(Integer.parseInt(cursor.getString(6)));
            exercise.setSpeed(Integer.parseInt(cursor.getString(7)));
            cursor.close();
        } else {
            exercise = new Exercise(-1, "NULL EXERCISE", "", 0, 0, 0, 0);
        }

        return exercise;

    }

    public List<Exercise> getAllExercises(int trainingID) {

        String query = "Select * FROM " + TABLE_EXERCISES +
                " Where " + COLUMN_TRAINING_ID + " = " + String.valueOf(trainingID);

        List<Exercise> result = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);


        while(cursor.moveToNext()) {
            Exercise exercise = new Exercise();
            exercise.setID(Integer.parseInt(cursor.getString(0)));
            exercise.setTrainingID(Integer.parseInt(cursor.getString(1)));
            exercise.setName(cursor.getString(2));
            exercise.setType(cursor.getString(3));
            exercise.setN_sequences(Integer.parseInt(cursor.getString(4)));
            exercise.setN_repetitions(Integer.parseInt(cursor.getString(5)));
            exercise.setBreak_length(Integer.parseInt(cursor.getString(6)));
            exercise.setSpeed(Integer.parseInt(cursor.getString(7)));
            result.add(exercise);
        }
        cursor.close();
        db.close();

        return result;

    }

    public boolean deleteExercise(String exerciseID) {

        boolean result = false;

        String query = "Select * FROM " + TABLE_EXERCISES + " WHERE " + COLUMN_ID + " =  \"" + exerciseID + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Exercise exercise = new Exercise();

        if (cursor.moveToFirst()) {
            exercise.setID(Integer.parseInt(cursor.getString(0)));
            db.delete(TABLE_EXERCISES, COLUMN_ID + " = ?",
                    new String[] { String.valueOf(exercise.getID()) });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }


}

