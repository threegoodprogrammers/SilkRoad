package algorithms.Dijkstra;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DijkstraAlgorithmTest {

    GraphObject graph;

    @BeforeEach
    void SetUp() {

    }

    @AfterEach
    void TearDown() {

    }

    @Test
    void GraphWith0Nodes_ThrowsArgumentException() {
        graph = GraphBuilder.Build().WithNodes(0).getGraph();

        Assertions.assertThrows(ArrayStoreException.class, () -> DijkstraAlgorithm.FindShortestPath(graph, "0", "0"));
    }

    @Test
    void GraphWith1Node_StartIs0() {
        graph = GraphBuilder.Build().WithNodes(1).getGraph();

        PathDataObject pathData = DijkstraAlgorithm.FindShortestPath(graph, "0", "0");

        Assertions.assertEquals(1, pathData.distances.size());
        Assertions.assertEquals(0, pathData.GetDistanceToNode(graph.getNode("0")));
    }

    @Test
    void GraphWith2Nodes_StartIs0_DistanceIs3_All() {
        graph = GraphBuilder.Build().WithNodes(2).AllConnected(3).getGraph();

        PathDataObject pathData = DijkstraAlgorithm.FindShortestPath(graph, "0", "1");

        Assertions.assertEquals(2, pathData.distances.size());
        Assertions.assertEquals(0, pathData.GetDistanceToNode(graph.getNode("0")));
        Assertions.assertEquals(3, pathData.GetDistanceToNode(graph.getNode("1")));
        Assertions.assertArrayEquals(graph.getNodes().toArray(), pathData.GetPathNodesToNode(graph.getNode("1")).toArray());
    }

    @Test
    void GraphWith2Nodes_StartIs1_DistanceIs3_All() {
        graph = GraphBuilder.Build().WithNodes(2).AllConnected(3).getGraph();

        PathDataObject pathData = DijkstraAlgorithm.FindShortestPath(graph, "1", "0");

        Assertions.assertEquals(2, pathData.distances.size());
        Assertions.assertEquals(3, pathData.GetDistanceToNode(graph.getNode("0")));
        Assertions.assertEquals(0, pathData.GetDistanceToNode(graph.getNode("1")));
        NodeGraphObject[] nodeList = {(NodeGraphObject) graph.getNodes().toArray()[1], (NodeGraphObject) graph.getNodes().toArray()[0]};
        Assertions.assertArrayEquals(nodeList, pathData.GetPathNodesToNode(graph.getNode("0")).toArray());
    }

    @Test
    void GraphWith3Nodes_StartIs0_DistanceIs2_All() {
        graph = GraphBuilder.Build().WithNodes(3).AllConnected(2).getGraph();
        PathDataObject pathData = DijkstraAlgorithm.FindShortestPath(graph, "0", "1");

        Assertions.assertEquals(3, pathData.distances.size());
        Assertions.assertEquals(0, pathData.GetDistanceToNode(graph.getNode("0")));
        Assertions.assertEquals(2, pathData.GetDistanceToNode(graph.getNode("1")));
        Assertions.assertEquals(2, pathData.GetDistanceToNode(graph.getNode("2")));

    }
}