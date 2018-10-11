package de.tina;

import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import de.tina.controller.GraphPainter;
import de.tina.controller.Master;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TinaTests {

    @Autowired
    Master master;

    @Autowired
    GraphPainter graphPainter;

    @Test
    public void contextLoads() {
        master.learn("Hallo wie geht es dir?", "Begruessung");
        master.learn("Geht es dir gut?", "Begruessung");
        master.learn("Hallo du, Geht es dir gut?", "Begruessung");
        master.learn("Hi, wie geht's?", "Begruessung");
        master.learn("Darf ich kuendigen?", "Frage");
        master.learn("Muss ich kuendigen?", "Frage");
        master.learn("Muss ich kuendigen?", "Frage");
        master.learn("Darf ich kuendigen?", "Kuendigung");
        master.learn("Muss ich kuendigen?", "Kuendigung");
        master.learn("Muss ich kuendigen?", "Kuendigung");
        master.learn("Ich will kuendigen.", "Kuendigung");
        master.learn("Ich kuendige!", "Kuendigung");
        master.learn("Hallo, ich kuendige.", "Kuendigung");
        master.learn("Hallo, ich will kuendigen!", "Kuendigung");
        master.learn("Hallo, darf ich kuendigen?", "Kuendigung");
        master.persist();

        graphPainter.setNodes(master.getAllNeuron());

        master.getKnowledge().values().forEach(m -> graphPainter.paintGraph(m));

        printInfo(master.ask("Hallo, ich kuendige!"));
        printInfo(master.ask("Darf ich kuendigen?"));
        printInfo(master.ask("Hallo ich, ich will kuendigen !"));
        printInfo(master.ask("Hallo Du, wie geht es dir?"));
    }

    private static void printInfo(Map<String, Integer> themes) {
        for (String key : themes.keySet()) {
            System.out.println("Is " + themes.get(key) + "% >" + key + "<");
        }
        System.out.println();
    }

}
