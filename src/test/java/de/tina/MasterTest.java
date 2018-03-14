package de.tina;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.io.File;
import java.util.Map;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.tina.knowledge.JsonFilenameFilter;
import de.tina.knowledge.KnowledgeBase;
import de.tina.knowledge.Memory;
import de.tina.master.Master;

public class MasterTest {

	private static final String TEST_SOURCE_PATH = "C:\\temp\\";
	@Autowired
	private static Master master;
	@Autowired
	private static Memory memory;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		for (File file : new File(TEST_SOURCE_PATH).listFiles(new JsonFilenameFilter())) {
			file.delete();
		}
		master.learn("Hallo wie geht es dir?", "Begrüßung");
		master.learn("Geht es dir gut?", "Begrüßung");
		master.learn("Hallo, ich kündige.", "Kündigung");
		master.learn("Hallo, ich möchte kündigen.", "Kündigung");
		master.finish();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		for (File file : new File(TEST_SOURCE_PATH).listFiles(new JsonFilenameFilter())) {
			file.delete();
		}
	}

	@Test
	public void testTrue() {
		Map<String, Integer> erg = master.ask("Hallo, ich kündige.");
		assertTrue(erg.containsKey("Kündigung"));
	}

	@Test
	public void testFalse() {
		Map<String, Integer> erg = master.ask("Hallo, ich heiße Horst.");
		assertTrue(erg.size() == 0);
		assertFalse(erg.containsKey("Kündigung"));
		assertFalse(erg.containsKey("Begrüßung"));
	}

	@Test
	public void testBegruessung() {
		try {
			tearDownAfterClass();
		} catch (Exception e) {
			e.printStackTrace();
		}
		master.learn("Hallo wie geht es dir?", "Begrüßung");
		master.learn("Geht es dir gut?", "Begrüßung");
		master.finish();
		Map<String, KnowledgeBase> knowledge = memory.remember();
		assertTrue(knowledge.containsKey("Begrüßung"));
		assertTrue(knowledge.values().size() == 1);
	}

	@Test
	public void testBegruessungAndKuedigung() {
		try {
			tearDownAfterClass();
		} catch (Exception e) {
			e.printStackTrace();
		}
		master.learn("Hallo wie geht es dir?", "Begrüßung");
		master.learn("Hallo, ich kündige.", "Kündigung");
		master.learn("Hallo, ich möchte kündigen.", "Kündigung");
		master.finish();
		Map<String, KnowledgeBase> knowledge = memory.remember();
		assertTrue(knowledge.containsKey("Begrüßung") && knowledge.containsKey("Kündigung"));
		assertTrue(knowledge.values().size() == 2);
	}

	@Test
	public void testWithoutFinish() {
		try {
			tearDownAfterClass();
		} catch (Exception e) {
			e.printStackTrace();
		}
		master.learn("Hallo wie geht es dir?", "Begrüßung");
		master.learn("Hallo, ich kündige.", "Kündigung");
		Map<String, KnowledgeBase> knowledge = memory.remember();
		assertFalse(knowledge.containsKey("Begrüßung"));
		assertFalse(knowledge.containsKey("Kündigung"));
		assertTrue(knowledge.values().size() == 0);
	}
}
