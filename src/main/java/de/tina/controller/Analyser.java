package de.tina.controller;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import de.tina.container.NeuronMatrix;
import de.tina.container.Neurons;

@Component
public class Analyser {
	private String[] stopwords;

	@Value("${sentence.seperator}")
	private String sentenceSeperator;

	@Value("${stop.symbols}")
	private String stopSymbols;

	@Autowired
	private Neurons neurons;

	@PostConstruct
	public void init() {
		stopwords = loadStopWords();
	}

	/**
	 * Fill the NeuronMatrix with the words
	 * 
	 * @param splitedText
	 * @param neuronMatrix
	 * @return the filled NeuronMatrix
	 */
	public NeuronMatrix fillTheKnowledgeBase(String text, NeuronMatrix neuronMatrix) {
		// Split the text by every sentence and than by every word
		List<String[]> splitedText = splitText(text);
		for (String[] neuronIDs : splitedText) {
			for (int i = 0; neuronIDs.length > i; i++) {
				if (neuronIDs.length > i + 1) {
					neuronMatrix.add(neuronIDs[i], neuronIDs[i + 1]);
				} else {
					neuronMatrix.add(neuronIDs[i]);
				}
			}
		}
		return neuronMatrix;
	}

	/**
	 * Extract the neurons of the text.
	 * 
	 * @param text
	 * @return the neurons
	 */
	public String[] getVocabulary(String text) {
		Neurons neurons = new Neurons();
		// And now we split the text
		List<String[]> splitedText = splitText(text);
		for (String[] words : splitedText) {
			for (String word : words) {
				// We add only unknown words
				if (neurons.contains(word)) {

					neurons.add(word);

				}
			}
		}
		return neurons.getIds();
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
				} else {
					neurons.add(word);
				}
			}
			out.add(words);
		}
		neurons.persist();
		return out;
	}

}
