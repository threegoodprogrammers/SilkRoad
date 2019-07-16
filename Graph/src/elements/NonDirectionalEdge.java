package elements;

import graph.App;
import graph.Main;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.CubicCurve;

/**
 * The type non-directional edge
 */
public class NonDirectionalEdge extends CubicCurve implements Cloneable {
    private double weight;
    private GraphEdge firstEdge;
    private GraphEdge secondEdge;

    /**
     * Other parts of edge in UI
     */

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
     * Edge selection boolean
     */

    private boolean isSelected = false;
    private boolean isDeleted = false;

    /**
     * Instantiates a new non-directional edge.
     *
     * @param weight     the weight
     * @param firstEdge  the first edge
     * @param secondEdge the second edge
     */

    public NonDirectionalEdge(double weight,
                              GraphEdge firstEdge, GraphEdge secondEdge) {
        this.weight = weight;
        this.firstEdge = firstEdge;
        this.secondEdge = secondEdge;
    }

    /**
     * Initialize graph edge different elements
     */

    public void initialize() {
        calculate();
        moveEdge();
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
        GraphNode left, right, sourceNode, targetNode;

        sourceNode = this.firstEdge.getSourceNode();
        targetNode = this.firstEdge.getTargetNode();

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
            this.endX = right.getTranslateX();
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
            this.endX = left.getTranslateX() + 70;
            this.startY = 35 + right.getTranslateY();
            this.endY = 35 + left.getTranslateY();
            this.controlX1 = mid / 2 - 150;
            this.controlX2 = mid / 2 + 150;
            this.controlY1 = 35 + right.getTranslateY();
            this.controlY2 = 35 + left.getTranslateY();
        }
    }

    /**
     * Add edge elements to canvas
     *
     * @param canvas the canvas
     */

    public void addToCanvas(PannableCanvas canvas) {
        canvas.getChildren().addAll(this, this.weightLabel);
    }

    /**
     * Remove from canvas.
     *
     * @param canvas the canvas
     */

    public void removeFromCanvas(PannableCanvas canvas) {
        canvas.getChildren().removeAll(this, this.weightLabel);
    }

    /**
     * Delete edge elements from canvas
     *
     * @param canvas the canvas
     */

    public void deleteFromCanvas(PannableCanvas canvas) {
        canvas.getChildren().removeAll(this, this.weightLabel);
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
        if (isSelected) {
            /*
             * If edge is SELECTED
             */
            style().remove("edge-idle");
            style().remove("edge-hover");
            weightLabel.style().remove("edge-weight-idle");
            weightLabel.style().remove("edge-weight-hover");

            if (!style().contains("edge-selected")) {
                style().add("edge-selected");
            }
            if (!weightLabel.style().contains("edge-weight-selected")) {
                weightLabel.style().add("edge-weight-selected");
            }
        } else {
            /*
             * If edge is not SELECTED
             */
            style().remove("edge-selected");
            style().remove("edge-hover");
            weightLabel.style().remove("edge-weight-selected");
            weightLabel.style().remove("edge-weight-hover");

            if (!style().contains("edge-idle")) {
                style().add("edge-idle");
            }
            if (!weightLabel.style().contains("edge-weight-idle")) {
                weightLabel.style().add("edge-weight-idle");
            }
        }
    }

    /**
     * Hover with mouse
     */

    public void hover() {
        if (!isSelected) {
            /*
             * If edge is not SELECTED
             */
            style().remove("edge-selected");
            style().remove("edge-idle");
            weightLabel.style().remove("edge-weight-selected");
            weightLabel.style().remove("edge-weight-idle");

            if (!style().contains("edge-hover")) {
                style().add("edge-hover");
            }
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
     * Hover edge
     */

    public void hoverEdge() {
        hover();
        this.firstNode().sendToFront();
        this.secondNode().sendToFront();

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
        App.sendNodesToFront();
    }

    /**
     * Add hover listener to edge elements
     */

    private void addHoverListeners() {
        EventHandler<MouseEvent> mouseHover = event -> {
            if (!this.isDeleted) {
                hoverEdge();
                Main.app.hoverItem();
            }
        };

        this.addEventFilter(MouseEvent.MOUSE_ENTERED, mouseHover);
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
        this.weightLabel.addEventFilter(MouseEvent.MOUSE_EXITED, mouseLeave);
    }

    /**
     * Add click listener to edge elements
     */

    private void addClickListeners() {
        EventHandler<MouseEvent> mouseClick = event -> {
            if (Main.app.getCurrentMode() == App.Mode.SELECT) {
                Main.app.select(this);
            }
            event.consume();
        };

        this.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseClick);
        this.weightLabel.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseClick);
    }

    /**
     * Send edge elements to front
     */

    public void sendToFront() {
        this.toFront();
        this.weightLabel.toFront();
    }

    /**
     * Gets first edge.
     *
     * @return the first edge
     */

    public GraphEdge getFirstEdge() {
        return firstEdge;
    }

    /**
     * Sets first edge.
     *
     * @param firstEdge the first edge
     */

    public void setFirstEdge(GraphEdge firstEdge) {
        this.firstEdge = firstEdge;
    }

    /**
     * Gets second edge.
     *
     * @return the second edge
     */

    public GraphEdge getSecondEdge() {
        return secondEdge;
    }

    /**
     * Sets second edge.
     *
     * @param secondEdge the second edge
     */

    public void setSecondEdge(GraphEdge secondEdge) {
        this.secondEdge = secondEdge;
    }

    /**
     * First node graph node.
     *
     * @return the graph node
     */
    public GraphNode firstNode() {
        return this.firstEdge.getSourceNode();
    }

    /**
     * Second node graph node.
     *
     * @return the graph node
     */
    public GraphNode secondNode() {
        return this.firstEdge.getTargetNode();
    }

    private ObservableList<String> style() {
        return this.getStyleClass();
    }
}
