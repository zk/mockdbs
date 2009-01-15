package napplelabs.dbssim;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import ddf.minim.AudioListener;
import ddf.minim.AudioOutput;
import ddf.minim.AudioSignal;

public class TracePanel extends JPanel implements Runnable {
	
	private AudioOutput signal;
	
	private int counter = 0;
	private float[] trace = new float[200000];
	
	private boolean done = false;
	private boolean resizeFlag = false;

	public TracePanel(AudioOutput signal) {
		this.signal = signal;
		
		//setOpaque(true);
		//setBackground(Color.black);
		
		addComponentListener(new ComponentAdapter() {
			

			@Override
			public void componentResized(ComponentEvent e) {
				resizeFlag = true;
			}
		});
		
	}
	
	
	public void paint(Graphics _g) {
		
		Graphics2D g = (Graphics2D) _g;
		if(resizeFlag) {
			System.out.println("FILL BLACK");
			g.setColor(Color.black);
			g.fillRect(0, 0, getWidth(), getHeight());
			resizeFlag = false;
		}
		
		g.setColor(Color.WHITE);
		int leftSize = signal.left.size();
		
		float[] samples = signal.left.toArray();
		
		//counter = counter + samples.length;
		
		
		/*for(int i=0; i < samples.length; i++) {
			trace[counter] = samples[i];
			counter++;
			
			if(counter >= trace.length) counter = 0;
			//repaint();
			
		}*/
		
		/*for(int i=0; i < counter; i++) {
			
			double one = (double) getWidth() / (double) trace.length;
			double diff = i * one;//+ counter * one;
			
			g.drawLine((int) diff, (int) (getHeight()/2 + -trace[i]*getHeight()/2), (int) (diff + one), (int) (getHeight()/2 + -trace[i+1]*getHeight()/2));
		}*/
		
		for(int i = 0; i < samples.length-1; i++) {
			
			double one = (double) getWidth() / (double) trace.length;
			double diff = i * one + counter * one;
			
			trace[counter] = samples[i];
			
			
			
			g.drawLine((int) diff, (int) (getHeight()/2 + -samples[i]*getHeight()/2), (int) (diff + one), (int) (getHeight()/2 + -samples[i+1]*getHeight()/2));
			
			counter++;
			
			if(counter >= trace.length) {
				System.out.println("COUNTER = 0");
				resizeFlag = true;
				counter = 0;
			}
				
		}
		
		
	}


	public void setDone(boolean done) {
		this.done = done;
	}


	public boolean isDone() {
		return done;
	}
	
	public void run() {
		done = false;
		while(!isDone()) {
		SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					repaint();
				}
			});

			try {
				Thread.sleep(1000 / 100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void start() {
		new Thread(this).start();
	}
	
	public void stop() {
		this.done = true;
	}
}
