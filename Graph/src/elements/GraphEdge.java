package elements;

import graph.App;
import graph.Main;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;

/**
 * The type Graph edge.
 */
public class GraphEdge extends CubicCurve implements Cloneable {
    private double weight;
    private GraphNode sourceNode;
    private GraphNode targetNode;

    /**
     * Other parts of edge in UI
     */

    private EdgeWeight weightLabel;
    private Arrow arrowHead;

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
     * Edge selection boolean
     */

    private boolean isSelected = false;
    private boolean isHighlighted = false;
    private boolean isDeleted = false;

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
    }

    /**
     * Initialize graph edge different elements
     */

    public void initialize() {
        calculate();
        moveEdge();
        initializeArrowHead();
        initializeWeightLabel();

        /*
         * Set mouse event listeners
         */
        addHoverListeners();
        addLeaveListeners();
        addClickListeners();

        /*
         * Set styles
         */
        setStyle();
    }

    /**
     * Update graph edge
     */

    public void update() {
        calculate();
        moveEdge();
        updateWeightLabel();
        updateArrowHead();
    }

    /**
     * Move edge curve along with arrow and weight label
     */

    private void moveEdge() {
        this.setStartX(this.startX);
        this.setEndX(this.endX);
        this.setStartY(this.startY);
        this.setEndY(this.endY);
        this.setControlX1(this.controlX1);
        this.setControlX2(this.controlX2);
        this.setControlY1(this.controlY1);
        this.setControlY2(this.controlY2);
    }

    /**
     * Calculate
     */

    private void calculate() {
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
            this.endX = right.getTranslateX() - 18;
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
            this.endX = left.getTranslateX() + 70 + 18;
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
        if (sourceNode.getTranslateY() <= targetNode.getTranslateY()) {
            top = sourceNode;
            bottom = targetNode;

            /*
             * Calculate properties
             */
            this.startX = 35 + top.getTranslateX();
            this.endX = 35 + bottom.getTranslateX();
            this.startY = top.getTranslateY() + 70;
            this.endY = bottom.getTranslateY() - 18;
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
            this.endY = top.getTranslateY() + 70 + 18;
            this.controlX1 = 35 + bottom.getTranslateX();
            this.controlX2 = 35 + top.getTranslateX();
            this.controlY1 = mid / 2 - 150;
            this.controlY2 = mid / 2 + 150;
        }
    }

    /**
     * Add edge elements to canvas
     *
     * @param canvas the canvas
     */

    public void addToCanvas(PannableCanvas canvas) {
        canvas.getChildren().addAll(this, this.arrowHead, this.weightLabel);
    }

    /**
     * Remove from canvas.
     *
     * @param canvas the canvas
     */

    public void removeFromCanvas(PannableCanvas canvas) {
        canvas.getChildren().removeAll(this, this.arrowHead, this.weightLabel);
    }

    /**
     * Delete edge elements from canvas
     *
     * @param canvas the canvas
     */

    public void deleteFromCanvas(PannableCanvas canvas) {
        canvas.getChildren().removeAll(this, this.arrowHead, this.weightLabel);
    }

    /**
     * Set edge styless
     */

    private void setStyle() {
        this.getStyleClass().add("edge");
        idle();
    }

    /**
     * Leave mouse and set the arrow to idle mode
     */

    public void idle() {
//        if (isSelected) {
//            /*
//             * If edge is SELECTED
//             */
//            style().remove("edge-idle");
//            style().remove("edge-hover");
//            arrowHead.style().remove("edge-arrow-idle");
//            arrowHead.style().remove("edge-arrow-hover");
//            weightLabel.style().remove("edge-weight-idle");
//            weightLabel.style().remove("edge-weight-hover");
//
//            if (!style().contains("edge-selected")) {
//                style().add("edge-selected");
//            }
//            if (!arrowHead.style().contains("edge-arrow-selected")) {
//                arrowHead.style().add("edge-arrow-selected");
//            }
//            if (!weightLabel.style().contains("edge-weight-selected")) {
//                weightLabel.style().add("edge-weight-selected");
//            }
//        } else {
//            /*
//             * If edge is not SELECTED
//             */
//            if (!isHighlighted) {
//                //////////////////////////
//                /////     NORMAL     /////
//                //////////////////////////
//
//                style().remove("edge-selected");
//                style().remove("edge-hover");
//                style().remove("edge-highlighted");
//                arrowHead.style().remove("edge-arrow-selected");
//                arrowHead.style().remove("edge-arrow-hover");
//                arrowHead.style().remove("edge-arrow-highlighted");
//                weightLabel.style().remove("edge-weight-selected");
//                weightLabel.style().remove("edge-weight-hover");
//                weightLabel.style().remove("edge-weight-highlighted");
//
//                if (!style().contains("edge-idle")) {
//                    style().add("edge-idle");
//                }
//                if (!arrowHead.style().contains("edge-arrow-idle")) {
//                    arrowHead.style().add("edge-arrow-idle");
//                }
//                if (!weightLabel.style().contains("edge-weight-idle")) {
//                    weightLabel.style().add("edge-weight-idle");
//                }
//            } else {
//                /////////////////////////////
//                /////     HIGHLIGHT     /////
//                /////////////////////////////
//
//                style().remove("edge-selected");
//                style().remove("edge-hover");
//                style().remove("edge-idle");
//                arrowHead.style().remove("edge-arrow-selected");
//                arrowHead.style().remove("edge-arrow-hover");
//                arrowHead.style().remove("edge-arrow-idle");
//                weightLabel.style().remove("edge-weight-selected");
//                weightLabel.style().remove("edge-weight-hover");
//                weightLabel.style().remove("edge-weight-idle");
//
//                if (!style().contains("edge-highlighted")) {
//                    style().add("edge-highlighted");
//                }
//                if (!arrowHead.style().contains("edge-arrow-highlighted")) {
//                    arrowHead.style().add("edge-arrow-highlighted");
//                }
//                if (!weightLabel.style().contains("edge-weight-highlighted")) {
//                    weightLabel.style().add("edge-weight-highlighted");
//                }
//            }
//        }

        if (isSelected) {
            /*
             * If edge is SELECTED
             */
            setStroke(Color.valueOf("#f0dd4e"));
            setOpacity(1);
            arrowHead.setStroke(Color.valueOf("#f0dd4e"));
            arrowHead.setOpacity(1);
            weightLabel.style().remove("edge-weight-idle");
            weightLabel.style().remove("edge-weight-hover");

            if (!weightLabel.style().contains("edge-weight-selected")) {
                weightLabel.style().add("edge-weight-selected");
            }
        } else {
            /*
             * If edge is not SELECTED
             */
            if (!isHighlighted) {
                //////////////////////////
                /////     NORMAL     /////
                //////////////////////////

                setStroke();
                weightLabel.style().remove("edge-weight-selected");
                weightLabel.style().remove("edge-weight-hover");
                weightLabel.style().remove("edge-weight-highlighted");

                if (!weightLabel.style().contains("edge-weight-idle")) {
                    weightLabel.style().add("edge-weight-idle");
                }
            } else {
                /////////////////////////////
                /////     HIGHLIGHT     /////
                /////////////////////////////

                setStroke(Color.valueOf("#f49496"));
                arrowHead.setStroke(Color.valueOf("#f49496"));
                weightLabel.style().remove("edge-weight-selected");
                weightLabel.style().remove("edge-weight-hover");
                weightLabel.style().remove("edge-weight-idle");

                if (!weightLabel.style().contains("edge-weight-highlighted")) {
                    weightLabel.style().add("edge-weight-highlighted");
                }
            }
        }
    }

    /**
     * Hover with mouse
     */

    public void hover() {
//        if (!isSelected) {
//            /*
//             * If edge is not SELECTED
//             */
//            style().remove("edge-selected");
//            style().remove("edge-idle");
//            arrowHead.style().remove("edge-arrow-selected");
//            arrowHead.style().remove("edge-arrow-idle");
//            weightLabel.style().remove("edge-weight-selected");
//            weightLabel.style().remove("edge-weight-idle");
//
//            if (!style().contains("edge-hover")) {
//                style().add("edge-hover");
//            }
//            if (!arrowHead.style().contains("edge-arrow-hover")) {
//                arrowHead.style().add("edge-arrow-hover");
//            }
//            if (!weightLabel.style().contains("edge-weight-hover")) {
//                weightLabel.style().add("edge-weight-hover");
//            }
//        }

        if (!isSelected && !isHighlighted) {
            /*
             * If edge is not SELECTED
             */
            setStroke(Color.valueOf("#f39c12"));
            setOpacity(1);
            arrowHead.setStroke(Color.valueOf("#f39c12"));
            arrowHead.setOpacity(1);
            weightLabel.style().remove("edge-weight-selected");
            weightLabel.style().remove("edge-weight-idle");

            if (!weightLabel.style().contains("edge-weight-hover")) {
                weightLabel.style().add("edge-weight-hover");
            }
        }
    }

    /**
     * Delete
     */

    public void delete() {
        this.isDeleted = true;
    }

    /**
     * Highlight
     */

    public void highlight() {
        this.isHighlighted = true;

        idle();
        Platform.runLater(this::toFront);
    }

    /**
     * Revert highlight
     */

    public void revertHighlight() {
        this.isHighlighted = false;

        idle();
    }

    /**
     * Hide edge.
     */

    public void hideEdge() {
        this.setVisible(false);
        this.arrowHead.setVisible(false);
        this.weightLabel.setVisible(false);
    }

    /**
     * Show edge.
     */

    public void showEdge(boolean nonDirectional) {
        this.setVisible(true);
        this.arrowHead.setVisible(!nonDirectional);
        this.weightLabel.setVisible(false);
    }

    /**
     * Sets dashed stroke style
     */

    public void setDashed() {
        this.getStrokeDashArray().addAll(25d, 10d);
    }

    /**
     * Sets solid stroke style
     */

    public void setSolid() {
        this.getStrokeDashArray().clear();
    }

    /**
     * Invert edge direction
     */

    public void invertDirection() {

    }

    /**
     * Create new weight label for the edge
     */

    private void initializeWeightLabel() {
        /*
         * Initialize new edge weight label
         */
        this.weightLabel = new EdgeWeight(this.weight);

        /*
         * Update the edge weight label according to edge curve
         */
        updateWeightLabel();
    }

    /**
     * Update weight label
     */

    private void updateWeightLabel() {
        this.weightLabel.update(this);
    }

    /**
     * Create new arrow head according to edge
     */

    private void initializeArrowHead() {
        /*
         * Initialize new edge weight label
         */
        this.arrowHead = new Arrow(200);

        /*
         * Update the edge arrow head according to edge curve
         */
        updateArrowHead();
    }

    /**
     * Update arrow head
     */

    private void updateArrowHead() {
        this.arrowHead.update(this);
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

    public EdgeWeight getWeightLabel() {
        return this.weightLabel;
    }

    /**
     * Gets arrow head.
     *
     * @return the arrow head
     */

    public Arrow getArrowHead() {
        return this.arrowHead;
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

    /**
     * Hover edge
     */

    public void hoverEdge() {
        hover();
        this.sourceNode.sendToFront();
        this.targetNode.sendToFront();

        /*
         * Set dashed style on edge
         */
        setDashed();

        /*
         * Send edge elements to front
         */
        sendToFront();
    }

    /**
     * Select edge
     */

    public void selectEdge() {
        this.isSelected = true;
        idle();
    }

    /**
     * Deselect edge
     */

    public void deselectEdge() {
        this.isSelected = false;
        idle();
    }

    /**
     * Set fill
     */

    public void setStroke() {
        this.setStroke(Color.valueOf("rgba(102, 189, 225, 0.5)"));
        this.arrowHead.setStroke(Color.valueOf("rgba(102, 189, 225, 0.5)"));
    }

    /**
     * Leave edge
     */

    public void leaveEdge() {
        idle();

        /*
         * Set solid style on edge
         */
        setSolid();

        /*
         * Send all nodes to front
         */
        Main.app.sendNodesToFront();
    }

    /**
     * Add hover listener to edge elements
     */

    private void addHoverListeners() {
        EventHandler<MouseEvent> mouseHover = event -> {
            if (!isDeleted) {
                hoverEdge();
                Main.app.hoverItem();
            }
        };

        this.addEventFilter(MouseEvent.MOUSE_ENTERED, mouseHover);
        this.arrowHead.addEventFilter(MouseEvent.MOUSE_ENTERED, mouseHover);
        this.weightLabel.addEventFilter(MouseEvent.MOUSE_ENTERED, mouseHover);
    }

    /**
     * Add leave listener to edge elements
     */

    private void addLeaveListeners() {
        EventHandler<MouseEvent> mouseLeave = event -> {
            if (!isDeleted) {
                leaveEdge();
                Main.app.leaveItem();
            }
        };

        this.addEventFilter(MouseEvent.MOUSE_EXITED, mouseLeave);
        this.arrowHead.addEventFilter(MouseEvent.MOUSE_EXITED, mouseLeave);
        this.weightLabel.addEventFilter(MouseEvent.MOUSE_EXITED, mouseLeave);
    }

    /**
     * Add click listener to edge elements
     */

    private void addClickListeners() {
        EventHandler<MouseEvent> mouseClick = event -> {
            if (Main.app.getCurrentState() != App.State.PLAYING && Main.app.getCurrentMode() == App.Mode.SELECT) {
                Main.app.select(this);
            }
            event.consume();
        };

        this.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseClick);
        this.arrowHead.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseClick);
        this.weightLabel.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseClick);
    }

    /**
     * Send edge elements to front
     */

    public void sendToFront() {
        this.toFront();
        this.weightLabel.toFront();
        this.arrowHead.toFront();
    }

    private ObservableList<String> style() {
        return this.getStyleClass();
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
