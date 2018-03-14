package de.tina;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.io.File;
import java.util.Map;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import de.tina.knowledge.JsonFilenameFilter;
import de.tina.knowledge.KnowledgeBase;
import de.tina.knowledge.Memory;
import de.tina.master.Master;

public class MasterTest {
    private static final int HIT_QUOTA = 90;

    private static final boolean PRE_FILTER = false;

    private static final String TEST_SOURCE_PATH = "C:\\temp\\";

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        for (File file : new File(TEST_SOURCE_PATH).listFiles(new JsonFilenameFilter())) {
            file.delete();
        }
        Master apprentice = new Master(TEST_SOURCE_PATH, PRE_FILTER, HIT_QUOTA);
        apprentice.learn("Hallo wie geht es dir?", "Begr��ung");
        apprentice.learn("Geht es dir gut?", "Begr��ung");
        apprentice.learn("Hallo, ich k�ndige.", "K�ndigung");
        apprentice.learn("Hallo, ich m�chte k�ndigen.", "K�ndigung");
        apprentice.finish();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        for (File file : new File(TEST_SOURCE_PATH).listFiles(new JsonFilenameFilter())) {
            file.delete();
        }
    }

    @Test
    public void testTrue() {
        Master master = new Master(TEST_SOURCE_PATH, PRE_FILTER, HIT_QUOTA);
        Map<String, Integer> erg = master.ask("Hallo, ich k�ndige.");
        assertTrue(erg.containsKey("K�ndigung"));
    }

    @Test
    public void testFalse() {
        Master master = new Master(TEST_SOURCE_PATH, PRE_FILTER, HIT_QUOTA);
        Map<String, Integer> erg = master.ask("Hallo, ich hei�e Horst.");
        assertTrue(erg.size() == 0);
        assertFalse(erg.containsKey("K�ndigung"));
        assertFalse(erg.containsKey("Begr��ung"));
    }

    @Test
    public void testBegruessung() {
        try {
            tearDownAfterClass();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Master apprentice = new Master(TEST_SOURCE_PATH, PRE_FILTER, HIT_QUOTA);
        apprentice.learn("Hallo wie geht es dir?", "Begr��ung");
        apprentice.learn("Geht es dir gut?", "Begr��ung");
        apprentice.finish();
        Map<String, KnowledgeBase> knowledge = Memory.getInstance(new File(TEST_SOURCE_PATH)).remember();
        assertTrue(knowledge.containsKey("Begr��ung"));
        assertTrue(knowledge.values().size() == 1);
    }

    @Test
    public void testBegruessungAndKuedigung() {
        try {
            tearDownAfterClass();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Master apprentice = new Master(TEST_SOURCE_PATH, PRE_FILTER, HIT_QUOTA);
        apprentice.learn("Hallo wie geht es dir?", "Begr��ung");
        apprentice.learn("Hallo, ich k�ndige.", "K�ndigung");
        apprentice.learn("Hallo, ich m�chte k�ndigen.", "K�ndigung");
        apprentice.finish();
        Map<String, KnowledgeBase> knowledge = Memory.getInstance(new File(TEST_SOURCE_PATH)).remember();
        assertTrue(knowledge.containsKey("Begr��ung") && knowledge.containsKey("K�ndigung"));
        assertTrue(knowledge.values().size() == 2);
    }

    @Test
    public void testWithoutFinish() {
        try {
            tearDownAfterClass();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Master apprentice = new Master(TEST_SOURCE_PATH, PRE_FILTER, HIT_QUOTA);
        apprentice.learn("Hallo wie geht es dir?", "Begr��ung");
        apprentice.learn("Hallo, ich k�ndige.", "K�ndigung");
        Map<String, KnowledgeBase> knowledge = Memory.getInstance(new File(TEST_SOURCE_PATH)).remember();
        assertFalse(knowledge.containsKey("Begr��ung"));
        assertFalse(knowledge.containsKey("K�ndigung"));
        assertTrue(knowledge.values().size() == 0);
    }
}
