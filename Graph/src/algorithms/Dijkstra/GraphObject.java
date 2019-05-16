package algorithms.Dijkstra;

import java.util.ArrayList;

public class GraphObject {
    private ArrayList<NodeGraphObject> nodes = new ArrayList<>();
    private ArrayList<EdgeGraphObject> edges = new ArrayList<>();

    public ArrayList<NodeGraphObject> getNodes() {
        return nodes;
    }

    public void setNodes(ArrayList<NodeGraphObject> nodes) {
        this.nodes = nodes;
    }

    public ArrayList<EdgeGraphObject> getEdges() {
        return edges;
    }

    public void setEdges(ArrayList<EdgeGraphObject> edges) {
        this.edges = edges;
    }

    public NodeGraphObject addNode(String iD) {
        NodeGraphObject newNode = new NodeGraphObject(iD);
        nodes.add(newNode);
        return newNode;
    }

    public EdgeGraphObject connect(double weight, String sourceNodeID, String targetNodeID) {
        return connect(weight, getNode(sourceNodeID), getNode(targetNodeID));
    }

    public EdgeGraphObject connect(double weight, NodeGraphObject sourceNode, NodeGraphObject targetNode) {
        EdgeGraphObject newEdge = new EdgeGraphObject(weight, sourceNode, targetNode);
        sourceNode.connect(targetNode, newEdge);
        edges.add(newEdge);
        return newEdge;
    }

    public NodeGraphObject getNode(String iD) {
        for (NodeGraphObject node : nodes) {
            if (node.getiD().equals(iD)) return node;
        }
        return null;
    }
}
