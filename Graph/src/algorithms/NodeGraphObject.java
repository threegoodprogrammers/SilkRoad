package algorithms;

import java.util.HashMap;

public class NodeGraphObject {
    private NodeGraphObject PreviousNodeInPath;
    private String iD;
    private HashMap<NodeGraphObject, EdgeGraphObject> attachedNodes = new HashMap<>();

    public NodeGraphObject getPreviousNodeInPath() {
        return PreviousNodeInPath;
    }

    public void setPreviousNodeInPath(NodeGraphObject previewsNodeInPath) {
        this.PreviousNodeInPath = previewsNodeInPath;
    }

    public String getiD() {
        return iD;
    }

    public void setiD(String iD) {
        this.iD = iD;
    }

    public HashMap<NodeGraphObject, EdgeGraphObject> getAttachedNodes() {
        return attachedNodes;
    }

    public void setAttachedNodes(HashMap<NodeGraphObject, EdgeGraphObject> attachedNodes) {
        this.attachedNodes = attachedNodes;
    }

    public NodeGraphObject(String iD) {
        this.iD = iD;
    }

    public void connect(NodeGraphObject node, EdgeGraphObject edge) {
        attachedNodes.put(node, edge);
    }
}
