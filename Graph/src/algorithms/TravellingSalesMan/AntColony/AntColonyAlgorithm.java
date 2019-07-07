package algorithms.TravellingSalesMan.AntColony;

import algorithms.GraphObject;
import algorithms.NodeGraphObject;
import algorithms.TravellingSalesMan.TravellingSalesManData;
import algorithms.TravellingSalesMan.Validation;
import elements.Graph;
import elements.GraphNode;

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
            final Graph graph,
            GraphNode baseNode,
            int iterationThreshold,
            int antsNumber
    ) {
        long start = System.currentTimeMillis();
        if (graph == null) return null;
        distanceMatrix = getDistanceMatrixFromObjects(graph.getNodes());
        if (Validation.isComplete(distanceMatrix, distanceMatrix.length)) {
            ArrayList<GraphNode> nodes = graph.getNodes();
            nodesCount = nodes.size();
            //Initialize

            if (distanceMatrix == null) return null;

            if (nodesCount == 0)
                return null;
            if (nodesCount == 1)
                return new TravellingSalesManData(nodes, 0);
            else if (nodesCount == 2) {
                GraphNode source = nodes.get(0);
                GraphNode target = nodes.get(1);
                return new TravellingSalesManData(nodes, (float) (2 * source.getOutgoingNodes().get(target).getWeight()));
            }
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


                    for (int e = 0; e < nodesCount - 1; e++) {
                        for (int i = 0; i < notVisitedCities.size(); i++) {
                            int city = notVisitedCities.get(i);
                            if (city != antNo) {
                                if (probabilityMatrix[currentCity][city] > maxP) {
                                    maxP = probabilityMatrix[currentCity][city];
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
                    visitedCities.add(antNo);

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

                double sigma = 0;

                for (int i = 0; i < nodesCount; i++) {
                    for (int j = 0; j < nodesCount; j++) {
                        sigma += pheromoneMatrix[i][j] * (1d / distanceMatrix[i][j]);
                    }
                }


                // Filling  the probability Matrix
                for (int i = 0; i < nodesCount; i++) {
                    for (int j = 0; j < nodesCount; j++) {
                        probabilityMatrix[i][j] = (pheromoneMatrix[i][j] * (1d / distanceMatrix[i][j])) / sigma;
                    }
                }
            }

            int currentCityIndex = graph.getNodes().indexOf(baseNode);

            ArrayList<GraphNode> path = new ArrayList<>();
            path.add(graph.getNodes().get(currentCityIndex));

            float distance = 0;
            for (int i = 0; i < nodesCount; i++) {

                double max = -1;
                int nextCity = -1;
                for (int j = 0; j < nodesCount; j++) {

                    if (currentCityIndex != j) {
                        if (probabilityMatrix[currentCityIndex][j] > max) {
                            max = probabilityMatrix[currentCityIndex][j];
                            nextCity = j;

                        }

                    }
                }
                distance += distanceMatrix[currentCityIndex][nextCity];
                currentCityIndex = nextCity;
                path.add(graph.getNodes().get(currentCityIndex));
            }
            long end = System.currentTimeMillis();
            float sec = (end - start) / 1000F;
            System.out.println(sec);
            return new TravellingSalesManData(path, distance);
        } else {
            //TODO: Show error that the the salesMan
            //TODO: cannot travel and get back home visiting the other cities just one time
            return null;
        }

    }

    public static TravellingSalesManData findShortestCycle(
            GraphObject graph,
            NodeGraphObject baseNode,
            int iterationThreshold,
            int antsNumber
    ) {
        long start = System.currentTimeMillis();
        if (graph == null) return null;
        distanceMatrix = getDistanceMatrixFromObjects(graph.getNodes());
        if (Validation.isComplete(distanceMatrix, distanceMatrix.length)) {
            ArrayList<NodeGraphObject> nodes = graph.getNodes();
            nodesCount = nodes.size();
            //Initialize

            if (distanceMatrix == null) return null;

            if (nodesCount == 0)
                return null;
            if (nodesCount == 1)
                return new TravellingSalesManData(nodes, 0);
            else if (nodesCount == 2) {
                NodeGraphObject source = nodes.get(0);
                NodeGraphObject target = nodes.get(1);
                return new TravellingSalesManData(nodes, (float) (2 * source.getAttachedNodes().get(target).getWeight()));
            }
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


                    for (int e = 0; e < nodesCount - 1; e++) {
                        for (int i = 0; i < notVisitedCities.size(); i++) {
                            int city = notVisitedCities.get(i);
                            if (city != antNo) {
                                if (probabilityMatrix[currentCity][city] > maxP) {
                                    maxP = probabilityMatrix[currentCity][city];
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
                    visitedCities.add(antNo);

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

                double sigma = 0;

                for (int i = 0; i < nodesCount; i++) {
                    for (int j = 0; j < nodesCount; j++) {
                        sigma += pheromoneMatrix[i][j] * (1d / distanceMatrix[i][j]);
                    }
                }


                // Filling  the probability Matrix
                for (int i = 0; i < nodesCount; i++) {
                    for (int j = 0; j < nodesCount; j++) {
                        probabilityMatrix[i][j] = (pheromoneMatrix[i][j] * (1d / distanceMatrix[i][j])) / sigma;
                    }
                }
            }

            int currentCityIndex = graph.getNodes().indexOf(baseNode);

            ArrayList<NodeGraphObject> path = new ArrayList<>();
            path.add(graph.getNodes().get(currentCityIndex));

            float distance = 0;
            for (int i = 0; i < nodesCount; i++) {

                double max = -1;
                int nextCity = -1;
                for (int j = 0; j < nodesCount; j++) {

                    if (currentCityIndex != j) {
                        if (probabilityMatrix[currentCityIndex][j] > max) {
                            max = probabilityMatrix[currentCityIndex][j];
                            nextCity = j;
                        }
                    }
                }
                distance += distanceMatrix[currentCityIndex][nextCity];
                currentCityIndex = nextCity;
                path.add(graph.getNodes().get(currentCityIndex));
            }
            long end = System.currentTimeMillis();
            float sec = (end - start) / 1000F;
            System.out.println(sec);
            return new TravellingSalesManData(path, distance);
        } else {
            //TODO: Show error that the the salesMan
            //TODO: cannot travel and get back home visiting the other cities just one time
            return null;
        }

    }

    private static void initForAnt(List<Integer> notVisitedCities) {
        for (int i = 0; i < nodesCount; i++) {
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

    private static double[][] getDistanceMatrixFromObjects(List<GraphNode> nodes) {
        if (nodes == null) return null;
        if (nodesCount < 0) return null;
        double[][] distanceMatrix = new double[nodesCount][nodesCount];

        for (int i = 0; i < nodesCount; i++) {
            for (int j = 0; j < nodesCount; j++) {
                if (i == j) {
                    distanceMatrix[i][j] = 0;
                } else {
                    GraphNode source = nodes.get(i);
                    GraphNode target = nodes.get(j);
                    if (source.getOutgoingNodes().get(target) != null) {
                        distanceMatrix[i][j] = source.getOutgoingNodes().get(target).getWeight();
                    }
                }
            }
        }
        return distanceMatrix;
    }

    private static void initializeMatrices() {
        if (nodesCount < 0) return;
        pheromoneMatrix = new double[nodesCount][nodesCount];
        deltaPheromoneMatrix = new double[nodesCount][nodesCount];
        probabilityMatrix = new double[nodesCount][nodesCount];
    }


    // finding the time before the operation is executed
//    long start = System.currentTimeMillis();
//      for (int i = 0; i <5; i++) {
//        Thread.sleep(60);
//    }
//    // finding the time after the operation is executed
//    long end = System.currentTimeMillis();
//    //finding the time difference and converting it into seconds
//    float sec = (end - start) / 1000F; System.out.println(sec + " seconds");

}
