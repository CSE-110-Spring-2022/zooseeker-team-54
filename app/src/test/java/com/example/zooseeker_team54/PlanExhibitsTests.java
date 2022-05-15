package com.example.zooseeker_team54;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.widget.EditText;

import androidx.lifecycle.Lifecycle;
import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

// User Story 2 Unit Tests
@RunWith(AndroidJUnit4.class)
public class PlanExhibitsTests {
    LocDatabase testDb;
    LocItemDao dao;
    ActivityScenario<MainActivity> scenario;

    /**
     * Launches the MainActivity
     */
    @Before
    public void startActivity() {
        scenario = ActivityScenario.launch(MainActivity.class);

        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.moveToState(Lifecycle.State.RESUMED);
    }

    /**
     * Resets the connection to the database
     */
    @Before
    public void resetDatabase() {
        Context context = ApplicationProvider.getApplicationContext();
        testDb = Room.inMemoryDatabaseBuilder(context, LocDatabase.class)
                .allowMainThreadQueries()
                .build();
        LocDatabase.injectTestDatabase(testDb);

        List<LocItem> exhibits = LocItem.loadJSON(context, "sample_node_info.json");
        dao = testDb.LocItemDao();
        dao.insertAll(exhibits);
    }


    /**
     * Checks to see that the list of planned exhibits start off as an empty list
     */
    @Test
    public void emptyPlannedList() {
        scenario.onActivity(activity -> {
            List<LocItem> plannedExhibits = activity.plannedLocsAdapter.getItems();
            assertEquals(0, plannedExhibits.size());
        });
    }

    /**
     * Adds an exhibit to an empty list of planned exhibits.
     */
    @Test
    public void addToEmptyList() {
        scenario.onActivity(activity -> {
            // Check that there are no exhibits planned yet
            List<LocItem> plannedExhibits = activity.plannedLocsAdapter.getItems();
            assertEquals(0, plannedExhibits.size());

            // Search for a specific exhibit.
            String query = "arctic fox";
            EditText searchBar = activity.findViewById(R.id.search_bar);
            searchBar.setText(query);
            List<LocItem> searchResults = activity.searchResultAdapter.getItems();

            // Get the search result
            List<LocItem> selectedExhibits = new ArrayList<>();
            selectedExhibits.add(searchResults.get(0));
            assertEquals(1, selectedExhibits.size());

            // Add the result to the list of planned exhibits and check for correctness.
            activity.plannedLocsAdapter.setItems(selectedExhibits);
            assertEquals(1, activity.plannedLocsAdapter.getItemCount());
            assertEquals("Arctic Foxes", activity.plannedLocsAdapter.getItems().get(0).name);
        });
    }

    /**
     * Test adding more than one exhibit to the plan.
     */
    @Test
    public void addMultipleExhibits() {
        scenario.onActivity(activity -> {
            // Check that there are no exhibits planned yet
            List<LocItem> plannedExhibits = activity.plannedLocsAdapter.getItems();
            assertEquals(0, plannedExhibits.size());

            // Search for a specific exhibit
            String query = "arctic fox";
            EditText searchBar = activity.findViewById(R.id.search_bar);
            searchBar.setText(query);
            List<LocItem> searchResults = activity.searchResultAdapter.getItems();

            // Get the search result
            List<LocItem> selectedExhibits = new ArrayList<>();
            selectedExhibits.add(searchResults.get(0));
            assertEquals(1, selectedExhibits.size());

            // Search for an exhibit with many variations
            query = "bear";
            searchBar.setText(query);
            searchResults = activity.searchResultAdapter.getItems();

            // Set the selected exhibit to "Brown Bears" and add to plan
            selectedExhibits.add(searchResults.get(1));
            activity.plannedLocsAdapter.setItems(selectedExhibits);

            // Check for correctness
            assertEquals(2, activity.plannedLocsAdapter.getItemCount());
            assertEquals("Arctic Foxes", activity.plannedLocsAdapter.getItems().get(0).name);
            assertEquals("Brown Bears", activity.plannedLocsAdapter.getItems().get(1).name);
        });
    }
}
