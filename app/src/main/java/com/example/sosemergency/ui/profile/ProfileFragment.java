package com.example.sosemergency.ui.profile;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
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
import java.util.Locale;

/**
 * ProfileFragment - Fragment for displaying and editing user profile information.
 *
 * This fragment allows the user to view and edit their profile details, including full name,
 * date of birth, age, blood type, height, weight, and allergies. The user can toggle between
 * edit mode and view mode, and can save changes made in edit mode.
 */
public class ProfileFragment extends Fragment {

    // Data binding
    private FragmentProfileBinding binding;

    // ViewModel for profile information
    private ProfileViewModel notificationsViewModel;

    // Date picker listener
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    // Dialogs for blood type, height, and weight selection
    private Dialog bloodTypeDialog;
    private Dialog heightDialog;
    private Dialog weightDialog;

    // UI elements for allergies
    private CardView appleAllergie;
    private CardView grapeAllergie;
    private CardView strawberryAllergie;
    private CardView orangeAllergie;
    private CardView bananaAllergie;
    private CardView fishAllergie;
    private CardView otherAllergie;

    // Edit and save icons for allergies
    private ImageView allergiesEditIcon;
    private ImageView allergiesSaveIcon;

    // Edit mode flag
    private boolean isEditMode = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize the ViewModel
        notificationsViewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Set the ActionBar title
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle(R.string.title_profile);

        // Inflate the layout using data binding
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize UI elements
        final EditText profileFullName = binding.profileFullName;
        final EditText profileBD = binding.profileBD;
        final ImageView profileEditIcon = binding.profileEditIcon;
        final ImageView profileSaveIcon = binding.profileSaveIcon;
        final EditText profileAge = binding.profileAge;
        final CardView profileBloodCard = binding.profileBloodCard;
        final CardView profileHeightCard = binding.profileHeightCard;
        final CardView profileWeightCard = binding.profileWeightCard;

        // Initialize the allergies
        appleAllergie = binding.appleAllergie;
        grapeAllergie = binding.grapeAllergie;
        strawberryAllergie = binding.strawberryAllergie;
        orangeAllergie = binding.orangeAllergie;
        bananaAllergie = binding.bananaAllergie;
        fishAllergie = binding.fishAllergie;
        otherAllergie = binding.otherAllergie;

        // Initialize the edit and save icons for allergies
        allergiesEditIcon = binding.AllergiesEditIcon;
        allergiesSaveIcon = binding.AllergiesSaveIcon;

        // Set click listeners for the edit and save icons for allergies
        allergiesEditIcon.setOnClickListener(v -> toggleAllergiesEditMode());
        allergiesSaveIcon.setOnClickListener(v -> saveSelectedAllergies());

        // Set click listeners for each allergy card
        setupAllergyClickListeners();

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

    /**
     * Show a date picker dialog for selecting a date.
     *
     * @param profileDate The EditText where the selected date will be displayed.
     */
    private void showDatePicker(EditText profileDate) {
        // Get the current date
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        // If profileDate has a valid date, use it as the initial date for the DatePicker
        String dateString = profileDate.getText().toString();
        if (!TextUtils.isEmpty(dateString)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            try {
                Date date = dateFormat.parse(dateString);
                cal.setTime(date);
                year = cal.get(Calendar.YEAR);
                month = cal.get(Calendar.MONTH);
                day = cal.get(Calendar.DAY_OF_MONTH);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // Create a DatePickerDialog with a custom theme and set the date listener
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), R.style.DatePickerTheme, mDateSetListener, year, month, day);

        // Set the background of the dialog to white
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        // Show the date picker dialog
        dialog.show();
    }

