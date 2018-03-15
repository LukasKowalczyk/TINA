package de.tina.controller;

import java.util.Map;
import org.apache.commons.lang.StringUtils;
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

    @GetMapping("/getMatrix")
    @ResponseBody
    public NeuronMatrix getMatrix(@RequestParam(name = "theme", required = true) String theme) {
        return master.getNeuronMatrix(theme);
    }

    @GetMapping("/getContent")
    @ResponseBody
    public byte[] getContent(@RequestParam(name = "id", required = true) Long id) {
        return master.getContentOfNeuron(id);
    }

    @GetMapping("/printOnConsol")
    @ResponseBody
    public void printOnConsol() {
        Map<String, NeuronMatrix> knowledge = master.getKnowledge();
        knowledge.entrySet().forEach(s -> {
            System.out.println(s.getKey());
            System.out.println(print(s.getValue()));
        });
    }

    public String print(NeuronMatrix neuronMatrix) {

        String ajinmatrix = "";
        String header = StringUtils.repeat(" ", 8) + "|";
        for (Long n : neuronMatrix.getNeuronIds()) {
            header += StringUtils.leftPad(new String(getContent(n)), 8, " ") + "|";
        }
        header += "\n";
        for (int i = 0; i < neuronMatrix.getAdjiazenMatrix().length; i++) {
            String row = StringUtils.leftPad(new String(getContent(neuronMatrix.getNeuronIds()[i])), 8, " ") + "|";
            for (int j = 0; j < neuronMatrix.getAdjiazenMatrix().length; j++) {
                row += StringUtils.leftPad(String.valueOf(neuronMatrix.getAdjiazenMatrix()[i][j]), 8, " ") + "|";
            }
            row += "\n";
            ajinmatrix += row;
        }

        return header + ajinmatrix;
    }

}
