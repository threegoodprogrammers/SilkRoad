package elements;

import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

/**
 * The type Arrow.
 */
public class Arrow extends Path {
    private Point2D ori;
    private Point2D tan;
    private double scale;

    /**
     * Instantiates a new Arrow
     */

    public Arrow(double size) {
        /*
         * Calculate scale using the provided size
         */
        setScale(size);

        /*
         * Set arrow head styles
         */
        setStyle();
    }

    /**
     * Update the arrow head
     *
     * @param edgeCurve the edge curve
     */

    public void update(GraphEdge edgeCurve) {
        /*
         * Calculate the tangent and orientation
         * according to the edge curve
         */
        calculate(edgeCurve);

        /*
         * Move the arrow head to its proper place
         * according to the provided edge curve
         */
        moveArrowHead();
    }

    /**
     * Calculate ori and tan
     *
     * @param edgeCurve the edge curve
     */

    private void calculate(GraphEdge edgeCurve) {
        ori = eval(edgeCurve, 1);
        tan = evalDt(edgeCurve, 1).normalize().multiply(scale);
    }

    /**
     * Move arrow head
     */

    private void moveArrowHead() {
        /*
         * Clear the old arrow head data
         */
        clear();

        /*
         * Create and add the new arrow head data
         */
        this.getElements().add(new MoveTo(ori.getX() - 0.2 * tan.getX() - 0.2 * tan.getY(),
                ori.getY() - 0.2 * tan.getY() + 0.2 * tan.getX()));
        this.getElements().add(new LineTo(ori.getX(), ori.getY()));
        this.getElements().add(new LineTo(ori.getX() - 0.2 * tan.getX() + 0.2 * tan.getY(),
                ori.getY() - 0.2 * tan.getY() - 0.2 * tan.getX()));
    }

    /**
     * Remove the old arrow head
     */

    private void clear() {
        this.getElements().clear();
    }

    /**
     * Set scale
     *
     * @param size size provided to calculate scale
     */

    private void setScale(double size) {
        scale = size / 4d;
    }

    /**
     * Set arrow style classes
     */

    private void setStyle() {
        this.getStyleClass().add("edge-arrow");
        idle();
    }

    /**
     * Leave mouse and set the arrow to idle mode
     */

    public void idle() {
        this.getStyleClass().remove("edge-arrow-select");
        this.getStyleClass().remove("edge-arrow-hover");
        this.getStyleClass().add("edge-arrow-idle");
    }

    /**
     * Hover with mouse
     */

    public void hover() {
        this.getStyleClass().remove("edge-arrow-idle");
        this.getStyleClass().remove("edge-arrow-select");
        this.getStyleClass().add("edge-arrow-hover");
    }

    /**
     * Select edge with mouse click
     */

    public void select() {
        this.getStyleClass().remove("edge-arrow-idle");
        this.getStyleClass().remove("edge-arrow-hover");
        this.getStyleClass().add("edge-arrow-select");
    }

    /**
     * Evaluate the cubic curve at a parameter 0<=t<=1, returns a Point2D
     *
     * @param c the GraphEdge
     * @param t param between 0 and 1
     * @return a Point2D
     */
    private Point2D eval(GraphEdge c, float t) {
        Point2D p = new Point2D(Math.pow(1 - t, 3) * c.getStartX() +
                3 * t * Math.pow(1 - t, 2) * c.getControlX1() +
                3 * (1 - t) * t * t * c.getControlX2() +
                Math.pow(t, 3) * c.getEndX(),
                Math.pow(1 - t, 3) * c.getStartY() +
                        3 * t * Math.pow(1 - t, 2) * c.getControlY1() +
                        3 * (1 - t) * t * t * c.getControlY2() +
                        Math.pow(t, 3) * c.getEndY());
        return p;
    }

    /**
     * Evaluate the tangent of the cubic curve at a parameter 0<=t<=1, returns a Point2D
     *
     * @param c the GraphEdge
     * @param t param between 0 and 1
     * @return a Point2D
     */
    private Point2D evalDt(GraphEdge c, float t) {
        Point2D p = new Point2D(-3 * Math.pow(1 - t, 2) * c.getStartX() +
                3 * (Math.pow(1 - t, 2) - 2 * t * (1 - t)) * c.getControlX1() +
                3 * ((1 - t) * 2 * t - t * t) * c.getControlX2() +
                3 * Math.pow(t, 2) * c.getEndX(),
                -3 * Math.pow(1 - t, 2) * c.getStartY() +
                        3 * (Math.pow(1 - t, 2) - 2 * t * (1 - t)) * c.getControlY1() +
                        3 * ((1 - t) * 2 * t - t * t) * c.getControlY2() +
                        3 * Math.pow(t, 2) * c.getEndY());
        return p;
    }

    public ObservableList<String> style() {
        return this.getStyleClass();
    }
}
