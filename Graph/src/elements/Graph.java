package elements;

import java.util.ArrayList;

/**
 * The type Graph.
 */
public class Graph {
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
     * @param id the id
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
        GraphEdge newEdge = new GraphEdge(weight, source, target);
        edges.add(newEdge);

        attachNodes(source, target, newEdge);

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

        GraphEdge edgeOne = addDirectionalEdge(weight, source, target);
        GraphEdge edgeTwo = addDirectionalEdge(weight, target, source);
        edges.add(edgeOne);
        edges.add(edgeTwo);

        attachNodes(source, target, edgeOne);
        attachNodes(target, source, edgeTwo);

        return new NonDirectionalEdge(edgeOne, edgeTwo);
    }

    /**
     * Add the target node to the attached nodes array list
     * of the source node
     *
     * @param source         Source node
     * @param target         Target node
     * @param connectingEdge The edge connecting the nodes together
     */

    public void attachNodes(GraphNode source, GraphNode target, GraphEdge connectingEdge) {
        source.addNodeToAttachedNodes(target, connectingEdge);
    }

    /**
     * Remove the target node from the attached nodes array list
     * of the source node
     *
     * @param source the source node
     * @param target the target node
     */

    public void detachNodes(GraphNode source, GraphNode target) {
        source.removeNodeFromAttachedNodes(target);
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
        edges.remove(edge);

        detachNodes(edge.getSourceNode(), edge.getTargetNode());
    }

    /**
     * Remove non directional edge.
     *
     * @param edge the edge
     */

    public void removeNonDirectionalEdge(NonDirectionalEdge edge) {

    }

    public GraphNode getNode(String nodeIdentifier) {
        for (GraphNode node : nodes)
            if (node.getIdentifier().equals(nodeIdentifier)) return node;
        return null;
    }
}
