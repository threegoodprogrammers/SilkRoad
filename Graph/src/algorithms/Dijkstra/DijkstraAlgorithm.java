package algorithms.Dijkstra;

import algorithms.GraphObject;
import algorithms.NodeGraphObject;
import elements.Graph;
import elements.GraphNode;

import java.util.ArrayList;

/**
 * A type that calculates the path and distance from the source node to any other node that the source node is connected to(Including the target node)
 * It's methods return an instance PathData.
 */
public class DijkstraAlgorithm {

    /**
     * Find shortest path path data.
     *
     * @param graph                the graph
     * @param sourceNodeIdentifier the source node identifier
     * @param targetNodeIdentifier the target node identifier
     * @return path data
     * @throws ArrayStoreException the array store exception
     */
    public static PathData findShortestPath(Graph graph, String sourceNodeIdentifier, String targetNodeIdentifier) throws ArrayStoreException {
        return findShortestPath(graph, graph.getNode(sourceNodeIdentifier), graph.getNode(targetNodeIdentifier));
    }

    /**
     * Find shortest path path data object.
     *
     * @param graph                the graph
     * @param sourceNodeIdentifier the source node identifier
     * @param targetNodeIdentifier the target node identifier
     * @return path data object
     * @throws ArrayStoreException the array store exception
     */
    public static PathDataObject findShortestPath(GraphObject graph, String sourceNodeIdentifier, String targetNodeIdentifier) throws ArrayStoreException {
        return findShortestPath(graph, graph.getNode(sourceNodeIdentifier), graph.getNode(targetNodeIdentifier));
    }

    /**
     * Find shortest path path data.
     *
     * @param graph      the graph
     * @param sourceNode the source node
     * @param targetNode the target node
     * @return path data
     * @throws ArrayStoreException the array store exception
     */
    public static PathData findShortestPath(Graph graph, GraphNode sourceNode, GraphNode targetNode) throws ArrayStoreException {
        if (graph.getNodes().size() == 0) throw new ArrayStoreException();
        ArrayList<GraphNode> nodes = new ArrayList<>(graph.getNodes());
        ArrayList<GraphNode> navigatedNodes = new ArrayList<>();
        PathData pathData = new PathData(nodes, sourceNode, targetNode);

        do {
            GraphNode leastDistantNode = pathData.getClosestNode(nodes);
            nodes.remove(leastDistantNode);
            navigatedNodes.add(leastDistantNode);
            for (GraphNode adjacentNode : leastDistantNode.getOutgoingNodes().keySet()) {
                double currentDistance = pathData.getDistanceToNode(leastDistantNode) + leastDistantNode.getOutgoingNodes().get(adjacentNode).getWeight();
                if (currentDistance < pathData.getDistanceToNode(adjacentNode)) {
                    pathData.distances.put(adjacentNode, currentDistance);
                    adjacentNode.setPreviousNodeInPath(leastDistantNode);
                }
            }
        } while (!nodes.isEmpty());

        return pathData;
    }

    /**
     * Find shortest path path data object.
     *
     * @param graph      the graph
     * @param sourceNode the source node
     * @param targetNode the target node
     * @return path data object
     * @throws ArrayStoreException the array store exception
     */
    public static PathDataObject findShortestPath(GraphObject graph, NodeGraphObject sourceNode, NodeGraphObject targetNode) throws ArrayStoreException {
        if (graph.getNodes().size() == 0) throw new ArrayStoreException();
        ArrayList<NodeGraphObject> nodes = new ArrayList<>(graph.getNodes());
        ArrayList<NodeGraphObject> navigatedNodes = new ArrayList<>();
        PathDataObject pathData = new PathDataObject(nodes, sourceNode, targetNode);

        do {
            NodeGraphObject leastDistantNode = pathData.GetClosestNode(nodes);
            nodes.remove(leastDistantNode);
            navigatedNodes.add(leastDistantNode);
            for (NodeGraphObject adjacentNode : leastDistantNode.getAttachedNodes().keySet()) {
                double currentDistance = pathData.GetDistanceToNode(leastDistantNode) + leastDistantNode.getAttachedNodes().get(adjacentNode).getWeight();
                if (currentDistance < pathData.GetDistanceToNode(adjacentNode)) {
                    pathData.distances.put(adjacentNode, currentDistance);
                    adjacentNode.setPreviousNodeInPath(leastDistantNode);
                }
            }
        } while (!nodes.isEmpty());

        return pathData;
    }
}
