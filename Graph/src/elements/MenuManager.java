package elements;

import com.jfoenix.controls.JFXButton;
import graph.App;
import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

import java.util.*;

/**
 * The type Menu manager.
 */
public class MenuManager {
    private App app;

    /**
     * Menus grid panes
     */

    private GridPane mainMenu;
    private GridPane problemsMenu;
    private GridPane toolsMenu;
    private GridPane modeMenu;
    private GridPane runtimeMenu;
    private HBox expandingProblemsMenu;

    /**
     * Problems menu buttons
     */

    private JFXButton shortestPathButton;
    private JFXButton travellingSalesmanButton;
    private JFXButton dynamicProgrammingButton;
    private JFXButton antColonyButton;

    /**
     * Tools menu buttons
     */

    private JFXButton setSourceNodeButton;
    private JFXButton setTargetNodeButton;
    private JFXButton setWeightButton;
    private JFXButton setLabelButton;
    private JFXButton changeDirectionButton;
    private JFXButton removeButton;

    /**
     * Mode menu buttons
     */

    private JFXButton nodeButton;
    private JFXButton directionalEdgeButton;
    private JFXButton nonDirectionalEdgeButton;
    private JFXButton selectButton;

    /**
     * Menu hide and show animations
     */

    private TranslateTransition hideMenu;
    private TranslateTransition showMenu;
    private TranslateTransition hideRuntimeMenu;
    private TranslateTransition showRuntimeMenu;
    private ParallelTransition hideExpandingProblemsMenu;
    private ParallelTransition showExpandingProblemsMenu;
    private boolean expandingProblemsMenuTransitionRunning = false;

    public enum State {
        SELECTED_BOTH_NODES_AND_EDGES, SELECTED_SINGLE_DIRECTIONAL_EDGE, SELECTED_SINGLE_NON_DIRECTIONAL_EDGE,
        SELECTED_MULTIPLE_EDGES, SELECTED_SINGLE_NODE, SELECTED_MULTIPLE_NODES, RUNNING_ALGORITHM, NOTHING_SELECTED
    }

    /**
     * Menu manager constructor
     *
     * @param buttons Hash map containing buttons
     * @param menus   Hash map containing menus
     */

    public MenuManager(App app, HashMap<Integer, JFXButton> buttons, HashMap<Integer, GridPane> menus, HBox expandingProblemsMenu) {
        /*
         * Set main app
         */
        this.app = app;

        /*
         * Menus
         */
        mainMenu = menus.get(0);
        problemsMenu = menus.get(1);
        toolsMenu = menus.get(2);
        modeMenu = menus.get(3);
        runtimeMenu = menus.get(4);
        this.expandingProblemsMenu = expandingProblemsMenu;

        /*
         * Problem buttons
         */
        shortestPathButton = buttons.get(0);
        travellingSalesmanButton = buttons.get(1);

        /*
         * Tool buttons
         */
        setSourceNodeButton = buttons.get(2);
        setTargetNodeButton = buttons.get(3);
        setWeightButton = buttons.get(4);
        setLabelButton = buttons.get(5);
        changeDirectionButton = buttons.get(6);
        removeButton = buttons.get(7);

        /*
         * Mode buttons
         */
        nodeButton = buttons.get(8);
        directionalEdgeButton = buttons.get(9);
        nonDirectionalEdgeButton = buttons.get(10);
        selectButton = buttons.get(11);

        /*
         * Travelling salesperson buttons
         */
        dynamicProgrammingButton = buttons.get(12);
        antColonyButton = buttons.get(13);

        /*
         * Set menu animations
         */
        setMenuAnimations();

        /*
         * Set mode buttons action listener
         */
        setMenuButtons();
    }

    /**
     * Set menu hide and show animations
     */

