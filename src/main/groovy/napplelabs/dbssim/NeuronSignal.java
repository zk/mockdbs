/*
	MockDBS: Deep Brain Stimulation Simulator
    Copyright (C) 2009 Zachary Kim

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along
    with this program; if not, write to the Free Software Foundation, Inc.,
    51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package napplelabs.dbssim;

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
	private boolean enabled = true;
	
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
		if(counter < spike.length && isEnabled()) {
			next = spike[counter];		
		}
		
		//else it's in the space range, output 0
		
		counter++;
		
		if(counter >= spike.length + space_len) {
			counter = 0;
			float diff = maxRate - minRate;
			float rnd = (float) (Math.random() * diff + minRate);
			space_len = (int) (44100 / rnd);
		}
		return next;
	}

	public void generate(float[] left, float[] right) {
		
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}

}
