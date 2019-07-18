package graph;

import algorithms.Dijkstra.DijkstraAlgorithm;
import algorithms.Dijkstra.PathData;
import algorithms.TravellingSalesMan.AntColony.AntColonyAlgorithm;
import algorithms.TravellingSalesMan.DynamicProgramming.TravellingSalesManAlgorithm;
import algorithms.TravellingSalesMan.TravellingSalesManData;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import elements.*;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

import java.util.*;

import static graph.Main.app;

/**
 * The type App.
 */
public class App {
    public FontAwesomeIconView panIcon;
    public StackPane panIconPane;
    public PannableGridPane background;
    public Pane overlay;

    /**
     * Menus grid panes
     */

    public GridPane mainMenu;
    public GridPane problemsMenu;
    public GridPane toolsMenu;
    public GridPane modeMenu;
    public GridPane antColonyMenu;
    public HBox expandingProblemsMenu;
    public boolean expandingProblemsMenuVisible = false;
    private boolean expandingProblemsMenuHovered = false;

    /**
     * Runtime menu elements
     */

    public GridPane runtimeMenu;
    public JFXButton playButton;
    public JFXButton stopButton;
    public JFXButton speedUpButton;
    public JFXButton speedDownButton;
    public Label timerValue;
    public Label timeTitle;
    public Label distanceValue;
    public Label distanceTitle;
    private boolean runtimeMenuVisible = false;

    /**
     * Ant colony menu elements
     */

    public JFXButton antsCountButton;
    public JFXButton alphaButton;
    public JFXButton betaButton;
    public JFXButton thresholdButton;
    public JFXButton vaporButton;
    public JFXButton randomFactorButton;
    public Label antsCountValue;
    public Label alphaValue;
    public Label betaValue;
    public Label thresholdValue;
    public Label vaporValue;
    public Label randomFactorValue;
    private boolean antColonyMenuVisible = false;

    /**
     * Ant colony values
     */

    private int antsCount = 50;
    private double alpha = 1.0;
    private double beta = 1.0;
    private int threshold = 40;
    private double vapor = .5;
    private double randomFactor = .3;

    /**
     * Problems menu buttons
     */

    public JFXButton shortestPathButton;
    public JFXButton travellingSalesmanButton;
    public JFXButton antColonyButton;
    public JFXButton dynamicProgrammingButton;

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
     * Loading elements
     */

    public FontAwesomeIconView algorithmSuccessIcon;
    public FontAwesomeIconView algorithmFailIcon;
    public StackPane loadingStack;
    public GridPane loading;

    /**
     * Run threads
     */

    private Thread shortestPathThread;
    private Thread dynamicProgrammingThread;
    private Thread antColonyThread;
    private Thread playingThread;
    private volatile boolean countTimer = false;
    private volatile boolean paused = false;
    private float speed = 1;

    /**
     * Highlight array list
     */

    private ArrayList<GraphNode> highlightedNodes = new ArrayList<>();
    private ArrayList<GraphEdge> highlightedEdges = new ArrayList<>();
    private ArrayList<NonDirectionalEdge> highlightedNonDirEdges = new ArrayList<>();

    /**
     * Problems results
     */

    private PathData shortestPathResult = null;
    private TravellingSalesManData dynamicProgrammingResult = null;
    private TravellingSalesManData antColonyResult = null;

    /**
     * New temp node
     */

    private GraphNode newNode;
    private boolean newNodeVisible = false;

    /**
     * Source and target nodes
     */

    private GraphNode sourceNode;
    private GraphNode targetNode;

    public Pane dummyPane;
    private static Pane staticDummyPane;

    /**
     * Hidden node and edges for drawing edge
     */
    public GraphNode hiddenNode;
    public GraphEdge newEdge;
    private boolean newEdgeVisible;

    /**
     * Menu manager object
     */

    private MenuManager menuManager;

    /**
     * Selection manager object
     */

    public SelectionManager selectionManager;

    /**
     * Transition manager object
     */

    public TransitionManager transitionManager;

    /**
     * Different states of app
     */

    public enum State {
        IDLE, DRAWING_EDGE, MOVING_NODE, RUNNING_ALGORITHM, PLAYING
    }

    /**
     * Different errors of running algorithm
     */

    public enum AlgorithmError {
        NONE, GRAPH_NOT_COMPLETE, NO_PATH_AVAILABLE
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
        SHORTEST_PATH, DYNAMIC_PROGRAMMING, ANT_COLONY
    }

    /**
     * Current application interaction mode
     */

    private Mode currentMode = null;
    private State currentState = State.IDLE;
    private Problem currentProblem = Problem.SHORTEST_PATH;

    /**
     * Key press status
     */

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
    private boolean mouseOnItem = false;
    private boolean menuVisible = true;

    public void set(Scene scene, Stage primaryStage) {
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
         * Set new node
         */
        setNewNode();

        /*
         * Set hidden drawing tools
         */
        setHiddenDrawingTools();

        /*
         * Set expanding menu listeners
         */
        setExpandingMenuListeners();

        /*
         * Set timer thread
         */
        setTimerThread();

        /*
         * Set current mode to node mode
         */
        changeMode(Mode.SELECT);
        menuManager.updateButtons(MenuManager.State.NOTHING_SELECTED);

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
        App.scene = scene;

        this.nodeGestures = new NodeGestures(this.canvas, this, mainGraph);
        this.sceneGestures = new SceneGestures(this.canvas, this, this.background);
        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, sceneGestures.getOnMousePressedEventHandler());
        scene.addEventFilter(MouseEvent.MOUSE_DRAGGED, sceneGestures.getOnMouseDraggedEventHandler());
        scene.addEventFilter(MouseEvent.MOUSE_MOVED, sceneGestures.getOnMouseMovedEventHandler());
        scene.addEventFilter(MouseDragEvent.MOUSE_RELEASED, sceneGestures.getOnMouseReleasedEventHandler());
        scene.addEventFilter(KeyEvent.KEY_PRESSED, sceneGestures.getOnKeyPressedEventHandler());
        scene.addEventFilter(KeyEvent.KEY_RELEASED, sceneGestures.getOnKeyReleasedEventHandler());

