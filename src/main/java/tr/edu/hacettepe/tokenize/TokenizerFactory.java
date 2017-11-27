package tr.edu.hacettepe.tokenize;

import tr.edu.hacettepe.document.Document;

/**
 * For flexibility lets move the configuration stuff for the tokenizer to this class.
 *
 * @author gonenc
 */
public class TokenizerFactory {

    public WordTokenizer createWordTokenizer(Document document) {
        return new WordTokenizer(document.openReader());
    }

}
