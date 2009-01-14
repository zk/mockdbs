package napplelabs.dbssim;

import org.junit.Test;

import junit.framework.TestCase;

public class TestNeuronSignal extends TestCase {
	
	@Test
	public void testNeuronSignal() {
		double one = (float) (Math.PI / 100);
		float[] spike = new float[100];
		for(int i=0; i < 100; i++) {
			spike[i] = (float) Math.sin(one * i);
		}
		
		NeuronSignal ns = new NeuronSignal(spike);
		
		float[] out = new float[100];
		ns.generate(out);
		
		for(int i=0; i < out.length; i++) {
			assertEquals("dc spike[" + i + "]", spike[i], out[i]);
		}
		
		float[] pout_0 = new float[21];
		float[] pout_1 = new float[89];
		
		ns.generate(pout_0);
		ns.generate(pout_1);
		
		float[] pfinal = new float[110];
		for(int i=0; i < pout_0.length; i++) {
			pfinal[i] = pout_0[i];
		}
		
		for(int i=pout_0.length; i < pout_0.length + pout_1.length; i++) {
			pfinal[i] = pout_1[i-pout_0.length];
		}
		
		for(int i=0; i < pfinal.length-10; i++) {
			assertEquals("spike[" + i + "]", spike[i], pfinal[i]);
		}
		
		for(int i=0; i < 10; i++) {
			assertEquals(spike[i], pfinal[i+100]);
		}
		
		
	}
}
