package de.tina.master;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.ArrayUtils;
import de.tina.knowledge.Analyser;
import de.tina.knowledge.KnowledgeBase;
import de.tina.knowledge.Memory;

public class Master {

	private boolean preFilter;

	private int succesQuota;

	private Analyser analyser = Analyser.getInstance();

	private Memory memory = Memory.getInstance();

	private Map<String, KnowledgeBase> knowledge;

	/**
	 * @param sourcePath
	 * @param preFilter
	 * @param succesQuota
	 */
	public Master(String sourcePath, boolean preFilter, int succesQuota) {
		this.preFilter = preFilter;
		this.succesQuota = succesQuota;
		knowledge = memory.remember(new File(sourcePath));

	}

	/**
	 * ask the master what the text is.
	 * @param text
	 */
	public Map<String, Integer> ask(String text) {
		System.out.println(">" + text + "<");
		Map<String, Integer> themes = think(text);
		if (themes.isEmpty()) {
			System.out.println("I can't assign >" + text + "<");
		}
		for (String key : themes.keySet()) {
			System.out.println("I know that >" + text + "< is " + themes.get(key) + "% >" + key + "<");
		}
		return themes;
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
				out.put(knowledgeBase.getName(), erg);
			}
		}
		return out;
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
