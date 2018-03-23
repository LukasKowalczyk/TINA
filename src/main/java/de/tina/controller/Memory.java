package de.tina.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import de.tina.container.NeuronMatrix;
import de.tina.container.NeuronMatrixRepository;

@Component
public class Memory {

    @Autowired
    private NeuronMatrixRepository neuronMatrix;

    /**
     * Loads the knowledge of the sourcePath
     * @return a Map with the Name of the knowledgeBase as Key an the knowledgeBase as Value
     */
    public Map<String, NeuronMatrix> remember() {
        Map<String, NeuronMatrix> knowledge = new HashMap<>();
        // load every JSON-File
        for (NeuronMatrix neuronMatrix : this.neuronMatrix.findAll()) {

            knowledge.put(neuronMatrix.getName(), neuronMatrix);
        }
        return knowledge;
    }

    /**
     * Lets persist our knowledge into the sourcePath
     * @param knowledge
     * @param sourcePath
     */
    public void persist(Map<String, NeuronMatrix> knowledge) {
        // System.out.println("This is what i learned >>>>");
        Iterator<String> i = knowledge.keySet().iterator();
        while (i.hasNext()) {
            NeuronMatrix neuronMatrix = knowledge.get(i.next());
            this.neuronMatrix.save(neuronMatrix);
        }
    }

}
