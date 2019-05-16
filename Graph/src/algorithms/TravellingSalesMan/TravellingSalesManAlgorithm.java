package algorithms.TravellingSalesMan;

import algorithms.GraphObject;
import algorithms.NodeGraphObject;

import java.util.ArrayList;

public class TravellingSalesManAlgorithm {

    public static ArrayList<NodeGraphObject> findShortestCycle(GraphObject graph, NodeGraphObject baseNode) {
        if (graph == null) return null;
        if (HasHamiltonianCycle(graph.getNodes())) {
            double[][] distanceMatrix = getDistanceMatrixFromObjects(graph.getNodes());
            if (distanceMatrix == null) return null;

            return new ArrayList<>();
        } else {
            //TODO: Show error that the the salesMan
            //TODO: cannot travel and get back home visiting the other cities just one time
        }
        return null;
    }

    private static boolean HasHamiltonianCycle(ArrayList<NodeGraphObject> nodes) {

        int nodesCount = nodes.size();
        int[][] adjacencyMatrix = new int[nodesCount][nodesCount];

        for (int i = 0; i < nodesCount; i++) {
            for (int j = 0; j < nodesCount; j++) {

                NodeGraphObject source = nodes.get(i);
                NodeGraphObject target = nodes.get(j);
                if (source.getAttachedNodes().get(target) != null) {
                    adjacencyMatrix[i][j] = 1;
                } else {
                    adjacencyMatrix[i][j] = 0;
                }
            }
        }
        return HamiltonianCycle.HamiltonianCycle(adjacencyMatrix);
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
