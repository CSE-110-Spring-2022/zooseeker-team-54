package com.example.zooseeker_team54;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

// User Story 5, 6 Unit Tests
@RunWith(AndroidJUnit4.class)
public class NextDirectionTests {
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

    @Test
    public void noMoreExhibitsTest() {
        directions.add(new LocEdge("Tasmanian Devils", 200, "Zoo Lane", "entrance", "exit"));

        // Launch RouteDirectionActivity class
        intent.putExtra("directions", (ArrayList<LocEdge>) directions);
        ActivityScenario<RouteDirectionActivity> scenario = ActivityScenario.launch(intent);

        // Check for Correctness
        scenario.onActivity(activity -> {
            activity.getButton().configureButton(null);
            Button btn = activity.findViewById(R.id.next_btn);
            String buttonText = "NEXT\n------\n" + "No Exhibits Left!";
            assertEquals(buttonText, btn.getText());
            assertEquals(false, btn.isEnabled());
        });
    }

    @Test
    public void MoreExhibitsTest() {
        directions.add(new LocEdge("Tasmanian Devils", 200, "Zoo Lane", "entrance", "exit"));

        // Launch RouteDirectionActivity class
        intent.putExtra("directions", (ArrayList<LocEdge>) directions);
        ActivityScenario<RouteDirectionActivity> scenario = ActivityScenario.launch(intent);

        // Check for Correctness
        scenario.onActivity(activity -> {
            LocItem newTarget = new LocItem("Baboons", "baboons", "exhibit", null);
            newTarget.planned = true;
            newTarget.currDist = 200;
            activity.getButton().configureButton(newTarget);
            Button btn = activity.findViewById(R.id.next_btn);
            assertEquals("NEXT\n------\nBaboons, 200", btn.getText());
            assertEquals(true, btn.isEnabled());
        });
    }
}
