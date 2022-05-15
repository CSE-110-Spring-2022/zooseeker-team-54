package com.example.zooseeker_team54;

import android.app.Activity;
import android.view.View;
import android.widget.Button;

import java.util.List;

/**
 * Class for the functionality of the "Next" button in RouteDirectionActivity
 */
public class DirectionsDisplayNextButton {
    // member variables
    public Button nextButton;
    private ViewModel viewModel;
    private DirectionsDisplayRecyclerView rview;
    LocItem newTarget;

    Activity context;

    /**
     * Constructor method for the Next button
     * @param context Activity to be displayed in
     * @param rview RecyclerView to be used
     */
    public DirectionsDisplayNextButton (Activity context, DirectionsDisplayRecyclerView rview) {
        this.context = context;
        this.rview = rview;
    }

    /**
     * Setter method for a given ViewModel
     * @param viewModel Passed in ViewModel to be set
     */
    public void setViewModel(ViewModel viewModel) {
        this.viewModel = viewModel;
    }

    /**
     * Initializes the button and puts a listener on the Next button
     */
    public void initializeButton() {
        nextButton = context.findViewById(R.id.next_btn);
        //newTarget = viewModel.getNextTarget(); TODO: Find why  this doesn't work
        newTarget = getNewTarget();
        configureButton(newTarget);
        nextButton.setOnClickListener(this::onNextBtnClicked);
    }

    /**
     * Method for when the next button is called. Gets a new nextButton and a new newTarget
     * @param view
     */
    public void onNextBtnClicked(View view) {
        LocItem currTarget = getNewTarget();
        List<LocEdge> newDirections = rview.getRoute().get(currTarget.id);
        rview.getAdapter().setLocEdges(newDirections);
        nextButton = context.findViewById(R.id.next_btn);
        newTarget = viewModel.getNextTarget();
        configureButton(newTarget);
    }

    /**
     * Method to set up the button that take the user to the next direction in the plan
     * @param newTarget LocItem that is next in the plan
     */
    void configureButton(LocItem newTarget) {
        // if newTarget is null, or if there is no exhibits left
        if (newTarget == null || !newTarget.kind.equals("exhibit") || !newTarget.planned) {
            // Tell user there are no exhibits left
            String buttonText = "NEXT\n------\n" + "No Exhibits Left!";
            nextButton.setText(buttonText);
            nextButton.setClickable(false);
            nextButton.setEnabled(false);
        }
        else {
            nextButton.setEnabled(true);
            LocItem currentTarget = viewModel.getNextUnvisitedExhibit();
            String newText = "NEXT\n------\n" + newTarget.name + ", " + (int) (newTarget.currDist - currentTarget.currDist);
            nextButton.setText(newText);
        }
    }

    /**
     * Method to get the information about the next LocItem in the plan
     * @return next LocItem in the plan
     */
    LocItem getNewTarget()
    {
        // Step 1: recalculate currDist for all unvisited LocItems
        LocItem currentTarget = viewModel.getNextUnvisitedExhibit();
        if(currentTarget == null)
        {
            nextButton.setText("NEXT");
            nextButton.setClickable(false);
            nextButton.setEnabled(false);
            return null;
        }
        Double currentTargetDist = currentTarget.currDist;

        List<LocItem> unvisited = viewModel.getAllPlannedUnvisited();
        for (LocItem locItem : unvisited) {
            viewModel.updateLocCurrentDist(locItem, locItem.currDist - currentTargetDist);
        }
        viewModel.addVisitedLoc(currentTarget);

        //Step 2: choose next exhibit to travel to
        newTarget = viewModel.getNextUnvisitedExhibit();
        return newTarget;
    }
}
