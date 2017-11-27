package tr.edu.hacettepe.tokenize;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Pattern;

import tr.edu.hacettepe.document.StringDocument;

public class WordTokenizer implements Closeable {
	
	
	private EnglishStopwordList englishStopwordList;
	private Locale locale;
	
	private Scanner scanner; 
	private Pattern regex; 
	
	public WordTokenizer(Reader reader) {
		englishStopwordList = new EnglishStopwordList();
		locale=new Locale("en");
		scanner = new Scanner(reader);
		scanner.useDelimiter(Pattern.compile("[\\s]*"));
		regex = Pattern.compile("[a-zA-Z0-9]+", Pattern.MULTILINE | Pattern.DOTALL);
	} 
	
	public String nextToken() {
		if(scanner.findWithinHorizon(regex, 0)!=null) {
			String word = scanner.match().group().toLowerCase(locale);
			if(englishStopwordList.isStopword(word)) 
				return nextToken();
			else
				return word;
			
		}
		
		return null;
	}

	@Override
	public void close() throws IOException {
		scanner.close();
	}

}
