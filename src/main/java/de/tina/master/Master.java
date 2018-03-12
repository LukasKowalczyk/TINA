package de.tina.master;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.RandomStringUtils;

import de.tina.knowledge.Analyser;
import de.tina.knowledge.KnowledgeBase;
import de.tina.knowledge.Memory;

public class Master {

	private boolean preFilter;

	private int succesQuota;

	private Analyser analyser = Analyser.getInstance();

	private Memory memory = Memory.getInstance();

	private String sourcePath;

	private Map<String, KnowledgeBase> knowledge;

	/**
	 * @param sourcePath
	 * @param preFilter
	 * @param succesQuota
	 */
	public Master(String sourcePath, boolean preFilter, int succesQuota) {
		this.preFilter = preFilter;
		this.succesQuota = succesQuota;
		this.sourcePath = sourcePath;
		knowledge = memory.remember(new File(sourcePath));

	}

	/**
	 * ask the master what the text is.
	 * 
	 * @param text
	 */
	@SuppressWarnings("deprecation")
	public Map<String, Integer> ask(String text) {
		System.out.println(">" + text + "<");
		Map<String, Integer> themes = think(text);

		// Here we analyse if there is for example a 50:50 Hit.
		// if thats the the case we must generate a new KnowladgeBase
		boolean specialCase = isThisASpecialCase(themes);
		if (specialCase) {
			// this text must be checked what this is
			try {
				FileUtils.writeStringToFile(new File(sourcePath, RandomStringUtils.randomAlphanumeric(6) + ".txt"),
						text);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return themes;
	}

	private boolean isThisASpecialCase(Map<String, Integer> themes) {
		int quota = 0;
		for (int value : themes.values()) {
			quota += value;
		}
		if (quota == 100 && themes.size() > 1) {
			return true;
		}
		return false;
	}

	private Map<String, Integer> think(String text) {
		Map<String, Integer> out = new HashMap<>();
		// Should the knowledgeBase be prefiltert?
		Collection<KnowledgeBase> knowledgeBases = knowledge.values();
		if (preFilter) {
			knowledgeBases = possibleThemeByVocabulary(analyser.getVocabulary(text));
		}
		// Now we must find our matching knowledgebase(s)
		if (!ArrayUtils.isEmpty(knowledgeBases.toArray())) {
			for (KnowledgeBase knowledgeBase : knowledgeBases) {
				// Create a new knowledgeBase out of the text but with a given
				// vocabulary
				KnowledgeBase newKnowledgeBase = analyser.fillTheKnowledgeBase(text,
						new KnowledgeBase(knowledgeBase.getName(), knowledgeBase.getVocabulary()));
				// Percent of the matching of the knowledgeBases
				int erg = compareMatrix(newKnowledgeBase.getAdjiazenMatrix(), knowledgeBase.getAdjiazenMatrix(),
						newKnowledgeBase.getMax());
				// Only themes that are greater than 0%
				if (erg > 0) {
					out.put(knowledgeBase.getName(), erg);
				}
			}
		}
		if (out.isEmpty()) {
			System.out.println("I can't assign >" + text + "<");
			return new HashMap<String, Integer>();
		}
		// When we don't want the Realtive Probability in our Hits
		calculateRelativProbability(out);
		return out;
	}

	private void calculateRelativProbability(Map<String, Integer> out) {
		for (String key : out.keySet()) {
			if (out.get(key) > 0) {
				out.put(key, out.get(key) / out.keySet().size());
			}
		}
	}

	private int compareMatrix(int[][] matrixToProve, int[][] possibleMatrix, int maxHits) {
		int countHits = 0;
		for (int i = 0; i < possibleMatrix.length; i++) {
			for (int j = 0; j < possibleMatrix[i].length; j++) {
				// Only if in each array there is not a 0
				if (matrixToProve[i][j] != 0 && possibleMatrix[i][j] != 0) {
					countHits += matrixToProve[i][j];
				}
			}
		}
		// Percent of hits
		return (int) ((countHits) / (double) maxHits * 100);
	}

	private List<KnowledgeBase> possibleThemeByVocabulary(String[] vocabulary) {
		List<KnowledgeBase> out = new ArrayList<KnowledgeBase>();
		for (String key : knowledge.keySet()) {
			KnowledgeBase v = knowledge.get(key);
			int countFails = 0;
			for (int i = 0; i < vocabulary.length; i++) {
				String word = vocabulary[i];
				// Counts every missmatch
				if (!ArrayUtils.contains(v.getVocabulary(), word)) {
					countFails++;
				}
			}
			int erg = (int) (countFails / (double) vocabulary.length * 100);
			int failQuota = 100 - succesQuota;
			// Only match percent is less then the fail Quota
			if (erg <= failQuota) {
				out.add(v);
			}
		}
		return out;
	}
}
