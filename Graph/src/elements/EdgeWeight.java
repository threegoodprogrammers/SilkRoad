package elements;

import javafx.geometry.Bounds;
import javafx.scene.control.Label;

/**
 * The type Edge weight.
 */
public class EdgeWeight extends Label {
    private double posX;
    private double posY;

    /**
     * Instantiates a new edge weight label
     */

    public EdgeWeight(double weight) {
        setWeight(weight);
        setStyle();
    }

    /**
     * Update the weight label
     *
     * @param edgeCurve the edge curve
     * @param scale     the scale
     */

    public void update(GraphEdge edgeCurve, double scale) {
        /*
         * Calculate the co-ordinates
         */
        calculate(edgeCurve, scale);

        /*
         * Move the edge weight label to
         * its proper position on the screen
         */
        moveEdgeWeight();
    }

    /**
     * Move the edge weight label
     */

    private void moveEdgeWeight() {
        this.setTranslateX(this.posX);
        this.setTranslateY(this.posY);
    }

    /**
     * Calculate the co-ordinations of the weight label
     *
     * @param edgeCurve the edge curve
     */

    private void calculate(GraphEdge edgeCurve, double scale) {
        /*
         * Calculate X co-ordinates of weight label
         */
        this.posX = edgeCurve.getStartX() + edgeCurve.getEndX() / 2 - width() / 2 / scale;

        /*
         * Calculate Y co-ordinates of weight label
         */
        this.posY = edgeCurve.getStartY() + edgeCurve.getEndY() / 2 - height() / 2 / scale;
    }

    /**
     * Set weight label style classes
     */

    private void setStyle() {
        this.getStyleClass().add("edge-weight");
        idle();
    }

    /**
     * Hover with mouse
     */

    public void hover() {
        this.getStyleClass().remove("edge-weight-idle");
        this.getStyleClass().remove("edge-weight-select");
        this.getStyleClass().add("edge-weight-hover");
    }

    /**
     * Leave mouse and set the arrow to idle mode
     */

    public void idle() {
        this.getStyleClass().remove("edge-weight-select");
        this.getStyleClass().remove("edge-weight-hover");
        this.getStyleClass().add("edge-weight-idle");
    }

    /**
     * Select edge with mouse click
     */

    public void select() {
        this.getStyleClass().remove("edge-weight-idle");
        this.getStyleClass().remove("edge-weight-hover");
        this.getStyleClass().add("edge-weight-select");
    }

    /**
     * Update node bounds in scene
     */

    private Bounds getBounds() {
        return this.localToScene(this.getBoundsInLocal());
    }

    /**
     * Get label height
     *
     * @return the height
     */

    private double height() {
        return getBounds().getHeight();
    }

    /**
     * Get label width
     *
     * @return the width
     */

    private double width() {
        return getBounds().getWidth();
    }

    /**
     * Sets weight
     *
     * @param weight the weight
     */

    public void setWeight(double weight) {
        this.setText(String.valueOf(weight));
    }

    /**
     * Gets co-ordinate of x
     *
     * @return the co-ordinate of x
     */

    public double getPosX() {
        return posX;
    }

    /**
     * Sets co-ordinate of x
     *
     * @param posX the co-ordinate of x
     */

    public void setPosX(double posX) {
        this.posX = posX;
    }

    /**
     * Gets co-ordinate of y
     *
     * @return the co-ordinate of y
     * y
     */
    public double getPosY() {
        return posY;
    }

    /**
     * Sets co-ordinate of y.
     *
     * @param posY the co-ordinate of y
     */

    public void setPosY(double posY) {
        this.posY = posY;
    }
}
