package graph;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import elements.*;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

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

    /**
     * Source and target nodes
     */

    public GraphNode sourceNode;
    public GraphNode targetNode;

    public Pane dummyPane;
    public static Pane staticDummyPane;

    /**
     * Hidden node and edges for drawing edge
     */
    public GraphNode hiddenNode;
    public GraphEdge newEdge;
    public boolean newEdgeVisible;

    /**
     * Menu manager object
     */

    private MenuManager menuManager;

    /**
     * Selection manager object
     */

    public SelectionManager selectionManager;

    /**
     * Different states of app
     */

    public enum State {
        IDLE, DRAWING_EDGE, MOVING_NODE
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
        SHORTEST_PATH, TRAVELLING_SALESMAN, ANT_COLONY
    }

    /**
     * Current application interaction mode
     */

    private Mode currentMode = Mode.SELECT;
    private State currentState = State.IDLE;
    private Problem currentProblem = Problem.SHORTEST_PATH;

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
         * Set selections
         */
        setSelections();

        /*
         * Set transitions
         */
        setTransitions();

        /*
         * Set hidden drawing tools
         */
        setHiddenDrawingTools();

        /*
         * Set current mode to node mode
         */
        setCurrentMode(Mode.SELECT);

        this.canvas.getChildren().add(newNode);

//        FontAwesomeIconView font = new FontAwesomeIconView(FontAwesomeIcon.MAP_PIN);

//        GraphNode node1 = mainGraph.addNode("1");
//        node1.setTranslateX(1000);
//        node1.setTranslateY(400);
//
//        GraphNode node2 = mainGraph.addNode("2");
//        node2.setTranslateX(100);
//        node2.setTranslateY(100);
//
//        GraphNode node3 = mainGraph.addNode("3");
//        node3.setTranslateX(350);
//        node3.setTranslateY(500);
//
//        node1.addEventFilter(MouseEvent.MOUSE_PRESSED, nodeGestures.getOnMousePressedEventHandler());
//        node1.addEventFilter(MouseEvent.MOUSE_DRAGGED, nodeGestures.getOnMouseDraggedEventHandler());
//        node1.addEventFilter(MouseEvent.MOUSE_RELEASED, nodeGestures.getOnMouseReleasedEventHandler());
//
//        GraphEdge newEdge = mainGraph.addDirectionalEdge(1, node1, node2);
//        newEdge.initialize();
//        newEdge.addToCanvas(this.canvas);
//
//        GraphEdge newEdge2 = mainGraph.addDirectionalEdge(2, node2, node1);
//        newEdge2.initialize();
//        newEdge2.addToCanvas(this.canvas);
//
//        NonDirectionalEdge newEdge3 = mainGraph.addNonDirectionalEdge(3, node2, node3);
//        newEdge3.initialize();
//        newEdge3.getFirstEdge().initialize();
//        newEdge3.getSecondEdge().initialize();
//        newEdge3.addToCanvas(this.canvas);

//        final Rectangle selectionRect = new Rectangle(10, 10, Color.TRANSPARENT);
//        selectionRect.getStyleClass().add("selection-rect");

        EventHandler<MouseEvent> mouseDragHandler = new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
//                System.out.println(selectionRect.intersects(newEdge.getTranslateX(),
//                        newEdge.getTranslateY(),
//                        newEdge.getBoundsInLocal().getWidth(),
//                        newEdge.getBoundsInLocal().getHeight()));
//                System.out.println(selectionRect.intersects(node2.localToParent(node2.getBoundsInLocal())));
            }
        };

        // Add selection gesture
//        MouseControlUtil.addSelectionRectangleGesture(canvas, selectionRect, mouseDragHandler, null, null);

