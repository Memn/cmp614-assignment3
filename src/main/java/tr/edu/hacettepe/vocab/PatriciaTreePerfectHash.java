package tr.edu.hacettepe.vocab;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.trie.PatriciaTrie;

import tr.edu.hacettepe.document.Corpus;
import tr.edu.hacettepe.document.Document;
import tr.edu.hacettepe.tokenize.TokenizerFactory;
import tr.edu.hacettepe.tokenize.WordTokenizer;

public class PatriciaTreePerfectHash implements MinPerfectHashing {
	
	private PatriciaTrie<Integer> dictionary;
	
	private List<String> inverseArray;

	public PatriciaTreePerfectHash(PatriciaTrie<Integer> filteredDictionary) {
		this.dictionary = filteredDictionary;
		inverseArray = new ArrayList<String>(dictionary.size());
		
		String key = dictionary.firstKey();
		for (int i = 0; i < dictionary.size(); i++) {
			inverseArray.add(key);
			key = dictionary.nextKey(key);
		}
	}

	@Override
	public int findIndex(String word) {
		if(dictionary.containsKey(word)) {
			return dictionary.get(word);
		} else {
			return -1;
		}
	}

	@Override
	public String findWord(int index) {
		return inverseArray.get(index);
	}
	
	public static PatriciaTreePerfectHash buildFromCorpus(Corpus corpus, TokenizerFactory tokenizerFactory, 
			int freqThreshold) {
		
		PatriciaTrie<Integer> dict = new PatriciaTrie<Integer>();
		
		List<Integer> frequencies = new ArrayList<Integer>();
		
		int wordIndex=0;
		
		for (Document document : corpus) {
			WordTokenizer tokenizer = tokenizerFactory.createWordTokenizer(document);
			String token=null;
			
			while((token=tokenizer.nextToken())!=null) {
				if(!dict.containsKey(token)) {
					dict.put(token, wordIndex);
					frequencies.add(1);
					wordIndex++;
				} else {
					int wi = dict.get(token);
					int count = frequencies.get(wi);
					count++;
					frequencies.set(wi, count);
				}
			}
		}
		
		
		PatriciaTrie<Integer> filteredDictionary = new PatriciaTrie<Integer>();
		
		Set<String> words = dict.keySet();
		int newIndex=0;
		for (String word : words) {
			int wi = dict.get(word);
			if(frequencies.get(wi)>freqThreshold) {
				filteredDictionary.put(word, newIndex);
				newIndex++;
			}
		}

		PatriciaTreePerfectHash hash = new PatriciaTreePerfectHash(filteredDictionary);
		
		return hash;
		
		
	}

	@Override
	public int size() {
		return this.dictionary.size();
	}

	

}
