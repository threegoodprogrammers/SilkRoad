package algorithms.TravellingSalesMan.AntColony;

import algorithms.GraphObject;
import algorithms.NodeGraphObject;
import algorithms.TravellingSalesMan.HamiltonianCycle;
import algorithms.TravellingSalesMan.TravellingSalesManData;

import java.util.ArrayList;
import java.util.List;

public class AntColonyAlgorithm {
    private static double[][] distanceMatrix;
    private static double[][] pheromoneMatrix;
    private static double[][] deltaPheromoneMatrix;
    private static double[][] probabilityMatrix;

    private static int nodesCount = -1;
    private final static double VAP = 0.3;

    public static TravellingSalesManData findShortestCycle(
            GraphObject graph,
            NodeGraphObject baseNode,
            int iterationThreshold,
            int antsNumber
    ) {
        if (graph == null) return null;
        if (HasHamiltonianCycle(graph.getNodes())) {
            nodesCount = graph.getNodes().size();

            //Initialize
            distanceMatrix = getDistanceMatrixFromObjects(graph.getNodes());
            if (distanceMatrix == null) return null;
            initializeMatrices();

            for (int iterationNo = 0; iterationNo < iterationThreshold; iterationNo++) {//for each iterate
                for (int antNo = 0; antNo < antsNumber; antNo++) {//Placing each ant in different city
                    double loopLength = 0;


                    List<Integer> visitedCities = new ArrayList<>();
                    visitedCities.add(antNo);
                    List<Integer> notVisitedCities = new ArrayList<>();
                    initForAnt(notVisitedCities);
                    int currentCity = antNo;
                    int nextCity = -1;
                    double maxP = -1;


                    for (int e = 0; e < nodesCount; e++) {
                        for (int i = 0; i < notVisitedCities.size(); i++) {
                            int city = notVisitedCities.get(i);
                            if (city != antNo) {
                                if (pheromoneMatrix[currentCity][city] > maxP) {
                                    maxP = pheromoneMatrix[currentCity][city];
                                    nextCity = city;
                                }
                            }
                        }

                        loopLength += distanceMatrix[currentCity][nextCity];

                        notVisitedCities.remove(nextCity);
                        visitedCities.add(nextCity);
                        currentCity = nextCity;
                        nextCity = -1;
                        maxP = -1;

                    }
                    loopLength += distanceMatrix[currentCity][antNo];


                    for (int node = 0; node < visitedCities.size() - 1; node++) {
                        deltaPheromoneMatrix[visitedCities.get(node)][visitedCities.get(node + 1)] += (1d / loopLength);
                    }
                }
                for (int i = 0; i < nodesCount; i++) {
                    for (int j = 0; j < nodesCount; j++) {

                        pheromoneMatrix[i][j] = ((1 - VAP) * pheromoneMatrix[i][j]) + deltaPheromoneMatrix[i][j];
                        deltaPheromoneMatrix[i][j] = 0;
                    }
                }
            }

        } else {
            //TODO: Show error that the the salesMan
            //TODO: cannot travel and get back home visiting the other cities just one time
        }
        return null;//TODO:RETURN CORRECT THING
    }

    private static void initForAnt(List<Integer> notVisitedCities) {
        for (int i = 0; i < nodesCount; i++) {
//            if (i != antNo)
            notVisitedCities.add(i);
        }
    }

    private static double[][] getDistanceMatrixFromObjects(ArrayList<NodeGraphObject> nodes) {
        if (nodes == null) return null;
        if (nodesCount < 0) return null;
        double[][] distanceMatrix = new double[nodesCount][nodesCount];

        for (int i = 0; i < nodesCount; i++) {
            for (int j = 0; j < nodesCount; j++) {
                if (i == j) {
                    distanceMatrix[i][j] = 0;
                } else {
                    NodeGraphObject source = nodes.get(i);
                    NodeGraphObject target = nodes.get(j);
                    if (source.getAttachedNodes().get(target) != null) {
                        distanceMatrix[i][j] = source.getAttachedNodes().get(target).getWeight();
                    }
                }
            }
        }
        return distanceMatrix;
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

    private static void initializeMatrices() {
        if (nodesCount < 0) return;
        pheromoneMatrix = new double[nodesCount][nodesCount];
        probabilityMatrix = new double[nodesCount][nodesCount];
        deltaPheromoneMatrix = new double[nodesCount][nodesCount];
    }

}
