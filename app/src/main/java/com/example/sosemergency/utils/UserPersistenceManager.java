package com.example.sosemergency.utils;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.room.Room;

import com.example.sosemergency.DataLoader.AppDatabase;
import com.example.sosemergency.DataLoader.UserDatabaseContract;
import com.example.sosemergency.entities.User;

/*
 * This class manages the persistence of User entities in the Room database.
 * It provides methods for initializing the database, registering a user, retrieving a user,
 * and deleting user information.
 *
 * Methods:
 *   - initAppDatabase: Initializes the Room database and returns an instance of AppDatabase.
 *   - registerUser: Registers a user by adding their information to the Room database.
 *   - deleteUser: Deletes all user information from the Room database.
 *   - getUser: Retrieves the user information from the Room database.
 *   - editUser: Edits user information in the Room database.
 */
public class UserPersistenceManager {

    private static Context appContext;
    private static AppDatabase appDatabase;
    private static User globalUser; // Added variable to store the global user

    public static AppDatabase initAppDatabase(Context context) {
        if (appDatabase == null) {
            appContext = context;
            appDatabase = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "user-database").build();
        }
        return appDatabase;
    }

    public static void registerUser(User user) {
        ContentValues values = new ContentValues();
        values.put(UserDatabaseContract.ContactEntry.COLUMN_NAME, user.getName());
        values.put(UserDatabaseContract.ContactEntry.COLUMN_BIRTH_DATE, DateConverterUtil.toTimestamp(user.getBirthDate()));
        values.put(UserDatabaseContract.ContactEntry.COLUMN_COUNTRY, user.getCountry());
        values.put(UserDatabaseContract.ContactEntry.COLUMN_BLOOD_TYPE, user.getBloodType());
        values.put(UserDatabaseContract.ContactEntry.COLUMN_HEIGHT, user.getHeight());
        values.put(UserDatabaseContract.ContactEntry.COLUMN_WEIGHT, user.getWeight());
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                appDatabase.userLoader().insertUser(user);
                // Retrieve the user with the actual ID
                globalUser = appDatabase.userLoader().getUser();
                Log.i("registerUser", globalUser.getName() + " user registered successfully !");
            }
        });
    }

    public static void editUser(User user) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                appDatabase.userLoader().updateUser(user.getId(),
                        user.getName(),
                        DateConverterUtil.toTimestamp(user.getBirthDate()),
                        user.getHeight(),
                        user.getWeight(),
                        user.getBloodType(),
                        user.getCountry());
                globalUser = user; // Update the stored user
            }
        });
    }

    public static void deleteUser() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                appDatabase.userLoader().deleteUser();
                globalUser = null; // Remove the stored user on deletion
                Log.i("deleteUser", "User deleted successfully");
            }
        });
    }

    // Retrieve the stored user without querying the database
    public static User getUser() {
        return globalUser;
    }
}