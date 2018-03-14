package de.tina.knowledge;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Analyser {
	private String[] stopwords;

	@Value("${sentence.seperator}")
	private String sentenceSeperator;

	@Value("${stop.symbols}")
	private String stopSymbols;

	@PostConstruct
	public void init() {
		stopwords = loadStopWords();
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

		// And now we split the text
		List<String[]> splitedText = splitText(text);
		for (String[] words : splitedText) {
			for (String word : words) {
				// We add only unknown words
				if (!ArrayUtils.contains(vocabulary, word)) {
					vocabulary = (String[]) ArrayUtils.add(vocabulary, word);
				}
			}
		}
		return vocabulary;
	}

	private String deleteStopwords(String text) {
		for (String stopword : stopwords) {
			text = text.replaceAll("\\b" + stopword.toLowerCase() + "\\b", "");
		}
		return text;
	}

	private String[] loadStopWords() {
		try {
			List<String> stopwords = Files.readAllLines(new File("stopwords").toPath(), StandardCharsets.ISO_8859_1);
			return stopwords.toArray(new String[stopwords.size()]);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new String[0];
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
		text = text.replaceAll(stopSymbols, "");

		// Delete the stopwords
		text = deleteStopwords(text).trim();
		if (text.isEmpty()) {
			return out;
		}
		// Split sentence after seperator for example ".", "!" or "?" and
		// so on.
		String[] sentences = text.split(sentenceSeperator);

		// For each sentence there will be a word with a follower saved in a
		// dataset
		for (String s : sentences) {
			String[] words = s.split(" ");
			for (String word : words) {
				word = word.trim();
				if (ArrayUtils.contains(stopwords, word) || word.isEmpty()) {
					words = (String[]) ArrayUtils.removeElement(words, word);
				}
			}
			out.add(words);
		}
		return out;
	}

}
