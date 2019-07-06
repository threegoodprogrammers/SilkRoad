package elements;

import java.util.ArrayList;

public class SelectionManager {
    private MenuManager menuManager;

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
    public SelectionManager(MenuManager menuManager) {
        this.menuManager = menuManager;
    }

    /**
     * Select node
     */
    public void selectNode(GraphNode node) {

    }

    /**
     * Select edge.
     *
     * @param edge the edge
     */
    public void selectEdge(GraphEdge edge) {

    }

    /**
     * Select non-directional edge
     *
     * @param edge the edge
     */
    public void selectNonDirectionalEdge(NonDirectionalEdge edge) {

    }

    /**
     * Update menu
     */
    public void updateMenu() {
        int nodesNumber, edgesNumber, nonDirEdgesNumber;

        nodesNumber = nodesNumber();
        edgesNumber = edgesNumber();
        nonDirEdgesNumber = nonDirectionalEdgesNumber();

        if (edgesNumber == 0) {
            if (nonDirEdgesNumber == 0) {
                if (nodesNumber == 0) {
                    menuManager.updateButtons(MenuManager.State.NOTHING_SELECTED);
                }

                if (nodesNumber == 1) {
                    menuManager.updateButtons(MenuManager.State.SELECTED_SINGLE_NODE);
                }
            }

        }
    }

    /**
     * Nodes number
     *
     * @return the int
     */
    public int nodesNumber() {
        return selectedNodes.size();
    }

    /**
     * Edges number
     *
     * @return the int
     */
    public int edgesNumber() {
        return selectedEdges.size();
    }

    /**
     * Non directional edges number
     *
     * @return the int
     */

    public int nonDirectionalEdgesNumber() {
        return selectedNonDirectionalEdges.size();
    }
}
