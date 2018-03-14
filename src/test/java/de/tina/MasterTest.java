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
        Apprentice apprentice = new Apprentice(TEST_SOURCE_PATH);
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

}
