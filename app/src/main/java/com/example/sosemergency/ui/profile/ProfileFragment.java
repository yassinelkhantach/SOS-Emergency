package com.example.sosemergency.ui.profile;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import com.example.sosemergency.entities.Allergy;
import com.example.sosemergency.entities.User;
import com.example.sosemergency.utils.AllergyPersistenceManager;
import com.example.sosemergency.utils.DateConverterUtil;
import com.example.sosemergency.utils.UserPersistenceManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

    // Check icons for Allergies
    private ImageView checkApple;
    private ImageView checkGrape;
    private ImageView checkStrawberry;
    private ImageView checkOrange;
    private ImageView checkBanana;
    private ImageView checkFish;
    private ImageView checkOthers;

    // Edit and save icons for allergies
    private ImageView allergiesEditIcon;
    private ImageView allergiesSaveIcon;

    // Edit mode flag
    private boolean isEditMode = false;

    //Map to store allergies

    private static Map<String, Boolean> allergyStates = new HashMap<>();
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
        final EditText profileBlood = binding.profileBlood;
        final CardView profileHeightCard = binding.profileHeightCard;
        final EditText profileHeight = binding.profileHeight;
        final CardView profileWeightCard = binding.profileWeightCard;
        final EditText profileWeight = binding.profileWeight;

        //initialize User Informations
        profileFullName.setText(UserPersistenceManager.getUser().getName().toString());
        profileBD.setText(DateConverterUtil.formatDateToString(UserPersistenceManager.getUser().getBirthDate()));
        profileAge.setText(String.valueOf(calculateAge(UserPersistenceManager.getUser().getBirthDate(),new Date())));
        if(UserPersistenceManager.getUser().getBloodType() != null){
            profileBlood.setText(UserPersistenceManager.getUser().getBloodType());
        }
        if(UserPersistenceManager.getUser().getHeight() != 0.0){
            profileHeight.setText(String.valueOf(UserPersistenceManager.getUser().getHeight()));
        }
        if(UserPersistenceManager.getUser().getWeight() != 0.0){
            profileWeight.setText(String.valueOf(UserPersistenceManager.getUser().getWeight()));
        }
        Log.i("allergies", AllergyPersistenceManager.getAllergies().toString());

        // Fetch all allergies and selected allergies
        List<Allergy> allAllergies = AllergyPersistenceManager.getAllergies();
        // Iterate over all allergies and show check icons for selected ones
        for (Allergy allergy : allAllergies) {
            allergyStates.put(allergy.getName(), true);
        }

        // Initialize the allergies
        appleAllergie = binding.appleAllergie;
        grapeAllergie = binding.grapeAllergie;
        strawberryAllergie = binding.strawberryAllergie;
        orangeAllergie = binding.orangeAllergie;
        bananaAllergie = binding.bananaAllergie;
        fishAllergie = binding.fishAllergie;
        otherAllergie = binding.otherAllergie;

        //Initialize check icons for Allergies
        checkApple = binding.checkApple;
        checkGrape = binding.checkGrape;
        checkStrawberry = binding.checkStrawberry;
        checkOrange = binding.checkOrange;
        checkBanana = binding.checkBanana;
        checkFish = binding.checkFish;
        checkOthers = binding.checkOthers;

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

        // save editable user informations
        User editableUser = (UserPersistenceManager.getUser());
        editableUser.setName(modifiedFullName);
        editableUser.setBirthDate(DateConverterUtil.parseDate(modifiedDOB));
        UserPersistenceManager.editUser(editableUser);
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
            if (selectedBloodType != null && !selectedBloodType.isEmpty()) {
                // save editable user blood type
                User editableUser = (UserPersistenceManager.getUser());
                editableUser.setBloodType(selectedBloodType);
                UserPersistenceManager.editUser(editableUser);
            }
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

            if (enteredHeight != null && !enteredHeight.isEmpty()) {
                // save editable user Height
                User editableUser = (UserPersistenceManager.getUser());
                editableUser.setHeight(Double.parseDouble(enteredHeight));
                UserPersistenceManager.editUser(editableUser);
            }

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
        if (heightDialog != null && !heightDialog.isShowing()) {
            // Get the height from the database
            double currentHeight = UserPersistenceManager.getUser().getHeight();

            // Inflate the layout for the dialog
            View dialogView = getLayoutInflater().inflate(R.layout.height_dialog, null);
            heightDialog.setContentView(dialogView);

            // Find views in the dialog layout
            EditText editTextHeight = heightDialog.findViewById(R.id.editTextHeight);

            // If the height is not 0.0, set it in the EditText, otherwise set a default text
            if (Double.compare(currentHeight, 0.0) != 0) {
                editTextHeight.setText(String.valueOf(currentHeight));
            }

            // Set up a listener for the button to confirm the entered height
            Button btnSelectHeight = heightDialog.findViewById(R.id.btnSelectHeight);
            btnSelectHeight.setOnClickListener(v -> {
                // Update the ViewModel with the entered height
                String enteredHeight = editTextHeight.getText().toString();

                // Update the EditText in your CardView with the entered height
                EditText editText = requireView().findViewById(R.id.profileHeight);
                editText.setText(enteredHeight);

                if (!enteredHeight.isEmpty()) {
                    // save editable user Height
                    User editableUser = UserPersistenceManager.getUser();
                    editableUser.setHeight(Double.parseDouble(enteredHeight));
                    UserPersistenceManager.editUser(editableUser);
                }

                // Dismiss the height selection dialog
                heightDialog.dismiss();
            });

            // Show the height selection dialog
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

            if (enteredWeight != null && !enteredWeight.isEmpty()) {
                // save editable user Height
                User editableUser = (UserPersistenceManager.getUser());
                editableUser.setWeight(Double.parseDouble(enteredWeight));
                UserPersistenceManager.editUser(editableUser);
            }

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
        if (weightDialog != null && !weightDialog.isShowing()) {
            // Get the weight from the database
            double currentWeight = UserPersistenceManager.getUser().getWeight();

            // Inflate the layout for the dialog
            View dialogView = getLayoutInflater().inflate(R.layout.weight_dialog, null);
            weightDialog.setContentView(dialogView);

            // Find views in the dialog layout
            EditText editTextWeight = weightDialog.findViewById(R.id.editTextWeight);

            // If the weight is not 0.0, set it in the EditText, otherwise set a default text
            if (Double.compare(currentWeight, 0.0) != 0) {
                editTextWeight.setText(String.valueOf(currentWeight));
            }

            // Set up a listener for the button to confirm the entered weight
            Button btnSelectWeight = weightDialog.findViewById(R.id.btnSelectWeight);
            btnSelectWeight.setOnClickListener(v -> {
                // Update the ViewModel with the entered weight
                String enteredWeight = editTextWeight.getText().toString();

                // Update the EditText in your CardView with the entered weight
                EditText editText = requireView().findViewById(R.id.profileWeight);
                editText.setText(enteredWeight);

                if (!enteredWeight.isEmpty()) {
                    // Save editable user weight
                    User editableUser = UserPersistenceManager.getUser();
                    editableUser.setWeight(Double.parseDouble(enteredWeight));
                    UserPersistenceManager.editUser(editableUser);
                }

                // Dismiss the weight selection dialog
                weightDialog.dismiss();
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
        setCardClickability(appleAllergie, checkApple, "apple", isClickable);
        setCardClickability(grapeAllergie, checkGrape, "grape", isClickable);
        setCardClickability(strawberryAllergie, checkStrawberry, "strawberry", isClickable);
        setCardClickability(orangeAllergie, checkOrange, "orange", isClickable);
        setCardClickability(bananaAllergie, checkBanana, "banana", isClickable);
        setCardClickability(fishAllergie, checkFish, "fish", isClickable);
        setCardClickability(otherAllergie, checkOthers, "others", isClickable);
    }

    /**
     * Set up click listeners for each allergy card.
     * This method assigns click listeners to each allergy card to toggle their selection.
     */
    private void setupAllergyClickListeners() {
        // Set click listener for the Apple Allergy card
        setupAllergyClickListener(appleAllergie, checkApple, "apple");

        // Set click listener for the Grape Allergy card
        setupAllergyClickListener(grapeAllergie, checkGrape, "grape");

        // Set click listener for the Strawberry Allergy card
        setupAllergyClickListener(strawberryAllergie, checkStrawberry, "strawberry");

        // Set click listener for the Orange Allergy card
        setupAllergyClickListener(orangeAllergie, checkOrange, "orange");

        // Set click listener for the Banana Allergy card
        setupAllergyClickListener(bananaAllergie, checkBanana, "banana");

        // Set click listener for the Fish Allergy card
        setupAllergyClickListener(fishAllergie, checkFish, "fish");

        // Set click listener for the Others Allergy card
        setupAllergyClickListener(otherAllergie, checkOthers, "others");
    }

    /**
     * Set up click listener for a specific allergy card.
     *
     * @param allergyCard The CardView representing the allergy.
     * @param checkIcon   The ImageView representing the check icon for the allergy.
     * @param allergyKey  A unique identifier for the allergy.
     */
    private void setupAllergyClickListener(CardView allergyCard, ImageView checkIcon, String allergyKey) {
        // Load the selected state for the allergy
        loadAllergyState(allergyCard, checkIcon, allergyKey);

        // Set click listener for the allergy card
        allergyCard.setOnClickListener(v -> toggleAllergySelection(checkIcon, allergyKey));
    }

    /**
     * Set the clickability of an allergy card.
     *
     * @param allergyCard The CardView representing the allergy.
     * @param checkIcon   The ImageView representing the check icon for the allergy.
     * @param allergyKey  A unique identifier for the allergy.
     * @param isClickable A boolean indicating whether the card should be clickable or not.
     */
    private void setCardClickability(CardView allergyCard, ImageView checkIcon, String allergyKey, boolean isClickable) {
        allergyCard.setClickable(isClickable);

        // Load the selected state for the allergy
        loadAllergyState(allergyCard, checkIcon, allergyKey);
    }

    /**
     * Load the selected state of an allergy.
     *
     * @param allergyCard The CardView representing the allergy.
     * @param checkIcon   The ImageView representing the check icon for the allergy.
     * @param allergyKey  A unique identifier for the allergy.
     */
    private void loadAllergyState(CardView allergyCard, ImageView checkIcon, String allergyKey) {
        // Load the selected state from the Map
        Boolean isSelected = allergyStates.get(allergyKey);

        // Set the visibility of the check icon based on the loaded state
        checkIcon.setVisibility(isSelected != null && isSelected ? View.VISIBLE : View.GONE);
    }

    /**
     * Toggle the selection of an allergy.
     *
     * @param checkIcon   The ImageView representing the check icon for the allergy.
     * @param allergyKey  A unique identifier for the allergy.
     */
    private void toggleAllergySelection(ImageView checkIcon, String allergyKey) {
        if(isEditMode){
        // Toggle the visibility of the check icon
        if (checkIcon.getVisibility() == View.GONE) {
            // Show the check icon if it's currently gone
            checkIcon.setVisibility(View.VISIBLE);

            // Save the selected state
            saveAllergyState(allergyKey, true);
        } else {
            // Hide the check icon if it's currently visible
            checkIcon.setVisibility(View.GONE);

            // Save the selected state
            saveAllergyState(allergyKey, false);
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

        // Get the user
        User user = UserPersistenceManager.getUser();

        // Update user's allergies based on the selected allergies
        for (Map.Entry<String, Boolean> entry : allergyStates.entrySet()) {
            String allergyKey = entry.getKey();
            boolean isSelected = entry.getValue();

            // Check if the allergy is selected and not already present in user's allergies
            if (isSelected && !userHasAllergy(user, allergyKey)) {
                // Add the selected allergy to the user's allergies
                Allergy selectedAllergy = new Allergy(allergyKey, "");
                selectedAllergy.setUserId(user.getId());
                AllergyPersistenceManager.insertAllergy(selectedAllergy);
            } else if (!isSelected && userHasAllergy(user, allergyKey)) {
                // Remove the allergy from the user's allergies if it's not selected
                Allergy removedAllergy = findUserAllergy(user, allergyKey);
                AllergyPersistenceManager.deleteAllergy(removedAllergy);
            }
        }

        // Log the selected allergies
        for (Map.Entry<String, Boolean> entry : allergyStates.entrySet()) {
            String allergyKey = entry.getKey();
            boolean isSelected = entry.getValue();
            Log.d("SelectedAllergies", "Allergy: " + allergyKey + ", Selected: " + isSelected);
        }
    }

    /**
     * Check if the user already has the given allergy.
     *
     * @param user       The user object.
     * @param allergyKey The unique identifier for the allergy.
     * @return True if the user has the allergy, false otherwise.
     */
    private boolean userHasAllergy(User user, String allergyKey) {
        for (Allergy allergy : AllergyPersistenceManager.getAllergies()) {
            if (allergy.getName().equals(allergyKey)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Find the allergy object in the user's allergies based on the allergy key.
     *
     * @param user       The user object.
     * @param allergyKey The unique identifier for the allergy.
     * @return The found allergy object or null if not found.
     */
    private Allergy findUserAllergy(User user, String allergyKey) {
        for (Allergy allergy : AllergyPersistenceManager.getAllergies()) {
            if (allergy.getName().equals(allergyKey)) {
                return allergy;
            }
        }
        return null;
    }


    /**
     * Saves the selected state of an allergy during the app's lifecycle.
     *
     * @param allergyKey   A unique identifier for the allergy.
     *                     This key is used to distinguish between different allergies.
     * @param isSelected   A boolean indicating whether the allergy is selected or not.
     *                     True represents the allergy is selected, and false represents it is not selected.
     */
    private void saveAllergyState(String allergyKey, boolean isSelected) {
        // Save the selected state in the Map
        allergyStates.put(allergyKey, isSelected);
    }

    private ImageView getCheckIconForAllergy(Allergy allergy) {
        switch (allergy.getName()) {
            case "apple":
                return binding.checkApple;
            case "grape":
                return binding.checkGrape;
            case "strawberry":
                return binding.checkStrawberry;
            case "orange":
                return binding.checkOrange;
            case "banana":
                return binding.checkBanana;
            case "fish":
                return binding.checkFish;
            case "others":
                return binding.checkOthers;
            default:
                return null;
        }
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