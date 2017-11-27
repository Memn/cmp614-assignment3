package tr.edu.hacettepe.tools;

import tr.edu.hacettepe.document.SingleFileCorpus;
import tr.edu.hacettepe.tokenize.TokenizerFactory;
import tr.edu.hacettepe.vocab.PatriciaTreePerfectHash;

public class VocabularyBuilder {

    public static void main(String[] args) {

        long start = System.currentTimeMillis();
        SingleFileCorpus corpus = new SingleFileCorpus("/home/gonenc/corpus/wikipedia/bil614WikiCorp.txt");
        TokenizerFactory tokenizerFactory = new TokenizerFactory();

        PatriciaTreePerfectHash hash = PatriciaTreePerfectHash.buildFromCorpus(corpus, tokenizerFactory, 5);


        System.out.printf("No of Tokens: %d. \n"
                + "Calculated in %d", hash.size(), System.currentTimeMillis() - start);

    }

}
