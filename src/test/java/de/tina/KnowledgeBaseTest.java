package de.tina;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.tina.knowledge.KnowledgeBase;

public class KnowledgeBaseTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void comparteTwoKnowledgesPositiv() {
		KnowledgeBase knowledgeBase1 = new KnowledgeBase("Test");
		knowledgeBase1.add("Hallo", "wie");
		KnowledgeBase knowledgeBase2 = new KnowledgeBase("Test");
		knowledgeBase2.add("Hallo", "wie");
		assertTrue(knowledgeBase1.equals(knowledgeBase2));
	}
	
	@Test
	public void comparteTwoKnowledgesNegativ() {
		KnowledgeBase knowledgeBase1 = new KnowledgeBase("Test");
		knowledgeBase1.add("Hallo", "wie");
		KnowledgeBase knowledgeBase2 = new KnowledgeBase("Test1");
		knowledgeBase2.add("Hallo", "wie");
		KnowledgeBase knowledgeBase3 = new KnowledgeBase("Test");
		knowledgeBase1.add("Hi", "wie");
		KnowledgeBase knowledgeBase4 = new KnowledgeBase("Test");
		knowledgeBase1.add("Hallo");
		assertFalse(knowledgeBase1.equals(knowledgeBase2));
		assertFalse(knowledgeBase1.equals(knowledgeBase3));
		assertFalse(knowledgeBase1.equals(knowledgeBase4));
	}
	@Test
	public void appendWord() {
		KnowledgeBase knowledgeBase = new KnowledgeBase("Test");
		knowledgeBase.add("Hallo");
		assertEquals(knowledgeBase.getName(), "Test");
		assertEquals(knowledgeBase.getVocabulary().length, 1);
		assertEquals(knowledgeBase.getAdjiazenMatrix().length, 1);
		assertEquals(knowledgeBase.getAdjiazenMatrix()[0].length, 1);
		assertEquals(knowledgeBase.getMax(), 0);
	}

	@Test
	public void appendTwoWords() {
		KnowledgeBase knowledgeBase = new KnowledgeBase("Test");
		knowledgeBase.add("Hallo", "wie");
		assertEquals(knowledgeBase.getName(), "Test");
		assertEquals(knowledgeBase.getVocabulary().length, 2);
		assertEquals(knowledgeBase.getAdjiazenMatrix().length, 2);
		assertEquals(knowledgeBase.getAdjiazenMatrix()[0].length, 2);
		assertEquals(knowledgeBase.getMax(), -1);
	}

}
