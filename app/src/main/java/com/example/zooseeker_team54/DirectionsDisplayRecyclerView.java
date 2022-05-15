package com.example.zooseeker_team54;

import android.app.Activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;

/**
 * Class for the functionality of the RecyclerView in RouteDirectionActivity
 */
public class DirectionsDisplayRecyclerView {
    // member variables
    List<LocEdge> directions;
    RouteDirectionAdapter routeDirectionAdapter;
    Activity context;
    RecyclerView routeDirectionView;
    HashMap<String, List<LocEdge>> route;

    /**
     * Constructor method for the RecyclerView with given directions and route
     * @param directions Given list of edges for the directions
     * @param route HashMap of the route to be displayed
     */
    public DirectionsDisplayRecyclerView(List<LocEdge> directions,HashMap<String, List<LocEdge>> route ) {
        this.directions = directions;
        this.route = route;
        initializeAdapter();
    }

    /**
     * Setter method to set the context for a given activity
     * @param activity Passed in Activity to be set
     */
    public void setContext(Activity activity) {
        this.context = activity;
    }

    /**
     * Method to initialize a new RouteDirectionAdapter
     */
    public void initializeAdapter() {
        routeDirectionAdapter = new RouteDirectionAdapter();
        routeDirectionAdapter.setHasStableIds(true);
        routeDirectionAdapter.setItems(directions);
    }

    /**
     * Method to initialize a new RecyclerView
     */
    public void initializeRecyclerView() {
        routeDirectionView = context.findViewById(R.id.route_direction);
        routeDirectionView.setLayoutManager(new LinearLayoutManager(context));
        routeDirectionView.setAdapter(routeDirectionAdapter);
    }

    /**
     * Setter method for the member variable, directions
     * @param directions given list of LocEdges to be set
     */
    public void setDirections(List<LocEdge> directions) {
        this.directions = directions;
    }

    /**
     * Getter method for the directions
     * @return Returns the current directions as a list of LocEdges
     */
    public List<LocEdge> getDirections() {
        return this.directions;
    }

    /**
     * Getter method for the RouteDirectionAdapter
     * @return Return the current RouteDirectionAdapter
     */
    public RouteDirectionAdapter getAdapter() {
        return this.routeDirectionAdapter;
    }

    /**
     * Getter method for the route
     * @return Return the current route as a HashMap
     */
    public HashMap<String, List<LocEdge>> getRoute() { return this.route; }
}