    private void setMenuAnimations() {
        /*
         * Hide menu animation
         */
        hideMenu = new TranslateTransition(Duration.millis(500), mainMenu);
        hideMenu.setToX(-220);

        /*
         * Show menu animation
         */
        showMenu = new TranslateTransition(Duration.millis(500), mainMenu);
        showMenu.setToX(0);

        /*
         * Hide runtime menu animation
         */
        hideRuntimeMenu = new TranslateTransition(Duration.millis(500), runtimeMenu);
        hideRuntimeMenu.setToY(-160);

        /*
         * Show runtime menu animation
         */
        showRuntimeMenu = new TranslateTransition(Duration.millis(500), runtimeMenu);
        showRuntimeMenu.setToY(15);

        /*
         * Hide menu expanding problems animation
         */
        RotateTransition rotateOutExpandingProblemsMenu = new RotateTransition(Duration.millis(500), expandingProblemsMenu);
        rotateOutExpandingProblemsMenu.setToAngle(0);
        TranslateTransition moveUpExpandingProblemsMenu = new TranslateTransition(Duration.millis(500), expandingProblemsMenu);
        moveUpExpandingProblemsMenu.setToY(-5);
        FadeTransition fadeOutExpandingProblemsMenu = new FadeTransition(Duration.millis(500), expandingProblemsMenu);
        fadeOutExpandingProblemsMenu.setToValue(0);
        hideExpandingProblemsMenu = new ParallelTransition(moveUpExpandingProblemsMenu, fadeOutExpandingProblemsMenu, rotateOutExpandingProblemsMenu);
        hideExpandingProblemsMenu.setOnFinished(event -> {
            expandingProblemsMenu.setVisible(false);
            app.expandingProblemsMenuVisible = false;
            expandingProblemsMenuTransitionRunning = false;
        });
        hideExpandingProblemsMenu.setInterpolator(Interpolator.EASE_OUT);

        /*
         * Show menu expanding problems animation
         */
        RotateTransition rotateInExpandingProblemsMenu = new RotateTransition(Duration.millis(500), expandingProblemsMenu);
        rotateInExpandingProblemsMenu.setToAngle(360);
        TranslateTransition moveDownExpandingProblemsMenu = new TranslateTransition(Duration.millis(500), expandingProblemsMenu);
        moveDownExpandingProblemsMenu.setToY(45);
        FadeTransition fadeInExpandingProblemsMenu = new FadeTransition(Duration.millis(500), expandingProblemsMenu);
        fadeInExpandingProblemsMenu.setToValue(100);
        showExpandingProblemsMenu = new ParallelTransition(moveDownExpandingProblemsMenu, fadeInExpandingProblemsMenu, rotateInExpandingProblemsMenu);
        showExpandingProblemsMenu.setInterpolator(Interpolator.EASE_OUT);
        showExpandingProblemsMenu.setOnFinished(event -> {
            app.expandingProblemsMenuVisible = true;
            expandingProblemsMenuTransitionRunning = false;
        });

    }

    /**
     * Show menu
     */

    public void showMenu() {
        hideMenu.stop();
        showMenu.play();
    }

    /**
     * Hide menu
     */

    public void hideMenu() {
        showMenu.stop();
        hideMenu.play();
    }

    /**
     * Show menu
     */

    public void showRuntimeMenu() {
        hideRuntimeMenu.stop();
        showRuntimeMenu.play();
    }

    /**
     * Hide menu
     */

    public void hideRuntimeMenu() {
        showRuntimeMenu.stop();
        hideRuntimeMenu.play();
    }

    /**
     * Enable necessary buttons
     *
     * @param buttonsList the list of the buttons to disable or enable
     */

    public void finalizeChangeButtons(HashMap<JFXButton, Boolean> buttonsList) {
        for (Map.Entry<JFXButton, Boolean> entry : buttonsList.entrySet()) {
            /*
             * Get button and boolean value
             */
            JFXButton button = entry.getKey();
            Boolean value = entry.getValue();

            /*
             * Disable if value is false and enable if value is true
             */
            button.setDisable(!value);
        }
    }

    /**
     * Change buttons status
     *
     * @param currentState the current state
     */

