package com.example.sosemergency.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfileViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private final MutableLiveData<String> bloodType; // Added for blood type


    public ProfileViewModel() {
        mText = new MutableLiveData<>();
        bloodType = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void setText(String newText) {
        mText.setValue(newText);
    }

    // Getter method for blood type
    public LiveData<String> getBloodType() {
        return bloodType;
    }

    // Setter method for blood type
    public void setBloodType(String newBloodType) {
        bloodType.setValue(newBloodType);
    }
}