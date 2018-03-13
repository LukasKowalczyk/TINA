package de.tina;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.tina.apprentice.Apprentice;
import de.tina.knowledge.JsonFilenameFilter;
import de.tina.knowledge.KnowledgeBase;
import de.tina.knowledge.Memory;

public class ApprenticeTest {
	private static final String TEST_SOURCE_PATH = "D:\\test\\";

	@BeforeClass
	@AfterClass
	public static void deleteFiles() {
		for (File file : new File(TEST_SOURCE_PATH).listFiles(new JsonFilenameFilter())) {
			file.delete();
		}
	}

	@Test
	public void testBegruessung() {
		deleteFiles();
		Apprentice apprentice = new Apprentice(TEST_SOURCE_PATH);
		apprentice.learn("Hallo wie geht es dir?", "Begrüßung");
		apprentice.learn("Geht es dir gut?", "Begrüßung");
		apprentice.finish();
		Map<String, KnowledgeBase> knowledge = Memory.getInstance().remember(new File(TEST_SOURCE_PATH));
		assertTrue(knowledge.containsKey("Begrüßung"));
		assertTrue(knowledge.values().size() == 1);
	}

	@Test
	public void testBegruessungAndKuedigung() {
		deleteFiles();
		Apprentice apprentice = new Apprentice(TEST_SOURCE_PATH);
		apprentice.learn("Hallo wie geht es dir?", "Begrüßung");
		apprentice.learn("Hallo, ich kündige.", "Kündigung");
		apprentice.learn("Hallo, ich möchte kündigen.", "Kündigung");
		apprentice.finish();
		Map<String, KnowledgeBase> knowledge = Memory.getInstance().remember(new File(TEST_SOURCE_PATH));
		assertTrue(knowledge.containsKey("Begrüßung") && knowledge.containsKey("Kündigung"));
		assertTrue(knowledge.values().size() == 2);
	}

	@Test
	public void testWithoutFinish() {
		deleteFiles();
		Apprentice apprentice = new Apprentice(TEST_SOURCE_PATH);
		apprentice.learn("Hallo wie geht es dir?", "Begrüßung");
		apprentice.learn("Hallo, ich kündige.", "Kündigung");
		Map<String, KnowledgeBase> knowledge = Memory.getInstance().remember(new File(TEST_SOURCE_PATH));
		assertFalse(knowledge.containsKey("Begrüßung"));
		assertFalse(knowledge.containsKey("Kündigung"));
		assertTrue(knowledge.values().size() == 0);
	}
}
