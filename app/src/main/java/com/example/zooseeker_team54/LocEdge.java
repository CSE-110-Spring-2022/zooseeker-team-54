package com.example.zooseeker_team54;

import java.io.Serializable;

/**
 * Class to represent the Edges between Locations and to be able to store and modify them in
 * containers of our choosing.
 */
public class LocEdge implements Serializable {

    // member variables of LocEdge
    public double weight;
    public String id, source, target, street;

    /**
     * Constructor method for LocEdge given its properties
     * @param id        ID for identification of the edge
     * @param weight    weight of the edge as a double
     * @param street    name of the street as a string
     * @param source    name of the source as a string
     * @param target    name of the target as a string
     */
    public LocEdge(String id, double weight, String street, String source, String target) {
        this.id = id;
        this.weight = weight;
        this.street = street;
        this.source = source;
        this.target = target;
    }

    /**
     * Gets the representation of the edge as a string
     * @return String representation of the LocEdge with its member variables
     */
    @Override
    public String toString() {
        return String.format("Proceed on '%s' %.0f meters towards '%s' from '%s'.\n",
                street, weight, target, source);
    }

}