    public void updateButtons(State currentState) {
        HashMap<JFXButton, Boolean> map = new HashMap<>();

        switch (currentState) {
            case RUNNING_ALGORITHM:
                map.put(shortestPathButton, false);
                map.put(travellingSalesmanButton, false);
                map.put(setSourceNodeButton, false);
                map.put(setTargetNodeButton, false);
                map.put(setWeightButton, false);
                map.put(setLabelButton, false);
                map.put(changeDirectionButton, false);
                map.put(removeButton, false);
                map.put(nodeButton, false);
                map.put(directionalEdgeButton, false);
                map.put(nonDirectionalEdgeButton, false);
                map.put(selectButton, false);

                break;
            case SELECTED_SINGLE_DIRECTIONAL_EDGE:
                map.put(shortestPathButton, true);
                map.put(travellingSalesmanButton, true);
                map.put(setSourceNodeButton, false);
                map.put(setTargetNodeButton, false);
                map.put(setWeightButton, true);
                map.put(setLabelButton, false);
                map.put(changeDirectionButton, true);
                map.put(removeButton, true);
                map.put(nodeButton, true);
                map.put(directionalEdgeButton, true);
                map.put(nonDirectionalEdgeButton, true);
                map.put(selectButton, true);

                break;
            case SELECTED_SINGLE_NON_DIRECTIONAL_EDGE:
                map.put(shortestPathButton, true);
                map.put(travellingSalesmanButton, true);
                map.put(setSourceNodeButton, false);
                map.put(setTargetNodeButton, false);
                map.put(setWeightButton, true);
                map.put(setLabelButton, false);
                map.put(changeDirectionButton, false);
                map.put(removeButton, true);
                map.put(nodeButton, true);
                map.put(directionalEdgeButton, true);
                map.put(nonDirectionalEdgeButton, true);
                map.put(selectButton, true);

                break;
            case SELECTED_SINGLE_NODE:
                map.put(shortestPathButton, true);
                map.put(travellingSalesmanButton, true);
                map.put(setSourceNodeButton, true);
                map.put(setTargetNodeButton, app.getCurrentProblem() == App.Problem.SHORTEST_PATH);
                map.put(setWeightButton, false);
                map.put(setLabelButton, true);
                map.put(changeDirectionButton, false);
                map.put(removeButton, true);
                map.put(nodeButton, true);
                map.put(directionalEdgeButton, true);
                map.put(nonDirectionalEdgeButton, true);
                map.put(selectButton, true);

                break;
            case SELECTED_MULTIPLE_EDGES:
                map.put(shortestPathButton, true);
                map.put(travellingSalesmanButton, true);
                map.put(setSourceNodeButton, false);
                map.put(setTargetNodeButton, false);
                map.put(setWeightButton, true);
                map.put(setLabelButton, false);
                map.put(changeDirectionButton, false);
                map.put(removeButton, true);
                map.put(nodeButton, true);
                map.put(directionalEdgeButton, true);
                map.put(nonDirectionalEdgeButton, true);
                map.put(selectButton, true);

                break;
            case SELECTED_BOTH_NODES_AND_EDGES:
                map.put(shortestPathButton, true);
                map.put(travellingSalesmanButton, true);
                map.put(setSourceNodeButton, false);
                map.put(setTargetNodeButton, false);
                map.put(setWeightButton, false);
                map.put(setLabelButton, false);
                map.put(changeDirectionButton, false);
                map.put(removeButton, true);
                map.put(nodeButton, true);
                map.put(directionalEdgeButton, true);
                map.put(nonDirectionalEdgeButton, true);
                map.put(selectButton, true);

                break;
            case SELECTED_MULTIPLE_NODES:
                map.put(shortestPathButton, true);
                map.put(travellingSalesmanButton, true);
                map.put(setSourceNodeButton, false);
                map.put(setTargetNodeButton, false);
                map.put(setWeightButton, false);
                map.put(setLabelButton, false);
                map.put(changeDirectionButton, false);
                map.put(removeButton, true);
                map.put(nodeButton, true);
                map.put(directionalEdgeButton, true);
                map.put(nonDirectionalEdgeButton, true);
                map.put(selectButton, true);

                break;
            default:
                map.put(shortestPathButton, true);
                map.put(travellingSalesmanButton, true);
                map.put(setSourceNodeButton, false);
                map.put(setTargetNodeButton, false);
                map.put(setWeightButton, false);
                map.put(setLabelButton, false);
                map.put(changeDirectionButton, false);
                map.put(removeButton, true);
                map.put(nodeButton, true);
                map.put(directionalEdgeButton, true);
                map.put(nonDirectionalEdgeButton, true);
                map.put(selectButton, true);
                break;
        }

        /*
         * Finalize editing buttons layout
         */
        finalizeChangeButtons(map);
    }

    /**
     * Change app interaction mode
     *
     * @param newMode the new mode
     */

    public void changeMode(App.Mode newMode) {
        /*
         * Return if the desired new mode is already selected
         */
        if (app.getCurrentMode() == newMode) {
            return;
        }

        /*
         * Add menu buttons to a list
         */

        List<JFXButton> modeButtons = Arrays.asList(nodeButton, selectButton,
                directionalEdgeButton, nonDirectionalEdgeButton);

        switch (newMode) {
            case NODE:
                app.deselectAllModeButtons(modeButtons);
                app.selectModeButton(nodeButton);

                break;
            case DIRECTIONAL_EDGE:
                app.deselectAllModeButtons(modeButtons);
                app.selectModeButton(directionalEdgeButton);

                break;
            case NON_DIRECTIONAL_EDGE:
                app.deselectAllModeButtons(modeButtons);
                app.selectModeButton(nonDirectionalEdgeButton);

                break;
            default:
                /*
                 * Set the default mode to select mode
                 */
                app.deselectAllModeButtons(modeButtons);
                app.selectModeButton(selectButton);

                break;
        }
    }

