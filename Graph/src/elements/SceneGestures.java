package elements;

import graph.App;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

public class SceneGestures {
    private static final double MAX_SCALE = 4.0d;
    private static final double MIN_SCALE = .4d;
    private DragContext sceneDragContext = new DragContext();
    private double mouseX;
    private double mouseY;

    private PannableCanvas canvas;
    private PannableGridPane background;
    private App app;

    private boolean isPanning = false;

    public SceneGestures(PannableCanvas canvas, App app, PannableGridPane background) {
        this.canvas = canvas;
        this.app = app;
        this.background = background;
    }

    public EventHandler<MouseEvent> getOnMousePressedEventHandler() {
        return onMousePressedEventHandler;
    }

//    public EventHandler<MouseEvent> getOnMouseClickEventHandler() {
//        return onMouseClickedEventHandler;
//    }

    public EventHandler<MouseEvent> getOnMouseReleasedEventHandler() {
        return onMouseReleasedEventHandler;
    }

    public EventHandler<MouseEvent> getOnMouseMovedEventHandler() {
        return onMouseMovedEventHandler;
    }

    public EventHandler<MouseEvent> getOnMouseEnteredEventHandler() {
        return onMouseEnteredEventHandler;
    }

    public EventHandler<KeyEvent> getOnKeyPressedEventHandler() {
        return onKeyPressedEventHandler;
    }

    public EventHandler<KeyEvent> getOnKeyReleasedEventHandler() {
        return onKeyReleasedEventHandler;
    }

    public EventHandler<MouseEvent> getOnMouseDraggedEventHandler() {
        return onMouseDraggedEventHandler;
    }

    public EventHandler<ScrollEvent> getOnScrollEventHandler() {
        return onScrollEventHandler;
    }

