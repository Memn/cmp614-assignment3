package hw3;

import org.apache.mahout.math.Matrix;
import tr.edu.hacettepe.document.Document;
import tr.edu.hacettepe.document.SingleFileCorpus;
import tr.edu.hacettepe.tokenize.TokenizerFactory;
import tr.edu.hacettepe.tools.DocumentTermMatrixBuilder;
import tr.edu.hacettepe.vocab.PatriciaTreePerfectHash;

import java.net.URL;

public class ReadReviews {

    private static final String RESTAURANTS_PATH = "files/Restaurants_Train_v2.xml";
    private static final ClassLoader CLASS_LOADER = ReadReviews.class.getClassLoader();

    public static void main(String[] args) {

        URL restaurantsResource = CLASS_LOADER.getResource(RESTAURANTS_PATH);

        if (restaurantsResource == null) {
            System.err.printf("Resource is not available in Path:%s\n", CLASS_LOADER.toString() + RESTAURANTS_PATH);
            System.exit(1);
        }

        String corpusPath = restaurantsResource.getPath();
        SingleFileCorpus corpus = new SingleFileCorpus(corpusPath, "text");
        TokenizerFactory factory = new TokenizerFactory();

        int noOfDocs = 0;
        for (Document ignored : corpus) {
            noOfDocs++;
        }

        PatriciaTreePerfectHash hashing = PatriciaTreePerfectHash.buildFromCorpus(corpus, factory, 0);

        Matrix matrix = DocumentTermMatrixBuilder.createMatrix(hashing, corpus, factory, noOfDocs);
        System.out.println("No of Docs : " + matrix.rowSize());
        System.out.println("No of Words : " + matrix.columnSize());


    }


}