    /**
     * Change app problem mode
     *
     * @param newProblem the new problem mode
     */

    public void changeProblem(App.Problem newProblem) {
        /*
         * Return if the desired new problem is already selected
         */
        if (app.getCurrentProblem() == newProblem) {
            return;
        }

        /*
         * Add menu buttons to a list
         */

        List<JFXButton> problemButtons = Arrays.asList(shortestPathButton, travellingSalesmanButton, dynamicProgrammingButton, antColonyButton);

        switch (newProblem) {
            case SHORTEST_PATH:
                app.deselectAllProblemButtons(problemButtons);
                app.selectProblemButton(shortestPathButton);

                break;
            case DYNAMIC_PROGRAMMING:
                app.deselectAllProblemButtons(problemButtons);
                app.selectProblemButton(travellingSalesmanButton);
                app.selectProblemButton(dynamicProgrammingButton);

                break;
            case ANT_COLONY:
                app.deselectAllProblemButtons(problemButtons);
                app.selectProblemButton(travellingSalesmanButton);
                app.selectProblemButton(antColonyButton);

                break;
            default:
//                app.deselectAllProblemButtons(problemButton);
//                app.selectProblemButton(selectButton);

                break;
        }
    }

    /**
     * Sets menu buttons actions
     */

    public void setMenuButtons() {
        shortestPathButton.setOnAction(event -> app.changeProblem(App.Problem.SHORTEST_PATH));
//        travellingSalesmanButton.setOnAction(event -> app.changeProblem(App.Problem.DYNAMIC_PROGRAMMING));
        travellingSalesmanButton.setOnAction(event -> app.showExpandingProblemsMenu());
        dynamicProgrammingButton.setOnAction(event -> app.changeProblem(App.Problem.DYNAMIC_PROGRAMMING));
        antColonyButton.setOnAction(event -> app.changeProblem(App.Problem.ANT_COLONY));
        setSourceNodeButton.setOnAction(event -> app.setSourceNode(app.selectionManager.getNode()));
        setTargetNodeButton.setOnAction(event -> app.setTargetNode(app.selectionManager.getNode()));
        setLabelButton.setOnAction(event -> app.setLabel(app.selectionManager.getNode()));
        setWeightButton.setOnAction(event -> app.setWeight());
        removeButton.setOnAction(event -> app.deleteItems());
        changeDirectionButton.setOnAction(event -> app.invertEdgeDirection(app.selectionManager.getEdge()));
        selectButton.setOnAction(event -> app.changeMode(App.Mode.SELECT));
        nodeButton.setOnAction(event -> app.changeMode(App.Mode.NODE));
        directionalEdgeButton.setOnAction(event -> app.changeMode(App.Mode.DIRECTIONAL_EDGE));
        nonDirectionalEdgeButton.setOnAction(event -> app.changeMode(App.Mode.NON_DIRECTIONAL_EDGE));
    }

    /**
     * Show expanding problems menu
     */

    public void showExpandingProblemsMenu() {
        hideExpandingProblemsMenu.stop();
        showExpandingProblemsMenu.play();
        expandingProblemsMenuTransitionRunning = true;
    }

    /**
     * Hide expanding problems menu
     */

    public void hideExpandingProblemsMenu() {
        showExpandingProblemsMenu.stop();
        hideExpandingProblemsMenu.play();
        expandingProblemsMenuTransitionRunning = true;
    }

    /**
     * Is expanding problems menu transition running boolean
     *
     * @return the boolean
     */

    public boolean isExpandingProblemsMenuTransitionRunning() {
        return this.expandingProblemsMenuTransitionRunning;
    }

    /**
     * Menu on top
     */

    public void menuOnTop() {
        mainMenu.toFront();
        runtimeMenu.toFront();
    }

    /**
     * Finalize on top.
     *
     * @param onTop the on top
     */

    public void finalizeOnTop(ArrayList<Node> onTop) {
        for (Node node : onTop) {
            node.toFront();
        }
    }
}
