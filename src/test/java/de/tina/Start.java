package de.tina;

import de.tina.apprentice.Apprentice;
import de.tina.master.Master;

public class Start {
	
	private static final String SOURCE_PATH = "D:\\";
	
	private static final boolean PRE_FILTER = false;
	
	private static final int SUCCES_QUOTA = 90;

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
		apprentice.finish();
		Master master = new Master(SOURCE_PATH, PRE_FILTER, SUCCES_QUOTA);
		master.ask("Hallo, ich kündige!");
		master.ask("Hallo ich, ich will kündigen !");
		master.ask("Hallo Du, wie geht es dir?");
	}

}
