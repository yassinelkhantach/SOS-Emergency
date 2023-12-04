package com.example.sosemergency.ui.profile;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.sosemergency.MainActivity;
import com.example.sosemergency.R;
import com.example.sosemergency.databinding.FragmentProfileBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private ProfileViewModel notificationsViewModel;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notificationsViewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle(R.string.title_profile);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final EditText profileFullName = binding.profileFullName;
        final EditText profileBD = binding.profileBD;
        final ImageView profileEditIcon = binding.profileEditIcon;
        final ImageView profileSaveIcon = binding.profileSaveIcon;
        final EditText profileAge = binding.profileAge;

        // Use lambda expression for click listeners
        profileEditIcon.setOnClickListener(v -> enableEditing(profileFullName, profileBD, profileEditIcon, profileSaveIcon));
        profileSaveIcon.setOnClickListener(v -> saveChanges(profileFullName, profileBD, profileAge, profileEditIcon, profileSaveIcon));

        // Observe the LiveData directly
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), profileFullName::setText);

        return root;
    }

    private void enableEditing(EditText profileName,EditText profileDate, ImageView editIcon, ImageView saveIcon) {
        // Make the TextView editable
        profileName.setFocusableInTouchMode(true);
        profileName.setFocusable(true);
        profileName.requestFocus();
        profileName.setSelection(profileName.getText().length());

        // Set a text color while activate editing
        profileName.setTextColor(ContextCompat.getColor(getContext(), R.color.primary));
        profileDate.setTextColor(ContextCompat.getColor(getContext(), R.color.primary));

        // Show the Keybord
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(profileName, InputMethodManager.SHOW_IMPLICIT);

        // Show the save icon and hide the edit icon
        editIcon.setVisibility(View.GONE);
        saveIcon.setVisibility(View.VISIBLE);

        profileDate.setOnClickListener(view -> showDatePicker(profileDate));
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String date = day+"-"+month+"-"+year;
                profileDate.setText(date);
            }
        };
    }

    private void saveChanges(EditText profileName, EditText profileDate, EditText profileAge, ImageView editIcon, ImageView saveIcon) {
        // Save the modified text
        String modifiedFullName = profileName.getText().toString();
        String modifiedDOB = profileDate.getText().toString();

        // Update the ViewModel with the modified text
        notificationsViewModel.setText(modifiedFullName);

        // Set a text color when saving
        profileName.setTextColor(ContextCompat.getColor(getContext(), R.color.navbar_text));
        profileDate.setTextColor(ContextCompat.getColor(getContext(), R.color.navbar_text));

        // Make the TextView non-editable again
        profileName.setFocusable(false);
        profileName.setFocusableInTouchMode(false);
        profileDate.setFocusable(false);
        profileDate.setFocusableInTouchMode(false);

        // Hide the Keybord
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(profileName.getWindowToken(), 0);

        // Calculate and display age based on the modified date of birth
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date dateOfBirth = dateFormat.parse(modifiedDOB);
            Date dateOfNow = new Date(); // Automatically generate the current date

            int age = calculateAge(dateOfBirth, dateOfNow);

            // Set the calculated age to the profileAge EditText
            profileAge.setText(String.valueOf(age));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Show the edit icon and hide the save icon
        editIcon.setVisibility(View.VISIBLE);
        saveIcon.setVisibility(View.GONE);
    }

    private void showDateDialog(EditText profileDate) {

    }

    private void showDatePicker(EditText profileDate) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(getActivity(),R.style.DatePickerTheme, mDateSetListener,year,month,day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.show();
    }

    // Helper method to calculate age
    private int calculateAge(Date dateOfBirth, Date dateOfNow) {
        Calendar dob = Calendar.getInstance();
        dob.setTime(dateOfBirth);

        Calendar now = Calendar.getInstance();
        now.setTime(dateOfNow);

        int age = now.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (now.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        return age;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}