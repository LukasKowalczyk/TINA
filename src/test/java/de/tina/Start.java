package de.tina;

import java.util.Map;

import de.tina.apprentice.Apprentice;
import de.tina.master.Master;

public class Start {

	private static final String SOURCE_PATH = System.getProperty("user.dir");

	private static final boolean PRE_FILTER = false;

	private static final int SUCCES_QUOTA = 80;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Apprentice apprentice = new Apprentice(SOURCE_PATH);
		apprentice.learn("Hallo wie geht es dir?", "Begr��ung");
		apprentice.learn("Geht es dir gut?", "Begr��ung");
		apprentice.learn("Hallo du, Geht es dir gut?", "Begr��ung");
		apprentice.learn("Hi, wie geht's?", "Begr��ung");
		apprentice.learn("Darf ich k�ndigen?", "Frage");
		apprentice.learn("Muss ich k�ndigen?", "Frage");
		apprentice.learn("Muss ich k�ndigen?", "Frage");
		apprentice.learn("Darf ich k�ndigen?", "K�ndigung");
		apprentice.learn("Muss ich k�ndigen?", "K�ndigung");
		apprentice.learn("Muss ich k�ndigen?", "K�ndigung");
		apprentice.learn("Ich will k�ndigen.", "K�ndigung");
		apprentice.learn("Ich k�ndige!", "K�ndigung");
		apprentice.learn("Hallo, ich k�ndige.", "K�ndigung");
		apprentice.learn("Hallo, ich will k�ndigen!", "K�ndigung");
		apprentice.learn("Hallo, darf ich k�ndigen?", "K�ndigung");
		apprentice.finish();
		Master master = new Master(SOURCE_PATH, PRE_FILTER, SUCCES_QUOTA);
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
