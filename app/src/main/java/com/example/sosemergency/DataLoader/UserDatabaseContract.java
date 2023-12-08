package com.example.sosemergency.DataLoader;

import android.provider.BaseColumns;

/*
 * This class defines the contract for the User database, including the table structure
 * and column names. It follows the recommended Android SQLite database contract design.
 *
 * The class is marked as final, and the constructor is private to prevent accidental instantiation.
 * It includes an inner class, ContactEntry, that implements the BaseColumns interface to
 * include standard column names like _ID.
 *
 * Table Structure:
 *   - TABLE_NAME: The name of the "users" table in the User database.
 *   - COLUMN_NAME: The column name for the user's name.
 *   - COLUMN_BIRTH_DATE: The column name for the user's birth date.
 *   - COLUMN_BLOOD_TYPE: The column name for the user's blood type.
 *   - COLUMN_HEIGHT: The column name for the user's height.
 *   - COLUMN_WEIGHT: The column name for the user's weight.
 *   - COLUMN_COUNTRY: The column name for the user's country.
 *
 */
public final class UserDatabaseContract {

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private UserDatabaseContract() {}

    /* Inner class that defines the table contents */
    public static class ContactEntry implements BaseColumns {

        /*
         * The name of the "users" table in the User database.
         */
        public static final String TABLE_NAME = "users";

        /*
         * The column name for the user's name.
         */
        public static final String COLUMN_NAME = "name";

        /*
         * The column name for the user's birth date.
         */
        public static final String COLUMN_BIRTH_DATE = "birth_date";

        /*
         * The column name for the user's blood type.
         */
        public static final String COLUMN_BLOOD_TYPE = "blood_type";

        /*
         * The column name for the user's height.
         */
        public static final String COLUMN_HEIGHT = "height";

        /*
         * The column name for the user's weight.
         */
        public static final String COLUMN_WEIGHT = "weight";

        /*
         * The column name for the user's country.
         */
        public static final String COLUMN_COUNTRY = "country";
    }
}


