package elements;

import java.util.ArrayList;

/**
 * The type Graph.
 */
public class Graph {
    private int lastID = 0;

    /**
     * Edge orientation different states
     * <p>
     * 1. Vertical: Edge starts from top of one of the nodes
     * and ends at bottom of the other node.
     * <p>
     * 2. Horizontal: Edge starts from left side of one of the
     * nodes and ends at right side of the other node.
     */

    public enum EdgeOrientation {
        VERTICAL, HORIZONTAL
    }

    private ArrayList<GraphNode> nodes = new ArrayList<>();
    private ArrayList<GraphEdge> edges = new ArrayList<>();

    /**
     * Gets nodes.
     *
     * @return the nodes
     */

    public ArrayList<GraphNode> getNodes() {
        return nodes;
    }

    /**
     * Sets nodes.
     *
     * @param nodes the nodes
     */

    public void setNodes(ArrayList<GraphNode> nodes) {
        this.nodes = nodes;
    }

    /**
     * Gets edges.
     *
     * @return the edges
     */

    public ArrayList<GraphEdge> getEdges() {
        return edges;
    }

    /**
     * Sets edges.
     *
     * @param edges the edges
     */

    public void setEdges(ArrayList<GraphEdge> edges) {
        this.edges = edges;
    }

    /**
     * Add node.
     *
     * @param id the lastID
     */

    public GraphNode addNode(String id) {
        GraphNode newNode = new GraphNode(id);
        nodes.add(newNode);

        return newNode;
    }

    /**
     * Add directional edge.
     *
     * @param source the source
     * @param target the target
     */

    public GraphEdge addDirectionalEdge(double weight, GraphNode source, GraphNode target) {
        /*
         * Generate the suitable orientation for the edge
         */
        EdgeOrientation orientation = generateOrientation(source, target);

        /*
         * Instantiate a new graph edge object and add it to the graph edges array list
         */
        GraphEdge newEdge = new GraphEdge(weight, source, target, orientation);
        edges.add(newEdge);

        /*
         * Attach the two nodes together
         */
        attachNodes(source, target, newEdge);

        /*
         * Return the final created edge
         */
        return newEdge;
    }

    /**
     * Add non directional edge.
     *
     * @param weight the weight
     * @param source the source
     * @param target the target
     */

    public NonDirectionalEdge addNonDirectionalEdge(double weight, GraphNode source, GraphNode target) {
        /*
         * Create two new edges in opposite
         * direction and add both edges to the nodes
         */
        GraphEdge edgeOne = addDirectionalEdge(weight, source, target);
        GraphEdge edgeTwo = addDirectionalEdge(weight, target, source);

        /*
         * Instantiate a new non-directional edge
         * using the two newly created directional edges
         */
        NonDirectionalEdge newEdge = new NonDirectionalEdge(edgeOne, edgeTwo);

        /*
         * Two-way attach the two nodes together
         */
        twoWayAttachNodes(source, target, newEdge);

        /*
         * Return the final created non-directional edge
         */
        return newEdge;
    }

    /**
     * Add the target node to the attached nodes array list
     * of the source and target node
     *
     * @param source         Source node
     * @param target         Target node
     * @param connectingEdge The edge connecting the nodes together
     */

    private void attachNodes(GraphNode source, GraphNode target, GraphEdge connectingEdge) {
        source.addNodeToOutgoingNodes(target, connectingEdge);
        target.addNodeToIncomingNodes(source, connectingEdge);
    }

    /**
     * Remove the target node from the attached nodes array list
     * of the source node and target node
     *
     * @param source the source node
     * @param target the target node
     */

    private void detachNodes(GraphNode source, GraphNode target) {
        source.removeNodeFromOutgoingNodes(target);
        target.removeNodeFromIncomingNodes(source);
    }

    /**
     * Add each node along with the edge to the
     * array list of two way attached nodes of the other node
     *
     * @param source         Source node
     * @param target         Target node
     * @param connectingEdge The edge connecting the nodes together
     */

    private void twoWayAttachNodes(GraphNode source, GraphNode target, NonDirectionalEdge connectingEdge) {
        source.addNodeToTwoWayAttachedNodes(target, connectingEdge);
        target.addNodeToTwoWayAttachedNodes(source, connectingEdge);
    }

    /**
     * Remove each node from the two way attached nodes array list
     * of the other node
     *
     * @param source the source node
     * @param target the target node
     */

    private void twoWayDetachNodes(GraphNode source, GraphNode target) {
        source.removeNodeFromTwoWayAttachedNodes(target);
        target.removeNodeFromTwoWayAttachedNodes(source);
    }

