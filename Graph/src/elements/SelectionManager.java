package elements;

import graph.App;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.Iterator;

public class SelectionManager {
    private MenuManager menuManager;
    private App app;

    /**
     * Selection array lists
     */
    private ArrayList<GraphNode> selectedNodes = new ArrayList<>();
    private ArrayList<GraphEdge> selectedEdges = new ArrayList<>();
    private ArrayList<NonDirectionalEdge> selectedNonDirectionalEdges = new ArrayList<>();

    /**
     * Instantiates a new Selection manager.
     *
     * @param menuManager the menu manager
     */
    public SelectionManager(MenuManager menuManager, App app) {
        this.menuManager = menuManager;
        this.app = app;
    }

    /**
     * Select item
     *
     * @param item the item for selection
     */

    public void select(Node item) {
        Class itemClass = item.getClass();

        if (itemClass == GraphNode.class) {
            //////////////////////////////////
            /////////      NODE      /////////
            //////////////////////////////////

            GraphNode node = (GraphNode) item;

            if (!app.isShiftPressed()) {
                /*
                 * First deselect everything
                 */
                if (nodesNumber() == 1 && getNode() != item) {
                    deselectAll();
                }
                if (nodesNumber() > 1) {
                    deselectAll();
                }

                /*
                 * Then select item
                 */
                selectNode(node);
            } else {
                if (selectedNodes.contains(item)) {
                    /*
                     * Deselect node if already selected
                     */
                    deselectNode(node);
                } else {
                    /*
                     * Select node if already not selected
                     */
                    selectNode(node);
                }
            }
        } else if (itemClass == GraphEdge.class) {
            //////////////////////////////////
            /////////      EDGE      /////////
            //////////////////////////////////

            GraphEdge edge = (GraphEdge) item;

            if (!app.isShiftPressed()) {
                /*
                 * First deselect everything
                 */
                deselectAll();

                /*
                 * Then select edge
                 */
                selectEdge(edge);
            } else {
                if (selectedEdges.contains(item)) {
                    /*
                     * Deselect edge if already selected
                     */
                    deselectEdge(edge);
                } else {
                    /*
                     * Select edge if not already selected
                     */
                    selectEdge(edge);
                }
            }
        } else if (itemClass == NonDirectionalEdge.class) {
            //////////////////////////////////////
            ///////      NON-DIR EDGE      ///////
            //////////////////////////////////////

            NonDirectionalEdge edge = (NonDirectionalEdge) item;

            if (!app.isShiftPressed()) {
                /*
                 * First deselect everything
                 */
                deselectAll();

                /*
                 * Then select edge
                 */
                selectNonDirectionalEdge(edge);
            } else {
                if (selectedNonDirectionalEdges.contains(item)) {
                    /*
                     * Deselect edge if already selected
                     */
                    deselectNonDirectionalEdge(edge);
                } else {
                    /*
                     * Select edge if not already selected
                     */
                    selectNonDirectionalEdge(edge);
                }
            }
        }

        /*
         * Update menu after selection done
         */
        updateMenu();
    }

    /**
     * Select node
     */
    private void selectNode(GraphNode node) {
        node.selectNode();

        if (!selectedNodes.contains(node)) {
            selectedNodes.add(node);
        }
    }

    /**
     * Deselect node
     *
     * @param node Node to deselect
     */
    private void deselectNode(GraphNode node, boolean remove) {
        node.deselectNode();

        if (remove) {
            selectedNodes.remove(node);
        }
    }

    /**
     * Deselect node
     *
     * @param node Node to deselect
     */
    private void deselectNode(GraphNode node) {
        node.deselectNode();
        selectedNodes.remove(node);
    }

    /**
     * Select edge
     *
     * @param edge the edge
     */
    private void selectEdge(GraphEdge edge) {
        edge.selectEdge();

        if (!selectedEdges.contains(edge)) {
            selectedEdges.add(edge);
        }
    }

    /**
     * Deselect edge
     *
     * @param edge the edge
     */
    private void deselectEdge(GraphEdge edge) {
        edge.deselectEdge();
        selectedEdges.remove(edge);
    }

    /**
     * Select non-directional edge
     *
     * @param edge the edge
     */
    private void selectNonDirectionalEdge(NonDirectionalEdge edge) {
        edge.selectEdge();

        if (!selectedNonDirectionalEdges.contains(edge)) {
            selectedNonDirectionalEdges.add(edge);
        }
    }

    /**
     * Deselect non directional edge.
     *
     * @param edge the edge
     */
    private void deselectNonDirectionalEdge(NonDirectionalEdge edge) {
        edge.deselectEdge();
        selectedNonDirectionalEdges.remove(edge);
    }

