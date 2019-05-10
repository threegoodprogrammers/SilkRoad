package elements;

import javafx.scene.control.Button;

import java.util.HashMap;

public class GraphNode extends Button {
    private GraphNode previousNodeInPath;
    private String identifier;
    private HashMap<GraphNode, GraphEdge> attachedNodes = new HashMap<>();

    /**
     * Instantiates a new Graph node.
     *
     * @param identifier the identifier
     */

    public GraphNode(String identifier) {
        super(identifier);
        this.identifier = identifier;
    }

    /**
     * Gets attached nodes hash map
     *
     * @return the attached nodes
     */

    public HashMap<GraphNode, GraphEdge> getAttachedNodes() {
        return attachedNodes;
    }

    /**
     * Gets edge connecting the current node to a specific node
     *
     * @param targetNode the target node
     * @return the edge to target node
     */

    public GraphEdge getEdgeToTargetNode(GraphNode targetNode) {
        return this.attachedNodes.get(targetNode);
    }

    /**
     * Add node to attached nodes hash map
     *
     * @param targetNode     the target node
     * @param connectingEdge the connecting edge
     */

    public void addNodeToAttachedNodes(GraphNode targetNode, GraphEdge connectingEdge) {
        this.attachedNodes.put(targetNode, connectingEdge);
    }

    /**
     * Remove node from attached nodes.
     *
     * @param targetNode the target node
     */

    public void removeNodeFromAttachedNodes(GraphNode targetNode) {
        this.attachedNodes.remove(targetNode);
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
