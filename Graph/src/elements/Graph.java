package elements;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Graph.
 */
public class Graph {
    private List<GraphNode> nodes = new ArrayList<>();
    private List<GraphEdge> edges = new ArrayList<>();

    /**
     * Gets nodes.
     *
     * @return the nodes
     */

    public List<GraphNode> getNodes() {
        return nodes;
    }

    /**
     * Sets nodes.
     *
     * @param nodes the nodes
     */

    public void setNodes(List<GraphNode> nodes) {
        this.nodes = nodes;
    }

    /**
     * Gets edges.
     *
     * @return the edges
     */

    public List<GraphEdge> getEdges() {
        return edges;
    }

    /**
     * Sets edges.
     *
     * @param edges the edges
     */

    public void setEdges(List<GraphEdge> edges) {
        this.edges = edges;
    }

    /**
     * Add node.
     *
     * @param id the id
     */

    public GraphNode addNode(String id) {
        /*
         * Instantiate new graph node and add it
         * to the main nodes array list
         */
        GraphNode newNode = new GraphNode(id, id);
        nodes.add(newNode);

        /*
         * Return the new node
         */
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
         * Instantiate a new graph edge and
         * add it to the main edges array list
         */
        GraphEdge newEdge = new GraphEdge(weight, source, target);
        edges.add(newEdge);

        /*
         * Add the target node to the attached nodes array list
         * of the source node
         */
        attachNodes(source, target, newEdge);

        /*
         * Return the new graph edge
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
         * Instantiate two new graph edges, one from the source node to the target
         * and the other one from target node to the source.
         * Then add both of the edges to the main edges array list
         */
        GraphEdge edgeOne = addDirectionalEdge(weight, source, target);
        GraphEdge edgeTwo = addDirectionalEdge(weight, target, source);
        edges.add(edgeOne);
        edges.add(edgeTwo);

        /*
         * Attach the nodes together
         */
        attachNodes(source, target, edgeOne);
        attachNodes(target, source, edgeTwo);

        /*
         * Instantiate a new non-directional edge
         * with the two directional edges already instantiated and return the edge
         */
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
        /*
         * Remove the edge from the main graph edges array list
         */
        edges.remove(edge);

        /*
         * Remove the target node from the attached nodes array list
         * of the source node
         */
        detachNodes(edge.getSourceNode(), edge.getTargetNode());

        
    }

    /**
     * Remove non directional edge.
     *
     * @param edge the edge
     */

    public void removeNonDirectionalEdge(NonDirectionalEdge edge) {

    }
}
