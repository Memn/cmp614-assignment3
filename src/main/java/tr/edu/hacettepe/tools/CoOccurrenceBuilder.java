package tr.edu.hacettepe.tools;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.apache.mahout.math.Matrix;
import org.apache.mahout.math.SparseRowMatrix;
import tr.edu.hacettepe.document.Corpus;
import tr.edu.hacettepe.document.Document;
import tr.edu.hacettepe.tokenize.TokenizerFactory;
import tr.edu.hacettepe.tokenize.WordTokenizer;
import tr.edu.hacettepe.vocab.MinPerfectHashing;

public class CoOccurrenceBuilder {


    public static Matrix createRawOccurence(MinPerfectHashing hashing,
                                            Corpus corpus, TokenizerFactory factory, int windowSize) {
        /* If the last parameter is false, we will use sorted lists, if it is true it will be
		 * HashMaps. The HashMap will use more memory, but will be faster. The array will perform 
		 * binarysearch and even worse shifts for inserts, but will use less memory. 
		 */
        SparseRowMatrix matrix = new SparseRowMatrix(hashing.size(), hashing.size(), false);

        for (Document document : corpus) {
            CircularFifoQueue<String> words = new CircularFifoQueue<>(windowSize);
            WordTokenizer tokenizer = factory.createWordTokenizer(document);
            String token;

            while ((token = tokenizer.nextToken()) != null) {
                int wi1 = hashing.findIndex(token);
                if (wi1 > -1) {
                    for (String w : words) {
                        int wi2 = hashing.findIndex(w);
                        if (wi2 > -1) {
                            double val = matrix.get(wi1, wi2);
                            val++;
                            matrix.set(wi1, wi2, val);
                            // We are keeping it symmetric.
                            matrix.set(wi2, wi1, val);
                        }
                    }
                }
                words.add(token);
            }
        }

        return matrix;
    }

}
