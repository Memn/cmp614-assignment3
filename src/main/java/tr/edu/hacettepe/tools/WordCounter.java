package tr.edu.hacettepe.tools;

import tr.edu.hacettepe.document.Document;
import tr.edu.hacettepe.document.SingleFileCorpus;
import tr.edu.hacettepe.tokenize.WordTokenizer;

public class WordCounter {
	
	
	
	public static void main(String[] args) {
		SingleFileCorpus corpus = new SingleFileCorpus("/home/gonenc/corpus/wikipedia/bil614WikiCorp.txt");
		int tokenCount=0;
		long start = System.currentTimeMillis();
		
		for (Document document : corpus) {
			WordTokenizer tokenizer = new WordTokenizer(document.openReader());
			
			String token=null;
			
			while((token=tokenizer.nextToken())!=null) {
				tokenCount++;
			}
		}
		
		System.out.printf("No of Tokens: %d. \n"
				+ "Calculated in %d", tokenCount, System.currentTimeMillis()-start);
	}

}
