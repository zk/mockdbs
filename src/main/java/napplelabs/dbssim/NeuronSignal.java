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
 *  //Then send first and second chunk to output
 * @author zkim
 *
 */
public class NeuronSignal implements AudioSignal {
	
	
	private float[] spike;
	private int counter = 0;
	private int space_len = 44100;
	//private int spike_cnt = 0;
	private int space_cnt = 0;
	private float minRate = 1;
	private float maxRate = 10;
	
	public NeuronSignal(float[] spike, float minRate, float maxRate) {
		this.spike = spike;
		this.minRate = minRate;
		this.maxRate = maxRate;
	}
	
	public void generate(float[] signal) {
		for(int i=0; i < signal.length; i++) {
			signal[i] = getNextValue(); 
		}
	}
	
	public float getNextValue() {
		float next = 0;
		
		//If counter is within spike range, output next spike value
		if(counter < spike.length) {
			next = spike[counter];		
		}
		
		//else it's in the space range, output 0
		
		counter++;
		
		if(counter >= spike.length + space_len) {
			counter = 0;
			float diff = maxRate - minRate;
			//System.out.println(diff);
			float rnd = (float) (Math.random() * diff + minRate);
			space_len = (int) (44100 / rnd);
			//System.out.println(space_len);
			//System.out.println();
		}
		return next;
	}

	public void generate(float[] left, float[] right) {
		
	}

}
