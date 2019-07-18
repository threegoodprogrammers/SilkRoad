package elements;

import graph.App;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.StrokeTransition;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import javafx.util.Duration;

import java.util.HashMap;

/**
 * The type Transition manager
 */
public class TransitionManager {
    private App app;
    private MenuManager menuManager;
    private boolean wait;

    public enum Part {
        ONE, TWO, THREE
    }

    public enum EdgeType {
        DIRECTIONAL, NON_DIRECTIONAL
    }

    private EdgeType edgeType;
    private Part currentPart;

    private GraphEdge edge;
    private NonDirectionalEdge nonDirectionalEdge;
    private Arrow edgeArrow;

    private float baseDuration = 1000;

    private StrokeTransition partOneEdge;
    private StrokeTransition partOneArrow;

    private StrokeTransition partTwoEdge;
    private StrokeTransition partTwoArrow;

    private StrokeTransition partThreeEdge;
    private StrokeTransition partThreeArrow;

    private ParallelTransition partOneDirectional;
    private ParallelTransition partTwoDirectional;
    private ParallelTransition partThreeDirectional;

    private ParallelTransition partOneNonDirectional;
    private ParallelTransition partTwoNonDirectional;
    private ParallelTransition partThreeNonDirectional;

    /**
     * Instantiates a new Transition manager
     */

    public TransitionManager(App app, MenuManager menuManager) {
        this.app = app;
        this.menuManager = menuManager;

        /*
         * Initialize transitions
         */
        initialize();
    }

    /**
     * Sets part one transition
     */
    public void initialize() {
        partOneEdge = new StrokeTransition(Duration.millis(baseDuration));
        partOneEdge.setToValue(Color.valueOf("#f49496"));

        partOneArrow = new StrokeTransition(Duration.millis(baseDuration));
        partOneArrow.setToValue(Color.valueOf("#f49496"));

        partTwoEdge = new StrokeTransition(Duration.millis(baseDuration));
        partTwoEdge.setToValue(Color.valueOf("#66bde1"));

        partTwoArrow = new StrokeTransition(Duration.millis(baseDuration));
        partTwoArrow.setToValue(Color.valueOf("#66bde1"));

        partThreeEdge = new StrokeTransition(Duration.millis(baseDuration));
        partThreeEdge.setToValue(Color.valueOf("#f49496"));

        partThreeArrow = new StrokeTransition(Duration.millis(baseDuration));
        partThreeArrow.setToValue(Color.valueOf("#f49496"));

        partOneDirectional = new ParallelTransition(partOneEdge, partOneArrow);
        partTwoDirectional = new ParallelTransition(partTwoEdge, partTwoArrow);
        partThreeDirectional = new ParallelTransition(partThreeEdge, partThreeArrow);

        partOneNonDirectional = new ParallelTransition(partOneEdge);
        partTwoNonDirectional = new ParallelTransition(partTwoEdge);
        partThreeNonDirectional = new ParallelTransition(partThreeEdge);
    }

    /**
     * Start traversing through nodes and edges
     *
     * @param nodesAndEdges the nodes and edges
     */

    public void start(HashMap<GraphNode, CubicCurve> nodesAndEdges) throws InterruptedException {
        for (GraphNode node : nodesAndEdges.keySet()) {
            /*
             * Check pause
             */
            checkPause();

            if (nodesAndEdges.get(node).getClass() == GraphEdge.class) {
                /*
                 * Get the edge from pair
                 */
                GraphEdge edge = (GraphEdge) nodesAndEdges.get(node);

                /*
                 * Set transition edge
                 */
                setEdge(edge);

                /*
                 * Set after finished events
                 */
                setAfterFinished(node);

                /*
                 * Iterate through animations
                 */
                iterateAnimations();

                /*
                 * Return if playing is stopped
                 */
                if (isStopped()) {
                    stop();
                    app.stopPlaying();
                    return;
                }
            } else {
                /*
                 * Get the edge from pair
                 */
                NonDirectionalEdge edge = (NonDirectionalEdge) nodesAndEdges.get(node);

                /*
                 * Set transition edge
                 */
                setNonDirectionalEdge(edge);

                /*
                 * Set after finished events
                 */
                setAfterFinished(node);

                /*
                 * Iterate through animations
                 */
                iterateAnimations();

                /*
                 * Return if playing is stopped
                 */
                if (isStopped()) {
                    stop();
                    app.stopPlaying();
                    return;
                }
            }
        }

        /*
         * Update runtime menu
         */
        menuManager.updateButtons(MenuManager.State.FINISHED_PLAYING);

        /*
         * Return if playing is stopped
         */
        while (!isStopped()) {
            Thread.sleep(10);
        }

        stop();
        app.stopPlaying();
    }

