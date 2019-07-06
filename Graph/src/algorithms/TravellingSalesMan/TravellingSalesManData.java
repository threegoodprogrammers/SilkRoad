package algorithms.TravellingSalesMan;

import algorithms.NodeGraphObject;
import elements.GraphNode;

import java.util.ArrayList;

public class TravellingSalesManData {
    private ArrayList<NodeGraphObject> path;
    private ArrayList<GraphNode> pathSecond;
    private double shortestDistance;

    public TravellingSalesManData(ArrayList<NodeGraphObject> path, double shortestDistance) {
        this.path = path;
        this.shortestDistance = shortestDistance;
    }

    public TravellingSalesManData(ArrayList<GraphNode> pathSecond, float shortestDistance) {
        this.pathSecond = pathSecond;
        this.shortestDistance = (double) shortestDistance;
    }

    public ArrayList<NodeGraphObject> getPath() {
        return path;
    }

    public ArrayList<GraphNode> getpathSecond() {
        return pathSecond;
    }

    public double getShortestDistance() {
        return shortestDistance;
    }


}
