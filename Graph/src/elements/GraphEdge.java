package elements;

import javafx.scene.control.Label;
import javafx.scene.shape.CubicCurve;

/**
 * The type Graph edge.
 */
public class GraphEdge extends CubicCurve {
    private double weight;
    private GraphNode sourceNode;
    private GraphNode targetNode;
    private EdgeWeight weightLabel;

    /**
     * Cubic curve properties
     */
    private double startX;
    private double startY;
    private double endX;
    private double endY;
    private double controlX1;
    private double controlY1;
    private double controlX2;
    private double controlY2;

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
     * Calculate
     */

    public void calculate() {
        switch (this.edgeOrientation) {
            case VERTICAL:
                /*
                 * Calculate co-ordinates for vertical edge
                 */
                calculateVertical();
                break;
            case HORIZONTAL:
                /*
                 * Calculate co-ordinates for horizontal edge
                 */
            default:
                calculateHorizontal();
        }
    }

    /**
     * Calculate co-ordinates for horizontal edge
     */

    private void calculateHorizontal() {
        GraphNode left, right;

        /*
         * Calculate the middle point
         */
        double mid = sourceNode.getTranslateX() + 70 + targetNode.getTranslateX();

        /*
         * Set left and right node according to translateX value
         */
        if (sourceNode.getTranslateX() <= targetNode.getTranslateX()) {
            left = sourceNode;
            right = targetNode;

            /*
             * Calculate properties
             */
            this.startX = left.getTranslateX() + 70;
            this.endX = right.getTranslateX() - 8;
            this.startY = 35 + left.getTranslateY();
            this.endY = 35 + right.getTranslateY();
            this.controlX1 = mid / 2 + 150;
            this.controlX2 = mid / 2 - 150;
            this.controlY1 = 35 + left.getTranslateY();
            this.controlY2 = 35 + right.getTranslateY();
        } else {
            left = targetNode;
            right = sourceNode;

            /*
             * Calculate properties
             */
            this.startX = right.getTranslateX();
            this.endX = left.getTranslateX() + 70 + 8;
            this.startY = 35 + right.getTranslateY();
            this.endY = 35 + left.getTranslateY();
            this.controlX1 = mid / 2 - 150;
            this.controlX2 = mid / 2 + 150;
            this.controlY1 = 35 + right.getTranslateY();
            this.controlY2 = 35 + left.getTranslateY();
        }
    }

    /**
     * Calculate co-ordinates for vertical edge
     */

    private void calculateVertical() {
        GraphNode top, bottom;

        /*
         * Calculate the middle point
         */
        double mid = targetNode.getTranslateY() + 70 + sourceNode.getTranslateY();

        /*
         * Set left and right node according to translateX value
         */
        if (sourceNode.getTranslateX() <= targetNode.getTranslateX()) {
            top = sourceNode;
            bottom = targetNode;

            /*
             * Calculate properties
             */
            this.startX = 35 + top.getTranslateX();
            this.endX = 35 + bottom.getTranslateX();
            this.startY = top.getTranslateY() + 70;
            this.endY = bottom.getTranslateY() - 8;
            this.controlX1 = 35 + top.getTranslateX();
            this.controlX2 = 35 + bottom.getTranslateX();
            this.controlY1 = mid / 2 + 150;
            this.controlY2 = mid / 2 - 150;
        } else {
            top = targetNode;
            bottom = sourceNode;

            /*
             * Calculate properties
             */
            this.startX = 35 + bottom.getTranslateX();
            this.endX = 35 + top.getTranslateX();
            this.startY = bottom.getTranslateY();
            this.endY = top.getTranslateY() + 70 + 8;
            this.controlX1 = 35 + bottom.getTranslateX();
            this.controlX2 = 35 + top.getTranslateX();
            this.controlY1 = mid / 2 - 150;
            this.controlY2 = mid / 2 + 150;
        }
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
