package graph;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import elements.*;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static graph.Main.app;

/**
 * The type App.
 */
public class App {
    public FontAwesomeIconView panIcon;
    public StackPane panIconPane;
    public PannableGridPane background;
    public Pane overlay;
    public Button traverseButton;

    /**
     * Menus grid panes
     */

    public GridPane mainMenu;
    public GridPane problemsMenu;
    public GridPane toolsMenu;
    public GridPane modeMenu;

    /**
     * Problems menu buttons
     */

    public JFXButton shortestPathButton;
    public JFXButton travellingSalesmanButton;

    /**
     * Tools menu buttons
     */

    public JFXButton setSourceNodeButton;
    public JFXButton setTargetNodeButton;
    public JFXButton setWeightButton;
    public JFXButton setLabelButton;
    public JFXButton changeDirectionButton;
    public JFXButton removeButton;

    /**
     * Mode menu buttons
     */

    public JFXButton nodeButton;
    public JFXButton directionalEdgeButton;
    public JFXButton nonDirectionalEdgeButton;
    public JFXButton selectButton;

    /**
     * New temp node
     */

    public GraphNode newNode;
    public Pane tempNodePane;
    private boolean newNodeVisible = false;

    public Pane dummyPane;
    public static Pane staticDummyPane;

    /**
     * Menu manager object
     */

    private MenuManager menuManager;

    public enum State {
        IDLE, PAN, MOVE
    }

    /**
     * Different modes of interaction within the application
     * <p>
     * 1. New node mode: User can add new nodes to the main canvas.
     * 2. New directional edge mode: User can add new directional edge between two nodes.
     * 3. New non-directional edge mode: User can add new non-directional edge between two nodes. Non-directional
     * edges are the same as two directional edges with opposite directions.
     * 4. Select mode: User can select nodes and edges to delete them.
     */

    public enum Mode {
        NODE, DIRECTIONAL_EDGE, NON_DIRECTIONAL_EDGE, SELECT
    }

    /**
     * Main application problem mode.
     * <p>
     * 1. Shortest path: User must choose one source node and one target node, so the application finds the shortest
     * path between the two nodes specified using Dijkstra algorithm.
     * 2. Travelling salesman: User must choose one source node, so the application finds the best way that one can
     * traverse through them all without visiting any node more than once. This method uses dynamic programming
     * algorithm.
     */

    public enum Problem {
        SHORTEST_PATH, TRAVELLING_SALESMAN
    }

    /**
     * Current application interaction mode
     */

    private Mode currentMode;

    /**
     * Hover transitions
     */

    private static FadeTransition showOnHover;
    private static FadeTransition hideOnHover;

    private boolean shiftPressed = false;
    private boolean ctrlPressed = false;
    private boolean leftClickPressed = false;

    private State state;
    public GridPane window;
    public PannableCanvas canvas;
    public static PannableCanvas appCanvas;
    private NodeGestures nodeGestures;
    private SceneGestures sceneGestures;
    private static Scene scene;
    private Stage primaryStage;

    private static Graph mainGraph = new Graph();
    private boolean mouseOnMenu = false;
    private boolean mouseOnNode = false;
    private boolean menuVisible = true;