//        canvas.getChildren().addAll(node1, node2, node3);

        setMenuOnTop();
    }

    /**
     * Set scene elements and listeners
     *
     * @param scene Main application scene
     */

    private void setScene(Scene scene) {
        appCanvas = this.canvas;
        staticDummyPane = this.dummyPane;

        this.nodeGestures = new NodeGestures(this.canvas, this, mainGraph);
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
     * Set hidden nodes an edges for drawing
     */

    private void setHiddenDrawingTools() {
        this.hiddenNode = new GraphNode("");
        this.hiddenNode.setVisible(false);
        this.hiddenNode.toBack();

//        this.hiddenNode.setDisable(false);

        this.newEdge = new GraphEdge(0, this.hiddenNode, this.hiddenNode,
                Graph.EdgeOrientation.HORIZONTAL);
        this.newEdge.initialize();
        this.newEdge.hideEdge();
        this.newEdge.setDashed();
        this.newEdge.hoverEdge();

        this.newEdgeVisible = false;

        /*
         * Add new node and edge to canvas
         */
        this.canvas.getChildren().addAll(hiddenNode);
        newEdge.addToCanvas(canvas);
    }

    /**
     * Sets selections
     */

    public void setSelections() {
        this.selectionManager = new SelectionManager(menuManager, this);
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
     * Select item
     *
     * @param item the item
     */

    public void select(Node item) {
        selectionManager.select(item);
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
     * Sets current state
     */

    public void setCurrentState(State newState) {
        currentState = newState;
    }

    /**
     * Gets current state
     */

    public State getCurrentState() {
        return currentState;
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
     * Add new edge
     *
     * @param sourceNode the source node
     * @param targetNode the target node
     */
    public void addNewEdge(GraphNode sourceNode, GraphNode targetNode, boolean nonDirectional) {
        /*
         * Check if it is possible to draw edge
         */
        boolean possible = possibleToDrawEdge(sourceNode, targetNode, nonDirectional);

        if (!possible) {
            showEdgeErrorDialog();
            return;
        }

        /*
         * Prompt for edge weight
         */
        double weight = showNumberInputDialog();

        if (weight == 0) {
            return;
        }

        if (nonDirectional) {
            /*
             * Create a new graph edge
             */
            NonDirectionalEdge newEdge = mainGraph.addNonDirectionalEdge(weight, sourceNode, targetNode);

            /*
             * Initialize the new edge
             */
            newEdge.initialize();
            newEdge.getFirstEdge().initialize();
            newEdge.getSecondEdge().initialize();

            /*
             * Place the new edge on the canvas
             */

            newEdge.addToCanvas(this.canvas);
        } else {
            /*
             * Create a new graph edge
             */
            GraphEdge newEdge = mainGraph.addDirectionalEdge(weight, sourceNode, targetNode);

            /*
             * Initialize the new edge
             */
            newEdge.initialize();

            /*
             * Place the new edge on the canvas
             */

            newEdge.addToCanvas(this.canvas);
        }
    }

    /**
     * Show edge error dialog
     */

    public void showEdgeErrorDialog() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.getDialogPane().getStylesheets().add(
                getClass().getResource("main.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("error-dialog");
        alert.setTitle("");
        alert.setHeaderText("Cannot Draw Edge...");
        FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.WARNING);
        icon.setGlyphSize(40);
        icon.setFill(Color.valueOf("#b41c1c"));
        alert.setGraphic(icon);
        alert.setContentText("You cannot draw an edge from desired source node to the target! Please try again.");
        alert.showAndWait();
    }

    /**
     * Show edge error dialog
     */

    public double showNumberInputDialog() {
        TextInputDialog alert = new TextInputDialog("");
        alert.getDialogPane().getStylesheets().add(
                getClass().getResource("main.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("custom-dialog");
        alert.setTitle("");
        alert.setHeaderText("Edge Weight Input...");
        FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.WARNING);
        icon.setGlyphSize(40);
        icon.setFill(Color.valueOf("#29a2b0"));
        alert.setGraphic(icon);
        alert.setContentText("Enter weight:");
        Optional<String> input = alert.showAndWait();

        if (!input.isPresent()) {
            return 0;
        }

        while (!isNumber(input.get()) || Double.parseDouble(input.get()) <= 0) {
            showNumberErrorDialog();
            input = alert.showAndWait();

            if (!input.isPresent()) {
                return 0;
            }
        }

        return Double.parseDouble(input.get());
    }

    /**
     * Show number error dialog
     */

    public void showNumberErrorDialog() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.getDialogPane().getStylesheets().add(
                getClass().getResource("main.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("error-dialog");
        alert.setTitle("");
        alert.setHeaderText("Number Error...");
        FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.WARNING);
        icon.setGlyphSize(40);
        icon.setFill(Color.valueOf("#b41c1c"));
        alert.setGraphic(icon);
        alert.setContentText("You have entered an invalid value! Please try again.");
        alert.showAndWait();
    }

    /**
     * Show confirm delete dialog
     */

    public boolean showConfirmDeleteDialog() {
        boolean userAccepted = false;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getDialogPane().getStylesheets().add(
                getClass().getResource("main.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("custom-dialog");
        alert.setTitle("");
        alert.setHeaderText("Confirm remove...");
        FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.INFO_CIRCLE);
        icon.setGlyphSize(40);
        icon.setFill(Color.valueOf("#29a2b0"));
        alert.setGraphic(icon);

        alert.setContentText("Are you sure to remove all?");
        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("Cancel");
        alert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == yesButton) {
            userAccepted = true;
        }

        // return if user has accepted dialog
        return userAccepted;
    }

    /**
     * Find intersecting node graph node.
     *
     * @param event the event
     * @return the graph node
     */

    public GraphNode findNodeOnMouse(MouseEvent event) {
        Optional<GraphNode> node =
                mainGraph.getNodes().stream().filter(n -> {
                    double scale = canvas.getScale();
                    double anchorX = -(1200 * (1 - scale) / 2) / scale - canvas.getTranslateX() / scale;
                    double anchorY = -(800 * (1 - scale) / 2) / scale - canvas.getTranslateY() / scale;
                    double finalX = event.getSceneX() / scale - n.getTranslateX() + anchorX;
                    double finalY = event.getSceneY() / scale - n.getTranslateY() + anchorY;
                    return n.contains(finalX, finalY);
                }).findAny();

        return node.orElse(null);
    }

    /**
     * Check if we can draw edge from source to target
     *
     * @param source the source
     * @param target the target
     * @return the boolean
     */

    public boolean possibleToDrawEdge(GraphNode source, GraphNode target, boolean nonDirectional) {
        for (GraphNode node : source.getTwoWayNodes().keySet()) {
            if (node == target) {
                return false;
            }
        }

        for (GraphNode node : source.getOutgoingNodes().keySet()) {
            if (node == target) {
                return false;
            }
        }

        if (nonDirectional) {
            for (GraphNode node : source.getIncomingNodes().keySet()) {
                if (node == target) {
                    return false;
                }
            }
        }

        return true;
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
        newNodeOnTop();
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

    /**
     * New node on top.
     */

    public void newNodeOnTop() {
        newNode.toFront();
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
     * Move hidden node.
     *
     * @param mouseX the mouse x
     * @param mouseY the mouse y
     */
    public void moveHiddenNode(double mouseX, double mouseY) {
        double scale = canvas.getScale();

        double translateX = -(1200 * (1 - scale) / 2) / scale - canvas.getTranslateX() / scale - 35;
        double translateY = -(800 * (1 - scale) / 2) / scale - canvas.getTranslateY() / scale - 35;

        this.hiddenNode.setTranslateX(mouseX / scale + translateX);
        this.hiddenNode.setTranslateY(mouseY / scale + translateY);
    }

    /**
     * Show new edge
     */

    public void showNewEdge(boolean nonDirectional) {
        if (!newEdgeVisible) {
            this.newEdge.showEdge(nonDirectional);
            this.newEdgeVisible = true;
        }
    }

    /**
     * Hide new edge
     */

    public void hideNewEdge() {
        if (newEdgeVisible) {
            this.newEdge.hideEdge();
            this.newEdgeVisible = false;
        }
    }

    /**
     * Move new edge
     */

    public void moveNewEdge() {
        this.newEdge.update();
    }

    /**
     * Move node.
     *
     * @param node the node
     */

    public void moveNode(GraphNode node, MouseEvent event) {
        /*
         * Calculate the values
         */
        double scale = canvas.getScale();
        double finalX = -(1200 * (1 - scale) / 2) / scale - canvas.getTranslateX() / scale - 35;
        double finalY = -(800 * (1 - scale) / 2) / scale - canvas.getTranslateY() / scale - 35;

        /*
         * Set translate value for node
         */
        node.setTranslateX(event.getSceneX() / scale + finalX);
        node.setTranslateY(event.getSceneY() / scale + finalY);

        /*
         * Update all of the attached edges
         */

        for (GraphEdge edge : node.getOutgoingNodes().values()) {
            edge.update();
        }
        for (GraphEdge edge : node.getIncomingNodes().values()) {
            edge.update();
        }
        for (NonDirectionalEdge edge : node.getTwoWayNodes().values()) {
            edge.update();
        }
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
     * Sets problem
     */

    public void changeProblem(Problem problem) {
        if (this.currentProblem == problem) {
            return;
        }

        switch (problem) {
            case SHORTEST_PATH:
                shortestPathProblem();
                break;
            case TRAVELLING_SALESMAN:
                travellingSalesmanProblem();
                break;
            case ANT_COLONY:
            default:
                antColonyProblem();
                break;
        }
    }

    /**
     * Shortest path problem
     */
    public void shortestPathProblem() {

    }

    /**
     * Travelling salesman problem
     */
    public void travellingSalesmanProblem() {

    }

    /**
     * Ant colony problem
     */
    public void antColonyProblem() {

    }

    /**
     * Gets current problem.
     *
     * @return the current problem
     */

    public Problem getCurrentProblem() {
        return this.currentProblem;
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
     * Sets source node
     */

    public void setSourceNode(GraphNode sourceNode) {
        if (this.targetNode == sourceNode) {
            resetTargetNode();
        }

        if (this.sourceNode == sourceNode) {
            resetSourceNode();
        } else {
            this.sourceNode = sourceNode;
            sourceNode.setAsSource();
        }
    }

    /**
     * Reset source node
     */

    public void resetSourceNode() {
        if (this.sourceNode != null) {
            this.sourceNode.setAsNormal();
        }

        this.sourceNode = null;
    }

    /**
     * Gets source node
     *
     * @return the source node
     */

    public GraphNode getSourceNode() {
        return this.sourceNode;
    }

    /**
     * Sets target node
     */

    public void setTargetNode(GraphNode targetNode) {
        if (this.sourceNode == targetNode) {
            resetSourceNode();
        }

        if (this.targetNode == targetNode) {
            resetTargetNode();
        } else {
            this.targetNode = targetNode;
            targetNode.setAsTarget();
        }
    }

    /**
     * Reset target node
     */

    public void resetTargetNode() {
        if (this.targetNode != null) {
            this.targetNode.setAsNormal();
        }

        this.targetNode = null;
    }

    /**
     * Gets target node
     *
     * @return the target node
     */

    public GraphNode getTargetNode() {
        return this.targetNode;
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
     * Is number valid
     *
     * @param string the string
     * @return the boolean
     */

    public boolean isNumber(String string) {
        try {
            Double.parseDouble(string);
            return true;
        } catch (Exception e) {
            return false;
        }
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
