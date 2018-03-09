package de.tina.knowledge;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Memory {
	
	private static final String JSON_EXTENSION = ".json";
	
	private static Memory memory;

	private Memory() {
	}

	public static Memory getInstance() {
		if (memory == null) {
			memory = new Memory();
		}
		return memory;
	}

	public Map<String, KnowledgeBase> remember(File sourcePath) {
		Map<String, KnowledgeBase> knowledge = new HashMap<>();
		// load every JSON-File
		for (File file : sourcePath.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(JSON_EXTENSION);
			}

		})) {
			// Map the JSON in the Knowledge
			KnowledgeBase bap = KnowledgeBase.load(file);
			if (bap != null) {
				knowledge.put(bap.getName(), bap);
			}
		}
		return knowledge;
	}

	/**
	 * lets persist our knowledge
	 */
	public void persist(Map<String, KnowledgeBase> knowledge, File sourcePath) {
		// System.out.println("This is what i learned >>>>");
		Iterator<String> i = knowledge.keySet().iterator();
		while (i.hasNext()) {
			KnowledgeBase knowledgeBase = knowledge.get(i.next());
			knowledgeBase.persist(new File(sourcePath, knowledgeBase.getName() + JSON_EXTENSION));
			// System.out.println(knowledgeBase.print());
		}

	}
}
