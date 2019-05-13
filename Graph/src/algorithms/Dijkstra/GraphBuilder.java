package algorithms.Dijkstra;

public class GraphBuilder {
    private GraphObject graph;

    private GraphBuilder() {
        graph = new GraphObject();
    }

    public static GraphBuilder Build() {
        return new GraphBuilder();
    }

    public GraphBuilder WithNodes(int nodeCount) {
        for (int index = 0; index < nodeCount; index++)
            graph.addNode(index + "");

        return this;
    }

    public GraphBuilder AllConnected(double weight) {
        for (NodeGraphObject graphNode1 : graph.nodes) {
            for (NodeGraphObject graphNode2 : graph.nodes) {
                if (graphNode1 != graphNode2) {
                    graph.connect(weight, graphNode1, graphNode2);
                }
            }
        }
        return this;
    }

    public GraphBuilder SomeConnected(String[] sourceIDs, String[] targetIDs, double... weights) {
        if (sourceIDs.length != targetIDs.length || targetIDs.length != weights.length)
            throw new IllegalArgumentException("Lengths Are Not Equal");

        for (int index = 0; index < sourceIDs.length; index++)
            graph.connect(weights[index], sourceIDs[index], targetIDs[index]);
        return this;
    }

    public GraphObject getGraph() {
        return graph;
    }
}
