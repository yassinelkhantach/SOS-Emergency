package com.example.sosemergency.ui.report;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.sosemergency.R;
import com.example.sosemergency.databinding.FragmentReportBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The ReportFragment class represents the fragment responsible for reporting emergencies.
 * It includes functionality for location reporting, map interaction, and emergency type selection.
 */
public class ReportFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener, View.OnClickListener {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FragmentReportBinding binding;
    private RadioGroup radioGroup;
    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationClient;
    private Button reportLocationButton;
    private EditText report_search_location;
    private List<Marker> markers = new ArrayList<>();
    private RadioButton reportFireButton;
    private RadioButton reportAccidentButton;
    private RadioButton reportEarthquakeButton;
    private RadioButton reportCrimeButton;
    private RadioButton reportFloodButton;
    private RadioButton reportBriberyButton;
    private boolean isMapReady = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle(R.string.title_report);
        ReportViewModel reportViewModel = new ViewModelProvider(this).get(ReportViewModel.class);

        binding = FragmentReportBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Initialize FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.report_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Set up the map click listener
        if (googleMap != null) {
            googleMap.setOnMapClickListener(this);
        }

        // Set up the EditText and button for address lookup
        report_search_location = view.findViewById(R.id.report_search_location);
        reportLocationButton = view.findViewById(R.id.report_location_button);

        // Add the click listener for the button
        reportLocationButton.setOnClickListener(v -> lookupAddress());

        // Find the RadioGroup in your layout
        radioGroup = view.findViewById(R.id.radioGroup);

        // Set click listener for each RadioButton
        reportFireButton = view.findViewById(R.id.report_fire_button);
        reportAccidentButton = view.findViewById(R.id.report_accident_button);
        reportEarthquakeButton = view.findViewById(R.id.report_earthquake_button);
        reportCrimeButton = view.findViewById(R.id.report_crime_button);
        reportFloodButton = view.findViewById(R.id.report_flood_button);
        reportBriberyButton = view.findViewById(R.id.report_bribery_button);

        reportFireButton.setOnClickListener(this);
        reportAccidentButton.setOnClickListener(this);
        reportEarthquakeButton.setOnClickListener(this);
        reportCrimeButton.setOnClickListener(this);
        reportFloodButton.setOnClickListener(this);
        reportBriberyButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        int checkedId = -1;

        // Identify which RadioButton was clicked
        if (v.getId() == R.id.report_fire_button) {
            checkedId = R.id.report_fire_button;
        } else if (v.getId() == R.id.report_accident_button) {
            checkedId = R.id.report_accident_button;
        } else if (v.getId() == R.id.report_earthquake_button) {
            checkedId = R.id.report_earthquake_button;
        } else if (v.getId() == R.id.report_crime_button) {
            checkedId = R.id.report_crime_button;
        } else if (v.getId() == R.id.report_flood_button) {
            checkedId = R.id.report_flood_button;
        } else if (v.getId() == R.id.report_bribery_button) {
            checkedId = R.id.report_bribery_button;
        }

        // Calling updateIconColors method to update colors and perform other actions
        updateIconColors(v, checkedId);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        // Customize your map here
        this.googleMap = googleMap;
        isMapReady = true;

        // Enable the My Location layer and the related control on the map
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
            // Set up the map click listener
            googleMap.setOnMapClickListener(this);
            // Set up the "My Location" button click listener
            googleMap.setOnMyLocationButtonClickListener(() -> {
                // Clear existing markers
                clearMarkers();
                // Load the current location on the map
                checkLocationPermissionAndLoadLocation();
                return true;
            });
            // Set up the "My Location" click listener
            googleMap.setOnMyLocationClickListener(location -> {
                // Clear existing markers
                clearMarkers();
                // Add a marker for the current location
                LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                Marker myLocationMarker = googleMap.addMarker(new MarkerOptions().position(currentLatLng).title("My Location"));
                markers.add(myLocationMarker);
            });

