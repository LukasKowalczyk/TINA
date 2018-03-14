package de.tina;

import static org.junit.Assert.assertEquals;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.tina.container.NeuronMatrix;
import de.tina.controller.Memory;
import de.tina.knowledge.JsonFilenameFilter;

public class MemoryTest {
	private static final File TEST_SOURCER_PATH = new File("D:\\test\\");
	@Autowired
	private Memory memory;
	private Map<String, NeuronMatrix> knowledge = new HashMap<>();

	public void setUpBefore() {
		knowledge = new HashMap<>();
		NeuronMatrix knowledgeBase1 = new NeuronMatrix("Test");
		knowledgeBase1.add("Hallo", "wie");
		NeuronMatrix knowledgeBase2 = new NeuronMatrix("Test1");
		knowledgeBase2.add("Hallo", "wie");
		knowledge.put("Test", knowledgeBase1);
		knowledge.put("Test1", knowledgeBase2);
	}

	public void tearDownAfter() {
		for (File file : TEST_SOURCER_PATH.listFiles(new JsonFilenameFilter())) {
			file.delete();
		}
	}

	@Test
	public void persist() {
		setUpBefore();
		memory.persist(knowledge);
		assertEquals(TEST_SOURCER_PATH.list(new JsonFilenameFilter()).length, 2);
		tearDownAfter();
	}

	@Test
	public void load() {
		setUpBefore();
		memory.persist(knowledge);
		Map<String, NeuronMatrix> loadKnowledge = memory.remember();
		assertEquals(knowledge, loadKnowledge);
		tearDownAfter();
	}

}