    /**
     * Remove node.
     *
     * @param node the node
     */

    public void removeNode(GraphNode node) {
        /* TODO: 5/9/2019
        Remove from main graph nodes array list, remove node from the canvas, remove the connected edges to
        node and finally remove the node from the connected nodes "attachedNodes" array list
         */
    }

    /**
     * Remove directional edge.
     *
     * @param edge the edge
     */

    public void removeDirectionalEdge(GraphEdge edge) {
        /* TODO: 5/9/2019 */

        edges.remove(edge);

        detachNodes(edge.getSourceNode(), edge.getTargetNode());
    }

    /**
     * Remove non directional edge.
     *
     * @param edge the edge
     */

    public void removeNonDirectionalEdge(NonDirectionalEdge edge) {
        /* TODO: 5/9/2019 */

    }

    /**
     * Check if two nodes are two way attached to
     * each other (with two edges in opposite directions)
     *
     * @param source the source node
     * @param target the target node
     * @return the boolean
     */

    public boolean areNodesTwoWayAttached(GraphNode source, GraphNode target) {
        return getTwoWayEdge(source, target) != null ||
                (isSourceAttachedToIncomingNode(source, target) && isSourceAttachedToOutgoingNode(source, target));
    }

    /**
     * Is source attached to outgoing node
     *
     * @param source the source
     * @param target the target
     * @return the boolean
     */

    public boolean isSourceAttachedToOutgoingNode(GraphNode source, GraphNode target) {
        return getOutgoingEdge(source, target) != null;
    }

    /**
     * Is source attached to incoming node
     *
     * @param source the source
     * @param target the target
     * @return the boolean
     */

    public boolean isSourceAttachedToIncomingNode(GraphNode source, GraphNode target) {
        return getIncomingEdge(source, target) != null;
    }

    /**
     * Get single edge between two nodes if there is any
     *
     * @param source the source
     * @param target the target
     * @return the boolean
     */

    public GraphEdge getSingleEdgeBetweenNodes(GraphNode source, GraphNode target) {
        /*
         * If there is an incoming edge from target to source
         */

        GraphEdge edge = getIncomingEdge(source, target);

        if (edge != null) {
            return edge;
        }

        edge = getOutgoingEdge(source, target);

        /*
         * If there is an outgoing edge from source to target
         */

        if (edge != null) {
            return edge;
        }

        /*
         * If there is no edge between the two nodes
         */

        return null;
    }

    /**
     * Gets incoming edge from target to source
     *
     * @param source the source
     * @param target the target
     * @return the incoming edge
     */

    public GraphEdge getIncomingEdge(GraphNode source, GraphNode target) {
        return source.getEdgeToTargetIncomingNode(target);
    }

    /**
     * Gets outgoing edge from source to target
     *
     * @param source the source
     * @param target the target
     * @return the outgoing edge
     */

    public GraphEdge getOutgoingEdge(GraphNode source, GraphNode target) {
        return source.getEdgeToTargetOutgoingNode(target);
    }

    /**
     * Gets two way edge from target to source and vice versa
     *
     * @param source the source
     * @param target the target
     * @return the incoming edge
     */

    public NonDirectionalEdge getTwoWayEdge(GraphNode source, GraphNode target) {
        return source.getTwoWayEdgeToTargetNode(target);
    }

    /**
     * Generate suitable edge orientation for new edge
     *
     * @param source The source node
     * @param target The target node
     * @return The suitable edge orientation for the new edge
     */

    private EdgeOrientation generateOrientation(GraphNode source, GraphNode target) {
        /*
         * Get single edge between the two nodes if there is any
         */
        GraphEdge firstEdge = getSingleEdgeBetweenNodes(source, target);

        if (firstEdge == null) {
            return EdgeOrientation.VERTICAL;
        } else {
            switch (firstEdge.getEdgeOrientation()) {
                case VERTICAL:
                    return EdgeOrientation.HORIZONTAL;
                case HORIZONTAL:
                default:
                    return EdgeOrientation.VERTICAL;
            }
        }
    }

    /**
     * Gets node
     *
     * @param nodeIdentifier the node identifier
     * @return the node
     */
    public GraphNode getNode(String nodeIdentifier) {
        for (GraphNode node : nodes)
            if (node.getIdentifier().equals(nodeIdentifier)) return node;
        return null;
    }

    /**
     * Generate lastID string.
     *
     * @return the string
     */

    public String generateID() {
        return String.valueOf(++lastID);
    }
}
