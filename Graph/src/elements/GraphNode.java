package elements;

import graph.App;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import java.util.HashMap;

/**
 * The type Graph node.
 */
public class GraphNode extends Button {
    private GraphNode previousNodeInPath;
    private String identifier;
    private HashMap<GraphNode, GraphEdge> incomingNodes = new HashMap<>();
    private HashMap<GraphNode, GraphEdge> outgoingNodes = new HashMap<>();
    private HashMap<GraphNode, NonDirectionalEdge> twoWayAttachedNodes = new HashMap<>();

    private boolean isSelected = false;

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
     * Initialize node
     */

    public void initialize() {
        /*
         * Set mouse event listeners
         */
        addHoverListeners();
        addLeaveListeners();

        /*
         * Set styles
         */
        setStyle();
    }

    private void setStyle() {
        style().add("node");
        idle();
    }

    /**
     * Hover node
     */

    public void hoverNode(boolean highlight) {
        sendToFront();
        hover();
        if (highlight) {
            highlightAttachedEdges();
        }
    }

    /**
     * Select node
     */

    public void selectNode() {
        this.isSelected = true;
        select();
    }

    /**
     * Deselect node
     */

    public void deselectNode() {
        this.isSelected = false;
        idle();
    }

    /**
     * Leave node
     */

    public void leaveNode(boolean sendToFront) {
        if (isSelected) {
            /*
             * Set "SELECT" style on edge elements
             */
            select();
        } else {
            /*
             * Set "IDLE" styles on edge elements
             */
            idle();
        }

        if (sendToFront) {
            /*
             * Send all nodes to front
             */
            App.sendNodesToFront();
        }
    }

    /**
     * Set idle style on node
     */
    public void idle() {
        if (style().contains("node-hover")) {
            style().remove("node-hover");
        }

        if (!this.getStyleClass().contains("node-idle")) {
            style().add("node-idle");
        }

    }

    /**
     * Set hover style on node
     */
    public void hover() {
//        if (isSelected) {
        if (this.getStyleClass().contains("node-idle")) {
            style().remove("node-idle");
        }

        if (!this.getStyleClass().contains("node-hover")) {
            this.getStyleClass().add("node-hover");
        }
//        } else {
//            this.getStyleClass().add("node-hover");
//        }
    }

    /**
     * Set select style on node
     */
    public void select() {

    }

    /**
     * Send edge elements to front
     */

    public void sendToFront() {
        this.toFront();
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

    public void addNodeToTwoWayAttachedNodes(GraphNode targetNode, NonDirectionalEdge connectingEdge) {
        this.twoWayAttachedNodes.put(targetNode, connectingEdge);
    }

    /**
     * Remove node from two way attached nodes.
     *
     * @param targetNode the target node
     */

    public void removeNodeFromTwoWayAttachedNodes(GraphNode targetNode) {
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

    /**
     * Add hover listener to edge elements
     */

    private void addHoverListeners() {
        EventHandler<MouseEvent> mouseHover = event -> {
            hoverNode(true);
//            event.consume();
        };

        this.addEventFilter(MouseEvent.MOUSE_ENTERED, mouseHover);
    }

    /**
     * Add leave listener to edge elements
     */

    private void addLeaveListeners() {
        EventHandler<MouseEvent> mouseLeave = event -> {
            leaveNode(true);
//            event.consume();
        };

        this.addEventFilter(MouseEvent.MOUSE_EXITED, mouseLeave);
    }

    /**
     * Highlight attached edges to the node
     */

    private void highlightAttachedEdges() {
        for (GraphEdge edge : this.getOutgoingNodes().values()) {
            edge.sendToFront();
            edge.getTargetNode().sendToFront();
        }

        for (GraphEdge edge : this.getIncomingNodes().values()) {
            edge.sendToFront();
            edge.getSourceNode().sendToFront();
        }

//        for (NonDirectionalEdge edge : this.getTwoWayNodes().values()) {
//            edge.sendToFront();
//            edge.getSourceNode().sendToFront();
//        }
    }

    private ObservableList<String> style() {
        return this.getStyleClass();
    }

//
//    /**
//     * Set attached edges to idle mode
//     */
//
//    private void idleAttachedEdges() {
//        for (GraphEdge edge : this.getOutgoingNodes().values()) {
////            edge.sendToFront();
//            edge.leaveEdge();
//        }
//
//        for (GraphEdge edge : this.getIncomingNodes().values()) {
////            edge.sendToFront();
//            edge.leaveEdge();
//        }
//    }
}
