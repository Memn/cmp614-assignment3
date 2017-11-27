package tr.edu.hacettepe.semspace;

import org.apache.mahout.math.Matrix;
import tr.edu.hacettepe.document.SingleFileCorpus;
import tr.edu.hacettepe.tokenize.TokenizerFactory;
import tr.edu.hacettepe.tools.CoOccurrenceBuilder;
import tr.edu.hacettepe.vocab.PatriciaTreePerfectHash;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ToeflQuestions {

    public static void main(String[] args) throws IOException {
        String corpusPath = "/home/gonenc/corpus/wikipedia/bil614WikiCorp.txt";

        List<String[]> questions = readQuestions();
        long start = System.currentTimeMillis();
        SingleFileCorpus corpus = new SingleFileCorpus(corpusPath);
        TokenizerFactory tokenizerFactory = new TokenizerFactory();

        PatriciaTreePerfectHash hash = PatriciaTreePerfectHash.buildFromCorpus(corpus, tokenizerFactory, 5);
        System.out.println("Vocabulary built");

        Matrix matrix = CoOccurrenceBuilder.createRawOccurence(hash, corpus, tokenizerFactory, 3);
        System.out.println("Co-Occurrence MAtrix built");
        PMIWeighting.applyWeighting(matrix);
        System.out.println("PMI Weighting");


        SemanticSpace semanticSpace = new SemanticSpace(hash, matrix);

        int correctCount = 0;
        for (String[] words : questions) {
            double max = Double.NEGATIVE_INFINITY;
            System.out.print(words[0] + " ");
            int maxi = -1;
            for (int i = 1; i < words.length; i++) {
                double sim = semanticSpace.similarity(words[0], words[i]);
                System.out.print(" " + words[i] + ":" + sim);
                if (max <= sim) {
                    max = sim;
                    maxi = i;
                }
            }

            if (maxi == 1) {
                correctCount++;
            }

            System.out.println();
        }

        System.out.println("Number of Correct : " + correctCount);
        System.out.printf("No of Tokens: %d. \n"
                + "Calculated in %d", hash.size(), System.currentTimeMillis() - start);
    }


    private static List<String[]> readQuestions() throws IOException {
        InputStream inputStream = ToeflQuestions.class.getResourceAsStream("toefl.txt");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        List<String[]> retval = new ArrayList<>();

        while ((line = bufferedReader.readLine()) != null) {
            String[] words = line.split("\\|");
            for (int i = 0; i < words.length; i++) {
                words[i] = words[i].trim();
            }
            retval.add(words);
        }


        return retval;
    }

}
