package com.example.zooseeker_team54;

import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * Java class representing the functionality of the SettingsActivity that is launched when the user
 * presses the settings button on ShowDirectionActivity
 */
public class SettingsActivity extends AppCompatActivity {

    // initialize buttons
    private Button brief;
    private Button detailed;
    private Button exitBtn;

    /**
     * Create the activity from a savedInstanceState and initialize everything
     *
     * @param savedInstanceState the saved instance from before
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        exitBtn = this.findViewById(R.id.exit_btn);
        exitBtn.setOnClickListener(this::onExitBtnClicked);

        brief = this.findViewById(R.id.briefDirectionsButton);
        setButton(brief, getIsBrief());
        detailed = this.findViewById(R.id.detailedDirectionsButton);
        setButton(detailed, !getIsBrief());

        brief.setOnClickListener(getOnClickedListener(detailed));
        detailed.setOnClickListener(getOnClickedListener(brief));
    }

    /**
     * Setter method for isBrief
     *
     * @param bool boolean for isBrief to be set to
     */
    public void setIsBrief(boolean bool) {
        SharedPreferences preferences = getSharedPreferences("isBrief", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isBrief", bool);
        editor.apply();
    }

    /**
     * Gets the OnClickListener for a given button for use in the OnClickListener setter method
     *
     * @param otherBtn button to retrieve onClickListener for
     * @return OnClickListener for the given button
     */
    private OnClickListener getOnClickedListener(Button otherBtn) {
        return (button) -> {
            if (button == brief && !getIsBrief() || button == detailed && getIsBrief()) {
                setButton(otherBtn, false);
                setButton(button, true);
                setIsBrief(otherBtn.equals(detailed));
                System.out.println(getSharedPreferences("isBrief", MODE_PRIVATE).getBoolean("isBrief", true));
            }
        };
    }

    /**
     * Setter method for a given button
     *
     * @param button Button to be set
     * @param isChecked Boolean representing if it has been checked or not
     */
    private void setButton(View button, boolean isChecked) {
        String color = isChecked ? "#8BC34A" : "#40737373";
        button.setBackgroundColor(Color.parseColor(color));
    }

    /**
     * Method for when the exit button is clicked, exits the activity
     *
     * @param view View to be passed in
     */
    private void onExitBtnClicked(View view) {
        Intent data = new Intent();
        setResult(RESULT_OK, data);
        finish();
    }

    /**
     * Getter method for the isBrief boolean, indicating if the directions should be brief
     *
     * @return boolean if directions should be brief
     */
    public boolean getIsBrief() {
        return getSharedPreferences("isBrief", MODE_PRIVATE).getBoolean("isBrief", true);
    }

    /**
     * Getter method for the detailed button
     *
     * @return detailed button
     */
    @VisibleForTesting
    public Button getDetailed() {
        return detailed;
    }

    /**
     * Getter method for the brief button
     *
     * @return brief button
     */
    @VisibleForTesting
    public Button getBrief() {
        return brief;
    }
}