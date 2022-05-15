package com.example.zooseeker_team54;

import org.jgrapht.alg.util.Pair;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.nio.Attribute;

/**
 * Exactly like a DefaultWeightedEdge, but has an id field we
 * can use to look up the information about the edge with.
 */
public class IdentifiedWeightedEdge extends DefaultWeightedEdge {
    private String id = null;

    /**
     * Getter method for the ID of the edge
     * @return ID of the current edge
     */
    public String getId() { return id; }

    /**
     * Setter method for the ID of the edge
     * @param id string to be set as the ID of the current edge
     */
    public void setId(String id) { this.id = id; }

    /**
     * Gets the representation of the edge as a string
     * @return String representation of the current edge
     */
    @Override
    public String toString() {
        return "(" + getSource() + " :" + id + ": " + getTarget() + ")";
    }

    /**
     * Attributes the consumer amd sets IDs accordingly
     * @param pair Pair containing the edge and it's name
     * @param attr Attribute containing its value
     */
    public static void attributeConsumer(Pair<IdentifiedWeightedEdge, String> pair, Attribute attr) {
        IdentifiedWeightedEdge edge = pair.getFirst();
        String attrName = pair.getSecond();
        String attrValue = attr.getValue();

        if (attrName.equals("id")) {
            edge.setId(attrValue);
        }
    }
}