    public void set(Scene scene, Stage primaryStage) {
        this.newNode = new GraphNode("•");
        newNode.setTranslateX(0);
        newNode.setTranslateY(0);
        newNode.setPrefHeight(70);
        newNode.setPrefWidth(70);
        newNode.getStyleClass().add("node-transparent");
        newNode.setVisible(false);

//        EventHandler<MouseEvent> handler = MouseEvent::consume;
//        newNode.addEventFilter(MouseEvent.ANY, handler);

        /*
         * Set scene elements and listeners
         */
        setScene(scene);

        /*
         * Set primary stage
         */
        setPrimaryStage(primaryStage);

        /*
         * Set menus
         */
        setMenus();

        /*
         * Set transitions
         */
        setTransitions();

        /*
         * Set current mode to node mode
         */
        setCurrentMode(Mode.SELECT);

//        GraphNode graphNode = new GraphNode("1");
//        graphNode.setTranslateX(50);
//        graphNode.setTranslateY(50);
//        graphNode.setPrefHeight(70);
//        graphNode.setPrefWidth(70);
////        graphNode.getStyleClass().add("node");
//        graphNode.getStyleClass().add("node-transparent");
//        graphNode.addEventFilter(MouseEvent.MOUSE_PRESSED, nodeGestures.getOnMousePressedEventHandler());
//        graphNode.addEventFilter(MouseEvent.MOUSE_DRAGGED, nodeGestures.getOnMouseDraggedEventHandler());

        this.canvas.getChildren().add(newNode);

        FontAwesomeIconView font = new FontAwesomeIconView(FontAwesomeIcon.MAP_PIN);

        GraphNode node1 = mainGraph.addNode("1");
        node1.setTranslateX(1000);
        node1.setTranslateY(400);

        GraphNode node2 = mainGraph.addNode("2");
        node2.setTranslateX(100);
        node2.setTranslateY(100);

        GraphNode node3 = mainGraph.addNode("3");
        node3.setTranslateX(150);
        node3.setTranslateY(100);

//        final double[] max = new double[2];
//        max[0] = (node1.getTranslateY() + 70 + node2.getTranslateY()) / 2;
//        max[1] = (node1.getTranslateX() + 70 + node2.getTranslateX()) / 2;

//        CubicCurve curve1 = new CubicCurve(35 + node1.getTranslateX(), node1.getTranslateY(),
//                35 + node1.getTranslateX(), max[0] / 2,
//                35 + node2.getTranslateX(), max[0] / 2,
//                35 + node2.getTranslateX(), node2.getTranslateY() + 70);
//        curve1.setStrokeWidth(4);
//        curve1.setFill(null);
//        curve1.setStroke(Color.ORANGE);
//        curve1.getStyleClass().add("edge");

//        CubicCurve curve2 = new CubicCurve(node1.getTranslateX(), node1.getTranslateY() + 35,
//                max[1] / 2, 35 + node1.getTranslateY(),
//                max[1] / 2, 35 + node2.getTranslateY(),
//                node2.getTranslateX() + 70, node2.getTranslateY() + 35);
//        curve2.setStrokeWidth(4);
//        curve2.setFill(null);
//        curve2.setStroke(Color.GREEN);
//        curve2.getStyleClass().add("edge");

//        final Path[] arrowTwo = {new Path()};
//        final Path[] arrowOne = {new Path()};

        GraphEdge newEdge = mainGraph.addDirectionalEdge(12, node1, node2);
        newEdge.initialize();
        newEdge.addToCanvas(this.canvas);

        GraphEdge newEdge2 = mainGraph.addDirectionalEdge(12, node2, node1);
        newEdge2.initialize();
        newEdge2.addToCanvas(this.canvas);

//        Label weight1 = new Label("14");
//        weight1.getStyleClass().add("edge-weight-2");
//
//        Label weight2 = new Label("25");
//        weight2.getStyleClass().add("edge-weight");

        node1.addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {
            double scale = canvas.getScale();

            double posX = -(1200 * (1 - scale) / 2) / scale - canvas.getTranslateX() / scale - 35 + event.getSceneX() / scale;
            double posY = -(800 * (1 - scale) / 2) / scale - canvas.getTranslateY() / scale - 35 + event.getSceneY() / scale;

            node1.setTranslateX(posX);
            node1.setTranslateY(posY);

            newEdge.update();
            newEdge2.update();

//            GraphNode top, bottom;
//
//            max[0] = node2.getTranslateY() + 70 + node1.getTranslateY();
//
//            if (node1.getTranslateY() <= node2.getTranslateY()) {
//                top = node1;
//                bottom = node2;
//
//                curve1.setStartY(top.getTranslateY() + 70);
//                curve1.setEndY(bottom.getTranslateY() - 8);
//                curve1.setStartX(35 + top.getTranslateX());
//                curve1.setEndX(35 + bottom.getTranslateX());
//                curve1.setControlX1(35 + top.getTranslateX());
//                curve1.setControlX2(35 + bottom.getTranslateX());
//                curve1.setControlY1(max[0] / 2 + 150);
//                curve1.setControlY2(max[0] / 2 - 150);
//
//                weight1.setTranslateX((curve1.getStartX() + curve1.getEndX()) / 2);
//                weight1.setTranslateY((curve1.getStartY() + curve1.getEndY()) / 2);
////                weight1.setRotate();
//
//            } else {
//                top = node2;
//                bottom = node1;
//
//                curve1.setStartY(bottom.getTranslateY());
//                curve1.setEndY(top.getTranslateY() + 70 + 8);
//                curve1.setStartX(35 + bottom.getTranslateX());
//                curve1.setEndX(35 + top.getTranslateX());
//                curve1.setControlX1(35 + bottom.getTranslateX());
//                curve1.setControlX2(35 + top.getTranslateX());
//                curve1.setControlY1(max[0] / 2 - 150);
//                curve1.setControlY2(max[0] / 2 + 150);
//
//                Bounds bounds = weight1.localToScene(weight1.getBoundsInLocal());
//
//                weight1.setTranslateX((curve1.getStartX() + curve1.getEndX()) / 2 - bounds.getWidth() / 2 / scale);
//                weight1.setTranslateY((curve1.getStartY() + curve1.getEndY()) / 2 - bounds.getHeight() / 2 / scale);
//
////                double incline = (-curve1.getStartY() + curve1.getEndY()) / (-curve1.getStartX() + curve1.getEndX());
////                System.out.println(Math.toDegrees(Math.atan(incline)));
////                weight1.setRotate(Math.toDegrees(Math.atan(incline)) + 45);
//
//            }
//
//            GraphNode right, left;
//            max[1] = node2.getTranslateX() + 70 + node1.getTranslateX();
//
//            if (node1.getTranslateX() <= node2.getTranslateX()) {
//                left = node1;
//                right = node2;
//
//                curve2.setStartX(right.getTranslateX());
//                curve2.setEndX(left.getTranslateX() + 70 + 8);
//                curve2.setControlX1(max[1] / 2 - 150);
//                curve2.setControlX2(max[1] / 2 + 150);
//                curve2.setStartY(35 + right.getTranslateY());
//                curve2.setControlY1(35 + right.getTranslateY());
//                curve2.setControlY2(35 + left.getTranslateY());
//                curve2.setEndY(35 + left.getTranslateY());
//            } else {
//                left = node2;
//                right = node1;
//
//                curve2.setStartX(left.getTranslateX() + 70);
//                curve2.setEndX(right.getTranslateX() - 8);
//                curve2.setControlX1(max[1] / 2 + 150);
//                curve2.setControlX2(max[1] / 2 - 150);
//                curve2.setStartY(35 + left.getTranslateY());
//                curve2.setControlY1(35 + left.getTranslateY());
//                curve2.setControlY2(35 + right.getTranslateY());
//                curve2.setEndY(35 + right.getTranslateY());
//
//                Bounds bounds = weight2.localToScene(weight2.getBoundsInLocal());
//
//                weight2.setTranslateX((curve2.getStartX() + curve2.getEndX()) / 2 - bounds.getWidth() / 2 / scale);
//                weight2.setTranslateY((curve2.getStartY() + curve2.getEndY()) / 2 - bounds.getHeight() / 2 / scale);
//
//            }

//            canvas.getChildren().removeAll(arrowOne[0], arrowTwo[0]);
//
//            double size = 250;
//            double scale2 = size / 4d;

//            Point2D ori = eval(curve1, 1);
//            Point2D tan = evalDt(curve1, 1).normalize().multiply(scale2);
//            arrowOne[0] = new Path();
//            arrowOne[0].getElements().add(new MoveTo(ori.getX() - 0.2 * tan.getX() - 0.2 * tan.getY(),
//                    ori.getY() - 0.2 * tan.getY() + 0.2 * tan.getX()));
//            arrowOne[0].getElements().add(new LineTo(ori.getX(), ori.getY()));
//            arrowOne[0].getElements().add(new LineTo(ori.getX() - 0.2 * tan.getX() + 0.2 * tan.getY(),
//                    ori.getY() - 0.2 * tan.getY() - 0.2 * tan.getX()));
//            arrowOne[0].setStroke(Color.ORANGE);
//            arrowOne[0].setStrokeWidth(4);
//
//            ori = eval(curve2, 1);
//            tan = evalDt(curve2, 1).normalize().multiply(scale2);
//            arrowTwo[0] = new Path();
//            arrowTwo[0].getElements().add(new MoveTo(ori.getX() - 0.2 * tan.getX() - 0.2 * tan.getY(),
//                    ori.getY() - 0.2 * tan.getY() + 0.2 * tan.getX()));
//            arrowTwo[0].getElements().add(new LineTo(ori.getX(), ori.getY()));
//            arrowTwo[0].getElements().add(new LineTo(ori.getX() - 0.2 * tan.getX() + 0.2 * tan.getY(),
//                    ori.getY() - 0.2 * tan.getY() - 0.2 * tan.getX()));
//            arrowTwo[0].setStroke(Color.GREEN);
//            arrowTwo[0].setStrokeWidth(4);

//            canvas.getChildren().addAll(arrowOne[0], arrowTwo[0]);
        });

//        /*
//         * Add mouse enter event listener
//         */
//        curve1.addEventFilter(MouseEvent.MOUSE_ENTERED, event2 -> {
//            weight1.toFront();
//            weight1.setBorder(new Border(new BorderStroke(Color.ORANGE, BorderStrokeStyle.SOLID,
//                    new CornerRadii(50, true), new BorderWidths(3))));
//            showOnHover.setNode(weight1);
//            showOnHover.setToValue(1);
//            hideOnHover.stop();
//            showOnHover.play();
//        });
//
//        /*
//         * Add mouse leave event listener
//         */
//        curve1.addEventFilter(MouseEvent.MOUSE_EXITED, event3 -> {
//            hideOnHover.setNode(weight1);
//            hideOnHover.setToValue(.3);
//            showOnHover.stop();
//            hideOnHover.play();
//        });
//
//        /*
//         * Add mouse enter event listener
//         */
//        curve2.addEventFilter(MouseEvent.MOUSE_ENTERED, event2 -> {
//            weight2.toFront();
//            showOnHover.setNode(weight2);
//            showOnHover.setToValue(1);
//            hideOnHover.stop();
//            showOnHover.play();
//        });
//
//        /*
//         * Add mouse leave event listener
//         */
//        curve2.addEventFilter(MouseEvent.MOUSE_EXITED, event3 -> {
//            hideOnHover.setNode(weight2);
//            hideOnHover.setToValue(.3);
//            showOnHover.stop();
//            hideOnHover.play();
//        });


//        double size = Math.max(curve1.getBoundsInLocal().getWidth(),
//                curve1.getBoundsInLocal().getHeight());
//        double scale = size / 4d;


//        double size = 250;
//        double scale = size / 4d;
//
//        Point2D ori = eval(curve1, 0);
//        Point2D tan = evalDt(curve1, 0).normalize().multiply(scale);
//        Path arrowIni = new Path();
//        arrowIni.getElements().add(new MoveTo(ori.getX() + 0.2 * tan.getX() - 0.2 * tan.getY(),
//                ori.getY() + 0.2 * tan.getY() + 0.2 * tan.getX()));
//        arrowIni.getElements().add(new LineTo(ori.getX(), ori.getY()));
//        arrowIni.getElements().add(new LineTo(ori.getX() + 0.2 * tan.getX() + 0.2 * tan.getY(),
//                ori.getY() + 0.2 * tan.getY() - 0.2 * tan.getX()));
//        arrowIni.setStroke(Color.valueOf("#66bde1"));
//        arrowIni.setStrokeWidth(4);
//
//        ori = eval(curve1, 1);
//        tan = evalDt(curve1, 1).normalize().multiply(scale);
//        Path arrowEnd = new Path();
//        arrowEnd.getElements().add(new MoveTo(ori.getX() - 0.2 * tan.getX() - 0.2 * tan.getY(),
//                ori.getY() - 0.2 * tan.getY() + 0.2 * tan.getX()));
//        arrowEnd.getElements().add(new LineTo(ori.getX(), ori.getY()));
//        arrowEnd.getElements().add(new LineTo(ori.getX() - 0.2 * tan.getX() + 0.2 * tan.getY(),
//                ori.getY() - 0.2 * tan.getY() - 0.2 * tan.getX()));
//        arrowEnd.setStroke(Color.valueOf("#66bde1"));
//        arrowEnd.setStrokeWidth(4);

