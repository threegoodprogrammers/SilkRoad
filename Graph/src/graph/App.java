package graph;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import elements.GraphNode;
import elements.NodeGestures;
import elements.PannableCanvas;
import elements.SceneGestures;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 * The type App.
 */
public class App {
    public FontAwesomeIconView panIcon;
    public StackPane panIconPane;
    public GridPane background;
    public Pane overlay;
    public Pane leftPane;
    public Pane rightPane;
    public Pane bottomPane;
    public Pane topPane;
    public Pane topActionPane;
    public Pane actionLabelPane;
    public Button traverseButton;

    public enum State {
        IDLE, PAN, MOVE
    }

    public enum Operation {
        DIJKSTRA, ANT
    }

    private boolean shiftPressed = false;
    private boolean ctrlPressed = false;
    private boolean leftClickPressed = false;

    private State state;
    public GridPane window;
    public PannableCanvas canvas;
    private NodeGestures nodeGestures;
    private SceneGestures sceneGestures;
    private static Scene scene;

    public void set(Scene scene) {
        /*
         * Set scene elements and listeners
         */
        setScene(scene);

        /*
         * Set the current state to interact mode
         */
        interactMode();

        GraphNode graphNode = new GraphNode("1");
        graphNode.setTranslateX(50);
        graphNode.setTranslateY(50);
        graphNode.setPrefHeight(70);
        graphNode.setPrefWidth(70);
        graphNode.getStyleClass().add("node");
        graphNode.addEventFilter(MouseEvent.MOUSE_PRESSED, nodeGestures.getOnMousePressedEventHandler());
        graphNode.addEventFilter(MouseEvent.MOUSE_DRAGGED, nodeGestures.getOnMouseDraggedEventHandler());

        GraphNode graphNode2 = new GraphNode("2");
        graphNode2.setTranslateX(200);
        graphNode2.setTranslateY(200);
        graphNode2.setPrefHeight(70);
        graphNode2.setPrefWidth(70);
        graphNode2.getStyleClass().add("node");
        graphNode2.addEventFilter(MouseEvent.MOUSE_PRESSED, nodeGestures.getOnMousePressedEventHandler());
        graphNode2.addEventFilter(MouseEvent.MOUSE_DRAGGED, nodeGestures.getOnMouseDraggedEventHandler());

        this.canvas.getChildren().addAll(graphNode, graphNode2);

//        FontAwesomeIconView font = new FontAwesomeIconView(FontAwesomeIcon.HAND_PAPER_ALT);

//        GraphNode node = new GraphNode("1", "1");
//        pane.getChildren().add(node);
//        node.setLayoutX(700);
//        node.setLayoutY(500);
    }

    /**
     * Set scene elements and listeners
     *
     * @param scene Main application scene
     */

    private void setScene(Scene scene) {
        this.nodeGestures = new NodeGestures(this.canvas, this);
        App.scene = scene;
        this.sceneGestures = new SceneGestures(this.canvas, this);
        App.scene.addEventFilter(MouseEvent.MOUSE_PRESSED, sceneGestures.getOnMousePressedEventHandler());
        App.scene.addEventFilter(MouseEvent.MOUSE_DRAGGED, sceneGestures.getOnMouseDraggedEventHandler());
        App.scene.addEventFilter(MouseDragEvent.MOUSE_RELEASED, sceneGestures.getOnMouseReleasedEventHandler());
        App.scene.addEventFilter(KeyEvent.KEY_PRESSED, sceneGestures.getOnKeyPressedEventHandler());
        App.scene.addEventFilter(KeyEvent.KEY_RELEASED, sceneGestures.getOnKeyReleasedEventHandler());

        App.scene.addEventFilter(ScrollEvent.ANY, sceneGestures.getOnScrollEventHandler());
        background.toBack();
        panIconPane.toBack();
        canvas.toFront();

    }

    /**
     * Switch to Move state
     */

