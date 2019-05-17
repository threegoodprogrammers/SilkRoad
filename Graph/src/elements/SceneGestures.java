package elements;

import graph.App;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

public class SceneGestures {
    private static final double MAX_SCALE = 4.0d;
    private static final double MIN_SCALE = .4d;
    private static final double MIN_SCALE_FOR_BG = 1;
    private DragContext sceneDragContext = new DragContext();

    private PannableCanvas canvas;
    private PannableGridPane background;
    private App app;

    public SceneGestures(PannableCanvas canvas, App app, PannableGridPane background) {
        this.canvas = canvas;
        this.app = app;
        this.background = background;
    }

    public EventHandler<MouseEvent> getOnMousePressedEventHandler() {
        return onMousePressedEventHandler;
    }

    public EventHandler<MouseEvent> getOnMouseReleasedEventHandler() {
        return onMouseReleasedEventHandler;
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
                return;
            }

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

    private EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event) {
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
             * Move the main canvas
             */
            app.moveCanvas(sceneDragContext.translateAnchorX + event.getSceneX() - sceneDragContext.mouseAnchorX,
                    sceneDragContext.translateAnchorY + event.getSceneY() - sceneDragContext.mouseAnchorY);

            /*
             * Move pan icon
             */
            app.movePanIcon(event.getSceneX() - app.getPanIconBounds().getWidth() / 2 + 9,
                    event.getSceneY() - app.getPanIconBounds().getHeight() / 2 + 9);

            /*
             * Set the current state to Pan Mode
             */
            app.panMode();

            event.consume();
        }
    };

    private EventHandler<MouseEvent> onMouseReleasedEventHandler = new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event) {
            /*
             * Return if the left click is not released
             */
//            if (!event.isPrimaryButtonDown()) {
//                return;
//            }
//
            /*
             * Hide pan icon
             */
            app.hidePanIcon();

            /*
             * Set the current state to interact mode
             */
            app.interactMode();

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
                        app.panMode();
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
                    app.interactMode();

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
            canvas.setPivot(f * dx, f * dy);

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
}
