package com.example.sosemergency.ui.home;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ReportFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.sosemergency.MainActivity;
import com.example.sosemergency.R;
import com.example.sosemergency.databinding.FragmentHomeBinding;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.title_home);
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //the go to report page button
        View goToReportPageButton  = binding.getRoot().findViewById(R.id.goToReportPageButton);
        goToReportPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the NavController
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_report);

                //// Navigate to the Report page
                navController.popBackStack(navController.getCurrentDestination().getId(),true);
                navController.navigate(R.id.navigation_report);

            }
        });

        //the emergency button
        CircleImageView getEmergencyButton = binding.getRoot().findViewById(R.id.getEmergencyButton);

        getEmergencyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayToastMessage("Please hold the button to call emergency");
            }
        });

        // Create a grayscale color matrix
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);

        // Create a ColorMatrixColorFilter with the grayscale matrix
        final ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);

        final int pressedColor = getResources().getColor(R.color.danger);

        final ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), Color.TRANSPARENT, modifyAlpha(pressedColor, 0.3f));
        colorAnimator.setDuration(500); // Set the duration of the animation


        getEmergencyButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                // Start the color change animation on long click
                colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        getEmergencyButton.setColorFilter(new ColorMatrixColorFilter(colorMatrix)); // Clear existing color filter
                        getEmergencyButton.setColorFilter((Integer) animator.getAnimatedValue(), android.graphics.PorterDuff.Mode.SRC_ATOP);
                    }
                });
                colorAnimator.start();
                //display a message
                displayToastMessage(String.valueOf(getString(R.string.home_emergency_coming_message)));
                return true;
            }
        });
        // Reset the color filter after the long click
        getEmergencyButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        // Reset to the original color with a fade out animation
                        colorAnimator.reverse();
                        break;
                }
                return false;
            }
        });


        return root;
    }
    public void displayToastMessage(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
    private int modifyAlpha(int color, float alpha) {
        int alphaInt = Math.round(Color.alpha(color) * alpha);
        return Color.argb(alphaInt, Color.red(color), Color.green(color), Color.blue(color));
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}