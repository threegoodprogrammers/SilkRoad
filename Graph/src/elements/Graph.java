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

    public void addNode(String id) {
    }

    /**
     * Add directional edge.
     *
     * @param source the source
     * @param target the target
     */

    public void addDirectionalEdge(GraphNode source, GraphNode target) {

    }

    /**
     * Add non directional edge.
     *
     * @param source the source
     * @param target the target
     */

    public void addNonDirectionalEdge(GraphNode source, GraphNode target) {

    }

    /**
     * Remove node.
     *
     * @param node the node
     */

    public void removeNode(GraphNode node) {

    }

    /**
     * Remove directional edge.
     *
     * @param edge the edge
     */

    public void removeDirectionalEdge(GraphEdge edge) {

    }

    /**
     * Remove non directional edge.
     *
     * @param edge the edge
     */

    public void removeNonDirectionalEdge(NonDirectionalEdge edge) {

    }
}
