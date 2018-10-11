package de.tina;

import static org.junit.Assert.assertEquals;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import de.tina.container.NeuronMatrix;
import de.tina.controller.Memory;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MemoryTest {

    @Autowired

    private Memory memory;

    private Map<String, NeuronMatrix> knowledge = new HashMap<>();

    public void setUpBefore() {
        knowledge = new HashMap<>();
        NeuronMatrix knowledgeBase1 = new NeuronMatrix("Test");
        knowledgeBase1.add(1L, 2L);
        NeuronMatrix knowledgeBase2 = new NeuronMatrix("Test1");
        knowledgeBase2.add(1L, 2L);
        knowledge.put("Test", knowledgeBase1);
        knowledge.put("Test1", knowledgeBase2);
    }

    public void tearDownAfter() {
        memory.deleteAll();
    }

    @Test
    public void persist() {
        setUpBefore();
        memory.persist(knowledge);
        assertEquals(memory.remember().size(), 2);
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
