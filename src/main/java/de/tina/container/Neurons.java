package de.tina.container;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

@Component
public class Neurons {
	@Value("${nerons.path}")
	private String neronsPath;

	private Neuron[] neurons;

	@PostConstruct
	public void init() {
		this.neurons = load();
	}

	private Neuron[] load() {
		try {
			Neurons fromJson = new Gson().fromJson(new String(FileUtils.readFileToByteArray(new File(neronsPath))),
					Neurons.class);
			return fromJson.neurons;
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new Neuron[0];
	}

	public void add(String word) {
		if (!contains(word)) {
			Neuron n = new Neuron();
			n.setId(UUID.randomUUID().toString());
			n.setInhalt(word.getBytes());
			ArrayUtils.add(neurons, n);
		}
	}

	public boolean contains(String word) {
		return ArrayUtils.contains(neurons, word);
	}

	public String[] getIds() {
		String[] ids = new String[neurons.length];
		for (int i = 0; i < neurons.length; i++) {
			ids[i] = neurons[i].getId();
		}
		return ids;
	}

	public void persist() {
		try {
			FileUtils.writeByteArrayToFile(new File(neronsPath), new Gson().toJson(this).getBytes(), false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
