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
	 * @param vocabulary
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

	// appent matrix by one row and one cole
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

	/**
	 * 
	 * @param word
	 */
	public void add(String word) {
		if (ArrayUtils.indexOf(vocabulary, word) < 0) {
			vocabulary = (String[]) ArrayUtils.add(vocabulary, word);
			adjiazenMatrix = appentToMatrix(adjiazenMatrix);
		}
	}

	/**
	 * 
	 * @param word
	 * @param follower
	 */
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

	// the sum of all hit-fields of the matrix
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
		return "BrainAreaPojo [name=" + name + ", vocabulary=" + Arrays.toString(vocabulary) + ", adjiazenMatrix="
				+ matrix + "]";
	}

	/**
	 * saves this object as a JSON-File
	 * 
	 * @param jsonFile
	 */
	public void persist(File jsonFile) {
		// Here the json file must be written
		try {
			FileUtils.writeByteArrayToFile(jsonFile, new Gson().toJson(this).getBytes(), false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * loads a JSON-File
	 * 
	 * @param jsonFile
	 * @return the mapped JSON-File
	 */
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

	/**
	 * Generate a string for a print
	 * 
	 * @return a string for a print
	 */
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

	public void append(KnowledgeBase knowledgeBase) {
		max += knowledgeBase.getMax();
		adjiazenMatrix = appentMatrix(knowledgeBase, appendVocabulary(knowledgeBase.getVocabulary()));
	}

	private String[] appendVocabulary(String[] vocabulary) {
		String[] out = this.vocabulary;
		for (String word : vocabulary) {
			if (!ArrayUtils.contains(this.vocabulary, word)) {
				out = (String[]) ArrayUtils.add(out, word);
			}
		}
		return out;
	}

	private int[][] appentMatrix(KnowledgeBase newKnowledgeBase, String[] newVocabulary) {
		// Witch array is greater
		int newLength = newVocabulary.length;
		int[][] out = new int[newLength][newLength];
		int[][] newAdjiazenMatrix = newKnowledgeBase.getAdjiazenMatrix();

		for (int i = 0; i < newVocabulary.length; i++) {
			// Get i position of the word in each vocabulary
			int iOfNewVocabulary = ArrayUtils.indexOf(newKnowledgeBase.getVocabulary(), newVocabulary[i]);
			int iOfOldVocabulary = ArrayUtils.indexOf(vocabulary, newVocabulary[i]);

			for (int j = 0; j < newLength; j++) {
				// Get j position of the word in each vocabulary
				int jOfNewVocabulary = ArrayUtils.indexOf(newKnowledgeBase.getVocabulary(), newVocabulary[j]);
				int jOfOldVocabulary = ArrayUtils.indexOf(vocabulary, newVocabulary[j]);
				// Compare if there is a word in the old matrix
				int oldField = 0;
				if (iOfOldVocabulary >= 0 && jOfOldVocabulary >= 0) {
					oldField = adjiazenMatrix[iOfOldVocabulary][jOfOldVocabulary];
				}
				// Compare if there is a word in the new matrix
				int newField = 0;
				if (iOfNewVocabulary >= 0 && jOfNewVocabulary >= 0) {
					newField = newAdjiazenMatrix[iOfNewVocabulary][jOfNewVocabulary];
				}
				// now save it in the output matrix
				out[i][j] = oldField + newField;
			}
		}
		// After that we save the new vocabulary
		vocabulary = newVocabulary;
		return out;
	}
}
