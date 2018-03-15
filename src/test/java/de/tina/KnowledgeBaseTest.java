package de.tina;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import de.tina.container.NeuronMatrix;

public class KnowledgeBaseTest {

    @Test
    public void comparteTwoKnowledgesPositiv() {
        NeuronMatrix knowledgeBase1 = new NeuronMatrix("Test");
        knowledgeBase1.add(1L, 2L);
        NeuronMatrix knowledgeBase2 = new NeuronMatrix("Test");
        knowledgeBase2.add(1L, 2L);
        assertTrue(knowledgeBase1.equals(knowledgeBase2));
    }

    @Test
    public void comparteTwoKnowledgesNegativ() {
        NeuronMatrix knowledgeBase1 = new NeuronMatrix("Test");
        knowledgeBase1.add(1L, 2L);
        NeuronMatrix knowledgeBase2 = new NeuronMatrix("Test1");
        knowledgeBase2.add(1L, 2L);
        NeuronMatrix knowledgeBase3 = new NeuronMatrix("Test");
        knowledgeBase1.add(3L, 2L);
        NeuronMatrix knowledgeBase4 = new NeuronMatrix("Test");
        knowledgeBase1.add(1L);
        assertFalse(knowledgeBase1.equals(knowledgeBase2));
        assertFalse(knowledgeBase1.equals(knowledgeBase3));
        assertFalse(knowledgeBase1.equals(knowledgeBase4));
    }

    @Test
    public void appendWord() {
        NeuronMatrix knowledgeBase = new NeuronMatrix("Test");
        knowledgeBase.add(1L);
        assertEquals(knowledgeBase.getName(), "Test");
        assertEquals(knowledgeBase.getNeuronIds().length, 1);
        assertEquals(knowledgeBase.getAdjiazenMatrix().length, 1);
        assertEquals(knowledgeBase.getAdjiazenMatrix()[0].length, 1);
        assertEquals(knowledgeBase.getMax(), 0);
    }

    @Test
    public void appendTwoWords() {
        NeuronMatrix knowledgeBase = new NeuronMatrix("Test");
        knowledgeBase.add(1L, 2L);
        assertEquals(knowledgeBase.getName(), "Test");
        assertEquals(knowledgeBase.getNeuronIds().length, 2);
        assertEquals(knowledgeBase.getAdjiazenMatrix().length, 2);
        assertEquals(knowledgeBase.getAdjiazenMatrix()[0].length, 2);
        assertEquals(knowledgeBase.getMax(), -1);
    }

}
