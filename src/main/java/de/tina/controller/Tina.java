package de.tina.controller;

import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import de.tina.container.Neuron;
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

    @GetMapping("/getNeuron")
    @ResponseBody
    public Neuron getNeuron(@RequestParam(name = "id", required = true) Long id) {
        return master.getNeuron(id);
    }

    @GetMapping("/printOnConsol")
    @ResponseBody
    public String printOnConsol() {
        String output = "";
        Map<String, NeuronMatrix> knowledge = master.getKnowledge();
        for (Entry<String, NeuronMatrix> s : knowledge.entrySet()) {
            if (s != null) {
                output += "<h3>" + s.getKey() + "</h3><br>";
                output += "<table border=\"1\">";
                output += print(s.getValue());
                output += "</table>" + "<br>" + "<br>";
            }
        }

        return output;

    }

    public String print(NeuronMatrix neuronMatrix) {

        String header = "<tr><td></td>";
        for (Long n : neuronMatrix.getNeuronIds()) {
            Neuron neuron = getNeuron(n);
            header += "<th>" + StringUtils.leftPad(new String(neuron.getContent()), 8, " ") + "</th>";
        }
        header += "</tr>";

        String ajinmatrix = "";
        for (int i = 0; i < neuronMatrix.getAdjiazenMatrix().length; i++) {
            String row = "<tr><td>"
                + StringUtils.leftPad(new String(getNeuron(neuronMatrix.getNeuronIds()[i]).getContent()), 8, " ") + "</td>";
            for (int j = 0; j < neuronMatrix.getAdjiazenMatrix().length; j++) {
                row += "<td>" + StringUtils.leftPad(String.valueOf(neuronMatrix.getAdjiazenMatrix()[i][j]), 8, " ") + "</td>";
            }
            ;
            ajinmatrix += row + "</tr>";
        }

        return header + ajinmatrix;
    }

}
