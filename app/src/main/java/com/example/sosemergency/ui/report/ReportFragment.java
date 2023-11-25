package com.example.sosemergency.ui.report;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.sosemergency.R;
import com.example.sosemergency.databinding.FragmentReportBinding;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class ReportFragment extends Fragment implements OnMapReadyCallback {

    private FragmentReportBinding binding;
    private RadioGroup radioGroup;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_report);
        ReportViewModel reportViewModel =
                new ViewModelProvider(this).get(ReportViewModel.class);

        binding = FragmentReportBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textReport;
        reportViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // Find the RadioGroup in your layout
        radioGroup = root.findViewById(R.id.radioGroup);

        // Set a listener for the radio group
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d("RadioButtons", "Checked ID: " + checkedId);
                updateIconColors(root,checkedId);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.report_map);
        mapFragment.getMapAsync(this);

        return root;
    }

    private void updateIconColors(View root,int checkedId) {
        Log.d("RadioButtons", "Checked ID: " + checkedId);
        RadioButton reportFireButton = root.findViewById(R.id.report_fire_button);
        RadioButton reportAccidentButton = root.findViewById(R.id.report_accident_button);
        RadioButton reportEarthquakeButton = root.findViewById(R.id.report_earthquake_button);
        RadioButton reportCrimeButton = root.findViewById(R.id.report_crime_button);
        RadioButton reportFloodButton = root.findViewById(R.id.report_flood_button);
        RadioButton reportBriberyButton = root.findViewById(R.id.report_bribery_button);

        // Reset color filter for all buttons
        reportFireButton.getCompoundDrawables()[1].setColorFilter(null);
        reportAccidentButton.getCompoundDrawables()[1].setColorFilter(null);
        reportEarthquakeButton.getCompoundDrawables()[1].setColorFilter(null);
        reportCrimeButton.getCompoundDrawables()[1].setColorFilter(null);
        reportFloodButton.getCompoundDrawables()[1].setColorFilter(null);
        reportBriberyButton.getCompoundDrawables()[1].setColorFilter(null);

        // Set color filter for the checked button
        if (checkedId == R.id.report_fire_button) {
            int color = ContextCompat.getColor(requireContext(), R.color.danger);
            reportFireButton.getCompoundDrawables()[1].setColorFilter(color, PorterDuff.Mode.SRC_IN);
        } else if (checkedId == R.id.report_accident_button) {
            int color = ContextCompat.getColor(requireContext(), R.color.danger);
            reportAccidentButton.getCompoundDrawables()[1].setColorFilter(color, PorterDuff.Mode.SRC_IN);
        } else if (checkedId == R.id.report_earthquake_button) {
            int color = ContextCompat.getColor(requireContext(), R.color.danger);
            reportEarthquakeButton.getCompoundDrawables()[1].setColorFilter(color, PorterDuff.Mode.SRC_IN);
        } else if (checkedId == R.id.report_crime_button) {
            int color = ContextCompat.getColor(requireContext(), R.color.danger);
            reportCrimeButton.getCompoundDrawables()[1].setColorFilter(color, PorterDuff.Mode.SRC_IN);
        } else if (checkedId == R.id.report_flood_button) {
            int color = ContextCompat.getColor(requireContext(), R.color.danger);
            reportFloodButton.getCompoundDrawables()[1].setColorFilter(color, PorterDuff.Mode.SRC_IN);
        } else if (checkedId == R.id.report_bribery_button) {
            int color = ContextCompat.getColor(requireContext(), R.color.danger);
            reportBriberyButton.getCompoundDrawables()[1].setColorFilter(color, PorterDuff.Mode.SRC_IN);
        }

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

    }
}
