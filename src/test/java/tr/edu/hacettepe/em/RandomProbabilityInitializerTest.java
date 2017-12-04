package tr.edu.hacettepe.em;

import org.apache.mahout.math.Matrix;
import org.junit.Assert;
import org.junit.Test;
import tr.edu.hacettepe.em.RandomProbabilityInitializer;

public class RandomProbabilityInitializerTest {

    @Test
    public void randomMatrix() throws Exception {
        // all columns should sums up to 1.
        Matrix matrix = RandomProbabilityInitializer.randomMatrix(10, 3);
        for (int i = 0; i < 10; i++) {
            System.out.printf("%f\t%f\t%f\n", matrix.getQuick(i, 0), matrix.getQuick(i, 1), matrix.getQuick(i, 2));
        }

        for (int c = 0; c < matrix.columnSize(); c++) {
            Assert.assertEquals(1, matrix.viewColumn(c).zSum(), 0.0001);
        }

    }
}