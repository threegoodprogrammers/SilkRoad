package elements;

import com.jfoenix.controls.JFXButton;

import java.util.HashMap;

public class GraphNode extends JFXButton {
    private GraphNode previousNodeInPath;
    private String identifier;
    private HashMap<GraphNode, GraphEdge> arrachedNodes = new HashMap<>();

    public GraphNode(String text, String identifier) {
        super(text);
        this.identifier = identifier;
    }

    //    public GraphNode(String identifier) {
//        this.identifier = identifier;
//        this.arrachedNodes = new HashMap<>();
//    }

    public GraphNode getPreviousNodeInPath() {
        return previousNodeInPath;
    }

    public void setPreviousNodeInPath(GraphNode previousNodeInPath) {
        this.previousNodeInPath = previousNodeInPath;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }


}
