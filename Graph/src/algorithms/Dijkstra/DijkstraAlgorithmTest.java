package algorithms.Dijkstra;

import algorithms.GraphBuilder;
import elements.Graph;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DijkstraAlgorithmTest {

    Graph graph;

    @BeforeEach
    void SetUp() {

    }

    @Test
    void Test1() {
        graph = GraphBuilder.Build().WithNodes(0).getGraph();

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> DijkstraAlgorithm.FindShortestPath(graph, "0", "0"));
    }
}