            // Load the current location on the map
            loadMyLocation();
        } else {
            // Request location permission from the user
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }


    @Override
    public void onMapClick(LatLng latLng) {
        // Clear existing markers
        clearMarkers();
        // Add a marker at the clicked position with latitude and longitude as the title
        String markerTitle = latLng.latitude + "," + latLng.longitude;
        Marker clickedMarker = googleMap.addMarker(new MarkerOptions().position(latLng).title(markerTitle));
        markers.add(clickedMarker);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, enable My Location layer
                if (googleMap != null) {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(requireActivity(),
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                LOCATION_PERMISSION_REQUEST_CODE);
                    }
                    googleMap.setMyLocationEnabled(true);
                }
            } else {
                // Permission denied, handle accordingly
                Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void centerMapOnMyLocation() {
        if (googleMap != null) {
            if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED) {
                // Enable My Location layer if permission granted
                googleMap.setMyLocationEnabled(true);

                // Get the last known location
                FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(requireActivity(), location -> {
                            if (location != null) {
                                LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f));
                            }
                        });
            } else {
                //TODO: We should handle the case where location permission is not granted
            }
        }
    }

    private void updateIconColors(View view, int checkedId) {
        // Reset color filter for all buttons
        resetColorFilter(reportFireButton);
        resetColorFilter(reportAccidentButton);
        resetColorFilter(reportEarthquakeButton);
        resetColorFilter(reportCrimeButton);
        resetColorFilter(reportFloodButton);
        resetColorFilter(reportBriberyButton);

        // Set color filter for the checked button
        RadioButton checkedButton = view.findViewById(checkedId);
        if (checkedButton != null) {
            int color = ContextCompat.getColor(requireContext(), R.color.danger);
            checkedButton.getCompoundDrawables()[1].setColorFilter(color, PorterDuff.Mode.SRC_IN);
        }
    }

    // Helper method to reset color filter
    private void resetColorFilter(RadioButton radioButton) {
        if (radioButton != null) {
            radioButton.getCompoundDrawables()[1].setColorFilter(null);
        }
    }

    private void checkLocationPermissionAndLoadLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            // Permission granted, we load current location
            googleMap.setMyLocationEnabled(true);
            loadMyLocation();
        } else {
            // Permission not granted, we request it
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void loadMyLocation() {
        if (isMapReady) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, we load current location
                googleMap.setMyLocationEnabled(true);
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(requireActivity(), location -> {
                            if (location != null) {
                                LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

                                // Clear existing markers
                                clearMarkers();

                                // Add a marker for the current location
                                Marker myLocationMarker = googleMap.addMarker(new MarkerOptions().position(currentLatLng).title("My Location"));
                                markers.add(myLocationMarker);

                                // Move the camera to the current location
                                googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
                                googleMap.animateCamera(CameraUpdateFactory.zoomTo(12.0f));
                            }
                        });
            } else {
                // Permission not granted, we request it
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
    }

    private void onLocationSuccess(Location location) {
        if (location != null) {
            LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

            // Clear existing markers
            clearMarkers();

            // Add a marker for the current location
            Marker myLocationMarker = googleMap.addMarker(new MarkerOptions().position(currentLatLng).title("My Location"));
            markers.add(myLocationMarker);

            // Move the camera to the current location
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(12.0f));
        }
    }

    private void lookupAddress() {
        String address = report_search_location.getText().toString().trim();
        if (!address.isEmpty()) {
            Geocoder geocoder = new Geocoder(requireContext());
            try {
                List<Address> addresses = geocoder.getFromLocationName(address, 1);
                if (!addresses.isEmpty()) {
                    Address foundAddress = addresses.get(0);
                    LatLng location = new LatLng(foundAddress.getLatitude(), foundAddress.getLongitude());

                    // Clear existing markers
                    clearMarkers();

                    // Add a marker at the found location
                    String markerTitle = "Address: " + foundAddress.getAddressLine(0);
                    Marker addressMarker = googleMap.addMarker(new MarkerOptions().position(location).title(markerTitle));
                    markers.add(addressMarker);

                    // Move the camera to the found location
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(12.0f));
                } else {
                    Toast.makeText(requireContext(), "Address not found", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(requireContext(), "Please enter an address", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearMarkers() {
        if (markers != null) {
            for (Marker marker : markers) {
                marker.remove();
            }
            markers.clear();
        }
    }
}
