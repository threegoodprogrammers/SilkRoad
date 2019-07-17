package algorithms.TravellingSalesMan;

import algorithms.GraphObject;
import algorithms.NodeGraphObject;
import elements.Graph;
import elements.GraphNode;

import java.util.ArrayList;

public class Validation {
    public static boolean isComplete(double[][] distanceMatrix, int nodesCount) {
        for (int i = 0; i < nodesCount; i++) {
            for (int j = 0; j < nodesCount; j++) {
                if (i != j) {
                    if (distanceMatrix[i][j] == Double.MAX_VALUE) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    public static boolean isComplete(Graph graph) {
        ArrayList<GraphNode> nodes = graph.getNodes();
        for (int i = 0; i < nodes.size(); i++) {
            for (int j = 0; j < nodes.size(); j++) {
                if (i != j) {
                    if (graph.getTwoWayEdge(nodes.get(i),nodes.get(j)) == null) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

}
