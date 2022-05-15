package com.example.zooseeker_team54;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;

import androidx.lifecycle.Lifecycle;
import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// User Story 4 Unit Tests
@RunWith(AndroidJUnit4.class)
public class RouteDirectionTest {
    List<LocEdge> directions;
    Intent intent;
    LocDatabase testDb;
    LocItemDao dao;

    @Before
    public void setUp() {
        directions = new ArrayList<>();
        intent = new Intent(ApplicationProvider.getApplicationContext(), RouteDirectionActivity.class);
    }

    @Before
    public void resetDatabase() {
        Context context = ApplicationProvider.getApplicationContext();
        testDb = Room.inMemoryDatabaseBuilder(context, LocDatabase.class)
                .allowMainThreadQueries()
                .build();
        LocDatabase.injectTestDatabase(testDb);

        List<LocItem> todos = LocItem.loadJSON(context, "sample_node_info.json");
        dao = testDb.LocItemDao();
        dao.insertAll(todos);
    }

    /**
     * Pass the RouteDirectionActivity class a single exhibit's directions to display.
     */
    @Test
    public void singleExhibitTest() {
        directions.add(new LocEdge("Tasmanian Devils", 200, "Zoo Lane", "entrance", "exit"));

        // Launch RouteDirectionActivity class
        intent.putExtra("directions", (ArrayList<LocEdge>) directions);
        ActivityScenario<RouteDirectionActivity> scenario = ActivityScenario.launch(intent);

        // Check for Correctness
        scenario.onActivity(activity -> {
            DirectionsDisplayRecyclerView display = activity.getDisplayView();
            assertEquals(directions.get(0), display.getDirections().get(0));
        });
    }

    /**
     * Pass the RouteDirectionActivity a plan with more than one exhibit.
     * Should only display the directions for the first exhibit in the plan.
     */
    @Test
    public void multipleExhibitTest() {
        directions.add(new LocEdge("Gorillas", 340, "jungle_lane", "safari_blvd", "exit"));
        directions.add(new LocEdge("Tasmanian Devils", 200, "Zoo Lane", "entrance", "exit"));

        intent.putExtra("directions", (ArrayList<LocEdge>) directions);
        ActivityScenario<RouteDirectionActivity> scenario = ActivityScenario.launch(intent);

        // Check for Correctness
        scenario.onActivity(activity -> {
            DirectionsDisplayRecyclerView display = activity.getDisplayView();
            assertEquals(directions.get(0), display.getDirections().get(0));
        });
    }
}
