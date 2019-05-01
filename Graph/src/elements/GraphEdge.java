package elements;

import javafx.scene.shape.Line;

public class GraphEdge extends Line {
    private String identifier;
    private double weight;
    private GraphNode sourceNode;
    private GraphNode targetNode;

    public GraphEdge(double startX, double startY, double endX, double endY, String identifier, double weight,
                     GraphNode sourceNode, GraphNode targetNode) {
//        super(startX, startY, endX, endY);
        this.identifier = identifier;
        this.weight = weight;
        this.sourceNode = sourceNode;
        this.targetNode = targetNode;
    }

    /**
     * Gets identifier.
     *
     * @return the identifier
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Sets identifier.
     *
     * @param identifier the identifier
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
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
