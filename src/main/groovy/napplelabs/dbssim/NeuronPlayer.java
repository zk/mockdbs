package napplelabs.dbssim;

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
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.DataLine.Info;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import processing.core.PApplet;

import ddf.minim.AudioOutput;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import ddf.minim.signals.PinkNoise;
import ddf.minim.signals.SineWave;

public class NeuronPlayer {
	
	private PinkNoise pink;
	private SineWave sin;
	private AudioOutput out;

	public NeuronPlayer() throws LineUnavailableException, InterruptedException {
		
		JFrame frame = new JFrame("");
		frame.setSize(500, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Minim minim = new Minim(new PApplet());
		out = minim.getLineOut(Minim.MONO);
		pink = new PinkNoise(0.2f);
		
		//out.addSignal(pink);
		
		
		
		
		File file = new File("/Users/zkim/Desktop/Rage Against The Machine - rage against the machine - 06 - Know Your Enemy.mp3");
		AudioPlayer player = minim.loadFile(file.getAbsolutePath());
		
		player.play();
		
		
		
		//TracePanel panel = new TracePanel(out);
		//frame.setContentPane(panel);
		
		frame.setVisible(true);
		
		//new Thread(panel).start();
	}
	
	public static void main(String[] args) throws LineUnavailableException, InterruptedException {
		new NeuronPlayer();
	}
}
