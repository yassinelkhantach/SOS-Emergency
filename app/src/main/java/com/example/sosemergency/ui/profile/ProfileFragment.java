package com.example.sosemergency.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_profile);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView profileFullName = binding.profileFullName;
        final ImageView profileEditIcon = binding.profileEditIcon;
        final ImageView profileSaveIcon = binding.profileSaveIcon;

        notificationsViewModel.getText().observe(getViewLifecycleOwner(), profileFullName::setText);

        profileEditIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Make the TextView editable
                profileFullName.setFocusableInTouchMode(true);
                profileFullName.setFocusable(true);
                profileFullName.requestFocus();

                // Show the save icon and hide the edit icon
                profileEditIcon.setVisibility(View.GONE);
                profileSaveIcon.setVisibility(View.VISIBLE);
            }
        });

        profileSaveIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save the modified text
                String modifiedFullName = profileFullName.getText().toString();

                // Update the ViewModel with the modified text
                notificationsViewModel.setText(modifiedFullName);

                // Perform your save logic here if needed

                // Make the TextView non-editable again
                profileFullName.setFocusable(false);
                profileFullName.setFocusableInTouchMode(false);

                // Show the edit icon and hide the save icon
                profileEditIcon.setVisibility(View.VISIBLE);
                profileSaveIcon.setVisibility(View.GONE);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
