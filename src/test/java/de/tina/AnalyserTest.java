package de.tina;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import de.tina.knowledge.Analyser;
import de.tina.knowledge.KnowledgeBase;

public class AnalyserTest {

	@Autowired
	private Analyser analyser;

	@Test
	public void testKuedigenText() {
		KnowledgeBase erg = analyser.fillTheKnowledgeBase("Hallo, ich will k�ndigen!",
				new KnowledgeBase("Test"));
		assertEquals(erg.getVocabulary().length, 2);
		assertTrue(ArrayUtils.contains(erg.getVocabulary(), "hallo"));
		assertTrue(ArrayUtils.contains(erg.getVocabulary(), "k�ndigen"));
		assertEquals(erg.getName(), "Test");
		assertEquals(erg.getAdjiazenMatrix().length, 2);
		assertEquals(erg.getAdjiazenMatrix()[0].length, 2);
		assertEquals(erg.getMax(), -1);
	}

	@Test
	public void testKuedigenFrageText() {
		KnowledgeBase erg = analyser.fillTheKnowledgeBase("Hallo, darf ich k�ndigen?",
				new KnowledgeBase("Test"));
		assertEquals(erg.getVocabulary().length, 3);
		assertTrue(ArrayUtils.contains(erg.getVocabulary(), "hallo"));
		assertTrue(ArrayUtils.contains(erg.getVocabulary(), "k�ndigen"));
		assertTrue(ArrayUtils.contains(erg.getVocabulary(), "darf"));
		assertEquals(erg.getName(), "Test");
		assertEquals(erg.getAdjiazenMatrix().length, 3);
		assertEquals(erg.getAdjiazenMatrix()[0].length, 3);
		assertEquals(erg.getMax(), -2);
	}

	@Test
	public void testStopWords() throws IOException {
		List<String> stopwords = Files.readAllLines(new File("stopwords").toPath(), StandardCharsets.ISO_8859_1);
		String stopwordsText = "";
		for (String s : stopwords) {
			stopwordsText += s + " ";
		}
		//z.b. bsp. and zb. must becoded that will dont be splittet
		KnowledgeBase erg = analyser.fillTheKnowledgeBase(stopwordsText, new KnowledgeBase("Test"));
		assertEquals(erg.getVocabulary().length, 0);
		assertEquals(erg.getName(), "Test");
	}
}
