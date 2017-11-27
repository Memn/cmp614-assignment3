package tr.edu.hacettepe.document;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.regex.Pattern;

public class SingleFileCorpus implements Corpus {
    private final Pattern regex;
    private final String corpusFile;

    public SingleFileCorpus(String corpusFile) {
        regex = Pattern.compile("<article>(.*?)</article>", Pattern.MULTILINE | Pattern.DOTALL);
        this.corpusFile = corpusFile;
    }


    public SingleFileCorpus(String corpusPath, String tagName) {
        this.regex = Pattern.compile("<" + tagName + ">(.*?)</" + tagName + ">", Pattern.MULTILINE | Pattern.DOTALL);
        this.corpusFile = corpusPath;

    }


    @Override
    public Iterator<Document> iterator() {
        DocIter docIter;

        try {
            docIter = new SingleFileCorpus.DocIter();
        } catch (IOException e) {
            throw new RuntimeException("Problem opening the corpus file", e);
        }

        return docIter;
    }

    public void close(Iterator<Document> iter) {
        ((SingleFileCorpus.DocIter) iter).scanner.close();
    }


    private class DocIter implements Iterator<Document> {

        private Scanner scanner;
        private Document document;

        private DocIter() throws IOException {
            scanner = new Scanner(new File(corpusFile));
            document = nextDoc();
        }

        private Document nextDoc() {
            if (scanner.findWithinHorizon(regex, 0) != null) {
                return new StringDocument(scanner.match().group(1));
            }

            scanner.close();
            return null;
        }

        @Override
        public boolean hasNext() {
            return document != null;
        }

        @Override
        public Document next() {
            Document tmp = document;
            document = nextDoc();
            return tmp;
        }

    }


}
