package tr.edu.hacettepe.tokenize;

import java.io.Reader;

import tr.edu.hacettepe.document.Document;

/**
 * For flexibility lets move the configuration stuff for the tokenizer to this class. 
 * 
 * 
 * @author gonenc
 *
 */
public class TokenizerFactory {
	
	public WordTokenizer createWordTokenizer(Document document) {
		WordTokenizer tokenizer = new WordTokenizer(document.openReader());
		return tokenizer;
	}

}
