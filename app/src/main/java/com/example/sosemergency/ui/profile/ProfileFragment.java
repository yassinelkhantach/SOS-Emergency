package com.example.sosemergency.ui.profile;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
    private Dialog bloodTypeDialog;
    private Dialog heightDialog;
    private Dialog weightDialog;


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
        final CardView profileBloodCard = binding.profileBloodCard;
        final CardView profileHeightCard = binding.profileHeightCard;
        final CardView profileWeightCard = binding.profileWeightCard;

        // Initialize the blood type dialog
        setupBloodTypeDialog();

        // Initialize the height dialog
        setupHeightDialog();

        // Initialize the weight dialog
        setupWeightDialog();

        // Use lambda expression for click listeners
        profileEditIcon.setOnClickListener(v -> enableEditing(profileFullName, profileBD, profileEditIcon, profileSaveIcon));
        profileSaveIcon.setOnClickListener(v -> saveChanges(profileFullName, profileBD, profileAge, profileEditIcon, profileSaveIcon));

        // Observe the LiveData directly
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), profileFullName::setText);

        // Show blood type dialog when blood card view is clicked
        profileBloodCard.setOnClickListener(v -> showBloodTypeDialog());

        // Set an OnClickListener for the height card
        profileHeightCard.setOnClickListener(v -> showHeightDialog());

        // Set an OnClickListener for the weight card
        profileWeightCard.setOnClickListener(v -> showWeightDialog());

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

    private void setupBloodTypeDialog() {
        bloodTypeDialog = new Dialog(requireContext());

        // Inflate the layout for the dialog
        View dialogView = getLayoutInflater().inflate(R.layout.blood_type_dialog, null);
        bloodTypeDialog.setContentView(dialogView);

        Spinner spinnerBloodType = bloodTypeDialog.findViewById(R.id.spinnerBloodType);
        Button btnSelectBloodType = bloodTypeDialog.findViewById(R.id.btnSelectBloodType);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.blood_types, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinnerBloodType.setAdapter(adapter);

        // Set up a listener to handle item selection
        spinnerBloodType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle the selected blood type
                String selectedBloodType = (String) parentView.getItemAtPosition(position);
                // You can use the selected blood type as needed
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });

        btnSelectBloodType.setOnClickListener(v -> {
            // Update the ViewModel with the selected blood type
            String selectedBloodType = (String) spinnerBloodType.getSelectedItem();
            notificationsViewModel.setBloodType(selectedBloodType);

            // Update the EditText in your CardView with the selected blood type
            EditText editText = requireView().findViewById(R.id.profileBlood);
            editText.setText(selectedBloodType);
            bloodTypeDialog.dismiss();
        });
    }
    private void showBloodTypeDialog() {
        if (bloodTypeDialog != null) {
            bloodTypeDialog.show();
        }
    }

    private void setupHeightDialog() {
        heightDialog = new Dialog(requireContext());

        // Inflate the layout for the dialog
        View dialogView = getLayoutInflater().inflate(R.layout.height_dialog, null);
        heightDialog.setContentView(dialogView);

        EditText editTextHeight = heightDialog.findViewById(R.id.editTextHeight);
        Button btnSelectHeight = heightDialog.findViewById(R.id.btnSelectHeight);

        btnSelectHeight.setOnClickListener(v -> {
            // Update the ViewModel with the entered height
            String enteredHeight = editTextHeight.getText().toString();
            // You can use the entered height as needed

            // Update the EditText in your CardView with the entered height
            EditText editText = requireView().findViewById(R.id.profileHeight);
            editText.setText(enteredHeight);

            heightDialog.dismiss();
        });
    }

    private void showHeightDialog() {
        if (heightDialog != null) {
            heightDialog.show();
        }
    }

    private void setupWeightDialog() {
        weightDialog = new Dialog(requireContext());

        // Inflate the layout for the dialog
        View dialogView = getLayoutInflater().inflate(R.layout.weight_dialog, null);
        weightDialog.setContentView(dialogView);

        EditText editTextWeight = weightDialog.findViewById(R.id.editTextWeight);
        Button btnSelectWeight = weightDialog.findViewById(R.id.btnSelectWeight);

        btnSelectWeight.setOnClickListener(v -> {
            // Update the ViewModel with the entered weight
            String enteredWeight = editTextWeight.getText().toString();
            // You can use the entered weight as needed

            // Update the EditText in your CardView with the entered weight
            EditText editText = requireView().findViewById(R.id.profileWeight);
            editText.setText(enteredWeight);

            weightDialog.dismiss();
        });
    }

    private void showWeightDialog() {
        if (weightDialog != null) {
            weightDialog.show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}