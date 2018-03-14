package de.tina;

import java.io.File;
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
    public void testKuendigung() {
        Master master = new Master(TEST_SOURCE_PATH, PRE_FILTER, HIT_QUOTA);
        master.ask("Hallo, ich k�ndige.");
    }

}
