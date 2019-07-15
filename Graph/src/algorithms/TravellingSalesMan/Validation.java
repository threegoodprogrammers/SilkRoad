package algorithms.TravellingSalesMan;

public class Validation {
    public static boolean isComplete(double[][] distanceMatrix, int nodesCount) {
        for (int i = 0; i < nodesCount; i++) {
            for (int j = 0; j < nodesCount; j++) {
                if (i != j) {
                    if (distanceMatrix[i][j] == Double.MAX_VALUE) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

}
