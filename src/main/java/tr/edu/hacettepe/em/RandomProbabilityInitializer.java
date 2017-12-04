package tr.edu.hacettepe.em;

import org.apache.mahout.math.DenseMatrix;
import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.Matrix;
import tr.edu.hacettepe.vocab.PatriciaTreePerfectHash;

import java.util.Arrays;
import java.util.Map;

public class RandomProbabilityInitializer {


    public static Matrix randomMatrix(int row, int column) {
        return new DenseMatrix(randomMatrixColumnsSumsTo1(row, column)).transpose();
    }

    private static double[][] randomMatrixColumnsSumsTo1(int row, int column) {
        // make transpose at the end
        double[][] randomVectors = new double[column][row];
        for (int i = 0; i < column; i++) {
            randomVectors[i] = randomVectorSums1(row);
        }
        return randomVectors;
    }

    private static double[] randomVectorSums1(int size) {
        double[] randomVector = new double[size];
        for (int i = 0; i < size; i++) {
            randomVector[i] = Math.random();
        }
        double sum = Arrays.stream(randomVector).sum();
        for (int i = 0; i < size; i++) {
            randomVector[i] = randomVector[i] / sum;
        }
        return randomVector;
    }

    public static void weightingByTopics(Matrix wz,
                                         PatriciaTreePerfectHash vocabulary,
                                         Map<String, String[]> topicMap) {
        int i = 0;
        for (Map.Entry<String, String[]> entry : topicMap.entrySet()) {
            for (String keyword : entry.getValue()) {
                increaseWeightOf(wz, i, vocabulary.findIndex(keyword));
            }
            i++;
        }


    }

    private static void increaseWeightOf(Matrix wz, int z, int w) {
        // re-init on wz
        double[] randomVector = new double[wz.columnSize()];
        for (int i = 0; i < wz.columnSize(); i++) {
            randomVector[i] = Math.random();
        }
        randomVector[z] += 2.0;
        // normalize
        double sum = Arrays.stream(randomVector).sum();
        for (int i = 0; i < wz.columnSize(); i++) {
            randomVector[i] = randomVector[i] / sum;
        }
        wz.assignRow(w, new DenseVector(randomVector));
    }
}