    /**
     * Go to next part
     */

    private void nextPart() {
        if (currentPart == Part.ONE) {
            currentPart = Part.TWO;
        } else if (currentPart == Part.TWO) {
            currentPart = Part.THREE;
        }
    }

    /**
     * Iterate through parts and play the corresponding animation per iteration.
     *
     * @throws InterruptedException the interrupted exception
     */

    private void iterateAnimations() throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            /*
             * Start playing
             */
            play();

            /*
             * Wait until playing is finished
             */
            checkWaiting();

            /*
             * Return if playing is stopped
             */
            if (isStopped()) {
                return;
            }

            /*
             * The go to the next part
             */
            nextPart();
        }
    }

    /**
     * Check for pause
     */

    private void checkPause() throws InterruptedException {
        boolean paused = app.isPaused();

        if (paused) {
            pause();
        }

        while (app.isPaused()) {
            if (isStopped()) {
                return;
            }

            Thread.sleep(5);
        }

        if (paused) {
            restart();
        }
    }

    /**
     * Check waiting
     */

    private void checkWaiting() throws InterruptedException {
        while (isWaiting()) {
            if (isStopped()) {
                return;
            }

            Thread.sleep(5);

            checkPause();
        }
    }

    /**
     * Check stopped
     */

    private boolean isStopped() {
        return app.getCurrentState() != App.State.PLAYING;
    }

    /**
     * Sets edge.
     *
     * @param edge the edge
     */

    public void setEdge(GraphEdge edge) {
        this.edge = edge;
        this.edgeArrow = edge.getArrowHead();

        /*
         * Set transition shapes
         */
        this.partOneEdge.setShape(this.edge);
        this.partTwoEdge.setShape(this.edge);
        this.partThreeEdge.setShape(this.edge);

        /*
         * Set transition shapes
         */
        this.partOneArrow.setShape(this.edgeArrow);
        this.partTwoArrow.setShape(this.edgeArrow);
        this.partThreeArrow.setShape(this.edgeArrow);

        /*
         * Sets current edge type and part
         */
        edgeType = EdgeType.DIRECTIONAL;
        currentPart = Part.ONE;
    }

    /**
     * Sets non directional edge.
     *
     * @param edge the edge
     */

    private void setNonDirectionalEdge(NonDirectionalEdge edge) {
        this.nonDirectionalEdge = edge;
        this.edgeArrow = null;

        /*
         * Set transition shapes
         */
        this.partOneEdge.setShape(edge);
        this.partTwoEdge.setShape(edge);
        this.partThreeEdge.setShape(edge);

        /*
         * Sets current edge type and part
         */
        edgeType = EdgeType.NON_DIRECTIONAL;
        currentPart = Part.ONE;
    }

    /**
     * Sets after transition finished task
     */

    private void setAfterFinished(GraphNode node) {
        switch (this.edgeType) {
            case DIRECTIONAL:
                setAfterFinished();
                this.partThreeDirectional.setOnFinished(event -> {
                    app.highlightNode(node);
                    goOn();
                });

                break;
            case NON_DIRECTIONAL:
                setAfterFinished();
                this.partThreeNonDirectional.setOnFinished(event -> {
                    app.highlightNode(node);
                    goOn();
                });

                break;
        }
    }

    /**
     * Sets after transition finished task
     */

    public void setAfterFinished() {
        switch (this.edgeType) {
            case DIRECTIONAL:
                this.partOneDirectional.setOnFinished(event -> {
                    app.highlightEdge(this.edge);
                    goOn();
                });

                this.partTwoDirectional.setOnFinished(event -> goOn());

                break;
            case NON_DIRECTIONAL:
                this.partOneNonDirectional.setOnFinished(event -> {
                    app.highlightNonDirEdge(this.nonDirectionalEdge);
                    goOn();
                });
                this.partTwoNonDirectional.setOnFinished(event -> goOn());

                break;
        }
    }

    /**
     * Update speed
     */

    public void updateSpeed() {
        float speed = app.getSpeed();

        /*
         * Update durations
         */
        this.partOneEdge.setDuration(Duration.millis(baseDuration / speed));
        this.partTwoEdge.setDuration(Duration.millis(baseDuration / speed));
        this.partThreeEdge.setDuration(Duration.millis(baseDuration / speed));
        this.partOneArrow.setDuration(Duration.millis(baseDuration / speed));
        this.partTwoArrow.setDuration(Duration.millis(baseDuration / speed));
        this.partThreeArrow.setDuration(Duration.millis(baseDuration / speed));

        /*
         * Pause and play from start
         */
        if (app.isPaused()) {
            app.pressPlay();
        }
        pause();
        restart();
    }

    /**
     * Play animations
     */

    private void play() {
        /*
         * Go into waiting mode
         */
        doWait();

        switch (currentPart) {
            case ONE:
                if (edgeType == EdgeType.DIRECTIONAL) {
                    partOneDirectional.play();
                } else {
                    partOneNonDirectional.play();
                }

                break;
            case TWO:
                if (edgeType == EdgeType.DIRECTIONAL) {
                    partTwoDirectional.play();
                } else {
                    partTwoNonDirectional.play();
                }

                break;
            case THREE:
            default:
                if (edgeType == EdgeType.DIRECTIONAL) {
                    partThreeDirectional.play();
                } else {
                    partThreeNonDirectional.play();
                }

                break;
        }
    }

    /**
     * Stop animations
     */

    private void stop() {
        switch (currentPart) {
            case ONE:
                if (edgeType == EdgeType.DIRECTIONAL) {
                    partOneDirectional.stop();
                    this.edge.idle();
                } else {
                    partOneNonDirectional.stop();
                    this.nonDirectionalEdge.idle();
                }

                break;
            case TWO:
                if (edgeType == EdgeType.DIRECTIONAL) {
                    partTwoDirectional.stop();
                } else {
                    partTwoNonDirectional.stop();
                }

                break;
            case THREE:
            default:
                if (edgeType == EdgeType.DIRECTIONAL) {
                    partThreeDirectional.stop();
                } else {
                    partThreeNonDirectional.stop();
                }

                break;
        }
    }

    /**
     * Pause
     */

    private void pause() {
        switch (currentPart) {
            case ONE:
                if (edgeType == EdgeType.DIRECTIONAL) {
                    partOneDirectional.pause();
                } else {
                    partOneNonDirectional.pause();
                }

                break;
            case TWO:
                if (edgeType == EdgeType.DIRECTIONAL) {
                    partTwoDirectional.pause();
                } else {
                    partTwoNonDirectional.pause();
                }

                break;
            case THREE:
            default:
                if (edgeType == EdgeType.DIRECTIONAL) {
                    partThreeDirectional.pause();
                } else {
                    partThreeNonDirectional.pause();
                }

                break;
        }
    }

    /**
     * Restart
     */

    private void restart() {
        switch (currentPart) {
            case ONE:
                if (edgeType == EdgeType.DIRECTIONAL) {
                    partOneDirectional.playFromStart();
                } else {
                    partOneNonDirectional.playFromStart();
                }

                break;
            case TWO:
                if (edgeType == EdgeType.DIRECTIONAL) {
                    partTwoDirectional.playFromStart();
                } else {
                    partTwoNonDirectional.playFromStart();
                }

                break;
            case THREE:
            default:
                if (edgeType == EdgeType.DIRECTIONAL) {
                    partThreeDirectional.playFromStart();
                } else {
                    partThreeNonDirectional.playFromStart();
                }

                break;
        }
    }

    /**
     * Do wait
     */

    private void doWait() {
        this.wait = true;
    }

    /**
     * Go on
     */

    private void goOn() {
        this.wait = false;
    }

    /**
     * Check if app is waiting for another task to finish
     *
     * @return the boolean
     */

    private boolean isWaiting() {
        return this.wait;
    }

    /**
     * Gets fade transition
     *
     * @param duration the duration
     * @param node     the node
     * @return the fade transition
     */

    public static FadeTransition getFadeTransition(Duration duration, Node node, Double toValue) {
        FadeTransition transition = new FadeTransition(duration, node);
        transition.setToValue(toValue);
        return transition;
    }


}
