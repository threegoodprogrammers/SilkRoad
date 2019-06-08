package elements;

import javafx.scene.control.Button;

import java.util.HashMap;

public class GraphNode extends Button {
    private GraphNode previousNodeInPath;
    private String identifier;
    private HashMap<GraphNode, GraphEdge> incomingNodes = new HashMap<>();
    private HashMap<GraphNode, GraphEdge> outgoingNodes = new HashMap<>();
    private HashMap<GraphNode, NonDirectionalEdge> twoWayAttachedNodes = new HashMap<>();

    /**
     * Instantiates a new Graph node.
     *
     * @param identifier the identifier
     */

    public GraphNode(String identifier) {
        super(identifier);
        this.identifier = identifier;

        this.getStyleClass().add("node");
    }

    /**
     * Gets attached nodes hash map
     *
     * @return the attached nodes
     */

    public HashMap<GraphNode, GraphEdge> getIncomingNodes() {
        return incomingNodes;
    }

    /**
     * Gets edge connecting the current node to a specific incoming node
     *
     * @param targetNode the target incoming node
     * @return the edge to target node
     */

    public GraphEdge getEdgeToTargetIncomingNode(GraphNode targetNode) {
        return this.incomingNodes.get(targetNode);
    }

    /**
     * Add node to incoming nodes hash map
     *
     * @param targetNode     the target node
     * @param connectingEdge the connecting edge
     */

    public void addNodeToIncomingNodes(GraphNode targetNode, GraphEdge connectingEdge) {
        this.incomingNodes.put(targetNode, connectingEdge);
    }

    /**
     * Remove node from incoming nodes.
     *
     * @param targetNode the target node
     */

    public void removeNodeFromIncomingNodes(GraphNode targetNode) {
        this.incomingNodes.remove(targetNode);
    }

    /**
     * Gets outgoing nodes.
     *
     * @return the outgoing nodes
     */

    public HashMap<GraphNode, GraphEdge> getOutgoingNodes() {
        return outgoingNodes;
    }

    /**
     * Gets edge connecting the current node to a specific outgoing node
     *
     * @param targetNode the target outgoing node
     * @return the edge to target node
     */

    public GraphEdge getEdgeToTargetOutgoingNode(GraphNode targetNode) {
        return this.outgoingNodes.get(targetNode);
    }

    /**
     * Add node to outgoing nodes hash map
     *
     * @param targetNode     the target node
     * @param connectingEdge the connecting edge
     */

    public void addNodeToOutgoingNodes(GraphNode targetNode, GraphEdge connectingEdge) {
        this.outgoingNodes.put(targetNode, connectingEdge);
    }

    /**
     * Remove node from outgoing nodes.
     *
     * @param targetNode the target node
     */

    public void removeNodeFromOutgoingNodes(GraphNode targetNode) {
        this.outgoingNodes.remove(targetNode);
    }

    /**
     * Gets two way attached nodes
     *
     * @return the two way attached nodes
     */

    public HashMap<GraphNode, NonDirectionalEdge> getTwoWayNodes() {
        return twoWayAttachedNodes;
    }

    /**
     * Gets edge connecting the current node to a specific outgoing node
     *
     * @param targetNode the target node
     * @return the edge to target node
     */

    public NonDirectionalEdge getTwoWayEdgeToTargetNode(GraphNode targetNode) {
        return this.twoWayAttachedNodes.get(targetNode);
    }

    /**
     * Add node to two way attached nodes hash map
     *
     * @param targetNode     the target node
     * @param connectingEdge the connecting edge
     */

    public void addNodeToTwoWayNodes(GraphNode targetNode, NonDirectionalEdge connectingEdge) {
        this.twoWayAttachedNodes.put(targetNode, connectingEdge);
    }

    /**
     * Remove node from two way attached nodes.
     *
     * @param targetNode the target node
     */

    public void removeNodeFromTwoWayNodes(GraphNode targetNode) {
        this.twoWayAttachedNodes.remove(targetNode);
    }

    /**
     * Gets previous node in path
     *
     * @return the previous node in path
     */

    public GraphNode getPreviousNodeInPath() {
        return previousNodeInPath;
    }

    /**
     * Sets previous node in path
     *
     * @param previousNodeInPath the previous node in path
     */

    public void setPreviousNodeInPath(GraphNode previousNodeInPath) {
        this.previousNodeInPath = previousNodeInPath;
    }

    /**
     * Gets the node ID
     *
     * @return the identifier
     */

    public String getIdentifier() {
        return identifier;
    }

    /**
     * Sets the node ID
     *
     * @param identifier the identifier
     */

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
