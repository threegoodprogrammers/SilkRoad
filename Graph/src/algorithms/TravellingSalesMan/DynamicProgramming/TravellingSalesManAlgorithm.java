package algorithms.TravellingSalesMan.DynamicProgramming;

import algorithms.GraphObject;
import algorithms.NodeGraphObject;
import algorithms.TravellingSalesMan.TravellingSalesManData;
import algorithms.TravellingSalesMan.Validation;
import elements.Graph;
import elements.GraphNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TravellingSalesManAlgorithm {

    private static boolean ranSolver = false;
    private static List<Integer> tour = new ArrayList<>();
    private static double minTourCost = Double.POSITIVE_INFINITY;

    public static TravellingSalesManData findShortestCycle(Graph graph, GraphNode baseNode) {
        if (graph == null) return null;
        double[][] distanceMatrix = getDistanceMatrixFromObjects(graph.getNodes(), graph.getNodes().size());
        if (Validation.isComplete(graph)) {
            int nodesCount = graph.getNodes().size();

            ArrayList<GraphNode> path = new ArrayList<>();
            for (int i : getTour(nodesCount, distanceMatrix, graph.getNodes().indexOf(baseNode))) {
                path.add(graph.getNodes().get(i));
            }
            return new TravellingSalesManData(path, (float) getTourCost(nodesCount, distanceMatrix, graph.getNodes().indexOf(baseNode)));
        } else {
            //TODO: Show error that the the salesMan
            //TODO: cannot travel and get back home visiting the other cities just one time
        }
        return null;
    }


    private static double[][] getDistanceMatrixFromObjects(ArrayList<NodeGraphObject> nodes, int nodesCount) {
        if (nodes == null) return null;
        double[][] distanceMatrix = new double[nodesCount][nodesCount];
        for (int i = 0; i < nodesCount; i++) {
            for (int j = 0; j < nodesCount; j++) {
                distanceMatrix[i][j] = Double.MAX_VALUE;
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

    private static double[][] getDistanceMatrixFromObjects(List<GraphNode> nodes, int nodesCount) {
        if (nodes == null) return null;
        double[][] distanceMatrix = new double[nodesCount][nodesCount];
        for (int i = 0; i < nodesCount; i++) {
            for (int j = 0; j < nodesCount; j++) {
                distanceMatrix[i][j] = Double.MAX_VALUE;
            }
        }
        for (int i = 0; i < nodesCount; i++) {
            for (int j = 0; j < nodesCount; j++) {
                GraphNode source = nodes.get(i);
                GraphNode target = nodes.get(j);
                if (source.getOutgoingNodes().get(target) != null) {
                    distanceMatrix[i][j] = source.getOutgoingNodes().get(target).getWeight();
                }
            }
        }
        return distanceMatrix;
    }

    private static void combinations(int set, int at, int r, int n, List<Integer> subsets) {

        // Return early if there are more elements left to select than what is available.
        int elementsLeftToPick = n - at;
        if (elementsLeftToPick < r) return;

        // We selected 'r' elements so we found a valid subset!
        if (r == 0) {
            subsets.add(set);
        } else {
            for (int i = at; i < n; i++) {
                // Try including this element
                set |= 1 << i;

                combinations(set, i + 1, r - 1, n, subsets);

                // Backtrack and try the instance where we did not include this element
                set &= ~(1 << i);
            }
        }
    }

    private static List<Integer> combinations(int r, int n) {
        List<Integer> subsets = new ArrayList<>();
        combinations(0, 0, r, n, subsets);
        return subsets;
    }

    private static boolean notIn(int elem, int subset) {
        return ((1 << elem) & subset) == 0;
    }

    private static void solve(int N, double distance[][], int start) {

        if (ranSolver) return;

        final int END_STATE = (1 << N) - 1;
        Double[][] memo = new Double[N][1 << N];

        // Add all outgoing edges from the starting node to memo table.
        for (int end = 0; end < N; end++) {
            if (end == start) continue;
            memo[end][(1 << start) | (1 << end)] = distance[start][end];
        }

        for (int r = 3; r <= N; r++) {
            for (int subset : combinations(r, N)) {
                if (notIn(start, subset)) continue;
                for (int next = 0; next < N; next++) {
                    if (next == start || notIn(next, subset)) continue;
                    int subsetWithoutNext = subset ^ (1 << next);
                    double minDist = Double.POSITIVE_INFINITY;
                    for (int end = 0; end < N; end++) {
                        if (end == start || end == next || notIn(end, subset)) continue;
                        double newDistance = memo[end][subsetWithoutNext] + distance[end][next];
                        if (newDistance < minDist) {
                            minDist = newDistance;
                        }
                    }
                    memo[next][subset] = minDist;
                }
            }
        }

        // Connect tour back to starting node and minimize cost.
        for (int i = 0; i < N; i++) {
            if (i == start) continue;
            double tourCost = memo[i][END_STATE] + distance[i][start];
            if (tourCost < minTourCost) {
                minTourCost = tourCost;
            }
        }

        int lastIndex = start;
        int state = END_STATE;
        tour.add(start);

        // Reconstruct TSP path from memo table.
        for (int i = 1; i < N; i++) {

            int index = -1;
            for (int j = 0; j < N; j++) {
                if (j == start || notIn(j, state)) continue;
                if (index == -1) index = j;
                double prevDist = memo[index][state] + distance[index][lastIndex];
                double newDist = memo[j][state] + distance[j][lastIndex];
                if (newDist < prevDist) {
                    index = j;
                }
            }

            tour.add(index);
            state = state ^ (1 << index);
            lastIndex = index;
        }

        tour.add(start);
        Collections.reverse(tour);

        ranSolver = true;
    }

    public static double getTourCost(int N, double[][] distance, int start) {
        if (!ranSolver) solve(N, distance, start);
        return minTourCost;
    }

    public static List<Integer> getTour(int N, double[][] distance, int start) {
        if (!ranSolver) solve(N, distance, start);
        return tour;
    }
}
