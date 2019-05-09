package elements;

import javafx.scene.shape.Line;

public class NonDirectionalEdge extends Line {
    private GraphEdge firstEdge;
    private GraphEdge secondEdge;

    public NonDirectionalEdge(GraphEdge firstEdge, GraphEdge secondEdge) {
        this.firstEdge = firstEdge;
        this.secondEdge = secondEdge;
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
}
