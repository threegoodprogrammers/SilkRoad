package algorithms.Dijkstra;

import algorithms.NodeGraphObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PathDataObject {
    NodeGraphObject sourceNode;
    NodeGraphObject targetNode;
    HashMap<NodeGraphObject, Double> distances;

    public PathDataObject(ArrayList<NodeGraphObject> nodes, NodeGraphObject sourceNode, NodeGraphObject targetNode) {
        this.sourceNode = sourceNode;
        this.targetNode = targetNode;
        distances = new HashMap<>();
        for (NodeGraphObject node : nodes)
            distances.put(node, Double.POSITIVE_INFINITY);
        sourceNode.setPreviousNodeInPath(null);
        distances.put(sourceNode, 0.0);
    }

    /**
     * @return
     */
    public ArrayList<NodeGraphObject> GetPathNodesToTargetNode() {
        return GetPathNodesToNode(targetNode);
    }

    /**
     * @return
     */
    public ArrayList<NodeGraphObject> GetPathNodesToNode(NodeGraphObject node) {
        ArrayList<NodeGraphObject> nodesInPath = new ArrayList<>();
        do {
            nodesInPath.add(0, node);
            node = node.getPreviousNodeInPath();
        } while (node != null);
        return nodesInPath;
    }


    /**
     * @return
     */
    public double GetDistanceToNode(NodeGraphObject node) {
        return distances.get(node);
    }

    public NodeGraphObject GetClosestNode(ArrayList<NodeGraphObject> nodes) {
        double minDistance = Double.POSITIVE_INFINITY;

        NodeGraphObject closestNode = nodes.get(0);
        for (NodeGraphObject graphNode : nodes) {
            double distance = distances.get(graphNode);
                if (distance < minDistance) {
                    minDistance = distance;
                    closestNode = graphNode;
                }

        }
        return closestNode;
    }
}
