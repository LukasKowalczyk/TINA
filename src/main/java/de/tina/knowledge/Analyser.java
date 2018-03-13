package de.tina.knowledge;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

public class Analyser {
	private static Analyser analyise;

	private String[] stopwords;

	private String sentenceSeperator = "";

	private String stopWordsAndSymbols = "\",";

	private Analyser() {
		loadSentenceSeperator();
		loadStopWords();
	}

	public static Analyser getInstance() {
		if (analyise == null) {
			analyise = new Analyser();
		}
		return analyise;

	}

	/**
	 * Fill the KnowledgeBase with the words
	 * 
	 * @param splitedText
	 * @param knowledgeBase
	 * @return the filled KnowledgeBase
	 */
	public KnowledgeBase fillTheKnowledgeBase(String text, KnowledgeBase knowledgeBase) {
		// Split the text by every sentence and than by every word
		List<String[]> splitedText = splitText(text);
		for (String[] words : splitedText) {
			for (int i = 0; words.length > i; i++) {
				if (words.length > i + 1) {
					knowledgeBase.add(words[i], words[i + 1]);
				} else {
					knowledgeBase.add(words[i]);
				}
			}
		}
		return knowledgeBase;
	}

	/**
	 * Extract the vocabulary of the text.
	 * 
	 * @param text
	 * @return the vocabulary
	 */
	public String[] getVocabulary(String text) {
		String[] vocabulary = new String[0];
		// First the text to lower case
		text = text.toLowerCase();
		// And now we split the text
		List<String[]> splitedText = splitText(text);
		for (String[] words : splitedText) {
			for (String word : words) {
				// We add only unknown words AND no stopwords
				if (ArrayUtils.indexOf(vocabulary, word) < 0 && !ArrayUtils.contains(stopwords, word)) {
					vocabulary = (String[]) ArrayUtils.add(vocabulary, word);
				}
			}
		}
		return vocabulary;
	}

	private void loadSentenceSeperator() {
		try {
			List<String> terminalsymbols = Files.readAllLines(new File("terminalsymbols").toPath());
			for (String terminalsymbol : terminalsymbols) {
				sentenceSeperator += terminalsymbol;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		sentenceSeperator = "[" + sentenceSeperator + "]";
	}

	private void loadStopWords() {
		try {
			List<String> stopwords = Files.readAllLines(new File("stopwords").toPath(), StandardCharsets.ISO_8859_1);
			this.stopwords = stopwords.toArray(new String[stopwords.size()]);
		} catch (IOException e) {
			e.printStackTrace();
		}
		stopWordsAndSymbols = "[" + stopWordsAndSymbols + "]";
	}

	/*
	 * Replace every "[",]".<br> Splits the text by sentence "[\.\?\!]" and the
	 * sentence after that by SPACE.
	 * 
	 * @return the text splited by sentence and words
	 */
	private List<String[]> splitText(String text) {
		List<String[]> out = new ArrayList<String[]>();
		text = text.toLowerCase();
		// Delete each "," and "\""
		text = text.replaceAll(stopWordsAndSymbols, "");

		// Split sentence after seperator for example ".", "!" or "?" and
		// so on.
		String[] sentences = text.split(sentenceSeperator);

		// For each sentence there will be a word with a follower saved in a
		// dataset
		for (String s : sentences) {
			String[] words = s.split(" ");
			words = (String[]) ArrayUtils.removeElement(words, StringUtils.EMPTY);
			for (String word : words) {
				if (ArrayUtils.contains(stopwords, word)) {
					words = (String[]) ArrayUtils.removeElement(words, word);
				}
			}
			out.add(words);
		}
		return out;
	}

}
