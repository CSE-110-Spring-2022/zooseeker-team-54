package com.example.zooseeker_team54;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import java.util.List;
import java.util.Locale;

// User Story 1 Unit Tests
@RunWith(AndroidJUnit4.class)
public class SearchResultsTests {
    LocDatabase testDb;
    LocItemDao dao;

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

    @Test
    public void testWithNormalQuery() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.moveToState(Lifecycle.State.RESUMED);

        scenario.onActivity(activity -> {
            String query = "lion";
            EditText searchBarText = activity.findViewById(R.id.search_bar);
            searchBarText.setText(query);

            List<LocItem> searchResults = activity.searchResultAdapter.getItems();
            for (LocItem locItem : searchResults) {
                assertTrue(locItem.name.toLowerCase().contains(query));
            }
        });
    }

    @Test
    public void testWithWeirdQuery() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.moveToState(Lifecycle.State.RESUMED);

        scenario.onActivity(activity -> {
            String query = "adsfasdfas";
            EditText searchBarText = activity.findViewById(R.id.search_bar);
            searchBarText.setText(query);

            boolean error = false;
            List<LocItem> searchResults = activity.searchResultAdapter.getItems();
            for (LocItem locItem : searchResults) {
                error = error || locItem.name.toLowerCase().contains(query);
            }
            assertFalse(error);
        });
    }

    @Test
    public void testWithNonExhibits() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.moveToState(Lifecycle.State.RESUMED);

        scenario.onActivity(activity -> {
            String query = "entrance";
            EditText searchBarText = activity.findViewById(R.id.search_bar);
            searchBarText.setText(query);

            boolean error = false;
            List<LocItem> searchResults = activity.searchResultAdapter.getItems();
            for (LocItem locItem : searchResults) {
                error = error || locItem.name.toLowerCase().contains(query);
            }
            assertFalse(error);
        });
    }
}