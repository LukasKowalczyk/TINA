package de.tina;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import de.tina.master.Master;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TinaTests {
	private static final String SOURCE_PATH = System.getProperty("user.dir");

	private static final boolean PRE_FILTER = false;

	private static final int SUCCES_QUOTA = 80;

	@Test
	public void contextLoads() {
		Master master = new Master(SOURCE_PATH, PRE_FILTER, SUCCES_QUOTA);
		master.learn("Hallo wie geht es dir?", "Begr��ung");
		master.learn("Geht es dir gut?", "Begr��ung");
		master.learn("Hallo du, Geht es dir gut?", "Begr��ung");
		master.learn("Hi, wie geht's?", "Begr��ung");
		master.learn("Darf ich k�ndigen?", "Frage");
		master.learn("Muss ich k�ndigen?", "Frage");
		master.learn("Muss ich k�ndigen?", "Frage");
		master.learn("Darf ich k�ndigen?", "K�ndigung");
		master.learn("Muss ich k�ndigen?", "K�ndigung");
		master.learn("Muss ich k�ndigen?", "K�ndigung");
		master.learn("Ich will k�ndigen.", "K�ndigung");
		master.learn("Ich k�ndige!", "K�ndigung");
		master.learn("Hallo, ich k�ndige.", "K�ndigung");
		master.learn("Hallo, ich will k�ndigen!", "K�ndigung");
		master.learn("Hallo, darf ich k�ndigen?", "K�ndigung");
		master.finish();

		printInfo(master.ask("Hallo, ich k�ndige!"));
		printInfo(master.ask("Darf ich k�ndigen?"));
		printInfo(master.ask("Hallo ich, ich will k�ndigen !"));
		printInfo(master.ask("Hallo Du, wie geht es dir?"));
	}

	private static void printInfo(Map<String, Integer> themes) {
		for (String key : themes.keySet()) {
			System.out.println("Is " + themes.get(key) + "% >" + key + "<");
		}
		System.out.println();
	}

}
