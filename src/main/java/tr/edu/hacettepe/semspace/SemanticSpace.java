package tr.edu.hacettepe.semspace;

import org.apache.mahout.math.Matrix;
import org.apache.mahout.math.Vector;
import tr.edu.hacettepe.vocab.MinPerfectHashing;

@SuppressWarnings("WeakerAccess")
public class SemanticSpace {

    private MinPerfectHashing hashing;
    private Matrix matrix;

    public SemanticSpace(MinPerfectHashing hashing, Matrix matrix) {
        this.matrix = matrix;
        this.hashing = hashing;
    }

    public double similarity(String word1, String word2) {
        int wi1 = hashing.findIndex(word1);
        int wi2 = hashing.findIndex(word2);

        if (wi1 < 0 || wi2 < 0) {
            return -1;
        }

        Vector v1 = matrix.viewRow(wi1).normalize();
        Vector v2 = matrix.viewRow(wi2).normalize();


        return v1.dot(v2);
    }


}
