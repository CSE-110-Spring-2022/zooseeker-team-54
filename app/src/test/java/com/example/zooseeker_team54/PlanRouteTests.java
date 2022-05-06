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

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class PlanRouteTests {
    LocDatabase testDb;
    LocItemDao dao;

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
    public void emptyPlansTest() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.moveToState(Lifecycle.State.RESUMED);

        scenario.onActivity(activity -> {
            List<LocItem> selectedExhibits = activity.plannedLocsAdapter.getLocItems();
            HashMap<String, List<LocEdge>> route = activity.findRoute(selectedExhibits);
            assertEquals(0, selectedExhibits.size());
            assertEquals(0, route.size());
        });
    }

    @Test
    public void singlePlanTest() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.moveToState(Lifecycle.State.RESUMED);

        scenario.onActivity(activity -> {
            String query = "gorillas";
            EditText searchBarText = activity.findViewById(R.id.search_bar);
            searchBarText.setText(query);
            List<LocItem> searchResults = activity.searchResultAdapter.getLocItems();

            List<LocItem> selectedExhibits = new ArrayList<>();
            selectedExhibits.add(searchResults.get(0));
            assertEquals(1, selectedExhibits.size());

            activity.plannedLocsAdapter.setLocItems(selectedExhibits);
            assertEquals(1, activity.plannedLocsAdapter.getItemCount());
            List<LocItem> locsAdapterContents = activity.plannedLocsAdapter.getLocItems();
            assertEquals(1, locsAdapterContents.size());
            assertEquals("Loc {id=gorillas, name='Gorillas', planned=false, visited=false, current distance=0.0}", locsAdapterContents.get(0).toString());

            HashMap<String, List<LocEdge>> route = activity.findRoute(locsAdapterContents);
            assertEquals(1, route.size());
            assertEquals("{gorillas=[Walk 10 meters along Entrance Way from 'Entrance and Exit Gate' to 'Entrance Plaza'.\n" +
                    ", Walk 200 meters along Africa Rocks Street from 'Entrance Plaza' to 'Gorillas'.\n" +
                    "]}", route.toString());
        });
    }

    @Test
    public void multiPlanTest() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.moveToState(Lifecycle.State.RESUMED);

        scenario.onActivity(activity -> {
            String query = "alligators";
            EditText searchBarText = activity.findViewById(R.id.search_bar);
            searchBarText.setText(query);
            List<LocItem> searchResults = activity.searchResultAdapter.getLocItems();

            List<LocItem> selectedExhibits = new ArrayList<>();
            selectedExhibits.add(searchResults.get(0));
            assertEquals(1, selectedExhibits.size());

            query = "lions";
            searchBarText.setText(query);

            selectedExhibits.add(activity.searchResultAdapter.getLocItems().get(0));
            assertEquals(2, selectedExhibits.size());

            activity.plannedLocsAdapter.setLocItems(selectedExhibits);
            assertEquals(2, activity.plannedLocsAdapter.getItemCount());
            List<LocItem> locsAdapterContents = activity.plannedLocsAdapter.getLocItems();
            assertEquals(2, locsAdapterContents.size());

            assertEquals("Loc {id=gators, name='Alligators', planned=false, visited=false, current distance=0.0}", locsAdapterContents.get(0).toString());
            assertEquals("Loc {id=lions, name='Lions', planned=false, visited=false, current distance=0.0}", locsAdapterContents.get(1).toString());

            HashMap<String, List<LocEdge>> route = activity.findRoute(locsAdapterContents);
            assertEquals(2, route.size());
            assertEquals("{lions=[Walk 200 meters along Sharp Teeth Shortcut from 'Alligators' to 'Lions'.\n" +
                    "], gators=[Walk 10 meters along Entrance Way from 'Entrance and Exit Gate' to 'Entrance Plaza'.\n" +
                    ", Walk 100 meters along Reptile Road from 'Entrance Plaza' to 'Alligators'.\n" +
                    "]}", route.toString());
        });
    }
}

