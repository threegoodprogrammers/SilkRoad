package algorithms;

import elements.Graph;
import elements.GraphNode;

public class GraphBuilder {
    private Graph graph;

    private GraphBuilder() {
        graph = new Graph();
    }

    public static GraphBuilder Build() {
        return new GraphBuilder();
    }

    public GraphBuilder WithNodes(int nodeCount) {
        for (int index = 0; index < nodeCount; index++)
            graph.addNode(index + "");

        return this;
    }

    public GraphBuilder AllConnected(int... weights) {
        int weightIndex = 0;
        for (GraphNode graphNode1 : graph.getNodes()) {
            for (GraphNode graphNode2 : graph.getNodes()) {
                if (graphNode1 != graphNode2) {
                    graph.addDirectionalEdge(weights[weightIndex], graphNode1, graphNode2);
                    weightIndex++;
                }
            }
        }

        return this;
    }

    public GraphBuilder SomeConnected(String[] sourceIdentifiers, String[] targetIdentifiers, int... weights) {
        int identifierIndex = 0;
        int weightIndex = 0;
        for (GraphNode graphNode1 : graph.getNodes()) {
            for (GraphNode graphNode2 : graph.getNodes()) {
                if (graphNode1 != graphNode2) {
                    if (graphNode1.getIdentifier().equals(sourceIdentifiers[identifierIndex]) && graphNode2.getIdentifier().equals(targetIdentifiers[identifierIndex])) {
                        graph.addDirectionalEdge(weights[weightIndex], graphNode1, graphNode2);
                        weightIndex++;
                        identifierIndex++;
                    }
                }
            }
        }

        return this;
    }

    public Graph getGraph() {
        return graph;
    }
}
