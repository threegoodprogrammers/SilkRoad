package algorithms.Dijkstra;

import algorithms.GraphBuilder;
import elements.Graph;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DijkstraAlgorithmTest {

    Graph graph;

    @BeforeEach
    void SetUp() {

    }

    @AfterEach
    void TearDown() {

    }

    @Test
    void GraphWithNoNodes_ThrowsArgumentException() {
        graph = GraphBuilder.Build().WithNodes(0).getGraph();

        Assertions.assertThrows(ArrayStoreException.class, () -> DijkstraAlgorithm.FindShortestPath(graph, "0", "0"));
    }

    @Test
    void GraphWith1Node_ReturnsPathWithOneNode() {
        graph = GraphBuilder.Build().WithNodes(1).getGraph();

        PathData pathData = DijkstraAlgorithm.FindShortestPath(graph, "0", "0");

        Assertions.assertEquals(1, pathData.distances.size());
        Assertions.assertEquals(0, pathData.GetDistanceToNode(graph.getNode("0")));
    }
}