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
		apprentice.learn("Hallo wie geht es dir?", "Begrüßung");
		apprentice.learn("Geht es dir gut?", "Begrüßung");
		apprentice.learn("Hallo du, Geht es dir gut?", "Begrüßung");
		apprentice.learn("Hi, wie geht's?", "Begrüßung");
		apprentice.learn("Darf ich kündigen?", "Frage");
		apprentice.learn("Muss ich kündigen?", "Frage");
		apprentice.learn("Muss ich kündigen?", "Frage");
		apprentice.learn("Darf ich kündigen?", "Kündigung");
		apprentice.learn("Muss ich kündigen?", "Kündigung");
		apprentice.learn("Muss ich kündigen?", "Kündigung");
		apprentice.learn("Ich will kündigen.", "Kündigung");
		apprentice.learn("Ich kündige!", "Kündigung");
		apprentice.learn("Hallo, ich kündige.", "Kündigung");
		apprentice.learn("Hallo, ich will kündigen!", "Kündigung");
		apprentice.learn("Hallo, darf ich kündigen?", "Kündigung");
		apprentice.finish();
		Master master = new Master(SOURCE_PATH, PRE_FILTER, SUCCES_QUOTA);
		printInfo(master.ask("Hallo, ich kündige!"));
		printInfo(master.ask("Darf ich kündigen?"));
		printInfo(master.ask("Hallo ich, ich will kündigen !"));
		printInfo(master.ask("Hallo Du, wie geht es dir?"));
	}

	private static void printInfo(Map<String, Integer> themes) {
		for (String key : themes.keySet()) {
			System.out.println("Is " + themes.get(key) + "% >" + key + "<");
		}
		System.out.println();
	}

}
