package de.tina;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import de.tina.container.NeuronMatrix;
import de.tina.controller.Master;
import de.tina.controller.Memory;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MasterTest {

    @Autowired
    private Master master;

    @Autowired
    private Memory memory;

    @Before
    public void setUpBeforeClass() throws Exception {
        memory.deleteAll();
        master.learn("Hallo wie geht es dir?", "Begrüßung");
        master.learn("Geht es dir gut?", "Begrüßung");
        master.learn("Hallo, ich kündige.", "Kündigung");
        master.learn("Hallo, ich möchte kündigen.", "Kündigung");
        master.persist();
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
        memory.deleteAll();
        master.learn("Hallo wie geht es dir?", "Begrüßung");
        master.learn("Geht es dir gut?", "Begrüßung");
        master.persist();
        Map<String, NeuronMatrix> knowledge = memory.remember();
        assertTrue(knowledge.containsKey("Begrüßung"));
        assertTrue(knowledge.values().size() == 2);
    }

    @Test
    public void testBegruessungAndKuedigung() {
        memory.deleteAll();
        master.learn("Hallo wie geht es dir?", "Begrüßung");
        master.learn("Hallo, ich kündige.", "Kündigung");
        master.learn("Hallo, ich möchte kündigen.", "Kündigung");
        master.persist();
        Map<String, NeuronMatrix> knowledge = memory.remember();
        assertTrue(knowledge.containsKey("Begrüßung") && knowledge.containsKey("Kündigung"));
        assertTrue(knowledge.values().size() == 2);
    }

    @Test
    public void testWithoutFinish() {
        memory.deleteAll();
        master.learn("Hallo wie geht es dir?", "Begrüßung");
        master.learn("Hallo, ich kündige.", "Kündigung");
        Map<String, NeuronMatrix> knowledge = memory.remember();
        assertFalse(knowledge.containsKey("Begrüßung"));
        assertFalse(knowledge.containsKey("Kündigung"));
        assertTrue(knowledge.values().size() == 0);
    }
}
