package algorithms.Dijkstra;

import elements.GraphNode;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A Type that contains and returns the path and distance from the source node to any other node in the graph that the source node is connected to(Including Target Node).
 */
public class PathData {
    private GraphNode sourceNode;
    private GraphNode targetNode;
    HashMap<GraphNode, Double> distances;

    /**
     * Instantiates a new Path data.
     *
     * @param nodes      the nodes
     * @param sourceNode the source node
     * @param targetNode the target node
     */

    public PathData(ArrayList<GraphNode> nodes, GraphNode sourceNode, GraphNode targetNode) {
        this.sourceNode = sourceNode;
        this.targetNode = targetNode;
        distances = new HashMap<>();
        for (GraphNode node : nodes)
            distances.put(node, Double.POSITIVE_INFINITY);
        sourceNode.setPreviousNodeInPath(null);
        distances.put(sourceNode, 0.0);
    }

    /**
     * Gets path nodes to target node.
     *
     * @return path nodes to target node
     */

    public ArrayList<GraphNode> getPathNodesToTargetNode() {
        return getPathNodesToNode(targetNode);
    }

    /**
     * Gets path nodes to node.
     *
     * @param node the node
     * @return path nodes to node
     */

    public ArrayList<GraphNode> getPathNodesToNode(GraphNode node) {
        ArrayList<GraphNode> nodesInPath = new ArrayList<>();
        do {
            nodesInPath.add(0, node);
            node = node.getPreviousNodeInPath();
        } while (node != null);
        return nodesInPath;
    }


    /**
     * Gets distance to node.
     *
     * @param node the node
     * @return distance to node
     */

    public double getDistanceToNode(GraphNode node) {
        return distances.get(node);
    }

    /**
     * Gets shortest distance
     *
     * @return the shortest distance
     */

    public double getShortestDistance() {
        return getDistanceToNode(targetNode);
    }

    /**
     * Gets closest node.
     *
     * @param nodes the nodes
     * @return the closest node
     */

    public GraphNode getClosestNode(ArrayList<GraphNode> nodes) {
        double minDistance = Double.POSITIVE_INFINITY;

        GraphNode closestNode = nodes.get(0);
        for (GraphNode graphNode : nodes) {
            double distance = distances.get(graphNode);
            if (distance < minDistance) {
                minDistance = distance;
                closestNode = graphNode;
            }

        }
        return closestNode;
    }
}