    /**
     * Calculate the age based on the date of birth and the current date.
     *
     * @param dateOfBirth The date of birth.
     * @param dateOfNow   The current date.
     * @return The calculated age.
     */
    private int calculateAge(Date dateOfBirth, Date dateOfNow) {
        // Create Calendar instances for the date of birth and the current date
        Calendar dob = Calendar.getInstance();
        dob.setTime(dateOfBirth);

        Calendar now = Calendar.getInstance();
        now.setTime(dateOfNow);

        // Calculate the age based on the difference in years
        int age = now.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        // Adjust the age if the birthday hasn't occurred yet in the current year
        if (now.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        return age;
    }

    /**
     * Set up and initialize the blood type selection dialog.
     * The dialog allows the user to choose their blood type from a spinner.
     */
    private void setupBloodTypeDialog() {
        // Create a new Dialog instance for blood type selection
        bloodTypeDialog = new Dialog(requireContext());

        // Inflate the layout for the dialog
        View dialogView = getLayoutInflater().inflate(R.layout.blood_type_dialog, null);
        bloodTypeDialog.setContentView(dialogView);

        // Find views in the dialog layout
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
            }
        });

        /**
         * OnClickListener for the button to confirm the selected blood type in the blood type dialog.
         * This method updates the ViewModel with the selected blood type, updates the associated EditText,
         * and dismisses the blood type selection dialog.
         */
        btnSelectBloodType.setOnClickListener(v -> {
            // Update the ViewModel with the selected blood type
            String selectedBloodType = (String) spinnerBloodType.getSelectedItem();
            notificationsViewModel.setBloodType(selectedBloodType);

            // Update the EditText in your CardView with the selected blood type
            EditText editText = requireView().findViewById(R.id.profileBlood);
            editText.setText(selectedBloodType);

            // Dismiss the blood type selection dialog
            bloodTypeDialog.dismiss();
        });
    }
    /**
     * Show the blood type selection dialog.
     * This method checks if the bloodTypeDialog is not null and then shows the dialog.
     */
    private void showBloodTypeDialog() {
        // Check if the blood type dialog is not null
        if (bloodTypeDialog != null) {
            // Get the default blood type from your ViewModel or any other source
            String defaultBloodType = String.valueOf(notificationsViewModel.getBloodType()); // Adjust this based on your ViewModel

            // Set the default blood type in the spinner
            if (defaultBloodType != null) {
                Spinner spinnerBloodType = bloodTypeDialog.findViewById(R.id.spinnerBloodType);
                ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinnerBloodType.getAdapter();

                for (int i = 0; i < adapter.getCount(); i++) {
                    if (defaultBloodType.equals(adapter.getItem(i))) {
                        spinnerBloodType.setSelection(i);
                        break;
                    }
                }
            }

            // Show the blood type selection dialog
            bloodTypeDialog.show();
        }
    }



    /**
     * Set up and initialize the height selection dialog.
     * The dialog allows the user to enter their height and save the selection.
     */
    private void setupHeightDialog() {
        // Create a new Dialog instance for height selection
        heightDialog = new Dialog(requireContext());

        // Inflate the layout for the dialog
        View dialogView = getLayoutInflater().inflate(R.layout.height_dialog, null);
        heightDialog.setContentView(dialogView);

        // Find views in the dialog layout
        EditText editTextHeight = heightDialog.findViewById(R.id.editTextHeight);
        Button btnSelectHeight = heightDialog.findViewById(R.id.btnSelectHeight);

        // Set up a listener for the button to confirm the entered height
        btnSelectHeight.setOnClickListener(v -> {
            // Update the ViewModel with the entered height
            String enteredHeight = editTextHeight.getText().toString();

            // Update the EditText in your CardView with the entered height
            EditText editText = requireView().findViewById(R.id.profileHeight);
            editText.setText(enteredHeight);

            // Dismiss the height selection dialog
            heightDialog.dismiss();
        });
    }

    /**
     * Show the height selection dialog.
     * This method checks if the heightDialog is not null and then shows the dialog.
     */
    private void showHeightDialog() {
        // Check if the height selection dialog is not null
        if (heightDialog != null) {
            // Observe the height LiveData
            notificationsViewModel.getHeight().observe(getViewLifecycleOwner(), currentHeight -> {
                // Set the current weight in the EditText
                EditText editTextHeight = heightDialog.findViewById(R.id.editTextHeight);
                editTextHeight.setText(currentHeight);
            });

            // Show the weight selection dialog
            heightDialog.show();
        }

    }

    /**
     * Set up and initialize the weight selection dialog.
     * The dialog allows the user to enter their weight and save the selection.
     */
    private void setupWeightDialog() {
        // Create a new Dialog instance for weight selection
        weightDialog = new Dialog(requireContext());

        // Inflate the layout for the dialog
        View dialogView = getLayoutInflater().inflate(R.layout.weight_dialog, null);
        weightDialog.setContentView(dialogView);

        // Find views in the dialog layout
        EditText editTextWeight = weightDialog.findViewById(R.id.editTextWeight);
        Button btnSelectWeight = weightDialog.findViewById(R.id.btnSelectWeight);

        // Set up a listener for the button to confirm the entered weight
        btnSelectWeight.setOnClickListener(v -> {
            // Update the ViewModel with the entered weight
            String enteredWeight = editTextWeight.getText().toString();

            // Update the EditText in your CardView with the entered weight
            EditText editText = requireView().findViewById(R.id.profileWeight);
            editText.setText(enteredWeight);

            // Dismiss the weight selection dialog
            weightDialog.dismiss();
        });
    }

    /**
     * Show the weight selection dialog.
     * This method checks if the weightDialog is not null and then shows the dialog.
     */
    private void showWeightDialog() {
        // Check if the weight selection dialog is not null
        if (weightDialog != null) {
            // Observe the weight LiveData
            notificationsViewModel.getWeight().observe(getViewLifecycleOwner(), currentWeight -> {
                // Set the current weight in the EditText
                EditText editTextWeight = weightDialog.findViewById(R.id.editTextWeight);
                editTextWeight.setText(currentWeight);
            });

            // Show the weight selection dialog
            weightDialog.show();
        }
    }


    /**
     * Toggle the allergies edit mode.
     * This method toggles the edit mode, showing or hiding the save and edit icons,
     * and enabling or disabling click listeners based on the edit mode.
     */
    private void toggleAllergiesEditMode() {
        // Toggle the edit mode
        isEditMode = !isEditMode;

        // Show or hide the save icon based on the edit mode
        allergiesSaveIcon.setVisibility(isEditMode ? View.VISIBLE : View.GONE);

        // Show or hide the edit icon based on the edit mode
        allergiesEditIcon.setVisibility(isEditMode ? View.GONE : View.VISIBLE);

        // Enable or disable click listeners based on the edit mode
        setAllergiesClickability(isEditMode);
    }

    /**
     * Set the clickability of allergy cards.
     *
     * @param isClickable A boolean indicating whether the cards should be clickable or not.
     */
    private void setAllergiesClickability(boolean isClickable) {
        // Set the clickability of each allergy card based on the provided boolean
        appleAllergie.setClickable(isClickable);
        grapeAllergie.setClickable(isClickable);
        strawberryAllergie.setClickable(isClickable);
        orangeAllergie.setClickable(isClickable);
        bananaAllergie.setClickable(isClickable);
        fishAllergie.setClickable(isClickable);
        otherAllergie.setClickable(isClickable);
    }

    /**
     * Set up click listeners for each allergy card.
     * This method assigns click listeners to each allergy card to toggle their selection.
     */
    private void setupAllergyClickListeners() {
        // Set click listener for the Apple Allergy card
        appleAllergie.setOnClickListener(v -> toggleAllergySelection(appleAllergie));

        // Set click listener for the Grape Allergy card
        grapeAllergie.setOnClickListener(v -> toggleAllergySelection(grapeAllergie));

        // Set click listener for the Strawberry Allergy card
        strawberryAllergie.setOnClickListener(v -> toggleAllergySelection(strawberryAllergie));

        // Set click listener for the Orange Allergy card
        orangeAllergie.setOnClickListener(v -> toggleAllergySelection(orangeAllergie));

        // Set click listener for the Banana Allergy card
        bananaAllergie.setOnClickListener(v -> toggleAllergySelection(bananaAllergie));

        // Set click listener for the Fish Allergy card
        fishAllergie.setOnClickListener(v -> toggleAllergySelection(fishAllergie));

        // Set click listener for the Other Allergy card
        otherAllergie.setOnClickListener(v -> toggleAllergySelection(otherAllergie));
    }

    /**
     * Toggle the selection state of an allergy card when in edit mode.
     *
     * @param allergyCard The CardView representing an allergy.
     */
    private void toggleAllergySelection(CardView allergyCard) {
        // Check if the activity is in edit mode
        if (isEditMode) {
            // Toggle the background color between white and the default color
            int defaultColor = ContextCompat.getColor(requireContext(), R.color.extra_white);
            int selectedColor = ContextCompat.getColor(requireContext(), R.color.white);

            // Check the current color of the allergy card background
            if (allergyCard.getCardBackgroundColor().getDefaultColor() == defaultColor) {
                // Set the background color to the selected color if not already selected
                allergyCard.setCardBackgroundColor(selectedColor);
            } else {
                // Set the background color to the default color if already selected
                allergyCard.setCardBackgroundColor(defaultColor);
            }
        }
    }

    /**
     * Save the selected allergies and update UI accordingly.
     */
    private void saveSelectedAllergies() {

        // After saving, hide the save icon and show the edit icon
        allergiesSaveIcon.setVisibility(View.GONE);
        allergiesEditIcon.setVisibility(View.VISIBLE);

        // Enable or disable click listeners based on the edit mode
        setAllergiesClickability(false);
    }

    /**
     * Lifecycle method called when the view is about to be destroyed.
     * It is responsible for cleaning up references to the view binding.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}