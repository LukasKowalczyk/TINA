package de.tina;

import java.util.Map;
import de.tina.master.Master;

public class Start {

    private static final String SOURCE_PATH = System.getProperty("user.dir");

    private static final boolean PRE_FILTER = false;

    private static final int SUCCES_QUOTA = 80;

    /**
     * @param args
     */
    public static void main(String[] args) {
        Master master = new Master(SOURCE_PATH, PRE_FILTER, SUCCES_QUOTA);
        master.learn("Hallo wie geht es dir?", "Begrüßung");
        master.learn("Geht es dir gut?", "Begrüßung");
        master.learn("Hallo du, Geht es dir gut?", "Begrüßung");
        master.learn("Hi, wie geht's?", "Begrüßung");
        master.learn("Darf ich kündigen?", "Frage");
        master.learn("Muss ich kündigen?", "Frage");
        master.learn("Muss ich kündigen?", "Frage");
        master.learn("Darf ich kündigen?", "Kündigung");
        master.learn("Muss ich kündigen?", "Kündigung");
        master.learn("Muss ich kündigen?", "Kündigung");
        master.learn("Ich will kündigen.", "Kündigung");
        master.learn("Ich kündige!", "Kündigung");
        master.learn("Hallo, ich kündige.", "Kündigung");
        master.learn("Hallo, ich will kündigen!", "Kündigung");
        master.learn("Hallo, darf ich kündigen?", "Kündigung");
        master.finish();

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
