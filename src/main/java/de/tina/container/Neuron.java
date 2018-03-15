package de.tina.container;

import java.util.Arrays;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Neuron implements Comparable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private byte[] content;

    /**
     * @param content
     */
    public Neuron(byte[] content) {
        this.content = content;
    }

    public Neuron(Long id, byte[] content) {
        this.id = id;
        this.content = content;
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (byte[].class != obj.getClass()) {
            return false;
        }
        byte[] other = (byte[]) obj;
        if (!Arrays.equals(content, other)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(String o) {
        return new String(content).compareTo(o);
    }

}
