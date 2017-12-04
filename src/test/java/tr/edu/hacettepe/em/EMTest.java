package tr.edu.hacettepe.em;

import org.apache.mahout.math.DenseMatrix;
import org.apache.mahout.math.Matrix;
import org.junit.Before;
import org.junit.Test;

public class EMTest {

    private EM em;

    @Before
    public void setUp() throws Exception {
        double[][] docTermMatrix = {
                {1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 1, 1, 1, 1, 0, 0, 1, 0},
                {0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0},
                {0, 1, 1, 0, 0, 2, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 0, 1, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1},
                {0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1}
        };


        Matrix documentTermMatrix = new DenseMatrix(docTermMatrix);
        em = new EM(documentTermMatrix, hashing, 2);

    }

    @Test
    public void execute() throws Exception {
        double[][] wz = {
                {0.114045, 0.053194},
                {0.134815, 0.103362},
                {0.068801, 0.091561},
                {0.039393, 0.044068},
                {0.028358, 0.086285},
                {0.058160, 0.080346},
                {0.103736, 0.037548},
                {0.117902, 0.143838},
                {0.033741, 0.158454},
                {0.052780, 0.047039},
                {0.120531, 0.138864},
                {0.127737, 0.015440}
        };
        double[][] zd = {
                {0.719819, 0.583991, 0.589761, 0.632499, 0.855673, 0.503936, 0.526986, 0.063956, 0.606774},
                {0.280181, 0.416009, 0.410239, 0.367501, 0.144327, 0.496064, 0.473014, 0.936044, 0.393226}
        };
        em.execute(new DenseMatrix(zd), new DenseMatrix(wz));
    }

}