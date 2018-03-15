package de.tina.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import de.tina.container.NeuronMatrix;
import de.tina.container.NeuronRepository;

@Component
public class Master {

    @Value("${pre.filter}")
    private boolean preFilter;

    @Value("${succes.quota}")
    private int succesQuota;

    @Autowired
    private Analyser analyser;

    @Autowired
    private Memory memory;

    @Autowired
    private NeuronRepository neurons;

    private Map<String, NeuronMatrix> knowledge;

    @PostConstruct
    public void init() {
        this.knowledge = memory.remember();
    }

    /**
     * Ask the master what the text is.
     * @param text
     * @return a map of hits what kind of themes the text is with the percents of hit
     */
    public Map<String, Integer> ask(String text) {
        Map<String, Integer> themes = new HashMap<>();
        // Should the knowledgeBase be prefiltert?
        Collection<NeuronMatrix> knowledgeBases = knowledge.values();
        if (preFilter) {
            knowledgeBases = possibleThemeByVocabulary(analyser.getVocabulary(text));
        }
        // Now we must find our matching knowledgebase(s)
        if (!ArrayUtils.isEmpty(knowledgeBases.toArray())) {
            for (NeuronMatrix knowledgeBase : knowledgeBases) {
                // Create a new knowledgeBase out of the text but with a given
                // neurons
                NeuronMatrix newKnowledgeBase = analyser.fillTheKnowledgeBase(text,
                    new NeuronMatrix(knowledgeBase.getName(), knowledgeBase.getNeuronIds()));
                // Percent of the matching of the knowledgeBases
                int erg = compareMatrix(newKnowledgeBase.getAdjiazenMatrix(), knowledgeBase.getAdjiazenMatrix(),
                    newKnowledgeBase.getMax());
                // Only themes that are greater than 0%
                if (erg > 0) {
                    themes.put(knowledgeBase.getName(), erg);
                }
            }
        }
        if (themes.isEmpty()) {
            // System.out.println("I can't assign >" + text + "<");
            return new HashMap<String, Integer>();
        }
        return themes;
    }

    private int compareMatrix(int[][] matrixToProve, int[][] possibleMatrix, int maxHits) {
        int countHits = 0;
        for (int i = 0; i < possibleMatrix.length; i++) {
            for (int j = 0; j < possibleMatrix[i].length; j++) {
                // Only if in each array there is not a 0
                if (matrixToProve[i][j] != 0 && possibleMatrix[i][j] != 0) {
                    countHits += matrixToProve[i][j];
                }
            }
        }
        // Percent of hits
        return (int) ((countHits) / (double) maxHits * 100);
    }

    private List<NeuronMatrix> possibleThemeByVocabulary(Long[] vocabulary) {
        List<NeuronMatrix> out = new ArrayList<NeuronMatrix>();
        for (String key : knowledge.keySet()) {
            NeuronMatrix v = knowledge.get(key);
            int countFails = 0;
            for (int i = 0; i < vocabulary.length; i++) {
                Long word = vocabulary[i];
                // Counts every missmatch
                if (!ArrayUtils.contains(v.getNeuronIds(), word)) {
                    countFails++;
                }
            }
            int erg = (int) (countFails / (double) vocabulary.length * 100);
            int failQuota = 100 - succesQuota;
            // Only match percent is less then the fail Quota
            if (erg <= failQuota) {
                out.add(v);
            }
        }
        return out;
    }

    /**
     * Teach the apprentice that this text is this theme
     * @param text
     * @param theme
     */
    public void learn(String text, String theme) {
        // First of all we check if its a theme we already know
        if (!knowledge.containsKey(theme)) {
            knowledge.put(theme, new NeuronMatrix(theme));
        }
        // System.out.println("I learning that >" + text + "< is >" + theme +
        // "<!");
        NeuronMatrix knowledgeBase = analyser.fillTheKnowledgeBase(text, knowledge.get(theme));
        knowledge.put(theme, knowledgeBase);
    }

    public void persist() {
        memory.persist(knowledge);
        knowledge = memory.remember();
    }

    public NeuronMatrix getNeuronMatrix(String theme) {
        return knowledge.get(theme);
    }

    /**
     * @param id
     * @return
     */
    public byte[] getContentOfNeuron(Long id) {
        return neurons.findOne(id).getContent();
    }

    /**
     * @return
     */
    public Map<String, NeuronMatrix> getKnowledge() {
        return knowledge;
    }
}
