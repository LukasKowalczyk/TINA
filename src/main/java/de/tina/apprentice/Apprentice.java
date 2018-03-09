package de.tina.apprentice;

import java.io.File;
import java.util.Map;
import de.tina.knowledge.Analyser;
import de.tina.knowledge.KnowledgeBase;
import de.tina.knowledge.Memory;

public class Apprentice {
	private Memory memory = Memory.getInstance();

	private Analyser analyser = Analyser.getInstance();

	private String sourcePath;

	private Map<String, KnowledgeBase> knowledge;

	/**
	 * @param sourcePath
	 */
	public Apprentice(String sourcePath) {
		this.sourcePath = sourcePath;
		knowledge = memory.remember(new File(sourcePath));
	}

	/**
	 * teach the apprentice that this text is this theme
	 * 
	 * @param text
	 * @param theme
	 */
	public void learn(String text, String theme) {
		// First of all we check if its a theme we already know
		if (!knowledge.containsKey(theme)) {
			knowledge.put(theme, new KnowledgeBase(theme));
		}
		// System.out.println("I learning that >" + text + "< is >" + theme +
		// "<!");
		KnowledgeBase knowledgeBase = analyser.fillTheKnowledgeBase(text, knowledge.get(theme));
		knowledge.put(theme, knowledgeBase);
	}

	public void finish() {
		memory.persist(knowledge, new File(sourcePath));
	}

}
