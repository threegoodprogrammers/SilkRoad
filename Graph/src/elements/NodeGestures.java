package elements;

import graph.App;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

public class NodeGestures {
    private DragContext nodeDragContext = new DragContext();
    private PannableCanvas canvas;
    private App app;

    public NodeGestures(PannableCanvas canvas, App app) {
        this.canvas = canvas;
        this.app = app;
    }

    public EventHandler<MouseEvent> getOnMousePressedEventHandler() {
        return onMousePressedEventHandler;
    }

    public EventHandler<MouseEvent> getOnMouseDraggedEventHandler() {
        return onMouseDraggedEventHandler;
    }

    private EventHandler<MouseEvent> onMousePressedEventHandler = event -> {
        // left mouse button => dragging
        if (!event.isPrimaryButtonDown() || app.getState() == App.State.PAN)
            return;

        nodeDragContext.mouseAnchorX = event.getSceneX();
        nodeDragContext.mouseAnchorY = event.getSceneY();

        Node node = (Node) event.getSource();

        nodeDragContext.translateAnchorX = node.getTranslateX();
        nodeDragContext.translateAnchorY = node.getTranslateY();

    };

    private EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event) {
            // left mouse button => dragging
            if (!event.isPrimaryButtonDown() || app.getState() == App.State.PAN)
                return;

            double scale = canvas.getScale();

            Node node = (Node) event.getSource();

            node.setTranslateX(nodeDragContext.translateAnchorX + ((event.getSceneX() - nodeDragContext.mouseAnchorX) / scale));
            node.setTranslateY(nodeDragContext.translateAnchorY + ((event.getSceneY() - nodeDragContext.mouseAnchorY) / scale));

            event.consume();
        }
    };
}