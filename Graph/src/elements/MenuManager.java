package elements;

import com.jfoenix.controls.JFXButton;
import graph.App;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
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


    /**
     * Problems menu buttons
     */

    private JFXButton shortestPathButton;
    private JFXButton travellingSalesmanButton;

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

    public MenuManager(App app, HashMap<Integer, JFXButton> buttons, HashMap<Integer, GridPane> menus) {
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

        List<JFXButton> problemButton = Arrays.asList(shortestPathButton, travellingSalesmanButton);

        switch (newProblem) {
            case SHORTEST_PATH:
                app.deselectAllProblemButtons(problemButton);
                app.selectProblemButton(shortestPathButton);

                break;
            case TRAVELLING_SALESMAN:
                app.deselectAllProblemButtons(problemButton);
                app.selectProblemButton(travellingSalesmanButton);

                break;
            case ANT_COLONY:
//                app.deselectAllProblemButtons(problemButton);
//                app.selectProblemButton(nonDirectionalEdgeButton);

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
        travellingSalesmanButton.setOnAction(event -> app.changeProblem(App.Problem.TRAVELLING_SALESMAN));
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
     * Menu on top
     */

    public void menuOnTop() {
        mainMenu.toFront();
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