    /**
     * Deselect everything
     */
    public void deselectAll() {
        /*
         * Deselect all nodes
         */
//        for (GraphNode node : selectedNodes) {
//            deselectNode(node);
//        }

        for (Iterator<GraphNode> iterator = selectedNodes.iterator(); iterator.hasNext(); ) {
            GraphNode node = iterator.next();
            deselectNode(node, false);
            iterator.remove();
        }

        /*
         * Deselect all edges
         */
        for (GraphEdge edge : selectedEdges) {
            deselectEdge(edge);
        }

        /*
         * Deselect all non-directional edges
         */
        for (NonDirectionalEdge edge : selectedNonDirectionalEdges) {
            deselectNonDirectionalEdge(edge);
        }

        /*
         * Update menu
         */
        updateMenu();
    }

    /**
     * Update menu
     */
    private void updateMenu() {
        int nodesNumber = nodesNumber(), edgesNumber = edgesNumber(),
                nonDirEdgesNumber = nonDirectionalEdgesNumber();

        if (edgesNumber == 0) {
            if (nonDirEdgesNumber == 0) {
                /*
                 * Edges = 0 and Non-dir Edges = 0
                 */
                if (nodesNumber == 0) {
                    menuManager.updateButtons(MenuManager.State.NOTHING_SELECTED);
                } else if (nodesNumber == 1) {
                    menuManager.updateButtons(MenuManager.State.SELECTED_SINGLE_NODE);
                } else if (nodesNumber > 1) {
                    menuManager.updateButtons(MenuManager.State.SELECTED_MULTIPLE_NODES);
                }
            } else if (nodesNumber == 0) {
                /*
                 * Edges = 0 and Nodes = 0
                 */
                if (nonDirEdgesNumber == 1) {
                    menuManager.updateButtons(MenuManager.State.SELECTED_SINGLE_NON_DIRECTIONAL_EDGE);
                } else if (nonDirEdgesNumber > 1) {
                    menuManager.updateButtons(MenuManager.State.SELECTED_MULTIPLE_EDGES);
                }
            } else {
                /*
                 * Edges = 0 and Multiple Nodes and Non-dir Edges
                 */

                if (nonDirEdgesNumber >= 1 && nodesNumber >= 1) {
                    menuManager.updateButtons(MenuManager.State.SELECTED_BOTH_NODES_AND_EDGES);
                }
            }
        } else if (nodesNumber == 0) {
            /*
             * Nodes = 0 and Non-dir Edges = 0
             */
            if (nonDirEdgesNumber == 0) {
                if (edgesNumber == 1) {
                    /*
                     * SELECTED SINGLE EDGE
                     */
                    menuManager.updateButtons(MenuManager.State.SELECTED_SINGLE_DIRECTIONAL_EDGE);
                } else if (edgesNumber > 1) {
                    /*
                     * SELECTED MULTIPLE EDGES
                     */
                    menuManager.updateButtons(MenuManager.State.SELECTED_MULTIPLE_EDGES);
                }
            }
        } else if (nonDirEdgesNumber == 0) {
            if (edgesNumber >= 1 && nodesNumber >= 1) {
                /*
                 * SELECTED BOTH NODES AND EDGES
                 */
                menuManager.updateButtons(MenuManager.State.SELECTED_BOTH_NODES_AND_EDGES);
            }
        } else if (nonDirEdgesNumber >= 1 && edgesNumber >= 1 && nodesNumber >= 1) {
            /*
             * SELECTED BOTH NODES AND EDGES
             */
            menuManager.updateButtons(MenuManager.State.SELECTED_BOTH_NODES_AND_EDGES);
        }
    }

    /**
     * Gets selected node
     *
     * @return the selected node
     */

    public GraphNode getNode() {
        return selectedNodes.get(0);
    }

    /**
     * Gets nodes array list
     *
     * @return the nodes
     */

    public ArrayList<GraphNode> getNodes() {
        return selectedNodes;
    }

    /**
     * Gets selected edge
     *
     * @return the selected edge
     */

    public GraphEdge getEdge() {
        return selectedEdges.get(0);
    }

    /**
     * Gets edges array list
     *
     * @return the edges
     */

    public ArrayList<GraphEdge> getEdges() {
        return selectedEdges;
    }

    /**
     * Gets non-directional edge
     *
     * @return the non directional edge
     */

    public NonDirectionalEdge getNonDirectionalEdge() {
        return selectedNonDirectionalEdges.get(0);
    }

    /**
     * Gets selected non-directional edges list
     *
     * @return the selected non directional edges list
     */

    public ArrayList<NonDirectionalEdge> getNonDirectionalEdgesList() {
        return selectedNonDirectionalEdges;
    }

    /**
     * Nodes number
     *
     * @return the int
     */
    private int nodesNumber() {
        return selectedNodes.size();
    }

    /**
     * Edges number
     *
     * @return the int
     */
    private int edgesNumber() {
        return selectedEdges.size();
    }

    /**
     * Non directional edges number
     *
     * @return the int
     */

    private int nonDirectionalEdgesNumber() {
        return selectedNonDirectionalEdges.size();
    }
}
