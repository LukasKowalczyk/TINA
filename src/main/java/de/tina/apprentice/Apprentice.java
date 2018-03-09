package de.tina.apprentice;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import de.tina.knowledge.Analyser;
import de.tina.knowledge.KnowledgeBase;

public class Apprentice {

	private  String sourcePath;

	private Map<String, KnowledgeBase> knowledge;

	/**
	 *
	 */
	public Apprentice(String sourcePath) {
		this.sourcePath=sourcePath;
		knowledge = new HashMap<String, KnowledgeBase>();
		remember();
	}

	public void learn(String text, String theme) {
		if (!knowledge.containsKey(theme)) {
			knowledge.put(theme, new KnowledgeBase(theme));
		}
		// System.out.println("I learning that >" + text + "< is >" + theme +
		// "<!");
		KnowledgeBase knowledgeBase = knowledge.get(theme);
		List<String[]> splittetText = Analyser.getInstance().splitText(text);
		for (String[] words : splittetText) {
			for (int i = 0; words.length > i; i++) {
				if (words.length > i + 1) {
					knowledgeBase.add(words[i], words[i + 1]);
				} else {
					knowledgeBase.add(words[i]);
				}
			}
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

	public void finish() {
		// System.out.println("This is what i learned >>>>");
		Iterator<String> i = knowledge.keySet().iterator();
		while (i.hasNext()) {
			KnowledgeBase knowledgeBase = knowledge.get(i.next());
			knowledgeBase.persist(new File(new File(sourcePath), knowledgeBase.getName() + ".json"));
			// System.out.println(knowledgeBase.print());
		}

	}
}
