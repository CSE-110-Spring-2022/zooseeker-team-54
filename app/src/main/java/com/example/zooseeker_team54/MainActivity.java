package com.example.zooseeker_team54;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public RecyclerView searchResultView;
    public RecyclerView plannedLocsView;

    private EditText searchBarText;
    private Button clearBtn;

    private LocViewModel locViewModel;
    private SearchResultAdapter searchResultAdapter;
    private PlannedLocsAdapter plannedLocsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locViewModel = new ViewModelProvider(this).get(LocViewModel.class);

        // Get search bar EditText and bind a text watcher to it
        searchBarText = this.findViewById(R.id.search_bar);
        searchBarText.addTextChangedListener(searchBarTextWatcher);

        // Create an adapter for the RecyclerView of search results
        searchResultAdapter = new SearchResultAdapter();
        searchResultAdapter.setOnSearchResultClicked(locViewModel::addPlannedLoc);
        searchResultAdapter.setHasStableIds(true);

        // Set the adapter for the actual RecyclerView
        searchResultView = this.findViewById(R.id.search_results);
        searchResultView.setLayoutManager(new LinearLayoutManager(this));
        searchResultView.setAdapter(searchResultAdapter);

        // Create an adapter for the RecyclerView of search results
        plannedLocsAdapter = new PlannedLocsAdapter();
        plannedLocsAdapter.setHasStableIds(true);

        // Set the adapter for the actual RecyclerView
        plannedLocsView = this.findViewById(R.id.planned_locs);
        plannedLocsView.setLayoutManager(new LinearLayoutManager(this));
        plannedLocsView.setAdapter(plannedLocsAdapter);

        //
        locViewModel.getPlannedLocs()
                .observe(this, plannedLocsAdapter::setLocItems);

        // Set up clear button for planned locs
        this.clearBtn = this.findViewById(R.id.clear_btn);
        clearBtn.setOnClickListener(this::onClearBtnClicked);

    }

    // Text Watcher for search bar textview
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

    void showSearchResult(String query) {

        // Display nothing when query is empty
        if (query.length() == 0) {
            searchResultAdapter.setLocItems(Collections.emptyList());
            return;
        }

        List<LocItem> allLocs = locViewModel.getAll();
        List<LocItem> searchResults = new ArrayList<>();

        for(LocItem locItem : allLocs) {
            if (locItem.name.toLowerCase().contains(query.toLowerCase()) && !locItem.planned) {
                searchResults.add(locItem);
            }
        }
        searchResultAdapter.setLocItems(searchResults);
    }

    private void onClearBtnClicked(View view) {
        locViewModel.clearPlannedLocs();
    }
}
