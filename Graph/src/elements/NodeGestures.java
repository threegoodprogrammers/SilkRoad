package elements;

import graph.App;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class NodeGestures {
    private DragContext nodeDragContext = new DragContext();
    private PannableCanvas canvas;
    private App app;
    private Graph mainGraph;

    private boolean drawingNonDirectionalEdge = false;

    public NodeGestures(PannableCanvas canvas, App app, Graph mainGraph) {
        this.canvas = canvas;
        this.app = app;
        this.mainGraph = mainGraph;
    }

    public EventHandler<MouseEvent> getOnMousePressedEventHandler() {
        return onMousePressedEventHandler;
    }

    public EventHandler<MouseEvent> getOnMouseDraggedEventHandler() {
        return onMouseDraggedEventHandler;
    }

    public EventHandler<MouseEvent> getOnMouseReleasedEventHandler() {
        return onMouseReleasedEventHandler;
    }

    private EventHandler<MouseEvent> onMousePressedEventHandler = event -> {
        // left mouse button => dragging
        if (!event.isPrimaryButtonDown() || app.isCtrlPressed())
            return;

        GraphNode node = (GraphNode) event.getSource();

        switch (app.getCurrentMode()) {
            case NODE:

                break;
            case DIRECTIONAL_EDGE:
                /*
                 * Set current state to drawing edge state
                 */
                app.setCurrentState(App.State.DRAWING_EDGE);
                this.drawingNonDirectionalEdge = false;
                newEdge().setSourceNode(node);

                break;
            case NON_DIRECTIONAL_EDGE:
                /*
                 * Set current state to drawing edge state
                 */
                app.setCurrentState(App.State.DRAWING_EDGE);
                this.drawingNonDirectionalEdge = true;
                newEdge().setSourceNode(node);

                break;
            case SELECT:
            default:
                /*
                 * Set current state to moving node state
                 */
                app.setCurrentState(App.State.MOVING_NODE);

                break;
        }

        node.sendToFront();
    };

    private EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event) {
            if (!event.isPrimaryButtonDown() || app.isCtrlPressed())
                return;

            GraphNode node = (GraphNode) event.getSource();

            switch (app.getCurrentState()) {
                case DRAWING_EDGE:
                    app.showNewEdge(drawingNonDirectionalEdge);
                    GraphNode nodeOnMouse = app.findNodeOnMouse(event);
                    node.hoverNode(false);

                    if (nodeOnMouse != null && nodeOnMouse != node) {
                        attachNewEdgeToNode(nodeOnMouse);
                        nodeOnMouse.hoverNode(false);

                    } else {
                        GraphNode lastNode = newEdge().getTargetNode();

                        if (lastNode != newNode()) {
                            lastNode.leaveNode(false);
                        }

                        detachNewEdge();
                        app.moveHiddenNode(event.getSceneX(), event.getSceneY());
                    }

                    /*
                     * Update new edge
                     */
                    app.moveNewEdge();

                    break;
                case MOVING_NODE:
                    /*
                     * Hide menu and move node
                     */
                    app.hideMenu();
                    app.moveNode(node, event);

                    break;
                case IDLE:
                default:

                    break;
            }

            event.consume();
        }
    };

    private EventHandler<MouseEvent> onMouseReleasedEventHandler = new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event) {
//            if (!event.isPrimaryButtonDown() || app.isCtrlPressed())
//                return;

            switch (app.getCurrentState()) {
                case DRAWING_EDGE:
                    switch (app.getCurrentMode()) {
                        case DIRECTIONAL_EDGE:
                            if (newEdge().getTargetNode() != newNode()) {
                                app.addNewEdge(newEdge().getSourceNode(), newEdge().getTargetNode(), false);
                                detachNewEdge();
                            }

                            break;
                        case NON_DIRECTIONAL_EDGE:
                            if (newEdge().getTargetNode() != newNode()) {
                                app.addNewEdge(newEdge().getSourceNode(), newEdge().getTargetNode(), true);
                                detachNewEdge();
                            }

                            break;
                        default:
                            break;
                    }

                    app.hideNewEdge();
                    newEdge().getSourceNode().leaveNode(false);

                    break;
                case MOVING_NODE:


                    break;
                case IDLE:
                default:

                    break;
            }

            /*
             * Show menu
             */
            app.showMenu();

            /*
             * Set the current state to idle
             */
            app.setCurrentState(App.State.IDLE);

            event.consume();
        }
    };

    private EventHandler<MouseEvent> onMouseEnteredEventHandler = new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event) {

        }
    };

    private EventHandler<MouseEvent> onMouseLeavedEventHandler = new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event) {

        }
    };

    /**
     * Attach new edge to a specific node
     *
     * @param targetNode Target node to attach new edge to
     */

    private void attachNewEdgeToNode(GraphNode targetNode) {
        if (newEdge().getTargetNode() != targetNode) {
            newEdge().setTargetNode(targetNode);
        }
    }

    /**
     * Detach the new edge from previously set node
     */

    private void detachNewEdge() {
        if (newEdge().getTargetNode() != newNode()) {
            newEdge().setTargetNode(newNode());
        }
    }

    /**
     * New node graph node
     *
     * @return the graph node
     */
    public GraphNode newNode() {
        return app.hiddenNode;
    }

    /**
     * New edge graph edge.
     *
     * @return the graph edge
     */
    public GraphEdge newEdge() {
        return app.newEdge;
    }

}
