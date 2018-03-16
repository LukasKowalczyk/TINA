package de.tina.container;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface NeuronRepository extends CrudRepository<Neuron, Long> {

    // List<Neuron> findByContent(byte[] cotnent);

    List<Neuron> findByNeuronTypAndContent(NeuronTyp neurontyp, byte[] cotnent);

}
