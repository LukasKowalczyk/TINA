package de.tina.master;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.ArrayUtils;
import de.tina.knowledge.Analyser;
import de.tina.knowledge.KnowledgeBase;

public class Master {

	private boolean preFilter;

	private int succesQuota;

	private Analyser analyser = Analyser.getInstance();

	private Map<String, KnowledgeBase> knowledge;

	private String sourcePath;

	public Master(String sourcePath, boolean preFilter, int succesQuota) {
		this.sourcePath=sourcePath;
		this.preFilter=preFilter;
		this.succesQuota=succesQuota;
		knowledge = new HashMap<String, KnowledgeBase>();
		remember();
	}

	public void ask(String text) {
		System.out.println(">" + text + "<");
		Map<String, Integer> themes = think(text);
		if (themes.isEmpty()) {
			System.out.println("I can't assign  >" + text + "<");
		}
		for (String key : themes.keySet()) {
			System.out.println("I know that >" + text + "< is " + themes.get(key) + "% >" + key + "<");
		}

	}

	private void remember() {
		// load every JSON-File
		for (File file : new File(sourcePath).listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".json");
			}

		})) {
			KnowledgeBase bap = KnowledgeBase.load(file);
			if (bap != null) {
				knowledge.put(bap.getName(), bap);
			}
		}
	}

	/**
	 * @param text
	 * @return
	 */
	private Map<String, Integer> think(String text) {
		Map<String, Integer> out = new HashMap<>();

		Collection<KnowledgeBase> possibleKnowledgeBases = knowledge.values();
		if (preFilter) {
			possibleKnowledgeBases = possibleThemeByVocabulary(analyser.getVocabulary(text));
		}
		if (!ArrayUtils.isEmpty(possibleKnowledgeBases.toArray())) {
			for (KnowledgeBase pknowledgeBase : possibleKnowledgeBases) {
				KnowledgeBase kb = generateKnowledgeBase(text, pknowledgeBase.getName(),
						pknowledgeBase.getVocabulary());
				// System.out.println(kb.print());
				// System.out.println(pknowledgeBase.print());
				int erg = compareMatrix(kb.getAdjiazenMatrix(), pknowledgeBase.getAdjiazenMatrix(), kb.getMax());
				out.put(pknowledgeBase.getName(), erg);
			}
		}
		return out;
	}

	/**
	 * @param matrixToProve
	 * @param possibleMatrix
	 * @return
	 */
	private int compareMatrix(int[][] matrixToProve, int[][] possibleMatrix, int maxHits) {
		int countHits = 0;
		for (int i = 0; i < possibleMatrix.length; i++) {
			for (int j = 0; j < possibleMatrix[i].length; j++) {
				if (matrixToProve[i][j] != 0 && possibleMatrix[i][j] != 0) {
					countHits += matrixToProve[i][j];
				}
			}
		}
		int erg = (int) ((countHits) / (double) maxHits * 100);
		// System.out.println((countHits) + " / " + maxHits + " = " + erg +
		// "%");
		return erg;
	}

	/**
	 * @param text
	 * @param kb
	 */
	private KnowledgeBase generateKnowledgeBase(String text, String theme, String[] vocabulary) {
		KnowledgeBase kb = new KnowledgeBase(theme, vocabulary);
		List<String[]> splittetText = Analyser.getInstance().splitText(text);
		for (String[] words : splittetText) {
			for (int i = 0; words.length > i; i++) {
				if (words.length > i + 1) {
					kb.add(words[i], words[i + 1]);
				} else {
					kb.add(words[i]);
				}
			}
		}
		return kb;
	}

	/**
	 * @param vocabulary
	 */
	private List<KnowledgeBase> possibleThemeByVocabulary(String[] vocabulary) {
		List<KnowledgeBase> out = new ArrayList<KnowledgeBase>();
		for (String key : knowledge.keySet()) {
			KnowledgeBase v = knowledge.get(key);
			int countFails = 0;
			for (int i = 0; i < vocabulary.length; i++) {
				String word = vocabulary[i];
				if (!ArrayUtils.contains(v.getVocabulary(), word)) {
					countFails++;
				}
			}

			int erg = (int) (countFails / (double) vocabulary.length * 100);
			// System.out.println(">" + v.getName() + "< = " + (100 - erg) +
			// "%");
			if (erg <= 100 - succesQuota) {
				out.add(v);
			}
		}
		// out.forEach(k -> {
		// System.out.println("Possible KnowledgeBase>" + k.getName() + "<");
		// });
		return out;
	}
}
