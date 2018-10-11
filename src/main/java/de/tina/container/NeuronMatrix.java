package de.tina.container;

import java.util.Arrays;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.apache.commons.lang.ArrayUtils;

@Entity
public class NeuronMatrix {
    @Id
    private String name;

    private int max;

    private Long[] neuronIds;

    private int[][] adjiazenMatrix;

    public NeuronMatrix() {
        this.max = 0;
        this.name = "";
        this.neuronIds = new Long[0];
        this.adjiazenMatrix = new int[0][0];
    }

    /**
     * @param name
     */
    public NeuronMatrix(String name) {
        this.max = 0;
        this.name = name;
        this.neuronIds = new Long[0];
        this.adjiazenMatrix = new int[0][0];
    }

    /**
     * @param name
     * @param neuronIds
     */
    public NeuronMatrix(String name, Long[] neuronIds) {
        this.name = name;
        this.max = 0;
        this.neuronIds = neuronIds;
        this.adjiazenMatrix = new int[neuronIds.length][neuronIds.length];
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the neuronIds
     */
    public Long[] getNeuronIds() {
        return neuronIds;
    }

    /**
     * @return the adjiazenMatrix
     */
    public int[][] getAdjiazenMatrix() {
        return adjiazenMatrix;
    }

    /**
     * @return the max
     */
    public int getMax() {
        return max;
    }

    /**
     * Add a new word to the knowledgeBase
     * @param neuronId
     */
    public void add(long neuronId) {
        if (ArrayUtils.indexOf(neuronIds, neuronId) < 0) {
            neuronIds = (Long[]) ArrayUtils.add(neuronIds, neuronId);
            adjiazenMatrix = appentToMatrix(adjiazenMatrix);
        }
    }

    /**
     * Add the two new words to the knowledgeBase and generate a input into the matrix
     * @param word
     * @param follower
     */
    public void add(Long word, Long follower) {
        add(word);
        add(follower);
        int indexWord = ArrayUtils.indexOf(neuronIds, word);
        int indexFollower = ArrayUtils.indexOf(neuronIds, follower);
        if (indexWord >= 0 && indexFollower >= 0) {
            adjiazenMatrix[indexWord][indexFollower] -= 1;
        }
        this.max = maxOfMatrix();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.deepHashCode(adjiazenMatrix);
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + Arrays.hashCode(neuronIds);
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
        NeuronMatrix other = (NeuronMatrix) obj;
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
        if (!Arrays.equals(neuronIds, other.neuronIds)) {
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
        return "NeuronMatrix [name=" + name + ", neuronIds=" + Arrays.toString(neuronIds) + ", adjiazenMatrix=" + matrix + "]";
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
}
