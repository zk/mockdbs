package napplelabs.dbssim;

import ddf.minim.AudioOutput;
import ddf.minim.AudioSignal;


/**
 * Neuron signal provides a mechanism for pushing chunks of a neural
 * signal out to a float buffer.  It abstracts away the need to 
 * think about the specifics of the target buffer (length), and
 * concentrate on the neuronal signal.
 * 
 * Usage:
 * 	float[] signal = new float[signal_length]
 *  for(int i=0; i<signal_length; i++) {
 *  	// Fill signal
 *  }
 *  
 *  NeuronSignal ns = new NeuronSignal(signal);
 *  
 *  float[] first_chunk = new float[100]; //notice different array sizes
 *  float[] second_chunk = new float[39];
 *  
 *  ns.generate(first_chunk); // gets first chunk
 *  ns.generate(second_chunk); // gets second chunk
 *  
 *  //Send first and second chunk to output
 * @author zkim
 *
 */
public class NeuronSignal implements AudioSignal {
	
	
	private float[] spike;
	private int counter = 0;
	
	public NeuronSignal(float[] spike) {
		this.spike = spike;
	}
	
	public void generate(float[] signal) {
		for(int i=0; i < signal.length; i++) {
			signal[i] = spike[counter];
			
			counter++;
			if(counter >= spike.length) counter = 0;
		}
	}

	public void generate(float[] left, float[] right) {
		
	}

}
