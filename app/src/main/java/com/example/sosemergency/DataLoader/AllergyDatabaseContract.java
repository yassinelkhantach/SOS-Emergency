package com.example.sosemergency.DataLoader;

import android.provider.BaseColumns;

/*
 * This class defines the contract for the Allergy database, including the table structure
 * and column names. It follows the recommended Android SQLite database contract design.
 *
 * The class is marked as final, and the constructor is private to prevent accidental instantiation.
 * It includes an inner class, ContactEntry, that implements the BaseColumns interface to
 * include standard column names like _ID.
 *
 * Table Structure:
 *   - TABLE_NAME: The name of the "allergies" table in the Allergy database.
 *   - COLUMN_NAME: The column name for the allergy's name.
 *   - COLUMN_DESCRIPTION: The column name for the allergy's description.
 *   - COLUMN_USER_ID: The column name for the allergy's user id.
 *
 */
public final class AllergyDatabaseContract {

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private AllergyDatabaseContract() {}

    /* Inner class that defines the table contents */
    public static class AllergyEntry implements BaseColumns {

        /*
         * The name of the "allergies" table in the Allergy database.
         */
        public static final String TABLE_NAME = "allergies";

        /*
         * The column name for the allergy's name.
         */
        public static final String COLUMN_NAME = "name";

        /*
         * The column name for the allergy's description.
         */
        public static final String COLUMN_DESCRIPTION = "description";

        /*
         * The column name for the allergy's user id.
         */
        public static final String COLUMN_USER_ID = "user_id";
    }
}



