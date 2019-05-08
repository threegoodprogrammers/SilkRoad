package elements;

import javafx.scene.shape.Line;

public class GraphEdge extends Line {
    private double weight;
    private GraphNode sourceNode;
    private GraphNode targetNode;

    /**
     * Instantiates a new Graph edge.
     *
     * @param weight     the weight
     * @param sourceNode the source node
     * @param targetNode the target node
     */

    public GraphEdge(double weight,
                     GraphNode sourceNode, GraphNode targetNode) {
        this.weight = weight;
        this.sourceNode = sourceNode;
        this.targetNode = targetNode;
    }

    /**
     * Gets weight.
     *
     * @return the weight
     */

    public double getWeight() {
        return weight;
    }

    /**
     * Sets weight.
     *
     * @param weight the weight
     */

    public void setWeight(double weight) {
        this.weight = weight;
    }

    /**
     * Gets source node.
     *
     * @return the source node
     */

    public GraphNode getSourceNode() {
        return sourceNode;
    }

    /**
     * Sets source node.
     *
     * @param sourceNode the source node
     */
    public void setSourceNode(GraphNode sourceNode) {
        this.sourceNode = sourceNode;
    }

    /**
     * Gets target node.
     *
     * @return the target node
     */

    public GraphNode getTargetNode() {
        return targetNode;
    }

    /**
     * Sets target node.
     *
     * @param targetNode the target node
     */

    public void setTargetNode(GraphNode targetNode) {
        this.targetNode = targetNode;
    }

    public void invertDirection() {

    }

}
