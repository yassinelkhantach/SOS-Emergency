package com.example.sosemergency.ui.profile;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.sosemergency.R;
import com.example.sosemergency.databinding.FragmentProfileBinding;

    public class ProfileFragment extends Fragment {

        private FragmentProfileBinding binding;
        private ProfileViewModel notificationsViewModel;

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
            final ImageView profileEditIcon = binding.profileEditIcon;
            final ImageView profileSaveIcon = binding.profileSaveIcon;

            // Use lambda expression for click listeners
            profileEditIcon.setOnClickListener(v -> enableEditing(profileFullName, profileEditIcon, profileSaveIcon));
            profileSaveIcon.setOnClickListener(v -> saveChanges(profileFullName, profileEditIcon, profileSaveIcon));

            // Observe the LiveData directly
            notificationsViewModel.getText().observe(getViewLifecycleOwner(), profileFullName::setText);

            return root;
        }

        private void enableEditing(EditText editText, ImageView editIcon, ImageView saveIcon) {
            // Make the TextView editable
            editText.setFocusableInTouchMode(true);
            editText.setFocusable(true);
            editText.requestFocus();
            editText.setSelection(editText.getText().length());

            // Set a text color while activate editing
            editText.setTextColor(ContextCompat.getColor(getContext(), R.color.primary));

            // Show the Keybord
            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);

            // Show the save icon and hide the edit icon
            editIcon.setVisibility(View.GONE);
            saveIcon.setVisibility(View.VISIBLE);
        }

        private void saveChanges(EditText editText, ImageView editIcon, ImageView saveIcon) {
            // Save the modified text
            String modifiedFullName = editText.getText().toString();

            // Update the ViewModel with the modified text
            notificationsViewModel.setText(modifiedFullName);

            // Set a text color when saving
            editText.setTextColor(ContextCompat.getColor(getContext(), R.color.navbar_text));

            // Make the TextView non-editable again
            editText.setFocusable(false);
            editText.setFocusableInTouchMode(false);

            // Hide the Keybord
            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

            // Show the edit icon and hide the save icon
            editIcon.setVisibility(View.VISIBLE);
            saveIcon.setVisibility(View.GONE);
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            binding = null;
        }
    }
