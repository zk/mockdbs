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

package napplelabs.dbssim.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import napplelabs.dbssim.neuron.NeuronType;
import processing.core.PApplet;

import com.explodingpixels.macwidgets.MacColorUtils;
import com.explodingpixels.painter.FocusStatePainter;
import com.explodingpixels.painter.GradientPainter;
import com.explodingpixels.painter.Painter;
import com.explodingpixels.swingx.EPPanel;
import com.google.common.collect.Lists;

import ddf.minim.AudioOutput;
import ddf.minim.Minim;
import ddf.minim.signals.PinkNoise;

/**
 * Responsible for initializing the program and creating the main UI frame.
 * 
 * 
 * Where: a = Top Bar (see MacFrame) b = CanvasPanel c = Control Panel d =
 * Bottom Bar (see MacFrame)
 */
public class SimUI {

	private MacFrame mf;
	private Component currentComponent = new JPanel();
	private Component tracePanel = new JPanel();

	// Fakes the signal interference found during DBS surgery.
	PinkNoise pink;

	public SimUI() {
		mf = new MacFrame(1200, 800);

		// Top bar setup

		// Bottom bar setup
		JLabel depthLabel = new JLabel("0.00 mm");
		depthLabel.setFont(new Font("Arial", Font.PLAIN, 30));
		mf.addBottombarComponentCenter(depthLabel);

		EPPanel noisePanel = createNoisePanel();
		mf.addBottombarComponentRight(noisePanel);

		// Create neuron types
		List<NeuronType> neuronTypes = Lists.newArrayList(
				new NeuronType(
						"Thalamus",
						"./neuron_media_files/thalamus.wav",
						Color.blue
						),
				new NeuronType(
						"STN",
						"./neuron_media_files/stn.wav",
						Color.red
						),
				new NeuronType(
						"SNr",
						"./neuron_media_files/snr.wav",
						Color.green
						)
		);
		
		Minim minim = new Minim(new PApplet());
		CanvasPanel canvasPanel = new CanvasPanel(neuronTypes, minim, depthLabel);
		mf.getContent().add(canvasPanel, BorderLayout.CENTER);

		double one = (float) (1.1 * Math.PI / 100);
		int sin_size = 100;
		float[] sin = new float[sin_size];
		for (int i = 0; i < sin_size; i++) {
			sin[i] = (float) Math.sin(one * i);
		}

		AudioOutput out = minim.getLineOut(Minim.MONO);
		pink = new PinkNoise(0.0f);
		out.addSignal(pink);

		mf.getFrame().setVisible(true);
	}

	private EPPanel createNoisePanel() {
		EPPanel noisePanel = new EPPanel();
		noisePanel.setLayout(new BorderLayout());
		Painter<Component> focusedPainter = new GradientPainter(
				MacColorUtils.OS_X_BOTTOM_BAR_ACTIVE_TOP_COLOR,
				MacColorUtils.OS_X_BOTTOM_BAR_ACTIVE_BOTTOM_COLOR);
		Painter<Component> unfocusedPainter = new GradientPainter(
				MacColorUtils.OS_X_BOTTOM_BAR_INACTIVE_TOP_COLOR,
				MacColorUtils.OS_X_BOTTOM_BAR_INACTIVE_BOTTOM_COLOR);

		Painter<Component> painter = new FocusStatePainter(focusedPainter,
				focusedPainter, unfocusedPainter);
		noisePanel.setBackgroundPainter(painter);

		final JSlider noiseSlider = new JSlider();
		noiseSlider.setMinimum(0);
		noiseSlider.setMaximum(100);
		noiseSlider.setValue(0);
		noiseSlider.addChangeListener(new ChangeListener() {

			public void stateChanged(final ChangeEvent e) {
				float val = noiseSlider.getValue();
				val /= 100;
				pink.setAmp(val);
			}

		});

		noisePanel.add(noiseSlider, BorderLayout.CENTER);
		noisePanel.add(new JLabel("Noise"), BorderLayout.WEST);

		return noisePanel;
	}

	public static void main(final String[] args) {
		new SimUI();
	}

}
