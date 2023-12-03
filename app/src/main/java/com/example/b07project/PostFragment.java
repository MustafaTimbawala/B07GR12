package com.example.b07project;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class PostFragment extends Fragment {

    private PostViewModel homeViewModel;
    private RadioGroup radioGroup;
    private LinearLayout linearLayoutRequirements;
    private TextView resultTextView;
    private Button submitButton;
    private View root;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = new ViewModelProvider(this).get(PostViewModel.class);

        root = inflater.inflate(R.layout.fragment_post, container, false);
        radioGroup = root.findViewById(R.id.radioGroup);
        linearLayoutRequirements = root.findViewById(R.id.linearLayoutRequirements);
        resultTextView = root.findViewById(R.id.resultTextView);
        submitButton = root.findViewById(R.id.submitRequirementsButton);

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            // Clear previous selections
            clearRequirements();

            if (checkedId == R.id.radio_math_major) {
                displayRequirementsForMathMajor();
            } else if (checkedId == R.id.radio_math_specialist) {
                displayRequirementsForMathSpecialist();
            } else if (checkedId == R.id.radio_minor_applied_stats) {
                displayRequirementsForMinorStats();
            } else if (checkedId == R.id.radio_major_stats) {
                displayRequirementsForMajorStats();
            } else if (checkedId == R.id.radio_specialist_statistics) {
                displayRequirementsForSpecialistStats();
            } else if (checkedId == R.id.radio_minor_cs) {
                displayRequirementsForMinorCS();
            } else if (checkedId == R.id.radio_major_specialist_cs) {
                displayRequirementsForMajorCS();
            }
            ScrollView scrollView = (ScrollView) root.findViewById(R.id.my_scroll_view);
            scrollView.post(() -> scrollView.smoothScrollTo(0, linearLayoutRequirements.getTop()));
        });

        submitButton.setOnClickListener(v -> onSubmitRequirements());

        return root;
    }

    private void clearRequirements() {
        linearLayoutRequirements.removeAllViews();
        resultTextView.setVisibility(View.GONE);
        resultTextView.setText("");
    }

    private void onSubmitRequirements() {
        LinearLayout layout = root.findViewById(R.id.linearLayoutRequirements);
        TextView resultTextView = root.findViewById(R.id.resultTextView);
        RadioGroup radioGroup = root.findViewById(R.id.radioGroup);
        int selectedId = radioGroup.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = root.findViewById(selectedId);
        String selectedPost = selectedRadioButton != null ? selectedRadioButton.getText().toString() : "the selected";

        boolean areAllChecked = areAllRequirementsChecked(layout);
        String resultMessage;
        if (areAllChecked) {
            resultMessage = String.format("Congratulations you qualify for %s POSt!", selectedPost);
        } else {
            resultMessage = String.format("Unfortunately you do not qualify for %s POSt!", selectedPost);
        }

        resultTextView.setText(resultMessage);
        resultTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20); // Increase text size
        resultTextView.setTypeface(null, Typeface.BOLD); // Make text bold
        resultTextView.setVisibility(View.VISIBLE); // Show the message
        resultTextView.requestLayout();

        ScrollView scrollView = (ScrollView) root.findViewById(R.id.my_scroll_view);
        scrollView.post(() -> scrollView.smoothScrollTo(0, submitButton.getTop()));

    }


    private boolean areAllRequirementsChecked(@NonNull LinearLayout layout) {
        for (int i = 0; i < layout.getChildCount(); i++) {
            View view = layout.getChildAt(i);
            if (view instanceof CheckBox) {
                if (!((CheckBox) view).isChecked()) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        radioGroup = null;
        linearLayoutRequirements = null;
        resultTextView = null;
        submitButton = null;
        root = null;
    }


    private void displayRequirementsForMinorCS() {
        linearLayoutRequirements.removeAllViews();

        TextView promptTextView = new TextView(getContext());
        promptTextView.setText("Please select all the requirements that apply:");
        promptTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        promptTextView.setTextColor(Color.BLACK);
        promptTextView.setGravity(Gravity.CENTER);
        promptTextView.setTypeface(Typeface.DEFAULT_BOLD);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 20, 0, 20);
        promptTextView.setLayoutParams(layoutParams);

        linearLayoutRequirements.addView(promptTextView);

        // Add checkboxes for requirements
        String[] requirements = {
                "CSCA08H3, CSCA48H3, and [one of: CSCA67H3, MATA67H3, MATA22H3, MATA23H3, MATA30H3, MATA31H3, or [(MATA32H3) or MATA34H3]]",
                "Completion of 4.0 credits"
        };

        for (String requirement : requirements) {
            CheckBox checkBox = new CheckBox(getContext());
            checkBox.setText(requirement);
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                // Logic to determine if both checkboxes are checked
                boolean areAllChecked = areAllRequirementsChecked(linearLayoutRequirements);
                homeViewModel.setRequirementsMet(areAllChecked);
            });
            linearLayoutRequirements.addView(checkBox);
        }
    }

    private void displayRequirementsForMajorCS() {
        linearLayoutRequirements.removeAllViews();

        TextView promptTextView = new TextView(getContext());
        promptTextView.setText("Please select all the requirements that apply:");
        promptTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        promptTextView.setTextColor(Color.BLACK);
        promptTextView.setGravity(Gravity.CENTER);
        promptTextView.setTypeface(Typeface.DEFAULT_BOLD);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 20, 0, 20);
        promptTextView.setLayoutParams(layoutParams);

        linearLayoutRequirements.addView(promptTextView);

        // Add checkboxes for Mathematics Specialist requirements
        String[] requirements = {
                "A grade point average of at least 2.5 across the following five courses: CSC/MAT A67, CSC A48, MAT A22, MAT A31, MAT A37",
                "A grade of at least B in CSC A48",
                "A grade of at least C- in two of CSC/MAT A67, MAT A22, MAT A37"
        };

        for (String requirement : requirements) {
            CheckBox checkBox = new CheckBox(getContext());
            checkBox.setText(requirement);
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                // Logic to determine if both checkboxes are checked
                boolean areAllChecked = areAllRequirementsChecked(linearLayoutRequirements);
                homeViewModel.setRequirementsMet(areAllChecked);
            });
            linearLayoutRequirements.addView(checkBox);
        }
    }

    private void displayRequirementsForMathMajor() {
        // Dynamically add checkboxes for the two requirements
        linearLayoutRequirements.removeAllViews();

        TextView promptTextView = new TextView(getContext());
        promptTextView.setText("Please select all the requirements that apply:");
        promptTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        promptTextView.setTextColor(Color.BLACK);
        promptTextView.setGravity(Gravity.CENTER);
        promptTextView.setTypeface(Typeface.DEFAULT_BOLD);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 20, 0, 20);
        promptTextView.setLayoutParams(layoutParams);

        linearLayoutRequirements.addView(promptTextView);

        String[] requirements = {
                "A grade point average of at least 2.0 across the following four courses: CSC/MAT A67, MAT A22, MAT A31, MAT A37.",
                "A grade of at least B in one of CSC/MAT A67, MAT A22, MAT A37."
        };

        for (String requirement : requirements) {
            CheckBox checkBox = new CheckBox(getContext());
            checkBox.setText(requirement);
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                // Logic to determine if both checkboxes are checked
                boolean areAllChecked = areAllRequirementsChecked(linearLayoutRequirements);
                homeViewModel.setRequirementsMet(areAllChecked);
            });
            linearLayoutRequirements.addView(checkBox);
        }

        TextView resultTextView = new TextView(getContext());
        resultTextView.setId(View.generateViewId()); // Set a unique ID for the TextView
        resultTextView.setVisibility(View.GONE); // Hide the TextView initially
    }

    private void displayRequirementsForMathSpecialist() {
        linearLayoutRequirements.removeAllViews();

        TextView promptTextView = new TextView(getContext());
        promptTextView.setText("Please select all the requirements that apply:");
        promptTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        promptTextView.setTextColor(Color.BLACK);
        promptTextView.setGravity(Gravity.CENTER);
        promptTextView.setTypeface(Typeface.DEFAULT_BOLD);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 20, 0, 20);
        promptTextView.setLayoutParams(layoutParams);

        linearLayoutRequirements.addView(promptTextView);

        // Add checkboxes for Mathematics Specialist requirements
        String[] requirements = {
                "A grade point average of at least 2.5 across the following four courses: CSC/MAT A67, MAT A22, MAT A31, MAT A37.",
                "A grade of at least B in two of CSC/MAT A67, MAT A22, MAT A37."
        };

        for (String requirement : requirements) {
            CheckBox checkBox = new CheckBox(getContext());
            checkBox.setText(requirement);
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                // Logic to determine if both checkboxes are checked
                boolean areAllChecked = areAllRequirementsChecked(linearLayoutRequirements);
                homeViewModel.setRequirementsMet(areAllChecked);
            });
            linearLayoutRequirements.addView(checkBox);
        }
    }

    private void displayRequirementsForMinorStats() {
        linearLayoutRequirements.removeAllViews();

        TextView promptTextView = new TextView(getContext());
        promptTextView.setText("Please select all the requirements that apply:");
        promptTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        promptTextView.setTextColor(Color.BLACK);
        promptTextView.setGravity(Gravity.CENTER);
        promptTextView.setTypeface(Typeface.DEFAULT_BOLD);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 20, 0, 20);
        promptTextView.setLayoutParams(layoutParams);

        linearLayoutRequirements.addView(promptTextView);

        // Add checkboxes for Mathematics Specialist requirements
        String[] requirements = {
                "CSCA08H3 or CSCA20H3",
                "MATA23H3 Linear Algebra I.",
                "MATA30H3 or MATA31H3 and MATA36H3 or MATA37H3."
        };

        for (String requirement : requirements) {
            CheckBox checkBox = new CheckBox(getContext());
            checkBox.setText(requirement);
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                // Logic to determine if both checkboxes are checked
                boolean areAllChecked = areAllRequirementsChecked(linearLayoutRequirements);
                homeViewModel.setRequirementsMet(areAllChecked);
            });
            linearLayoutRequirements.addView(checkBox);
        }
    }

    private void displayRequirementsForMajorStats() {
        linearLayoutRequirements.removeAllViews();

        TextView promptTextView = new TextView(getContext());
        promptTextView.setText("Please select all the requirements that apply:");
        promptTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        promptTextView.setTextColor(Color.BLACK);
        promptTextView.setGravity(Gravity.CENTER);
        promptTextView.setTypeface(Typeface.DEFAULT_BOLD);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 20, 0, 20);
        promptTextView.setLayoutParams(layoutParams);

        linearLayoutRequirements.addView(promptTextView);

        // Add checkboxes for Mathematics Specialist requirements
        String[] requirements = {
                "A grade point average of at least 2.3 across the following four courses: CSCA08/A20, MATA22, MATA30/A31, MATA36/A37"
        };

        for (String requirement : requirements) {
            CheckBox checkBox = new CheckBox(getContext());
            checkBox.setText(requirement);
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                // Logic to determine if both checkboxes are checked
                boolean areAllChecked = areAllRequirementsChecked(linearLayoutRequirements);
                homeViewModel.setRequirementsMet(areAllChecked);
            });
            linearLayoutRequirements.addView(checkBox);
        }
    }

    private void displayRequirementsForSpecialistStats() {
        linearLayoutRequirements.removeAllViews();

        TextView promptTextView = new TextView(getContext());
        promptTextView.setText("Please select all the requirements that apply:");
        promptTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        promptTextView.setTextColor(Color.BLACK);
        promptTextView.setGravity(Gravity.CENTER);
        promptTextView.setTypeface(Typeface.DEFAULT_BOLD);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 20, 0, 20);
        promptTextView.setLayoutParams(layoutParams);

        linearLayoutRequirements.addView(promptTextView);

        // Add checkboxes for Mathematics Specialist requirements
        String[] requirements = {
                "A grade point average of at least 2.5 across the following five courses: CSCA08, CSC/MATA67, MATA22, MATA31, MATA37.",
                "For the Machine Learning and Data Specialist Stream only: A grade of at least B in CSCA48"
        };

        for (String requirement : requirements) {
            CheckBox checkBox = new CheckBox(getContext());
            checkBox.setText(requirement);
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                // Logic to determine if both checkboxes are checked
                boolean areAllChecked = areAllRequirementsChecked(linearLayoutRequirements);
                homeViewModel.setRequirementsMet(areAllChecked);
            });
            linearLayoutRequirements.addView(checkBox);
        }
    }

}