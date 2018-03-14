package de.tina.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import de.tina.container.NeuronMatrix;

@RestController
public class Tina {
	@Autowired
	private Master master;

	@GetMapping("/learn")
	@ResponseBody
	public void learn(@RequestParam(name = "text", required = true) String text,
			@RequestParam(name = "theme", required = true) String theme) {
		master.learn(text, theme);
		master.persist();
	}

	@GetMapping("/ask")
	@ResponseBody
	public Map<String, Integer> ask(@RequestParam(name = "text", required = true) String text) {
		return master.ask(text);
	}
	
	@GetMapping("/get")
	@ResponseBody
	public NeuronMatrix get(@RequestParam(name = "theme", required = true) String theme) {
		return master.getKnowledgeBase(theme);
	}
}