        App.scene.addEventFilter(ScrollEvent.ANY, sceneGestures.getOnScrollEventHandler());
        background.toBack();
        panIconPane.toBack();
        canvas.toFront();
    }

    private void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;

        this.primaryStage.iconifiedProperty().addListener((ov, t, t1) -> {
            checkExpandingMenuStatusAfterClick();
            leaveMenu();
            releaseLeftClick();
            releaseCtrl();
            releaseShift();
        });

        this.primaryStage.focusedProperty().addListener((ov, onHidden, onShown) -> {
            checkExpandingMenuStatusAfterClick();
            leaveMenu();
            releaseLeftClick();
            releaseCtrl();
            releaseShift();
        });
    }

    /**
     * Set new node transparent object
     */

    private void setNewNode() {
        this.newNode = new GraphNode("•");
        newNode.setTranslateX(0);
        newNode.setTranslateY(0);
        newNode.setPrefHeight(70);
        newNode.setPrefWidth(70);
        newNode.getStyleClass().add("node-transparent");
        newNode.setVisible(false);
        this.canvas.getChildren().add(newNode);
    }

    /**
     * Set hidden nodes an edges for drawing
     */

    private void setHiddenDrawingTools() {
        this.hiddenNode = new GraphNode("");
        this.hiddenNode.setVisible(false);
        this.hiddenNode.toBack();

        this.newEdge = new GraphEdge(0, this.hiddenNode, this.hiddenNode,
                Graph.EdgeOrientation.HORIZONTAL);
        this.newEdge.initialize();
        this.newEdge.hideEdge();
//        this.newEdge.setDashed();
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
     * Sets expanding menu listeners
     */

    private void setExpandingMenuListeners() {
        this.expandingProblemsMenu.addEventFilter(MouseEvent.MOUSE_ENTERED, event -> hoverExpandingProblemsMenu());
        this.expandingProblemsMenu.addEventFilter(MouseEvent.MOUSE_EXITED, event -> leaveExpandingProblemsMenu());
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
        menus.put(4, runtimeMenu);
        menus.put(5, antColonyMenu);

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
        buttons.put(12, dynamicProgrammingButton);
        buttons.put(13, antColonyButton);
        buttons.put(14, antsCountButton);
        buttons.put(15, alphaButton);
        buttons.put(16, betaButton);
        buttons.put(17, thresholdButton);
        buttons.put(18, playButton);
        buttons.put(19, stopButton);
        buttons.put(20, speedUpButton);
        buttons.put(21, speedDownButton);
        buttons.put(22, vaporButton);
        buttons.put(23, randomFactorButton);

        /*
         * Instantiate menu manager object
         */
        menuManager = new MenuManager(app, buttons, menus, expandingProblemsMenu);

        /*
         * Set loading elements
         */
        menuManager.setLoading(loading, loadingStack, algorithmSuccessIcon, algorithmFailIcon);

        /*
         * Set runtime labels and title and theirs animations
         */
        menuManager.setRuntimeLabels(timeTitle, timerValue, distanceTitle, distanceValue);
    }

    /**
     * Highlight node
     *
     * @param node the node
     */

    public void highlightNode(GraphNode node) {
        if (highlightedNodes.contains(node)) {
            return;
        }

        highlightedNodes.add(node);
        node.highlight();
    }

    /**
     * Highlight edge
     *
     * @param edge the edge
     */

    public void highlightEdge(GraphEdge edge) {
        if (highlightedEdges.contains(edge)) {
            return;
        }

        highlightedEdges.add(edge);
        edge.highlight();
    }

    /**
     * Highlight non-directional edge
     *
     * @param edge the edge
     */

    public void highlightNonDirEdge(NonDirectionalEdge edge) {
        if (highlightedNonDirEdges.contains(edge)) {
            return;
        }

        highlightedNonDirEdges.add(edge);
        edge.highlight();
    }

    /**
     * Reset highlights
     */

    public void resetHighlights() {
        /*
         * Revert highlight for every node
         */
        for (GraphNode node : highlightedNodes) {
            node.revertHighlight();
        }

        /*
         * Revert highlight for every edge
         */
        for (GraphEdge edge : highlightedEdges) {
            edge.revertHighlight();
        }

        /*
         * Revert highlight for every non-directional edge
         */
        for (NonDirectionalEdge edge : highlightedNonDirEdges) {
            edge.revertHighlight();
        }

        /*
         * Clear highlight array lists
         */
        this.highlightedNonDirEdges.clear();
        this.highlightedEdges.clear();
        this.highlightedNodes.clear();
    }

    /**
     * Sets shortest path thread.
     */

    public void startShortestPathThread() {
        shortestPathThread = new Thread(() -> {
            this.shortestPathResult = DijkstraAlgorithm.findShortestPath(mainGraph, this.sourceNode, this.targetNode);

            if (shortestPathResult == null) {
                finishProcessing(AlgorithmError.NO_PATH_AVAILABLE);
            } else {
                finishProcessing(AlgorithmError.NONE);
            }
        });

        /*
         * Start the thread
         */
        this.shortestPathThread.start();
    }

    /**
     * Stop shortest path thread
     */

    public void stopShortestPathThread() {
        this.shortestPathThread.stop();
    }

    /**
     * Start dynamic programming thread
     */

    private void startDynamicProgrammingThread() {
        dynamicProgrammingThread = new Thread(() -> {
            this.dynamicProgrammingResult = TravellingSalesManAlgorithm.findShortestCycle(mainGraph, this.sourceNode);

            if (dynamicProgrammingResult == null) {
                finishProcessing(AlgorithmError.GRAPH_NOT_COMPLETE);
            } else {
                finishProcessing(AlgorithmError.NONE);
            }
        });

        /*
         * Start the thread
         */
        this.dynamicProgrammingThread.start();
    }

    /**
     * Stop dynamic programming thread
     */

    private void stopDynamicProgrammingThread() {
        this.dynamicProgrammingThread.stop();
    }

    /**
     * Start ant colony thread
     */

    private void startAntColonyThread() {
        antColonyThread = new Thread(() -> {
            this.antColonyResult = AntColonyAlgorithm.findShortestCycle(mainGraph, this.sourceNode,
                    this.threshold, this.alpha, this.beta, this.vapor, this.antsCount, this.randomFactor);

            if (antColonyResult == null) {
                finishProcessing(AlgorithmError.GRAPH_NOT_COMPLETE);
            } else {
                finishProcessing(AlgorithmError.NONE);
            }
        });

        /*
         * Start the thread
         */
        this.antColonyThread.start();
    }

    /**
     * Stop ant colony thread
     */

    private void stopAntColonyThread() {
        antColonyThread.stop();
    }

    /**
     * Start playing thread
     */

    private void startPlayingThread() {
        playingThread = new Thread(() -> {
            this.transitionManager = new TransitionManager(this, menuManager);

            switch (getCurrentProblem()) {
                case SHORTEST_PATH:

                    break;
                case DYNAMIC_PROGRAMMING:
                    playTravellingSalesman();

                    break;
                case ANT_COLONY:
                    playTravellingSalesman();

                    break;
            }
        });

        /*
         * Start the thread
         */
        this.playingThread.start();
    }

    /**
     * Stop playing
     */

    public void stopPlaying() {
        /*
         * Exit processing mode
         */
        exitProcessingMode();

        /*
         * Reset highlights
         */
        resetHighlights();

        /*
         * Resume if paying is paused
         */
        if (paused) {
            resume();
        }
    }

    /**
     * Increase speed
     */

    public void increaseSpeed() {
        if (this.speed < 4) {
            this.speed *= 2;

            /*
             * Update menu buttons
             */
            menuManager.updateButtons(MenuManager.State.PLAYING);

            /*
             * Update duration for animations
             */
            transitionManager.updateSpeed();
        }
    }

    /**
     * Decrease speed
     */

    public void decreaseSpeed() {
        if (this.speed > 0.25) {
            this.speed /= 2;

            /*
             * Update menu buttons
             */
            menuManager.updateButtons(MenuManager.State.PLAYING);


            /*
             * Update duration for animations
             */
            transitionManager.updateSpeed();
        }
    }

    /**
     * Reset speed
     */

    public void resetSpeed() {
        this.speed = 1;
        menuManager.updateButtons(MenuManager.State.PLAYING);
    }

    /**
     * Gets playing speed
     *
     * @return the speed
     */

    public float getSpeed() {
        return this.speed;
    }

    /**
     * Reset results
     */

    private void resetResults() {
        this.dynamicProgrammingResult = null;
        this.shortestPathResult = null;
        this.antColonyResult = null;
    }

    /**
     * Sets timer thread.
     */

    public void setTimerThread() {
        Thread timerThread = new Thread(() -> {
            while (true) {
                double start = System.nanoTime();
                double current;
                int rounded = 0;
                boolean reset = true;

                while (countTimer) {
                    if (reset) {
                        Platform.runLater(() -> printTime(0));
                        reset = false;
                    }

                    current = System.nanoTime();
                    double elapsed = (current - start) / 1000000000;

                    if ((int) elapsed > rounded) {
                        rounded = (int) elapsed;

                        int finalRounded = rounded;
                        Platform.runLater(() -> printTime(finalRounded));
                    }
                }

                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        /*
         * Start timer thread
         */
        timerThread.start();
    }

    /**
     * Sets runtime titles animation thread.
     */

    public void setRuntimeAnimationsThread() {
        Thread runtimeAnimationsThread = new Thread(() -> {
            while (getCurrentState() == State.PLAYING) {
                try {
                    /*
                     * Show time
                     */
                    menuManager.showTime();

                    /*
                     * Wait 4 seconds
                     */
                    for (int i = 0; i < 400; i++) {
                        if (getCurrentState() != State.PLAYING) {
                            break;
                        }

                        Thread.sleep(10);
                    }

                    /*
                     * Show distance
                     */
                    menuManager.showDistance();

                    /*
                     * Wait 4 seconds again
                     */
                    for (int i = 0; i < 400; i++) {
                        if (getCurrentState() != State.PLAYING) {
                            break;
                        }

                        Thread.sleep(10);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            /*
             * Stop the animations after finished
             */
            menuManager.stopRuntimeLabelsAnimations();

        });

        /*
         * Start the thread
         */
        runtimeAnimationsThread.start();
    }

    /**
     * Start timer
     */

    public void startTimer() {
        this.countTimer = true;
    }

    /**
     * Stop timer
     */

    public void stopTimer() {
        this.countTimer = false;
    }

    /**
     * Reset timer
     */

    public void resetTimer() {
        printTime(0);
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
        node.addEventFilter(MouseEvent.MOUSE_CLICKED, nodeGestures.getOnMouseClickedEventHandler());
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
     * Print time on runtime menu
     */

    public void printTime(int seconds) {
        int min = seconds / 60;
        int sec = seconds % 60;
        String time = min + ":" + (sec >= 10 ? sec : "0" + sec);
        this.timerValue.setText(time);
    }

    /**
     * Print distance on runtime menu
     */

    public void printDistance(double distance) {
        String text;
        if (distance % 1 == 0) {
            text = String.valueOf((int) distance);
        } else {
            text = String.format("%.1f", distance);
        }

        Platform.runLater(() -> distanceValue.setText(text));
    }

    /**
     * Reset runtime labels opacity
     */

    public void resetRuntimeLabels() {
        timerValue.setOpacity(1);
        timeTitle.setOpacity(1);
        distanceValue.setOpacity(0);
        distanceTitle.setOpacity(0);
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
        overlay.toFront();
        panIconPane.toFront();
    }

    /**
     * Sets scene on top.
     */

    public void setSceneOnTop() {
        overlay.getStyleClass().remove("dark-overlay");
        canvas.toFront();

        if (loading.isVisible()) {
            loading.toFront();
        }
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

        /*
         * Deselect everything
         */
        selectionManager.deselectAll();
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

        /*
         * Deselect everything
         */
        selectionManager.deselectAll();
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

        /*
         * Deselect everything
         */
        selectionManager.deselectAll();
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
        addNodeToCanvas(newNode);

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
        double weight = showNumberInputDialog("Edge Weight Input...", "Enter weight:");

        if (weight == -1) {
            return;
        }

        if (nonDirectional) {
            placeNewNonDirectionalEdge(weight, sourceNode, targetNode);
        } else {
            placeNewEdge(weight, sourceNode, targetNode);
        }
    }

    /**
     * Batch add edges
     *
     * @param sourceNode     the source node
     * @param nonDirectional the non directional
     */

    public void batchAddEdges(GraphNode sourceNode, boolean nonDirectional) {
        /*
         * Prompt for min and max weight
         */
        Pair<Double, Double> pair = app.showMinMaxInputDialog("weight for the edges");

        if (pair == null) {
            return;
        }

        double min = pair.getKey();
        double max = pair.getValue();

        for (GraphNode node : mainGraph.getNodes()) {
            if (node != sourceNode && possibleToDrawEdge(sourceNode, node, nonDirectional)) {
                double weight = min == max ? min : Math.random() * (max - min) + min;

                if (nonDirectional) {
                    placeNewNonDirectionalEdge(Math.round(weight), sourceNode, node);
                } else {
                    placeNewEdge(Math.round(weight * 4) / 4f, sourceNode, node);
                }
            }
        }
    }

    /**
     * Place new edge
     *
     * @param weight     the weight
     * @param sourceNode the source node
     * @param targetNode the target node
     */
    public void placeNewEdge(double weight, GraphNode sourceNode, GraphNode targetNode) {
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

    /**
     * Place non directional edge
     *
     * @param weight     the weight
     * @param sourceNode the source node
     * @param targetNode the target node
     */
    public void placeNewNonDirectionalEdge(double weight, GraphNode sourceNode, GraphNode targetNode) {
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
    }

    /**
     * Remove directional edges
     */

    public void filterEdges() {
        ArrayList<GraphEdge> edges = (ArrayList<GraphEdge>) mainGraph.getEdges().clone();

        for (GraphEdge edge : edges) {
            GraphNode source = edge.getSourceNode();
            GraphNode target = edge.getTargetNode();

            if (mainGraph.getTwoWayEdge(source, target) == null) {
                if (source.getIncomingNodes().get(target) == null) {
                    /*
                     * Get the weight value and the delete the edge
                     */
                    double weight = edge.getWeight();
                    deleteEdge(edge);

                    /*
                     * Create a new non-directional edge instead
                     */
                    placeNewNonDirectionalEdge(weight, source, target);
                } else {
                    /*
                     * Delete the edge
                     */
                    deleteEdge(edge);
                }
            }
        }
    }

    /**
     * Set ants count
     */

    public void setAntsCount() {
        int count = (int) showNumberInputDialog("Ants Count Input...", "Enter ants count:");

        if (count == -1) {
            return;
        }

        this.antsCount = count;
        antsCountValue.setText(String.valueOf(count));
    }

    /**
     * Set alpha value
     */

    public void setAlpha() {
        double alpha = showNumberInputDialog("Alpha Input...", "Enter value of alpha:");

        if (alpha == -1) {
            return;
        }

        this.alpha = alpha;
        alphaValue.setText(String.valueOf(alpha));
    }

    /**
     * Set beta value
     */

    public void setBeta() {
        double beta = showNumberInputDialog("Beta Input...", "Enter value of beta:");

        if (beta == -1) {
            return;
        }

        this.beta = beta;
        betaValue.setText(String.valueOf(beta));
    }

    /**
     * Set threshold value
     */

    public void setThreshold() {
        int threshold = (int) showNumberInputDialog("Threshold Input...", "Enter threshold:");

        if (threshold == -1) {
            return;
        }

        this.threshold = threshold;
        thresholdValue.setText(String.valueOf(threshold));
    }

    /**
     * Set vapor value
     */

    public void setVapor() {
        double vapor = showSmallNumberInputDialog("Evaporation Constant Input...", "Enter evaporation constant (between 0 and 1):");

        if (vapor == -1) {
            return;
        }

        this.vapor = vapor;
        vaporValue.setText(String.valueOf(vapor));
    }

    /**
     * Set random factor value
     */

    public void setRandomFactor() {
        double randomFactor = showSmallNumberInputDialog("Random Factor Input...", "Enter random factor (between 0 and 1):");

        if (randomFactor == -1) {
            return;
        }

        this.randomFactor = randomFactor;
        randomFactorValue.setText(String.valueOf(randomFactor));
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
     * Show ID string input dialog
     */

    public String showIDInputDialog() {
        TextInputDialog alert = new TextInputDialog("");
        alert.getDialogPane().getStylesheets().add(
                getClass().getResource("main.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("custom-dialog");
        alert.setTitle("");
        alert.setHeaderText("Node ID Input...");
        FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.WARNING);
        icon.setGlyphSize(40);
        icon.setFill(Color.valueOf("#29a2b0"));
        alert.setGraphic(icon);
        alert.setContentText("Enter ID:");
        Optional<String> input = alert.showAndWait();

        if (!input.isPresent()) {
            return "";
        }

        while (input.get().equals("")) {
            showInputErrorDialog();
            input = alert.showAndWait();

            if (input.isPresent()) {
                return "";
            }
        }

        return input.orElse("");

    }

    /**
     * Show number input dialog
     */

    public double showNumberInputDialog(String header, String text) {
        TextInputDialog alert = new TextInputDialog("");
        alert.getDialogPane().getStylesheets().add(
                getClass().getResource("main.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("custom-dialog");
        alert.setTitle("");
        alert.setHeaderText(header);
        FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.WARNING);
        icon.setGlyphSize(40);
        icon.setFill(Color.valueOf("#29a2b0"));
        alert.setGraphic(icon);
        alert.setContentText(text);
        Optional<String> input = alert.showAndWait();

        if (!input.isPresent()) {
            return -1;
        }

        while (!isNumber(input.get()) || Double.parseDouble(input.get()) <= 0) {
            showInputErrorDialog();
            input = alert.showAndWait();

            if (!input.isPresent()) {
                return -1;
            }
        }

        return Double.parseDouble(input.get());
    }

    /**
     * Show min and max number input dialog
     */

    public Pair<Double, Double> showMinMaxInputDialog(String postfix) {
        Dialog<Pair<String, String>> alert = new Dialog<>();
        alert.getDialogPane().getStylesheets().add(
                getClass().getResource("main.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("custom-dialog");
        alert.setHeaderText("Min And Max Input...");
        FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.WARNING);
        icon.setGlyphSize(40);
        icon.setFill(Color.valueOf("#29a2b0"));
        alert.setGraphic(icon);

        // Set the button types
        ButtonType okayButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        alert.getDialogPane().getButtonTypes().addAll(okayButton, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 10, 10, 10));

        TextField from = new TextField();
        from.setPromptText("Enter min...");
        HBox first = new HBox();
        first.setSpacing(50);
        first.getChildren().addAll(new Label("Min:"), from);

        TextField to = new TextField();
        to.setPromptText("Enter max...");
        HBox second = new HBox();
        second.setSpacing(50);
        second.getChildren().addAll(new Label("Max:"), to);

//        gridPane.add(new Label("Min:"), 0, 1);
//        gridPane.add(from, 1, 1);
//        gridPane.add(new Label("Max:"), 0, 2);
//        gridPane.add(to, 1, 2);

        gridPane.add(new Label("Please enter min and max " + postfix + ":"), 0, 0);
        gridPane.add(first, 0, 1);
        gridPane.add(second, 0, 2);

        alert.getDialogPane().setContent(gridPane);

        // Request focus on the minimum field by default.
        Platform.runLater(() -> from.requestFocus());

        // Convert the result to a min-max pair when the okay button is clicked.
        alert.setResultConverter(dialogButton -> {
            if (dialogButton == okayButton) {
                return new Pair<>(from.getText(), to.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = alert.showAndWait();

        if (!result.isPresent()) {
            return null;
        }

        boolean condition = !isNumber(result.get().getKey())
                || !isNumber(result.get().getValue())
                || Double.parseDouble(result.get().getKey()) <= 0
                || Double.parseDouble(result.get().getValue()) <= 0
                || Double.parseDouble(result.get().getKey()) > Double.parseDouble(result.get().getValue());

        while (condition) {
            showInputErrorDialog();
            result = alert.showAndWait();

            if (!result.isPresent()) {
                return null;
            }

            condition = !isNumber(result.get().getKey())
                    || !isNumber(result.get().getValue())
                    || Double.parseDouble(result.get().getKey()) <= 0
                    || Double.parseDouble(result.get().getValue()) <= 0
                    || Double.parseDouble(result.get().getKey()) > Double.parseDouble(result.get().getValue());
        }

        return new Pair<>(Double.parseDouble(result.get().getKey()), Double.parseDouble(result.get().getValue()));
    }

    /**
     * Show 0-1 number input dialog
     */

    public double showSmallNumberInputDialog(String header, String text) {
        TextInputDialog alert = new TextInputDialog("");
        alert.getDialogPane().getStylesheets().add(
                getClass().getResource("main.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("custom-dialog");
        alert.setTitle("");
        alert.setHeaderText(header);
        FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.WARNING);
        icon.setGlyphSize(40);
        icon.setFill(Color.valueOf("#29a2b0"));
        alert.setGraphic(icon);
        alert.setContentText(text);
        Optional<String> input = alert.showAndWait();

        if (!input.isPresent()) {
            return -1;
        }

        while (!isNumber(input.get()) || Double.parseDouble(input.get()) < 0 || Double.parseDouble(input.get()) > 1) {
            showInputErrorDialog();
            input = alert.showAndWait();

            if (!input.isPresent()) {
                return -1;
            }
        }

        return Double.parseDouble(input.get());
    }

    /**
     * Show input error dialog
     */

    public void showInputErrorDialog() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.getDialogPane().getStylesheets().add(
                getClass().getResource("main.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("error-dialog");
        alert.setTitle("");
        alert.setHeaderText("Input Error...");
        FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.WARNING);
        icon.setGlyphSize(40);
        icon.setFill(Color.valueOf("#b41c1c"));
        alert.setGraphic(icon);
        alert.setContentText("You have entered an invalid value! Please try again.");
        alert.showAndWait();
    }

    /**
     * Show input error dialog
     */

    public void showErrorDialog(String header, String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.getDialogPane().getStylesheets().add(
                getClass().getResource("main.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("error-dialog");
        alert.setTitle("");
        alert.setHeaderText(header);
        FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.WARNING);
        icon.setGlyphSize(40);
        icon.setFill(Color.valueOf("#b41c1c"));
        alert.setGraphic(icon);
        alert.setContentText(text);
        alert.show();
    }

    /**
     * Show source node error dialog
     */

    public void showSourceNodeErrorDialog() {
        showErrorDialog("Source Node Error...",
                "You must set a node as source. Please choose source node and try again.");
    }

    /**
     * Show target node error dialog
     */

    public void showTargetNodeErrorDialog() {
        showErrorDialog("Target Node Error...",
                "You must set a node as destination target. Please choose target node and try again.");
    }

    /**
     * Show graph not complete error dialog
     */

    public void showGraphNotCompleteErrorDialog() {
        showErrorDialog("Graph Not Complete Error...",
                "The graph must be complete to be processed. Draw every possible edge and try again.");
    }

    /**
     * Show no path available error dialog
     */

    public void showNoPathAvailableErrorDialog() {
        showErrorDialog("No Path Available Error...",
                "There is no path available from source to the target node. Please draw some more edges and try again.");
    }

    /**
     * Show no path available error dialog
     */

    public void showNotEnoughNodesErrorDialog() {
        showErrorDialog("Not Enough Nodes Error...",
                "There are not enough nodes. Please add more nodes and try again.");
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
     * Press enter
     */

    public void pressEnter() {
        /*
         * Check for open expanding problems menu
         */
        checkExpandingMenuStatusAfterClick();

        /*
         * Return if application is in running state
         */
        if (getCurrentState() != State.IDLE) {
            return;
        }

        /*
         * Set mode to select mode
         */
        changeMode(Mode.SELECT);

        /*
         * Reset results at first
         */
        resetResults();

        /*
         * Start processing
         */

        switch (getCurrentProblem()) {
            case SHORTEST_PATH:
                processShortestPath();

                break;
            case DYNAMIC_PROGRAMMING:
                processDynamicProgramming();

                break;
            case ANT_COLONY:
            default:
                processAntColony();

                break;
        }
    }

    /**
     * Process shortest path
     */

    private void processShortestPath() {
        /*
         * Check if there is not enough nodes
         */
        if (mainGraph.getNodes().size() < 2) {
            showNotEnoughNodesErrorDialog();
            return;
        }

        /*
         * Check for source node
         */
        if (sourceNode == null) {
            showSourceNodeErrorDialog();
            return;
        }

        /*
         * Check for target node
         */
        if (targetNode == null) {
            showTargetNodeErrorDialog();
            return;
        }

        /*
         * Enter processing mode
         */
        enterProcessingMode();

        /*
         * Start processing as soon as loading screen appears
         */
        PauseTransition wait = new PauseTransition(Duration.millis(500));
        wait.setOnFinished(event -> {
            /*
             * Start timer
             */
            startTimer();

            /*
             * Start a new thread to process the shortest path
             */
            startShortestPathThread();
        });
        wait.play();
    }

    /**
     * Process dynamic programming
     */

    private void processDynamicProgramming() {
        /*
         * Check if there is not enough nodes
         */
        if (mainGraph.getNodes().size() <= 2) {
            showNotEnoughNodesErrorDialog();
            return;
        }

        /*
         * Check for source node
         */
        if (sourceNode == null) {
            showSourceNodeErrorDialog();
            return;
        }

        /*
         * Enter processing mode
         */
        enterProcessingMode();

        /*
         * Start processing as soon as loading screen appears
         */
        PauseTransition wait = new PauseTransition(Duration.millis(500));
        wait.setOnFinished(event -> {
            /*
             * Start timer
             */
            startTimer();

            /*
             * Start a new thread to process dynamic programming
             */
            startDynamicProgrammingThread();
        });
        wait.play();
    }

    /**
     * Process ant colony
     */

    private void processAntColony() {
        /*
         * Check if there is not enough nodes
         */
        if (mainGraph.getNodes().size() <= 2) {
            showNotEnoughNodesErrorDialog();
            return;
        }

        /*
         * Check for source node
         */
        if (sourceNode == null) {
            showSourceNodeErrorDialog();
            return;
        }

        /*
         * Enter processing mode
         */
        enterProcessingMode();

        /*
         * Start processing as soon as loading screen appears
         */
        PauseTransition wait = new PauseTransition(Duration.millis(500));
        wait.setOnFinished(event -> {
            /*
             * Start timer
             */
            startTimer();

            /*
             * Start a new thread to process ant colony
             */
            startAntColonyThread();
        });
        wait.play();
    }

    /**
     * Enter processing mode
     */

    private void enterProcessingMode() {
        /*
         * Set current state to running algorithm
         */
        setCurrentState(State.RUNNING_ALGORITHM);

        hideMenu();
        showRuntimeMenu();
        resetTimer();
        showLoading();
        menuManager.updateButtons(MenuManager.State.RUNNING_ALGORITHM);
    }

    /**
     * Exit processing mode
     */

    private void exitProcessingMode() {
        hideRuntimeMenu();
        setCurrentState(State.IDLE);
        showMenu();
        selectionManager.clear();
    }

    /**
     * Finish processing
     */

    private void finishProcessing(AlgorithmError error) {
        /*
         * Stop timer
         */
        stopTimer();

        PauseTransition wait = new PauseTransition(Duration.millis(1000));

        switch (error) {
            case GRAPH_NOT_COMPLETE:
                /*
                 * Set current state to idle
                 */
                setCurrentState(State.IDLE);

                /*
                 * First show fail icon then hide the loading screen
                 */
                hideLoadingWithFail();

                /*
                 * Wait for the fail icon to fade out then show error dialog
                 */
                wait.setOnFinished(event -> {
                    exitProcessingMode();
                    showGraphNotCompleteErrorDialog();
                });
                wait.play();

                break;
            case NO_PATH_AVAILABLE:
                /*
                 * Set current state to idle
                 */
                setCurrentState(State.IDLE);

                /*
                 * First show fail icon then hide the loading screen
                 */
                hideLoadingWithFail();

                /*
                 * Wait for the fail icon to fade out then show error dialog
                 */
                wait.setOnFinished(event -> {
                    exitProcessingMode();
                    showNoPathAvailableErrorDialog();
                });
                wait.play();

                break;
            case NONE:
                /*
                 * First show success icon then hide the loading screen
                 */
                hideLoadingWithSuccess();

                /*
                 * Wait for the fail icon to fade out then play the results
                 */
                wait.setOnFinished(event -> {
                    if (getCurrentState() == State.RUNNING_ALGORITHM) {
                        playResults();
                    }
                });
                wait.play();

                break;
        }
    }

    /**
     * Play results
     */

    private void playResults() {
        /*
         * Set current state to playing, reset playing
         * speed and update the menu accordingly
         */
        setCurrentState(State.PLAYING);
        resetSpeed();

        /*
         * Start the playing thread
         */
        startPlayingThread();
    }

    /**
     * Play shortest path
     */

    private void playShortestPath(PathData pathData) {
//        System.out.println("Distance: " + pathData.);
    }

    /**
     * Play travelling salesman
     */

    private void playTravellingSalesman() {
        GraphNode source, target;
        LinkedHashMap<GraphNode, CubicCurve> dataMap = new LinkedHashMap<>();

        /*
         * Get the shortest distance from the results
         */
        double distance = getCurrentProblem() == Problem.ANT_COLONY ?
                antColonyResult.getShortestDistance() : dynamicProgrammingResult.getShortestDistance();

        /*
         * Print the distance of the runtime menu
         */
        printDistance(distance);

        /*
         * Start runtime labels animations thread
         */
        setRuntimeAnimationsThread();

        /*
         * Get the results array list according to current problems
         */
        ArrayList<GraphNode> results = getCurrentProblem() == Problem.ANT_COLONY ?
                antColonyResult.getpathSecond() : dynamicProgrammingResult.getpathSecond();

        for (int i = 0; i < results.size() - 1; i++) {
            source = results.get(i);
            target = results.get(i + 1);

            /*
             * Get the edge connecting the two nodes together
             */
            NonDirectionalEdge edge = mainGraph.getTwoWayEdge(source, target);

            /*
             * Add node and edge to hash map
             */
            dataMap.put(target, edge);
        }

        try {
            transitionManager.start(dataMap);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Press stop button
     */

    public void pressStop() {
        switch (getCurrentState()) {
            case RUNNING_ALGORITHM:
                /*
                 * Stop timer
                 */
                stopTimer();

                /*
                 * Stop the corresponding algorithm thread
                 */
                switch (getCurrentProblem()) {
                    case SHORTEST_PATH:
                        stopShortestPathThread();
                        break;
                    case DYNAMIC_PROGRAMMING:
                        stopDynamicProgrammingThread();
                        break;
                    case ANT_COLONY:
                        stopAntColonyThread();
                        break;
                }

                /*
                 * Exit processing mode and hide loading
                 */
                hideLoading();
                exitProcessingMode();

                break;
            case PLAYING:
                /*
                 * Set the current state to idle
                 */
                setCurrentState(State.IDLE);

                break;
            default:
                break;
        }
    }


    /**
     * Press play
     */

    public void pressPlay() {
        if (this.paused) {
            resume();
        } else {
            pause();
        }
    }

    /**
     * Pause
     */

    public void pause() {
        this.paused = true;
        menuManager.pause();
    }

    /**
     * Resume
     */

    public void resume() {
        this.paused = false;
        menuManager.play();
    }

    /**
     * Is playing
     */

    public boolean isPaused() {
        return paused;
    }

    /**
     * Show loading
     */

    public void showLoading() {
        menuManager.showLoading();
    }

    /**
     * Hide loading
     */

    public void hideLoading() {
        menuManager.hideLoading();
    }

    /**
     * Show success icon and then hide loading screen
     */

    public void hideLoadingWithSuccess() {
        menuManager.showSuccessIcon();
        PauseTransition wait = new PauseTransition(Duration.millis(1000));
        wait.setOnFinished(event -> hideLoading());
        wait.play();
    }

    /**
     * Show fail icon and then hide loading screen
     */

    public void hideLoadingWithFail() {
        menuManager.showFailIcon();
        PauseTransition wait = new PauseTransition(Duration.millis(1000));
        wait.setOnFinished(event -> {
            hideLoading();
            hideRuntimeMenu();
        });
        wait.play();
    }

    /**
     * Show expanding problems menu
     */

    public void showExpandingProblemsMenu() {
        if (!expandingProblemsMenuVisible && !menuManager.isExpandingProblemsMenuTransitionRunning()) {
            menuManager.showExpandingProblemsMenu();
            expandingProblemsMenu.setVisible(true);
        }
    }

    /**
     * Hide expanding problems menu
     */

    public void hideExpandingProblemsMenu() {
        if (expandingProblemsMenuVisible && !menuManager.isExpandingProblemsMenuTransitionRunning()) {
            menuManager.hideExpandingProblemsMenu();
        }
    }

    /**
     * Check expanding menu status after click
     */

    public void checkExpandingMenuStatusAfterClick() {
        if (expandingProblemsMenuVisible && !expandingProblemsMenuHovered) {
            hideExpandingProblemsMenu();
        }
    }

    /**
     * Hover expanding problems menu
     */

    public void hoverExpandingProblemsMenu() {
        this.expandingProblemsMenuHovered = true;
    }

    /**
     * Leave expanding problems menu
     */

    public void leaveExpandingProblemsMenu() {
        this.expandingProblemsMenuHovered = false;
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
        if (source.getTwoWayNodes().get(target) != null) {
            return false;
        }

        if (source.getOutgoingNodes().get(target) != null) {
            return false;
        }

        if (nonDirectional) {
            if (source.getIncomingNodes().get(target) != null) {
                return false;
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
     * Deselect all problem buttons.
     *
     * @param buttons the buttons
     */

    public void deselectAllProblemButtons(List<JFXButton> buttons) {
        for (JFXButton button : buttons) {
            button.getStyleClass().remove("menu-button-blue");
            button.getStyleClass().addAll("menu-button-transparent", "menu-button-hover-blue-outline");
        }
    }

    /**
     * Select problem button
     *
     * @param button the button
     */

    public void selectProblemButton(JFXButton button) {
        button.getStyleClass().removeAll("menu-button-transparent", "menu-button-hover-blue-outline");
        button.getStyleClass().add("menu-button-blue");
    }

    /**
     * Sets current problem
     *
     * @param problem the problem
     */

    public void setCurrentProblem(Problem problem) {
        this.currentProblem = problem;
    }

    /**
     * Sets problem
     */

    public void changeProblem(Problem problem) {
        if (this.currentProblem == problem) {
            return;
        }

        /*
         * Change the menu layout according to the
         * new application interaction mode
         */
        menuManager.changeProblem(problem);

        switch (problem) {
            case SHORTEST_PATH:
                shortestPathProblem();
                break;
            case DYNAMIC_PROGRAMMING:
                dynamicProgrammingProblem();
                break;
            case ANT_COLONY:
            default:
                antColonyProblem();
                break;
        }

        /*
         * Update the menu based of selections
         */
        selectionManager.deselectAll();
    }

    /**
     * Shortest path problem
     */
    public void shortestPathProblem() {
        setCurrentProblem(Problem.SHORTEST_PATH);

        /*
         * Hide ant colony menu
         */
        hideAntColonyMenu();
    }

    /**
     * Travelling salesman problem
     */
    public void dynamicProgrammingProblem() {
        setCurrentProblem(Problem.DYNAMIC_PROGRAMMING);

        /*
         * Reset target node
         */
        resetTargetNode();

        /*
         * Hide ant colony menu
         */
        hideAntColonyMenu();

        /*
         * Filter the edges and convert the directional ones to non-directional
         */
        filterEdges();

        if (getCurrentMode() == Mode.DIRECTIONAL_EDGE) {
            changeMode(Mode.SELECT);
        }
    }

    /**
     * Ant colony problem
     */
    public void antColonyProblem() {
        setCurrentProblem(Problem.ANT_COLONY);

        /*
         * Reset target node
         */
        resetTargetNode();

        /*
         * Show ant colony menu
         */
        showAntColonyMenu();

        /*
         * Filter the edges and convert the directional ones to non-directional
         */
        filterEdges();

        if (getCurrentMode() == Mode.DIRECTIONAL_EDGE) {
            changeMode(Mode.SELECT);
        }
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
            if (this.sourceNode != null) {
                resetSourceNode();
            }

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
            if (this.targetNode != null) {
                resetTargetNode();
            }
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
     * Sets label for the selected node
     */
    public void setLabel(GraphNode node) {
        String newID = showIDInputDialog();

        if (newID.equals("")) {
            return;
        }

        node.setIdentifier(newID);
    }

    /**
     * Sets weight for the selected edge or edges
     */
    public void setWeight() {
        double newWeight = showNumberInputDialog("Edge Weight Input...", "Enter weight:");

        if (newWeight == -1) {
            return;
        }

        ArrayList<GraphEdge> edges = selectionManager.getEdges();
        ArrayList<NonDirectionalEdge> nonDirectionalEdges = selectionManager.getNonDirectionalEdges();

        /*
         * Set weight for every selected edge
         */
        for (GraphEdge edge : edges) {
            edge.setWeight(newWeight);
        }

        /*
         * Set weight for every selected non-directional edge
         */
        for (NonDirectionalEdge edge : nonDirectionalEdges) {
            edge.setWeight(newWeight);
        }
    }

    /**
     * Invert edge direction
     *
     * @param edge the edge
     */

    public void invertEdgeDirection(GraphEdge edge) {
        GraphNode sourceNode = edge.getSourceNode(), targetNode = edge.getTargetNode();

        /*
         * Check if it's possible to draw inverted edge
         */
        boolean possible = possibleToDrawEdge(targetNode, sourceNode, false);

        if (possible) {
            /*
             * Delete selected items
             */
            deleteItems();

            /*
             * Add new edge in opposite direction
             */
            double weight = edge.getWeight();
            placeNewEdge(weight, targetNode, sourceNode);
        } else {
            showEdgeErrorDialog();
        }
    }

    /**
     * Delete items
     */

    public void deleteItems() {
        if (getCurrentState() != State.IDLE) {
            return;
        }

        if (selectionManager.nothing()) {
            boolean confirm = showConfirmDeleteDialog();

            if (!confirm) {
                return;
            }

            /*
             * Delete all of the nodes, so the attached edges and non-directional
             * edges are removed themselves automatically
             */
            ArrayList<GraphNode> nodes = (ArrayList<GraphNode>) mainGraph.getNodes().clone();

            for (GraphNode node : nodes) {
                deleteNode(node);
            }

            /*
             * Reset graph ID generator
             */
            mainGraph.resetID();
        } else {
            /*
             * Delete selected non-directional edges
             */
            for (NonDirectionalEdge edge : selectionManager.getNonDirectionalEdges()) {
                deleteNonDirectionalEdge(edge);
            }

            /*
             * Delete selected edges
             */
            for (GraphEdge edge : selectionManager.getEdges()) {
                deleteEdge(edge);
            }

            /*
             * Delete selected nodes
             */
            for (GraphNode node : selectionManager.getNodes()) {
                deleteNode(node);
            }
        }

        /*
         * Deselect everything
         */
//        selectionManager.deselectAll();
        selectionManager.clear();
    }

    /**
     * Delete node.
     *
     * @param node the node
     */

    public void deleteNode(GraphNode node) {
        /*
         * Delete all non-directional edges
         */
        ArrayList<NonDirectionalEdge> nonDirectionalEdges = new ArrayList<>(node.getTwoWayNodes().values());
        for (NonDirectionalEdge edge : nonDirectionalEdges) {
            deleteNonDirectionalEdge(edge);
        }

        /*
         * Delete all incoming edges
         */
        ArrayList<GraphEdge> incomingEdges = new ArrayList<>(node.getIncomingNodes().values());
        for (GraphEdge edge : incomingEdges) {
            deleteEdge(edge);
        }

        /*
         * Delete all outgoing edges
         */
        ArrayList<GraphEdge> outgoingEdges = new ArrayList<>(node.getOutgoingNodes().values());
        for (GraphEdge edge : outgoingEdges) {
            deleteEdge(edge);
        }

        /*
         * Check if node is source node or target node
         */
        if (this.sourceNode == node) {
            resetSourceNode();
        } else if (this.targetNode == node) {
            resetTargetNode();
        }

        /*
         * Set node as deleted
         */
        node.delete();

        /*
         * Remove from canvas
         */
        removeNodeFromCanvas(node);

        /*
         * Remove node from the main graph
         */
        mainGraph.removeNode(node);
    }

    /**
     * Delete edge.
     *
     * @param edge the edge
     */

    public void deleteEdge(GraphEdge edge) {
        /*
         * Set edge as deleted
         */
        edge.delete();

        /*
         * Remove from canvas
         */
        edge.removeFromCanvas(this.canvas);

        /*
         * Remove edge from main graph
         */
        mainGraph.removeDirectionalEdge(edge);
    }

    /**
     * Delete non-directional edge
     *
     * @param edge the edge
     */

    public void deleteNonDirectionalEdge(NonDirectionalEdge edge) {
        /*
         * Set edge as deleted
         */
        edge.delete();

        /*
         * Remove from canvas
         */
        edge.removeFromCanvas(this.canvas);

        /*
         * Remove edge from main graph
         */
        mainGraph.removeNonDirectionalEdge(edge);
    }

    /**
     * Get non-directional edges on the canvas
     *
     * @return
     */

    private ArrayList<NonDirectionalEdge> getNonDirectionalEdgesOnCanvas() {
        ArrayList<NonDirectionalEdge> edges = new ArrayList<>();

        for (Node node : this.canvas.getChildren()) {
            if (node.getClass() == NonDirectionalEdge.class) {
                edges.add((NonDirectionalEdge) node);
            }
        }

        return edges;
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
        if (getCurrentState() == State.RUNNING_ALGORITHM || getCurrentState() == State.PLAYING) {
            return;
        }

        showMainMenu();

        if (this.currentProblem == Problem.ANT_COLONY) {
            showAntColonyMenu();
        }
    }

    /**
     * Hide menu
     */

    public void hideMenu() {
        hideMainMenu();

        if (this.currentProblem == Problem.ANT_COLONY) {
            hideAntColonyMenu();
        }
    }

    /**
     * Show main menu
     */
    public void showMainMenu() {
        if (menuVisible) {
            return;
        }

        menuManager.showMenu();
        menuVisible = true;
    }

    /**
     * Hide main menu
     */
    public void hideMainMenu() {
        if (!menuVisible) {
            return;
        }

        menuManager.hideMenu();
        menuVisible = false;
    }

    /**
     * Show runtime menu
     */

    public void showRuntimeMenu() {
        if (runtimeMenuVisible) {
            return;
        }

        resetRuntimeLabels();
        menuManager.showRuntimeMenu();
        runtimeMenuVisible = true;
    }

    /**
     * Hide runtime menu
     */

    public void hideRuntimeMenu() {
        if (!runtimeMenuVisible) {
            return;
        }

        menuManager.hideRuntimeMenu();
        runtimeMenuVisible = false;
    }

    /**
     * Show ant colony menu
     */

    public void showAntColonyMenu() {
        if (antColonyMenuVisible) {
            return;
        }

        menuManager.showAntColonyMenu();
        antColonyMenuVisible = true;
    }

    /**
     * Hide ant colony menu
     */

    public void hideAntColonyMenu() {
        if (!antColonyMenuVisible) {
            return;
        }

        menuManager.hideAntColonyMenu();
        antColonyMenuVisible = false;
    }

    /**
     * Hover nodes
     */

    public void hoverItem() {
        this.mouseOnItem = true;
    }

    /**
     * Leave node
     */

    public void leaveItem() {
        this.mouseOnItem = false;
    }

    /**
     * Is node hovered boolean
     *
     * @return the boolean
     */

    public boolean isAnythingHovered() {
        return this.mouseOnItem;
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

    public static void addNodeToCanvas(GraphNode node) {
        appCanvas.getChildren().add(node);
    }

    /**
     * Remove node from canvas
     *
     * @param node the node
     */

    public static void removeNodeFromCanvas(GraphNode node) {
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

    public void sendNodesToFront() {
        for (GraphNode node : mainGraph.getNodes()) {
            node.toFront();
        }

        if (getCurrentState() == State.PLAYING) {
            sendHighlightedToFront();
        }
    }

    /**
     * Send highlighted to front
     */

    public void sendHighlightedToFront() {
        /*
         * Send every highlighted edge to front
         */
        for (GraphEdge edge : highlightedEdges) {
            edge.sendToFront();
        }

        /*
         * Send every highlighted non-directional edge to front
         */
        for (NonDirectionalEdge edge : highlightedNonDirEdges) {
            edge.sendToFront();
        }

        /*
         * Send every highlighted node to front
         */
        for (GraphNode node : highlightedNodes) {
            node.sendToFront();
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
