package com.example.sosemergency.DataLoader;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.sosemergency.entities.User;

/*
 * This interface serves as the Data Access Object (DAO) for the User entity
 * in the Room database. It includes methods annotated with Room annotations
 * for performing CRUD (Create, Read, Update, Delete) operations on the User table.
 *
 * @Dao annotation:
 *   Indicates that this interface is a Data Access Object for Room.
 *
 * Methods:
 *   - insertUser: Inserts a new User into the database.
 *                 Returns the row ID of the inserted user.
 *
 *   - getUser: Retrieves a single user from the database.
 *              Returns a User object.
 *
 *   - deleteUser: Deletes all users from the database.
 *
 * Note: Unlike getAllContacts in the ContactLoader interface, getUser here
 *       returns a single User object instead of a List. This assumes that
 *       there is only one user in the "users" table.
 *
 */
@Dao
public interface UserLoader {

    /*
     * Inserts a new user into the database.
     *
     * @param user - The User object to be inserted.
     * @return long - The row ID of the inserted user.
     */
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertUser(User user);

    @Query("SELECT * from users")
    User getUser();

    /*
     * Deletes all users from the database.
     */
    @Query("DELETE FROM users")
    void deleteUser();

    /*
     * Updates user information in the database.
     *
     * @param user - The User object with updated information.
     */
    @Query("UPDATE users SET country = :country, bloodType = :bloodType, weight = :weight, height = :height, birthDate = :birthDate, name = :name WHERE id = :id")
    void updateUser(Long id, String name, Long birthDate, double height, double weight, String bloodType, String country);

}


