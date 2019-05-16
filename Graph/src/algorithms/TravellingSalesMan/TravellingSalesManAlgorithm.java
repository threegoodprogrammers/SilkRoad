package algorithms.TravellingSalesMan;

import algorithms.GraphObject;
import algorithms.NodeGraphObject;

import java.util.ArrayList;

public class TravellingSalesManAlgorithm {

    public static void findShortestCycle(GraphObject graph, NodeGraphObject baseNode) {
        if (graph == null) return;
        double[][] distanceMatrix = getDistanceMatrixFromObjects(graph.getNodes());


    }

    private static double[][] getDistanceMatrixFromObjects(ArrayList<NodeGraphObject> nodes) {
        if (nodes == null) return null;
        int nodesCount = nodes.size();
        double[][] distanceMatrix = new double[nodesCount][nodesCount];
        for (int i = 0; i < nodesCount; i++) {
            for (int j = 0; j < nodesCount; j++) {
                distanceMatrix[i][j] = Double.MIN_VALUE;
            }
        }
        for (int i = 0; i < nodesCount; i++) {
            for (int j = 0; j < nodesCount; j++) {
                NodeGraphObject source = nodes.get(i);
                NodeGraphObject target = nodes.get(j);
                if (source.getAttachedNodes().get(target) != null) {
                    distanceMatrix[i][j] = source.getAttachedNodes().get(target).getWeight();
                }
            }
        }
        return distanceMatrix;
    }
}
