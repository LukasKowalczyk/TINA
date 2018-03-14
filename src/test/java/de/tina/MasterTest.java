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
        apprentice.learn("Hallo wie geht es dir?", "Begrüßung");
        apprentice.learn("Geht es dir gut?", "Begrüßung");
        apprentice.learn("Hallo, ich kündige.", "Kündigung");
        apprentice.learn("Hallo, ich möchte kündigen.", "Kündigung");
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
        Map<String, Integer> erg = master.ask("Hallo, ich kündige.");
        assertTrue(erg.containsKey("Kündigung"));
    }

    @Test
    public void testFalse() {
        Master master = new Master(TEST_SOURCE_PATH, PRE_FILTER, HIT_QUOTA);
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
        Master apprentice = new Master(TEST_SOURCE_PATH, PRE_FILTER, HIT_QUOTA);
        apprentice.learn("Hallo wie geht es dir?", "Begrüßung");
        apprentice.learn("Geht es dir gut?", "Begrüßung");
        apprentice.finish();
        Map<String, KnowledgeBase> knowledge = Memory.getInstance(new File(TEST_SOURCE_PATH)).remember();
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
        Master apprentice = new Master(TEST_SOURCE_PATH, PRE_FILTER, HIT_QUOTA);
        apprentice.learn("Hallo wie geht es dir?", "Begrüßung");
        apprentice.learn("Hallo, ich kündige.", "Kündigung");
        apprentice.learn("Hallo, ich möchte kündigen.", "Kündigung");
        apprentice.finish();
        Map<String, KnowledgeBase> knowledge = Memory.getInstance(new File(TEST_SOURCE_PATH)).remember();
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
        Master apprentice = new Master(TEST_SOURCE_PATH, PRE_FILTER, HIT_QUOTA);
        apprentice.learn("Hallo wie geht es dir?", "Begrüßung");
        apprentice.learn("Hallo, ich kündige.", "Kündigung");
        Map<String, KnowledgeBase> knowledge = Memory.getInstance(new File(TEST_SOURCE_PATH)).remember();
        assertFalse(knowledge.containsKey("Begrüßung"));
        assertFalse(knowledge.containsKey("Kündigung"));
        assertTrue(knowledge.values().size() == 0);
    }
}
