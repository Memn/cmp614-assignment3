package hw3;

import org.apache.mahout.math.Matrix;
import tr.edu.hacettepe.document.Document;
import tr.edu.hacettepe.document.SingleFileCorpus;
import tr.edu.hacettepe.tokenize.TokenizerFactory;
import tr.edu.hacettepe.tools.DocumentTermMatrixBuilder;
import tr.edu.hacettepe.vocab.PatriciaTreePerfectHash;

public class ReadReviews {

    public static void main(String[] args) {
        String corpusPath = "/Users/Gonenc/Documents/Restaurants_Train_v2.xml";

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
