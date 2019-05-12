package algorithms.Dijkstra;

public class EdgeGraphObject {
    NodeGraphObject sourceNode;
    NodeGraphObject targetNode;
    double weight;

    public NodeGraphObject getSourceNode() {
        return sourceNode;
    }

    public void setSourceNode(NodeGraphObject sourceNode) {
        this.sourceNode = sourceNode;
    }

    public NodeGraphObject getTargetNode() {
        return targetNode;
    }

    public void setTargetNode(NodeGraphObject targetNode) {
        this.targetNode = targetNode;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public EdgeGraphObject(double weight, NodeGraphObject sourceNode, NodeGraphObject targetNode) {
        this.sourceNode = sourceNode;
        this.targetNode = targetNode;
        this.weight = weight;
    }
}
