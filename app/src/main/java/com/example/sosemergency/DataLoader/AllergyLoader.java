package com.example.sosemergency.DataLoader;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.sosemergency.entities.Allergy;

import java.util.List;

/*
 * This interface serves as the Data Access Object (DAO) for the Allergy entity
 * in the Room database. It includes methods annotated with Room annotations
 * for performing CRUD (Create, Read, Update, Delete) operations on the Allergy table.
 *
 * @Dao annotation:
 *   Indicates that this interface is a Data Access Object for Room.
 *
 * Methods:
 *   - insertAllergy: Inserts a new Allergy into the database.
 *                 Returns the row ID of the inserted allergy.
 *
 *   - getAllergy: Retrieves a single allergy from the database.
 *              Returns a Allergy object.
 *
 *   - deleteAllergies: Deletes all allergies from the database.
 *
 *   - deleteAllergy: Delete one allergy from the database.
 *
 * Note: Unlike getAllContacts in the ContactLoader interface, getAllergy here
 *       returns a single Allergy object instead of a List. This assumes that
 *       there is only one allergy in the "allergies" table.
 *
 */
@Dao
public interface AllergyLoader {

    /*
     * Inserts a new allergy into the database.
     *
     * @param allergy - The Allergy object to be inserted.
     * @return long - The row ID of the inserted allergy.
     */
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertAllergy(Allergy allergy);

    /*
     * Retrieves all allergies from the database.
     *
     * @return Allergy - The Allergy object retrieved from the database.
     */
    @Query("SELECT * FROM allergies WHERE userId = :userId")
    List<Allergy> getUserAllergies(Long userId);

    /*
     * Retrieves allergies associated with the user from the database.
     *
     * @return Allergy - The Allergy object retrieved from the database.
     */
    @Query("SELECT * FROM allergies")
    List<Allergy> getAllergies();

    /*
     * Deletes all allergies from the database.
     */
    @Query("DELETE FROM allergies")
    void deleteAllergies();

    /*
     * Delete selected allergy from the database.
     */
    @Query("DELETE FROM allergies WHERE name= :allergieName")
    void deleteAllergy(String allergieName);
}


