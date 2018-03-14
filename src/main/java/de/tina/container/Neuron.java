package de.tina.container;

import java.util.Arrays;

public class Neuron implements Comparable<String> {
	private String id;

	private byte[] inhalt;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public byte[] getInhalt() {
		return inhalt;
	}

	public void setInhalt(byte[] inhalt) {
		this.inhalt = inhalt;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (String.class != obj.getClass())
			return false;
		String other = (String) obj;
		if (!Arrays.equals(inhalt, other.getBytes()))
			return false;
		return true;
	}

	@Override
	public int compareTo(String o) {
		return new String(inhalt).compareTo(o);
	}

}
