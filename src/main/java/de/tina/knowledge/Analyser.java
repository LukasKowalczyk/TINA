package de.tina.knowledge;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

public class Analyser {
    private static Analyser analyise;

    private Analyser() {

    }

    public static Analyser getInstance() {
        if (analyise == null) {
            analyise = new Analyser();
        }
        return analyise;
    }

    public List<String[]> splitText(String text) {
        List<String[]> out = new ArrayList<String[]>();
        text = text.toLowerCase();
        // Delete each "," and "\""
        text = text.replaceAll("[\",]", "");

        // Split sentence after seperator for example ".", ",", "!" or "?" and so on.
        String[] sentences = text.split("[\\.\\?\\!]");

        // For each sentence there will be a word with a follower saved in a dataset
        for (String s : sentences) {
            String[] words = s.split(" ");
            words = ArrayUtils.removeElements(words, StringUtils.EMPTY);
            out.add(words);

        }
        return out;
    }

    public String[] getVocabulary(String text) {
        String[] vocabulary = new String[0];
        text = text.toLowerCase();
        List<String[]> splittedText = splitText(text);
        for (String[] words : splittedText) {
            for (String word : words) {
                if (ArrayUtils.indexOf(vocabulary, word) < 0) {
                    vocabulary = ArrayUtils.add(vocabulary, word);
                }
            }

        }
        return vocabulary;
    }
}
