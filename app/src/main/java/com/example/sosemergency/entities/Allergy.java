package com.example.sosemergency.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.sosemergency.DataLoader.AllergyDatabaseContract;

@Entity(tableName = AllergyDatabaseContract.AllergyEntry.TABLE_NAME)
public class Allergy {

    @PrimaryKey(autoGenerate = true)
    private Long id;
    private String name;
    private String description;
    private Long userId; // Foreign key referencing the User entity

    public Allergy(String name, String description, Long userId) {
        this.name = name;
        this.description = description;
        this.userId = userId;
    }
    @Ignore
    public Allergy(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Getters and setters for the fields

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Allergy{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", userId=" + userId +
                '}';
    }
}

