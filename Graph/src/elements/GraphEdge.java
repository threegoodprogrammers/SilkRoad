package elements;

import javafx.scene.control.Label;
import javafx.scene.shape.CubicCurve;

public class GraphEdge extends CubicCurve {
    private double weight;
    private GraphNode sourceNode;
    private GraphNode targetNode;
    private EdgeWeight weightLabel;

    /**
     * Edge orientation (horizontal or vertical)
     */

    private Graph.EdgeOrientation edgeOrientation;

    /**
     * Instantiates a new Graph edge.
     *
     * @param weight     the weight
     * @param sourceNode the source node
     * @param targetNode the target node
     */

    public GraphEdge(double weight,
                     GraphNode sourceNode, GraphNode targetNode,
                     Graph.EdgeOrientation edgeOrientation) {
        this.weight = weight;
        this.sourceNode = sourceNode;
        this.targetNode = targetNode;
        this.edgeOrientation = edgeOrientation;

        /*
         * Set weight label and edge transitions
         */
//        setWeightLabel(createWeightLabel());
//        setHoverTransitions();
    }


    /**
     * Invert edge direction
     */

    public void invertDirection() {

    }

    /**
     * Create new weight label for the edge
     */

    private void createWeightLabel() {
        this.weightLabel = new EdgeWeight(this.weight);
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
        /*
         * Set weight property of edge
         */
        this.weight = weight;

        /*
         * Update the value on the weight label node
         */
        this.weightLabel.setWeight(weight);
    }

    /**
     * Gets weight label
     *
     * @return the weight label
     */

    public Label getWeightLabel() {
        return this.weightLabel;
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

    /**
     * Gets edge orientation
     *
     * @return the edge orientation
     */
    public Graph.EdgeOrientation getEdgeOrientation() {
        return edgeOrientation;
    }

    /**
     * Sets edge orientation
     *
     * @param edgeOrientation the edge orientation
     */

    public void setEdgeOrientation(Graph.EdgeOrientation edgeOrientation) {
        this.edgeOrientation = edgeOrientation;
    }

//    public

//    /**
//     * Set edge hover transitions
//     */
//
//    private void setHoverTransitions() {
//        /*
//         * Add mouse enter event listener
//         */
//        this.addEventFilter(MouseEvent.MOUSE_ENTERED, event -> {
//            App.hoverEdge(this);
//        });
//
//        /*
//         * Add mouse leave event listener
//         */
//        this.addEventFilter(MouseEvent.MOUSE_EXITED, event -> {
//            App.leaveEdge(this);
//        });
//    }

}
