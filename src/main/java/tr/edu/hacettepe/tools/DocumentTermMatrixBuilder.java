package tr.edu.hacettepe.tools;

import org.apache.mahout.math.Matrix;
import org.apache.mahout.math.SparseRowMatrix;
import tr.edu.hacettepe.document.Corpus;
import tr.edu.hacettepe.document.Document;
import tr.edu.hacettepe.tokenize.TokenizerFactory;
import tr.edu.hacettepe.tokenize.WordTokenizer;
import tr.edu.hacettepe.vocab.MinPerfectHashing;

public class DocumentTermMatrixBuilder {

    public static Matrix createMatrix(MinPerfectHashing hashing,
                                      Corpus corpus, TokenizerFactory factory, int noOfDocs) {

        SparseRowMatrix matrix = new SparseRowMatrix(noOfDocs,
                hashing.size(), true);

        int di = 0;
        for (Document document : corpus) {
            WordTokenizer tokenizer = factory.createWordTokenizer(document);
            String token;

            while ((token = tokenizer.nextToken()) != null) {
                int wi = hashing.findIndex(token);
                if (wi > -1) {
                    double count = matrix.get(di, wi);
                    count++;
                    matrix.set(di, wi, count);
                }
            }
            di++;
        }

        return matrix;
    }

}
