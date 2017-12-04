package tr.edu.hacettepe.em;

import org.apache.mahout.math.Matrix;
import org.apache.mahout.math.Vector;
import tr.edu.hacettepe.vocab.PatriciaTreePerfectHash;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;


public class EM {

    //the threshold by which we measure convergence
    private static final double DELTA = .1;
    //max iterations that we go through
    private static final int MAX_ITERATIONS = 100;

    private final Matrix documentTermMatrix;

    private final int wordSize;
    private final int documentSize;
    private final PatriciaTreePerfectHash vocabulary;
    private final int SHOW_ASPECTS = 20;
    private int topicSize;
    private Matrix wz;
    private Matrix zd;
    private double[][][] expected;
    private Map<String, String[]> topicMap;
    private int numIterations = 0;


    /*
     * constructor
     */
    public EM(Matrix documentTermMatrix,
              PatriciaTreePerfectHash vocabulary,
              Map<String, String[]> topicMap) {
        this.documentTermMatrix = documentTermMatrix;
        this.documentSize = documentTermMatrix.rowSize();
        this.wordSize = documentTermMatrix.columnSize();
        this.vocabulary = vocabulary;
        this.topicMap = topicMap;
        this.topicSize = topicMap.size();
        expected = new double[topicSize][documentSize][wordSize];
    }

    public void execute() {

        initialize();
        RandomProbabilityInitializer.weightingByTopics(wz, vocabulary, topicMap);
        executeEM();
    }

    private void initialize() {
        int documentSize = this.documentSize;
        int wordSize = this.wordSize;


        this.wz = RandomProbabilityInitializer.randomMatrix(wordSize, topicMap.size());
        this.zd = RandomProbabilityInitializer.randomMatrix(topicMap.size(), documentSize);
    }

    private void executeEM() {
        double likelihood = computeLikelihood();
        double newLikelihood;
        System.out.printf("Iteration " + numIterations + "\nLikelihood: " + "%.3f\n\n", likelihood);

        while (true) {
            numIterations++;

            expectation();
            maximization();

            newLikelihood = computeLikelihood();

            System.out.printf("Iteration " + numIterations + "\nLikelihood: " + "%.3f\n\n", newLikelihood);

            //test for convergence
            if (Math.abs(likelihood - newLikelihood) < DELTA || numIterations > MAX_ITERATIONS) {
                break;
            }

            likelihood = newLikelihood;
        }
        findPrintAspectTerms();
//        printMatrix(wz);
//        System.out.println();
//        printMatrix(zd);
    }

    /*
     *  Given the observations and current estimated parameters,
     *  compute the expected counts of each observation
     */
    private void expectation() {
        //        P(z0|w0, d0) = (P(w0|z0) * P(z0|d0)) / \sum_z{ P(w0|z) * P(z|d0)}
        for (int z = 0; z < topicSize; z++) {
            for (int w = 0; w < wordSize; w++) {
                double pwz = wz.get(w, z);
                for (int d = 0; d < documentSize; d++) {
                    double pzd = zd.get(z, d);
                    double sum = 0;
                    for (int zi = 0; zi < topicSize; zi++) {
                        sum += (wz.get(w, zi) * zd.get(zi, d));
                    }
                    if (sum == 0) {
                        expected[z][d][w] = 0;
                    } else {
                        expected[z][d][w] = (pwz * pzd) / (sum);
                    }
                }
            }
        }

    }

    /*
     * Calculates new estimated parameters based on new estimated observations
     */
    private void maximization() {
        updateWZ();
        updateZD();
    }

    private void updateWZ() {
        // P(w|z) = \sum_d{ n(d,w)*P(z|w,d) } / \sum_w{\sum_d{ n(d,w)*P(z|w,d) }}
        for (int z = 0; z < topicSize; z++) {
            double sumWD = 0;
            for (int d = 0; d < documentSize; d++) {
                for (int w = 0; w < wordSize; w++) {
                    sumWD += (documentTermMatrix.get(d, w) * expected[z][d][w]);
                }
            }
            if (sumWD == 0) {
                continue;
            }
            for (int w = 0; w < wordSize; w++) {
                double sumD = 0;
                for (int d = 0; d < documentSize; d++) {
                    sumD += (documentTermMatrix.get(d, w) * expected[z][d][w]);
                }
                wz.set(w, z, sumD / sumWD);
            }
        }
    }

    private void updateZD() {

        // P(z|d) = \sum_d{ n(d,w)*P(z|w,d) } / n(d) }
        for (int z = 0; z < topicSize; z++) {
            for (int d = 0; d < documentSize; d++) {
                double sumD = 0;
                for (int w = 0; w < wordSize; w++) {
                    sumD += (documentTermMatrix.get(d, w) * expected[z][d][w]);
                }
                double wordsInDoc = documentTermMatrix.viewRow(d).zSum();
                if (wordsInDoc != 0) {
                    zd.set(z, d, sumD / wordsInDoc);
                }
            }
        }

    }

    /*
     * Returns the likelihood that the given data was generated by the given parameters
     * We take the log of the likelihood to avoid underflow
     */
    private double computeLikelihood() {
        //L1 = \sum_d{ \sum_w {n(d,w) * log[P(d) * \sum_z{P(w|z)P(z|d)}] }}
        double totalWordCount = documentTermMatrix.zSum();
        double likelihood = 0;
        for (int d = 0; d < documentSize; d++) {
            for (int w = 0; w < wordSize; w++) {
                double pd = documentTermMatrix.viewRow(d).zSum() / totalWordCount;
                double sumZ = 0;
                for (int z = 0; z < topicSize; z++) {
                    sumZ += (wz.get(w, z) * zd.get(z, d));
                }
                if (pd == 0) {
                    // ooh a document with no word!
                    continue;
                }
                if (sumZ == 0) {
                    continue;
                }
                if (documentTermMatrix.get(d, w) == 0) {
                    continue;
                }
                likelihood += (documentTermMatrix.get(d, w) * Math.log(pd * sumZ));
            }
        }

        return likelihood;
    }

    void findPrintAspectTerms() {
        System.out.printf("Maximum Likelihood after iteration %d:\n", numIterations);

        int z = 0;
        for (Map.Entry<String, String[]> entry : topicMap.entrySet()) {
            System.out.printf("Words for Topic: %s\n", entry.getKey());
            printMaxWords(z, SHOW_ASPECTS);
            z++;
        }
    }

    private void printMaxWords(int z, int size) {
        Vector topic = wz.viewColumn(z);

        Map<Integer, Double> topicWordIndex = new HashMap<>(topicSize);
        for (int i = 0; i < topic.size(); i++) {
            topicWordIndex.put(i, topic.get(i));
        }

        StringBuilder builder = new StringBuilder();
        topicWordIndex.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(size)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new))
                .forEach((index, probability) -> builder.append(vocabulary.findWord(index)).append(", "));
        System.out.println(builder.toString());
    }

    void execute(Matrix zd, Matrix wz) {

        this.zd = zd;
        this.wz = wz;
        executeEM();

    }

}