        canvas.getChildren().addAll(node1, node2, node3);

//        root.setTranslateX(400);
//        root.setTranslateY(400);
//        canvas.getChildren().add(root);

        setMenuOnTop();
    }

//    private void set

    /**
     * Set scene elements and listeners
     *
     * @param scene Main application scene
     */

    private void setScene(Scene scene) {
        appCanvas = this.canvas;
        staticDummyPane = this.dummyPane;

        this.nodeGestures = new NodeGestures(this.canvas, this);
        App.scene = scene;
        this.sceneGestures = new SceneGestures(this.canvas, this, this.background);
        App.scene.addEventFilter(MouseEvent.MOUSE_PRESSED, sceneGestures.getOnMousePressedEventHandler());
        App.scene.addEventFilter(MouseEvent.MOUSE_DRAGGED, sceneGestures.getOnMouseDraggedEventHandler());
        window.addEventFilter(MouseEvent.MOUSE_MOVED, sceneGestures.getOnMouseMovedEventHandler());
        App.scene.addEventFilter(MouseDragEvent.MOUSE_RELEASED, sceneGestures.getOnMouseReleasedEventHandler());
        App.scene.addEventFilter(KeyEvent.KEY_PRESSED, sceneGestures.getOnKeyPressedEventHandler());
        App.scene.addEventFilter(KeyEvent.KEY_RELEASED, sceneGestures.getOnKeyReleasedEventHandler());

        App.scene.addEventFilter(ScrollEvent.ANY, sceneGestures.getOnScrollEventHandler());
        background.toBack();
        panIconPane.toBack();
        canvas.toFront();

//        TranslateTransition hideMenu = new TranslateTransition(Duration.millis(500), mainMenu);
//        hideMenu.setToX(-220);
//        hideMenu.setOnFinished(event -> {
//            hideMenu.setToX(0);
//        });
//
        PauseTransition wait = new PauseTransition(Duration.millis(3000));
        wait.setOnFinished(event -> {
//            hideMenu.play();
//            menuManager.hideMenu();
        });
        wait.play();
//
//        PauseTransition wait2 = new PauseTransition(Duration.millis(6000));
//        wait2.setOnFinished(event -> {
//            hideMenu.play();
//        });
//        wait2.play();

//        menuManager.hideMenu();

    }

    private void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;

        this.primaryStage.iconifiedProperty().addListener((ov, t, t1) -> {
            leaveMenu();
            releaseLeftClick();
            releaseCtrl();
            releaseShift();
        });
    }

    /**
     * Set mouse hover show and hide transitions
     */

    private void setTransitions() {
        /*
         * Show node transition
         */
        showOnHover = new FadeTransition(Duration.millis(300));
        showOnHover.setToValue(1);

        /*
         * Hide node transition
         */
        hideOnHover = new FadeTransition(Duration.millis(300));
        hideOnHover.setToValue(.3);
    }

    /**
     * Hover graph edge
     */

    public static void hoverEdge(GraphEdge edge) {
        /*
         * Get the weight label node
         */
        Label weightLabel = edge.getWeightLabel();

        /*
         * Set the transition node, stop the hide transition and play the show one
         */
        showOnHover.setNode(weightLabel);
        hideOnHover.stop();
        showOnHover.play();
    }

    /**
     * Leave edge
     *
     * @param edge the edge
     */

    public static void leaveEdge(GraphEdge edge) {
        /*
         * Get the weight label node
         */
        Label weightLabel = edge.getWeightLabel();

        /*
         * Set the transition node, stop the show transition and play the hide one
         */
        hideOnHover.setNode(weightLabel);
        showOnHover.stop();
        hideOnHover.play();
    }

    /**
     * Set menus in the menu manager
     */

    private void setMenus() {
        /*
         * Define the hash maps
         */
        HashMap<Integer, GridPane> menus = new HashMap<>();
        HashMap<Integer, JFXButton> buttons = new HashMap<>();

        /*
         * Put menus in hash map
         */
        menus.put(0, mainMenu);
        menus.put(1, problemsMenu);
        menus.put(2, toolsMenu);
        menus.put(3, modeMenu);

        /*
         * Put menu buttons in hash map
         */
        buttons.put(0, shortestPathButton);
        buttons.put(1, travellingSalesmanButton);
        buttons.put(2, setSourceNodeButton);
        buttons.put(3, setTargetNodeButton);
        buttons.put(4, setWeightButton);
        buttons.put(5, setLabelButton);
        buttons.put(6, changeDirectionButton);
        buttons.put(7, removeButton);
        buttons.put(8, nodeButton);
        buttons.put(9, directionalEdgeButton);
        buttons.put(10, nonDirectionalEdgeButton);
        buttons.put(11, selectButton);

        /*
         * Instantiate menu manager object
         */
        menuManager = new MenuManager(app, buttons, menus);
    }

    /**
     * Add mouse event filters to node
     */

    private void addEventFilterToNode(GraphNode node) {
        node.addEventFilter(MouseEvent.MOUSE_PRESSED, nodeGestures.getOnMousePressedEventHandler());
        node.addEventFilter(MouseEvent.MOUSE_DRAGGED, nodeGestures.getOnMouseDraggedEventHandler());
        node.addEventFilter(MouseEvent.MOUSE_RELEASED, nodeGestures.getOnMouseReleasedEventHandler());
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
        overlay.setDisable(false);

        panIconPane.toFront();
        canvas.toBack();
        background.toBack();
    }

    /**
     * Sets scene on top.
     */

    public void setSceneOnTop() {
        overlay.getStyleClass().remove("dark-overlay");
//        overlay.setDisable(true);
//        canvas.toBack();
        panIconPane.toBack();
        panIcon.toBack();
        canvas.toFront();
        setMenuOnTop();
    }

    /**
     * Change application interaction mode
     */

    public void changeMode(Mode newMode) {
        /*
         * Return if the desired new mode is already selected
         */
        if (getCurrentMode() == newMode) {
            return;
        }

        /*
         * Change the menu layout according to the
         * new application interaction mode
         */
        menuManager.changeMode(newMode);

        switch (newMode) {
            case NODE:
                /*
                 * Node Mode
                 */
                nodeMode();

                break;
            case DIRECTIONAL_EDGE:
                /*
                 * Directional Edge Mode
                 */
                directionalEdgeMode();

                break;
            case NON_DIRECTIONAL_EDGE:
                /*
                 * Non-directional Edge Mode
                 */
                nonDirectionalEdgeMode();

                break;
            default:
                /*
                 * Select Mode
                 */
                selectMode();

                break;
        }
    }

    /**
     * Set the application interaction mode
     *
     * @param newMode New mode to set
     */

    private void setCurrentMode(Mode newMode) {
        this.currentMode = newMode;
    }

    /**
     * Switch to node mode
     */

    public void nodeMode() {
        showNewNode();

        /*
         * Set new current mode
         */
        setCurrentMode(Mode.NODE);
    }

    /**
     * Switch to select mode
     */

    public void selectMode() {
        hideNewNode();

        /*
         * Set new current mode
         */
        setCurrentMode(Mode.SELECT);
    }

    /**
     * Switch to non-directional edge mode
     */

    public void nonDirectionalEdgeMode() {
        hideNewNode();

        /*
         * Set new current mode
         */
        setCurrentMode(Mode.NON_DIRECTIONAL_EDGE);
    }

    /**
     * Switch to directional edge mode
     */

    public void directionalEdgeMode() {
        hideNewNode();

        /*
         * Set new current mode
         */
        setCurrentMode(Mode.DIRECTIONAL_EDGE);
    }

    /**
     * Get current application interaction mode
     *
     * @return the current mode
     */

    public Mode getCurrentMode() {
        return this.currentMode;
    }

    /**
     * Add new node
     *
     * @param event The mouse event
     */

    public void addNewNode(MouseEvent event) {
        /*
         * Create a new graph node
         */
        GraphNode newNode = mainGraph.addNode(mainGraph.generateID());

        /*
         * Calculate new node co-ordinates
         */
        double scale = canvas.getScale();
        double anchorX = -(1200 * (1 - scale) / 2) / scale - canvas.getTranslateX() / scale - 35;
        double anchorY = -(800 * (1 - scale) / 2) / scale - canvas.getTranslateY() / scale - 35;
        double finalX = event.getSceneX() / scale + anchorX;
        double finalY = event.getSceneY() / scale + anchorY;

        /*
         * Call the function with the new coordinates
         */

        placeNewNode(newNode, finalX, finalY);

        /*
         * Add event filters to new node
         */
        addEventFilterToNode(newNode);
    }

    /**
     * Add new node to screen
     */

    private void placeNewNode(GraphNode newNode, double x, double y) {
        /*
         * Add the new node to the canvas
         */
        canvas.getChildren().add(newNode);

        /*
         * Set the co-ordinates as translate values
         */
        newNode.setTranslateX(x);
        newNode.setTranslateY(y);
    }

    /**
     * Show new temp node on screen
     */

    private void showNewNode() {
        /*
         * Return if new temp node is already visible
         */
        if (newNodeVisible) {
            return;
        }

        /*
         * Show new node
         */
        moveNewNode(sceneGestures.getX(), sceneGestures.getY());
        newNode.setVisible(true);
        setNewNodeVisibility(true);
    }

    /**
     * Hide new temp node on the canvas
     */

    private void hideNewNode() {
        /*
         * Return if new temp node is already hidden
         */
        if (!newNodeVisible) {
            return;
        }

        /*
         * Hide new node
         */
        newNode.setVisible(false);
        setNewNodeVisibility(false);
    }

    private void addNewNodeListener() {

    }

    private void removeNewNodeListener() {

    }

    /**
     * Mode new temp node on the canvas
     *
     * @param mouseX X co-ordinate of the new node
     * @param mouseY Y co-ordinate of the new node
     */

    public void moveNewNode(double mouseX, double mouseY) {
        double scale = canvas.getScale();

        double translateX = -(1200 * (1 - scale) / 2) / scale - canvas.getTranslateX() / scale - 35;
        double translateY = -(800 * (1 - scale) / 2) / scale - canvas.getTranslateY() / scale - 35;

        this.newNode.setTranslateX(mouseX / scale + translateX);
        this.newNode.setTranslateY(mouseY / scale + translateY);
    }

    /**
     * Sets new node visibility
     *
     * @param visibility the visibility status
     */

    public void setNewNodeVisibility(boolean visibility) {
        this.newNodeVisible = visibility;
    }

    /**
     * Is new node visible boolean
     *
     * @return the boolean
     */

    public boolean isNewNodeVisible() {
        return this.newNodeVisible;
    }

    /**
     * Deselect mode button
     *
     * @param button the button
     */

    public void deselectModeButton(JFXButton button) {
        button.getStyleClass().remove("menu-button-pink");
        button.getStyleClass().addAll("menu-button-transparent", "menu-button-hover-pink-outline");
    }

    /**
     * Deselect all mode buttons.
     *
     * @param buttons the buttons
     */

    public void deselectAllModeButtons(List<JFXButton> buttons) {
        for (JFXButton button : buttons) {
            button.getStyleClass().remove("menu-button-pink");
            button.getStyleClass().addAll("menu-button-transparent", "menu-button-hover-pink-outline");
        }
    }

    /**
     * Select mode button
     *
     * @param button the button
     */

    public void selectModeButton(JFXButton button) {
        button.getStyleClass().removeAll("menu-button-transparent", "menu-button-hover-pink-outline");
        button.getStyleClass().add("menu-button-pink");
    }

    /**
     * Sets necessary objects on top
     */

    public void setOnTop() {
        ArrayList<Node> onTop = new ArrayList<>();
        setMenuOnTop();

        switch (currentMode) {
            case NODE:
//                onTop.addAll(mainGraph.getNodes());

                break;
            case NON_DIRECTIONAL_EDGE:
//                onTop.addAll(mainGraph.getNodes());

                break;
            case DIRECTIONAL_EDGE:
//                onTop.addAll(mainGraph.getNodes());

                break;
            default:
//                onTop.addAll(mainGraph.getNodes());

                break;
        }

        menuManager.finalizeOnTop(onTop);
    }

    /**
     * Sets menu on top
     */

    public void setMenuOnTop() {
        menuManager.menuOnTop();
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
     * Hover menu.
     */

    public void hoverMenu() {
        this.mouseOnMenu = true;
    }

    /**
     * Leave menu.
     */

    public void leaveMenu() {
        this.mouseOnMenu = false;
    }

    /**
     * Show menu
     */

    public void showMenu() {
        if (menuVisible) {
            return;
        }

        menuManager.showMenu();
        menuVisible = true;
    }

    /**
     * Hide menu
     */

    public void hideMenu() {
        if (!menuVisible) {
            return;
        }

        menuManager.hideMenu();
        menuVisible = false;
    }

    /**
     * Hover nodes
     */

    public void hoverNode() {
        this.mouseOnNode = true;
    }

    /**
     * Leave node
     */

    public void leaveNode() {
        this.mouseOnNode = false;
    }

    /**
     * Is node hovered boolean
     *
     * @return the boolean
     */

    public boolean isNodeHovered() {
        return this.mouseOnNode;
    }


    /**
     * Has user hovered menu
     *
     * @return the boolean
     */

    public boolean isMenuHovered() {
//        return this.mouseOnMenu;
        return sceneGestures.getX() <= 220;
    }

    /**
     * Add to canvas
     *
     * @param node the node
     */

    public static void addToCanvas(Node node) {
        appCanvas.getChildren().add(node);
    }

    /**
     * Remove from canvas
     *
     * @param node the node
     */

    public static void removeFromCanvas(Node node) {
        appCanvas.getChildren().remove(node);
    }

    /**
     * Gets canvas scale
     *
     * @return the scale
     */

    public static double getScale() {
        return appCanvas.getScale();
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
     * Send nodes to front
     */

    public static void sendNodesToFront() {
        for (GraphNode node : mainGraph.getNodes()) {
            node.toFront();
        }
    }

    /**
     * Add to dummy pane
     */

    public static void addToDummyPane(Node node) {
        staticDummyPane.getChildren().add(node);
    }

    /**
     * Remove from dummy pane
     */

    public static void removeFromDummyPane(Node node) {
        staticDummyPane.getChildren().remove(node);
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
