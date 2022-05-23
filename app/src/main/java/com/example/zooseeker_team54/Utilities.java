package com.example.zooseeker_team54;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.Pair;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.HashMap;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

/**
 * Utilities class for the findShortestPathBetween function and to create Alerts
 */
public class Utilities {
    private static Graph<String, IdentifiedWeightedEdge> g;
    private static Map<String, ZooData.VertexInfo> vInfo;
    private static Map<String, ZooData.EdgeInfo> eInfo;

    /**
     * Constructor method for Utilities
     * @param context
     */
    public Utilities(Context context) {
        g = ZooData.loadZooGraphJSON("zoo_graph.json", context);
        vInfo = ZooData.loadVertexInfoJSON("zoo_node_info.json", context);
        eInfo = ZooData.loadEdgeInfoJSON("zoo_edge_info.json", context);
    }

    public static void loadOldZooJson(Context context) {
        g = ZooData.loadZooGraphJSON("sample_zoo_graph.json", context);
        vInfo = ZooData.loadVertexInfoJSON("sample_node_info.json", context);
        eInfo = ZooData.loadEdgeInfoJSON("sample_edge_info.json", context);
    }

    public static List<LocItem> findSearchResult(String query, List<LocItem> allLocations) {
        if (query.length() == 0)
            return Collections.emptyList();

        List<LocItem> searchResults = new ArrayList<>();
        for (LocItem locItem : allLocations) {
            if (locItem.name.toLowerCase().contains(query.toLowerCase())
                    && !locItem.planned && locItem.kind.equals("exhibit")) {
                searchResults.add(locItem);
            }
        }
        return searchResults;
    }

    /**
     * Finds shortest path between two edges
     * @param start vertex we're starting from
     * @param goal  vertex we're trying to reach
     * @return      a pair that consists of the path and the total weight of that path
     */
    public static Pair<List<LocEdge>, Double> findShortestPathBetween(String start, String goal) {

        String current = start, temp;
        List<LocEdge> shortestPath = new ArrayList<>();
        GraphPath<String, IdentifiedWeightedEdge> path = DijkstraShortestPath.findPathBetween(g, start, goal);

        double sumWeight = 0;

        // iterate through the list of edges
        for (IdentifiedWeightedEdge e : path.getEdgeList()) {
            double weight = g.getEdgeWeight(e);
            String id = e.getId();
            String street = eInfo.get(id).street;
            String source = vInfo.get(g.getEdgeSource(e)).name;
            String target = vInfo.get(g.getEdgeTarget(e)).name;

            // we need to swap source or target if the edge we are going is opposite
            if (!current.equals(g.getEdgeSource(e))) {
                temp = source;
                source = target;
                target = temp;
                current = g.getEdgeSource(e);
            }
            else {
                current = g.getEdgeTarget(e);
            }

            shortestPath.add(new LocEdge(id, weight, street, source, target));
            sumWeight += weight;
        }

        return new Pair<>(shortestPath, sumWeight);
    }

    /**
     * Method to make showing alerts easier
     * @param activity Activity to display the alert in
     * @param message message to be displayed in the alert
     */
    public static void showAlert(Activity activity, String message) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity);

        alertBuilder
                .setTitle("Alert!")
                .setMessage(message)
                .setPositiveButton("Ok", (dialog, id) -> {
                    dialog.cancel();
                })
                .setCancelable(true);

        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    /**
     * From a given list of LocItems, find the most optimal route through the graph
     * using our findShortestPathBetween function
     * @param mainActivity
     * @param plannedLocItems
     * @return route from the planned exhibits as a HashMap of edges
     */
    public static HashMap<String, List<LocEdge>> findRoute(MainActivity mainActivity, List<LocItem> plannedLocItems) {

        // the final route to return
        HashMap<String, List<LocEdge>> route = new HashMap<>();

        // set up a list unvisited locations
        List<String> unvisited = new ArrayList<>();
        plannedLocItems.forEach((word) -> unvisited.add(word.id));

        // start at the entrance of the zoo
        double currDist = 0;
        String current = "entrance_exit_gate";

        // while there are still unvisited locations
        while (unvisited.size() > 0) {

            // initialize index, distance, and the path to the shortest planned locations
            int minIndex = 0;
            String closest = "", target = "";
            double minDist = Double.MAX_VALUE;
            List<LocEdge> minPath = new ArrayList<>();

            // loop through each other planned locations
            for (int i = 0; i < unvisited.size(); i++) {
                target = unvisited.get(i);
                Pair<List<LocEdge>, Double> pair = findShortestPathBetween(current, target);

                // if the distance is shorter than current min distance, update
                if (pair.second < minDist) {
                    minPath = pair.first;
                    minDist = pair.second;
                    minIndex = i;
                    closest = target;
                }
            }

            //
            LocItem targetLocItem = mainActivity.getViewModel().getLocItemById(closest);
            if (!targetLocItem.visited) {
                currDist += minDist;
                mainActivity.getViewModel().updateLocCurrentDist(targetLocItem, currDist);
            }

            //
            current = closest;
            unvisited.remove(minIndex);
            route.put(closest, minPath);
        }

        // TODO: figure out whether we need to finish at entrance/exit gate

        return route;
    }
}
