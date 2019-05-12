package algorithms.Dijkstra;

import elements.GraphNode;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A Type that contains and returns the path and distance from the source node to any other node in the graph that the source node is connected to(Including Target Node).
 */
public class PathData {
    GraphNode sourceNode;
    GraphNode targetNode;
    HashMap<GraphNode, Double> distances;

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
     * @return
     */
    public ArrayList<GraphNode> GetPathNodesToTargetNode() {
        return null;
    }

    /**
     * @return
     */
    public ArrayList<GraphNode> GetPathNodesToNode(GraphNode node) {
        return null;
    }


    /**
     * @return
     */
    public double GetDistanceToNode(GraphNode node) {
        return 0;
    }

    public GraphNode GetClosestNode(ArrayList<GraphNode> nodes) {
        double minDistance = Double.POSITIVE_INFINITY;

        GraphNode closestNode = sourceNode;
        for (GraphNode graphNode : nodes) {
            if (graphNode != sourceNode) {
                double distance = distances.get(graphNode);
                if (distance < minDistance) {
                    minDistance = distance;
                    closestNode = graphNode;
                }
            }
        }
        return closestNode;
    }
}
