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
import de.tina.container.Neuron;
import de.tina.container.NeuronMatrix;
import de.tina.container.NeuronRepository;
import de.tina.container.NeuronTyp;

@Component
public class Analyser {
    private String[] stopwords;

    @Value("${sentence.seperator}")
    private String sentenceSeperator;

    @Value("${stop.symbols}")
    private String stopSymbols;

    @Autowired
    private NeuronRepository neurons;

    @PostConstruct
    public void init() {
        stopwords = loadStopWords();
    }

    /**
     * Fill the NeuronMatrix with the words
     * @param splitedText
     * @param neuronMatrix
     * @return the filled NeuronMatrix
     */
    public NeuronMatrix fillTheKnowledgeBase(String text, NeuronMatrix neuronMatrix) {
        // Split the text by every sentence and than by every word
        List<String[]> splitedText = splitText(text);
        for (String[] words : splitedText) {
            for (int i = 0; words.length > i; i++) {
                // if not exist in Database than add to it
                Long id = findIdByWordInNeurons(words[i]);
                if (words.length > i + 1) {
                    Long idFollower = findIdByWordInNeurons(words[i + 1]);
                    neuronMatrix.add(id, idFollower);
                } else {
                    neuronMatrix.add(id);
                }
            }
        }
        return neuronMatrix;
    }

    /**
     * @param words
     */
    private long findIdByWordInNeurons(String word) {
        List<Neuron> findByContent = neurons.findByNeuronTypAndContent(NeuronTyp.TEXT, word.getBytes());
        if (findByContent == null) {
            return neurons.save(new Neuron(NeuronTyp.TEXT, word.getBytes())).getId();
        }
        return findByContent.get(0).getId();
    }

    /**
     * @param words
     */
    private Neuron findWordInNeurons(String word) {
        List<Neuron> findByContent = neurons.findByNeuronTypAndContent(NeuronTyp.TEXT, word.getBytes());
        if (findByContent == null) {
            return neurons.save(new Neuron(NeuronTyp.TEXT, word.getBytes()));
        }
        return findByContent.get(0);
    }

    /**
     * Extract the neurons of the text.
     * @param text
     * @return the neurons
     */
    public Long[] getVocabulary(String text) {
        // And now we split the text
        List<Neuron> output = new ArrayList<>();
        List<String[]> splitedText = splitText(text);
        for (String[] words : splitedText) {
            for (String word : words) {
                // We add only unknown words
                if (neurons.findByNeuronTypAndContent(NeuronTyp.TEXT, word.getBytes()) == null) {
                    neurons.save(new Neuron(NeuronTyp.TEXT, word.getBytes()));
                }
                output.add(findWordInNeurons(word));
            }
        }
        return output.toArray(new Long[output.size()]);
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
     * Replace every "[",]".<br> Splits the text by sentence "[\.\?\!]" and the sentence after that by SPACE.
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
                    neurons.save(new Neuron(NeuronTyp.TEXT, word.getBytes()));
                }
            }
            out.add(words);
        }
        return out;
    }

}
