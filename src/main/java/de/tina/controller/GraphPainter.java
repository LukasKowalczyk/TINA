package de.tina.controller;

import java.util.List;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.springframework.stereotype.Component;
import de.tina.container.Neuron;
import de.tina.container.NeuronMatrix;

/**
 * @author pd06286
 */
@Component
public class GraphPainter {
    protected String styleSheet = "node {" + "   fill-color: green;" + "}" + "node.marked {" + "   fill-color: red;" + "}";

    List<Neuron> allNeurons;

    public void paintGraph(NeuronMatrix matrix) {
        Graph graph = new SingleGraph(matrix.getName());
        graph.addAttribute("ui.stylesheet", styleSheet);
        int len = matrix.getNeuronIds().length - 1;
        System.out.println(len);
        for (Neuron neuron : allNeurons) {
            String label = new String(neuron.getContent());
            graph.addNode(Long.toString(neuron.getId())).addAttribute("ui.label", label);
        }
        int[][] mat = matrix.getAdjiazenMatrix();
        for (int y = 0; y <= len; y++) {
            for (int x = 0; x <= len; x++) {
                if (mat[y][x] < 0) {

                    System.out.println(matrix.getNeuronIds()[y]);
                    System.out.println(matrix.getNeuronIds()[x]);
                    String quell = new String(findNeuron(matrix.getNeuronIds()[y]).getContent());
                    String ziel = new String(findNeuron(matrix.getNeuronIds()[x]).getContent());
                    graph.addEdge(quell + ziel, quell, ziel).addAttribute("ui.label", mat[y][x]);
                }
            }
        }
        graph.display();
    }

    /**
     * @param allNeurons
     */
    public void setNodes(List<Neuron> allNeurons) {
        this.allNeurons = allNeurons;
    }

    private Neuron findNeuron(Long id) {
        for (Neuron neuron : allNeurons) {
            if (neuron.getId() == id) {
                return neuron;
            }
        }
        return null;
    }
}
