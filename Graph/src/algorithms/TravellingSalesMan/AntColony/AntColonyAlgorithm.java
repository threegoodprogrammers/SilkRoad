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
            double beta,
            double vapor,
            int antCount
    ) {
        if (graph == null || baseNode == null || antCount < 1) return null;
        ArrayList<GraphNode> nodes = graph.getNodes();
        nodesCount = nodes.size();
        initMatrices(nodesCount);
        int baseCityIndex = nodes.indexOf(baseNode);
        distanceMatrix = getDistanceMatrixFromObjects(new ArrayList<>(nodes));
        if (Validation.isComplete(graph)) {
            //---------------------------------------------------
            if (nodesCount == 0)
                return null;
            if (nodesCount == 1)
                return new TravellingSalesManData(nodes, 0);
            else if (nodesCount == 2) {
                GraphNode source = nodes.get(0);
                GraphNode target = nodes.get(1);
                if (baseNode == source) {
                    return new TravellingSalesManData(nodes, (float) (2 * source.getOutgoingNodes().get(target).getWeight()));
                } else {
                    ArrayList<GraphNode> newNodes = new ArrayList<>();
                    newNodes.add(target);
                    newNodes.add(source);
                    newNodes.add(target);
                    return new TravellingSalesManData(newNodes, (float) (2 * source.getOutgoingNodes().get(target).getWeight()));
                }
            } else if (nodesCount == 3) {
                GraphNode s1 = nodes.get(0);
                GraphNode s2 = nodes.get(1);
                GraphNode s3 = nodes.get(2);
                if (s1.equals(baseNode)) {
                    nodes.add(s1);
                    return new TravellingSalesManData(nodes, (float) (s1.getOutgoingNodes().get(s2).getWeight() + s2.getOutgoingNodes().get(s3).getWeight() + s3.getOutgoingNodes().get(s1).getWeight()));
                } else if (s2.equals(baseNode)) {

                    ArrayList<GraphNode> newNodes = new ArrayList<>();
                    newNodes.add(s2);
                    newNodes.add(s3);
                    newNodes.add(s1);
                    newNodes.add(s2);

                    return new TravellingSalesManData(newNodes, (float) (s1.getOutgoingNodes().get(s2).getWeight() + s2.getOutgoingNodes().get(s3).getWeight() + s3.getOutgoingNodes().get(s1).getWeight()));
                } else {
                    ArrayList<GraphNode> newNodes = new ArrayList<>();
                    newNodes.add(s3);
                    newNodes.add(s1);
                    newNodes.add(s2);
                    newNodes.add(s3);
                    return new TravellingSalesManData(newNodes, (float) (s1.getOutgoingNodes().get(s2).getWeight() + s2.getOutgoingNodes().get(s3).getWeight() + s3.getOutgoingNodes().get(s1).getWeight()));
                }

            }
            //---------------------------------------------------

            double generalDistance = Double.MAX_VALUE;
            List<Integer> generalPath = new ArrayList<>();


            for (int iterationNo = 0; iterationNo < iterationThreshold; iterationNo++) {
//                for (int i = 0; i < nodesCount; i++) {
//                    for (int j = 0; j < nodesCount; j++) {
//                        pheromoneMatrix[i][j] = 0;
//                    }
//                }
                for (int antNo = 0; antNo < antCount; antNo++) {

                    double loopLength = 0;

                    List<Integer> visitedCities = new ArrayList<>();
                    visitedCities.add(baseCityIndex);
                    boolean[] citiesState = new boolean[nodesCount];

                    int currentCity = baseCityIndex;

                    for (int e = 0; e < nodesCount; e++) {
                        if (e != nodesCount - 1) {
                            double sumProb = 0;
                            for (int i = 0; i < nodesCount; i++) {
                                if (i == baseCityIndex || i == currentCity || citiesState[i])
                                    continue;
                                sumProb += probabilityMatrix[currentCity][i];
                            }
                            double randNumber = Math.random() * sumProb;
                            sumProb = 0.0;

                            for (int city = 0; city < nodesCount; city++) {
                                if (currentCity != city && city != baseCityIndex && !citiesState[city]) {
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
                            loopLength += distanceMatrix[currentCity][baseCityIndex];
                            citiesState[baseCityIndex] = true;
                            currentCity = baseCityIndex;
                            visitedCities.add(baseCityIndex);
                        }
                    }

                    if (generalDistance > loopLength) {
                        generalDistance = loopLength;
                        generalPath = new ArrayList<>(visitedCities);
                    }


                    for (int node = 0; node < nodesCount - 1; node++) {
                        deltaPheromoneMatrix[visitedCities.get(node)][visitedCities.get(node + 1)] += (1d / loopLength);
                        deltaPheromoneMatrix[visitedCities.get(node + 1)][visitedCities.get(node)] += (1d / loopLength);
                    }
                }

                for (int i = 0; i < nodesCount; i++) {
                    for (int j = 0; j < nodesCount; j++) {

                        pheromoneMatrix[i][j] = ((1 - vapor) * pheromoneMatrix[i][j]) + deltaPheromoneMatrix[i][j];
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
                        probabilityMatrix[i][j] =
                                (Math.pow(pheromoneMatrix[i][j], alpha) * Math.pow(1d / distanceMatrix[i][j], beta))
                                        / sigma;
                    }
                }
            }

            for (int i = 0; i < generalPath.size(); i++) {
                System.out.println(generalPath.get(i));

            }


            ArrayList<GraphNode> path = new ArrayList<>();
            for (int i : generalPath) {
                path.add(nodes.get(i));

            }

            return new TravellingSalesManData(path, (float) generalDistance);
        } else {
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
        for (int i = 0; i < nodesCount; i++) {
            probabilityMatrix[i][i] = Double.MIN_VALUE;
        }
    }


}