    public void moveMode() {
        this.state = State.MOVE;
    }

    /**
     * Switch to Interact state
     */

    public void interactMode() {
        this.state = State.IDLE;
    }

    /**
     * Switch to Pan state
     */

    public void panMode() {
        this.state = State.PAN;
    }

    /**
     * Set cursor
     */

    private void setCursor(Cursor cursor) {
        scene.setCursor(cursor);
    }

    /**
     * Hide cursor
     */

    private void hideCursor() {
        scene.setCursor(Cursor.NONE);
    }

    /**
     * Move the main canvas
     *
     * @param x X co-ordinate
     * @param y Y co-ordinate
     */

    public void moveCanvas(double x, double y) {
        canvas.setTranslateX(x);
        canvas.setTranslateY(y);
    }

    /**
     * Show panIcon on cursor
     */

    public void showPanIcon(double x, double y) {
        hideCursor();
        panIconPane.setVisible(true);
        setPanOnTop();

        panIconPane.setLayoutX(x);
        panIconPane.setLayoutY(y);
    }

    /**
     * Move panIcon
     *
     * @param x the x
     * @param y the y
     */

    public void movePanIcon(double x, double y) {
        if (!panIconPane.isVisible()) {
            showPanIcon(x, y);
            return;
        }

        panIconPane.setLayoutX(x);
        panIconPane.setLayoutY(y);
    }

    /**
     * Stop panIcon.
     */

    public void hidePanIcon() {
        setCursor(Cursor.DEFAULT);
        panIconPane.setVisible(false);
        setSceneOnTop();
    }

    /**
     * Sets hand on top.
     */

    public void setPanOnTop() {
        overlay.getStyleClass().add("dark-overlay");
        panIconPane.toFront();
        canvas.toBack();
        background.toBack();
    }

    /**
     * Sets scene on top.
     */

    public void setSceneOnTop() {
        overlay.getStyleClass().remove("dark-overlay");
        panIconPane.toBack();
        panIcon.toBack();
        canvas.toFront();
    }

    /**
     * Return the state of the app
     *
     * @return The state
     */

    public State getState() {
        return this.state;
    }

    /**
     * Is shift pressed boolean.
     *
     * @return the boolean
     */

    public boolean isShiftPressed() {
        return shiftPressed;
    }

    /**
     * Press shift.
     */

    public void pressShift() {
        this.shiftPressed = true;
    }

    /**
     * Release shift.
     */

    public void releaseShift() {
        this.shiftPressed = false;
    }

    /**
     * Is ctrl pressed boolean.
     *
     * @return the boolean
     */

    public boolean isCtrlPressed() {
        return ctrlPressed;
    }

    /**
     * Press ctrl.
     */

    public void pressCtrl() {
        this.ctrlPressed = true;
    }

    /**
     * Release ctrl.
     */

    public void releaseCtrl() {
        this.ctrlPressed = false;
    }

    /**
     * Check if left click is pressed
     *
     * @return Pressed status of left click
     */

    public boolean isLeftClickPressed() {
        return this.leftClickPressed;
    }

    /**
     * Press left click
     */

    public void pressLeftClick() {
        this.leftClickPressed = true;
    }

    /**
     * Release left click.
     */

    public void releaseLeftClick() {
        this.leftClickPressed = false;
    }

    /**
     * Gets pan icon bounds.
     *
     * @return the pan icon bounds
     */

    public Bounds getPanIconBounds() {
        return panIconPane.localToScene(panIconPane.getBoundsInLocal());
    }

    /**
     * Check if both ctrl and shift keys are pressed
     *
     * @return If multiple keys are pressed
     */

    public boolean areMultipleKeysPressed() {
        return this.shiftPressed && this.ctrlPressed;
    }

    /**
     * Get the main app scene
     *
     * @return The main scene
     */

    public static Scene getScene() {
        return scene;
    }
}
