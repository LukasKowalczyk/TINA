package de.tina.container;

import java.util.Arrays;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Neuron {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private NeuronTyp neuronTyp;

    private byte[] content;

    public Neuron() {
        this.content = new byte[0];
        neuronTyp = NeuronTyp.UNKOWN;
    }

    public Neuron(NeuronTyp neuronTyp, byte[] content) {
        this.content = content;
        this.neuronTyp = neuronTyp;
    }

    public Neuron(NeuronTyp neuronTyp, Long id, byte[] content) {
        this.id = id;
        this.content = content;
        this.neuronTyp = neuronTyp;
    }

    public Long getId() {
        return id;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public NeuronTyp getNeuronTyp() {
        return neuronTyp;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(content);
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((neuronTyp == null) ? 0 : neuronTyp.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
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
        Neuron other = (Neuron) obj;
        if (!Arrays.equals(content, other.content)) {
            return false;
        }
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        if (neuronTyp != other.neuronTyp) {
            return false;
        }
        return true;
    }

}
