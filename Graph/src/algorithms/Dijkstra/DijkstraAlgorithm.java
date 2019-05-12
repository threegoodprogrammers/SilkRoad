package algorithms.Dijkstra;

import elements.Graph;
import elements.GraphNode;

import java.util.ArrayList;

/**
 * A type that calculates the path and distance from the source node to any other node that the source node is connected to(Including the target node)
 * It's methods return an instance PathData.
 */
public class DijkstraAlgorithm {

    public static PathData FindShortestPath(Graph graph, String sourceNodeIdentifier, String targetNodeIdentifier) throws ArrayStoreException {
        return FindShortestPath(graph, graph.getNode(sourceNodeIdentifier), graph.getNode(targetNodeIdentifier));
    }

    public static PathData FindShortestPath(Graph graph, GraphNode sourceNode, GraphNode targetNode) throws ArrayStoreException {
        if (graph.getNodes().size() == 0) throw new ArrayStoreException();
        ArrayList<GraphNode> nodes = graph.getNodes();
        ArrayList<GraphNode> navigatedNodes = new ArrayList<>();
        PathData pathData = new PathData(nodes, sourceNode, targetNode);

        nodes.remove(sourceNode);
        navigatedNodes.add(sourceNode);

        GraphNode leastDistantNode = pathData.GetClosestNode(nodes);

        nodes.remove(leastDistantNode);
        navigatedNodes.add(leastDistantNode);

        for (GraphNode node : leastDistantNode.getAttachedNodes().keySet()) {
            double currentDistance = pathData.GetDistanceToNode(leastDistantNode) +
                    leastDistantNode.getAttachedNodes().get(node).getWeight();
            if (currentDistance < pathData.GetDistanceToNode(node)){
                pathData.distances.put(node, currentDistance);
                node.setPreviousNodeInPath(leastDistantNode);
                break;
            }
        }

        return pathData;
    }
}
