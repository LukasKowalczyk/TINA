package de.tina.knowledge;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

@Component
public class Memory {

    @Value("${source.path}")
    private String sourcePath;

    /**
     * Loads the knowledge of the sourcePath
     * @return a Map with the Name of the knowledgeBase as Key an the knowledgeBase as Value
     */
    public Map<String, KnowledgeBase> remember() {
        Map<String, KnowledgeBase> knowledge = new HashMap<>();
        // load every JSON-File
        for (File file : getSourcePath().listFiles(new JsonFilenameFilter())) {
            // Map the JSON in the Knowledge
            KnowledgeBase bap = load(file);
            if (bap != null) {
                knowledge.put(bap.getName(), bap);
            }
        }
        return knowledge;
    }

    private File getSourcePath() {
		return new File(sourcePath);
	}

	/**
     * Lets persist our knowledge into the sourcePath
     * @param knowledge
     * @param sourcePath
     */
    public void persist(Map<String, KnowledgeBase> knowledge) {
        // System.out.println("This is what i learned >>>>");
        Iterator<String> i = knowledge.keySet().iterator();
        while (i.hasNext()) {
            KnowledgeBase knowledgeBase = knowledge.get(i.next());
            persist(knowledgeBase, new File(sourcePath, knowledgeBase.getName() + JsonFilenameFilter.JSON_EXTENSION));
            // System.out.println(knowledgeBase.print());
        }

    }

    /*
     * Saves this object as a JSON-File
     */
    private void persist(KnowledgeBase knowledgeBase, File jsonFile) {
        // Here the json file must be written
        try {
            FileUtils.writeByteArrayToFile(jsonFile, new Gson().toJson(knowledgeBase).getBytes(), false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * loads a JSON-File
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