    private EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event) {
            mouseX = event.getSceneX();
            mouseY = event.getSceneY();

            /*
             * Set co-ordinates of mouse click position
             */
            setSceneDragContext(event);

            /*
             * Check for expanding menu after mouse click
             */
            app.checkExpandingMenuStatusAfterClick();

            /*
             * Return if the left click is not pressed
             */
            if (!event.isPrimaryButtonDown() || app.isLeftClickPressed()) {
                return;
            }

            /*
             * Press left click
             */
            app.pressLeftClick();

            /*
             * Return if Control key is not pressed
             */
            if (!app.isCtrlPressed()) {
                if (!app.isAnythingHovered() && !app.isMenuHovered()) {
                    app.selectionManager.deselectAll();
                }
                return;
            }

            /*
             * Set is panning boolean to true
             */
            isPanning = true;

            /*
             * Hide menu
             */
            app.hideMenu();

            /*
             * Set co-ordinates of mouse click position
             */
            setSceneDragContext(event);

            /*
             * Show pan icon on the main scene
             */
            app.showPanIcon(event.getSceneX() - app.getPanIconBounds().getWidth() / 2 + 9,
                    event.getSceneY() - app.getPanIconBounds().getHeight() / 2 + 9);

            event.consume();
        }
    };

    private EventHandler<MouseEvent> onMouseMovedEventHandler = new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event) {
            mouseX = event.getSceneX();
            mouseY = event.getSceneY();

            if (app.getCurrentMode() == App.Mode.NODE && !app.isMenuHovered()) {
                moveNewNode(event);
                app.newNodeOnTop();
            }

        }
    };

    private EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event) {
            mouseX = event.getSceneX();
            mouseY = event.getSceneY();

            switch (app.getCurrentMode()) {
                case NODE:
                    if (app.isCtrlPressed() || app.isMenuHovered()) {
                        break;
                    }

                    moveNewNode(event);
                    app.newNodeOnTop();

                    return;
                case NON_DIRECTIONAL_EDGE:
                    if (app.isCtrlPressed()) {
                        break;
                    }

                    break;
                case DIRECTIONAL_EDGE:
                    if (app.isCtrlPressed()) {
                        break;
                    }

                    break;
                default:
                    if (app.isCtrlPressed()) {
                        break;
                    }

                    break;
            }

            /*
             * Return if the left click is not pressed
             */
            if (!event.isPrimaryButtonDown()) {
                return;
            }

            /*
             * Press left click
             */
            app.pressLeftClick();

            /*
             * Return if Control key is not pressed
             */
            if (!app.isCtrlPressed()) {
                return;
            }

            /*
             * Hide menu
             */
            app.hideMenu();

            /*
             * Move the main canvas
             */
            app.moveCanvas(sceneDragContext.translateAnchorX + mouseX - sceneDragContext.mouseAnchorX,
                    sceneDragContext.translateAnchorY + mouseY - sceneDragContext.mouseAnchorY);

            /*
             * Move pan icon
             */
            app.movePanIcon(mouseX - app.getPanIconBounds().getWidth() / 2 + 9,
                    mouseY - app.getPanIconBounds().getHeight() / 2 + 9);

            event.consume();
        }
    };


    private EventHandler<MouseEvent> onMouseEnteredEventHandler = new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event) {
            /*
             * Return if the left click is not pressed
             */
//            if (app.getCurrentMode() != App.Mode.NODE) {
//                return;
//            }

            /*
             * Move the main canvas
             */
//            setSceneDragContext(event);

//            event.consume();
        }
    };

    private EventHandler<MouseEvent> onMouseReleasedEventHandler = new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event) {
            switch (app.getCurrentMode()) {
                case NODE:
                    if (app.isCtrlPressed() || app.isMenuHovered()) {
                        break;
                    }

                    moveNewNode(event);
                    app.newNodeOnTop();

                    /*
                     * Place the new node is left click is pressed
                     */

                    if (app.isLeftClickPressed()) {
                        app.addNewNode(event);
                        app.newNodeOnTop();
                    }

                    break;

//                    return;
                case NON_DIRECTIONAL_EDGE:
                    if (app.isCtrlPressed()) {
                        break;
                    }

                    break;
                case DIRECTIONAL_EDGE:
                    if (app.isCtrlPressed()) {
                        break;
                    }

                    break;
                default:
                    if (app.isCtrlPressed()) {
                        break;
                    }

                    break;
            }

            if (isPanning) {
                /*
                 * Hide pan icon
                 */
                app.hidePanIcon();

                /*
                 * Show menu again
                 */
                app.showMenu();

                isPanning = false;
            }

            /*
             * Release left mouse click
             */
            app.releaseLeftClick();
        }
    };

    private EventHandler<KeyEvent> onKeyPressedEventHandler = new EventHandler<KeyEvent>() {
        public void handle(KeyEvent event) {
            switch (event.getCode()) {
                case CONTROL:
                    /*
                     * Press control key
                     */
                    if (!app.isLeftClickPressed() && !app.isShiftPressed()) {
                        app.pressCtrl();
                    }

                    break;
                case SHIFT:
                    /*
                     * Press shift key
                     */
                    if (!app.isLeftClickPressed() && !app.isCtrlPressed()) {
                        app.pressShift();
                    }

                    break;
                case ENTER:
                    /*
                     * Press enter key
                     */
                    app.pressEnter();

                    break;
                case DELETE:
                    /*
                     * Press enter key
                     */
                    app.deleteItems();

                    break;
                default:
                    break;
            }

            event.consume();
        }
    };

    private EventHandler<KeyEvent> onKeyReleasedEventHandler = new EventHandler<KeyEvent>() {
        public void handle(KeyEvent event) {
            switch (event.getCode()) {
                case CONTROL:
                    /*
                     * Release control key
                     */
                    app.releaseCtrl();
                    app.hidePanIcon();

                    break;
                case SHIFT:
                    /*
                     * Release shift key
                     */
                    app.releaseShift();

                    break;
                default:
                    break;
            }

            event.consume();
        }
    };

    /**
     * Mouse wheel handler: zoom to pivot point
     */

    private EventHandler<ScrollEvent> onScrollEventHandler = new EventHandler<ScrollEvent>() {
        @Override
        public void handle(ScrollEvent event) {

            double delta = 1.2;

            double scale = canvas.getScale(); // currently we only use Y, same value is used for X
            double oldScale = scale;

            if (event.getDeltaY() < 0)
                scale /= delta;
            else
                scale *= delta;

            scale = clamp(scale, MIN_SCALE, MAX_SCALE);

            double f = (scale / oldScale) - 1;

            double dx =
                    (event.getSceneX() - (canvas.getBoundsInParent().getWidth() / 2 + canvas.getBoundsInParent().getMinX()));
            double dy =
                    (event.getSceneY() - (canvas.getBoundsInParent().getHeight() / 2 + canvas.getBoundsInParent().getMinY()));

            canvas.setScale(scale);
            background.setScale(scale * 10 / 4);

            // note: pivot value must be untransformed, i. e. without scaling
//            canvas.setPivot(f * dx, f * dy);

            if (app.getCurrentMode() == App.Mode.NODE) {
                app.moveNewNode(mouseX, mouseY);
            }

            event.consume();
        }

    };

    public static double clamp(double value, double min, double max) {

        if (Double.compare(value, min) < 0)
            return min;

        if (Double.compare(value, max) > 0)
            return max;

        return value;
    }

    /**
     * Sets scene drag context.
     *
     * @param event the event
     */
    public void setSceneDragContext(MouseEvent event) {
        sceneDragContext.mouseAnchorX = event.getSceneX();
        sceneDragContext.mouseAnchorY = event.getSceneY();

        sceneDragContext.translateAnchorX = canvas.getTranslateX();
        sceneDragContext.translateAnchorY = canvas.getTranslateY();
    }

    /**
     * Move new node
     *
     * @param event Mouse event
     */

    private void moveNewNode(MouseEvent event) {
        app.moveNewNode(event.getSceneX(), event.getSceneY());
    }

    /**
     * Gets x.
     *
     * @return the x
     */

    public double getX() {
        return this.mouseX;
    }

    /**
     * Gets y.
     *
     * @return the y
     */

    public double getY() {
        return this.mouseY;
    }
}
