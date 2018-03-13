package de.tina.knowledge;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;

public class Memory {

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
		for (File file : sourcePath.listFiles(new JsonFilenameFilter())) {
			// Map the JSON in the Knowledge
			KnowledgeBase bap = load(file);
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
			persist(knowledgeBase, new File(sourcePath, knowledgeBase.getName() + JsonFilenameFilter.JSON_EXTENSION));
			// System.out.println(knowledgeBase.print());
		}

	}

	/**
	 * saves this object as a JSON-File
	 * 
	 * @param jsonFile
	 */
	private void persist(KnowledgeBase knowledgeBase, File jsonFile) {
		// Here the json file must be written
		try {
			FileUtils.writeByteArrayToFile(jsonFile, new Gson().toJson(knowledgeBase).getBytes(), false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * loads a JSON-File
	 * 
	 * @param jsonFile
	 * @return the mapped JSON-File
	 */
	private KnowledgeBase load(File jsonFile) {
		// Here the json file must be written
		try {
			String inhalt = new String(FileUtils.readFileToByteArray(jsonFile));
			KnowledgeBase bareaPojo = new Gson().fromJson(inhalt, KnowledgeBase.class);
			return bareaPojo;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
