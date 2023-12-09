package com.example.sosemergency.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * ViewModel class responsible for handling data related to the user profile.
 */
public class ProfileViewModel extends ViewModel {

    // MutableLiveData to store the user's full name
    private final MutableLiveData<String> mText;

    // MutableLiveData to store the user's blood type
    private final MutableLiveData<String> bloodType; // Added for blood type

    // MutableLiveData to store the user's height
    private final MutableLiveData<String> height;

    // MutableLiveData to store the user's weight
    private final MutableLiveData<String> weight;

    /**
     * Constructor to initialize MutableLiveData instances.
     */
    public ProfileViewModel() {
        mText = new MutableLiveData<>();
        bloodType = new MutableLiveData<>();
        height = new MutableLiveData<>();
        weight = new MutableLiveData<>();
    }

    /**
     * Getter method for observing the user's full name.
     *
     * @return LiveData containing the user's full name.
     */
    public LiveData<String> getText() {
        return mText;
    }

    /**
     * Setter method to update the user's full name.
     *
     * @param newText The new full name to set.
     */
    public void setText(String newText) {
        mText.setValue(newText);
    }

    /**
     * Getter method for observing the user's blood type.
     *
     * @return LiveData containing the user's blood type.
     */
    public LiveData<String> getBloodType() {
        return bloodType;
    }

    /**
     * Setter method to update the user's blood type.
     *
     * @param newBloodType The new blood type to set.
     */
    public void setBloodType(String newBloodType) {
        bloodType.setValue(newBloodType);
    }

    /**
     * Getter method for observing the user's height.
     *
     * @return LiveData containing the user's height.
     */
    public LiveData<String> getHeight() {
        return height;
    }

    /**
     * Setter method to update the user's height.
     *
     * @param newHeight The new height to set.
     */
    public void setHeight(String newHeight) {
        height.setValue(newHeight);
    }

    /**
     * Getter method for observing the user's weight.
     *
     * @return LiveData containing the user's weight.
     */
    public LiveData<String> getWeight() {
        return weight;
    }

    /**
     * Setter method to update the user's weight.
     *
     * @param newWeight The new weight to set.
     */
    public void setWeight(String newWeight) {
        weight.setValue(newWeight);
    }
}