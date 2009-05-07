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

package napplelabs.dbssim.neuron;

import java.awt.Color;

/**
 * Lightweight data-holder for neuron type information, including name, media
 * path, and color. Meant to be used to capture neuron information for creation
 * of a NeuronPath to put on the canvas.
 * 
 * @author zkim
 * 
 */
public class NeuronType {
	private String name = "";
	private String mediaPath = "";
	private Color color = Color.WHITE;
	
	public NeuronType() {
		
	}
	
	public NeuronType(String name, String mediaPath, Color color) {
		this.name = name;
		this.mediaPath = mediaPath;
		this.color = color;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setMediaPath(String mediaPath) {
		this.mediaPath = mediaPath;
	}

	public String getMediaPath() {
		return mediaPath;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return color;
	}
}
