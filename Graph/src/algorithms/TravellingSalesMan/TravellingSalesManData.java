package algorithms.TravellingSalesMan;

import algorithms.NodeGraphObject;

import java.util.ArrayList;

public class TravellingSalesManData {
    private ArrayList<NodeGraphObject> path;
    private double shortestDistance;

    public TravellingSalesManData(ArrayList<NodeGraphObject> path, double shortestDistance) {
        this.path = path;
        this.shortestDistance = shortestDistance;
    }
    public ArrayList<NodeGraphObject> getPath() {
        return path;
    }

    public double getShortestDistance() {
        return shortestDistance;
    }
}
