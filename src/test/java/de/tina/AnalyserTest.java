package de.tina;

import static org.junit.Assert.assertEquals;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import de.tina.container.NeuronMatrix;
import de.tina.controller.Analyser;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AnalyserTest {

    @Autowired
    private Analyser analyser;

    @Test
    public void testKuedigenText() {
        NeuronMatrix erg = analyser.fillTheKnowledgeBase("Hallo, ich will k�ndigen!", new NeuronMatrix("Test"));
        assertEquals(erg.getNeuronIds().length, 2);
        assertEquals(erg.getName(), "Test");
        assertEquals(erg.getAdjiazenMatrix().length, 2);
        assertEquals(erg.getAdjiazenMatrix()[0].length, 2);
        assertEquals(erg.getMax(), -1);
    }

    @Test
    public void testKuedigenFrageText() {
        NeuronMatrix erg = analyser.fillTheKnowledgeBase("Hallo, darf ich k�ndigen?", new NeuronMatrix("Test"));
        assertEquals(erg.getNeuronIds().length, 3);
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
        // z.b. bsp. and zb. must becoded that will dont be splittet
        NeuronMatrix erg = analyser.fillTheKnowledgeBase(stopwordsText, new NeuronMatrix("Test"));
        assertEquals(erg.getNeuronIds().length, 0);
        assertEquals(erg.getName(), "Test");
    }
}
