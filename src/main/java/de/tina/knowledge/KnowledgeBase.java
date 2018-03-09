package de.tina.knowledge;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import com.google.gson.Gson;

public class KnowledgeBase {

    private String name;

    private int max;

    private String[] vocabulary;

    private int[][] adjiazenMatrix;

    /**
     * @param theme
     */
    public KnowledgeBase(String name) {
        this.max = 0;
        this.name = name;
        this.vocabulary = new String[0];
        this.adjiazenMatrix = new int[0][0];
    }

    /**
     * @param theme
     * @param vocabulary2
     */
    public KnowledgeBase(String name, String[] vocabulary) {
        this.name = name;
        this.max = 0;
        this.vocabulary = vocabulary;
        this.adjiazenMatrix = new int[vocabulary.length][vocabulary.length];
    }

    /**
     * @return the brainAreaName
     */
    public String getName() {
        return name;
    }

    /**
     * @return the vocabulary
     */
    public String[] getVocabulary() {
        return vocabulary;
    }

    /**
     * @return the adjiazenMatrix
     */
    public int[][] getAdjiazenMatrix() {
        return adjiazenMatrix;
    }

    /**
     * @param adjiazenMatrix
     */
    private int[][] appentToMatrix(int[][] adjiazenMatrix) {
        int newLength = adjiazenMatrix.length + 1;
        int[][] newMatrix = new int[newLength][newLength];
        for (int i = 0; i < newLength; i++) {
            for (int j = 0; j < newLength; j++) {
                int wert = 0;
                if (i < adjiazenMatrix.length && j < adjiazenMatrix.length) {
                    wert = adjiazenMatrix[i][j];
                }
                newMatrix[i][j] = wert;
            }
        }
        return newMatrix;
    }

    public void add(String word) {
        if (ArrayUtils.indexOf(vocabulary, word) < 0) {
            vocabulary = ArrayUtils.add(vocabulary, word);
            adjiazenMatrix = appentToMatrix(adjiazenMatrix);
        }
    }

    public void add(String word, String follower) {
        add(word);
        add(follower);
        int indexWord = ArrayUtils.indexOf(vocabulary, word);
        int indexFollower = ArrayUtils.indexOf(vocabulary, follower);
        if (indexWord >= 0 && indexFollower >= 0) {
            adjiazenMatrix[indexWord][indexFollower] -= 1;
        }
        this.max = maxOfMatrix();
    }

    private int maxOfMatrix() {
        int countHits = 0;
        for (int i = 0; i < adjiazenMatrix.length; i++) {
            for (int j = 0; j < adjiazenMatrix[i].length; j++) {
                if (adjiazenMatrix[i][j] != 0) {
                    countHits += adjiazenMatrix[i][j];
                }
            }
        }
        return countHits;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.deepHashCode(adjiazenMatrix);
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + Arrays.hashCode(vocabulary);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        KnowledgeBase other = (KnowledgeBase) obj;
        if (!Arrays.deepEquals(adjiazenMatrix, other.adjiazenMatrix)) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (!Arrays.equals(vocabulary, other.vocabulary)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String matrix = "[";
        for (int[] is : adjiazenMatrix) {
            matrix += Arrays.toString(is) + ", ";
        }
        matrix += "]";
        return "BrainAreaPojo [name=" + name + ", vocabulary=" + Arrays.toString(vocabulary) + ", adjiazenMatrix=" + matrix
            + "]";
    }

    public void persist(File jsonFile) {
        // Here the json file must be written
        try {
            FileUtils.writeByteArrayToFile(jsonFile, new Gson().toJson(this).getBytes(), false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static KnowledgeBase load(File jsonFile) {
        // Here the json file must be written
        try {
            String inhalt = new String(FileUtils.readFileToByteArray(jsonFile));
            KnowledgeBase bareaPojo = new Gson().fromJson(inhalt, KnowledgeBase.class);
            return bareaPojo;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String print() {
        String ajinmatrix = "";
        String header = StringUtils.repeat(" ", 8) + "|";
        for (String n : vocabulary) {
            header += StringUtils.leftPad(n, 8, " ") + "|";
        }
        header += "\n";
        for (int i = 0; i < adjiazenMatrix.length; i++) {
            String row = StringUtils.leftPad(vocabulary[i], 8, " ") + "|";
            for (int j = 0; j < adjiazenMatrix.length; j++) {
                row += StringUtils.leftPad(String.valueOf(adjiazenMatrix[i][j]), 8, " ") + "|";
            }
            row += "\n";
            ajinmatrix += row;
        }

        return header + ajinmatrix;
    }

    /**
     * @return the max
     */
    public int getMax() {
        return max;
    }
}
