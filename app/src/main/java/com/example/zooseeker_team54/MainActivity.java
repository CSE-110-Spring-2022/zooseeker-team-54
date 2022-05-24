package com.example.zooseeker_team54;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class to represent the functionality of the MainActivity that is displayed on launch of our app
 */
public class MainActivity extends AppCompatActivity {

    public RecyclerView searchResultView;
    public RecyclerView plannedLocsView;

    public SearchResultAdapter searchResultAdapter;
    public PlannedLocsAdapter plannedLocsAdapter;

    private TextView planSizeText;
    private AutoCompleteTextView searchBarText;
    private Button clearBtn;
    private Button planBtn;

    private ViewModel viewModel;
    private Utilities utils;

    /**
     * Text Watcher for search bar textview
     */
    private TextWatcher searchBarTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

        @Override
        public void afterTextChanged(Editable editable) {
            showSearchResult(editable.toString());
        }
    };

    // TODO: figure what should happen if a plan is there but users modify the plan in main

    /**
     * Create the activity from a given savedInstanceState and initialize everything
     * @param savedInstanceState the saved instance from before
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utilities.loadOldZooJson(this);

        // prevents UI difficulties resulting from a rotated screen
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // get view model from ViewModelProvider
        viewModel = new ViewModelProvider(this).get(ViewModel.class);

        // Get search bar EditText and bind a text watcher to it
        searchBarText = this.findViewById(R.id.search_bar);
        searchBarText.addTextChangedListener(searchBarTextWatcher);

        // generate a list of exhibits from utilities and create the array adapter for autocomplete suggestions
        List<String> EXHIBITS = viewModel.getAllExhibits()
                .stream()
                .map(l -> l.name)
                .collect(Collectors.toList());
        System.out.println(EXHIBITS);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, EXHIBITS);
        searchBarText.setAdapter(adapter);

        // Create an adapter for the RecyclerView of search results
        searchResultAdapter = new SearchResultAdapter();
        searchResultAdapter.setHasStableIds(true);
        searchResultAdapter.setItemOnClickListener(this::addPlannedLoc);

        // Set the adapter for the actual RecyclerView
        searchResultView = this.findViewById(R.id.search_results);
        searchResultView.setLayoutManager(new LinearLayoutManager(this));
        searchResultView.setAdapter(searchResultAdapter);

        // Create an adapter for the RecyclerView of search results
        plannedLocsAdapter = new PlannedLocsAdapter();
        plannedLocsAdapter.setOnDeleteClicked(this::removePlannedLoc);
        plannedLocsAdapter.setHasStableIds(true);

        // Set the adapter for the actual RecyclerView
        plannedLocsView = this.findViewById(R.id.planned_locs);
        plannedLocsView.setLayoutManager(new LinearLayoutManager(this));
        plannedLocsView.setAdapter(plannedLocsAdapter);

        // get all the planned live LocItems
        viewModel.getAllPlannedLive()
                .observe(this, plannedLocsAdapter::setItems);

        // Show the size of the plan
        planSizeText = this.findViewById(R.id.plan_size);
        updatePlanSizeText();

        // Set up clear button for planned locations
        this.clearBtn = this.findViewById(R.id.clear_btn);
        clearBtn.setOnClickListener(this::onClearBtnClicked);

        // Set up plan button to take us to the route activity
        this.planBtn = this.findViewById(R.id.plan_btn);
        planBtn.setOnClickListener(this::onPlanButtonClicked);
    }

    /**
     * Finds route from a given list of LocItems
     * @param plannedLocItems List of LocItems to find a route for
     * @return HashMap of the route to be displayed
     */
    public HashMap<String, List<LocEdge>> findRoute(List<LocItem> plannedLocItems) {
        Pair<HashMap<String, List<LocEdge>>, HashMap<String, Double>> pair = Utilities.findRoute(plannedLocItems);
        HashMap<String, List<LocEdge>> route = pair.first;
        HashMap<String, Double> distances = pair.second;

        for (Map.Entry<String, Double> entry : distances.entrySet()) {
            String location = entry.getKey();
            Double newDistance = entry.getValue();

            LocItem targetLocItem = viewModel.getLocItemById(location);
            viewModel.updateLocCurrentDist(targetLocItem, newDistance);
        }

        return route;
    }

    /**
     * Removes the given LocItem from the viewModel
     * @param locItem LocItem to be removed
     */
    private void removePlannedLoc(LocItem locItem) {
        viewModel.removePlannedLoc(locItem);
        updatePlanSizeText();
    }

    /**
     * Adds the given LocItem to the viewModel
     * @param locItem LocItem to be added
     */
    private void addPlannedLoc(LocItem locItem) {
        viewModel.addPlannedLoc(locItem);
        updatePlanSizeText();
    }

    /**
     * Updates the planSizeText by getting the planSize
     */
    private void updatePlanSizeText() {
        planSizeText.setText(String.format("Planned (%s)"
                , Integer.toString(viewModel.countPlannedExhibits())));
    }

    /**
     * Using our searchResultAdapter, we display the search results from the given query
     * @param query String query typed in by user
     */
    private void showSearchResult(String query) {
        searchResultAdapter.setItems(Utilities.findSearchResult(query, viewModel.getAll()));
    }

    /**
     * Function for when our clear button is clicked in MainActivity
     * @param view Passed in when "Clear" is clicked
     */
    private void onClearBtnClicked(View view) {
        viewModel.clearPlannedLocs();
        planSizeText.setText("Planned (0)");
    }

    /**
     * Function for when our plan button is clicked in MainActivity
     * @param view Passed in when "Plan" is clicked
     */
    private void onPlanButtonClicked(View view) {
        // should create plan on database to display on routePlanActivity and take us there

        // get number of exhibits in plan from our adapter
        String planSizeString = Integer.toString(plannedLocsAdapter.getItemCount());
        int planSize = Integer.parseInt(planSizeString);

        // show an alert if plan size is 0
        if (planSize == 0) {
            Utilities.showAlert(this, "Plan list is empty, can't create plan!");
            return;
        }

        HashMap<String, List<LocEdge>> directions = findRoute(plannedLocsAdapter.getItems());

        // launch ShowRouteActivity to display directions
        Intent intent = new Intent(this, ShowRouteActivity.class);
        intent.putExtra("route", directions);
        startActivity(intent);
    }
}
