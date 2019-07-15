package algorithms.TravellingSalesMan.AntColony;

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


    public static TravellingSalesManData findShortestCycle(
            final Graph graph,
            GraphNode baseNode,
            int iterationThreshold,
            double alpha,
            double betha,
            double VAP,
            int antCount
    ) {
        if (graph == null || baseNode == null || antCount < 1) return null;
        ArrayList<GraphNode> nodes = graph.getNodes();
        nodesCount = nodes.size();
        initMatrices(nodesCount);
        distanceMatrix = getDistanceMatrixFromObjects(new ArrayList<>(nodes));
        if (Validation.isComplete(distanceMatrix, distanceMatrix.length)) {
            long start = System.currentTimeMillis();
            //---------------------------------------------------
            if (nodesCount == 0)
                return null;
            if (nodesCount == 1)
                return new TravellingSalesManData(nodes, 0);
            else if (nodesCount == 2) {
                GraphNode source = nodes.get(0);
                GraphNode target = nodes.get(1);
                return new TravellingSalesManData(nodes, (float) (2 * source.getOutgoingNodes().get(target).getWeight()));
            } else if (nodesCount == 3) {
                GraphNode s1 = nodes.get(0);
                GraphNode s2 = nodes.get(1);
                GraphNode s3 = nodes.get(2);
                return new TravellingSalesManData(nodes, (float) (s1.getOutgoingNodes().get(s2).getWeight() + s2.getOutgoingNodes().get(s3).getWeight() + s3.getOutgoingNodes().get(s1).getWeight()));
            }
            //---------------------------------------------------

            for (int iterationNo = 0; iterationNo < iterationThreshold; iterationNo++) {
                int cityCounter = -1;
                for (int antNo = 0; antNo < antCount; antNo++) {
                    int startCity = (cityCounter + 1) % nodesCount;
                    double loopLength = 0;

                    List<Integer> visitedCities = new ArrayList<>();
                    visitedCities.add(startCity);
                    boolean[] citiesState = new boolean[nodesCount];

                    int currentCity = startCity;

                    for (int e = 0; e < nodesCount; e++) {
                        if (e != nodesCount - 1) {

                            double sumProb = 0;
                            for (int i = 0; i < nodesCount; i++) {
                                if (i == startCity || i == currentCity || citiesState[i])
                                    continue;
                                sumProb += probabilityMatrix[currentCity][i];
                            }
                            double randNumber = Math.random() * sumProb;
                            sumProb = 0.0;

                            for (int city = 0; city < nodesCount; city++) {
                                if (city != startCity && !citiesState[city]) {
                                    sumProb += probabilityMatrix[currentCity][city];
                                    if (randNumber <= sumProb) {
                                        loopLength += distanceMatrix[currentCity][city];
                                        visitedCities.add(city);
                                        currentCity = city;
                                        citiesState[city] = true;
                                        break;
                                    }
                                }
                            }
                        } else {
                            loopLength += distanceMatrix[currentCity][startCity];
                            citiesState[startCity] = true;
                            currentCity = startCity;
                            visitedCities.add(startCity);
                        }
                    }
                    for (int node = 0; node < visitedCities.size() - 1; node++) {
                        deltaPheromoneMatrix[visitedCities.get(node)][visitedCities.get(node + 1)] += (1d / loopLength);
                        deltaPheromoneMatrix[visitedCities.get(node + 1)][visitedCities.get(node)] += (1d / loopLength);
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
                        probabilityMatrix[i][j] = (Math.pow(pheromoneMatrix[i][j], alpha) * Math.pow(1d / distanceMatrix[i][j], betha)) / sigma;
                    }
                }
            }

            for (int i = 0; i < nodesCount; i++) {
                for (int j = 0; j < nodesCount; j++) {
                    System.out.println("[" + i + "][" + j + "]=" + probabilityMatrix[i][j]);
                }

            }


            int currentCityIndex = nodes.indexOf(baseNode);

            ArrayList<GraphNode> path = new ArrayList<>();
            path.add(graph.getNodes().get(currentCityIndex));

            float distance = 0;
            for (int i = 0; i < nodesCount - 1; i++) {

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
                path.add(nodes.get(currentCityIndex));
            }
            path.add(baseNode);
            distance += distanceMatrix[currentCityIndex][nodes.indexOf(baseNode)];

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


    private static double[][] getDistanceMatrixFromObjects(ArrayList<GraphNode> nodes) {
        double[][] distanceMatrix = new double[nodesCount][nodesCount];

        for (int i = 0; i < nodesCount; i++) {
            for (int j = 0; j < nodesCount; j++) {
                if (i == j) {
                    distanceMatrix[i][j] = Double.MAX_VALUE;
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

    private static void initMatrices(int nodesCount) {
        pheromoneMatrix = new double[nodesCount][nodesCount];
        deltaPheromoneMatrix = new double[nodesCount][nodesCount];
        probabilityMatrix = new double[nodesCount][nodesCount];
    }


}