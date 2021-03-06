package elements;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import graph.App;
import javafx.animation.*;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private GridPane antColonyMenu;
    private HBox expandingProblemsMenu;

    /**
     * Loading elements
     */

    public FontAwesomeIconView algorithmSuccessIcon;
    public FontAwesomeIconView algorithmFailIcon;
    public StackPane loadingStack;
    public GridPane loading;

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
     * Ant colony menu elements
     */

    private JFXButton antsCountButton;
    private JFXButton alphaButton;
    private JFXButton betaButton;
    private JFXButton thresholdButton;
    private JFXButton vaporButton;
    private JFXButton randomFactorButton;

    /**
     * Ant colony menu elements
     */

    private JFXButton playButton;
    private JFXButton stopButton;
    private JFXButton speedUpButton;
    private JFXButton speedDownButton;

    /**
     * Runtime menu labels and titles
     */

    private Label timerValue;
    private Label timeTitle;
    private Label distanceValue;
    private Label distanceTitle;

    /**
     * Menu hide and show animations
     */

    private TranslateTransition hideMenu;
    private TranslateTransition showMenu;
    private FadeTransition fadeInLoading;
    private FadeTransition fadeOutLoading;
    private ParallelTransition showSuccessIcon;
    private ParallelTransition hideSuccessIcon;
    private ParallelTransition showFailIcon;
    private ParallelTransition hideFailIcon;
    private TranslateTransition hideRuntimeMenu;
    private TranslateTransition showRuntimeMenu;
    private ParallelTransition shrinkRuntimeMenu;
    private ParallelTransition expandRuntimeMenu;
    private TranslateTransition hideAntColonyMenu;
    private TranslateTransition showAntColonyMenu;
    private ParallelTransition hideExpandingProblemsMenu;
    private ParallelTransition showExpandingProblemsMenu;
    private ParallelTransition showTime;
    private ParallelTransition showDistance;
    private boolean expandingProblemsMenuTransitionRunning = false;

    public enum State {
        SELECTED_BOTH_NODES_AND_EDGES, SELECTED_SINGLE_DIRECTIONAL_EDGE, SELECTED_SINGLE_NON_DIRECTIONAL_EDGE,
        SELECTED_MULTIPLE_EDGES, SELECTED_SINGLE_NODE, SELECTED_MULTIPLE_NODES, RUNNING_ALGORITHM, PLAYING, FINISHED_PLAYING, NOTHING_SELECTED
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
        antColonyMenu = menus.get(5);
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
         * Ant colony buttons
         */
        antsCountButton = buttons.get(14);
        alphaButton = buttons.get(15);
        betaButton = buttons.get(16);
        thresholdButton = buttons.get(17);
        vaporButton = buttons.get(22);
        randomFactorButton = buttons.get(23);

        /*
         * Runtime menu buttons
         */
        playButton = buttons.get(18);
        stopButton = buttons.get(19);
        speedUpButton = buttons.get(20);
        speedDownButton = buttons.get(21);

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
     * Sets loading
     */

    public void setLoading(GridPane loading, StackPane loadingStack, FontAwesomeIconView algorithmSuccessIcon, FontAwesomeIconView algorithmFailIcon) {
        this.loading = loading;
        this.loadingStack = loadingStack;
        this.algorithmSuccessIcon = algorithmSuccessIcon;
        this.algorithmFailIcon = algorithmFailIcon;

        /*
         * Set loading animations
         */
        setLoadingAnimations();
    }

    /**
     * Sets runtime labels
     */

    public void setRuntimeLabels(Label timeTitle, Label timerValue, Label distanceTitle, Label distanceValue) {
        /*
         * Set the labels
         */
        this.timeTitle = timeTitle;
        this.timerValue = timerValue;
        this.distanceTitle = distanceTitle;
        this.distanceValue = distanceValue;

        /*s
         * Set the animations for the labels
         */
        setRuntimeLabelsAnimations();
    }

    /**
     * Sets runtime labels animations
     */

    public void setRuntimeLabelsAnimations() {
        /*
         * Initialize time animations
         */
        FadeTransition fadeInTimeTitle = new FadeTransition(Duration.millis(1000), this.timeTitle);
        FadeTransition fadeOutTimeTitle = new FadeTransition(Duration.millis(1000), this.timeTitle);
        FadeTransition fadeInTimerValue = new FadeTransition(Duration.millis(1000), this.timerValue);
        FadeTransition fadeOutTimerValue = new FadeTransition(Duration.millis(1000), this.timerValue);

        /*
         * Initialize distance animations
         */
        FadeTransition fadeInDistanceTitle = new FadeTransition(Duration.millis(1000), this.distanceTitle);
        FadeTransition fadeInDistanceValue = new FadeTransition(Duration.millis(1000), this.distanceValue);
        FadeTransition fadeOutDistanceTitle = new FadeTransition(Duration.millis(1000), this.distanceTitle);
        FadeTransition fadeOutDistanceValue = new FadeTransition(Duration.millis(1000), this.distanceValue);

        /*
         * Set time animations
         */
        fadeInTimeTitle.setToValue(1);
        fadeOutTimeTitle.setToValue(0);
        fadeInTimerValue.setToValue(1);
        fadeOutTimerValue.setToValue(0);

        /*
         * Set distance animations
         */
        fadeInDistanceTitle.setToValue(1);
        fadeOutDistanceTitle.setToValue(0);
        fadeInDistanceValue.setToValue(1);
        fadeOutDistanceValue.setToValue(0);

        /*
         * Set the parallel animations
         */
        this.showTime = new ParallelTransition(fadeOutDistanceTitle, fadeOutDistanceValue,
                fadeInTimeTitle, fadeInTimerValue);
        this.showDistance = new ParallelTransition(fadeOutTimeTitle, fadeOutTimerValue,
                fadeInDistanceTitle, fadeInDistanceValue);
    }

    /**
     * Show time
     */

    public void showTime() {
        showDistance.stop();
        showTime.play();
    }

    /**
     * Show distance
     */

    public void showDistance() {
        showTime.stop();
        showDistance.play();
    }

    /**
     * Stop runtime labels animations
     */

    public void stopRuntimeLabelsAnimations() {
        showTime.stop();
        showDistance.stop();
    }

    /**
     * Sets loading animations
     */

    public void setLoadingAnimations() {
        /*
         * Fade in loading animation
         */
        fadeInLoading = new FadeTransition(Duration.millis(500), loading);
        fadeInLoading.setToValue(1);

        /*
         * Fade out loading animation
         */
        fadeOutLoading = new FadeTransition(Duration.millis(500), loading);
        fadeOutLoading.setToValue(0);
        fadeOutLoading.setOnFinished(event -> {
            loading.setVisible(false);
            loading.toBack();
        });

        /*
         * Scale in success icon animation
         */
        ScaleTransition scaleInSuccessIcon = new ScaleTransition(Duration.millis(500), algorithmSuccessIcon);
        scaleInSuccessIcon.setToX(1);
        scaleInSuccessIcon.setToY(1);

        /*
         * Scale out success icon animation
         */
        ScaleTransition scaleOutSuccessIcon = new ScaleTransition(Duration.millis(500), algorithmSuccessIcon);
        scaleOutSuccessIcon.setToX(0);
        scaleOutSuccessIcon.setToY(0);

        /*
         * Rotate in success icon animation
         */
        RotateTransition rotateInSuccessIcon = new RotateTransition(Duration.millis(500), algorithmSuccessIcon);
        rotateInSuccessIcon.setToAngle(360);

        /*
         * Rotate out success icon animation
         */
        RotateTransition rotateOutSuccessIcon = new RotateTransition(Duration.millis(500), algorithmSuccessIcon);
        rotateOutSuccessIcon.setToAngle(0);

        /*
         * Mix success icon show transitions
         */
        showSuccessIcon = new ParallelTransition(scaleInSuccessIcon, rotateInSuccessIcon);
        showSuccessIcon.setOnFinished(event -> {
            PauseTransition wait = new PauseTransition(Duration.millis(500));
            wait.setOnFinished(event1 -> hideSuccessIcon());
            wait.play();
        });

        /*
         * Mix success icon hide transitions
         */
        hideSuccessIcon = new ParallelTransition(rotateOutSuccessIcon, scaleOutSuccessIcon);
        hideSuccessIcon.setOnFinished(event -> algorithmSuccessIcon.setVisible(false));

        /*
         * Scale in fail icon animation
         */
        ScaleTransition scaleInFailIcon = new ScaleTransition(Duration.millis(500), algorithmFailIcon);
        scaleInFailIcon.setToX(1);
        scaleInFailIcon.setToY(1);

        /*
         * Scale out fail icon animation
         */
        ScaleTransition scaleOutFailIcon = new ScaleTransition(Duration.millis(500), algorithmFailIcon);
        scaleOutFailIcon.setToX(0);
        scaleOutFailIcon.setToY(0);

        /*
         * Rotate in fail icon animation
         */
        RotateTransition rotateInFailIcon = new RotateTransition(Duration.millis(500), algorithmFailIcon);
        rotateInFailIcon.setToAngle(360);

        /*
         * Rotate out fail icon animation
         */
        RotateTransition rotateOutFailIcon = new RotateTransition(Duration.millis(500), algorithmFailIcon);
        rotateOutFailIcon.setToAngle(0);

        /*
         * Mix fail icon show transitions
         */
        showFailIcon = new ParallelTransition(rotateInFailIcon, scaleInFailIcon);
        showFailIcon.setOnFinished(event -> {
            PauseTransition wait = new PauseTransition(Duration.millis(500));
            wait.setOnFinished(event1 -> hideFailIcon());
            wait.play();
        });

        /*
         * Mix fail icon hide transitions
         */
        hideFailIcon = new ParallelTransition(rotateOutFailIcon, scaleOutFailIcon);
        hideFailIcon.setOnFinished(event -> algorithmFailIcon.setVisible(false));

        /*
         * Set shrink and expand runtime menu animations
         */
        TranslateTransition pullUpRuntimeMenu = new TranslateTransition(Duration.millis(500), runtimeMenu);
        pullUpRuntimeMenu.setToY(-120);
        TranslateTransition pullDownRuntimeMenu = new TranslateTransition(Duration.millis(500), runtimeMenu);
        pullDownRuntimeMenu.setToY(15);
        FadeTransition fadeInRuntimeMenu = new FadeTransition(Duration.millis(500), runtimeMenu);
        fadeInRuntimeMenu.setToValue(1);
        FadeTransition fadeOutRuntimeMenu = new FadeTransition(Duration.millis(500), runtimeMenu);
        fadeOutRuntimeMenu.setToValue(.5);

        /*
         * Set parallel transitions
         */
        this.shrinkRuntimeMenu = new ParallelTransition(pullUpRuntimeMenu, fadeOutRuntimeMenu);
        this.expandRuntimeMenu = new ParallelTransition(pullDownRuntimeMenu, fadeInRuntimeMenu);
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
         * Hide ant colony menu animation
         */
        hideAntColonyMenu = new TranslateTransition(Duration.millis(500), antColonyMenu);
        hideAntColonyMenu.setToX(220);

        /*
         * Show ant colony menu animation
         */
        showAntColonyMenu = new TranslateTransition(Duration.millis(500), antColonyMenu);
        showAntColonyMenu.setToX(0);

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
     * Expand runtime menu
     */

    public void expandRuntimeMenu() {
        shrinkRuntimeMenu.stop();
        expandRuntimeMenu.play();
    }

    /**
     * Shrink runtime menu
     */

    public void shrinkRuntimeMenu() {
        expandRuntimeMenu.stop();
        shrinkRuntimeMenu.play();
    }

    /**
     * Show menu
     */

    public void showAntColonyMenu() {
        hideAntColonyMenu.stop();
        showAntColonyMenu.play();
    }

    /**
     * Hide menu
     */

    public void hideAntColonyMenu() {
        showAntColonyMenu.stop();
        hideAntColonyMenu.play();
    }

    /**
     * Show loading
     */

    public void showLoading() {
        loadingStack.setVisible(true);
        loading.setVisible(true);
        loading.toFront();
        runtimeMenu.toFront();
        fadeOutLoading.stop();
        fadeInLoading.play();
    }

    /**
     * Hide loading
     */

    public void hideLoading() {
        loadingStack.setVisible(false);
        fadeInLoading.stop();
        fadeOutLoading.play();
    }

    /**
     * Show success icon
     */

    public void showSuccessIcon() {
        loadingStack.setVisible(false);
        algorithmSuccessIcon.setVisible(true);
        hideSuccessIcon.stop();
        showSuccessIcon.play();
    }

    /**
     * Hide success icon
     */

    public void hideSuccessIcon() {
        showSuccessIcon.stop();
        hideSuccessIcon.play();
    }

    /**
     * Show fail icon
     */

    public void showFailIcon() {
        loadingStack.setVisible(false);
        algorithmFailIcon.setVisible(true);
        hideFailIcon.stop();
        showFailIcon.play();
    }

    /**
     * Hide success icon
     */

    public void hideFailIcon() {
        showFailIcon.stop();
        hideFailIcon.play();
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

        boolean directionEdgeEnabled = app.getCurrentProblem() == App.Problem.SHORTEST_PATH;
        boolean antColonyButtonsEnabled = app.getCurrentProblem() == App.Problem.ANT_COLONY;
        boolean speedUpEnabled = app.getSpeed() < 4;
        boolean speedDownEnabled = app.getSpeed() > 0.25;

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
                map.put(playButton, false);
                map.put(stopButton, true);
                map.put(speedUpButton, false);
                map.put(speedDownButton, false);
                map.put(antsCountButton, false);
                map.put(alphaButton, false);
                map.put(betaButton, false);
                map.put(thresholdButton, false);
                map.put(vaporButton, false);
                map.put(randomFactorButton, false);

                break;
            case PLAYING:
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
                map.put(playButton, true);
                map.put(stopButton, true);
                map.put(speedUpButton, speedUpEnabled);
                map.put(speedDownButton, speedDownEnabled);
                map.put(antsCountButton, false);
                map.put(alphaButton, false);
                map.put(betaButton, false);
                map.put(thresholdButton, false);
                map.put(vaporButton, false);
                map.put(randomFactorButton, false);

                break;
            case FINISHED_PLAYING:
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
                map.put(playButton, false);
                map.put(stopButton, true);
                map.put(speedUpButton, false);
                map.put(speedDownButton, false);
                map.put(antsCountButton, false);
                map.put(alphaButton, false);
                map.put(betaButton, false);
                map.put(thresholdButton, false);
                map.put(vaporButton, false);
                map.put(randomFactorButton, false);

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
                map.put(directionalEdgeButton, directionEdgeEnabled);
                map.put(nonDirectionalEdgeButton, true);
                map.put(selectButton, true);
                map.put(playButton, false);
                map.put(stopButton, false);
                map.put(speedUpButton, false);
                map.put(speedDownButton, false);
                map.put(antsCountButton, antColonyButtonsEnabled);
                map.put(alphaButton, antColonyButtonsEnabled);
                map.put(betaButton, antColonyButtonsEnabled);
                map.put(thresholdButton, antColonyButtonsEnabled);
                map.put(vaporButton, antColonyButtonsEnabled);
                map.put(randomFactorButton, antColonyButtonsEnabled);

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
                map.put(directionalEdgeButton, directionEdgeEnabled);
                map.put(nonDirectionalEdgeButton, true);
                map.put(selectButton, true);
                map.put(playButton, false);
                map.put(stopButton, false);
                map.put(speedUpButton, false);
                map.put(speedDownButton, false);
                map.put(antsCountButton, antColonyButtonsEnabled);
                map.put(alphaButton, antColonyButtonsEnabled);
                map.put(betaButton, antColonyButtonsEnabled);
                map.put(thresholdButton, antColonyButtonsEnabled);
                map.put(vaporButton, antColonyButtonsEnabled);
                map.put(randomFactorButton, antColonyButtonsEnabled);

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
                map.put(directionalEdgeButton, directionEdgeEnabled);
                map.put(nonDirectionalEdgeButton, true);
                map.put(selectButton, true);
                map.put(playButton, false);
                map.put(stopButton, false);
                map.put(speedUpButton, false);
                map.put(speedDownButton, false);
                map.put(antsCountButton, antColonyButtonsEnabled);
                map.put(alphaButton, antColonyButtonsEnabled);
                map.put(betaButton, antColonyButtonsEnabled);
                map.put(thresholdButton, antColonyButtonsEnabled);
                map.put(vaporButton, antColonyButtonsEnabled);
                map.put(randomFactorButton, antColonyButtonsEnabled);

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
                map.put(directionalEdgeButton, directionEdgeEnabled);
                map.put(nonDirectionalEdgeButton, true);
                map.put(selectButton, true);
                map.put(playButton, false);
                map.put(stopButton, false);
                map.put(speedUpButton, false);
                map.put(speedDownButton, false);
                map.put(antsCountButton, antColonyButtonsEnabled);
                map.put(alphaButton, antColonyButtonsEnabled);
                map.put(betaButton, antColonyButtonsEnabled);
                map.put(thresholdButton, antColonyButtonsEnabled);
                map.put(vaporButton, antColonyButtonsEnabled);
                map.put(randomFactorButton, antColonyButtonsEnabled);

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
                map.put(directionalEdgeButton, directionEdgeEnabled);
                map.put(nonDirectionalEdgeButton, true);
                map.put(selectButton, true);
                map.put(playButton, false);
                map.put(stopButton, false);
                map.put(speedUpButton, false);
                map.put(speedDownButton, false);
                map.put(antsCountButton, antColonyButtonsEnabled);
                map.put(alphaButton, antColonyButtonsEnabled);
                map.put(betaButton, antColonyButtonsEnabled);
                map.put(thresholdButton, antColonyButtonsEnabled);
                map.put(vaporButton, antColonyButtonsEnabled);
                map.put(randomFactorButton, antColonyButtonsEnabled);

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
                map.put(directionalEdgeButton, directionEdgeEnabled);
                map.put(nonDirectionalEdgeButton, true);
                map.put(selectButton, true);
                map.put(playButton, false);
                map.put(stopButton, false);
                map.put(speedUpButton, false);
                map.put(speedDownButton, false);
                map.put(antsCountButton, antColonyButtonsEnabled);
                map.put(alphaButton, antColonyButtonsEnabled);
                map.put(betaButton, antColonyButtonsEnabled);
                map.put(thresholdButton, antColonyButtonsEnabled);
                map.put(vaporButton, antColonyButtonsEnabled);
                map.put(randomFactorButton, antColonyButtonsEnabled);

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
                map.put(directionalEdgeButton, directionEdgeEnabled);
                map.put(nonDirectionalEdgeButton, true);
                map.put(selectButton, true);
                map.put(playButton, false);
                map.put(stopButton, false);
                map.put(speedUpButton, false);
                map.put(speedDownButton, false);
                map.put(antsCountButton, antColonyButtonsEnabled);
                map.put(alphaButton, antColonyButtonsEnabled);
                map.put(betaButton, antColonyButtonsEnabled);
                map.put(thresholdButton, antColonyButtonsEnabled);
                map.put(vaporButton, antColonyButtonsEnabled);
                map.put(randomFactorButton, antColonyButtonsEnabled);

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
        antsCountButton.setOnAction(event -> app.setAntsCount());
        alphaButton.setOnAction(event -> app.setAlpha());
        betaButton.setOnAction(event -> app.setBeta());
        thresholdButton.setOnAction(event -> app.setThreshold());
        vaporButton.setOnAction(event -> app.setVapor());
        randomFactorButton.setOnAction(event -> app.setRandomFactor());
        playButton.setOnAction(event -> app.pressPlay());
        stopButton.setOnAction(event -> app.pressStop());
        speedUpButton.setOnAction(event -> app.increaseSpeed());
        speedDownButton.setOnAction(event -> app.decreaseSpeed());
    }

    /**
     * Pause
     */

    public void pause() {
        FontAwesomeIconView icon = (FontAwesomeIconView) this.playButton.getGraphic();
        icon.setGlyphName("PLAY");
    }

    /**
     * Play
     */

    public void play() {
        FontAwesomeIconView icon = (FontAwesomeIconView) this.playButton.getGraphic();
        icon.setGlyphName("PAUSE");
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
        antColonyMenu.toFront();
    }
}
