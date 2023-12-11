package com.example.sosemergency.ui.setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.sosemergency.BootstrapActivity;
import com.example.sosemergency.R;
import com.example.sosemergency.databinding.FragmentSettingsBinding;

public class SettingFragment extends Fragment {

    private FragmentSettingsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_settings);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            requireActivity().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        hideBottomNavigationBar();
    }

    @Override
    public void onPause() {
        super.onPause();
        showBottomNavigationBar();
    }

    private void hideBottomNavigationBar() {
        if (getActivity() != null && getActivity() instanceof BootstrapActivity) {
            BootstrapActivity bootstrapActivity = (BootstrapActivity) getActivity();
            bootstrapActivity.hideBottomNavigationBar();
        }
    }

    private void showBottomNavigationBar() {
        if (getActivity() != null && getActivity() instanceof BootstrapActivity) {
            BootstrapActivity bootstrapActivity = (BootstrapActivity) getActivity();
            bootstrapActivity.showBottomNavigationBar();
        }
    }

}